package com.almonium.subscription.controller;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.auth.common.annotation.RequireRecentLogin;
import com.almonium.subscription.dto.request.AccessGrantRequest;
import com.almonium.subscription.service.AccessGrantService;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.service.UserService;
import com.almonium.util.dto.ApiResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ops/users/{userId}/access-grant")
@RequiredArgsConstructor
public class OperationsAccessController {
    private final AccessGrantService accessGrantService;
    private final UserService userService;

    @PostMapping
    @RequireRecentLogin
    public ResponseEntity<ApiResponse> replace(
            @PathVariable UUID userId, @Valid @RequestBody AccessGrantRequest request, @Auth User operator) {
        accessGrantService.replace(
                userService.getById(userId), operator, request.entitlement(), request.expiresAt(), request.reason());
        return ResponseEntity.ok(new ApiResponse(true, "Access grant updated"));
    }

    @DeleteMapping
    @RequireRecentLogin
    public ResponseEntity<ApiResponse> revoke(@PathVariable UUID userId) {
        accessGrantService.revoke(userService.getById(userId));
        return ResponseEntity.ok(new ApiResponse(true, "Access grant revoked"));
    }
}
