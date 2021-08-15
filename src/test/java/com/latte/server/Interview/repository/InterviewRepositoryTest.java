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
public class InterviewRepositoryTest {

    @Autowired
    InterviewService interviewService;

    @Autowired
    InterviewRepository interviewRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void 인터뷰_게시글_출력() {
        //given
        User user = createUser();
        String interviewContent = "test content";
        String interviewTitle = "test title";
        interviewService.createInterview(user.getId(), interviewContent, interviewTitle);
        String interviewContent2 = "test content2";
        String interviewTitle2 = "test title2";
        interviewService.createInterview(user.getId(), interviewContent2, interviewTitle2);

        //when
        Interview interviewByCreatedDate = interviewRepository.findInterviewByCreatedDate();

        //then
        assertThat(interviewByCreatedDate.getInterviewContent()).isEqualTo("test content2");
        assertThat(interviewByCreatedDate.getInterviewTitle()).isEqualTo("test title2");
        assertThat(interviewByCreatedDate.getIsDeleted()).isEqualTo(0);
    }


    private User createUser() {
        User user = User.createTestUser("userA", "test", "test@test.com", "test intro");
        em.persist(user);
        return user;
    }
}