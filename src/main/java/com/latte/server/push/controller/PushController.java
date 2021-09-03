package com.latte.server.push.controller;

import com.latte.server.push.domain.PushToken;
import com.latte.server.push.service.PushTokenService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * Created by Minky on 2021-09-03
 */

@RestController
@RequestMapping("/api/push")
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class PushController {

    private final PushTokenService pushTokenService;

    @PostMapping
    public ResponseEntity<Void> uploadPushToken(
            @RequestHeader(value = "X-PUSH-TOKEN") String token,
            @AuthenticationPrincipal String email
    ) {
        Long id = pushTokenService.create(token, email);
        return ResponseEntity.created(URI.create("/api/push/" + id)).build();
    }
}
