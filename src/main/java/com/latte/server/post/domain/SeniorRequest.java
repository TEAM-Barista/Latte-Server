package com.latte.server.post.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.latte.server.category.domain.Category;
import com.latte.server.common.domain.BaseTimeEntity;
import com.latte.server.interview.domain.Interview;
import com.latte.server.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by Donggun on 2021-09-13
 */

@Entity
@Table(name = "SENIOR_REQUEST_TB")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeniorRequest extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "senior_request_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id")
    private Interview interview;

    @Column(name = "senior_request_title")
    private String seniorRequestTitle;

    @Column(name = "senior_request_content")
    private String seniorRequestContent;

    private SeniorRequest(User user, Interview interview, String seniorRequestTitle, String seniorRequestContent) {
        this.user = user;
        this.interview = interview;
        this.seniorRequestTitle = seniorRequestTitle;
        this.seniorRequestContent = seniorRequestContent;
    }

    // == 생성 메서드 == //
    public static SeniorRequest createSeniorRequest(User user, Interview interview, String seniorRequestTitle, String seniorRequestContent) {
        return new SeniorRequest(user, interview, seniorRequestTitle, seniorRequestContent);
    }
}
