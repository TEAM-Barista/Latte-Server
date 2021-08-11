package com.latte.server.user.controller;

import com.latte.server.user.dto.UserProfileImageUrlRequestDto;
import com.latte.server.user.dto.UserRequestDto;
import com.latte.server.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.ws.Response;
import java.net.URI;

/**
 * Created by Minky on 2021-07-27
 */

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Void> createUser(
            @Valid @RequestBody UserRequestDto userRequestDto
    ) {
        Long userId = userService.create(userRequestDto);
        return ResponseEntity.created(URI.create("/api/users/" + userId)).build();
    }

    @PatchMapping("profile")
    public ResponseEntity<Void> setProfileImage(
            @AuthenticationPrincipal String email,
            @Valid @RequestBody UserProfileImageUrlRequestDto userProfileImageUrlRequestDto
    ) {
        userService.setProfileImage(userProfileImageUrlRequestDto, email);
        return ResponseEntity.ok().build();
    }

    /**
     * TODO: Edit User Profile
     * PUT AND PATCH
     */

    /**
     * TODO: Delete User (We muse choice logic about disable or hard delete)
     * DELETE
     */

    /**
     * TODO: Edit User Hashtag
     */

}
