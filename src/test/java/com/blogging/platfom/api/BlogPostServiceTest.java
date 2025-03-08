package com.blogging.platfom.api;

import com.blogging.platfom.api.dto.BlogPostDTO;
import com.blogging.platfom.api.entity.BlogPost;
import com.blogging.platfom.api.exception.ResourceNotFoundException;
import com.blogging.platfom.api.repository.BlogPostRepository;
import com.blogging.platfom.api.service.BlogPostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BlogPostServiceTest {

    @Mock
    private BlogPostRepository repository;

    @InjectMocks
    private BlogPostService service;

    private BlogPost blogPost;
    private BlogPostDTO blogPostDTO;

    @BeforeEach
    void setUp() {
        blogPost = new BlogPost(1L, "Test Title", "Test Content", "Tech", List.of("Spring", "Java"), null, null);
        blogPostDTO = new BlogPostDTO("New Title", "Updated Content", "Tech", List.of("Spring Boot"));
    }

    @Test
    void shouldCreatePost() {
        when(repository.save(any())).thenReturn(blogPost);
        BlogPost createdPost = service.createPost(blogPostDTO);
        assertNotNull(createdPost);
        assertEquals(blogPost.getTitle(), createdPost.getTitle());
    }

    @Test
    void shouldReturnAllPosts() {
        when(repository.findAll()).thenReturn(List.of(blogPost));
        List<BlogPost> posts = service.getAllPosts(null);
        assertFalse(posts.isEmpty());
        assertEquals(1, posts.size());
    }

    @Test
    void shouldReturnPostById() {
        when(repository.findById(1L)).thenReturn(Optional.of(blogPost));
        BlogPost foundPost = service.getPostById(1L);
        assertEquals(blogPost.getTitle(), foundPost.getTitle());
    }

    @Test
    void shouldThrowExceptionWhenPostNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getPostById(1L));
    }

    @Test
    void shouldUpdatePost() {
        when(repository.findById(1L)).thenReturn(Optional.of(blogPost));
        when(repository.save(any())).thenReturn(blogPost);

        BlogPost updatedPost = service.updatePost(1L, blogPostDTO);
        assertEquals(blogPostDTO.getTitle(), updatedPost.getTitle());
    }

    @Test
    void shouldDeletePost() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        assertDoesNotThrow(() -> service.deletePost(1L));
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentPost() {
        when(repository.existsById(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> service.deletePost(1L));
    }
}

