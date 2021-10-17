package com.latte.server.interview.repository;

import com.latte.server.interview.domain.*;
import com.latte.server.interview.dto.*;
import com.latte.server.post.domain.Post;
import com.latte.server.post.dto.PostListDto;
import com.latte.server.post.dto.QPostListDto;
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
import java.util.List;

import static com.latte.server.interview.domain.QInterview.*;
import static com.latte.server.interview.domain.QInterviewBookmark.*;
import static com.latte.server.interview.domain.QInterviewLike.*;
import static com.latte.server.post.domain.QBookmark.bookmark;
import static com.latte.server.post.domain.QPost.post;
import static com.latte.server.post.domain.QReply.reply;
import static com.querydsl.core.types.ExpressionUtils.count;

/**
 * Created by Donggun on 2021-08-05
 */

public class InterviewRepositoryImpl implements InterviewRepositoryCustom {
    private static final Integer NOT_DELETED = 0;

    private final JPAQueryFactory queryFactory;

    public InterviewRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Interview findInterviewByCreatedDate() {
        return queryFactory
                .selectFrom(interview)
                .orderBy(interview.createdDate.desc())
                .limit(1)
                .fetchOne();
    }

    @Override
    public Page<InterviewListDto> searchInterviewPageRecent(InterviewSearchCondition condition, Pageable pageable) {
        List<InterviewListDto> content = queryFactory
                .select(new QInterviewListDto(
                        interview,
                        count(interviewLike),
                        count(interviewBookmark),
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(count(interviewLike))
                                        .from(interviewLike)
                                        .where(interviewLike.user.id.eq(condition.getUserId()))
                                , "isLiked"
                        ),
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(count(interviewBookmark))
                                        .from(interviewBookmark)
                                        .where(interviewBookmark.user.id.eq(condition.getUserId()))
                                , "isBookmarked"
                        )
                ))
                .from(interview)
                .leftJoin(interviewLike)
                .on(interviewLike.interview.id.eq(interview.id)).fetchJoin()
                .leftJoin(interviewBookmark)
                .on(interviewBookmark.interview.id.eq(interview.id)).fetchJoin()
                .where(
                        interview.isDeleted.eq(NOT_DELETED),
                        titleKeyword(condition.getTitleKeyword()),
                        contentKeyword(condition.getContentKeyword()),
                        dateAfter(condition.getDateAfter()),
                        dateBefore(condition.getDateBefore())
                )
                .groupBy(interview.id)
                .orderBy(interview.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // count 쿼리
        JPAQuery<Interview> countQuery = queryFactory
                .selectFrom(interview)
                .where(
                        interview.isDeleted.eq(NOT_DELETED),
                        titleKeyword(condition.getTitleKeyword()),
                        contentKeyword(condition.getContentKeyword()),
                        dateAfter(condition.getDateAfter()),
                        dateBefore(condition.getDateBefore())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    @Override
    public Page<InterviewListDto> searchInterviewPageRecommend(InterviewSearchCondition condition, Pageable pageable) {
        List<InterviewListDto> content = queryFactory
                .select(new QInterviewListDto(
                        interview,
                        count(interviewLike),
                        count(interviewBookmark),
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(count(interviewLike))
                                        .from(interviewLike)
                                        .where(interviewLike.user.id.eq(condition.getUserId()))
                                , "isLiked"
                        ),
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(count(interviewBookmark))
                                        .from(interviewBookmark)
                                        .where(interviewBookmark.user.id.eq(condition.getUserId()))
                                , "isBookmarked"
                        )
                ))
                .from(interview)
                .leftJoin(interviewLike)
                .on(interviewLike.interview.id.eq(interview.id)).fetchJoin()
                .leftJoin(interviewBookmark)
                .on(interviewBookmark.interview.id.eq(interview.id)).fetchJoin()
                .where(
                        interview.isDeleted.eq(NOT_DELETED),
                        dateBefore(condition.getDateBefore()),
                        containsTag(condition.getInterviewTag())
                )
                .groupBy(interview.id)
                .orderBy(interview.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // count 쿼리
        JPAQuery<Interview> countQuery = queryFactory
                .selectFrom(interview)
                .where(
                        interview.isDeleted.eq(NOT_DELETED),
                        dateBefore(condition.getDateBefore()),
                        containsTag(condition.getInterviewTag())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    private BooleanExpression containsTag(InterviewTag interviewTag) {
        return interviewTag != null ? interview.interviewTags.contains(interviewTag) : null;
    }

    private BooleanExpression titleKeyword(String keyword) {
        return keyword != null ? interview.interviewTitle.contains(keyword) : null;
    }

    private BooleanExpression contentKeyword(String keyword) {
        return keyword != null ? interview.interviewContent.contains(keyword) : null;
    }

    private BooleanExpression dateAfter(LocalDateTime localDateTime) {
        return localDateTime != null ? interview.createdDate.after(localDateTime) : null;
    }

    private BooleanExpression dateBefore(LocalDateTime localDateTime) {
        return localDateTime != null ? interview.createdDate.before(localDateTime) : null;
    }
}
