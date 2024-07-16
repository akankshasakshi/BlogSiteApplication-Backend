package com.blogsite.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class KafKaConsumerServiceTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private KafKaConsumerService consumerService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testConsume1() {
        String message = "Test message for registration";
        consumerService.consume1(message);
        verify(kafkaTemplate, times(0)).send(anyString(), anyString());
        // Optionally verify the behavior of consume1 method
    }

    @Test
    public void testConsume2() {
        String message = "Test message for login";
        consumerService.consume2(message);
        verify(kafkaTemplate, times(0)).send(anyString(), anyString());
        // Optionally verify the behavior of consume2 method
    }

    @Test
    public void testConsume3() {
        String message = "Test message for registration failure";
        consumerService.consume3(message);
        verify(kafkaTemplate, times(0)).send(anyString(), anyString());
        // Optionally verify the behavior of consume3 method
    }
}
