package linguarium.auth.local.controller;

import static lombok.AccessLevel.PRIVATE;

import jakarta.validation.Valid;
import linguarium.auth.local.dto.request.LocalAuthRequest;
import linguarium.auth.local.dto.request.PasswordResetConfirmRequest;
import linguarium.auth.local.dto.request.PasswordResetRequest;
import linguarium.auth.local.dto.response.JwtAuthResponse;
import linguarium.auth.local.service.LocalAuthService;
import linguarium.util.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth/public")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class LocalAuthController {
    LocalAuthService localAuthService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@Valid @RequestBody LocalAuthRequest localAuthRequest) {
        return ResponseEntity.ok(localAuthService.login(localAuthRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody LocalAuthRequest request) {
        localAuthService.register(request);
        return ResponseEntity.ok(
                new ApiResponse(true, "User registration attempt recorded. Needs verification to complete"));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse> verifyEmail(@RequestParam("token") String token) {
        localAuthService.verifyEmail(token);
        return ResponseEntity.ok(new ApiResponse(true, "Email verified successfully"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> requestPasswordReset(
            @Valid @RequestBody PasswordResetRequest passwordResetRequest) {
        localAuthService.requestPasswordReset(passwordResetRequest.email());
        return ResponseEntity.ok(new ApiResponse(true, "Password reset email sent successfully"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(
            @Valid @RequestBody PasswordResetConfirmRequest passwordResetConfirmRequest) {
        localAuthService.resetPassword(passwordResetConfirmRequest.token(), passwordResetConfirmRequest.newPassword());
        return ResponseEntity.ok(new ApiResponse(true, "Password reset successfully"));
    }
}
