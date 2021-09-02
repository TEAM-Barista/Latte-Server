package com.latte.server.user.dto;

import com.latte.server.user.domain.User;
import com.latte.server.user.domain.UserCategory;
import com.latte.server.user.domain.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by Minky on 2021-09-02
 */

@NoArgsConstructor
@Getter
@Setter
public class UserCategoriesRequestDto {
    @NotNull(message = "userCategories cannot be null")
    private List<Long> userCategories;

    public UserCategoriesRequestDto(List<Long> userCategories) {
        this.userCategories = userCategories;
    }
}
