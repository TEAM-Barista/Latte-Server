package com.latte.server.Interview.service;

import com.latte.server.interview.domain.Interview;
import com.latte.server.interview.domain.InterviewBookmark;
import com.latte.server.interview.domain.InterviewLike;
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

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Created by Donggun on 2021-08-05
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class InterviewServiceTest {

    @Autowired
    InterviewService interviewService;

    @Autowired
    InterviewRepository interviewRepository;
    @Autowired
    InterviewLikeRepository interviewLikeRepository;
    @Autowired
    InterviewBookmarkRepository interviewBookMarkRepository;
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
    public void 인터뷰_좋아요() {
        //given
        User user = createUser();
        Interview interview = Interview.createInterview(user, "test content", "test title");
        em.persist(interview);

        em.flush();
        em.clear();

        //when
        Long interviewLike = interviewService.createInterviewLike(user.getId(), interview);

        //then
        InterviewLike findInterviewLike = interviewLikeRepository.findById(interviewLike).get();
        assertThat(findInterviewLike.getInterview()).isEqualTo(interview);
        assertThat(findInterviewLike.getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    public void 이미_좋아요된_인터뷰_다시_좋아요() {
        //given
        User user = createUser();
        Interview interview = Interview.createInterview(user, "test content", "test title");
        em.persist(interview);

        em.flush();
        em.clear();

        interviewService.createInterviewLike(user.getId(), interview);

        //when
        Long interviewLike = interviewService.createInterviewLike(user.getId(), interview);

        //then
        assertThat(interviewLike).isEqualTo(0L);
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

    @Test
    public void 인터뷰_북마크_여부_판별() {
        //given
        User user = createUser();
        Interview interview = Interview.createInterview(user, "test content", "test title");
        em.persist(interview);

        em.flush();
        em.clear();

        //when
        int bookmarkCount = interviewBookMarkRepository.countByInterviewAndAndUser(interview, user);

        //then
        assertThat(bookmarkCount).isEqualTo(0);
    }
    @Test
    public void 인터뷰_북마크() {
        //given
        User user = createUser();
        Interview interview = Interview.createInterview(user, "test content", "test title");
        em.persist(interview);

        em.flush();
        em.clear();

        //when
        Long interviewBookmark = interviewService.createInterviewBookmark(user.getId(), interview);

        //then
        InterviewBookmark findInterviewBookmark = interviewBookMarkRepository.findById(interviewBookmark).get();
        assertThat(findInterviewBookmark.getInterview()).isEqualTo(interview);
        assertThat(findInterviewBookmark.getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    public void 이미_북마크된_인터뷰_다시_북마크() {
        //given
        User user = createUser();
        Interview interview = Interview.createInterview(user, "test content", "test title");
        em.persist(interview);

        em.flush();
        em.clear();

        interviewService.createInterviewBookmark(user.getId(), interview);

        //when
        Long interviewLike = interviewService.createInterviewBookmark(user.getId(), interview);

        //then
        assertThat(interviewLike).isEqualTo(0L);
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
        int bookmarkCount = interviewBookMarkRepository.countByInterviewAndAndUser(interview, user);

        //then
        assertThat(bookmarkCount).isEqualTo(1);
    }

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
        List<Interview> interviewByCreatedDate = interviewRepository.findInterviewByCreatedDate();
        Interview findInterview = interviewByCreatedDate.get(0);

        //then
        assertThat(findInterview.getInterviewContent()).isEqualTo("test content2");
        assertThat(findInterview.getInterviewTitle()).isEqualTo("test title2");
        assertThat(findInterview.getIsDeleted()).isEqualTo(0);
    }

    @Test
    public void 인터뷰_게시글_생성() {
        //given
        User user = createUser();
        String interviewContent = "test content";
        String interviewTitle = "test title";

        //when
        Long id = interviewService.createInterview(user.getId(), interviewContent, interviewTitle);

        //then
        Interview interview = interviewRepository.findById(id).get();
        assertThat(interview.getInterviewContent()).isEqualTo("test content");
        assertThat(interview.getInterviewTitle()).isEqualTo("test title");
    }

    @Test
    public void 인터뷰_해시태그_가져오기() {
    }

    private User createUser() {
        User user = User.createTestUser("userA", "test", "test@test.com", "test intro");
        em.persist(user);
        return user;
    }
}
