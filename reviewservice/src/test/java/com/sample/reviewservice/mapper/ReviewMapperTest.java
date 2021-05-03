package com.sample.reviewservice.mapper;

import com.sample.reviewservice.domain.Review;
import com.sample.reviewservice.service.dto.ReviewDTO;
import com.sample.reviewservice.service.mapper.ReviewMapperImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ReviewMapperTest {
    @InjectMocks
    private ReviewMapperImpl reviewMapper;

    @Test
    void mapNullReview() {
        Review review = null;
        ReviewDTO reviewDTO = reviewMapper.toDto(review);
        Assertions.assertNull(reviewDTO);
    }

    @Test
    void mapNullReviewDTO() {
        ReviewDTO review = null;
        Review reviewDTO = reviewMapper.toEntity(review);
        Assertions.assertNull(reviewDTO);
    }

    @Test
    void mapNullReviews() {
        List<Review> review = null;
        List<ReviewDTO> reviewDTO = reviewMapper.toDto(review);
        Assertions.assertNull(reviewDTO);

        review = new ArrayList<>(); review.add(null);
        reviewDTO = reviewMapper.toDto(review);
        reviewDTO.stream().forEach(p -> Assertions.assertNull(p));
    }

    @Test
    void mapNullReviewDTOs() {
        List<ReviewDTO> review = null;
        List<Review> reviewDTO = reviewMapper.toEntity(review);
        Assertions.assertNull(reviewDTO);

        review = new ArrayList<>(); review.add(null);
        reviewDTO = reviewMapper.toEntity(review);
        reviewDTO.stream().forEach(p -> Assertions.assertNull(p));
    }

    @Test
    void reviewToReviewDTOWithNulls() {
        Review review = new Review();
        ReviewDTO reviewDTO = reviewMapper.toDto(review);
        assertEquals(review, reviewDTO);
    }

    @Test
    void reviewToReviewDTOWithNonNulls() {
        Review review = new Review();
        review.setId(1l);
        review.setReviewCount(30l);
        review.setReviewScore(3f);
        review.setActive(true);
        ReviewDTO reviewDTO = reviewMapper.toDto(review);
        assertEquals(review, reviewDTO);
    }

    @Test
    void reviewsToReviewDTOWithNulls() {
        Review review = new Review();
        List<ReviewDTO> reviewDTO = reviewMapper.toDto(Arrays.asList(review));
        reviewDTO.stream().forEach(p -> assertEquals(review, p));
    }

    @Test
    void reviewsToReviewDTOWithNonNulls() {
        Review review = new Review();
        review.setId(1l);
        review.setReviewCount(30l);
        review.setReviewScore(3f);
        review.setActive(true);
        List<ReviewDTO> reviewDTO = reviewMapper.toDto(Arrays.asList(review));
        reviewDTO.stream().forEach(p -> assertEquals(review, p));
    }

    @Test
    void reviewDTOToReviewWithNulls() {
        ReviewDTO review = new ReviewDTO();
        Review reviewDTO = reviewMapper.toEntity(review);
        assertEquals(reviewDTO, review);
    }

    @Test
    void reviewDTOToReviewWithNonNulls() {
        ReviewDTO review = new ReviewDTO();
        review.setId(1l);
        review.setReviewCount(30l);
        review.setReviewScore(3f);
        review.setActive(true);
        Review reviewDTO = reviewMapper.toEntity(review);
        assertEquals(reviewDTO, review);
    }

    @Test
    void reviewDTOsToReviewWithNulls() {
        ReviewDTO review = new ReviewDTO();
        List<Review> reviewDTO = reviewMapper.toEntity(Arrays.asList(review));
        reviewDTO.stream().forEach(p -> assertEquals(p, review));
    }

    @Test
    void reviewDTOsToReviewWithNonNulls() {
        ReviewDTO review = new ReviewDTO();
        review.setId(1l);
        review.setReviewCount(30l);
        review.setReviewScore(3f);
        review.setActive(true);
        List<Review> reviewDTO = reviewMapper.toEntity(Arrays.asList(review));
        reviewDTO.stream().forEach(p -> assertEquals(p, review));
    }

    @Test
    void partialUpdateWithNulls() {
        ReviewDTO review = new ReviewDTO();
        Review review1 = new Review();
        reviewMapper.partialUpdate(review1, review);
        assertEquals(review1, review);
    }

    @Test
    void partialUpdateWithNonNulls() {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setReviewCount(31l);
        Review review = new Review();
        review.setId(1l);
        review.setReviewScore(3f);
        review.setReviewCount(30l);
        review.setActive(false);
        reviewMapper.partialUpdate(review, reviewDTO);
        Assertions.assertEquals(review.getReviewCount(), reviewDTO.getReviewCount());
        Assertions.assertNotEquals(review.getId(), reviewDTO.getId());
        Assertions.assertNotEquals(review.getReviewScore(), reviewDTO.getReviewScore());
        Assertions.assertNotEquals(review.getActive(), reviewDTO.getActive());

        reviewDTO.setReviewCount(31l);
        reviewDTO.setReviewScore(4f);
        reviewDTO.setActive(true);
        review.setId(1l);
        review.setReviewScore(3f);
        review.setReviewCount(30l);
        reviewDTO.setId(review.getId());
        reviewMapper.partialUpdate(review, reviewDTO);
        assertEquals(review, reviewDTO);
    }

    private void assertEquals(Review review, ReviewDTO reviewDTO) {
        Assertions.assertEquals(review.getId(), reviewDTO.getId());
        Assertions.assertEquals(review.getReviewCount(), reviewDTO.getReviewCount());
        Assertions.assertEquals(review.getReviewScore(), reviewDTO.getReviewScore());
        Assertions.assertEquals(review.getActive(), reviewDTO.getActive());
    }


}
