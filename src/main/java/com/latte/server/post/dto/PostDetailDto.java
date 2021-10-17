package com.latte.server.post.dto;

import com.latte.server.post.domain.Post;
import com.latte.server.post.domain.Tag;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Donggun on 2021-09-02
 */

@Data
public class PostDetailDto {
    private Long userId;
    private String userName;
    private Long postId;
    private String postTitle;
    private String postContent;
    private String postCode;
    private LocalDateTime createdDate;
    private Long replyCount;
    private Long bookmarkCount;
    private long isBookmarked;
    private List<Long> tagIds;
    private List<String> tags;

    public PostDetailDto(Post post, Long replyCount, Long bookmarkCount, long isBookmarked) {
        this.userId = post.getUser().getId();
        this.userName = post.getUser().getUserName();
        this.postId = post.getId();
        this.postTitle = post.getPostTitle();
        this.postContent = post.getPostContent();
        this.postCode = post.getPostCode();
        this.createdDate = post.getCreatedDate();
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
