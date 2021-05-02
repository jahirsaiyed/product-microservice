package com.sample.productservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "elastic", url = "${spring.cloud.elasticsearch.rest.uris}")
public interface ElasticClient {
    @PutMapping("/products-master-idx")
    void createIndex(@RequestBody Map index);
}
