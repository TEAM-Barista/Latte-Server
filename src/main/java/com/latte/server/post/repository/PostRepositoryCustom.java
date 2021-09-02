package com.latte.server.post.repository;

import com.latte.server.post.dto.PostListDto;
import com.latte.server.post.dto.PostSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<PostListDto> searchPostPage(PostSearchCondition condition, Pageable pageable);
    Page<PostListDto> searchPostPageRecent(PostSearchCondition condition, Pageable pageable);
    Page<PostListDto> searchPostPagePopular(PostSearchCondition condition, Pageable pageable);
}
