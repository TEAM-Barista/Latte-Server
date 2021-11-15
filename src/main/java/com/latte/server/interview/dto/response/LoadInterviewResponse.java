package com.latte.server.interview.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoadInterviewResponse<T> {
    private int count;
    private T data;
}

