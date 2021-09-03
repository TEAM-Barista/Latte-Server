package com.latte.server.post.domain;

import com.latte.server.common.domain.BaseTimeEntity;
import com.latte.server.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by Donggun on 2021-08-05
 */

@Entity
@Table(name = "REPLY_LIKE_TB")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    private ReplyLike(Reply reply, User user) {
        this.reply = reply;
        this.user = user;
    }

    // == 생성 메서드 == //
    public static ReplyLike createReplyLike(Reply reply, User user) {
        return new ReplyLike(reply, user);
    }
}
