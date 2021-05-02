package com.sample.productservice.client;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReviewDTO implements Serializable {
    private Long id;
    private Long productId;
    private Float reviewScore;
    private Long reviewCount;
}
