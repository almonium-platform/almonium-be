package com.almonium.auth.common.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.service.AuthMethodManagementService;
import com.almonium.auth.local.dto.request.PasswordResetConfirmRequest;
import com.almonium.util.dto.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for authentication actions supported by verification tokens.
 */
@RestController
@RequestMapping("/auth/verification")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class VerificationTokenController {
    AuthMethodManagementService authMethodManagementService;

    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse> verifyEmail(@RequestParam String token) {
        authMethodManagementService.verifyEmail(token);
        return ResponseEntity.ok(new ApiResponse(true, "Email verified successfully"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(
            @Valid @RequestBody PasswordResetConfirmRequest passwordResetConfirmRequest) {
        authMethodManagementService.resetPassword(
                passwordResetConfirmRequest.token(), passwordResetConfirmRequest.newPassword());
        return ResponseEntity.ok(new ApiResponse(true, "Password reset successfully"));
    }

    @PostMapping("/email-changes/confirm")
    public ResponseEntity<?> confirmEmailChange(@NotBlank @RequestParam String token) {
        authMethodManagementService.changeEmail(token);
        return ResponseEntity.ok().build();
    }
}
