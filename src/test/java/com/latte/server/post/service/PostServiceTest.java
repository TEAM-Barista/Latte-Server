package com.latte.server.post.service;

import com.latte.server.post.domain.Post;
import com.latte.server.user.domain.User;
import com.latte.server.post.repository.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

/**
 * Created by Donggun on 2021-08-05
 */

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
        int isQna = 0;

        //when
        Long postId = postService.post(user.getId(), postContent, postTitle, postCode, isQna);

        //then
        Post post = postRepository.findById(postId).get();
        assertThat(post.getPostContent()).isEqualTo("test content");
        assertThat(post.getPostTitle()).isEqualTo("test title");
        assertThat(post.getPostCode()).isEqualTo("#stdio.h");
        assertThat(post.getIsQna()).isEqualTo(0);
        assertThat(post.getIsDeleted()).isEqualTo(0);

    }

    @Test
    public void 포스트_추가_타이틀_공백() {
        //given
        String NOT_EXIST_TEXT = "[ERROR] Do not contain text";

        User user = createUser();
        String postContent = "test content";
        String postTitle = "";
        String postCode = "#stdio.h";
        int isQna = 0;

        assertThatIllegalArgumentException()
                .isThrownBy(() -> {
                    // when
                    postService.post(user.getId(), postContent, postTitle, postCode, isQna);
                })
                // then
                .withMessage(NOT_EXIST_TEXT);
    }

    @Test
    public void 포스트_추가_컨텐츠_공백() {
        //given
        String NOT_EXIST_TEXT = "[ERROR] Do not contain text";

        User user = createUser();
        String postContent = "";
        String postTitle = "test title";
        String postCode = "#stdio.h";
        int isQna = 0;

        assertThatIllegalArgumentException()
                .isThrownBy(() -> {
                    // when
                    postService.post(user.getId(), postContent, postTitle, postCode, isQna);
                })
                // then
                .withMessage(NOT_EXIST_TEXT);
    }

    @Test
    public void 포스트_추가_둘다_공백() {
        //given
        String NOT_EXIST_TEXT = "[ERROR] Do not contain text";

        User user = createUser();
        String postContent = "";
        String postTitle = "";
        String postCode = "#stdio.h";
        int isQna = 0;

        assertThatIllegalArgumentException()
                .isThrownBy(() -> {
                    // when
                    postService.post(user.getId(), postContent, postTitle, postCode, isQna);
                })
                // then
                .withMessage(NOT_EXIST_TEXT);
    }

    @Test
    public void 포스트_추가_띄어쓰기() {
        //given
        String NOT_EXIST_TEXT = "[ERROR] Do not contain text";

        User user = createUser();
        String postContent = " ";
        String postTitle = " ";
        String postCode = "#stdio.h";
        int isQna = 0;

        assertThatIllegalArgumentException()
                .isThrownBy(() -> {
                    // when
                    postService.post(user.getId(), postContent, postTitle, postCode, isQna);
                })
                // then
                .withMessage(NOT_EXIST_TEXT);
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
        Post post = postRepository.findById(postId).get();
        assertThat(post.getPostContent()).isEqualTo("update content");
        assertThat(post.getPostTitle()).isEqualTo("update title");
        assertThat(post.getPostCode()).isEqualTo("update code");
        assertThat(post.getIsQna()).isEqualTo(1);

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
        Post post = postRepository.findById(postId).get();
        assertThat(post.getIsDeleted()).isEqualTo(1);
    }

    private User createUser() {
        User user = User.createTestUser("userA", "test", "test@test.com", "test intro");
        em.persist(user);
        return user;
    }
}
