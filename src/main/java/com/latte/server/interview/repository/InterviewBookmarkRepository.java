package com.latte.server.interview.repository;

import com.latte.server.interview.domain.Interview;
import com.latte.server.interview.domain.InterviewBookmark;
import com.latte.server.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewBookmarkRepository extends JpaRepository<InterviewBookmark, Long> {
    int countByInterview(Interview interview);

    int countByInterviewAndAndUser(Interview interview, User user);

    InterviewBookmark findByInterview(Interview interview);
}
