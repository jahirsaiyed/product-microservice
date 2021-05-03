package com.sample.reviewservice.domain;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "review")
@Data
@Document(indexName = "review-idx")
public class Review implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "average_review_score")
    private Float reviewScore;

    @Column(name = "review_count")
    private Long reviewCount;

    @Column(name = "active")
    private Boolean active;

}
