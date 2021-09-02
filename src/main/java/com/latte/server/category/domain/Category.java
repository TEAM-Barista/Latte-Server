package com.latte.server.category.domain;

import com.latte.server.common.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by Donggun on 2021-08-05
 */

@Entity
@Table(name = "CATEGORY_TB")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String category;
    private String kind;

    private Category(String category, String kind) {
        this.category = category;
        this.kind = kind;
    }

    public static Category createCategory(String category, String kind) {
        return new Category(category, kind);
    }
}
