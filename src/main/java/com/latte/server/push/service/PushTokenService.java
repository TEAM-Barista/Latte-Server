package com.latte.server.push.service;

import com.latte.server.common.exception.custom.NotFoundEmailException;
import com.latte.server.push.domain.PushToken;
import com.latte.server.push.repository.PushTokenRepository;
import com.latte.server.user.domain.User;
import com.latte.server.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Minky on 2021-09-03
 */

@Service
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class PushTokenService {
    private final PushTokenRepository pushTokenRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long create(String token, String email) {
        // TODO: token 검증 로직 추가

        User user = userRepository.findByEmail(email)
                .orElseThrow(NotFoundEmailException::new);

        PushToken pushToken = new PushToken(null, token, user);
        pushTokenRepository.save(pushToken);

        return pushToken.getId();
    }
}
