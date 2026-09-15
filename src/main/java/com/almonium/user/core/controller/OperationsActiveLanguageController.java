package com.almonium.user.core.controller;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.auth.common.annotation.RequireRecentLogin;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.service.ActiveLanguageService;
import com.almonium.user.core.service.UserService;
import com.almonium.util.dto.ApiResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ops/users/{userId}/active-language-switch")
@RequiredArgsConstructor
@RequireRecentLogin
public class OperationsActiveLanguageController {
    private final ActiveLanguageService activeLanguageService;
    private final UserService userService;

    @PostMapping("/reset")
    public ResponseEntity<ApiResponse> reset(@PathVariable UUID userId, @Auth User operator) {
        activeLanguageService.resetSwitchCooldown(userService.getById(userId), operator);
        return ResponseEntity.ok(new ApiResponse(true, "Active-language switch cooldown cleared"));
    }
}
