package com.latte.server.post.service;

import com.latte.server.category.domain.Category;
import com.latte.server.category.repository.CategoryRepository;
import com.latte.server.interview.domain.Interview;
import com.latte.server.interview.domain.InterviewBookmark;
import com.latte.server.post.domain.*;
import com.latte.server.post.dto.PostDetailDto;
import com.latte.server.post.dto.PostListDto;
import com.latte.server.post.repository.*;
import com.latte.server.user.domain.User;
import com.latte.server.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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

    public PostDetailDto loadPost(Long userId, Long postId) {

        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_POST));
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_USER));


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
}
