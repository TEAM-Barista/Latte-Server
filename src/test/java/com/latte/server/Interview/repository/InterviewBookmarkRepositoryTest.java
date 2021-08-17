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
public class InterviewBookmarkRepositoryTest {

    @Autowired
    InterviewService interviewService;

    @Autowired
    InterviewBookmarkRepository interviewBookMarkRepository;
    @PersistenceContext
    EntityManager em;


    @Test
    public void 인터뷰_북마크_갯수_카운트() {
        //given
        User user = createUser();
        Interview interview = Interview.createInterview(user, "test content", "test title");
        em.persist(interview);

        em.flush();
        em.clear();

        //when
        int bookMarkCount = interviewBookMarkRepository.countByInterview(interview);

        //then
        assertThat(bookMarkCount).isEqualTo(0);
    }

    @Test
    public void 인터뷰_북마크_여부_판별() {
        //given
        User user = createUser();
        Interview interview = Interview.createInterview(user, "test content", "test title");
        em.persist(interview);

        em.flush();
        em.clear();

        //when
        int bookmarkCount = interviewBookMarkRepository.countByInterviewAndUser(interview, user);

        //then
        assertThat(bookmarkCount).isEqualTo(0);
    }


    @Test
    public void 인터뷰_북마크_여부_판별_북마크_상태() {
        //given
        User user = createUser();
        Interview interview = Interview.createInterview(user, "test content", "test title");
        em.persist(interview);

        interviewService.createInterviewBookmark(user.getId(), interview);

        em.flush();
        em.clear();

        //when
        int bookmarkCount = interviewBookMarkRepository.countByInterviewAndUser(interview, user);

        //then
        assertThat(bookmarkCount).isEqualTo(1);
    }


    private User createUser() {
        User user = User.createTestUser("userA", "test", "test@test.com", "test intro");
        em.persist(user);
        return user;
    }
}