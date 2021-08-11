package com.latte.server.auth.controller;

import com.latte.server.auth.dto.AuthRequestDto;
import com.latte.server.auth.dto.AuthResponseDto;
import com.latte.server.auth.dto.RefreshRequestDto;
import com.latte.server.auth.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Minky on 2021-08-04
 */

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<AuthResponseDto> signIn(@RequestBody AuthRequestDto authRequestDto) {
        return ResponseEntity.ok().body(authService.signIn(authRequestDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refreshing(@RequestBody RefreshRequestDto refreshRequestDto) {
        return ResponseEntity.ok().body(authService.refreshing(refreshRequestDto));
    }

    /**
     * TODO: Sign Out
     * DELETE
     */
}
