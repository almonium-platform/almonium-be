package com.almonium.auth.token.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.util.CookieUtil;
import com.almonium.auth.token.service.impl.AuthTokenService;
import com.almonium.util.dto.ApiResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RecentManualLoginVerificationController {
    AuthTokenService authTokenService;

    @GetMapping("/access-token/verify-live")
    public ResponseEntity<ApiResponse> verifyTokenLiveStatus(
            @NotBlank @CookieValue(value = CookieUtil.ACCESS_TOKEN_COOKIE_NAME, required = false) String accessToken) {
        if (accessToken == null || !authTokenService.validateToken(accessToken)) {
            return ResponseEntity.ok().body(new ApiResponse(false, null));
        }

        return authTokenService
                .recentLoginPrivilegeExpiresAt(accessToken)
                .map(expiresAtInstant -> ResponseEntity.ok().body(new ApiResponse(true, expiresAtInstant.toString())))
                .orElseGet(() -> ResponseEntity.ok().body(new ApiResponse(false, null)));
    }
}
