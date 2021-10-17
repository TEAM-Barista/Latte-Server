package com.latte.server.interview.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.latte.server.category.domain.Category;
import com.latte.server.common.domain.BaseTimeEntity;
import com.latte.server.post.domain.Post;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by Donggun on 2021-08-05
 */

@Entity
@Table(name = "INTERVIEW_TAG_TB")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterviewTag extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "interview_tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "interview_id")
    private Interview interview;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;

    private InterviewTag(Interview interview, Category category) {
        this.interview = interview;
        this.category = category;
    }

    // == 연관관계 편의 메서드 == //
    public void changeInterview(Interview interview) {
        this.interview = interview;
    }

    // == 생성 메서드 == //
    public static InterviewTag createInterviewTag(Interview interview, Category category) {
        return new InterviewTag(interview, category);
    }
}
