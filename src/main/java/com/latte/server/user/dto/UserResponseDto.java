package com.latte.server.user.dto;

import com.latte.server.user.domain.User;
import com.latte.server.user.domain.UserCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


/**
 * Created by Minky on 2021-08-11
 */

@NoArgsConstructor
@Setter
@Getter
public class UserResponseDto {
    private Long id;
    private String nickName;
    private String email;
    private String profileImageUrl;
    private List<UserCategoryResponseDto> userCategoryList;

    public UserResponseDto(
            Long id,
            String nickName,
            String email,
            String profileImageUrl,
            List<UserCategoryResponseDto> userCategoryList
    ) {
        this.id = id;
        this.nickName = nickName;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.userCategoryList = userCategoryList;
    }

    public static UserResponseDto of(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getNickName(),
                user.getEmail(),
                user.getProfileImageUrl(),
                UserCategoryResponseDto.listOf(user.getUserCategories())
        );
    }
}
