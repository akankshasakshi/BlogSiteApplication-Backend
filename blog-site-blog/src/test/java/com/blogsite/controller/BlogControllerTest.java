package com.blogsite.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.blogsite.Utils.JwtUtils;
import com.blogsite.commands.CreateBlogCommand;
import com.blogsite.entity.Blogs;
import com.blogsite.query.DeleteBlogQuery;
import com.blogsite.repository.BlogRepository;
import com.blogsite.service.KafKaProducerService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(BlogController.class)
public class BlogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlogRepository blogRepository;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private KafKaProducerService kafkaProducerService;

    @MockBean
    private CommandGateway commandGateway;

    @MockBean
    private QueryGateway queryGateway;

    @InjectMocks
    private BlogController blogController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(blogController).build();
    }

    @Test
    public void testAddBlog_Success() throws Exception {
        Blogs blog = new Blogs();
        blog.setBlogname("Test Blog");
        blog.setAuthorname("Test Author");
        blog.setArticle("Test Article");
        blog.setCategory("Test Category");

        String token = "validToken";

        when(jwtUtils.validateJwtToken(token)).thenReturn(true);
        when(jwtUtils.getUserIdFromJwtToken(token)).thenReturn("userId");
        when(blogRepository.findByBlogname(blog.getBlogname())).thenReturn(Optional.empty());
        when(commandGateway.sendAndWait(any(CreateBlogCommand.class))).thenReturn(null);

        mockMvc.perform(post("/blogsite/user/blogs/add")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(blog)))
                .andExpect(status().isOk());
    }


    @Test
    public void testDeleteBlog_Success() throws Exception {
        Blogs blog = new Blogs();
        blog.setBlogname("Test Blog");
        blog.setUserid("userId");

        String token = "validToken";

        when(jwtUtils.validateJwtToken(token)).thenReturn(true);
        when(jwtUtils.getUserIdFromJwtToken(token)).thenReturn("userId");
        when(blogRepository.findByBlognameAndUserid(blog.getBlogname(), blog.getUserid())).thenReturn(Optional.of(blog));

        mockMvc.perform(delete("/blogsite/user/blogs/delete/{blogName}", blog.getBlogname())
                .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteBlog_BlogNotFound() throws Exception {
        String blogName = "Test Blog";
        String token = "validToken";

        when(jwtUtils.validateJwtToken(token)).thenReturn(true);
        when(jwtUtils.getUserIdFromJwtToken(token)).thenReturn("userId");
        when(blogRepository.findByBlognameAndUserid(blogName, "userId")).thenReturn(Optional.empty());

        mockMvc.perform(delete("/blogsite/user/blogs/delete/{blogName}", blogName)
                .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteBlog_Unauthorized() throws Exception {
        String blogName = "Test Blog";
        String token = "invalidToken";

        when(jwtUtils.validateJwtToken(token)).thenReturn(false);

        mockMvc.perform(delete("/blogsite/user/blogs/delete/{blogName}", blogName)
                .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetAllBlogs_Success() throws Exception {
        List<Blogs> blogs = new ArrayList<>();
        blogs.add(new Blogs());
        blogs.add(new Blogs());

        when(blogRepository.findAll()).thenReturn(blogs);

        mockMvc.perform(get("/blogsite/user/blogs/getall"))
                .andExpect(status().isOk());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
