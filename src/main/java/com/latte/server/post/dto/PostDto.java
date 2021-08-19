package com.latte.server.post.dto;

import com.latte.server.post.domain.Tag;
import lombok.Data;

import java.util.List;

/**
 * Created by Donggun on 2021-08-17
 */

@Data
public class PostDto {
    private Long userId;
    private String postTitle;
    private String postContent;
    private String postCode;
    private List<Long> postTags;

    public PostDto(Long userId, String postTitle, String postContent, String postCode, List<Long> postTags) {
        this.userId = userId;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postCode = postCode;
        this.postTags = postTags;
    }
}
