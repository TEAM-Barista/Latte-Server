package com.latte.server.post.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReplyLikeResponse {
    private Long replyId;
    private Long replyLikeId;
}

