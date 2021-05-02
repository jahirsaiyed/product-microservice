package com.sample.productservice.utils;

import com.sample.productservice.domain.Product;
import com.sample.productservice.service.dto.ProductDTO;

public class ProductUtils {

    public static Product copy(Product product) {
        Product p = new Product();
        p.setId(product.getId());
        p.setName(product.getName());
        p.setBrand(product.getBrand());
        p.setActive(product.getActive());
        return p;
    }

    public static ProductDTO copy(ProductDTO product) {
        ProductDTO p = new ProductDTO();
        p.setId(product.getId());
        p.setName(product.getName());
        p.setBrand(product.getBrand());
        p.setReview(product.getReview());
        return p;
    }
}
