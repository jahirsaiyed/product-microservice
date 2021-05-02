package com.sample.productservice.service.mapper;

import com.sample.productservice.domain.Product;
import com.sample.productservice.service.dto.ProductDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
}
