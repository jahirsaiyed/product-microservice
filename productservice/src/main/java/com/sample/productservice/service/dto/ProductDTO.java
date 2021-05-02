package com.sample.productservice.service.dto;

import com.sample.productservice.client.ReviewDTO;
import com.wordnik.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO implements Serializable {
    private Long id;
    private String name;
    private String brand;
    @ApiModelProperty(hidden = true)
    private ReviewDTO review;
    private Boolean active;
}
