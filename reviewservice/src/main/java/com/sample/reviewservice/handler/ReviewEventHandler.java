package com.sample.reviewservice.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.reviewservice.domain.Review;
import com.sample.reviewservice.repository.search.ReviewSearchRepository;
import com.sample.reviewservice.service.dto.ReviewDTO;
import com.sample.reviewservice.service.mapper.ReviewMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReviewEventHandler {

    private final Logger log = LoggerFactory.getLogger(ReviewEventHandler.class);

    private final ObjectMapper mapper;
    private final ReviewMapper reviewMapper;
    private final ReviewSearchRepository reviewSearchRepository;

    public ReviewEventHandler(ObjectMapper mapper, ReviewMapper reviewMapper, ReviewSearchRepository reviewSearchRepository) {
        this.mapper = mapper;
        this.reviewMapper = reviewMapper;
        this.reviewSearchRepository = reviewSearchRepository;
    }

    @KafkaListener(topics = "${REVIEW_EVENTS_TOPIC}", groupId = "${REVIEW_EVENTS_GROUP}")
    public void handle(@Payload String eventMessage) {
        try {
            ReviewDTO reviewDTO = mapper.readValue(eventMessage, ReviewDTO.class);
            Review review = reviewMapper.toEntity(reviewDTO);
            reviewSearchRepository.save(review);
        } catch(Exception e) {
            log.error("Error while saving to elastic search", e);
        }
    }
}
