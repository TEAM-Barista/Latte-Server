package com.latte.server.user.dto;

import com.latte.server.user.domain.User;
import com.latte.server.user.domain.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * Created by Minky on 2021-07-27
 */

@NoArgsConstructor
@Getter
@Setter
public class UserRequestDto {
    @NotBlank(message = "nickName cannot be null")
    private String nickName;

    @Email(message = "email format is not valid")
    private String email;

    @NotBlank(message = "password cannot be null")
    private String password;

    public UserRequestDto(String nickName, String email, String password) {
        this.nickName = nickName;
        this.email = email;
        this.password = password;
    }

    public User toEntity() {
        return new User(
                null,
                this.nickName,
                this.email,
                toEncodePassword(),
                UserRole.CLIENT,
                false
        );
    }

    private String toEncodePassword() {
        return new BCryptPasswordEncoder().encode(password);
    }
}
