package com.latte.server.post.service;

import com.latte.server.interview.domain.Interview;
import com.latte.server.interview.domain.InterviewBookmark;
import com.latte.server.post.domain.Bookmark;
import com.latte.server.post.domain.Post;
import com.latte.server.post.dto.PostListDto;
import com.latte.server.post.repository.BookmarkRepository;
import com.latte.server.post.repository.PostRepository;
import com.latte.server.user.domain.User;
import com.latte.server.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
    private static final String NOT_EXIST_TEXT = "[ERROR] Do not contain text";
    private static final Long POST_BOOKMARK_DELETED = 0L;

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final BookmarkRepository bookmarkRepository;

    /**
     * Post
     */
    @Transactional
    public Long post(Long userId, String postContent, String postTitle, String postCode) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_USER));
        valifyText(postContent, postTitle);

        Post post = Post.createPost(user, postContent, postTitle, postCode);

        postRepository.save(post);

        return post.getId();
    }

    public void update(Long postId, String postContent, String postTitle, String postCode) {
        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_POST));
        valifyText(postContent, postTitle);

        findPost.changePost(postContent, postTitle, postCode);
    }

    private void valifyText(String postContent, String postTitle) {
        if (!(hasText(postTitle) && hasText(postContent))) {
            throw new IllegalArgumentException(NOT_EXIST_TEXT);
        }
    }

    public void delete(Long postId) {
        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_POST));

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

}
