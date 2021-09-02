package com.latte.server.post.repository;

import com.latte.server.post.domain.*;
import com.latte.server.post.dto.PostListDto;
import com.latte.server.post.dto.PostSearchCondition;
import com.latte.server.post.dto.QPostListDto;
import com.latte.server.user.domain.QUser;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static com.latte.server.post.domain.QBookmark.*;
import static com.latte.server.post.domain.QPost.*;
import static com.latte.server.post.domain.QReply.reply;
import static com.latte.server.user.domain.QUser.*;
import static com.querydsl.core.types.ExpressionUtils.count;

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
                        dateAfter(condition.getDateAfter())
                )
                .groupBy(post.id)
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // count 쿼리
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

        // count 쿼리
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

        // count 쿼리
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
}