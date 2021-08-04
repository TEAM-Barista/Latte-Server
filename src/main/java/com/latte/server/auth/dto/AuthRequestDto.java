package com.latte.server.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by Minky on 2021-08-04
 */

@NoArgsConstructor
@Getter
public class AuthRequestDto {
    public String email;
    public String password;

    public AuthRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
