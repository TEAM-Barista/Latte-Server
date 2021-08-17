package com.latte.server.interview.repository;

import com.latte.server.interview.domain.Interview;
import com.latte.server.interview.domain.QInterview;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.latte.server.interview.domain.QInterview.*;

/**
 * Created by Donggun on 2021-08-05
 */

public class InterviewRepositoryImpl implements InterviewRepositoryCustom {
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
}
