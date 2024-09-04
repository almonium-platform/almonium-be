package com.almonium.auth.token.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.util.CookieUtil;
import com.almonium.auth.token.dto.response.JwtTokenResponse;
import com.almonium.auth.token.service.impl.AuthTokenService;
import com.almonium.util.dto.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RefreshTokenController {
    AuthTokenService tokenService;

    @PostMapping("${app.auth.jwt.refresh-token-url}")
    public ResponseEntity<?> refreshToken(
            @CookieValue(value = CookieUtil.REFRESH_TOKEN_COOKIE_NAME, required = false) String refreshToken,
            HttpServletResponse response) {

        if (refreshToken == null || !tokenService.validateToken(refreshToken)) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "invalid_grant"));
        }

        Authentication authentication = tokenService.getAuthenticationFromToken(refreshToken);
        String newAccessToken = tokenService.createAndSetAccessToken(authentication, response);

        return ResponseEntity.ok(new JwtTokenResponse(newAccessToken, refreshToken));
    }
}
