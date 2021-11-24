package com.latte.server.Interview.repository;

import com.latte.server.interview.domain.Interview;
import com.latte.server.interview.repository.InterviewBookmarkRepository;
import com.latte.server.interview.service.InterviewService;
import com.latte.server.user.domain.User;
import com.latte.server.user.domain.UserRole;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

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
    InterviewBookmarkRepository interviewBookmarkRepository;
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
        int bookmarkCount = interviewBookmarkRepository.countByInterview(interview);

        //then
        assertThat(bookmarkCount).isEqualTo(0);
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
        int bookmarkCount = interviewBookmarkRepository.countByInterviewAndUser(interview, user);

        //then
        assertThat(bookmarkCount).isEqualTo(0);
    }


    @Test
    public void 인터뷰_북마크_여부_판별_북마크_상태() {
        //given
        User user = createUser();
        Interview interview = Interview.createInterview(user, "test content", "test title");
        em.persist(interview);

        interviewService.createInterviewBookmark(user, interview);

        em.flush();
        em.clear();

        //when
        int bookmarkCount = interviewBookmarkRepository.countByInterviewAndUser(interview, user);

        //then
        assertThat(bookmarkCount).isEqualTo(1);
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