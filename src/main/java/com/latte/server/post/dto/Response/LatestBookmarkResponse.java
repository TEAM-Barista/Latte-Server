package com.latte.server.post.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LatestBookmarkResponse<T> {
    private int count;
    private T data;
}
