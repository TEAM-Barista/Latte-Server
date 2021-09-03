package com.latte.server.common.util;

import com.latte.server.category.domain.Category;
import com.latte.server.interview.domain.Interview;
import com.latte.server.interview.domain.InterviewBookmark;
import com.latte.server.interview.domain.InterviewLike;
import com.latte.server.interview.domain.InterviewTag;
import com.latte.server.post.domain.Post;
import com.latte.server.post.domain.Reply;
import com.latte.server.post.domain.Tag;
import com.latte.server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

/**
 * Created by Donggun on 2021-08-05
 */

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
            User user = createUser("동건", "donggun", "I'm donggun", "donggun@google.com");
            em.persist(user);

            Post post1 = createPost(user, "배가 고프다. 많이 고프다.", "배고파요", null);
            em.persist(post1);

            Post post2 = createPost(user, "post content", "post title", "post code");
            em.persist(post2);

            Category category = Category.createCategory("category test", "kind test");
            em.persist(category);

            Interview interview = Interview.createInterview(user, "test interview", "test interview title");
            em.persist(interview);

            InterviewBookmark interviewBookmark = InterviewBookmark.createInterviewBookmark(interview, user);
            em.persist(interviewBookmark);

            InterviewLike interviewLike = InterviewLike.createInterviewLike(interview, user);
            em.persist(interviewLike);

            InterviewTag interviewTag = InterviewTag.createInterviewTag(interview, category);
            em.persist(interviewTag);

            Tag tag = Tag.createTag(post1, category);
            em.persist(tag);

            Reply reply = Reply.createNewReply(user, post1, "test reply", 0);
            em.persist(reply);

        }

        private Post createPost(User user, String postContent, String postTitle, String postCode) {
            Post post = Post.createPost(user, postContent, postTitle, postCode);
            return post;
        }

        private User createUser(String userName, String userId, String intro, String email) {
            return User.createTestUser(userName, userId, email, intro);
        }


    }

}