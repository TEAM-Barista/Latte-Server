package com.latte.server.user.repository;

import com.latte.server.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Donggun on 2021-08-05
 */

public interface UserRepository extends JpaRepository<User, Long> {
}
