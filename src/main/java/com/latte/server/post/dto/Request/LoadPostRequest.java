package com.latte.server.post.dto.Request;

import lombok.Data;

@Data
public class LoadPostRequest {
    private Long userId;
    private Long postId;
}
