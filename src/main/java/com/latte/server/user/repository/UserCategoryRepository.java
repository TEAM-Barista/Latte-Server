package com.latte.server.user.repository;

import com.latte.server.category.domain.Category;
import com.latte.server.user.domain.User;
import com.latte.server.user.domain.UserCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Minky on 2021-09-02
 */
public interface UserCategoryRepository extends JpaRepository<UserCategory, Long> {
    Optional<UserCategory> findByUserAndCategory(User user, Category category);
    List<UserCategory> findByUser(User user);
}
