package com.latte.server.common.util;

import com.latte.server.post.domain.Post;
import com.latte.server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    // 실행 될 때 넣어주게 된다.
    @PostConstruct
    public void init() {
        initService.dbInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;
        public void dbInit1() {
            User user = createUser("동건", "donggun", "I'm donggun", 1011111111, "local");
            em.persist(user);

            Post post1 = createPost(user, "배가 고프다. 많이 고프다.", "배고파요", null, 10, 0);
            em.persist(post1);

            Post post2 = createPost(user, "post content", "post title", "post code", 21, 0);
            em.persist(post2);

        }

        private Post createPost(User user, String postContent, String postTitle, String postCode, int postHit, int isQna) {
            Post post = Post.createPost(user, postContent, postHit, postTitle, postCode, isQna);

            return post;
        }

        private User createUser(String userName, String userId, String intro, int phone, String loginBy) {
            User user = new User();
            user.setUserName(userName);
            user.setUserId(userId);
            user.setIntro(intro);
            user.setPhone(phone);
            user.setLoginBy(loginBy);
            return user;
        }

    }

}