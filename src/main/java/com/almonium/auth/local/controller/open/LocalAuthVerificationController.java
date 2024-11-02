package com.almonium.auth.local.controller.open;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.local.dto.request.PasswordResetConfirmRequest;
import com.almonium.auth.local.service.LocalAuthVerificationService;
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
@RequestMapping("/public/auth/verification")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class LocalAuthVerificationController {
    LocalAuthVerificationService localAuthVerificationService;

    // Verify email using a token
    @PostMapping("/emails")
    public ResponseEntity<ApiResponse> verifyEmail(@NotBlank @RequestParam String token) {
        localAuthVerificationService.verifyEmail(token);
        return ResponseEntity.ok(new ApiResponse(true, "Email verified successfully"));
    }

    // Confirm email change using a token
    @PostMapping("/emails/change")
    public ResponseEntity<?> confirmEmailChange(@NotBlank @RequestParam String token) {
        localAuthVerificationService.changeEmail(token);
        return ResponseEntity.ok().build();
    }

    // Reset password using a token
    @PostMapping("/passwords")
    public ResponseEntity<ApiResponse> resetPassword(
            @Valid @RequestBody PasswordResetConfirmRequest passwordResetConfirmRequest) {
        localAuthVerificationService.resetPassword(
                passwordResetConfirmRequest.token(), passwordResetConfirmRequest.newPassword());
        return ResponseEntity.ok(new ApiResponse(true, "Password reset successfully"));
    }
}
