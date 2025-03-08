package com.blogging.platfom.api.dto;


import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class BlogPostDTO {
    private String title;
    private String content;
    private String category;
    private List<String> tags;
}
