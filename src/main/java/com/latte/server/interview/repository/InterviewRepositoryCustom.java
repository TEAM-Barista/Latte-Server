package com.latte.server.interview.repository;

import com.latte.server.interview.domain.Interview;

import java.util.List;

public interface InterviewRepositoryCustom {
    Interview findInterviewByCreatedDate();
}
