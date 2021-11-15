package com.latte.server.post.dto.Request;

import lombok.Data;

@Data
public class ReplyLikeRequest {
    private Long replyId;
    private Long userId;
}
