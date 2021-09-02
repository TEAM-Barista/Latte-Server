package com.latte.server.user.domain;

import com.latte.server.category.domain.Category;
import lombok.*;

import javax.persistence.*;

/**
 * Created by Minky on 2021-09-02
 */

/**
 * Weak Entity
 */

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
@Entity
public class UserCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_category_sequence_gen")
    @SequenceGenerator(name = "user_category_sequence_gen", sequenceName = "user_category_sequence")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder
    public UserCategory(Long id, User user, Category category) {
        this.id = id;
        this.user = user;
        this.category = category;
    }
}
