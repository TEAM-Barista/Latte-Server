package com.latte.server.interview.dto;

import com.latte.server.interview.domain.InterviewTag;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by Donggun on 2021-09-09
 */

@Data
public class
InterviewSearchCondition {
    private Long userId;
    private Long interviewId;
    private Long tagId;
    private InterviewTag interviewTag;
    private String titleKeyword;
    private String contentKeyword;
    private LocalDateTime dateAfter;
    private LocalDateTime dateBefore;
}
