package com.sample.reviewservice.service.mapper;

import com.sample.reviewservice.domain.Review;
import com.sample.reviewservice.service.dto.ReviewDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewMapper extends EntityMapper<ReviewDTO, Review> {
}
