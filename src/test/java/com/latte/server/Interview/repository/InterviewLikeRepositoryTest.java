package com.latte.server.Interview.repository;

import com.latte.server.interview.domain.Interview;
import com.latte.server.interview.repository.InterviewBookmarkRepository;
import com.latte.server.interview.repository.InterviewLikeRepository;
import com.latte.server.interview.repository.InterviewRepository;
import com.latte.server.interview.service.InterviewService;
import com.latte.server.user.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Donggun on 2021-08-05
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class InterviewLikeRepositoryTest {

    @Autowired
    InterviewService interviewService;

    @Autowired
    InterviewLikeRepository interviewLikeRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void 인터뷰_좋아요_갯수_카운트() {
        //given
        User user = createUser();
        Interview interview = Interview.createInterview(user, "test content", "test title");
        em.persist(interview);

        em.flush();
        em.clear();

        //when
        int likeCount = interviewLikeRepository.countByInterview(interview);

        //then
        assertThat(likeCount).isEqualTo(0);
    }

    @Test
    public void 인터뷰_좋아요_여부_판별() {
        //given
        User user = createUser();
        Interview interview = Interview.createInterview(user, "test content", "test title");
        em.persist(interview);

        em.flush();
        em.clear();

        //when
        int likeCount = interviewLikeRepository.countByInterviewAndAndUser(interview, user);

        //then
        assertThat(likeCount).isEqualTo(0);
    }

    @Test
    public void 인터뷰_좋아요_여부_판별_좋아요_상태() {
        //given
        User user = createUser();
        Interview interview = Interview.createInterview(user, "test content", "test title");
        em.persist(interview);

        interviewService.createInterviewLike(user.getId(), interview);

        em.flush();
        em.clear();

        //when
        int likeCount = interviewLikeRepository.countByInterviewAndAndUser(interview, user);

        //then
        assertThat(likeCount).isEqualTo(1);
    }


    private User createUser() {
        User user = User.createTestUser("userA", "test", "test@test.com", "test intro");
        em.persist(user);
        return user;
    }
}