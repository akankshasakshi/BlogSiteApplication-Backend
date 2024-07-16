package com.blogsite.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.ArrayList;
import java.util.List;

import org.axonframework.queryhandling.QueryGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.blogsite.entity.Blogs;
import com.blogsite.repository.BlogRepository;
import com.blogsite.service.KafKaProducerService;

@WebMvcTest(BlogSearchController.class)
public class BlogSearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlogRepository blogRepository;

    @MockBean
    private KafKaProducerService kafkaProducerService;

    @MockBean
    private QueryGateway queryGateway;

    @InjectMocks
    private BlogSearchController blogSearchController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(blogSearchController).build();
    }

    @Test
    public void testSearchByCategory_Found() throws Exception {
        List<Blogs> blogs = new ArrayList<>();
        Blogs blog = new Blogs();
        blog.setBlogname("Test Blog");
        blog.setCategory("Tech");
        blogs.add(blog);

        when(blogRepository.findAllByCategory(anyString())).thenReturn(blogs);

        mockMvc.perform(get("/blogsite/blogs/info/{category}", "Tech")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'blogname':'Test Blog', 'category':'Tech'}]"));
    }

    @Test
    public void testSearchByCategory_NotFound() throws Exception {
        when(blogRepository.findAllByCategory(anyString())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/blogsite/blogs/info/{category}", "NonExistentCategory")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("No Blogs Found"));
    }
}
