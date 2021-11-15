package com.latte.server.interview.dto.request;


import lombok.Data;

@Data
public class LikeInterviewRequest {
    private Long interviewId;
    private Long userId;
}