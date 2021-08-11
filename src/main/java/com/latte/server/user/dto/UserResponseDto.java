package com.latte.server.user.dto;

import com.latte.server.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


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

    public UserResponseDto(
            Long id,
            String nickName,
            String email,
            String profileImageUrl
    ) {
        this.id = id;
        this.nickName = nickName;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
    }

    public static UserResponseDto of(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getNickName(),
                user.getEmail(),
                user.getProfileImageUrl()
        );
    }
}
