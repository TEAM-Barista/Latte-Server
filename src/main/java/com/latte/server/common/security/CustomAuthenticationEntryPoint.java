package com.latte.server.common.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Minky on 2021-08-04
 */

/**
 * 인증 관련 엔트리 포인트 로직
 */

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException,
            ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed. Must check your token.");
    }
}