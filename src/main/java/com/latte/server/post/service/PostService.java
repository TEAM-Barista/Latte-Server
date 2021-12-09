package com.latte.server.post.service;

import com.latte.server.category.domain.Category;
import com.latte.server.category.repository.CategoryRepository;
import com.latte.server.common.exception.custom.*;
import com.latte.server.common.job.AsyncJob;
import com.latte.server.post.domain.*;
import com.latte.server.post.dto.*;
import com.latte.server.post.dto.Response.CreatePostResponse;
import com.latte.server.post.repository.*;
import com.latte.server.push.domain.PushToken;
import com.latte.server.push.domain.PushWrapper;
import com.latte.server.push.repository.PushTokenRepository;
import com.latte.server.push.util.FirebaseCloudMessageUtility;
import com.latte.server.user.domain.User;
import com.latte.server.user.domain.UserCategory;
import com.latte.server.user.repository.UserCategoryRepository;
import com.latte.server.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.latte.server.push.domain.PushMessage.MATCH_NEW_POST_PUSH_MESSAGE;
import static com.latte.server.push.domain.PushMessage.MATCH_NEW_QUESTION_AND_ANSWER_PUSH_MESSAGE;
import static com.latte.server.push.domain.PushWrapper.*;
import static org.springframework.util.StringUtils.*;

/**
 * Created by Donggun on 2021-08-05
 */

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private static final Long POST_BOOKMARK_DELETED = 0L;
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
     * NEW DI
     */

    private final FirebaseCloudMessageUtility firebaseCloudMessageUtility;
    private final UserCategoryRepository userCategoryRepository;
    private final PushTokenRepository pushTokenRepository;
    private final AsyncJob asyncJob;

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

        /**
         * 알림 전송 로직
         * 비동기 로직
         */
        asyncJob.onStart(() -> {
            sendPushMessagesMatchByTags(false, post.getPostTitle(), postTagList);
        });

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

        /**
         * 알림 전송 로직
         * 비동기 로직
         */
        asyncJob.onStart(() -> {
            sendPushMessagesMatchByTags(true, post.getPostTitle(), postTagList);
        });

        return postId;
    }

    @Transactional
    public Long writeReply(String email, Long postId, String replyContent) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(NotFoundUserException::new);

        Post findPost = postRepository.findById(postId)
                .orElseThrow(NotFoundPostException::new);

        Long replyId = reply(findPost, user, replyContent);

        /**
         * 알림 전송 로직
         * 비동기 로직
         */
        asyncJob.onStart(() -> {
            sendPushMessageByReply(findPost.getPostTitle(), findPost.getUser());
        });

        return replyId;
    }

    public Long likeReply(String email, Long replyId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(NotFoundUserException::new);

        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(NotFoundReplyException::new);

        Long replyLikeId = createReplyLike(user, reply);

        return replyLikeId;
    }

    /**
     * 태그 매칭 알람 전송 가능 사용자 선별 함수
     * @param isQna
     * @param title
     * @param postTags
     */
    private void sendPushMessagesMatchByTags(boolean isQna, String title, List<Long> postTags) {
        ConcurrentHashMap<Long, List<String>> userTokenMaps = getUserTokenMaps();
        ConcurrentHashMap<Long, ConcurrentHashMap<Long, Boolean>> userCategoriesMaps = getUserCategoriesMaps();
        // 메시징 선별 작업
        userCategoriesMaps.forEach((userId, categoryMap) ->{
            for (Long categoryId : postTags) {
                if (validateSendCondition(userId, categoryId, categoryMap, userTokenMaps)) {
                    // 푸시 메시지 전송
                    sendPushMessagesByTokens(isQna, title, userTokenMaps.get(userId));
                    break;
                }
            }
        });
    }

    /**
     * 댓글 알람 전송 함수
     * @param title
     * @param user
     */
    @Transactional(readOnly = true)
    public void sendPushMessageByReply(String title, User user) {
        pushTokenRepository.findByUser(user).ifPresent(pushToken -> {
            firebaseCloudMessageUtility.sendTargetMessage(
                    ofNewReply(pushToken.getToken(), title)
            );
        });
    }

    /**
     * 사용자 토큰 리스트 HashMap 변환 함수
     * 구조
     * Long userId : [String pushToken... ]
     * @return
     */
    @Transactional(readOnly = true)
    public ConcurrentHashMap<Long, List<String>> getUserTokenMaps() {
        ConcurrentHashMap<Long, List<String>> userTokenMaps = new ConcurrentHashMap();
        List<PushToken> pushTokens = pushTokenRepository.findAll();
        for (PushToken pushToken : pushTokens) {
            Long userId = pushToken.getUser().getId();
            String token = pushToken.getToken();

            if (userTokenMaps.containsKey(userId)) {
                userTokenMaps.get(userId).add(token);
                continue;
            }

            List<String> tokens = new ArrayList();
            tokens.add(token);
            userTokenMaps.put(userId, tokens);
        }
        return userTokenMaps;
    }

    /**
     * 사용자 카테고리 리스트 HashMap 변환
     * 구조
     * Long userId : {
     *     Long categoryId: Boolean
     * }
     * @return
     */
    @Transactional(readOnly = true)
    public ConcurrentHashMap<Long, ConcurrentHashMap<Long, Boolean>> getUserCategoriesMaps() {
        ConcurrentHashMap<Long, ConcurrentHashMap<Long, Boolean>> userCategoriesMaps = new ConcurrentHashMap();
        List<UserCategory> userCategories = userCategoryRepository.findAll();
        for (UserCategory userCategory : userCategories) {
            Long userId = userCategory.getUser().getId();
            Long categoryId = userCategory.getCategory().getId();

            if (userCategoriesMaps.containsKey(userId)) {
                userCategoriesMaps.get(userId).put(categoryId, true);
                continue;
            }

            ConcurrentHashMap<Long, Boolean> categoryMap = new ConcurrentHashMap();
            categoryMap.put(categoryId, true);
            userCategoriesMaps.put(userId, categoryMap);
        }
        return userCategoriesMaps;
    }

    private boolean validateSendCondition(
            Long userId,
            Long categoryId,
            ConcurrentHashMap<Long, Boolean> categoryMap,
            ConcurrentHashMap<Long, List<String>> userTokenMaps
    ) {
        if (categoryMap.containsKey(categoryId) && userTokenMaps.containsKey(userId)) {
            return true;
        }
        return false;
    }

    private void sendPushMessagesByTokens(boolean isQna, String title, List<String> tokens) {
        for (String targetToken : tokens) {
            // 비동기 로직
            asyncJob.onStart(() -> {
                firebaseCloudMessageUtility.sendTargetMessage(
                        getPushWrapper(isQna, title, targetToken)
                );
            });
        }
    }

    private PushWrapper getPushWrapper(
            boolean isQna,
            String title,
            String targetToken
    ) {
        if (isQna) {
            return ofMatchQnA(targetToken, title);
        }
        return ofMatchPost(targetToken, title);
    }
}
