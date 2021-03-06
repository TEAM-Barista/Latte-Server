package com.latte.server.post.repository;

import com.latte.server.post.domain.*;
import com.latte.server.post.dto.*;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.latte.server.post.domain.QBookmark.*;
import static com.latte.server.post.domain.QPost.post;
import static com.latte.server.post.domain.QReply.reply;
import static com.latte.server.post.domain.QReplyLike.*;
import static com.latte.server.user.domain.QUser.*;
import static com.querydsl.core.types.ExpressionUtils.count;


/**
 * Created by Donggun on 2021-08-26
 */

public class PostRepositoryImpl implements PostRepositoryCustom {

    private static final Integer NOT_DELETED = 0;
    private final JPAQueryFactory queryFactory;

    public PostRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<PostListDto> searchPostPage(PostSearchCondition condition, Pageable pageable) {
        List<PostListDto> content = queryFactory
                .select(new QPostListDto(
                        post,
                        count(reply),
                        count(bookmark),
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(count(bookmark))
                                        .from(bookmark)
                                        .where(bookmark.user.id.eq(condition.getUserId()))
                                , "isBookmarked"
                        )
                ))
                .from(post)
                .leftJoin(reply)
                .on(reply.post.id.eq(post.id)).fetchJoin()
                .leftJoin(bookmark)
                .on(bookmark.post.id.eq(post.id)).fetchJoin()
                .where(
                        post.isDeleted.eq(NOT_DELETED),
                        isQna(condition.getIsQna()),
                        titleKeyword(condition.getTitleKeyword()),
                        contentKeyword(condition.getContentKeyword()),
                        codeKeyword(condition.getCodeKeyword()),
                        dateAfter(condition.getDateAfter()),
                        searchTag1(condition.getCategoryIds()),
                        searchTag2(condition.getCategoryIds()),
                        searchTag3(condition.getCategoryIds()),
                        searchTag4(condition.getCategoryIds()),
                        searchTag5(condition.getCategoryIds())
                )
                .groupBy(post.id)
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // count ??????
        JPAQuery<Post> countQuery = queryFactory
                .selectFrom(post)
                .where(
                        post.isDeleted.eq(NOT_DELETED),
                        isQna(condition.getIsQna()),
                        titleKeyword(condition.getTitleKeyword()),
                        contentKeyword(condition.getContentKeyword()),
                        codeKeyword(condition.getCodeKeyword()),
                        dateAfter(condition.getDateAfter()),
                        searchTag1(condition.getCategoryIds()),
                        searchTag2(condition.getCategoryIds()),
                        searchTag3(condition.getCategoryIds()),
                        searchTag4(condition.getCategoryIds()),
                        searchTag5(condition.getCategoryIds())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }


