package com.latte.server.post.repository;

import com.latte.server.post.service.PostService;
import com.latte.server.user.domain.User;
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
public class PostRepositoryTest {

    @Autowired
    EntityManager em;
    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;

    @Test
    public void 댓글_갯수_카운트() {
        //given
        User user = createUser();
        String postContent = "test content";
        String postTitle = "test title";
        String postCode = "#stdio.h";
        int isQna = 0;
        Long postId = postService.post(user.getId(), postContent, postTitle, postCode, isQna);

        //when
        int count = postRepository.countReplies(postId);

        //then
        Assertions.assertThat(count).isEqualTo(0);
    }

    private User createUser() {
        User user = User.createTestUser("userA", "test", "test@test.com", "test intro");
        em.persist(user);
        return user;
    }
}
