package com.latte.server.interview.dto;

import com.latte.server.interview.domain.Interview;
import com.latte.server.interview.domain.InterviewTag;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class InterviewListDto {

    private Long interviewId;
    private String interviewTitle;
    private String interviewContent;
    private Long interviewLikeCount;
    private Long interviewBookmarkCount;
    private long isLiked;
    private long isBookmarked;
    private List<String> interviewTags;
    private List<Long> interviewTagIds;

    @QueryProjection
    public InterviewListDto(Interview interview, Long interviewLikeCount,
                       Long interviewBookmarkCount, long isLiked, long isBookmarked) {
        this.interviewId = interview.getId();
        this.interviewTitle = interview.getInterviewTitle();
        this.interviewContent = interview.getInterviewContent();
        this.interviewLikeCount = interviewLikeCount;
        this.interviewBookmarkCount = interviewBookmarkCount;
        this.isLiked = isLiked;
        this.isBookmarked = isBookmarked;

        List<String> interviewTagNames = new ArrayList<>();
        List<Long> interviewTagIds = new ArrayList<>();

        // interviewTag는 Category의 name으로, id는 category id 가 아닌 interivew id로 통일.
        for (InterviewTag interviewTag : interview.getInterviewTags()) {
            interviewTagNames.add(interviewTag.getCategory().getCategory());
            interviewTagIds.add(interviewTag.getId());
        }
        this.interviewTags = interviewTagNames;
        this.interviewTagIds = interviewTagIds;
    }
}
