package com.sample.productservice.web.rest;

import com.sample.productservice.client.ReviewClient;
import com.sample.productservice.client.ReviewDTO;
import com.sample.productservice.repository.ProductRepository;
import com.sample.productservice.service.ProductService;
import com.sample.productservice.service.dto.ProductDTO;
import com.sample.productservice.web.rest.util.ResponseUtil;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.decorators.Decorators;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductResource {

    private final Logger log = LoggerFactory.getLogger(ProductResource.class);

    private final ProductService productService;
    private final ProductRepository productRepository;
    private final CircuitBreakerConfig circuitBreakerConfig;
    private final ReviewClient reviewClient;
//    private final RestTemplate restTemplate;

    /**
     * {@code POST  /products} : Create a new product.
     *
     * @param productDTO the productDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productDTO, or with status {@code 400 (Bad Request)} if the product has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/products")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) throws URISyntaxException {
        log.debug("REST request to save Product : {}", productDTO);
        if (productDTO.getId() != null) {
            throw new IllegalArgumentException("A new product cannot already have an ID");
        }
        ProductDTO result = productService.save(productDTO);
        return ResponseEntity
            .created(new URI("/api/products/" + result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /products/:id} : Updates an existing product.
     *
     * @param id the id of the productDTO to save.
     * @param productDTO the productDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productDTO,
     * or with status {@code 400 (Bad Request)} if the productDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductDTO productDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Product : {}, {}", id, productDTO);
        if (productDTO.getId() == null) {
            throw new IllegalArgumentException("Invalid id");
        }
        if (!Objects.equals(id, productDTO.getId())) {
            throw new IllegalArgumentException("Invalid ID");
        }

        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Entity not found");
        }

        ProductDTO result = productService.save(productDTO);
        return ResponseEntity
            .ok()
            .body(result);
    }

    /**
     * {@code PATCH  /products/:id} : Partial updates given fields of an existing product, field will ignore if it is null
     *
     * @param id the id of the productDTO to save.
     * @param productDTO the productDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productDTO,
     * or with status {@code 400 (Bad Request)} if the productDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/products/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ProductDTO> partialUpdateProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductDTO productDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Product partially : {}, {}", id, productDTO);
        if (productDTO.getId() == null) {
            throw new IllegalArgumentException("Invalid id");
        }
        if (!Objects.equals(id, productDTO.getId())) {
            throw new IllegalArgumentException("Invalid ID");
        }

        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Entity not found");
        }

        return ResponseUtil.wrapOrNotFound(productService.partialUpdate(productDTO));
    }

    /**
     * {@code GET  /products} : get all the products.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of products in body.
     */
    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getAllProducts(Pageable pageable) {
        log.debug("REST request to get a page of Products");
        Page<ProductDTO> page = productService.findAll(pageable);
        return ResponseEntity.ok().body(page.getContent());
    }

    /**
     * {@code GET  /products/:id} : get the "id" product.
     *
     * @param id the id of the productDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        log.debug("REST request to get Product : {}", id);
        Optional<ProductDTO> productDTO = productService.findOne(id);

        io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreaker = io.github.resilience4j.circuitbreaker.CircuitBreaker
                .of("reviewservice", circuitBreakerConfig);
        Supplier<ReviewDTO> supplier = () -> getReview(id);
        Supplier<ReviewDTO> decoratedSupplier = Decorators.ofSupplier(supplier)
                .withCircuitBreaker(circuitBreaker)
                .decorate();
        ReviewDTO review = Try.ofSupplier(decoratedSupplier)
                .recover(throwable -> getReviewFallback(id)).get();
        productDTO.map(product -> {
            product.setReview(review);
            return product;
        });
        return ResponseUtil.wrapOrNotFound(productDTO);
    }

    public ReviewDTO getReview(Long id) {
//        return restTemplate.getForEntity("http://localhost:6017/api/reviews/"+id, ReviewDTO.class).getBody();
        return reviewClient.getReviews(id);
    }

    public ReviewDTO getReviewFallback(Long id) {
        return new ReviewDTO();
    }

    /**
     * {@code DELETE  /products/:id} : delete the "id" product.
     *
     * @param id the id of the productDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.debug("REST request to delete Product : {}", id);
        productService.delete(id);
        return ResponseEntity
            .noContent()
            .build();
    }

    /**
     * {@code SEARCH  /_search/products?query=:query} : search for the product corresponding
     * to the query.
     *
     * @param query the query of the product search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
   /* @GetMapping("/_search/products")
    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Products for query {}", query);
        Page<ProductDTO> page = productService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }*/
}
