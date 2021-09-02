package com.latte.server.post.dto;
import com.latte.server.post.domain.Post;
import com.latte.server.post.domain.Tag;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Donggun on 2021-08-05
 */

@Data
public class PostListDto {
    private Long postId;
    private Long userId;
    private String userName;
    private String postTitle;
    private String postContent;
    private Long replyCount;
    private Long bookmarkCount;
    private long isBookmarked;
    private List<Long> tagIds;
    private List<String> tags;

    @QueryProjection
    public PostListDto(Post post, Long replyCount, Long bookmarkCount, long isBookmarked) {
        this.postId = post.getId();
        this.userId = post.getUser().getId();
        this.userName = post.getUser().getNickName();
        this.postTitle = post.getPostTitle();
        this.postContent = post.getPostContent();
        this.replyCount = replyCount;
        this.bookmarkCount = bookmarkCount;
        this.isBookmarked = isBookmarked;

        List<Long> tagIds = new ArrayList<>();
        List<String> tags = new ArrayList<>();
        List<Tag> postTags = post.getPostTags();
        for (Tag postTag : postTags) {
            tagIds.add(postTag.getCategory().getId());
            tags.add(postTag.getCategory().getCategory());
        }
        this.tagIds = tagIds;
        this.tags = tags;
    }
}
