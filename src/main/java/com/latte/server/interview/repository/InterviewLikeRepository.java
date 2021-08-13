package com.latte.server.interview.repository;

import com.latte.server.interview.domain.Interview;
import com.latte.server.interview.domain.InterviewLike;
import com.latte.server.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Donggun on 2021-08-05
 */

public interface InterviewLikeRepository extends JpaRepository<InterviewLike, Long> {

    int countByInterview(Interview interview);

    int countByInterviewAndAndUser(Interview interview, User user);

    InterviewLike findByInterview(Interview interview);
}
