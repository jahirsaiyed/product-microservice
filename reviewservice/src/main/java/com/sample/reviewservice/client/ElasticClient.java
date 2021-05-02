package com.sample.reviewservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "elastic", url = "${elasticsearch.httpUrls}")
public interface ElasticClient {
    @PutMapping("/review-idx")
    void createIndex(@RequestBody Map index);
}
