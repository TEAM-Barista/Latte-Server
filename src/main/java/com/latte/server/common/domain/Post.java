package com.latte.server.common.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "POST_TB")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    private User user;

    @Column(name = "post_content")
    private String postContent;

    @Column(name = "post_hit")
    private int postHit;

    @Column(name = "post_title")
    private String postTitle;

    @Column(name = "post_code")
    private String postCode;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "is_deleted")
    private int isDeleted;

    @Column(name = "is_qna")
    private int isQna;

    // == 생성 메서드 == //
    public static Post createPost(User user, String postContent, int postHit, String postTitle, String postCode) {
        Post post = new Post();
        post.setUser(user);
        post.setPostContent(postContent);
        post.setPostHit(postHit);
        post.setPostTitle(postTitle);
        post.setPostCode(postCode);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        post.setDeletedAt(LocalDateTime.now());
        post.setIsDeleted(0);
        return post;
    }
}
