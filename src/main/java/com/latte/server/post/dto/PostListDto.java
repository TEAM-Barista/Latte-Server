package com.latte.server.post.dto;
import com.latte.server.post.domain.Post;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Created by Donggun on 2021-08-05
 */

@Data
public class PostListDto {
    private Long postId;
    private String userName;
    private String postTitle;
    private String postContent;
    private int replyCount;

    public PostListDto(Post post, int replyCount) {
        this.postId = post.getId();
        this.userName = post.getUser().getUserName();
        this.postTitle = post.getPostTitle();
        this.postContent = post.getPostContent();
        this.replyCount = replyCount;
    }

}
