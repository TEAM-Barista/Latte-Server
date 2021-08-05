package com.latte.server.category.domain;

import com.latte.server.common.domain.BaseTimeEntity;
import lombok.Getter;

import javax.persistence.*;

/**
 * Created by Donggun on 2021-08-05
 */

@Entity
@Table(name = "CATEGORY_TB")
@Getter
public class Category extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String category;
    private String kind;
}
