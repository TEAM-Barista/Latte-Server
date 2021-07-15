package com.latte.server.service;

import com.latte.server.common.domain.Post;
import com.latte.server.common.domain.User;
import com.latte.server.common.repository.PostRepository;
import com.latte.server.common.service.PostService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PostServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;

    @Test
    public void 포스트_추가() {
        //given
        User user = createUser();
        String postContent = "test content";
        String postTitle = "test title";
        String postCode = "#stdio.h";

        //when
        System.out.println("user.getUserId() = " + user.getUserId());
        Long postId = postService.post(user.getId(), postContent, postTitle, postCode);

        //then
        Post post = postRepository.findOne(postId);
        Assertions.assertThat(post.getPostContent()).isEqualTo("test content");
        Assertions.assertThat(post.getPostHit()).isEqualTo(0);
        Assertions.assertThat(post.getPostTitle()).isEqualTo("test title");
        Assertions.assertThat(post.getPostCode()).isEqualTo("#stdio.h");


    }

    private User createUser() {
        User user = new User();
        user.setUserName("user1");
        user.setUserId("test");
        user.setEmail("test@test.com");
        user.setIntro("test intro");
        em.persist(user);
        return user;
    }
}
