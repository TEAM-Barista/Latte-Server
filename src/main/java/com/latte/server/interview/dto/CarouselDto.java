package com.latte.server.interview.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.latte.server.category.domain.Category;
import lombok.Data;

import javax.persistence.Column;
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

    public CarouselDto(Long interviewId, String interviewTitle, String interviewContent, int interviewLikeCount, int interviewBookmarkCount, int isLiked, int isBookmarked) {
        this.interviewId = interviewId;
        this.interviewTitle = interviewTitle;
        this.interviewContent = interviewContent;
        this.interviewLikeCount = interviewLikeCount;
        this.interviewBookmarkCount = interviewBookmarkCount;
        this.isLiked = isLiked;
        this.isBookmarked = isBookmarked;
    }
}
