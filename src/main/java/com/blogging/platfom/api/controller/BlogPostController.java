package com.blogging.platfom.api.controller;

import com.blogging.platfom.api.dto.BlogPostDTO;
import com.blogging.platfom.api.entity.BlogPost;
import com.blogging.platfom.api.service.BlogPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class BlogPostController {
    private final BlogPostService service;

    @PostMapping
    public ResponseEntity<BlogPost> createPost(@RequestBody BlogPostDTO postDTO) {
        return new ResponseEntity<>(service.createPost(postDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BlogPost>> getAllPosts(@RequestParam(required = false) String term) {
        return ResponseEntity.ok(service.getAllPosts(term));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogPost> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getPostById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogPost> updatePost(@PathVariable Long id, @RequestBody BlogPostDTO postDTO) {
        return ResponseEntity.ok(service.updatePost(id, postDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        service.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}

