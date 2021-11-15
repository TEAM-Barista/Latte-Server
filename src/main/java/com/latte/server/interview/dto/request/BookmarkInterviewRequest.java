package com.latte.server.interview.dto.request;

import lombok.Data;

@Data
public class BookmarkInterviewRequest {
    private Long interviewId;
    private Long userId;
}
