package com.latte.server.user.dto;

import com.latte.server.user.domain.User;
import com.latte.server.user.domain.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * Created by Minky on 2021-08-11
 */

@NoArgsConstructor
@Getter
@Setter
public class UserProfileImageUrlRequestDto {
    @NotBlank(message = "profileImageUrl cannot be null")
    private String profileImageUrl;

    public UserProfileImageUrlRequestDto(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
