package com.almonium.auth.token.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.util.CookieUtil;
import com.almonium.auth.token.dto.response.JwtTokenResponse;
import com.almonium.auth.token.service.AuthTokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth")
@RestController
@RequestMapping
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RefreshTokenController {
    AuthTokenService tokenService;

    @PostMapping("${app.auth.jwt.refresh-token.url}")
    public ResponseEntity<JwtTokenResponse> refreshToken(
            @CookieValue(value = CookieUtil.REFRESH_TOKEN_COOKIE_NAME, required = false) String refreshToken,
            HttpServletResponse response) {

        if (refreshToken == null || !tokenService.validateToken(refreshToken)) {
            return ResponseEntity.badRequest().build();
        }

        Authentication authentication = tokenService.getAuthenticationFromToken(refreshToken);
        String newAccessToken = tokenService.createAndSetAccessTokenForRefresh(authentication, response);

        return ResponseEntity.ok(new JwtTokenResponse(newAccessToken, refreshToken));
    }
}
