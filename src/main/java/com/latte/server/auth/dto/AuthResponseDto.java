package com.latte.server.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by Minky on 2021-08-04
 */

@NoArgsConstructor
@Getter
public class AuthResponseDto {
    private String accessToken;
    private String refreshToken;

    public AuthResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
