package com.latte.server.post.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostSearchCondition {
    private Long userId;
    private Long postId;
    private String titleKeyword;
    private String contentKeyword;
    private String codeKeyword;
    private Integer isQna;
    private LocalDateTime dateAfter;
}