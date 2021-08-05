package com.latte.server.interview.domain;


import com.latte.server.common.domain.BaseTimeEntity;
import com.latte.server.post.domain.Post;
import com.latte.server.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by Donggun on 2021-08-05
 */

@Entity
@Table(name = "INTERVIEW_BOOKMARK_TB")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterviewBookmark extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "interview_bookmark_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id")
    private Interview interview;

    private InterviewBookmark(User user, Interview interview) {
        this.user = user;
        this.interview = interview;
    }

    // == 생성 메서드 == //
    public static InterviewBookmark createInterviewBookmark(Interview interview, User user) {
        return new InterviewBookmark(user, interview);
    }
}
