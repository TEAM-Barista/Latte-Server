package com.latte.server.post.domain;

import com.latte.server.common.domain.BaseTimeEntity;
import com.latte.server.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by Donggun on 2021-08-05
 */

@Entity
@Table(name = "REPLY_TB")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "reply_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private int depth;

    @Column(name = "bundle_id")
    private int bundleId;

    @Column(name = "bundle_order")
    private int bundleOrder;

    @Column(name = "reply_content")
    private String replyContent;

    @Column(name = "is_deleted")
    private int isDeleted;

    private Reply(User user, Post post, int depth, int bundleId, int bundleOrder, String replyContent, int isDeleted) {
        this.user = user;
        this.post = post;
        this.depth = depth;
        this.bundleId = bundleId;
        this.bundleOrder = bundleOrder;
        this.replyContent = replyContent;
        this.isDeleted = isDeleted;
    }

    // == 생성 메서드 == //
    public static Reply createNewReply(User user, Post post, String replyContent, int isDeleted) {
        return new Reply(user, post, 0, 0, 0, replyContent, isDeleted);
    }
}
