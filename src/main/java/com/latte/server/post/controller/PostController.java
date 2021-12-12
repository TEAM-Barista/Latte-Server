package com.latte.server.post.controller;

import com.latte.server.interview.dto.InterviewListDto;
import com.latte.server.interview.dto.request.LoadInterviewRequest;
import com.latte.server.interview.dto.response.LoadInterviewResponse;
import com.latte.server.post.domain.Post;
import com.latte.server.post.domain.Reply;
import com.latte.server.post.domain.Tag;
import com.latte.server.post.dto.*;
import com.latte.server.post.dto.Request.*;
import com.latte.server.post.dto.Response.*;
import com.latte.server.post.repository.PostRepository;
import com.latte.server.post.repository.ReplyRepository;
import com.latte.server.post.repository.TagRepository;
import com.latte.server.post.service.PostService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {
    private static final int LOADED_POST_SIZE = 1;
    private static final int POST_LIST_SIZE = 1;

    private final PostService postService;

    @GetMapping("/api/v1/post")
    public Page<PostListDto> postListV1(@RequestParam("popular") boolean isPopular, PostSearchCondition condition, Pageable pageable, @AuthenticationPrincipal String email) {
        if (isPopular == true) {
            Page<PostListDto> result = postService.searchRepositoryPostListPopular(condition, pageable, email);
            return result;
        }
        Page<PostListDto> result = postService.searchRepositoryPostListRecent(condition, pageable, email);
        return result;
    }

    @GetMapping("/api/v1/post/search")
    public Page<PostListDto> searchPostsV1(PostSearchCondition condition, Pageable pageable, @AuthenticationPrincipal String email) {
        Page<PostListDto> result = postService.searchRepositoryPostPage(condition, pageable, email);

        return result;
    }

    @GetMapping("/api/v1/post/bookmarked")
    public Page<PostListDto> latestBookmarkedPostsV1(PostSearchCondition condition, Pageable pageable, @AuthenticationPrincipal String email) {
        Page<PostListDto> result = postService.searchRepositoryBookmarkedPostPageRecent(condition, pageable, email);

        return result;
    }

    @GetMapping("/api/v1/post/my-recent")
    public Page<PostListDto> latestMyPostsV1(PostSearchCondition condition, Pageable pageable, @AuthenticationPrincipal String email) {
        Page<PostListDto> result = postService.searchRepositoryMyPostPageRecent(condition, pageable, email);

        return result;
    }

    @GetMapping("/api/v1/post/reply")
    public Page<ReplyDto> replyListV1(@RequestParam("recent") boolean isRecent, ReplySearchCondition condition, Pageable pageable, @AuthenticationPrincipal String email) {
        if (isRecent == true) {
            Page<ReplyDto> result = postService.searchRepositoryReplyPageRecent(condition, pageable, email);
            return result;
        }
        Page<ReplyDto> result = postService.searchRepositoryReplyPageOld(condition, pageable, email);
        return result;
    }

    @PostMapping("/api/v1/post/bookmark")
    public BookmarkPostResponse postBookmarkV1(@RequestBody @Valid BookmarkPostRequest request, @AuthenticationPrincipal String email) {
        Long bookmarkedPostId = postService.bookmarkPost(email, request.getPostId());

        return new BookmarkPostResponse(bookmarkedPostId);
    }

    @GetMapping("/api/v1/post/{postId}")
    public LoadPostResponse<PostDetailDto> loadPostV1(@PathVariable("postId") Long postId, @AuthenticationPrincipal String email) {
        return new LoadPostResponse<>(LOADED_POST_SIZE, postService.loadPost(email, postId));
    }

    @PostMapping("/api/v1/post")
    public CreatePostResponse postV1(@RequestBody @Valid CreatePostRequest request, @AuthenticationPrincipal String email) {
        Long postId = postService.writePost(email, request.getPostContent(), request.getPostTitle(), request.getPostCode(), request.getPostTags());

        return new CreatePostResponse(postId);
    }

    @PostMapping("/api/v1/post/qna")
    public CreatePostResponse qnaV1(@RequestBody @Valid CreatePostRequest request, @AuthenticationPrincipal String email) {
        Long postId = postService.writeQna(email, request.getPostContent(), request.getPostTitle(), request.getPostCode(), request.getPostTags());

        return new CreatePostResponse(postId);
    }

    @PostMapping("/api/v1/post/reply")
    public CreateReplyResponse replyV1(@RequestBody @Valid CreateReplyRequest request, @AuthenticationPrincipal String email) {
        Long replyId = postService.writeReply(email, request.getPostId(), request.getReplyContent());

        return new CreateReplyResponse(request.getPostId(), replyId);
    }

    @PostMapping("/api/v1/post/reply-like")
    public ReplyLikeResponse replyLikeV1(@RequestBody @Valid ReplyLikeRequest request, @AuthenticationPrincipal String email) {
        Long replyLikeId = postService.likeReply(email, request.getReplyId());

        return new ReplyLikeResponse(request.getReplyId(), replyLikeId);
    }

    @DeleteMapping("/api/v1/post/reply/{replyId}")
    public DeleteReplyResponse deleteReplyV1(@PathVariable("replyId") Long replyId) {
        postService.replyDelete(replyId);
        return new DeleteReplyResponse(replyId);
    }

    @PutMapping("/api/v1/post/reply/{replyId}")
    public UpdateReplyResponse updateReplyV1(@PathVariable("replyId") Long replyId, @RequestBody @Valid UpdateReplyRequest request) {
        postService.replyUpdate(replyId, request.getReplyContent());
        return new UpdateReplyResponse(replyId);
    }

    @DeleteMapping("/api/v1/post/{postId}")
    public DeletePostResponse deletePostV1(@PathVariable("postId") Long postId) {
        postService.delete(postId);
        return new DeletePostResponse(postId);
    }

    @PutMapping("/api/v1/post/{postId}")
    public UpdatePostResponse updatePostV1(@PathVariable("postId") Long postId, @RequestBody @Valid UpdatePostRequest request) {
        postService.update(postId, request.getPostContent(), request.getPostTitle(), request.getPostCode());
        postService.updatePostTag(postId, request.getTagIds());
        return new UpdatePostResponse(postId);
    }

    @GetMapping("/api/v1/post/bookmark-one")
    public LatestBookmarkResponse<PostListDto> latestOnePostBookmarkV1(@AuthenticationPrincipal String email) {
        return new LatestBookmarkResponse<>(POST_LIST_SIZE, postService.latestBookmark(email));
    }
    @GetMapping("/api/v1/post/my-post-one")
    public LatestWritingResponse<PostListDto> latestOnePostWritingV1(@AuthenticationPrincipal String email) {
        return new LatestWritingResponse<>(POST_LIST_SIZE, postService.latestWriting(email));
    }

}
