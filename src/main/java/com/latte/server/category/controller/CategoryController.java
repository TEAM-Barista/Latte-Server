package com.latte.server.category.controller;

import com.latte.server.category.dto.CategoryRequestDto;
import com.latte.server.category.dto.CategoryResponseDto;
import com.latte.server.category.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

/**
 * Created by Minky on 2021-09-03
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getCategories(
            @RequestParam(value = "eqKind", required = false) String kind
    ) {
        return ResponseEntity.ok(categoryService.getCategories(kind));
    }

    @PostMapping
    public ResponseEntity<Long> uploadCategory(
            @Valid @RequestBody CategoryRequestDto categoryRequestDto
    ) {
        Long id = categoryService.create(categoryRequestDto);
        return ResponseEntity.created(URI.create("/api/categories/" + id)).build();
    }
}
