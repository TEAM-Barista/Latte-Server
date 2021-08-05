package com.latte.server.post.domain;

import com.latte.server.common.domain.BaseTimeEntity;
import com.latte.server.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by Donggun on 2021-08-05
 */

@Entity
@Table(name = "POST_TB")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    private User user;

    @Column(name = "post_content")
    private String postContent;

    @Column(name = "post_title")
    private String postTitle;

    @Column(name = "post_code")
    private String postCode;

    @Column(name = "is_deleted")
    private int isDeleted;

    @Column(name = "is_qna")
    private int isQna;

    private Post(User user, String postContent, String postTitle, String postCode, int isDeleted, int isQna) {
        this.user = user;
        this.postContent = postContent;
        this.postTitle = postTitle;
        this.postCode = postCode;
        this.isDeleted = isDeleted;
        this.isQna = isQna;
    }

    // == 생성 메서드 == //
    public static Post createPost(User user, String postContent, String postTitle, String postCode, int isQna) {
        return new Post(user, postContent, postTitle, postCode, 0, isQna);
    }

    public void changePost(String postContent, String postTitle, String postCode, int isQna) {
        this.postContent = postContent;
        this.postTitle = postTitle;
        this.postCode = postCode;
        this.isQna = isQna;
    }

    public void deletePost() {
        this.isDeleted = 1;
    }
}
