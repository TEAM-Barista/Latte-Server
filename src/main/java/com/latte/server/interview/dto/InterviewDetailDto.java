package com.latte.server.interview.dto;

import com.latte.server.interview.domain.Interview;
import com.latte.server.interview.domain.InterviewTag;
import com.latte.server.post.domain.Post;
import com.latte.server.post.domain.Tag;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Donggun on 2021-09-16
 */

@Data
public class InterviewDetailDto {
    private Long userId;
    private String userName;
    private Long interviewId;
    private String interviewTitle;
    private String interviewContent;
    private LocalDateTime createdDate;
    private Long bookmarkCount;
    private Long likeCount;
    private long isBookmarked;
    private long isLiked;
    private List<Long> tagIds;
    private List<String> tags;

    public InterviewDetailDto(Interview interview, Long bookmarkCount, long isBookmarked, Long likeCount, long isLiked) {
        this.userId = interview.getUser().getId();
        this.userName = interview.getUser().getNickName();
        this.interviewId = interview.getId();
        this.interviewTitle = interview.getInterviewTitle();
        this.interviewContent = interview.getInterviewContent();
        this.createdDate = interview.getCreatedDate();
        this.bookmarkCount = bookmarkCount;
        this.likeCount = likeCount;
        this.isBookmarked = isBookmarked;
        this.isLiked = isLiked;

        List<Long> tagIds = new ArrayList<>();
        List<String> tags = new ArrayList<>();
        List<InterviewTag> interviewTags = interview.getInterviewTags();
        for (InterviewTag interviewTag : interviewTags) {
            tagIds.add(interviewTag.getCategory().getId());
            tags.add(interviewTag.getCategory().getCategory());
        }

        this.tagIds = tagIds;
        this.tags = tags;
    }
}
