package com.latte.server.category.domain;

import com.latte.server.common.domain.BaseTimeEntity;
import com.latte.server.user.domain.User;
import com.latte.server.user.domain.UserCategory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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

    @OneToMany(mappedBy = "category", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<UserCategory> userCategories;

    public Category(String category, String kind) {
        this.category = category;
        this.kind = kind;
    }

    public static Category createCategory(String category, String kind) {
        return new Category(category, kind);
    }
}
