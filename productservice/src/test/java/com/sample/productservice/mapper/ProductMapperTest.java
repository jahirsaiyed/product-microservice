package com.sample.productservice.mapper;

import com.sample.productservice.domain.Product;
import com.sample.productservice.service.dto.ProductDTO;
import com.sample.productservice.service.mapper.ProductMapperImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ProductMapperTest {
    @InjectMocks
    private ProductMapperImpl productMapper;

    @Test
    void mapNullProduct() {
        Product product = null;
        ProductDTO productDTO = productMapper.toDto(product);
        Assertions.assertNull(productDTO);
    }

    @Test
    void mapNullProductDTO() {
        ProductDTO product = null;
        Product productDTO = productMapper.toEntity(product);
        Assertions.assertNull(productDTO);
    }

    @Test
    void mapNullProducts() {
        List<Product> product = null;
        List<ProductDTO> productDTO = productMapper.toDto(product);
        Assertions.assertNull(productDTO);

        product = new ArrayList<>(); product.add(null);
        productDTO = productMapper.toDto(product);
        productDTO.stream().forEach(p -> Assertions.assertNull(p));
    }

    @Test
    void mapNullProductDTOs() {
        List<ProductDTO> product = null;
        List<Product> productDTO = productMapper.toEntity(product);
        Assertions.assertNull(productDTO);

        product = new ArrayList<>(); product.add(null);
        productDTO = productMapper.toEntity(product);
        productDTO.stream().forEach(p -> Assertions.assertNull(p));
    }

    @Test
    void productToProductDTOWithNulls() {
        Product product = new Product();
        ProductDTO productDTO = productMapper.toDto(product);
        assertEquals(product, productDTO);
    }

    @Test
    void productToProductDTOWithNonNulls() {
        Product product = new Product();
        product.setId(1l);
        product.setName("Name");
        product.setBrand("Brand");
        product.setActive(true);
        ProductDTO productDTO = productMapper.toDto(product);
        assertEquals(product, productDTO);
    }

    @Test
    void productsToProductDTOWithNulls() {
        Product product = new Product();
        List<ProductDTO> productDTO = productMapper.toDto(Arrays.asList(product));
        productDTO.stream().forEach(p -> assertEquals(product, p));
    }

    @Test
    void productsToProductDTOWithNonNulls() {
        Product product = new Product();
        product.setId(1l);
        product.setName("Name");
        product.setBrand("Brand");
        product.setActive(true);
        List<ProductDTO> productDTO = productMapper.toDto(Arrays.asList(product));
        productDTO.stream().forEach(p -> assertEquals(product, p));
    }

    @Test
    void productDTOToProductWithNulls() {
        ProductDTO product = new ProductDTO();
        Product productDTO = productMapper.toEntity(product);
        assertEquals(productDTO, product);
    }

    @Test
    void productDTOToProductWithNonNulls() {
        ProductDTO product = new ProductDTO();
        product.setId(1l);
        product.setName("Name");
        product.setBrand("Brand");
        product.setActive(true);
        Product productDTO = productMapper.toEntity(product);
        assertEquals(productDTO, product);
    }

    @Test
    void productDTOsToProductWithNulls() {
        ProductDTO product = new ProductDTO();
        List<Product> productDTO = productMapper.toEntity(Arrays.asList(product));
        productDTO.stream().forEach(p -> assertEquals(p, product));
    }

    @Test
    void productDTOsToProductWithNonNulls() {
        ProductDTO product = new ProductDTO();
        product.setId(1l);
        product.setName("Name");
        product.setBrand("Brand");
        product.setActive(true);
        List<Product> productDTO = productMapper.toEntity(Arrays.asList(product));
        productDTO.stream().forEach(p -> assertEquals(p, product));
    }

    @Test
    void partialUpdateWithNulls() {
        ProductDTO product = new ProductDTO();
        Product product1 = new Product();
        productMapper.partialUpdate(product1, product);
        assertEquals(product1, product);
    }

    @Test
    void partialUpdateWithNonNulls() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Name1");
        Product product = new Product();
        product.setId(1l);
        product.setBrand("Brand");
        product.setName("Name");
        product.setActive(false);
        productMapper.partialUpdate(product, productDTO);
        Assertions.assertEquals(product.getName(), productDTO.getName());
        Assertions.assertNotEquals(product.getId(), productDTO.getId());
        Assertions.assertNotEquals(product.getBrand(), productDTO.getBrand());
        Assertions.assertNotEquals(product.getActive(), productDTO.getActive());

        productDTO.setName("Name1");
        productDTO.setBrand("Brand1");
        productDTO.setActive(true);
        product.setId(1l);
        product.setBrand("Brand");
        product.setName("Name");
        productDTO.setId(product.getId());
        productMapper.partialUpdate(product, productDTO);
        assertEquals(product, productDTO);
    }

    private void assertEquals(Product product, ProductDTO productDTO) {
        Assertions.assertEquals(product.getId(), productDTO.getId());
        Assertions.assertEquals(product.getName(), productDTO.getName());
        Assertions.assertEquals(product.getBrand(), productDTO.getBrand());
        Assertions.assertEquals(product.getActive(), productDTO.getActive());
    }


}
