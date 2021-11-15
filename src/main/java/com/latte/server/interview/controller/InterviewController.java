package com.latte.server.interview.controller;

import com.latte.server.interview.domain.Interview;
import com.latte.server.interview.dto.CarouselDto;
import com.latte.server.interview.dto.InterviewDetailDto;
import com.latte.server.interview.dto.InterviewListDto;
import com.latte.server.interview.dto.InterviewSearchCondition;
import com.latte.server.interview.dto.request.BookmarkInterviewRequest;
import com.latte.server.interview.dto.request.LikeInterviewRequest;
import com.latte.server.interview.dto.request.LoadInterviewRequest;
import com.latte.server.interview.dto.request.SeniorRequestRequest;
import com.latte.server.interview.dto.response.BookmarkInterviewResponse;
import com.latte.server.interview.dto.response.LikeInterviewResponse;
import com.latte.server.interview.dto.response.LoadInterviewResponse;
import com.latte.server.interview.dto.response.SeniorRequestResponse;
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
    private static final String NOT_EXIST_INTERVIEW = "[ERROR] No such Interview";

    private final InterviewRepository interviewRepository;
    private final InterviewService interviewService;



    @GetMapping("/api/v1/interview/carousel")
    public CarouselDto carouselV1(Long uid) {
        return interviewService.loadCarousel(uid);
    }


    @PostMapping("/api/v1/interviewBookmark")
    public BookmarkInterviewResponse interviewBookmarkV1(@RequestBody @Valid BookmarkInterviewRequest request, @AuthenticationPrincipal String email) {

        Interview findInterview = interviewRepository.findById(request.getInterviewId())
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_INTERVIEW));

        Long interviewId = interviewService.createInterviewBookmark(request.getUserId(), findInterview);

        return new BookmarkInterviewResponse(interviewId);
    }

    @PostMapping("/api/v1/interviewLike")
    public LikeInterviewResponse interviewLikeV1(@RequestBody @Valid LikeInterviewRequest request) {
        Interview findInterview = interviewRepository.findById(request.getInterviewId())
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_INTERVIEW));

        Long interviewId = interviewService.createInterviewLike(request.getUserId(), findInterview);

        return new LikeInterviewResponse(interviewId);
    }

    @PostMapping("/api/v1/seniorRequest")
    public SeniorRequestResponse seniorRequestV1(@RequestBody @Valid SeniorRequestRequest request) {
        Interview findInterview = interviewRepository.findById(request.getInterviewId())
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_INTERVIEW));

        Long seniorRequestId = interviewService.createSeniorRequest(request.getUserId(), findInterview, request.getTitle(), request.getContent());

        return new SeniorRequestResponse(seniorRequestId);
    }

    @GetMapping("/api/v1/interviewListRecent")
    public Page<InterviewListDto> interviewListRecentV1(InterviewSearchCondition condition, Pageable pageable) {
        return interviewRepository.searchInterviewPageRecent(condition, pageable);
    }

    @GetMapping("/api/v1/interviewListRecommend")
    public Page<InterviewListDto> interviewListRecommendV1(InterviewSearchCondition condition, Pageable pageable) {
        return interviewRepository.searchInterviewPageRecommend(condition, pageable);
    }


    @GetMapping("/api/v1/interview")
    public LoadInterviewResponse<InterviewDetailDto> loadInterviewV1(@RequestBody @Valid LoadInterviewRequest request) {
        return new LoadInterviewResponse<>(LOADED_INTERVIEW_SIZE, interviewService.loadInterview(request.getUserId(), request.getInterviewId()));
    }

}
