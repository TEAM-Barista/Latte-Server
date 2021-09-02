package com.latte.server.interview.domain;

import com.latte.server.common.domain.BaseTimeEntity;
import com.latte.server.interview.domain.Interview;
import com.latte.server.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by Donggun on 2021-08-05
 */

@Entity
@Table(name = "INTERVIEW_LIKE_TB")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterviewLike extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "interview_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id")
    private Interview interview;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    private User user;

    private InterviewLike(Interview interview, User user) {
        this.interview = interview;
        this.user = user;
    }

    // == 생성 메서드 == //
    public static InterviewLike createInterviewLike(Interview interview, User user) {
        return new InterviewLike(interview, user);
    }
}
