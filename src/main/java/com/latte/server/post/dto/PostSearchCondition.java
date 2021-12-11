package com.latte.server.post.dto;

import com.latte.server.category.domain.Category;
import com.latte.server.post.domain.Tag;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Donggun on 2021-08-17
 */

@Data
public class PostSearchCondition {
    private Long userId;
    private Long postId;
    private String titleKeyword;
    private String contentKeyword;
    private String codeKeyword;
    private Integer isQna;
    private ArrayList<Long> categoryIds;
    private LocalDateTime dateAfter;
}
