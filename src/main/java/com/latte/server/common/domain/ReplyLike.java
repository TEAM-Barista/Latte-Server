package com.latte.server.common.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "REPLY_LIKE_TB")
@Getter
public class ReplyLike {

    @Id
    @GeneratedValue
    @Column(name = "reply_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_id")
    private Reply reply;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    private User user;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
