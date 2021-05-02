package com.sample.productservice.service;

import com.sample.productservice.domain.Product;
import com.sample.productservice.repository.ProductRepository;
import com.sample.productservice.repository.search.ProductSearchRepository;
import com.sample.productservice.service.dto.ProductDTO;
import com.sample.productservice.service.impl.ProductServiceImpl;
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
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Spy
    private ProductMapperImpl productMapper;
    @Mock
    private ProductSearchRepository productSearchRepository;
    @Mock
    private MessagingService messagingService;
    @InjectMocks
    private ProductServiceImpl productService;
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
    void createProduct() {
        when(productRepository.save(Mockito.any())).then(returnsFirstArg());
        ProductDTO save = productService.save(productDTO);
        verify(productRepository).save(product);
        verify(messagingService).send(productDTO);
        Assertions.assertEquals(productDTO, save);
    }

    @Test
    void partialUpdate() {
        when(productRepository.findById(1l)).thenReturn(Optional.of(product));
        when(productRepository.save(Mockito.any())).then(returnsFirstArg());
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1l);
        productDTO.setBrand("Brand1");
        ProductDTO save = productService.partialUpdate(productDTO).get();
        verify(productRepository).save(product);
        ProductDTO productDTOMessageSent = ProductUtils.copy(this.productDTO);
        productDTOMessageSent.setId(productDTO.getId());
        productDTOMessageSent.setBrand(productDTO.getBrand());
        verify(messagingService).send(productDTOMessageSent);
        Assertions.assertNotEquals(this.productDTO, save);
        save.setBrand(productDTO.getBrand());
        Assertions.assertEquals(productDTOMessageSent, save);
    }

    @Test
    void findAll() {
        when(productSearchRepository.findByActive(any()))
                .thenReturn(new PageImpl<>(Arrays.asList(new Product()), PageRequest.of(0,1), 1));
        Page<ProductDTO> productDTOPage = productService.findAll(PageRequest.of(1, 1));
        Assertions.assertEquals(1, productDTOPage.getTotalElements());
    }

    @Test
    void findAllEmpty() {
        when(productSearchRepository.findByActive(any())).
                thenReturn(new PageImpl<>(Collections.emptyList(), PageRequest.of(0,1), 0));
        Page<ProductDTO> productDTOPage = productService.findAll(PageRequest.of(1, 1));
        Assertions.assertEquals(0, productDTOPage.getTotalElements());
    }

    @Test
    void findAllNextPage() {
        when(productSearchRepository.findByActive(any()))
                .thenReturn(new PageImpl<>(Collections.emptyList(), PageRequest.of(1,10), 3));
        Page<ProductDTO> productDTOPage = productService.findAll(PageRequest.of(1, 10));
        Assertions.assertEquals(3, productDTOPage.getTotalElements());
    }

    @Test
    void findOne() {
        when(productSearchRepository.findByIdAndActive(anyLong(), anyBoolean()))
                .thenReturn(Optional.of(product));
        Optional<ProductDTO> responseDTO = productService.findOne(1l);
        Assertions.assertEquals(productDTO, responseDTO.get());
    }

    @Test
    void delete() {
        when(productSearchRepository.findById(anyLong())).thenReturn(Optional.of(product));
        productService.delete(1l);
        ProductDTO deletedProductMessage = ProductUtils.copy(productDTO);
        deletedProductMessage.setActive(false);
        verify(messagingService).send(deletedProductMessage);
        Assertions.assertNotEquals(deletedProductMessage, productDTO);
    }

}
