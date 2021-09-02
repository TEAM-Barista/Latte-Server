package com.latte.server.category.dto;

import com.latte.server.category.domain.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Minky on 2021-09-02
 */
@NoArgsConstructor
@Setter
@Getter
public class CategoryResponseDto {
    private Long id;
    private String category;
    private String kind;

    public CategoryResponseDto(Long id, String category, String kind) {
        this.id = id;
        this.category = category;
        this.kind = kind;
    }

    public static CategoryResponseDto of(Category category) {
        return new CategoryResponseDto(
                category.getId(),
                category.getCategory(),
                category.getKind()
        );
    }

    public static List<CategoryResponseDto> listOf(List<Category> categoryList) {
        return categoryList
                .stream()
                .map(CategoryResponseDto::of)
                .collect(Collectors.toList());
    }
}
