package com.latte.server.post.dto;
import com.latte.server.post.domain.Post;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PostListDto {
    private Long postId;
    private String userName;
    private String postTitle;
    private String postContent;
    private int replyCount;
    private int postLikeCount;
    private LocalDateTime createdAt;

    public PostListDto(Post post, int replyCount, int postLikeCount) {
        this.postId = post.getId();
        this.userName = post.getUser().getUserName();
        this.postTitle = post.getPostTitle();
        this.postContent = post.getPostContent();
        this.createdAt = post.getCreatedAt();
        this.replyCount = replyCount;
        this.postLikeCount = postLikeCount;
    }
}
