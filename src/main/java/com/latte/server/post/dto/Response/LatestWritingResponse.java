package com.latte.server.post.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LatestWritingResponse<T> {
    private int count;
    private T data;
}
