package com.blogging.platfom.api.service;


import com.blogging.platfom.api.dto.BlogPostDTO;
import com.blogging.platfom.api.entity.BlogPost;
import com.blogging.platfom.api.exception.ResourceNotFoundException;
import com.blogging.platfom.api.repository.BlogPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogPostService {
    private final BlogPostRepository repository;

    public BlogPost createPost(BlogPostDTO postDTO) {
        BlogPost post = new BlogPost(null, postDTO.getTitle(), postDTO.getContent(), postDTO.getCategory(), postDTO.getTags(), null, null);
        return repository.save(post);
    }

    public List<BlogPost> getAllPosts(String searchTerm) {
        if (searchTerm != null && !searchTerm.isEmpty()) {
            return repository.findByTitleContainingOrContentContainingOrCategoryContaining(searchTerm, searchTerm, searchTerm);
        }
        return repository.findAll();
    }

    public BlogPost getPostById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
    }

    public BlogPost updatePost(Long id, BlogPostDTO postDTO) {
        BlogPost post = getPostById(id);
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setCategory(postDTO.getCategory());
        post.setTags(postDTO.getTags());
        return repository.save(post);
    }

    public void deletePost(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Post not found");
        }
        repository.deleteById(id);
    }
}
