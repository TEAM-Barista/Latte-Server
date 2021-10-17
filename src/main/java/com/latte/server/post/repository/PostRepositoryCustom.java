package com.latte.server.post.repository;

import com.latte.server.post.dto.PostListDto;
import com.latte.server.post.dto.PostSearchCondition;
import com.latte.server.post.dto.ReplyDto;
import com.latte.server.post.dto.ReplySearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;


/**
 * Created by Donggun on 2021-08-26
 */
public interface PostRepositoryCustom {
    Page<PostListDto> searchPostPage(PostSearchCondition condition, Pageable pageable);
    Page<PostListDto> searchPostPageRecent(PostSearchCondition condition, Pageable pageable);
    Page<PostListDto> searchPostPagePopular(PostSearchCondition condition, Pageable pageable);
    Page<ReplyDto> searchReplyPageRecent(ReplySearchCondition condition, Pageable pageable);
    Page<ReplyDto> searchReplyPageOld(ReplySearchCondition condition, Pageable pageable);
}
