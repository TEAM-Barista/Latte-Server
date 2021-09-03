package com.latte.server.post.repository;

import com.latte.server.post.service.PostService;
import com.latte.server.user.domain.User;
import com.latte.server.user.domain.UserRole;
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
        Long postId = postService.post(user.getId(), postContent, postTitle, postCode);

        //when
        int count = postRepository.countReplies(postId);

        //then
        Assertions.assertThat(count).isEqualTo(0);
    }

    private User createUser() {
        User user = new User().builder()
                .userRole(UserRole.ROLE_ADMIN)
                .email("test@test.com")
                .nickName("userA")
                .password("$2a$10$GTHxsIH/0g0j/cv9MF9Iu.7mX.KMJvuGpDn/kMtBxIftCTgdsoLD6")
                .accessNotify(false)
                .build();
        em.persist(user);
        return user;
    }
}
