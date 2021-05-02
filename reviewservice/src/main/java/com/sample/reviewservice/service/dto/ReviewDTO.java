package com.sample.reviewservice.service.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReviewDTO implements Serializable {
    private Long id;
    private Long productId;
    private Float reviewScore;
    private Long reviewCount;
}
