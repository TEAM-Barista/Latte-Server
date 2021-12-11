package com.latte.server.interview.dto;

import com.latte.server.interview.domain.InterviewTag;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Created by Donggun on 2021-09-09
 */

@Data
public class
InterviewSearchCondition {
    private Long userId;
    private Long interviewId;
    private InterviewTag interviewTag;
    private String titleKeyword;
    private String contentKeyword;
    private ArrayList<Long> categoryIds;
    private LocalDateTime dateAfter;
    private LocalDateTime dateBefore;
}
