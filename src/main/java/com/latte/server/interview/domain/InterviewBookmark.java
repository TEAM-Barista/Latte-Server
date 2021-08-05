package com.latte.server.interview.domain;


import com.latte.server.common.domain.BaseTimeEntity;
import com.latte.server.post.domain.Post;
import com.latte.server.user.domain.User;
import lombok.Getter;

import javax.persistence.*;

/**
 * Created by Donggun on 2021-08-05
 */

@Entity
@Table(name = "INTERVIEW_BOOKMARK_TB")
@Getter
public class InterviewBookMark extends BaseTimeEntity {

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
}
