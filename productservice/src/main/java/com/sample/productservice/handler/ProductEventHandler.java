package com.sample.productservice.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.productservice.domain.Product;
import com.sample.productservice.repository.search.ProductSearchRepository;
import com.sample.productservice.service.dto.ProductDTO;
import com.sample.productservice.service.mapper.ProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductEventHandler {

    private final Logger log = LoggerFactory.getLogger(ProductEventHandler.class);

    private final ObjectMapper mapper;
    private final ProductMapper productMapper;
    private final ProductSearchRepository productSearchRepository;

    public ProductEventHandler(ObjectMapper mapper, ProductMapper productMapper, ProductSearchRepository productSearchRepository) {
        this.mapper = mapper;
        this.productMapper = productMapper;
        this.productSearchRepository = productSearchRepository;
    }

    @KafkaListener(topics = "${PRODUCT_EVENTS_TOPIC}", groupId = "${PRODUCT_EVENTS_GROUP}")
    public void handle(@Payload String eventMessage) {
        try {
            ProductDTO productDTO = mapper.readValue(eventMessage, ProductDTO.class);
            Product product = productMapper.toEntity(productDTO);
            productSearchRepository.save(product);
        } catch(Exception e) {
            log.error("Error while saving to elastic search", e);
        }
    }
}
