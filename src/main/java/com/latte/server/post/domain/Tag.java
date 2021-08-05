package com.latte.server.post.domain;

import com.latte.server.category.domain.Category;
import com.latte.server.common.domain.BaseTimeEntity;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by Donggun on 2021-08-05
 */

@Entity
@Table(name = "TAG_TB")
@Getter
public class Tag extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
