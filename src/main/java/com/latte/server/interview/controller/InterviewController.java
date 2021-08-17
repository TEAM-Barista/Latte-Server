package com.latte.server.interview.controller;

import com.latte.server.interview.domain.Interview;
import com.latte.server.interview.domain.InterviewTag;
import com.latte.server.interview.dto.CarouselDto;
import com.latte.server.interview.repository.InterviewBookmarkRepository;
import com.latte.server.interview.repository.InterviewLikeRepository;
import com.latte.server.interview.repository.InterviewRepository;
import com.latte.server.interview.repository.InterviewTagRepository;
import com.latte.server.interview.service.InterviewService;
import com.latte.server.user.domain.User;
import com.latte.server.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private static final String NOT_EXIST_USER = "[ERROR] No such User";

    private final UserRepository userRepository;
    private final InterviewRepository interviewRepository;
    private final InterviewBookmarkRepository interviewBookmarkRepository;
    private final InterviewLikeRepository interviewLikeRepository;
    private final InterviewTagRepository interviewTagRepository;

    private final InterviewService interviewService;


    @GetMapping("/v1/interview/carousel")
    public CarouselResult carouselV1(Long uid) {
        User user = userRepository.findById(uid).orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_USER));

        Interview interviewByCreatedDate = interviewRepository.findInterviewByCreatedDate();
        Long interviewId = interviewByCreatedDate.getId();
        String interviewTitle = interviewByCreatedDate.getInterviewTitle();
        String interviewContent = interviewByCreatedDate.getInterviewContent();
        int interviewLikeCount = interviewLikeRepository.countByInterview(interviewByCreatedDate);
        int interviewBookmarkCount = interviewBookmarkRepository.countByInterview(interviewByCreatedDate);
        int isLiked = interviewLikeRepository.countByInterviewAndUser(interviewByCreatedDate, user);
        int isBookmarked = interviewBookmarkRepository.countByInterviewAndUser(interviewByCreatedDate, user);

        List<InterviewTag> interviewTags = interviewTagRepository.findByInterview(interviewByCreatedDate);

        List<String> categories = new ArrayList<>();

        for (InterviewTag interviewTag : interviewTags) {
            categories.add(interviewTag.getCategory().getCategory());
        }

        CarouselDto carousel = new CarouselDto(interviewId, interviewTitle, interviewContent, interviewLikeCount, interviewBookmarkCount, isLiked, isBookmarked);

        return new CarouselResult(CAROUSEL_SIZE, carousel, categories);
    }

    @Data
    @AllArgsConstructor
    static class CarouselResult<T> {
        private int size;
        private T data;
        private List<String> tags;
    }

}