    @Override
    public Page<PostListDto> searchPostPageRecent(PostSearchCondition condition, Pageable pageable) {
        List<PostListDto> content = queryFactory
                .select(new QPostListDto(
                        post,
                        count(reply),
                        count(bookmark),
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(count(bookmark))
                                        .from(bookmark)
                                        .where(bookmark.user.id.eq(condition.getUserId()))
                                , "isBookmarked"
                        )
                ))
                .from(post)
                .leftJoin(reply)
                .on(reply.post.id.eq(post.id)).fetchJoin()
                .leftJoin(bookmark)
                .on(bookmark.post.id.eq(post.id)).fetchJoin()
                .where(
                        post.isDeleted.eq(NOT_DELETED),
                        isQna(condition.getIsQna()),
                        titleKeyword(condition.getTitleKeyword()),
                        contentKeyword(condition.getContentKeyword()),
                        codeKeyword(condition.getCodeKeyword()),
                        dateAfter(condition.getDateAfter())
                )
                .groupBy(post.id)
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // count ??????
        JPAQuery<Post> countQuery = queryFactory
                .selectFrom(post)
                .where(
                        post.isDeleted.eq(NOT_DELETED),
                        isQna(condition.getIsQna()),
                        titleKeyword(condition.getTitleKeyword()),
                        contentKeyword(condition.getContentKeyword()),
                        codeKeyword(condition.getCodeKeyword()),
                        dateAfter(condition.getDateAfter())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    @Override
    public Page<PostListDto> searchBookmarkedPostPageRecent(PostSearchCondition condition, Pageable pageable) {
        List<PostListDto> content = queryFactory
                .select(new QPostListDto(
                        post,
                        count(reply),
                        count(bookmark),
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(count(bookmark))
                                        .from(bookmark)
                                        .where(bookmark.user.id.eq(condition.getUserId()))
                                , "isBookmarked"
                        )
                ))
                .from(post)
                .leftJoin(reply)
                .on(reply.post.id.eq(post.id)).fetchJoin()
                .leftJoin(bookmark)
                .on(bookmark.post.id.eq(post.id)).fetchJoin()
                .where(
                        post.isDeleted.eq(NOT_DELETED),
                        bookmark.user.id.eq(condition.getUserId()),
                        isQna(condition.getIsQna()),
                        titleKeyword(condition.getTitleKeyword()),
                        contentKeyword(condition.getContentKeyword()),
                        codeKeyword(condition.getCodeKeyword()),
                        dateAfter(condition.getDateAfter())
                )
                .groupBy(post.id)
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // count ??????
        JPAQuery<Post> countQuery = queryFactory
                .selectFrom(post)
                .where(
                        post.isDeleted.eq(NOT_DELETED),
                        bookmark.user.id.eq(condition.getUserId()),
                        isQna(condition.getIsQna()),
                        titleKeyword(condition.getTitleKeyword()),
                        contentKeyword(condition.getContentKeyword()),
                        codeKeyword(condition.getCodeKeyword()),
                        dateAfter(condition.getDateAfter())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    @Override
    public Page<PostListDto> searchMyPostPageRecent(PostSearchCondition condition, Pageable pageable) {
        List<PostListDto> content = queryFactory
                .select(new QPostListDto(
                        post,
                        count(reply),
                        count(bookmark),
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(count(bookmark))
                                        .from(bookmark)
                                        .where(bookmark.user.id.eq(condition.getUserId()))
                                , "isBookmarked"
                        )
                ))
                .from(post)
                .leftJoin(reply)
                .on(reply.post.id.eq(post.id)).fetchJoin()
                .leftJoin(bookmark)
                .on(bookmark.post.id.eq(post.id)).fetchJoin()
                .where(
                        post.isDeleted.eq(NOT_DELETED),
                        post.user.id.eq(condition.getUserId()),
                        isQna(condition.getIsQna()),
                        titleKeyword(condition.getTitleKeyword()),
                        contentKeyword(condition.getContentKeyword()),
                        codeKeyword(condition.getCodeKeyword()),
                        dateAfter(condition.getDateAfter())
                )
                .groupBy(post.id)
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // count ??????
        JPAQuery<Post> countQuery = queryFactory
                .selectFrom(post)
                .where(
                        post.isDeleted.eq(NOT_DELETED),
                        post.user.id.eq(condition.getUserId()),
                        isQna(condition.getIsQna()),
                        titleKeyword(condition.getTitleKeyword()),
                        contentKeyword(condition.getContentKeyword()),
                        codeKeyword(condition.getCodeKeyword()),
                        dateAfter(condition.getDateAfter())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    @Override
    public Page<PostListDto> searchPostPagePopular(PostSearchCondition condition, Pageable pageable) {
        List<PostListDto> content = queryFactory
                .select(new QPostListDto(
                        post,
                        count(reply),
                        count(bookmark),
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(count(bookmark))
                                        .from(bookmark)
                                        .where(bookmark.user.id.eq(condition.getUserId()))
                                , "isBookmarked"
                        )
                ))
                .from(post)
                .leftJoin(reply)
                .on(reply.post.id.eq(post.id)).fetchJoin()
                .leftJoin(bookmark)
                .on(bookmark.post.id.eq(post.id)).fetchJoin()
                .where(
                        post.isDeleted.eq(NOT_DELETED),
                        isQna(condition.getIsQna()),
                        titleKeyword(condition.getTitleKeyword()),
                        contentKeyword(condition.getContentKeyword()),
                        codeKeyword(condition.getCodeKeyword()),
                        dateAfter(condition.getDateAfter())
                )
                .groupBy(post.id)
                .orderBy(bookmark.count().desc(), post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // count ??????
        JPAQuery<Post> countQuery = queryFactory
                .selectFrom(post)
                .where(
                        post.isDeleted.eq(NOT_DELETED),
                        isQna(condition.getIsQna()),
                        titleKeyword(condition.getTitleKeyword()),
                        contentKeyword(condition.getContentKeyword()),
                        codeKeyword(condition.getCodeKeyword()),
                        dateAfter(condition.getDateAfter())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    @Override
    public Page<ReplyDto> searchReplyPageRecent(ReplySearchCondition condition, Pageable pageable) {
        List<ReplyDto> content = queryFactory
                .select(new QReplyDto(
                        post,
                        user,
                        reply,
                        count(replyLike),
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(count(replyLike))
                                        .from(replyLike)
                                        .where(replyLike.user.id.eq(condition.getUserId()))
                                , "isLiked"
                        )
                ))
                .from(reply)
                .leftJoin(post)
                .on(reply.post.id.eq(post.id)).fetchJoin()
                .leftJoin(user)
                .on(reply.user.id.eq(user.id)).fetchJoin()
                .leftJoin(replyLike)
                .on(replyLike.reply.id.eq(reply.id)).fetchJoin()
                .where(
                        reply.isDeleted.eq(NOT_DELETED),
                        replyContentKeyword(condition.getReplyContentKeyword()),
                        replyUser(condition.getReplyUserName()),
                        dateAfter(condition.getDateAfter())
                )
                .groupBy(reply.id)
                .orderBy(reply.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // count ??????
        JPAQuery<Reply> countQuery = queryFactory
                .selectFrom(reply)
                .leftJoin(post)
                .on(reply.post.id.eq(post.id)).fetchJoin()
                .where(
                        reply.isDeleted.eq(NOT_DELETED),
                        replyContentKeyword(condition.getReplyContentKeyword()),
                        replyUser(condition.getReplyUserName()),
                        dateAfter(condition.getDateAfter())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    @Override
    public Page<ReplyDto> searchReplyPageOld(ReplySearchCondition condition, Pageable pageable) {
        List<ReplyDto> content = queryFactory
                .select(new QReplyDto(
                        post,
                        user,
                        reply,
                        count(replyLike),
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(count(replyLike))
                                        .from(replyLike)
                                        .where(replyLike.user.id.eq(condition.getUserId()))
                                , "isLiked"
                        )
                ))
                .from(reply)
                .leftJoin(post)
                .on(reply.post.id.eq(post.id)).fetchJoin()
                .leftJoin(user)
                .on(reply.user.id.eq(user.id)).fetchJoin()
                .leftJoin(replyLike)
                .on(replyLike.reply.id.eq(reply.id)).fetchJoin()
                .where(
                        reply.isDeleted.eq(NOT_DELETED),
                        replyContentKeyword(condition.getReplyContentKeyword()),
                        replyUser(condition.getReplyUserName()),
                        dateAfter(condition.getDateAfter())
                )
                .groupBy(reply.id)
                .orderBy(reply.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // count ??????
        JPAQuery<Reply> countQuery = queryFactory
                .selectFrom(reply)
                .leftJoin(post)
                .on(reply.post.id.eq(post.id)).fetchJoin()
                .where(
                        reply.isDeleted.eq(NOT_DELETED),
                        replyContentKeyword(condition.getReplyContentKeyword()),
                        replyUser(condition.getReplyUserName()),
                        dateAfter(condition.getDateAfter())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    private BooleanExpression replyUser(String replyUserName) {
        return replyUserName != null ? reply.user.nickName.contains(replyUserName) : null;
    }

    private BooleanExpression replyContentKeyword(String keyword) {
        return keyword != null ? reply.replyContent.contains(keyword) : null;
    }

    private BooleanExpression isQna(Integer validation) {
        return validation != null ? post.isQna.eq(validation) : null;
    }

    private BooleanExpression titleKeyword(String keyword) {
        return keyword != null ? post.postTitle.contains(keyword) : null;
    }

    private BooleanExpression contentKeyword(String keyword) {
        return keyword != null ? post.postContent.contains(keyword) : null;
    }

    private BooleanExpression codeKeyword(String keyword) {
        return keyword != null ? post.postCode.contains(keyword) : null;
    }

    private BooleanExpression dateAfter(LocalDateTime localDateTime) {
        return localDateTime != null ? post.createdDate.after(localDateTime) : null;
    }

    private BooleanExpression searchTag1(ArrayList<Long> categoryId) {
        return categoryId != null ? post.postTags.get(0).category.id.eq(categoryId.get(0)) : null;
    }

    private BooleanExpression searchTag2(ArrayList<Long> categoryId) {
        return categoryId != null ? post.postTags.get(1).category.id.eq(categoryId.get(1)) : null;
    }

    private BooleanExpression searchTag3(ArrayList<Long> categoryId) {
        return categoryId != null ? post.postTags.get(2).category.id.eq(categoryId.get(2)) : null;
    }

    private BooleanExpression searchTag4(ArrayList<Long> categoryId) {
        return categoryId != null ? post.postTags.get(3).category.id.eq(categoryId.get(3)) : null;
    }

    private BooleanExpression searchTag5(ArrayList<Long> categoryId) {
        return categoryId != null ? post.postTags.get(4).category.id.eq(categoryId.get(4)) : null;
    }

}
