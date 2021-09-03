package com.latte.server.push.repository;

import com.latte.server.push.domain.PushToken;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Minky on 2021-09-03
 */
public interface PushTokenRepository extends JpaRepository<PushToken, Long> {
}
