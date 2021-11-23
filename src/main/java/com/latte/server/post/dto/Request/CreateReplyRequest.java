package com.latte.server.post.dto.Request;

import lombok.Data;

@Data
public class CreateReplyRequest {
    private Long postId;
    private String replyContent;
}

