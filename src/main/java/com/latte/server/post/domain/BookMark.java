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
@Table(name = "BOOKMARK_TB")
@Getter
public class BookMark extends BaseTimeEntity {

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
}
