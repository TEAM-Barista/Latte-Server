package com.latte.server.common.interceptor;

import com.latte.server.auth.constants.AuthConstants;
import com.latte.server.auth.util.TokenUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Minky on 2021-07-22
 */

@Log4j2
public class JwtTokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler
    ) throws IOException {
        final String header = request.getHeader(AuthConstants.AUTH_HEADER);
        if (header != null) {
            // 헤더 존재
            final String token = TokenUtils.getTokenFromHeader(header);
            if (TokenUtils.isValidToken(token)) { // 헤더 값 검증
                return true;
            }
        }
        response.sendRedirect("/error/unauthorized");
        return false;
    }

}
