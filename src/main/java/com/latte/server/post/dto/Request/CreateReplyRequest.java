package com.latte.server.post.dto.Request;

import lombok.Data;

@Data
public class CreateReplyRequest {
    private Long userId;
    private Long postId;
    private String postContent;
}

