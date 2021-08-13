package com.latte.server.interview.service;

import com.latte.server.interview.domain.Interview;
import com.latte.server.interview.domain.InterviewBookmark;
import com.latte.server.interview.domain.InterviewLike;
import com.latte.server.interview.repository.InterviewBookmarkRepository;
import com.latte.server.interview.repository.InterviewLikeRepository;
import com.latte.server.interview.repository.InterviewRepository;
import com.latte.server.user.domain.User;
import com.latte.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Donggun on 2021-08-05
 */

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InterviewService {
    private static final String NOT_EXIST_USER = "[ERROR] No such User";
    private static final Long INTERVIEW_LIKE_DELETED = 0L;
    private static final Long INTERVIEW_BOOKMARK_DELETED = 0L;

    private final UserRepository userRepository;
    private final InterviewRepository interviewRepository;
    private final InterviewLikeRepository interviewLikeRepository;
    private final InterviewBookmarkRepository interviewBookmarkRepository;


    @Transactional
    public Long createInterview(Long userId, String interviewContent, String interviewTitle) {
        // 엔티티 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_USER));

        Interview interview = Interview.createInterview(user, interviewContent, interviewTitle);

        interviewRepository.save(interview);

        return interview.getId();
    }

    @Transactional
    public Long createInterviewLike(Long userId, Interview interview) {

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_USER));

        if (interviewLikeRepository.findByInterview(interview) == null) {
            InterviewLike interviewLike = InterviewLike.createInterviewLike(interview, user);
            interviewLikeRepository.save(interviewLike);
            return interviewLike.getId();
        }

        interviewLikeRepository.delete(interviewLikeRepository.findByInterview(interview));
        return INTERVIEW_LIKE_DELETED;
    }

    @Transactional
    public Long createInterviewBookmark(Long userId, Interview interview) {

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_USER));

        if (interviewBookmarkRepository.findByInterview(interview) == null) {
            InterviewBookmark interviewBookmark = InterviewBookmark.createInterviewBookmark(interview, user);
            interviewBookmarkRepository.save(interviewBookmark);
            return interviewBookmark.getId();
        }

        interviewBookmarkRepository.delete(interviewBookmarkRepository.findByInterview(interview));
        return INTERVIEW_BOOKMARK_DELETED;
    }
}
