package com.latte.server.interview.dto.request;

import lombok.Data;

@Data
public class LoadInterviewRequest {
    private Long userId;
    private Long interviewId;
}
