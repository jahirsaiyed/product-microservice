package com.sample.reviewservice.service;

import com.sample.reviewservice.domain.Review;
import com.sample.reviewservice.repository.ReviewRepository;
import com.sample.reviewservice.repository.search.ReviewSearchRepository;
import com.sample.reviewservice.service.dto.ReviewDTO;
import com.sample.reviewservice.service.impl.ReviewServiceImpl;
import com.sample.reviewservice.service.mapper.ReviewMapperImpl;
import com.sample.reviewservice.utils.ReviewUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {
    @Mock
    private ReviewRepository reviewRepository;
    @Spy
    private ReviewMapperImpl reviewMapper;
    @Mock
    private ReviewSearchRepository reviewSearchRepository;
    @Mock
    private MessagingService messagingService;
    @InjectMocks
    private ReviewServiceImpl reviewService;
    private Review review;
    private ReviewDTO reviewDTO;

    @BeforeEach
    void init() {
        review = new Review();
        review.setId(1l);
        review.setProductId(1l);
        review.setReviewCount(30l);
        review.setReviewScore(4f);
        reviewDTO = new ReviewDTO();
        reviewDTO.setId(1l);
        reviewDTO.setProductId(1l);
        reviewDTO.setReviewCount(30l);
        reviewDTO.setReviewScore(4f);
    }

    @Test
    void createReview() {
        when(reviewRepository.save(Mockito.any())).then(returnsFirstArg());
        ReviewDTO save = reviewService.save(reviewDTO);
        verify(reviewRepository).save(review);
        verify(messagingService).send(reviewDTO);
        Assertions.assertEquals(reviewDTO, save);
    }

    @Test
    void partialUpdate() {
        when(reviewRepository.findById(1l)).thenReturn(Optional.of(review));
        when(reviewRepository.save(Mockito.any())).then(returnsFirstArg());
        ReviewDTO productDTO = new ReviewDTO();
        productDTO.setId(1l);
        productDTO.setReviewCount(31l);
        ReviewDTO save = reviewService.partialUpdate(productDTO).get();
        verify(reviewRepository).save(review);
        ReviewDTO productDTOMessageSent = ReviewUtils.copy(this.reviewDTO);
        productDTOMessageSent.setId(productDTO.getId());
        productDTOMessageSent.setReviewCount(productDTO.getReviewCount());
        verify(messagingService).send(productDTOMessageSent);
        Assertions.assertNotEquals(this.reviewDTO, save);
        save.setReviewCount(productDTO.getReviewCount());
        Assertions.assertEquals(productDTOMessageSent, save);
    }

    @Test
    void findAll() {
        when(reviewSearchRepository.findByActive(any()))
                .thenReturn(new PageImpl<>(Arrays.asList(new Review()), PageRequest.of(0,1), 1));
        Page<ReviewDTO> productDTOPage = reviewService.findAll(PageRequest.of(1, 1));
        Assertions.assertEquals(1, productDTOPage.getTotalElements());
    }

    @Test
    void findAllEmpty() {
        when(reviewSearchRepository.findByActive(any())).
                thenReturn(new PageImpl<>(Collections.emptyList(), PageRequest.of(0,1), 0));
        Page<ReviewDTO> productDTOPage = reviewService.findAll(PageRequest.of(1, 1));
        Assertions.assertEquals(0, productDTOPage.getTotalElements());
    }

    @Test
    void findAllNextPage() {
        when(reviewSearchRepository.findByActive(any()))
                .thenReturn(new PageImpl<>(Collections.emptyList(), PageRequest.of(1,10), 3));
        Page<ReviewDTO> productDTOPage = reviewService.findAll(PageRequest.of(1, 10));
        Assertions.assertEquals(3, productDTOPage.getTotalElements());
    }

    @Test
    void findOne() {
        when(reviewSearchRepository.findByIdAndActive(anyLong(), anyBoolean()))
                .thenReturn(Optional.of(review));
        Optional<ReviewDTO> responseDTO = reviewService.findOne(1l);
        Assertions.assertEquals(reviewDTO, responseDTO.get());
    }

    @Test
    void delete() {
        when(reviewSearchRepository.findById(anyLong())).thenReturn(Optional.of(review));
        reviewService.delete(1l);
        ReviewDTO deletedReviewMessage = ReviewUtils.copy(reviewDTO);
        deletedReviewMessage.setActive(false);
        verify(messagingService).send(deletedReviewMessage);
        Assertions.assertNotEquals(deletedReviewMessage, reviewDTO);
    }

}
