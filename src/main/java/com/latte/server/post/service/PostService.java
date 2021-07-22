package com.latte.server.post.service;

import com.latte.server.post.domain.Post;
import com.latte.server.post.repository.PostRepository;
import com.latte.server.user.domain.User;
import com.latte.server.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    /**
     * Post
     */
    @Transactional
    public Long post(Long userId, String postContent, String postTitle, String postCode, int isQna) {
        int basePostHit = 0;

        // 엔티티 조회
        User user = userRepository.findOne(userId);

        Post post = Post.createPost(user, postContent, basePostHit, postTitle, postCode, isQna);

        postRepository.save(post);

        return post.getId();
    }

    public void update(Long postId, String postContent, String postTitle, String postCode, int isQna) {
        Post findPost = postRepository.findOne(postId);
        findPost.setPostContent(postContent);
        findPost.setPostTitle(postTitle);
        findPost.setPostCode(postCode);
        findPost.setIsQna(isQna);
    }

    public void delete(Long postId) {
        Post findPost = postRepository.findOne(postId);
        findPost.setIsDeleted(1);
    }
}
