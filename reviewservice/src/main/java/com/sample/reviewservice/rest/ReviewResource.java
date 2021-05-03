package com.sample.reviewservice.rest;

import com.sample.reviewservice.repository.ReviewRepository;
import com.sample.reviewservice.rest.util.ResponseUtil;
import com.sample.reviewservice.service.ReviewService;
import com.sample.reviewservice.service.dto.ReviewDTO;
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

@RestController
@RequestMapping("/api")
public class ReviewResource {

    private final Logger log = LoggerFactory.getLogger(ReviewResource.class);

    private final ReviewService reviewService;

    private final ReviewRepository reviewRepository;

    public ReviewResource(ReviewService reviewService, ReviewRepository reviewRepository) {
        this.reviewService = reviewService;
        this.reviewRepository = reviewRepository;
    }

    /**
     * {@code POST  /reviews} : Create a new review.
     *
     * @param reviewDTO the reviewDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reviewDTO, or with status {@code 400 (Bad Request)} if the review has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/reviews")
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO reviewDTO) throws URISyntaxException {
        log.debug("REST request to save Review : {}", reviewDTO);
        if (reviewDTO.getId() != null) {
            throw new IllegalArgumentException("A new review cannot already have an ID");
        }
        ReviewDTO result = reviewService.save(reviewDTO);
        return ResponseEntity
            .created(new URI("/api/reviews/" + result.getId()))
//            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /reviews/:id} : Updates an existing review.
     *
     * @param id the id of the reviewDTO to save.
     * @param reviewDTO the reviewDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reviewDTO,
     * or with status {@code 400 (Bad Request)} if the reviewDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reviewDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/reviews/{id}")
    public ResponseEntity<ReviewDTO> updateReview(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReviewDTO reviewDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Review : {}, {}", id, reviewDTO);
        if (reviewDTO.getId() == null) {
            throw new IllegalArgumentException("Invalid id");
        }
        if (!Objects.equals(id, reviewDTO.getId())) {
            throw new IllegalArgumentException("Invalid ID");
        }

        if (!reviewRepository.existsById(id)) {
            throw new IllegalArgumentException("Entity not found");
        }

        ReviewDTO result = reviewService.save(reviewDTO);
        return ResponseEntity
            .ok()
            .body(result);
    }

    /**
     * {@code PATCH  /reviews/:id} : Partial updates given fields of an existing review, field will ignore if it is null
     *
     * @param id the id of the reviewDTO to save.
     * @param reviewDTO the reviewDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reviewDTO,
     * or with status {@code 400 (Bad Request)} if the reviewDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reviewDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reviewDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/reviews/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ReviewDTO> partialUpdateReview(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReviewDTO reviewDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Review partially : {}, {}", id, reviewDTO);
        if (reviewDTO.getId() == null) {
            throw new IllegalArgumentException("Invalid id");
        }
        if (!Objects.equals(id, reviewDTO.getId())) {
            throw new IllegalArgumentException("Invalid ID");
        }

        if (!reviewRepository.existsById(id)) {
            throw new IllegalArgumentException("Entity not found");
        }

        return ResponseUtil.wrapOrNotFound(reviewService.partialUpdate(reviewDTO));
    }

    /**
     * {@code GET  /reviews} : get all the reviews.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reviews in body.
     */
    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewDTO>> getAllReviews(Pageable pageable) {
        log.debug("REST request to get a page of Reviews");
        Page<ReviewDTO> page = reviewService.findAll(pageable);
        return ResponseEntity.ok().body(page.getContent());
    }

    /**
     * {@code GET  /reviews/:id} : get the "id" review.
     *
     * @param productId the id of the reviewDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reviewDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/reviews/{productId}")
    public ResponseEntity<ReviewDTO> getReview(@PathVariable Long productId) {
        log.debug("REST request to get Review : {}", productId);
        Optional<ReviewDTO> reviewDTO = reviewService.findByProductId(productId);
        return ResponseUtil.wrapOrNotFound(reviewDTO);
    }

//    public ResponseEntity<ReviewDTO> getReviewFallback(@PathVariable Long id) {
//        return new ResponseEntity<ReviewDTO>(new ReviewDTO(), HttpStatus.OK);
//    }

//    /**
//     * {@code GET  /reviews/:id} : get the "id" review.
//     *
//     * @param id the id of the reviewDTO to retrieve.
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reviewDTO, or with status {@code 404 (Not Found)}.
//     */
//    @GetMapping("/reviews/product/{id}")
//    public ResponseEntity<ReviewDTO> getReviewByproduct(@PathVariable Long id) {
//        log.debug("REST request to get Review : {}", id);
//        Optional<ReviewDTO> reviewDTO = reviewService.findOne(id);
//        return ResponseUtil.wrapOrNotFound(reviewDTO);
//    }

    /**
     * {@code DELETE  /reviews/:id} : delete the "id" review.
     *
     * @param id the id of the reviewDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        log.debug("REST request to delete Review : {}", id);
        reviewService.delete(id);
        return ResponseEntity
            .noContent()
            .build();
    }

    /**
     * {@code SEARCH  /_search/reviews?query=:query} : search for the review corresponding
     * to the query.
     *
     * @param query the query of the review search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
   /* @GetMapping("/_search/reviews")
    public ResponseEntity<List<ReviewDTO>> searchReviews(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Reviews for query {}", query);
        Page<ReviewDTO> page = reviewService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }*/
}
