package com.latte.server.common.service;

import com.latte.server.common.domain.Post;
import com.latte.server.common.domain.User;
import com.latte.server.common.repository.PostRepository;
import com.latte.server.common.repository.UserRepository;
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
    public Long post(Long userId, String postContent, String postTitle, String postCode) {
        int basePostHit = 0;

        // 엔티티 조회
        User user = userRepository.findOne(userId);

        Post post = Post.createPost(user, postContent, basePostHit, postTitle, postCode);

        postRepository.save(post);

        return post.getId();
    }

}
