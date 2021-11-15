package com.latte.server.post.dto.Request;


import lombok.Data;

import java.util.List;

@Data
public class UpdatePostRequest {
    private String postTitle;
    private String postContent;
    private String postCode;
    private List<Long> tagIds;
}