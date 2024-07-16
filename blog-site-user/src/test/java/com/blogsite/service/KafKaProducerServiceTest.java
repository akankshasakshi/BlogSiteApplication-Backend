package com.blogsite.service;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.SettableListenableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class KafKaProducerServiceTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private KafKaProducerService producerService;

    @Captor
    private ArgumentCaptor<String> topicCaptor;

    @Captor
    private ArgumentCaptor<String> messageCaptor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUserActions_Success() {
        // Mocking the KafkaTemplate send method to return a successful future
        when(kafkaTemplate.send(any(String.class), any(String.class)))
                .thenReturn(new SettableListenableFuture<>());

        // Call the method under test
        producerService.userActions("Test message", "testTopic");

        // Verify that kafkaTemplate.send was called with correct arguments
        verify(kafkaTemplate).send(topicCaptor.capture(), messageCaptor.capture());

        // Assert that the captured arguments match expected values
        assertEquals("testTopic", topicCaptor.getValue());
        assertEquals("Test message", messageCaptor.getValue());
    }
}
