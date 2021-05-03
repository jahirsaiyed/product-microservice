package com.sample.reviewservice.service;

import com.sample.reviewservice.service.dto.ReviewDTO;
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
    private String reviewEventTopic;
    @InjectMocks
    private MessagingService messagingService;

//    @Test
//    public void send() {
//        ReviewDTO reviewDTO = new ReviewDTO();
//        reviewDTO.setId(1l);
//        reviewDTO.setReviewScore(3f);
//        reviewDTO.setReviewCount(30l);
//        reviewDTO.setActive(true);
//        messagingService.send(reviewDTO);
//        Mockito.verify(kafkaTemplate).send(new ProducerRecord<String, String>(reviewEventTopic, "{\"id\" : 2,\"reviewScore\" : 3.0,\"reviewCount\" : 30}"));
//    }
}
