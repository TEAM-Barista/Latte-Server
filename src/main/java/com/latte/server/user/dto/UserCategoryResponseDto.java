package com.latte.server.user.dto;

import com.latte.server.category.dto.CategoryResponseDto;
import com.latte.server.user.domain.UserCategory;
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
public class UserCategoryResponseDto {
    private Long id;
    private UserResponseDto user;
    private CategoryResponseDto category;

    public UserCategoryResponseDto(
            Long id,
            UserResponseDto user,
            CategoryResponseDto category
    ) {
        this.id = id;
        this.user = user;
        this.category = category;
    }

    public static UserCategoryResponseDto of(UserCategory userCategory) {
        return new UserCategoryResponseDto(
                userCategory.getId(),
                UserResponseDto.of(userCategory.getUser()),
                CategoryResponseDto.of(userCategory.getCategory())
        );
    }

    public static List<UserCategoryResponseDto> listOf(List<UserCategory> userCategoryList) {
        return userCategoryList
                .stream()
                .map(UserCategoryResponseDto::of)
                .collect(Collectors.toList());
    }
}
