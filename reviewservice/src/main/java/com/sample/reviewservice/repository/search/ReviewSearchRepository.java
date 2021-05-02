package com.sample.reviewservice.repository.search;

import com.sample.reviewservice.domain.Review;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewSearchRepository extends ElasticsearchRepository<Review, Long> {}
