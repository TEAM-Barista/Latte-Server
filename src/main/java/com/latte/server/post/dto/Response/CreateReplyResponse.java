package com.latte.server.post.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateReplyResponse {
    private Long userId;
    private Long postId;
    private Long replyId;
}
