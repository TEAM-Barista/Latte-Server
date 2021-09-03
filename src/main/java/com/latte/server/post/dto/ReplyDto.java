package com.latte.server.post.dto;

import com.latte.server.post.domain.Post;
import com.latte.server.post.domain.Reply;
import com.latte.server.user.domain.User;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by Donggun on 2021-09-02
 */

@Data
public class ReplyDto {
    private Long postId;
    private Long userId;
    private String image;
    private String userName;
    private Long replyId;
    private String replyContent;
    private LocalDateTime createdDate;
    private Long replyLikeCount;
    private long isLiked;

    @QueryProjection
    public ReplyDto(Post post, User user, Reply reply, Long replyLikeCount, long isLiked) {
        this.postId = post.getId();
        this.userId = user.getId();
        this.image = user.getImage();
        this.userName = user.getUserName();
        this.replyId = reply.getId();
        this.replyContent = reply.getReplyContent();
        this.createdDate = reply.getCreatedDate();
        this.replyLikeCount = replyLikeCount;
        this.isLiked = isLiked;
    }
}
