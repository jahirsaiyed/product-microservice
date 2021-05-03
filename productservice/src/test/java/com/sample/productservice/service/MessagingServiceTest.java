package com.sample.productservice.service;

import com.sample.productservice.service.dto.ProductDTO;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

@ExtendWith(MockitoExtension.class)
public class MessagingServiceTest {

    @Mock
    private KafkaTemplate kafkaTemplate;
    @Mock
    private String productEventTopic;
    @InjectMocks
    private MessagingService messagingService;

//    @Test
//    public void send() {
//        ProductDTO productDTO = new ProductDTO();
//        productDTO.setId(1l);
//        productDTO.setBrand("brand1");
//        productDTO.setName("name1");
//        productDTO.setActive(true);
//        messagingService.send(productDTO);
//        Mockito.verify(kafkaTemplate).send(new ProducerRecord<String, String>(productEventTopic, "{\"id\":1, \"name\":\"name1\", \"brand\":\"brand1\",\"active\":true}"));
//    }

}
