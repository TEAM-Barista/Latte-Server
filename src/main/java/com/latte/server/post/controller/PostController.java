package com.latte.server.post.controller;

import com.latte.server.post.domain.Post;
import com.latte.server.post.domain.Tag;
import com.latte.server.post.dto.PostDto;
import com.latte.server.post.dto.PostListDto;
import com.latte.server.post.repository.PostRepository;
import com.latte.server.post.repository.TagRepository;
import com.latte.server.post.service.PostService;
import com.latte.server.post.service.TagService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PostController {
    private static final String NOT_EXIST_POST = "[ERROR] No such Post";
    private static final String NOT_EXIST_TAG = "[ERROR] No such tag";

    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    private final PostService postService;
    private final TagService tagService;


    @GetMapping("/api/v1/postList")
    public Result postListV1() {
        List<Post> posts = postRepository.findAll();
        List<PostListDto> result = posts.stream()
                .map(p -> new PostListDto(p, postRepository.countReplies(p.getId())))
                .collect(Collectors.toList());

        return new Result(result.size(), result);
    }

    @PostMapping("/api/v1/post")
    public Long postV1(@RequestBody @Valid CreatePostRequest request) {

        Long postId = postService.post(request.userId, request.postContent, request.postTitle, request.postCode);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_POST));

        List<Long> postTags = request.postTags;
        for (Long postTag : postTags) {
            Tag tag = tagRepository.findById(postTag)
                    .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_TAG));
            post.addPostTag(tag);
        }

        return postId;
    }

    @Data
    static class CreatePostRequest {
        private Long userId;
        private String postTitle;
        private String postContent;
        private String postCode;
        private List<Long> postTags;
    }

    // data로 반환
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }
}
