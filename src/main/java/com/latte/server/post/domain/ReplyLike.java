package com.latte.server.post.domain;

import com.latte.server.common.domain.BaseTimeEntity;
import com.latte.server.user.domain.User;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by Donggun on 2021-08-05
 */

@Entity
@Table(name = "REPLY_LIKE_TB")
@Getter
public class ReplyLike extends BaseTimeEntity {

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
