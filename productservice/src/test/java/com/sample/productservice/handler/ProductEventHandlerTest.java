package com.sample.productservice.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.productservice.domain.Product;
import com.sample.productservice.repository.ProductRepository;
import com.sample.productservice.repository.search.ProductSearchRepository;
import com.sample.productservice.service.MessagingService;
import com.sample.productservice.service.dto.ProductDTO;
import com.sample.productservice.service.impl.ProductServiceImpl;
import com.sample.productservice.service.mapper.ProductMapper;
import com.sample.productservice.service.mapper.ProductMapperImpl;
import com.sample.productservice.utils.ProductUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductEventHandlerTest {
    @Spy
    private ObjectMapper mapper;
    @Spy
    private ProductMapperImpl productMapper;
    @Mock
    private ProductSearchRepository productSearchRepository;
    @InjectMocks
    private ProductEventHandler productEventHandler;
    private Product product;
    private ProductDTO productDTO;

    @BeforeEach
    void init() {
        product = new Product();
        product.setId(1l);
        product.setBrand("Brand");
        product.setName("Name");
        productDTO = new ProductDTO();
        productDTO.setBrand("Brand");
        productDTO.setName("Name");
        productDTO.setId(1l);
    }

    @Test
    void handle() {
        String event = "{\"id\":1, \"name\":\"name1\", \"brand\":\"brand1\",\"active\":true}";
        productEventHandler.handle(event);
        Product product = new Product();
        product.setId(1l);
        product.setName("name1");
        product.setBrand("brand1");
        product.setActive(true);
        verify(productSearchRepository).save(product);
    }

    @Test
    void handleDeActivate() {
        String event = "{\"id\":1, \"name\":\"name1\", \"brand\":\"brand1\",\"active\":false}";
        productEventHandler.handle(event);
        Product product = new Product();
        product.setId(1l);
        product.setName("name1");
        product.setBrand("brand1");
        product.setActive(false);
        verify(productSearchRepository).save(product);
    }

    @Test
    void handleMalformedMessageDoesNotPropagateExceptionToKafka() {
        String event = "{\"id\":1, \"name\":\"name1\", \"brand\":\"brand1\",\"active\":";
        productEventHandler.handle(event);
        verifyNoInteractions(productSearchRepository);
    }
}
