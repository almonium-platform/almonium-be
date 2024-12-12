package com.almonium.auth.common.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.auth.common.exception.BadAuthActionRequest;
import com.almonium.auth.common.service.EmailVerificationService;
import com.almonium.auth.local.dto.response.VerificationTokenDto;
import com.almonium.user.core.model.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/verification/email/requests")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EmailVerificationController {
    EmailVerificationService emailVerificationService;

    @GetMapping("/last/token")
    public ResponseEntity<VerificationTokenDto> getLastEmailVerificationToken(@Auth Long id) {
        return emailVerificationService
                .getLastEmailVerificationToken(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    /**
     * Can only be used if app.auth.email-verification-required is set to false
     * (and more than an hour passed since the last email verification request)
     * then we allow users to request verification email from the settings
     * If the email is already verified, throw an exception
     *
     * @param user - authenticated user
     * @return - 200 OK if the email was sent successfully
     */
    @PostMapping
    public ResponseEntity<?> requestEmailVerification(@Auth User user) {
        if (user.isEmailVerified()) {
            throw new BadAuthActionRequest("Email is already verified");
        }

        emailVerificationService.sendEmailVerification(user.getId());
        return ResponseEntity.ok().build();
    }

    // either email_change or email_verification
    @PostMapping("/resend")
    public ResponseEntity<?> resendEmailVerificationRequest(@Auth Long id) {
        emailVerificationService.resendEmailVerificationRequest(id);
        return ResponseEntity.ok().build();
    }

    // either email_change or email_verification
    @DeleteMapping
    public ResponseEntity<?> deleteEmailVerificationRequest(@Auth Long id) {
        emailVerificationService.cancelEmailChangeRequest(id);
        return ResponseEntity.ok().build();
    }
}
