package com.latte.server.Interview.service;

import com.latte.server.category.domain.Category;
import com.latte.server.interview.domain.Interview;
import com.latte.server.interview.domain.InterviewBookmark;
import com.latte.server.interview.domain.InterviewLike;
import com.latte.server.interview.domain.InterviewTag;
import com.latte.server.interview.repository.InterviewBookmarkRepository;
import com.latte.server.interview.repository.InterviewLikeRepository;
import com.latte.server.interview.repository.InterviewRepository;
import com.latte.server.interview.repository.InterviewTagRepository;
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
    @Autowired
    InterviewTagRepository interviewTagRepository;
    @PersistenceContext
    EntityManager em;

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
    public void 인터뷰_태그_생성() {
        //given
        User user = createUser();
        Interview interview = Interview.createInterview(user, "test content", "test title");
        Category category = Category.createCategory("test category", "test");

        //when
        Long interviewTag = interviewService.createInterviewTag(interview, category);

        //then
        InterviewTag findInterviewTag = interviewTagRepository.findById(interviewTag).get();
        assertThat(findInterviewTag.getInterview()).isEqualTo(interview);
        assertThat(findInterviewTag.getId()).isEqualTo(interviewTag);
        assertThat(findInterviewTag.getCategory().getCategory()).isEqualTo("test category");
        assertThat(findInterviewTag.getCategory().getKind()).isEqualTo("test");
    }


    @Test
    public void 인터뷰_해시태그_가져오기() {
        //given
        User user = createUser();
        Interview interview = Interview.createInterview(user, "test content", "test title");

        Category category1 = Category.createCategory("test category1", "test1");
        interviewService.createInterviewTag(interview, category1);
        Category category2 = Category.createCategory("test category2", "test2");
        interviewService.createInterviewTag(interview, category2);

        //when
        List<InterviewTag> findTags = interviewTagRepository.findByInterview(interview);

        //then
        assertThat(findTags.size()).isEqualTo(2);
        assertThat(findTags.get(0).getCategory()).isEqualTo(category1);
        assertThat(findTags.get(0).getInterview()).isEqualTo(interview);

        assertThat(findTags.get(1).getCategory()).isEqualTo(category2);
        assertThat(findTags.get(1).getInterview()).isEqualTo(interview);
    }

    private User createUser() {
        User user = User.createTestUser("userA", "test", "test@test.com", "test intro");
        em.persist(user);
        return user;
    }
}