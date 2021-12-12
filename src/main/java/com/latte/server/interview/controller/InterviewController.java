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
import org.springframework.web.bind.annotation.*;

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


    @PostMapping("/api/v1/interview/bookmark")
    public BookmarkInterviewResponse interviewBookmarkV1(@RequestBody @Valid BookmarkInterviewRequest request, @AuthenticationPrincipal String email) {
        Long interviewBookmarkId = interviewService.bookmarkInterview(request.getInterviewId(), email);

        return new BookmarkInterviewResponse(interviewBookmarkId);
    }

    @PostMapping("/api/v1/interview/like")
    public LikeInterviewResponse interviewLikeV1(@RequestBody @Valid LikeInterviewRequest request, @AuthenticationPrincipal String email) {
        Long interviewLikeId = interviewService.likeInterview(request.getInterviewId(), email);

        return new LikeInterviewResponse(interviewLikeId);
    }

    @PostMapping("/api/v1/interview/senior-request")
    public SeniorRequestResponse seniorRequestV1(@RequestBody @Valid SeniorRequestRequest request, @AuthenticationPrincipal String email) {
        Long seniorRequestId = interviewService.requestSenior(request.getInterviewId(), request.getTitle(), request.getContent(), email);

        return new SeniorRequestResponse(seniorRequestId);
    }

    @GetMapping("/api/v1/interview")
    public Page<InterviewListDto> interviewListV1(@RequestParam(name = "recommend", defaultValue = "false") Boolean isRecommend, InterviewSearchCondition condition, Pageable pageable, @AuthenticationPrincipal String email) {
        if (isRecommend == true) {
            Page<InterviewListDto> result = interviewService.searchRepositoryInterviewPageRecommend(condition, pageable, email);
            return result;
        }
        Page<InterviewListDto> result = interviewService.searchRepositoryInterviewPageRecent(condition, pageable, email);

        return result;
    }

    @GetMapping("/api/v1/interview/search")
    public Page<InterviewListDto> searchInterviewsV1(InterviewSearchCondition condition, Pageable pageable, @AuthenticationPrincipal String email) {
        Page<InterviewListDto> result = interviewService.searchRepositoryInterviewPage(condition, pageable, email);
        return result;
    }

    @GetMapping("/api/v1/interview/{interviewId}")
    public LoadInterviewResponse<InterviewDetailDto> loadInterviewV1(@PathVariable Long interviewId, @AuthenticationPrincipal String email) {
        return new LoadInterviewResponse<>(LOADED_INTERVIEW_SIZE, interviewService.loadInterview(email, interviewId));
    }

}
