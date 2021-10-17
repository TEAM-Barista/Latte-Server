package com.latte.server.Interview.service;

import com.latte.server.category.domain.Category;
import com.latte.server.interview.domain.Interview;
import com.latte.server.interview.domain.InterviewBookmark;
import com.latte.server.interview.domain.InterviewLike;
import com.latte.server.interview.domain.InterviewTag;
import com.latte.server.interview.dto.CarouselDto;
import com.latte.server.interview.dto.InterviewDetailDto;
import com.latte.server.interview.dto.InterviewListDto;
import com.latte.server.interview.dto.InterviewSearchCondition;
import com.latte.server.interview.repository.*;
import com.latte.server.interview.service.InterviewService;
import com.latte.server.post.domain.Post;
import com.latte.server.post.domain.SeniorRequest;
import com.latte.server.post.domain.Tag;
import com.latte.server.post.dto.PostDetailDto;
import com.latte.server.post.dto.PostListDto;
import com.latte.server.post.dto.PostSearchCondition;
import com.latte.server.user.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @Autowired
    SeniorRequestRepository seniorRequestRepository;

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

    @Test
    public void 인터뷰_리스트_최신순() {
        //given
        User user = createUser();
        Long userId = user.getId();
        String interviewContent = "test content";
        String interviewTitle = "test title";
        for (int i = 0; i < 100; i++) {
            interviewService.createInterview(user.getId(), interviewContent + i, interviewTitle + i);
        }
        InterviewSearchCondition condition = new InterviewSearchCondition();
        condition.setUserId(userId);

        //when
        PageRequest pageRequest = PageRequest.of(0, 7);
        Page<InterviewListDto> result = interviewRepository.searchInterviewPageRecent(condition, pageRequest);

        //then
        assertThat(result.getSize()).isEqualTo(7);
        assertThat(result.getContent()).extracting("interviewTitle").containsExactly("test title99", "test title98", "test title97", "test title96", "test title95", "test title94", "test title93");
        assertThat(result.getContent()).extracting("interviewContent").containsExactly("test content99", "test content98", "test content97", "test content96", "test content95", "test content94", "test content93");
    }

    @Test
    public void 인터뷰_추천() {
        //given
        User user = createUser();
        Long userId = user.getId();
        String interviewContent = "test content";
        String interviewTitle = "test title";
        Long findInterviewId = interviewService.createInterview(user.getId(), interviewContent, interviewTitle);

        Interview findInterview = interviewRepository.findById(findInterviewId).get();

        Category testCategory1 = Category.createCategory("test1", "test");
        Long interviewTag1 = interviewService.createInterviewTag(findInterview, testCategory1);

        Category testCategory2 = Category.createCategory("test2", "test");
        Long interviewTag2 = interviewService.createInterviewTag(findInterview, testCategory2);

        Category testCategory3 = Category.createCategory("test3", "test");
        Long interviewTag3 = interviewService.createInterviewTag(findInterview, testCategory3);

        InterviewSearchCondition condition = new InterviewSearchCondition();
        condition.setUserId(userId);
        InterviewTag interviewTag = interviewTagRepository.findById(interviewTag3).get();
        condition.setInterviewTag(interviewTag);

        //when
        PageRequest pageRequest = PageRequest.of(0, 7);
        Page<InterviewListDto> result = interviewRepository.searchInterviewPageRecommend(condition, pageRequest);

        //then
        assertThat(result.getContent()).extracting("interviewTitle").containsExactly("test title");
        assertThat(result.getContent()).extracting("interviewContent").containsExactly("test content");
        assertThat(result.getContent()).flatExtracting("interviewTagIds").contains(interviewTag3);
        assertThat(result.getContent()).flatExtracting("interviewTags").contains("test3");
    }

    @Test
    public void 인터뷰_질문_요청() {
        //given
        User user = createUser();
        Interview interview = Interview.createInterview(user, "test content", "test title");

        //when
        Long seniorRequestId = interviewService.createSeniorRequest(user.getId(), interview, "test title", "test content");

        //then
        SeniorRequest findSeniorRequest = seniorRequestRepository.findById(seniorRequestId).get();
        assertThat(findSeniorRequest.getSeniorRequestTitle()).isEqualTo("test title");
        assertThat(findSeniorRequest.getSeniorRequestContent()).isEqualTo("test content");
        assertThat(findSeniorRequest.getInterview()).isEqualTo(interview);
    }


    @Test
    public void 인터뷰_가져오기() {
        //given
        User user = createUser();
        Long userId = user.getId();
        String interviewContent = "test content";
        String interviewTitle = "test title";
        Long interviewId = interviewService.createInterview(userId, interviewContent, interviewTitle);
        Interview interview = interviewRepository.findById(interviewId).get();
        LocalDateTime createdDate = interview.getCreatedDate();
        List<InterviewTag> interviewTags = interview.getInterviewTags();
        List<String> tagNames = new ArrayList<>();
        List<Long> tagIds = new ArrayList<>();
        for (InterviewTag interviewTag : interviewTags) {
            tagIds.add(interviewTag.getCategory().getId());
            tagNames.add(interviewTag.getCategory().getCategory());
        }

        //when
        InterviewDetailDto interviewDetailDto = interviewService.loadInterview(userId, interviewId);

        //then
        assertThat(interviewDetailDto.getInterviewId()).isEqualTo(interviewId);
        assertThat(interviewDetailDto.getInterviewTitle()).isEqualTo(interviewTitle);
        assertThat(interviewDetailDto.getInterviewContent()).isEqualTo(interviewContent);
        assertThat(interviewDetailDto.getBookmarkCount()).isEqualTo(0);
        assertThat(interviewDetailDto.getLikeCount()).isEqualTo(0);
        assertThat(interviewDetailDto.getIsLiked()).isEqualTo(0);
        assertThat(interviewDetailDto.getIsBookmarked()).isEqualTo(0);
        assertThat(interviewDetailDto.getCreatedDate()).isEqualTo(createdDate);
        assertThat(interviewDetailDto.getTagIds()).isEqualTo(tagIds);
        assertThat(interviewDetailDto.getTags()).isEqualTo(tagNames);
    }


    private User createUser() {
        User user = User.createTestUser("userA", "test", "test@test.com", "test intro");
        em.persist(user);
        return user;
    }
}
