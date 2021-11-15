package com.latte.server.interview.dto.request;

import lombok.Data;

@Data
public class SeniorRequestRequest {
    private Long interviewId;
    private Long userId;
    private String title;
    private String content;
}
