package com.sample.productservice.service.impl;

import com.sample.productservice.domain.Product;
import com.sample.productservice.repository.ProductRepository;
import com.sample.productservice.repository.search.ProductSearchRepository;
import com.sample.productservice.service.MessagingService;
import com.sample.productservice.service.ProductService;
import com.sample.productservice.service.dto.ProductDTO;
import com.sample.productservice.service.mapper.ProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final ProductSearchRepository productSearchRepository;

    private final MessagingService messagingService;

    public ProductServiceImpl(
            ProductRepository productRepository,
            ProductMapper productMapper,
            ProductSearchRepository productSearchRepository,
            MessagingService messagingService) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.productSearchRepository = productSearchRepository;
        this.messagingService = messagingService;
    }

    @Override
    public ProductDTO save(ProductDTO productDTO) {
        log.debug("Request to save Product : {}", productDTO);
        Product product = productMapper.toEntity(productDTO);
        product = productRepository.save(product);
        ProductDTO result = productMapper.toDto(product);
        messagingService.send(result);
        return result;
    }

    @Override
    public Optional<ProductDTO> partialUpdate(ProductDTO productDTO) {
        log.debug("Request to partially update Product : {}", productDTO);

        return productRepository
                .findById(productDTO.getId())
                .map(existingProduct -> {
                    productMapper.partialUpdate(existingProduct, productDTO);
                    return existingProduct;
                })
                .map(product -> {
                    product = productRepository.save(product);
                    messagingService.send(productMapper.toDto(product));
                    return product;
                })
                .map(productMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Products");
        return productSearchRepository.findByActive(pageable).map(productMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductDTO> findOne(Long id) {
        log.debug("Request to get Product : {}", id);
        return productSearchRepository.findByIdAndActive(id, true).map(productMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Product : {}", id);
        productRepository.deleteById(id);
        Optional<Product> product = productSearchRepository.findById(id);
        product.ifPresent(p -> {
            p.setActive(false);
            messagingService.send(productMapper.toDto(p));
        });
    }

//    @Override
//    @Transactional(readOnly = true)
//    public Page<ProductDTO> search(String query, Pageable pageable) {
//        log.debug("Request to search for a page of Products for query {}", query);
//        return productSearchRepository.search(queryStringQuery(query), pageable).map(productMapper::toDto);
//    }

}
