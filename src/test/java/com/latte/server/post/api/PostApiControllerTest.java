package com.latte.server.post.api;


import com.latte.server.post.domain.Post;
import com.latte.server.post.dto.PostListDto;
import com.latte.server.post.repository.PostRepository;
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
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PostApiControllerTest {

    @Autowired
    EntityManager em;
    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;

    @Test
    public void 포스트_목록_불러오기() {

        //given
        User user = createUser();
        String postContent = "test content";
        String postTitle = "test title";
        String postCode = "#stdio.h";
        int isQna = 0;
        postService.post(user.getId(), postContent, postTitle, postCode, isQna);
        

        //when
        List<Post> posts = postRepository.findAll();
        List<PostListDto> result = posts.stream()
                .map(p -> new PostListDto(p, postRepository.countReplies(p.getId()), postRepository.countPostLikes(p.getId())))
                .collect(Collectors.toList());

        //then
        System.out.println("result.size() = " + result.size());
        Assertions.assertThat(result.size()).isNotEqualTo(0);
        
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
