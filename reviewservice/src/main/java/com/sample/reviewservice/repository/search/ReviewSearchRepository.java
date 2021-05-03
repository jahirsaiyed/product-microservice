package com.sample.reviewservice.repository.search;

import com.sample.reviewservice.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewSearchRepository extends ElasticsearchRepository<Review, Long> {
    Page<Review> findByActive(Pageable pageable);
    Optional<Review> findByIdAndActive(Long id, Boolean active);
}
