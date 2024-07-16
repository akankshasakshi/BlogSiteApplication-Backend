package com.blogsite.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import com.blogsite.entity.Blogs;
import com.blogsite.entity.MongoBlogs;

public class KafKaProducerServiceTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private KafKaProducerService kafKaProducerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddDelBlog() {
        Blogs blog = new Blogs();
        blog.setBlogname("Test Blog");

        kafKaProducerService.addDelBlog(blog, "test-topic");

        verify(kafkaTemplate).send(anyString(), any(Blogs.class));
    }

    @Test
    public void testFindBlogs() {
        List<MongoBlogs> blogs = List.of(new MongoBlogs(), new MongoBlogs());

        kafKaProducerService.findBlogs(blogs, "test-topic");

        verify(kafkaTemplate).send(anyString(), any(List.class));
    }

    @Test
    public void testSearchBlogs() {
        List<MongoBlogs> blogs = List.of(new MongoBlogs(), new MongoBlogs());

        kafKaProducerService.searchBlogs(blogs, "test-topic");

        verify(kafkaTemplate).send(anyString(), any(List.class));
    }
}
