package com.latte.server.interview.repository;

import com.latte.server.interview.domain.Interview;
import com.latte.server.interview.domain.InterviewBookmark;
import com.latte.server.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Donggun on 2021-08-05
 */

public interface InterviewBookmarkRepository extends JpaRepository<InterviewBookmark, Long> {
    int countByInterview(Interview interview);

    Long countLongByInterview(Interview interview);

    int countByInterviewAndUser(Interview interview, User user);

    Long countLongByInterviewAndUser(Interview interview, User user);

    InterviewBookmark findByInterview(Interview interview);
}
