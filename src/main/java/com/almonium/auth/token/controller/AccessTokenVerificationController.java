package com.almonium.auth.token.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.util.CookieUtil;
import com.almonium.auth.token.service.impl.AuthTokenService;
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
public class AccessTokenVerificationController {
    AuthTokenService authTokenService;

    @GetMapping("/access-token/verify-live")
    public ResponseEntity<Boolean> verifyLiveStatus(
            @NotBlank @CookieValue(value = CookieUtil.ACCESS_TOKEN_COOKIE_NAME, required = false) String accessToken) {
        if (accessToken == null || !authTokenService.validateToken(accessToken)) {
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(
                authTokenService.isAccessTokenLive(accessToken)); // special filter? for live token actions
    }
}
