package com.almonium.auth.local.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.local.dto.request.LocalAuthRequest;
import com.almonium.auth.local.dto.request.PasswordResetConfirmRequest;
import com.almonium.auth.local.dto.request.PasswordResetRequest;
import com.almonium.auth.local.dto.response.JwtAuthResponse;
import com.almonium.auth.local.service.LocalAuthService;
import com.almonium.util.dto.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/auth")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class LocalAuthController {
    LocalAuthService localAuthService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(
            @Valid @RequestBody LocalAuthRequest localAuthRequest, HttpServletResponse response) {
        return ResponseEntity.ok(localAuthService.login(localAuthRequest, response));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody LocalAuthRequest request) {
        localAuthService.register(request);
        return ResponseEntity.ok(new ApiResponse(true, "Successfully registered. Please verify your email address"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> requestPasswordReset(
            @Valid @RequestBody PasswordResetRequest passwordResetRequest) {
        localAuthService.requestPasswordReset(passwordResetRequest.email());
        return ResponseEntity.ok(new ApiResponse(true, "Password reset email sent successfully"));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse> verifyEmail(@RequestParam("token") String token) {
        localAuthService.verifyEmail(token);
        return ResponseEntity.ok(new ApiResponse(true, "Email verified successfully"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(
            @Valid @RequestBody PasswordResetConfirmRequest passwordResetConfirmRequest) {
        localAuthService.resetPassword(passwordResetConfirmRequest.token(), passwordResetConfirmRequest.newPassword());
        return ResponseEntity.ok(new ApiResponse(true, "Password reset successfully"));
    }
}
