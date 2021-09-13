package com.latte.server.interview.controller;

import com.latte.server.interview.domain.Interview;
import com.latte.server.interview.domain.InterviewTag;
import com.latte.server.interview.dto.CarouselDto;
import com.latte.server.interview.dto.InterviewListDto;
import com.latte.server.interview.dto.InterviewSearchCondition;
import com.latte.server.interview.repository.InterviewBookmarkRepository;
import com.latte.server.interview.repository.InterviewLikeRepository;
import com.latte.server.interview.repository.InterviewRepository;
import com.latte.server.interview.repository.InterviewTagRepository;
import com.latte.server.interview.service.InterviewService;
import com.latte.server.post.controller.PostController;
import com.latte.server.post.domain.Post;
import com.latte.server.user.domain.User;
import com.latte.server.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Donggun on 2021-08-15
 */

@RestController
@RequiredArgsConstructor
public class InterviewController {
    private static final int CAROUSEL_SIZE = 1;
    private static final String NOT_EXIST_INTERVIEW = "[ERROR] No such Interview";

    private final InterviewRepository interviewRepository;

    private final InterviewService interviewService;


    @GetMapping("/api/v1/interview/carousel")
    public CarouselResult carouselV1(Long uid) {
        return new CarouselResult(CAROUSEL_SIZE, interviewService.loadCarousel(uid));
    }


    @PostMapping("/api/v1/interviewBookmark")
    public BookmarkInterviewResponse interviewBookmarkV1(@RequestBody @Valid BookmarkInterviewRequest request) {
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

    @Data
    static class BookmarkInterviewRequest {
        private Long interviewId;
        private Long userId;
    }

    @Data
    @AllArgsConstructor
    static class BookmarkInterviewResponse {
        private Long interviewId;
    }

    @Data
    static class LikeInterviewRequest {
        private Long interviewId;
        private Long userId;
    }

    @Data
    @AllArgsConstructor
    static class LikeInterviewResponse {
        private Long interviewId;
    }

    @Data
    static class SeniorRequestRequest {
        private Long interviewId;
        private Long userId;
        private String title;
        private String content;
    }

    @Data
    @AllArgsConstructor
    static class SeniorRequestResponse {
        private Long seniorRequestId;
    }


    @Data
    @AllArgsConstructor
    static class CarouselResult<T> {
        private int size;
        private T data;
    }

}
