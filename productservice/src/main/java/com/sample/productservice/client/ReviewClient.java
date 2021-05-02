package com.sample.productservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "reviewservice")
public interface ReviewClient {
    @GetMapping("/api/reviews/{id}")
    ReviewDTO getReviews(@PathVariable Long id);
}
