package com.latte.server.common.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "POST_LIKE_TB")
@Getter
public class PostLike {

    @Id
    @GeneratedValue
    @Column(name = "post_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "uid")
    private User user;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
