package com.latte.server.auth.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Created by Minky on 2021-07-21
 */

/**
 * 인증과 관련해 자주 사용되는 상수 정의 클래스
 * TOKEN 관련 상수
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthConstants {

    public static final String AUTH_HEADER = "Authorization";
    public static final String TOKEN_TYPE = "BEARER";

}
