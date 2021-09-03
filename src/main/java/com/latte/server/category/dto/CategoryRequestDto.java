package com.latte.server.category.dto;

import com.latte.server.category.domain.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * Created by Minky on 2021-09-03
 */

@NoArgsConstructor
@Getter
@Setter
public class CategoryRequestDto {
    @NotBlank(message = "category cannot be blank")
    private String category;
    @NotBlank(message = "kind cannot be blank")
    private String kind;

    public CategoryRequestDto(String category, String kind) {
        this.category = category;
        this.kind = kind;
    }

    public Category toEntity() {
        return new Category(this.category, this.kind);
    }
}
