package com.latte.server.interview.domain;


import com.latte.server.common.domain.BaseTimeEntity;
import com.latte.server.post.domain.Tag;
import com.latte.server.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Donggun on 2021-08-05
 */

@Entity
@Table(name = "INTERVIEW_TB")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Interview extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "interview_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    private User user;

    @Column(name = "interview_content")
    private String interviewContent;

    @Column(name = "interview_title")
    private String interviewTitle;

    @Column(name = "interview_tags")
    @OneToMany(mappedBy = "interview", cascade = CascadeType.ALL)
    private List<InterviewTag> interviewTags = new ArrayList<>();

    @Column(name = "is_deleted")
    private int isDeleted;

    private Interview(User user, String interviewContent, String interviewTitle, int isDeleted) {
        this.user = user;
        this.interviewContent = interviewContent;
        this.interviewTitle = interviewTitle;
        this.isDeleted = isDeleted;
    }

    // == 연관관계 편의 메서드 == //
    public void addInterviewTag(InterviewTag tag) {
        interviewTags.add(tag);
        tag.changeInterview(this);
    }

    public void clearInterviewTag() {
        interviewTags = new ArrayList<>();
    }

    // == 생성 메서드 == //
    public static Interview createInterview(User user, String interviewContent, String interviewTitle) {
        return new Interview(user, interviewContent, interviewTitle, 0);
    }
}
