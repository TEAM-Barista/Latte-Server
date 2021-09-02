package com.latte.server.interview.repository;

import com.latte.server.interview.domain.Interview;

import java.util.List;

/**
 * Created by Donggun on 2021-08-05
 */

public interface InterviewRepositoryCustom {
    Interview findInterviewByCreatedDate();
}
