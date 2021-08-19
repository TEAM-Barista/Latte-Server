package com.latte.server.post.service;

import com.latte.server.post.domain.Post;
import com.latte.server.post.repository.PostRepository;
import com.latte.server.user.domain.User;
import com.latte.server.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
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
    private static final String NOT_EXIST_TEXT = "[ERROR] Do not contain text";

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    /**
     * Post
     */
    @Transactional
    public Long post(Long userId, String postContent, String postTitle, String postCode) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_USER));
        if (!(hasText(postTitle) && hasText(postContent))) {
            throw new IllegalArgumentException(NOT_EXIST_TEXT);
        }

        Post post = Post.createPost(user, postContent, postTitle, postCode);

        postRepository.save(post);

        return post.getId();
    }

    public void update(Long postId, String postContent, String postTitle, String postCode, int isQna) {
        Post findPost = postRepository.findById(postId).get();
        findPost.changePost(postContent, postTitle, postCode, isQna);
    }

    public void delete(Long postId) {
        Post findPost = postRepository.findById(postId).get();
        findPost.deletePost();
    }
}
