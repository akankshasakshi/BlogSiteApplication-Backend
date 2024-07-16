package com.blogsite.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;

import com.blogsite.common.AppConstants;
import com.blogsite.entity.Blogs;
import com.blogsite.entity.MongoBlogs;
import com.blogsite.repository.MongoBlogRepository;

public class KafKaConsumerServiceTest {

    @Mock
    private MongoBlogRepository mongoRepo;

    @InjectMocks
    private KafKaConsumerService kafKaConsumerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testConsumeAddBlog() {
        Blogs blogs = new Blogs();
        blogs.setBlogname("Test Blog");

        MongoBlogs mongoBlogs = new MongoBlogs();
        BeanUtils.copyProperties(blogs, mongoBlogs);

        kafKaConsumerService.consume1(blogs);

        verify(mongoRepo).save(any(MongoBlogs.class));
    }

    @Test
    public void testConsumeDeleteBlog() {
        Blogs blogs = new Blogs();
        blogs.setBlogname("Test Blog");

        MongoBlogs mongoBlogs = new MongoBlogs();
        mongoBlogs.setBlogname("Test Blog");

        when(mongoRepo.findByBlogname(blogs.getBlogname())).thenReturn(Optional.of(mongoBlogs));

        kafKaConsumerService.consume2(blogs);

        verify(mongoRepo).delete(any(MongoBlogs.class));
    }

    @Test
    public void testConsumeFindAllBlogs() {
        List<Blogs> blogs = List.of(new Blogs(), new Blogs());

        kafKaConsumerService.consume3(blogs);

        // No database interaction, just logging
    }

    @Test
    public void testConsumeFindMyBlogs() {
        List<Blogs> blogs = List.of(new Blogs(), new Blogs());

        kafKaConsumerService.consume4(blogs);

        // No database interaction, just logging
    }

    @Test
    public void testConsumeSearchBlogByCategory() {
        List<Blogs> blogs = List.of(new Blogs(), new Blogs());

        kafKaConsumerService.consume5(blogs);

        // No database interaction, just logging
    }

    @Test
    public void testConsumeSearchBlogByCategoryAndRange() {
        List<Blogs> blogs = List.of(new Blogs(), new Blogs());

        kafKaConsumerService.consume6(blogs);

        // No database interaction, just logging
    }
}
