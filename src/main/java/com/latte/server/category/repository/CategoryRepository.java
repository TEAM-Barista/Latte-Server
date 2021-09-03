package com.latte.server.category.repository;

import com.latte.server.category.domain.Category;
import com.latte.server.user.domain.UserCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByIdIn(List<Long> categoryIds);
    List<Category> findByKind(String kind);
}
