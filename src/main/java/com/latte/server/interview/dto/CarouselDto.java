package com.latte.server.interview.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.latte.server.category.domain.Category;
import com.latte.server.interview.domain.InterviewTag;
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
    private List<String> interviewTags = new ArrayList<>();
    private List<Long> interviewTagIds = new ArrayList<>();

    public CarouselDto(Long interviewId, String interviewTitle, String interviewContent, int interviewLikeCount,
                       int interviewBookmarkCount, int isLiked, int isBookmarked, List<InterviewTag> interviewTags) {
        this.interviewId = interviewId;
        this.interviewTitle = interviewTitle;
        this.interviewContent = interviewContent;
        this.interviewLikeCount = interviewLikeCount;
        this.interviewBookmarkCount = interviewBookmarkCount;
        this.isLiked = isLiked;
        this.isBookmarked = isBookmarked;

        List<String> interviewTagNames = new ArrayList<>();
        List<Long> interviewTagIds = new ArrayList<>();

        for (InterviewTag interviewTag : interviewTags) {
            interviewTagNames.add(interviewTag.getCategory().getCategory());
            interviewTagIds.add(interviewTag.getCategory().getId());
        }
        this.interviewTags = interviewTagNames;
        this.interviewTagIds = interviewTagIds;
    }
}
