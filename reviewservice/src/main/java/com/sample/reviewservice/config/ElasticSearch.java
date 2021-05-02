package com.sample.reviewservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.reviewservice.client.ElasticClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Map;

@Component
public class ElasticSearch {

    private final Logger log = LoggerFactory.getLogger(ElasticSearch.class);

    @Autowired
    ObjectMapper objectMapper;
    @Value("${elasticsearch.httpUrls}")
    private String elasticSearchUrls;


    @Autowired
    private ElasticClient elasticClient;

    @PostConstruct
    public void initialLoad() {
        new Thread(() -> {
            try {
                Thread.sleep(10000);
                addReviews();
            } catch (Exception e) {}
        }).start();
    }

    private void addReviews() {
        createIndex();
    }

    private void createIndex() {
        try {
            File indexDefinition = new ClassPathResource("review-idx.json").getFile();
            Map index = objectMapper.readValue(indexDefinition, Map.class);
            elasticClient.createIndex(index);
        } catch (Exception e) {
            log.error("Index already present..", e);
        }
    }
}
