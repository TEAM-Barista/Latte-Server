package com.latte.server.common.security;

import com.latte.server.common.exception.CustomException;
import com.latte.server.common.exception.custom.NotFoundEmailException;
import com.latte.server.user.domain.User;
import com.latte.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Created by Minky on 2021-07-21
 */


@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(NotFoundEmailException::new);
    }

}
