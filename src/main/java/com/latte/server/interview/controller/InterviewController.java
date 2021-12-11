package com.latte.server.interview.controller;

import com.latte.server.interview.domain.Interview;
import com.latte.server.interview.dto.CarouselDto;
import com.latte.server.interview.dto.InterviewDetailDto;
import com.latte.server.interview.dto.InterviewListDto;
import com.latte.server.interview.dto.InterviewSearchCondition;
import com.latte.server.interview.dto.request.*;
import com.latte.server.interview.dto.response.*;
import com.latte.server.interview.repository.InterviewRepository;
import com.latte.server.interview.service.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by Donggun on 2021-08-15
 */

@RestController
@RequiredArgsConstructor
public class InterviewController {
    private static final int LOADED_INTERVIEW_SIZE = 1;

    private final InterviewService interviewService;


    @GetMapping("/api/v1/interview/carousel")
    public CarouselDto carouselV1(@AuthenticationPrincipal String email) {
        return interviewService.loadCarousel(email);
    }


    @PostMapping("/api/v1/interview/interviewBookmark")
    public BookmarkInterviewResponse interviewBookmarkV1(@RequestBody @Valid BookmarkInterviewRequest request, @AuthenticationPrincipal String email) {
        Long interviewBookmarkId = interviewService.bookmarkInterview(request.getInterviewId(), email);

        return new BookmarkInterviewResponse(interviewBookmarkId);
    }

    @PostMapping("/api/v1/interview/interviewLike")
    public LikeInterviewResponse interviewLikeV1(@RequestBody @Valid LikeInterviewRequest request, @AuthenticationPrincipal String email) {
        Long interviewLikeId = interviewService.likeInterview(request.getInterviewId(), email);

        return new LikeInterviewResponse(interviewLikeId);
    }

    @PostMapping("/api/v1/interview/seniorRequest")
    public SeniorRequestResponse seniorRequestV1(@RequestBody @Valid SeniorRequestRequest request, @AuthenticationPrincipal String email) {
        Long seniorRequestId = interviewService.requestSenior(request.getInterviewId(), request.getTitle(), request.getContent(), email);

        return new SeniorRequestResponse(seniorRequestId);
    }

    @GetMapping("/api/v1/interview/interviewListRecent")
    public Page<InterviewListDto> interviewListRecentV1(InterviewSearchCondition condition, Pageable pageable, @AuthenticationPrincipal String email) {
        Page<InterviewListDto> result = interviewService.searchRepositoryInterviewPageRecent(condition, pageable, email);

        return result;
    }

    @GetMapping("/api/v1/interview/interviewListRecommend")
    public Page<InterviewListDto> interviewListRecommendV1(InterviewSearchCondition condition, Pageable pageable, @AuthenticationPrincipal String email) {
        Page<InterviewListDto> result = interviewService.searchRepositoryInterviewPageRecommend(condition, pageable, email);
        return result;
    }

    @GetMapping("/api/v1/interview/searchInterviews")
    public Page<InterviewListDto> searchInterviewsV1(InterviewSearchCondition condition, Pageable pageable, @AuthenticationPrincipal String email) {
        Page<InterviewListDto> result = interviewService.searchRepositoryInterviewPage(condition, pageable, email);
        return result;
    }

    @GetMapping("/api/v1/interview/readInterview")
    public LoadInterviewResponse<InterviewDetailDto> loadInterviewV1(@RequestBody @Valid LoadInterviewRequest request, @AuthenticationPrincipal String email) {
        return new LoadInterviewResponse<>(LOADED_INTERVIEW_SIZE, interviewService.loadInterview(email, request.getInterviewId()));
    }

}
