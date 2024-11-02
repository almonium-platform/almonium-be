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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/verification/email/")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EmailVerificationController {
    EmailVerificationService emailVerificationService;

    @GetMapping("/me")
    public ResponseEntity<?> isEmailVerified(@Auth Principal auth) {
        boolean verified =
                emailVerificationService.isEmailVerified(auth.getUser().getId());
        return ResponseEntity.ok(verified);
    }

    @GetMapping("/last-token")
    public ResponseEntity<VerificationTokenDto> getLastEmailVerificationToken(@Auth Principal auth) {
        return emailVerificationService
                .getLastEmailVerificationToken(auth.getUser().getId())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/request")
    public ResponseEntity<?> requestEmailVerification(@Auth Principal auth) {
        boolean verified =
                emailVerificationService.isEmailVerified(auth.getUser().getId());
        if (verified) {
            throw new BadAuthActionRequest("Email is already verified");
        }

        emailVerificationService.sendEmailVerification(auth.getUser().getId());
        return ResponseEntity.ok().build();
    }
}
