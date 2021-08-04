package com.latte.server.auth.service;

import com.latte.server.auth.dto.AuthRequestDto;
import com.latte.server.auth.dto.AuthResponseDto;
import com.latte.server.auth.dto.RefreshRequestDto;
import com.latte.server.common.exception.CustomException;
import com.latte.server.common.security.JwtTokenProvider;
import com.latte.server.user.domain.User;
import com.latte.server.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Minky on 2021-08-04
 */

@Service
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class AuthService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponseDto signIn(AuthRequestDto authRequestDto) {
        User user = getUserByEmail(authRequestDto.getEmail());
        if (!passwordEncoder.matches(authRequestDto.getPassword(), user.getPassword())) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "Cannot find Password");
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail(), user.getUserRole().name());
        String refreshToken = jwtTokenProvider.createRefreshToken();

        return new AuthResponseDto(accessToken, refreshToken);
    }

    @Transactional
    public void signOut(AuthRequestDto authRequestDto) {
    }

    @Transactional
    public AuthResponseDto refreshing(RefreshRequestDto refreshRequestDto) {
        if (!jwtTokenProvider.validateToken(refreshRequestDto.getRefreshToken())) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "Refresh token expired");
        }

        String email = jwtTokenProvider.getUserPk(refreshRequestDto.getAccessToken());
        String role = jwtTokenProvider.getUserRole(refreshRequestDto.getAccessToken());

        String accessToken = jwtTokenProvider.createAccessToken(email, role);
        String refreshToken = jwtTokenProvider.createRefreshToken();

        return new AuthResponseDto(accessToken, refreshToken);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new CustomException(HttpStatus.UNAUTHORIZED, "Cannot find email: " + email));
    }
}
