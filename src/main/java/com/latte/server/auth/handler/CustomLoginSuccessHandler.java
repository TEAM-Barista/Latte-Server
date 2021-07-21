package com.latte.server.auth.handler;

import com.latte.server.auth.constants.AuthConstants;
import com.latte.server.auth.util.TokenUtils;
import com.latte.server.user.domain.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Minky on 2021-07-21
 */

/**
 * 이렇게 직접 제작한 Filter를 이제 적용시켜야 하므로 UsernamePasswordAuthenticationFilter 필터 이전에 적용시켜야 한다.
 * 그리고 해당 CustomAuthenticationFilter가 수행된 후에 처리될 Handler 역시 Bean으로 등록하고
 * CustomAuthenticationFilter의 핸들러로 추가해주어야 하는데, 해당 코드들은 WebSecurityConfig에 아래와 같이 추가해줄 수 있다.
 */

@Log4j2
public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request,
                                        final HttpServletResponse response,
                                        final Authentication authentication
    ) {
        final User user = ((User) authentication.getPrincipal());
        final String token = TokenUtils.generateJwtToken(user);
        response.addHeader(AuthConstants.AUTH_HEADER, AuthConstants.TOKEN_TYPE + " " + token);
    }
}
