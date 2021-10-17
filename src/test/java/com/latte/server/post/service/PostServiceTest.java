package com.latte.server.post.service;

import com.latte.server.category.domain.Category;
import com.latte.server.post.domain.Bookmark;
import com.latte.server.post.domain.Post;
import com.latte.server.post.domain.Reply;
import com.latte.server.post.domain.Tag;
import com.latte.server.post.dto.*;
import com.latte.server.post.repository.BookmarkRepository;
import com.latte.server.post.repository.ReplyLikeRepository;
import com.latte.server.post.repository.ReplyRepository;
import com.latte.server.user.domain.User;
import com.latte.server.post.repository.PostRepository;
import com.latte.server.user.domain.UserRole;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/**
 * Created by Donggun on 2021-08-05
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PostServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    BookmarkRepository bookmarkRepository;
    @Autowired
    ReplyRepository replyRepository;
    @Autowired
    ReplyLikeRepository replyLikeRepository;

    @Test
    public void 포스트_추가() {
        //given
        User user = createUser();
        String postContent = "test content";
        String postTitle = "test title";
        String postCode = "#stdio.h";

        //when
        Long postId = postService.post(user.getId(), postContent, postTitle, postCode);

        //then
        Post post = postRepository.findById(postId).get();
        assertThat(post.getPostContent()).isEqualTo("test content");
        assertThat(post.getPostTitle()).isEqualTo("test title");
        assertThat(post.getPostCode()).isEqualTo("#stdio.h");
        assertThat(post.getIsQna()).isEqualTo(0);
        assertThat(post.getIsDeleted()).isEqualTo(0);

    }

    @Test
    public void QnA_추가() {
        //given
        User user = createUser();
        String postContent = "test content";
        String postTitle = "test title";
        String postCode = "#stdio.h";

        //when
        Long postId = postService.qna(user.getId(), postContent, postTitle, postCode);

        //then
        Post post = postRepository.findById(postId).get();
        assertThat(post.getPostContent()).isEqualTo("test content");
        assertThat(post.getPostTitle()).isEqualTo("test title");
        assertThat(post.getPostCode()).isEqualTo("#stdio.h");
        assertThat(post.getIsQna()).isEqualTo(1);
        assertThat(post.getIsDeleted()).isEqualTo(0);

    }

    @Test
    public void 포스트_추가_타이틀_공백() {
        //given
        String NOT_EXIST_TEXT = "[ERROR] Do not contain text";

        User user = createUser();
        String postContent = "test content";
        String postTitle = "";
        String postCode = "#stdio.h";

        assertThatIllegalArgumentException()
                .isThrownBy(() -> {
                    // when
                    postService.post(user.getId(), postContent, postTitle, postCode);
                })
                // then
                .withMessage(NOT_EXIST_TEXT);
    }

    @Test
    public void 포스트_추가_컨텐츠_공백() {
        //given
        String NOT_EXIST_TEXT = "[ERROR] Do not contain text";

        User user = createUser();
        String postContent = "";
        String postTitle = "test title";
        String postCode = "#stdio.h";

        assertThatIllegalArgumentException()
                .isThrownBy(() -> {
                    // when
                    postService.post(user.getId(), postContent, postTitle, postCode);
                })
                // then
                .withMessage(NOT_EXIST_TEXT);
    }

    @Test
    public void 포스트_추가_둘다_공백() {
        //given
        String NOT_EXIST_TEXT = "[ERROR] Do not contain text";

        User user = createUser();
        String postContent = "";
        String postTitle = "";
        String postCode = "#stdio.h";

        assertThatIllegalArgumentException()
                .isThrownBy(() -> {
                    // when
                    postService.post(user.getId(), postContent, postTitle, postCode);
                })
                // then
                .withMessage(NOT_EXIST_TEXT);
    }

    @Test
    public void 포스트_추가_띄어쓰기() {
        //given
        String NOT_EXIST_TEXT = "[ERROR] Do not contain text";

        User user = createUser();
        String postContent = " ";
        String postTitle = " ";
        String postCode = "#stdio.h";

        assertThatIllegalArgumentException()
                .isThrownBy(() -> {
                    // when
                    postService.post(user.getId(), postContent, postTitle, postCode);
                })
                // then
                .withMessage(NOT_EXIST_TEXT);
    }

    @Test
    public void 포스트_수정() {
        //given
        User user = createUser();
        String postContent = "test content";
        String postTitle = "test title";
        String postCode = "#stdio.h";
        Long postId = postService.post(user.getId(), postContent, postTitle, postCode);

        String updateContent = "update content";
        String updataeTitle = "update title";
        String updateCode = "update code";

        //when
        postService.update(postId, updateContent, updataeTitle, updateCode);

        //then
        Post post = postRepository.findById(postId).get();
        assertThat(post.getPostContent()).isEqualTo("update content");
        assertThat(post.getPostTitle()).isEqualTo("update title");
        assertThat(post.getPostCode()).isEqualTo("update code");
        assertThat(post.getIsQna()).isEqualTo(0);

    }

    @Test
    public void 포스트_수정_제목_공백() {
        //given
        String NOT_EXIST_TEXT = "[ERROR] Do not contain text";
        User user = createUser();
        String postContent = "test content";
        String postTitle = "test title";
        String postCode = "#stdio.h";
        Long postId = postService.post(user.getId(), postContent, postTitle, postCode);

        String updateContent = "update content";
        String updataeTitle = "";
        String updateCode = "update code";

        assertThatIllegalArgumentException()
                .isThrownBy(() -> {
                    // when
                    postService.update(postId, updateContent, updataeTitle, updateCode);
                })
                // then
                .withMessage(NOT_EXIST_TEXT);

    }

    @Test
    public void 포스트_수정_내용_공백() {
        //given
        String NOT_EXIST_TEXT = "[ERROR] Do not contain text";
        User user = createUser();
        String postContent = "test content";
        String postTitle = "test title";
        String postCode = "#stdio.h";
        Long postId = postService.post(user.getId(), postContent, postTitle, postCode);

        String updateContent = "";
        String updataeTitle = "update title";
        String updateCode = "update code";

        assertThatIllegalArgumentException()
                .isThrownBy(() -> {
                    // when
                    postService.update(postId, updateContent, updataeTitle, updateCode);
                })
                // then
                .withMessage(NOT_EXIST_TEXT);

    }

    @Test
    public void 포스트_수정_띄어쓰기() {
        //given
        String NOT_EXIST_TEXT = "[ERROR] Do not contain text";
        User user = createUser();
        String postContent = "test content";
        String postTitle = "test title";
        String postCode = "#stdio.h";
        Long postId = postService.post(user.getId(), postContent, postTitle, postCode);

        String updateContent = " ";
        String updataeTitle = " ";
        String updateCode = "update code";

        assertThatIllegalArgumentException()
                .isThrownBy(() -> {
                    // when
                    postService.update(postId, updateContent, updataeTitle, updateCode);
                })
                // then
                .withMessage(NOT_EXIST_TEXT);
    }


    @Test
    public void 포스트_삭제() {
        //given
        User user = createUser();
        String postContent = "test content";
        String postTitle = "test title";
        String postCode = "#stdio.h";
        Long postId = postService.post(user.getId(), postContent, postTitle, postCode);

        //when
        postService.delete(postId);

        //then
        Post post = postRepository.findById(postId).get();
        assertThat(post.getIsDeleted()).isEqualTo(1);
    }

    @Test
    public void 포스트_리스트_최신순() {
        //given
        User user = createUser();
        Long userId = user.getId();
        String postContent = "test content";
        String postTitle = "test title";
        String postCode = "#stdio.h";
        for (int i = 0; i < 100; i++) {
            postService.post(user.getId(), postContent + i, postTitle + i, postCode + i);
        }
        PostSearchCondition condition = new PostSearchCondition();
        condition.setIsQna(0);
        condition.setUserId(userId);

        //when
        PageRequest pageRequest = PageRequest.of(0, 7);
        Page<PostListDto> result = postRepository.searchPostPageRecent(condition, pageRequest);

        //then
        assertThat(result.getSize()).isEqualTo(7);
        assertThat(result.getContent()).extracting("postTitle").containsExactly("test title99", "test title98", "test title97", "test title96", "test title95", "test title94", "test title93");
        assertThat(result.getContent()).extracting("postContent").containsExactly("test content99", "test content98", "test content97", "test content96", "test content95", "test content94", "test content93");
    }

    @Test
    public void 포스트_리스트_인기순() {
        //given
        User user = createUser();
        Long userId = user.getId();
        String postContent = "test content";
        String postTitle = "test title";
        String postCode = "#stdio.h";
        for (int i = 0; i < 100; i++) {
            Long postId = postService.post(user.getId(), postContent + i, postTitle + i, postCode + i);
            if (10 < i && i < 18) {
                Post post = postRepository.findById(postId).get();
                Bookmark bookmark = Bookmark.createBookmark(user, post);
                bookmarkRepository.save(bookmark);
            }
        }

        PostSearchCondition condition = new PostSearchCondition();
        condition.setIsQna(0);
        condition.setUserId(userId);

        //when
        PageRequest pageRequest = PageRequest.of(0, 7);
        Page<PostListDto> result = postRepository.searchPostPagePopular(condition, pageRequest);

        //then
        assertThat(result.getSize()).isEqualTo(7);
        assertThat(result.getContent()).extracting("postTitle").containsExactly("test title17", "test title16", "test title15", "test title14", "test title13", "test title12", "test title11");
        assertThat(result.getContent()).extracting("postContent").containsExactly("test content17", "test content16", "test content15", "test content14", "test content13", "test content12", "test content11");
    }

    @Test
    public void 포스트_제목_검색() {
        //given
        User user = createUser();
        Long userId = user.getId();
        String postContent = "test content";
        String postTitle = "test title";
        String postCode = "#stdio.h";
        for (int i = 0; i < 100; i++) {
            postService.post(user.getId(), postContent + i, postTitle + i, postCode + i);
        }

        PostSearchCondition condition = new PostSearchCondition();
        condition.setIsQna(0);
        condition.setUserId(userId);
        condition.setTitleKeyword("13");

        //when
        PageRequest pageRequest = PageRequest.of(0, 1);
        Page<PostListDto> result = postRepository.searchPostPage(condition, pageRequest);

        //then
        assertThat(result.getSize()).isEqualTo(1);
        assertThat(result.getContent()).extracting("postTitle").containsExactly("test title13");
        assertThat(result.getContent()).extracting("postContent").containsExactly("test content13");
    }

    @Test
    public void 포스트_본문_검색() {
        //given
        User user = createUser();
        Long userId = user.getId();
        String postContent = "test content";
        String postTitle = "test title";
        String postCode = "#stdio.h";
        for (int i = 0; i < 100; i++) {
            postService.post(user.getId(), postContent + i, postTitle + i, postCode + i);
        }

        PostSearchCondition condition = new PostSearchCondition();
        condition.setIsQna(0);
        condition.setUserId(userId);
        condition.setContentKeyword("13");

        //when
        PageRequest pageRequest = PageRequest.of(0, 1);
        Page<PostListDto> result = postRepository.searchPostPage(condition, pageRequest);

        //then
        assertThat(result.getSize()).isEqualTo(1);
        assertThat(result.getContent()).extracting("postTitle").containsExactly("test title13");
        assertThat(result.getContent()).extracting("postContent").containsExactly("test content13");
    }

    @Test
    public void 포스트_코드_검색() {
        //given
        User user = createUser();
        Long userId = user.getId();
        String postContent = "test content";
        String postTitle = "test title";
        String postCode = "#stdio.h";
        for (int i = 0; i < 100; i++) {
            postService.post(user.getId(), postContent + i, postTitle + i, postCode + i);
        }

        PostSearchCondition condition = new PostSearchCondition();
        condition.setIsQna(0);
        condition.setUserId(userId);
        condition.setCodeKeyword("13");

        //when
        PageRequest pageRequest = PageRequest.of(0, 1);
        Page<PostListDto> result = postRepository.searchPostPage(condition, pageRequest);

        //then
        assertThat(result.getSize()).isEqualTo(1);
        assertThat(result.getContent()).extracting("postTitle").containsExactly("test title13");
        assertThat(result.getContent()).extracting("postContent").containsExactly("test content13");
    }

    @Test
    public void 포스트_날짜_이후_검색() {
        //given
        User user = createUser();
        Long userId = user.getId();
        String postContent = "test content";
        String postTitle = "test title";
        String postCode = "#stdio.h";
        for (int i = 0; i < 100; i++) {
            postService.post(user.getId(), postContent + i, postTitle + i, postCode + i);
        }

        PostSearchCondition condition = new PostSearchCondition();
        condition.setIsQna(0);
        condition.setUserId(userId);
        condition.setDateAfter(LocalDateTime.now());
        postService.post(user.getId(), "after content", "after title", "after code");

        //when
        PageRequest pageRequest = PageRequest.of(0, 1);
        Page<PostListDto> result = postRepository.searchPostPage(condition, pageRequest);

        //then
        assertThat(result.getContent().size()).isEqualTo(1);
        assertThat(result.getContent()).extracting("postTitle").containsExactly("after title");
        assertThat(result.getContent()).extracting("postContent").containsExactly("after content");
    }

    @Test
    public void 포스트_북마크() {
        //given
        User user = createUser();
        Long userId = user.getId();
        String postContent = "test content";
        String postTitle = "test title";
        String postCode = "#stdio.h";
        Long postId = postService.post(user.getId(), postContent, postTitle, postCode);
        Post post = postRepository.findById(postId).get();

        //when
        postService.createPostBookmark(userId, post);

        //then
        assertThat(bookmarkRepository.findByPost(post)).isNotNull();
    }

    @Test
    public void 포스트_북마크_해제() {
        //given
        User user = createUser();
        Long userId = user.getId();
        String postContent = "test content";
        String postTitle = "test title";
        String postCode = "#stdio.h";
        Long postId = postService.post(user.getId(), postContent, postTitle, postCode);
        Post post = postRepository.findById(postId).get();
        postService.createPostBookmark(userId, post);

        //when
        postService.createPostBookmark(userId, post);

        //then
        assertThat(bookmarkRepository.findByPost(post)).isNull();
    }

    @Test
    public void 포스트_가져오기() {
        //given
        User user = createUser();
        Long userId = user.getId();
        String postContent = "test content";
        String postTitle = "test title";
        String postCode = "#stdio.h";
        Long postId = postService.post(user.getId(), postContent, postTitle, postCode);
        Post post = postRepository.findById(postId).get();
        LocalDateTime createdDate = post.getCreatedDate();
        List<Tag> postTags = post.getPostTags();
        List<String> tagNames = new ArrayList<>();
        List<Long> tagIds = new ArrayList<>();
        for (Tag postTag : postTags) {
            tagIds.add(postTag.getCategory().getId());
            tagNames.add(postTag.getCategory().getCategory());
        }

        //when
        PostDetailDto postDetailDto = postService.loadPost(userId, postId);

        //then
        assertThat(postDetailDto.getPostId()).isEqualTo(postId);
        assertThat(postDetailDto.getPostTitle()).isEqualTo(postTitle);
        assertThat(postDetailDto.getPostContent()).isEqualTo(postContent);
        assertThat(postDetailDto.getPostCode()).isEqualTo(postCode);
        assertThat(postDetailDto.getBookmarkCount()).isEqualTo(0);
        assertThat(postDetailDto.getIsBookmarked()).isEqualTo(0);
        assertThat(postDetailDto.getCreatedDate()).isEqualTo(createdDate);
        assertThat(postDetailDto.getReplyCount()).isEqualTo(0);
        assertThat(postDetailDto.getTagIds()).isEqualTo(tagIds);
        assertThat(postDetailDto.getTags()).isEqualTo(tagNames);
    }

    @Test
    public void 포스트_없음() {
        //given
        User user = createUser();
        Long userId = user.getId();

        //then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> {
                    // when
                    postService.loadPost(userId, 0L);
                })
                // then
                .withMessage("[ERROR] No such Post");
    }

    @Test
    public void 포스트_유저_없음() {
        //given
        User user = createUser();
        Long userId = user.getId();
        String postContent = "test content";
        String postTitle = "test title";
        String postCode = "#stdio.h";
        Long postId = postService.post(user.getId(), postContent, postTitle, postCode);

        //then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> {
                    // when
                    postService.loadPost(0L, postId);
                })
                // then
                .withMessage("[ERROR] No such User");
    }

    @Test
    public void 댓글_가져오기_최신순() {
        //given
        User user = createUser();
        Long userId = user.getId();
        String postContent = "test content";
        String postTitle = "test title";
        String postCode = "#stdio.h";
        Long postId = postService.post(user.getId(), postContent, postTitle, postCode);
        Post post = postRepository.findById(postId).get();

        for (int i = 0; i < 100; i++) {
            Long replyId = postService.reply(post, userId, "test comment" + i);
        }

        ReplySearchCondition condition = new ReplySearchCondition();
        condition.setPostId(postId);
        condition.setUserId(userId);

        //when
        PageRequest pageRequest = PageRequest.of(0, 3);
        Page<ReplyDto> result = postRepository.searchReplyPageRecent(condition, pageRequest);

        //then
        assertThat(result.getContent().size()).isEqualTo(3);
        assertThat(result.getContent()).extracting("replyContent").containsExactly("test comment99", "test comment98", "test comment97");
    }

    @Test
    public void 댓글_가져오기_오래된순() {
        //given
        User user = createUser();
        Long userId = user.getId();
        String postContent = "test content";
        String postTitle = "test title";
        String postCode = "#stdio.h";
        Long postId = postService.post(user.getId(), postContent, postTitle, postCode);
        Post post = postRepository.findById(postId).get();

        for (int i = 0; i < 100; i++) {
            Long replyId = postService.reply(post, userId, "test comment" + i);
        }

        ReplySearchCondition condition = new ReplySearchCondition();
        condition.setPostId(postId);
        condition.setUserId(userId);

        //when
        PageRequest pageRequest = PageRequest.of(0, 3);
        Page<ReplyDto> result = postRepository.searchReplyPageOld(condition, pageRequest);

        //then
        assertThat(result.getContent().size()).isEqualTo(3);
        assertThat(result.getContent()).extracting("replyContent").containsExactly("test reply", "test comment0", "test comment1");
    }

    @Test
    public void 댓글_달기() {
        //given
        User user = createUser();
        Long userId = user.getId();
        String postContent = "test content";
        String postTitle = "test title";
        String postCode = "#stdio.h";
        Long postId = postService.post(user.getId(), postContent, postTitle, postCode);
        Post post = postRepository.findById(postId).get();
        String replyContent = "test reply";

        //when
        Long replyId = postService.reply(post, userId, replyContent);

        //then
        Reply reply = replyRepository.findById(replyId).get();
        assertThat(reply.getPost()).isEqualTo(post);
        assertThat(reply.getId()).isEqualTo(replyId);
        assertThat(reply.getUser()).isEqualTo(user);
        assertThat(reply.getReplyContent()).isEqualTo("test reply");
    }

    @Test
    public void 댓글_좋아요() {
        //given
        User user = createUser();
        Long userId = user.getId();
        String postContent = "test content";
        String postTitle = "test title";
        String postCode = "#stdio.h";
        Long postId = postService.post(user.getId(), postContent, postTitle, postCode);
        Post post = postRepository.findById(postId).get();
        String replyContent = "test reply";
        Long replyId = postService.reply(post, userId, replyContent);
        Reply reply = replyRepository.findById(replyId).get();

        //when
        Long replyLikeId = postService.createReplyLike(userId, reply);

        //then
        assertThat(replyLikeRepository.countByReply(reply)).isEqualTo(1);
        assertThat(replyLikeRepository.findById(replyLikeId)).isNotEmpty();
    }

    @Test
    public void 댓글_좋아요_취소() {
        //given
        User user = createUser();
        Long userId = user.getId();
        String postContent = "test content";
        String postTitle = "test title";
        String postCode = "#stdio.h";
        Long postId = postService.post(user.getId(), postContent, postTitle, postCode);
        Post post = postRepository.findById(postId).get();
        String replyContent = "test reply";
        Long replyId = postService.reply(post, userId, replyContent);
        Reply reply = replyRepository.findById(replyId).get();

        //when
        postService.createReplyLike(userId, reply);
        Long replyLikeId = postService.createReplyLike(userId, reply);

        //then
        assertThat(replyLikeRepository.countByReply(reply)).isEqualTo(0);
        assertThat(replyLikeRepository.findById(replyLikeId)).isEmpty();
    }

    @Test
    public void 댓글_수정() {
        //given
        User user = createUser();
        Long userId = user.getId();
        String postContent = "test content";
        String postTitle = "test title";
        String postCode = "#stdio.h";
        Long postId = postService.post(user.getId(), postContent, postTitle, postCode);
        Post post = postRepository.findById(postId).get();
        String replyContent = "test reply";
        Long replyId = postService.reply(post, userId, replyContent);
        String updateReplyContent = "updated content";

        //when
        postService.replyUpdate(replyId, updateReplyContent);

        //then
        Reply reply = replyRepository.findById(replyId).get();
        assertThat(reply.getReplyContent()).isEqualTo("updated content");
    }

    @Test
    public void 댓글_수정_공백() {
        //given
        String NOT_EXIST_TEXT = "[ERROR] Do not contain text";
        User user = createUser();
        Long userId = user.getId();
        String postContent = "test content";
        String postTitle = "test title";
        String postCode = "#stdio.h";
        Long postId = postService.post(user.getId(), postContent, postTitle, postCode);
        Post post = postRepository.findById(postId).get();
        String replyContent = "test reply";
        Long replyId = postService.reply(post, userId, replyContent);
        String updateReplyContent = "";

        assertThatIllegalArgumentException()
                .isThrownBy(() -> {
                    // when
                    postService.replyUpdate(replyId, updateReplyContent);
                })
                // then
                .withMessage(NOT_EXIST_TEXT);

    }

    @Test
    public void 댓글_수정_띄어쓰기() {
        //given
        String NOT_EXIST_TEXT = "[ERROR] Do not contain text";
        User user = createUser();
        Long userId = user.getId();
        String postContent = "test content";
        String postTitle = "test title";
        String postCode = "#stdio.h";
        Long postId = postService.post(user.getId(), postContent, postTitle, postCode);
        Post post = postRepository.findById(postId).get();
        String replyContent = "test reply";
        Long replyId = postService.reply(post, userId, replyContent);
        String updateReplyContent = " ";

        assertThatIllegalArgumentException()
                .isThrownBy(() -> {
                    // when
                    postService.replyUpdate(replyId, updateReplyContent);
                })
                // then
                .withMessage(NOT_EXIST_TEXT);
    }

    @Test
    public void 댓글_삭제() {
        //given
        User user = createUser();
        Long userId = user.getId();
        String postContent = "test content";
        String postTitle = "test title";
        String postCode = "#stdio.h";
        Long postId = postService.post(user.getId(), postContent, postTitle, postCode);
        Post post = postRepository.findById(postId).get();
        String replyContent = "test reply";
        Long replyId = postService.reply(post, userId, replyContent);

        //when
        postService.replyDelete(replyId);

        //then
        Reply reply = replyRepository.findById(replyId).get();
        assertThat(reply.getIsDeleted()).isEqualTo(1);
    }

    @Test
    public void 포스트_태그_수정() {
        //given
        Post post = createPost();
        Category category = createCategory();
        Long postId = post.getId();
        Long categoryId = category.getId();
        List<Long> categoryIds = new ArrayList<>();
        categoryIds.add(categoryId);

        //when
        Long findPostId = postService.updatePostTag(postId, categoryIds);
        Post findPost = postRepository.findById(findPostId).get();

        //then
        List<Tag> tags = post.getPostTags();
        assertThat(tags).isEqualTo(findPost.getPostTags());
    }

    private Post createPost() {
        User user = User.createTestUser("userA", "test", "test@test.com", "test intro");
        em.persist(user);
        Post post = Post.createPost(user, "test content", "test title", "#stdio.h");
        em.persist(post);
        return post;
    }

    private Category createCategory() {
        Category category = Category.createCategory("test category", "test kind");
        em.persist(category);
        return category;
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
