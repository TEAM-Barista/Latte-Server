package com.latte.server.category.service;

import com.latte.server.category.domain.Category;
import com.latte.server.category.dto.CategoryRequestDto;
import com.latte.server.category.dto.CategoryResponseDto;
import com.latte.server.category.repository.CategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Minky on 2021-09-03
 */

@Service
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public Long create(CategoryRequestDto categoryRequestDto) {
        Category category = categoryRepository.save(categoryRequestDto.toEntity());
        return category.getId();
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getCategories(String kind) {
        if (kind == null) {
            List<Category> categoryList = categoryRepository.findAll();
            return CategoryResponseDto.listOf(categoryList);
        }

        List<Category> categoryList = categoryRepository.findByKind(kind);
        return CategoryResponseDto.listOf(categoryList);
    }
}
