package com.latte.server.interview.repository;

import com.latte.server.interview.domain.Interview;
import com.latte.server.interview.dto.InterviewListDto;
import com.latte.server.interview.dto.InterviewSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Donggun on 2021-08-05
 */

public interface InterviewRepositoryCustom {
    Interview findInterviewByCreatedDate();

    Page<InterviewListDto> searchInterviewPageRecent(InterviewSearchCondition condition, Pageable pageable);

    Page<InterviewListDto> searchInterviewPageRecommend(InterviewSearchCondition condition, Pageable pageable);
}
