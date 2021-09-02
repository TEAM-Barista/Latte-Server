package com.latte.server.service;

import com.latte.server.post.domain.Post;
import com.latte.server.user.domain.User;
import com.latte.server.post.repository.PostRepository;
import com.latte.server.post.service.PostService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)
@SpringBootTest
@MockBean(JpaMetamodelMappingContext.class)
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
        int isQna = 0;

        //when
        Long postId = postService.post(user.getId(), postContent, postTitle, postCode, isQna);

        //then
        Post post = postRepository.findOne(postId);
        Assertions.assertThat(post.getPostContent()).isEqualTo("test content");
        Assertions.assertThat(post.getPostHit()).isEqualTo(0);
        Assertions.assertThat(post.getPostTitle()).isEqualTo("test title");
        Assertions.assertThat(post.getPostCode()).isEqualTo("#stdio.h");
        Assertions.assertThat(post.getIsQna()).isEqualTo(0);
        Assertions.assertThat(post.getIsDeleted()).isEqualTo(0);

    }

    @Test
    public void 포스트_수정() {
        //given
        User user = createUser();
        String postContent = "test content";
        String postTitle = "test title";
        String postCode = "#stdio.h";
        int isQna = 0;
        Long postId = postService.post(user.getId(), postContent, postTitle, postCode, isQna);

        String updateContent = "update content";
        String updataeTitle = "update title";
        String updateCode = "update code";
        int updateQna = 1;

        //when
        postService.update(postId, updateContent, updataeTitle, updateCode, updateQna);

        //then
        Post post = postRepository.findOne(postId);
        Assertions.assertThat(post.getPostContent()).isEqualTo("update content");
        Assertions.assertThat(post.getPostTitle()).isEqualTo("update title");
        Assertions.assertThat(post.getPostCode()).isEqualTo("update code");
        Assertions.assertThat(post.getIsQna()).isEqualTo(1);

    }

    @Test
    public void 포스트_삭제() {
        //given
        User user = createUser();
        String postContent = "test content";
        String postTitle = "test title";
        String postCode = "#stdio.h";
        int isQna = 0;
        Long postId = postService.post(user.getId(), postContent, postTitle, postCode, isQna);

        //when
        postService.delete(postId);

        //then
        Post post = postRepository.findOne(postId);
        Assertions.assertThat(post.getIsDeleted()).isEqualTo(1);
    }

    private User createUser() {
        User user = new User();
        user.setNickName("user1");
        user.setEmail("test@test.com");
        em.persist(user);
        return user;
    }
}
