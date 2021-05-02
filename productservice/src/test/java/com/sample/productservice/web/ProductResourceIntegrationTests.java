package com.sample.productservice.web;

import com.sample.productservice.ProductserviceApplication;
import com.sample.productservice.domain.Product;
import com.sample.productservice.repository.ProductRepository;
import com.sample.productservice.service.dto.ProductDTO;
import com.sample.productservice.service.mapper.ProductMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = ProductserviceApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProductResourceIntegrationTests {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Test
    public void testCreateProduct() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setBrand("Brand");
        productDTO.setName("Name");
        ResponseEntity<ProductDTO> responseEntity = this.restTemplate
                .postForEntity("http://localhost:" + port + "/api/products", productDTO, ProductDTO.class);
        assertEquals(201, responseEntity.getStatusCodeValue());
        ProductDTO dto = responseEntity.getBody();
        Optional<Product> optionalProduct = productRepository.findById(dto.getId());
        Assertions.assertEquals(dto, productMapper.toDto(optionalProduct.get()));
    }
}