package com.almonium.auth.common.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.auth.common.exception.BadAuthActionRequest;
import com.almonium.auth.common.model.entity.Principal;
import com.almonium.auth.common.service.EmailVerificationService;
import com.almonium.auth.local.dto.response.VerificationTokenDto;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/verification/email")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EmailVerificationController {
    EmailVerificationService emailVerificationService;

    @GetMapping("/last-token")
    public ResponseEntity<VerificationTokenDto> getLastEmailVerificationToken(@Auth Principal auth) {
        return emailVerificationService
                .getLastEmailVerificationToken(auth.getUser().getId())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    /** Can only be used if app.auth.email-verification-required is set to false
     *  (and more than an hour passed since the last email verification request)
     * then we allow users to request verification email from the settings
     * If the email is already verified, throw an exception
     *
     * @param auth - authenticated user
     * @return - 200 OK if the email was sent successfully
     */
    @PostMapping("/request")
    public ResponseEntity<?> requestEmailVerification(@Auth Principal auth) {
        if (auth.getUser().isEmailVerified()) {
            throw new BadAuthActionRequest("Email is already verified");
        }

        emailVerificationService.sendEmailVerification(auth.getUser().getId());
        return ResponseEntity.ok().build();
    }

    // either email_change or email_verification
    @PostMapping("/resend")
    public ResponseEntity<?> resendEmailVerificationRequest(@Auth Principal auth) {
        emailVerificationService.resendEmailVerificationRequest(auth.getUser().getId());
        return ResponseEntity.ok().build();
    }

    // either email_change or email_verification
    @DeleteMapping
    public ResponseEntity<?> deleteEmailVerificationRequest(@Auth Principal auth) {
        emailVerificationService.cancelEmailChangeRequest(auth.getUser().getId());
        return ResponseEntity.ok().build();
    }
}
