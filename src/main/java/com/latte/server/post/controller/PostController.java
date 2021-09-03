package com.latte.server.post.controller;

import com.latte.server.post.domain.Post;
import com.latte.server.post.domain.Reply;
import com.latte.server.post.domain.Tag;
import com.latte.server.post.dto.*;
import com.latte.server.post.repository.PostRepository;
import com.latte.server.post.repository.ReplyRepository;
import com.latte.server.post.repository.TagRepository;
import com.latte.server.post.service.PostService;
import com.latte.server.post.service.TagService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PostController {
    private static final String NOT_EXIST_POST = "[ERROR] No such Post";
    private static final String NOT_EXIST_TAG = "[ERROR] No such tag";
    private static final String NOT_EXIST_REPLY = "[ERROR] No such reply";
    private static final int LOADED_POST_SIZE = 1;

    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final ReplyRepository replyRepository;

    private final PostService postService;


    @GetMapping("/api/v1/postList")
    public Page<PostListDto> postListV1(PostSearchCondition condition, Pageable pageable) {
        return postRepository.searchPostPage(condition, pageable);
    }

    @GetMapping("/api/v1/postListPopular")
    public Page<PostListDto> postListPopularV1(PostSearchCondition condition, Pageable pageable) {
        return postRepository.searchPostPagePopular(condition, pageable);
    }

    @GetMapping("/api/v1/postListRecent")
    public Page<PostListDto> postListRecentV1(PostSearchCondition condition, Pageable pageable) {
        return postRepository.searchPostPageRecent(condition, pageable);
    }

    @GetMapping("/api/v1/replyListRecent")
    public Page<ReplyDto> replyListRecentV1(ReplySearchCondition condition, Pageable pageable) {
        return postRepository.searchReplyPageRecent(condition, pageable);
    }

    @GetMapping("/api/v1/replyListOld")
    public Page<ReplyDto> replyListOldV1(ReplySearchCondition condition, Pageable pageable) {
        return postRepository.searchReplyPageOld(condition, pageable);
    }

    @PostMapping("/api/v1/postBookmark")
    public BookmarkPostResponse postBookmarkV1(@RequestBody @Valid BookmarkPostRequest request) {
        Post findPost = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_POST));

        Long bookmarkedPostId = postService.createPostBookmark(request.getUserId(), findPost);
        return new BookmarkPostResponse(bookmarkedPostId);
    }

    @GetMapping("/api/v1/post")
    public Result<PostDetailDto> loadPostV1(Long userId, Long postId) {
        return new Result(LOADED_POST_SIZE, postService.loadPost(userId, postId));
    }

    @PostMapping("/api/v1/post")
    public CreatePostResponse postV1(@RequestBody @Valid CreatePostRequest request) {

        Long postId = postService.post(request.getUserId(), request.getPostContent(), request.getPostTitle(), request.getPostCode());
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_POST));

        List<Long> postTags = request.getPostTags();
        for (Long postTag : postTags) {
            Tag tag = tagRepository.findById(postTag)
                    .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_TAG));
            post.addPostTag(tag);
        }

        return new CreatePostResponse(postId);
    }

    @PostMapping("/api/v1/reply")
    public CreateReplyResponse replyV1(@RequestBody @Valid CreateReplyRequest request) {

        Post findPost = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_POST));

        Long replyId = postService.reply(findPost, request.getUserId(), request.getPostContent());

        return new CreateReplyResponse(request.getUserId(), request.getPostId(), replyId);
    }

    @PostMapping("/api/v1/replyLike")
    public ReplyLikeResponse replyLikeV1(@RequestBody @Valid ReplyLikeRequest request) {
        Reply reply = replyRepository.findById(request.getReplyId())
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_REPLY));
        Long replyLikeId = postService.createReplyLike(request.getUserId(), reply);

        return new ReplyLikeResponse(reply.getId(), replyLikeId);
    }

    @PostMapping("/api/v1/deleteReply")
    public DeleteReplyResponse deleteReplyV1(@RequestBody @Valid DeleteReplyRequest request) {
        postService.replyDelete(request.getReplyId());
        return new DeleteReplyResponse(request.getReplyId());
    }

    @PutMapping("/api/v1/updateReply")
    public UpdateReplyResponse updateReplyV1(@PathVariable("replyId") Long replyId, @RequestBody @Valid UpdateReplyRequest request) {
        postService.replyUpdate(replyId, request.getReplyContent());
        return new UpdateReplyResponse(replyId);
    }

    @PostMapping("/api/v1/deletePost")
    public DeletePostResponse deletePostV1(@RequestBody @Valid DeletePostRequest request) {
        postService.delete(request.getPostId());
        return new DeletePostResponse(request.getPostId());
    }

    @PutMapping("/api/v1/updatePost")
    public UpdatePostResponse updatePostV1(@PathVariable("postId") Long postId, @RequestBody @Valid UpdatePostRequest request) {
        postService.update(postId, request.postContent, request.postTitle, request.postCode);
        return new UpdatePostResponse(postId);
    }

    @Data
    static class UpdatePostRequest {
        private String postTitle;
        private String postContent;
        private String postCode;
    }

    @Data
    @AllArgsConstructor
    static class UpdatePostResponse {
        private Long postId;
    }

    @Data
    static class DeletePostRequest {
        private Long postId;
    }

    @Data
    @AllArgsConstructor
    static class DeletePostResponse {
        private Long postId;
    }

    @Data
    static class UpdateReplyRequest {
        private String replyContent;
    }

    @Data
    @AllArgsConstructor
    static class UpdateReplyResponse {
        private Long replyId;
    }

    @Data
    static class DeleteReplyRequest {
        private Long replyId;
    }

    @Data
    @AllArgsConstructor
    static class DeleteReplyResponse {
        private Long replyId;
    }

    @Data
    static class BookmarkPostRequest {
        private Long postId;
        private Long userId;
    }

    @Data
    @AllArgsConstructor
    static class BookmarkPostResponse {
        private Long postId;
    }

    @Data
    static class ReplyLikeRequest {
        private Long replyId;
        private Long userId;
    }

    @Data
    @AllArgsConstructor
    static class ReplyLikeResponse {
        private Long replyId;
        private Long replyLikeId;
    }

    @Data
    static class CreatePostRequest {
        private Long userId;
        private String postTitle;
        private String postContent;
        private String postCode;
        private List<Long> postTags;
    }

    @Data
    @AllArgsConstructor
    static class CreatePostResponse {
        private Long postId;
    }

    @Data
    static class CreateReplyRequest {
        private Long userId;
        private Long postId;
        private String postContent;
    }

    @Data
    @AllArgsConstructor
    static class CreateReplyResponse {
        private Long userId;
        private Long postId;
        private Long replyId;
    }

    // data로 반환
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }
}
