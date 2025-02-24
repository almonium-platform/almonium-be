package com.almonium.auth.local.controller.open;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.local.dto.request.PasswordResetConfirmRequest;
import com.almonium.auth.local.service.PublicLocalAuthVerificationService;
import com.almonium.util.dto.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for authentication actions supported by verification tokens.
 */
@Tag(name = "Auth")
@RestController
@RequestMapping("/public/auth/verification")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class PublicLocalAuthVerificationController {
    PublicLocalAuthVerificationService publicLocalAuthVerificationService;

    // Verify email using a token
    @PostMapping("/emails")
    public ResponseEntity<ApiResponse> verifyEmail(@NotBlank @RequestParam String token) {
        publicLocalAuthVerificationService.verifyEmail(token);
        return ResponseEntity.ok(new ApiResponse(true, "Email verified successfully"));
    }

    // Confirm email change using a token
    @PostMapping("/emails/change")
    public ResponseEntity<Void> confirmEmailChange(@NotBlank @RequestParam String token) {
        publicLocalAuthVerificationService.changeEmail(token);
        return ResponseEntity.ok().build();
    }

    // Reset password using a token
    @PostMapping("/passwords")
    public ResponseEntity<ApiResponse> resetPassword(
            @Valid @RequestBody PasswordResetConfirmRequest passwordResetConfirmRequest) {
        publicLocalAuthVerificationService.resetPassword(
                passwordResetConfirmRequest.token(), passwordResetConfirmRequest.newPassword());
        return ResponseEntity.ok(new ApiResponse(true, "Password reset successfully"));
    }

    @GetMapping("/passwords/tokens")
    public ResponseEntity<Boolean> preemptivelyCheckResetPasswordToken(@NotBlank @RequestParam String token) {
        return ResponseEntity.ok(publicLocalAuthVerificationService.validateResetPasswordToken(token));
    }
}
