package com.sample.productservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.productservice.domain.Product;
import com.sample.productservice.service.dto.ProductDTO;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessagingService {
    private final KafkaTemplate kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;
    @Value("${PRODUCT_EVENTS_TOPIC}")
    private String productEventTopic;

    @Autowired
    public MessagingService(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(ProductDTO product) {
        try {
            kafkaTemplate.send(new ProducerRecord<>(productEventTopic, objectMapper.writeValueAsString(product)));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid product");
        }
    }

}
