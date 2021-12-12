package com.latte.server.post.service;

import com.latte.server.category.domain.Category;
import com.latte.server.category.repository.CategoryRepository;
import com.latte.server.common.exception.custom.*;
import com.latte.server.post.domain.*;
import com.latte.server.post.dto.*;
import com.latte.server.post.repository.*;
import com.latte.server.user.domain.User;
import com.latte.server.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.util.StringUtils.*;

/**
 * Created by Donggun on 2021-08-05
 */

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private static final Long POST_BOOKMARK_DELETED = 0L;
    private static final Long BOOKMARKED_POST = 1L;
    private static final Long REPLY_LIKE_DELETED = 0L;
    private static final int POST_REPLY_NOT_DELETED = 0;

    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final BookmarkRepository bookmarkRepository;
    private final ReplyRepository replyRepository;
    private final ReplyLikeRepository replyLikeRepository;

    /**
     * Post
     */
    @Transactional
    public Long post(User user, String postContent, String postTitle, String postCode) {
        valifyText(postContent, postTitle);

        Post post = Post.createPost(user, postContent, postTitle, postCode);

        postRepository.save(post);

        return post.getId();
    }

    @Transactional
    public Long qna(User user, String postContent, String postTitle, String postCode) {
        valifyText(postContent, postTitle);

        Post post = Post.createQna(user, postContent, postTitle, postCode);

        postRepository.save(post);

        return post.getId();
    }

    @Transactional
    public void update(Long postId, String postContent, String postTitle, String postCode) {
        Post findPost = postRepository.findById(postId).orElseThrow(NotFoundPostException::new);
        valifyText(postContent, postTitle);

        findPost.changePost(postContent, postTitle, postCode);
    }

    private void valifyText(String postContent, String postTitle) {
        if (!(hasText(postTitle) && hasText(postContent))) {
            throw new NotFoundTextException();
        }
    }

    private void valifyReply(String replyContent) {
        if (!(hasText(replyContent))) {
            throw new NotFoundTextException();
        }
    }

    @Transactional
    public void delete(Long postId) {
        Post findPost = postRepository.findById(postId).orElseThrow(NotFoundPostException::new);

        findPost.deletePost();
    }

    @Transactional
    public Long createPostBookmark(User user, Post post) {
        if (bookmarkRepository.findByPost(post) == null) {
            Bookmark bookmark = Bookmark.createBookmark(user, post);
            bookmarkRepository.save(bookmark);
            return bookmark.getId();
        }

        bookmarkRepository.delete(bookmarkRepository.findByPost(post));
        return POST_BOOKMARK_DELETED;
    }

    public PostDetailDto loadPost(String email, Long postId) {
        User findUser = userRepository.findByEmail(email)
                .orElseThrow(NotFoundUserException::new);

        Post findPost = postRepository.findById(postId)
                .orElseThrow(NotFoundPostException::new);

        Long replyCount = replyRepository.countByPostAndIsDeleted(findPost, POST_REPLY_NOT_DELETED);
        Long bookmarkCount = bookmarkRepository.countByPost(findPost);
        Long isBookmarked = bookmarkRepository.countByPostAndUser(findPost, findUser);

        return new PostDetailDto(findPost, replyCount, bookmarkCount, isBookmarked);
    }

    @Transactional
    public Long reply(Post post, User user, String replyContent) {
        valifyReply(replyContent);

        Reply newReply = Reply.createNewReply(user, post, replyContent, POST_REPLY_NOT_DELETED);

        replyRepository.save(newReply);

        return newReply.getId();
    }


    @Transactional
    public Long createReplyLike(User user, Reply reply) {
        if (replyLikeRepository.findByReplyAndUser(reply, user) == null) {
            ReplyLike replyLike = ReplyLike.createReplyLike(reply, user);
            replyLikeRepository.save(replyLike);
            return replyLike.getId();
        }

        replyLikeRepository.delete(replyLikeRepository.findByReply(reply));
        return REPLY_LIKE_DELETED;
    }

    @Transactional
    public void replyUpdate(Long replyId, String replyContent) {
        Reply findReply = replyRepository.findById(replyId)
                .orElseThrow(NotFoundReplyException::new);
        valifyReply(replyContent);

        findReply.changeReply(replyContent);
    }

    @Transactional
    public void replyDelete(Long replyId) {
        Reply findReply = replyRepository.findById(replyId)
                .orElseThrow(NotFoundReplyException::new);

        findReply.deleteReply();
    }

    @Transactional
    public Long updatePostTag(Long postId, List<Long> categoryIds) {
        Post post = postRepository.findById(postId)
                .orElseThrow(NotFoundPostException::new);

        post.clearPostTag();

        for (Long categoryId : categoryIds) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(NotFoundCategoryException::new);

            Tag tag = Tag.createTag(post, category);
            post.addPostTag(tag);
            tagRepository.save(tag);
        }
        return postId;
    }

    public Page<PostListDto> searchRepositoryPostPage(PostSearchCondition condition, Pageable pageable, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(NotFoundUserException::new);
        condition.setUserId(user.getId());
        return postRepository.searchPostPage(condition, pageable);
    }

    public Page<PostListDto> searchRepositoryPostListPopular(PostSearchCondition condition, Pageable pageable, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(NotFoundUserException::new);
        condition.setUserId(user.getId());
        return postRepository.searchPostPagePopular(condition, pageable);
    }

    public Page<PostListDto> searchRepositoryPostListRecent(PostSearchCondition condition, Pageable pageable, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(NotFoundUserException::new);
        condition.setUserId(user.getId());
        return postRepository.searchPostPageRecent(condition, pageable);
    }

    public Page<ReplyDto> searchRepositoryReplyPageRecent(ReplySearchCondition condition, Pageable pageable, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(NotFoundUserException::new);
        condition.setUserId(user.getId());
        return postRepository.searchReplyPageRecent(condition, pageable);
    }

    public Page<ReplyDto> searchRepositoryReplyPageOld(ReplySearchCondition condition, Pageable pageable, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(NotFoundUserException::new);
        condition.setUserId(user.getId());
        return postRepository.searchReplyPageOld(condition, pageable);
    }

    public Page<PostListDto> searchRepositoryBookmarkedPostPageRecent(PostSearchCondition condition, Pageable pageable, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(NotFoundUserException::new);
        condition.setUserId(user.getId());
        return postRepository.searchBookmarkedPostPageRecent(condition, pageable);
    }

    public Page<PostListDto> searchRepositoryMyPostPageRecent(PostSearchCondition condition, Pageable pageable, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(NotFoundUserException::new);
        condition.setUserId(user.getId());
        return postRepository.searchMyPostPageRecent(condition, pageable);
    }

    @Transactional
    public Long bookmarkPost(String email, Long postId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(NotFoundUserException::new);

        Post findPost = postRepository.findById(postId)
                .orElseThrow(NotFoundPostException::new);

        Long bookmarkedPostId = createPostBookmark(user, findPost);

        return bookmarkedPostId;
    }

    @Transactional
    public Long writePost(String email, String postContent, String postTitle, String postCode, List<Long> postTags) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(NotFoundUserException::new);

        Long postId = post(user, postContent, postTitle, postCode);
        Post post = postRepository.findById(postId)
                .orElseThrow(NotFoundPostException::new);

        List<Long> postTagList = postTags;
        for (Long postTag : postTagList) {
            Tag tag = tagRepository.findById(postTag)
                    .orElseThrow(NotFoundTagException::new);
            post.addPostTag(tag);
        }

        return postId;
    }

    @Transactional
    public Long writeQna(String email, String postContent, String postTitle, String postCode, List<Long> postTags) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(NotFoundUserException::new);

        Long postId = qna(user, postContent, postTitle, postCode);
        Post post = postRepository.findById(postId)
                .orElseThrow(NotFoundPostException::new);

        List<Long> postTagList = postTags;
        for (Long postTag : postTagList) {
            Tag tag = tagRepository.findById(postTag)
                    .orElseThrow(NotFoundTagException::new);
            post.addPostTag(tag);
        }

        return postId;
    }

    @Transactional
    public Long writeReply(String email, Long postId, String replyContent) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(NotFoundUserException::new);

        Post findPost = postRepository.findById(postId)
                .orElseThrow(NotFoundPostException::new);

        Long replyId = reply(findPost, user, replyContent);

        return replyId;
    }

    @Transactional
    public Long likeReply(String email, Long replyId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(NotFoundUserException::new);

        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(NotFoundReplyException::new);

        Long replyLikeId = createReplyLike(user, reply);

        return replyLikeId;
    }

    public PostListDto latestBookmark(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(NotFoundUserException::new);

        Bookmark findBookmark = bookmarkRepository.findTopByUserOrderByCreatedDateDesc(user);
        Post findPost = findBookmark.getPost();

        Long replyCount = replyRepository.countByPostAndIsDeleted(findPost, POST_REPLY_NOT_DELETED);

        Long bookmarkCount = bookmarkRepository.countByPost(findPost);

        return new PostListDto(findPost, replyCount, bookmarkCount, BOOKMARKED_POST);
    }

    public PostListDto latestWriting(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(NotFoundUserException::new);

        Post findPost = postRepository.findTopByUserOrderByCreatedDateDesc(user);

        Long replyCount = replyRepository.countByPostAndIsDeleted(findPost, POST_REPLY_NOT_DELETED);

        Long bookmarkCount = bookmarkRepository.countByPost(findPost);

        Long isBookmarked = bookmarkRepository.countByPostAndUser(findPost, user);

        return new PostListDto(findPost, replyCount, bookmarkCount, isBookmarked);
    }
}
