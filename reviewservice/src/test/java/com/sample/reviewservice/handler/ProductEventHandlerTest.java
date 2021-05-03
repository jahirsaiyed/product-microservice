package com.sample.reviewservice.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.reviewservice.domain.Review;
import com.sample.reviewservice.repository.search.ReviewSearchRepository;
import com.sample.reviewservice.service.dto.ReviewDTO;
import com.sample.reviewservice.service.mapper.ReviewMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
public class ProductEventHandlerTest {
    @Spy
    private ObjectMapper mapper;
    @Spy
    private ReviewMapperImpl reviewMapper;
    @Mock
    private ReviewSearchRepository reviewSearchRepository;
    @InjectMocks
    private ReviewEventHandler reviewEventHandler;
    private Review review;
    private ReviewDTO reviewDTO;

    @BeforeEach
    void init() {
        review = new Review();
        review.setId(1l);
        review.setProductId(1l);
        review.setReviewCount(30l);
        review.setReviewScore(3f);
        reviewDTO = new ReviewDTO();
        reviewDTO.setId(1l);
        reviewDTO.setProductId(1l);
        reviewDTO.setReviewCount(30l);
        reviewDTO.setReviewScore(3f);
    }

    @Test
    void handle() {
        String event = "{\"id\":1, \"reviewCount\":31, \"reviewScore\":4.0,\"active\":true}";
        reviewEventHandler.handle(event);
        Review review = new Review();
        review.setId(1l);
        review.setReviewCount(31l);
        review.setReviewScore(4f);
        review.setActive(true);
        verify(reviewSearchRepository).save(review);
    }

    @Test
    void handleDeActivate() {
        String event = "{\"id\":1, \"reviewCount\":31, \"reviewScore\":4.0,\"active\":false}";
        reviewEventHandler.handle(event);
        Review review = new Review();
        review.setId(1l);
        review.setReviewCount(31l);
        review.setReviewScore(4f);
        review.setActive(false);
        verify(reviewSearchRepository).save(review);
    }

    @Test
    void handleMalformedMessageDoesNotPropagateExceptionToKafka() {
        String event = "{\"jumbledstring\" : \"efege\"}";
        reviewEventHandler.handle(event);
        verifyNoInteractions(reviewSearchRepository);
    }
}
