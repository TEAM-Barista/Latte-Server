package com.latte.server.post.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by Donggun on 2021-09-03
 */

@Data
public class ReplySearchCondition {
    private Long userId;
    private Long postId;
    private String replyContentKeyword;
    private String replyUserName;
    private LocalDateTime dateAfter;
}
