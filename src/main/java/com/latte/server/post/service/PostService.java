package com.latte.server.post.service;

import com.latte.server.category.domain.Category;
import com.latte.server.category.repository.CategoryRepository;
import com.latte.server.post.domain.*;
import com.latte.server.post.dto.*;
import com.latte.server.post.dto.Response.CreatePostResponse;
import com.latte.server.post.repository.*;
import com.latte.server.user.domain.User;
import com.latte.server.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.util.StringUtils.*;

/**
 * Created by Donggun on 2021-08-05
 */

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private static final String NOT_EXIST_USER = "[ERROR] No such User";
    private static final String NOT_EXIST_POST = "[ERROR] No such Post";
    private static final String NOT_EXIST_REPLY = "[ERROR] No such Reply";
    private static final String NOT_EXIST_TEXT = "[ERROR] Do not contain text";
    private static final String NOT_EXIST_CATEGORY = "[ERROR] No such Category";
    private static final String NOT_EXIST_TAG = "[ERROR] No such tag";
    private static final Long POST_BOOKMARK_DELETED = 0L;
    private static final Long REPLY_LIKE_DELETED = 0L;
    private static final int POST_REPLY_NOT_DELETED = 0;

    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final BookmarkRepository bookmarkRepository;
    private final ReplyRepository replyRepository;
    private final ReplyLikeRepository replyLikeRepository;

    /**
     * Post
     */
    @Transactional
    public Long post(Long userId, String postContent, String postTitle, String postCode) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_USER));
        valifyText(postContent, postTitle);

        Post post = Post.createPost(user, postContent, postTitle, postCode);

        postRepository.save(post);

        return post.getId();
    }

    @Transactional
    public Long qna(Long userId, String postContent, String postTitle, String postCode) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_USER));
        valifyText(postContent, postTitle);

        Post post = Post.createQna(user, postContent, postTitle, postCode);

        postRepository.save(post);

        return post.getId();
    }

    @Transactional
    public void update(Long postId, String postContent, String postTitle, String postCode) {
        Post findPost = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_POST));
        valifyText(postContent, postTitle);

        findPost.changePost(postContent, postTitle, postCode);
    }

    private void valifyText(String postContent, String postTitle) {
        if (!(hasText(postTitle) && hasText(postContent))) {
            throw new IllegalArgumentException(NOT_EXIST_TEXT);
        }
    }

    private void valifyReply(String replyContent) {
        if (!(hasText(replyContent))) {
            throw new IllegalArgumentException(NOT_EXIST_TEXT);
        }
    }

    public void delete(Long postId) {
        Post findPost = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_POST));

        findPost.deletePost();
    }

    @Transactional
    public Long createPostBookmark(Long userId, Post post) {

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_USER));

        if (bookmarkRepository.findByPost(post) == null) {
            Bookmark bookmark = Bookmark.createBookmark(user, post);
            bookmarkRepository.save(bookmark);
            return bookmark.getId();
        }

        bookmarkRepository.delete(bookmarkRepository.findByPost(post));
        return POST_BOOKMARK_DELETED;
    }

    public PostDetailDto loadPost(String email, Long postId) {
        User findUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_USER));

        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_POST));

        Long replyCount = replyRepository.countByPostAndIsDeleted(findPost, POST_REPLY_NOT_DELETED);
        Long bookmarkCount = bookmarkRepository.countByPost(findPost);
        Long isBookmarked = bookmarkRepository.countByPostAndUser(findPost, findUser);

        return new PostDetailDto(findPost, replyCount, bookmarkCount, isBookmarked);
    }

    @Transactional
    public Long reply(Post post, Long userId, String replyContent) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_USER));
        valifyReply(replyContent);

        Reply newReply = Reply.createNewReply(user, post, replyContent, POST_REPLY_NOT_DELETED);

        replyRepository.save(newReply);

        return newReply.getId();
    }


    @Transactional
    public Long createReplyLike(Long userId, Reply reply) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_USER));

        if (replyLikeRepository.findByReplyAndUser(reply, user) == null) {
            ReplyLike replyLike = ReplyLike.createReplyLike(reply, user);
            replyLikeRepository.save(replyLike);
            return replyLike.getId();
        }

        replyLikeRepository.delete(replyLikeRepository.findByReply(reply));
        return REPLY_LIKE_DELETED;
    }

    @Transactional
    public void replyUpdate(Long replyId, String replyContent) {
        Reply findReply = replyRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_REPLY));
        valifyReply(replyContent);

        findReply.changeReply(replyContent);
    }

    public void replyDelete(Long replyId) {
        Reply findReply = replyRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_REPLY));

        findReply.deleteReply();
    }

    @Transactional
    public Long updatePostTag(Long postId, List<Long> categoryIds) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_POST));

        post.clearPostTag();

        for (Long categoryId : categoryIds) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_CATEGORY));

            Tag tag = Tag.createTag(post, category);
            post.addPostTag(tag);
            tagRepository.save(tag);
        }
        return postId;
    }

    public Page<PostListDto> searchRepositoryPostPage(PostSearchCondition condition, Pageable pageable) {
        return postRepository.searchPostPage(condition, pageable);
    }

    public Page<PostListDto> searchRepositoryPostListPopular(PostSearchCondition condition, Pageable pageable) {
        return postRepository.searchPostPagePopular(condition, pageable);
    }

    public Page<PostListDto> searchRepositoryPostListRecent(PostSearchCondition condition, Pageable pageable) {
        return postRepository.searchPostPageRecent(condition, pageable);
    }

    public Page<ReplyDto> searchRepositoryReplyPageRecent(ReplySearchCondition condition, Pageable pageable) {
        return postRepository.searchReplyPageRecent(condition, pageable);
    }

    public Page<ReplyDto> searchRepositoryReplyPageOld(ReplySearchCondition condition, Pageable pageable) {
        return postRepository.searchReplyPageOld(condition, pageable);
    }

    public Long bookmarkPost(String email, Long postId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_USER));

        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_POST));

        Long bookmarkedPostId = createPostBookmark(user.getId(), findPost);

        return bookmarkedPostId;
    }

    public Long writePost(String email, String postContent, String postTitle, String postCode, List<Long> postTags) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_USER));

        Long postId = post(user.getId(), postContent, postTitle, postCode);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_POST));

        List<Long> postTagList = postTags;
        for (Long postTag : postTagList) {
            Tag tag = tagRepository.findById(postTag)
                    .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_TAG));
            post.addPostTag(tag);
        }

        return postId;
    }

    public Long writeQna(String email, String postContent, String postTitle, String postCode, List<Long> postTags) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_USER));

        Long postId = qna(user.getId(), postContent, postTitle, postCode);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_POST));

        List<Long> postTagList = postTags;
        for (Long postTag : postTagList) {
            Tag tag = tagRepository.findById(postTag)
                    .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_TAG));
            post.addPostTag(tag);
        }

        return postId;
    }

    public Long writeReply(String email, Long postId, String replyContent) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_USER));

        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_POST));

        Long replyId = reply(findPost, user.getId(), replyContent);

        return replyId;
    }

    public Long likeReply(String email, Long replyId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_USER));

        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_REPLY));

        Long replyLikeId = createReplyLike(user.getId(), reply);

        return replyLikeId;
    }
}
