package com.latte.server.interview.domain;

import com.latte.server.category.domain.Category;
import com.latte.server.common.domain.BaseTimeEntity;
import com.latte.server.post.domain.Post;
import lombok.Getter;

import javax.persistence.*;

/**
 * Created by Donggun on 2021-08-05
 */

@Entity
@Table(name = "INTERVIEW_TAG_TB")
@Getter
public class InterviewTag extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "interview_tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
