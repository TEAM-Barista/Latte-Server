package com.latte.server.interview.service;

import com.latte.server.category.domain.Category;
import com.latte.server.category.repository.CategoryRepository;
import com.latte.server.interview.domain.Interview;
import com.latte.server.interview.domain.InterviewBookmark;
import com.latte.server.interview.domain.InterviewLike;
import com.latte.server.interview.domain.InterviewTag;
import com.latte.server.interview.dto.CarouselDto;
import com.latte.server.interview.dto.InterviewDetailDto;
import com.latte.server.interview.repository.*;
import com.latte.server.post.domain.Post;
import com.latte.server.post.domain.SeniorRequest;
import com.latte.server.post.domain.Tag;
import com.latte.server.post.dto.PostDetailDto;
import com.latte.server.user.domain.User;
import com.latte.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;

/**
 * Created by Donggun on 2021-08-05
 */

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InterviewService {
    private static final String NOT_EXIST_USER = "[ERROR] No such User";
    private static final String NOT_EXIST_INTERIVEW = "[ERROR] No such Interview";
    private static final String NOT_EXIST_CATEGORY = "[ERROR] No such Category";
    private static final String NOT_EXIST_TEXT = "[ERROR] Do not contain text";
    private static final Long INTERVIEW_LIKE_DELETED = 0L;
    private static final Long INTERVIEW_BOOKMARK_DELETED = 0L;

    private final UserRepository userRepository;
    private final InterviewRepository interviewRepository;
    private final InterviewLikeRepository interviewLikeRepository;
    private final InterviewBookmarkRepository interviewBookmarkRepository;
    private final InterviewTagRepository interviewTagRepository;
    private final CategoryRepository categoryRepository;
    private final SeniorRequestRepository seniorRequestRepository;

    public CarouselDto loadCarousel(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_USER));

        Interview interviewByCreatedDate = interviewRepository.findInterviewByCreatedDate();
        int interviewLikeCount = interviewLikeRepository.countByInterview(interviewByCreatedDate);
        int interviewBookmarkCount = interviewBookmarkRepository.countByInterview(interviewByCreatedDate);
        int isLiked = interviewLikeRepository.countByInterviewAndUser(interviewByCreatedDate, user);
        int isBookmarked = interviewBookmarkRepository.countByInterviewAndUser(interviewByCreatedDate, user);

        return new CarouselDto(interviewByCreatedDate, interviewLikeCount, interviewBookmarkCount,
                isLiked, isBookmarked);
    }

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

    @Transactional
    public Long updateInterviewTag(Long interviewId, List<Long> categoryIds) {

        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_INTERIVEW));

        interview.clearInterviewTag();

        for (Long categoryId : categoryIds) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_CATEGORY));

            InterviewTag interviewTag = InterviewTag.createInterviewTag(interview, category);
            interview.addInterviewTag(interviewTag);
            interviewTagRepository.save(interviewTag);
        }
        return interviewId;
    }

    @Transactional
    public Long createInterviewTag(Interview interview, Category category) {
        InterviewTag interviewTag = InterviewTag.createInterviewTag(interview, category);
        interview.addInterviewTag(interviewTag);
        interviewTagRepository.save(interviewTag);
        return interviewTag.getId();
    }

    @Transactional
    public Long createSeniorRequest(Long userId, Interview interview, String title, String content) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_USER));
        valifyText(content, title);

        SeniorRequest seniorRequest = SeniorRequest.createSeniorRequest(user, interview, title, content);

        seniorRequestRepository.save(seniorRequest);

        return seniorRequest.getId();
    }

    private void valifyText(String content, String title) {
        if (!(hasText(title) && hasText(content))) {
            throw new IllegalArgumentException(NOT_EXIST_TEXT);
        }
    }

    public InterviewDetailDto loadInterview(Long userId, Long interviewId) {

        Interview findInterview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_INTERIVEW));
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_USER));

        Long isBookmarked = interviewBookmarkRepository.countLongByInterviewAndUser(findInterview, findUser);
        Long bookmarkCount = interviewBookmarkRepository.countLongByInterview(findInterview);

        Long isLiked = interviewLikeRepository.countLongByInterviewAndUser(findInterview, findUser);
        Long likeCount = interviewLikeRepository.countLongByInterview(findInterview);

        return new InterviewDetailDto(findInterview, bookmarkCount, isBookmarked, likeCount, isLiked);
    }


}
