package com.latte.server.interview.service;

import com.latte.server.category.domain.Category;
import com.latte.server.category.repository.CategoryRepository;
import com.latte.server.common.exception.custom.NotFoundCategoryException;
import com.latte.server.common.exception.custom.NotFoundInterviewException;
import com.latte.server.common.exception.custom.NotFoundTextException;
import com.latte.server.common.exception.custom.NotFoundUserException;
import com.latte.server.interview.domain.Interview;
import com.latte.server.interview.domain.InterviewBookmark;
import com.latte.server.interview.domain.InterviewLike;
import com.latte.server.interview.domain.InterviewTag;
import com.latte.server.interview.dto.CarouselDto;
import com.latte.server.interview.dto.InterviewDetailDto;
import com.latte.server.interview.dto.InterviewListDto;
import com.latte.server.interview.dto.InterviewSearchCondition;
import com.latte.server.interview.repository.*;
import com.latte.server.post.domain.SeniorRequest;
import com.latte.server.user.domain.User;
import com.latte.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.util.StringUtils.hasText;

/**
 * Created by Donggun on 2021-08-05
 */

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InterviewService {
    private static final Long INTERVIEW_LIKE_DELETED = 0L;
    private static final Long INTERVIEW_BOOKMARK_DELETED = 0L;

    private final UserRepository userRepository;
    private final InterviewRepository interviewRepository;
    private final InterviewLikeRepository interviewLikeRepository;
    private final InterviewBookmarkRepository interviewBookmarkRepository;
    private final InterviewTagRepository interviewTagRepository;
    private final CategoryRepository categoryRepository;
    private final SeniorRequestRepository seniorRequestRepository;

    public CarouselDto loadCarousel(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(NotFoundUserException::new);

        Interview interviewByCreatedDate = interviewRepository.findInterviewByCreatedDate();
        int interviewLikeCount = interviewLikeRepository.countByInterview(interviewByCreatedDate);
        int interviewBookmarkCount = interviewBookmarkRepository.countByInterview(interviewByCreatedDate);
        int isLiked = interviewLikeRepository.countByInterviewAndUser(interviewByCreatedDate, user);
        int isBookmarked = interviewBookmarkRepository.countByInterviewAndUser(interviewByCreatedDate, user);

        return new CarouselDto(interviewByCreatedDate, interviewLikeCount, interviewBookmarkCount,
                isLiked, isBookmarked);
    }

    @Transactional
    public Long createInterview(User user, String interviewContent, String interviewTitle) {
        Interview interview = Interview.createInterview(user, interviewContent, interviewTitle);

        interviewRepository.save(interview);

        return interview.getId();
    }

    @Transactional
    public Long createInterviewLike(User user, Interview interview) {
        if (interviewLikeRepository.findByInterview(interview) == null) {
            InterviewLike interviewLike = InterviewLike.createInterviewLike(interview, user);
            interviewLikeRepository.save(interviewLike);
            return interviewLike.getId();
        }

        interviewLikeRepository.delete(interviewLikeRepository.findByInterview(interview));
        return INTERVIEW_LIKE_DELETED;
    }

    @Transactional
    public Long createInterviewBookmark(User user, Interview interview) {
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
                .orElseThrow(NotFoundInterviewException::new);

        interview.clearInterviewTag();

        for (Long categoryId : categoryIds) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(NotFoundCategoryException::new);

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
    public Long createSeniorRequest(User user, Interview interview, String title, String content) {
        valifyText(content, title);

        SeniorRequest seniorRequest = SeniorRequest.createSeniorRequest(user, interview, title, content);

        seniorRequestRepository.save(seniorRequest);

        return seniorRequest.getId();
    }

    private void valifyText(String content, String title) {
        if (!(hasText(title) && hasText(content))) {
            throw new NotFoundTextException();
        }
    }

    public InterviewDetailDto loadInterview(String email, Long interviewId) {
        User findUser = userRepository.findByEmail(email)
                .orElseThrow(NotFoundUserException::new);
        Interview findInterview = interviewRepository.findById(interviewId)
                .orElseThrow(NotFoundInterviewException::new);

        Long isBookmarked = interviewBookmarkRepository.countLongByInterviewAndUser(findInterview, findUser);
        Long bookmarkCount = interviewBookmarkRepository.countLongByInterview(findInterview);

        Long isLiked = interviewLikeRepository.countLongByInterviewAndUser(findInterview, findUser);
        Long likeCount = interviewLikeRepository.countLongByInterview(findInterview);

        return new InterviewDetailDto(findInterview, bookmarkCount, isBookmarked, likeCount, isLiked);
    }


    public Long bookmarkInterview(Long interviewId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(NotFoundUserException::new);

        Interview findInterview = interviewRepository.findById(interviewId)
                .orElseThrow(NotFoundInterviewException::new);

        Long interviewBookmarkId = createInterviewBookmark(user, findInterview);

        return interviewBookmarkId;
    }

    public Long likeInterview(Long interviewId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(NotFoundUserException::new);

        Interview findInterview = interviewRepository.findById(interviewId)
                .orElseThrow(NotFoundInterviewException::new);

        Long interviewLikeId = createInterviewLike(user, findInterview);

        return interviewLikeId;
    }

    public Long requestSenior(Long interviewId, String title, String content, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(NotFoundUserException::new);

        Interview findInterview = interviewRepository.findById(interviewId)
                .orElseThrow(NotFoundInterviewException::new);

        Long seniorRequestId = createSeniorRequest(user, findInterview, title, content);

        return seniorRequestId;
    }

    public Page<InterviewListDto> searchRepositoryInterviewPageRecent(InterviewSearchCondition condition, Pageable pageable, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(NotFoundUserException::new);
        condition.setUserId(user.getId());
        return interviewRepository.searchInterviewPageRecent(condition, pageable);
    }

    public Page<InterviewListDto> searchRepositoryInterviewPageRecommend(InterviewSearchCondition condition, Pageable pageable, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(NotFoundUserException::new);
        condition.setUserId(user.getId());
        return interviewRepository.searchInterviewPageRecommend(condition, pageable);
    }

    public Page<InterviewListDto> searchRepositoryInterviewPage(InterviewSearchCondition condition, Pageable pageable, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(NotFoundUserException::new);
        condition.setUserId(user.getId());
        return interviewRepository.searchInterviewPageRecommend(condition, pageable);
    }
}
