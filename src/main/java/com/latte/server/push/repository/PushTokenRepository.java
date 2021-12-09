package com.latte.server.push.repository;

import com.latte.server.push.domain.PushToken;
import com.latte.server.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by Minky on 2021-09-03
 */
public interface PushTokenRepository extends JpaRepository<PushToken, Long> {
    Optional<PushToken> findByUser(User user);
}
