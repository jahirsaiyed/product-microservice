package com.sample.reviewservice.utils;

import com.sample.reviewservice.domain.Review;
import com.sample.reviewservice.service.dto.ReviewDTO;

public class ReviewUtils {

    public static Review copy(Review review) {
        Review review1 = new Review();
        review1.setId(review.getId());
        review1.setProductId(review.getProductId());
        review1.setReviewScore(review.getReviewScore());
        review1.setReviewCount(review.getReviewCount());
        return review1;
    }

    public static ReviewDTO copy(ReviewDTO reviewDTO) {
        ReviewDTO reviewDTO1 = new ReviewDTO();
        reviewDTO1.setId(reviewDTO.getId());
        reviewDTO1.setProductId(reviewDTO.getProductId());
        reviewDTO1.setReviewScore(reviewDTO.getReviewScore());
        reviewDTO1.setReviewCount(reviewDTO.getReviewCount());
        return reviewDTO1;
    }
}
