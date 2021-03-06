package com.latte.server.interview.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.latte.server.category.domain.Category;
import com.latte.server.interview.domain.Interview;
import com.latte.server.interview.domain.InterviewTag;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;

@Data
public class CarouselDto {

    private Long interviewId;
    private String interviewTitle;
    private String interviewContent;
    private int interviewLikeCount;
    private int interviewBookmarkCount;
    private int isLiked;
    private int isBookmarked;
    private List<String> interviewTags;
    private List<Long> interviewTagIds;

    @QueryProjection
    public CarouselDto(Interview interview, int interviewLikeCount,
                       int interviewBookmarkCount, int isLiked, int isBookmarked) {
        this.interviewId = interview.getId();
        this.interviewTitle = interview.getInterviewTitle();
        this.interviewContent = interview.getInterviewContent();
        this.interviewLikeCount = interviewLikeCount;
        this.interviewBookmarkCount = interviewBookmarkCount;
        this.isLiked = isLiked;
        this.isBookmarked = isBookmarked;

        List<String> interviewTagNames = new ArrayList<>();
        List<Long> interviewTagIds = new ArrayList<>();

        for (InterviewTag interviewTag : interview.getInterviewTags()) {
            interviewTagNames.add(interviewTag.getCategory().getCategory());
            interviewTagIds.add(interviewTag.getCategory().getId());
        }
        this.interviewTags = interviewTagNames;
        this.interviewTagIds = interviewTagIds;
    }
}
