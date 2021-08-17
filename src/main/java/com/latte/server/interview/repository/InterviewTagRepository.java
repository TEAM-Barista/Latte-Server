package com.latte.server.interview.repository;

import com.latte.server.interview.domain.Interview;
import com.latte.server.interview.domain.InterviewLike;
import com.latte.server.interview.domain.InterviewTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Donggun on 2021-08-15
 */

public interface InterviewTagRepository extends JpaRepository<InterviewTag, Long> {

    List<InterviewTag> findByInterview(Interview interview);

}
