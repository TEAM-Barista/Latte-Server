package com.latte.server.post.dto.Request;

import lombok.Data;

@Data
public class BookmarkPostRequest {
    private Long postId;
    private Long userId;
}
