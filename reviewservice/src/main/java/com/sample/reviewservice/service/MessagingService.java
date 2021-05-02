package com.sample.reviewservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.reviewservice.service.dto.ReviewDTO;
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
    @Value("${REVIEW_EVENTS_TOPIC}")
    private String reviewEventTopic;

    @Autowired
    public MessagingService(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(ReviewDTO reviewDTO) {
        try {
            kafkaTemplate.send(new ProducerRecord<>(reviewEventTopic, objectMapper.writeValueAsString(reviewDTO)));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid reviewDTO");
        }
    }

}
