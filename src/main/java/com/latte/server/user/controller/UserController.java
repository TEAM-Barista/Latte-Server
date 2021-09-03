package com.latte.server.user.controller;

import com.latte.server.category.domain.Category;
import com.latte.server.user.domain.UserCategory;
import com.latte.server.user.dto.*;
import com.latte.server.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

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

    @PatchMapping("/profile")
    public ResponseEntity<Void> setProfileImage(
            @AuthenticationPrincipal String email,
            @Valid @RequestBody UserProfileImageUrlRequestDto userProfileImageUrlRequestDto
    ) {
        userService.setProfileImage(userProfileImageUrlRequestDto, email);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/notify")
    public ResponseEntity<Void> setAccessNotify(
            @AuthenticationPrincipal String email,
            @Valid @RequestBody UserAccessNotifyRequestDto UserAccessNotifyRequestDto
    ) {
        userService.setAccessNotify(UserAccessNotifyRequestDto, email);
        return ResponseEntity.ok().build();
    }

    /**
     * TODO: Delete User Profile
     * Delete
     */

    @GetMapping("/categories")
    public ResponseEntity<List<UserCategoryResponseDto>> getUserCategories(
            @AuthenticationPrincipal String email
    ) {
        return ResponseEntity.ok(userService.getUserCategories(email));
    }

    @PatchMapping("/categories")
    public ResponseEntity<Void> setUserCategories(
            @AuthenticationPrincipal String email,
            @Valid @RequestBody UserCategoriesRequestDto userCategoriesRequestDto
    ) {
        userService.setUserCategories(userCategoriesRequestDto, email);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/categories/{id}")
    public ResponseEntity<Void> setUserCategory(
            @AuthenticationPrincipal String email,
            @PathVariable Long id
    ) {
        userService.setUserCategory(id, email);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteUserCategory(
            @AuthenticationPrincipal String email,
            @PathVariable Long id
    ) {
        userService.deleteUserCategory(id, email);
        return ResponseEntity.ok().build();
    }

    /**
     * TODO: Delete User (We muse choice logic about disable or hard delete)
     * DELETE
     */
}
