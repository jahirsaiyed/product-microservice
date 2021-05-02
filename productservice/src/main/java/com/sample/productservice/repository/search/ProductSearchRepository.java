package com.sample.productservice.repository.search;

import com.sample.productservice.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductSearchRepository extends ElasticsearchRepository<Product, Long> {
    Page<Product> findByActive(Pageable pageable);
    Optional<Product> findByIdAndActive(Long id, Boolean active);
}
