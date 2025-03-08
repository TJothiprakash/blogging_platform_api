package com.blogging.platfom.api;


import com.blogging.platfom.api.dto.BlogPostDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BlogPostControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateBlogPost() throws Exception {
        // Arrange
        BlogPostDTO post = new BlogPostDTO("Integration Test", "Testing content", "Testing", List.of("Spring", "Test"));

        // Act
        ResultActions response = mockMvc.perform(post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(post)));

        // Assert
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Integration Test"))
                .andExpect(jsonPath("$.category").value("Testing"));
    }

    @Test
    void shouldGetAllBlogPosts() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldGetBlogPostById() throws Exception {
        // Arrange: Create a post first
        BlogPostDTO post = new BlogPostDTO("Sample Post", "Content", "Category", List.of("Java"));
        String response = mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andReturn().getResponse().getContentAsString();

        Long postId = objectMapper.readTree(response).get("id").asLong();

        // Act & Assert
        mockMvc.perform(get("/posts/" + postId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(postId));
    }

    @Test
    void shouldUpdateBlogPost() throws Exception {
        // Arrange: Create a post first
        BlogPostDTO post = new BlogPostDTO("Old Title", "Old Content", "Old Category", List.of("Java"));
        String response = mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andReturn().getResponse().getContentAsString();

        Long postId = objectMapper.readTree(response).get("id").asLong();

        BlogPostDTO updatedPost = new BlogPostDTO("Updated Title", "Updated Content", "Updated Category", List.of("Spring"));

        // Act & Assert
        mockMvc.perform(put("/posts/" + postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPost)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    void shouldDeleteBlogPost() throws Exception {
        // Arrange: Create a post first
        BlogPostDTO post = new BlogPostDTO("To be deleted", "Content", "Category", List.of("Java"));
        String response = mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andReturn().getResponse().getContentAsString();

        Long postId = objectMapper.readTree(response).get("id").asLong();

        // Act & Assert: Delete the post and verify
        mockMvc.perform(delete("/posts/" + postId))
                .andExpect(status().isNoContent());

        // Verify it's deleted
        mockMvc.perform(get("/posts/" + postId))
                .andExpect(status().isNotFound());
    }
}

