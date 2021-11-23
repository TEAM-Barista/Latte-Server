package com.latte.server.post.dto.Request;

import lombok.Data;

import java.util.List;

@Data
public class CreatePostRequest {
    private String postTitle;
    private String postContent;
    private String postCode;
    private List<Long> postTags;
}

