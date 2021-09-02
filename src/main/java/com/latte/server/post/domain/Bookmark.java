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
@Table(name = "BOOKMARK_TB")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "bookmark_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private Bookmark(User user, Post post) {
        this.user = user;
        this.post = post;
    }

    // == 생성 메서드 == //
    public static Bookmark createBookmark(User user, Post post) {
        return new Bookmark(user, post);
    }
}
