package com.almonium.auth.firebase.controller;

import com.almonium.auth.firebase.dto.EmailAddressRequest;
import com.almonium.auth.firebase.dto.FirebaseSessionRequest;
import com.almonium.auth.firebase.service.FirebaseAuthEmailService;
import com.almonium.auth.firebase.service.PendingEmailChangeService;
import com.almonium.util.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/auth")
@RequiredArgsConstructor
public class PublicFirebaseAuthEmailController {
    private final FirebaseAuthEmailService firebaseAuthEmailService;
    private final PendingEmailChangeService pendingEmailChangeService;

    @PostMapping("/email-verification")
    public ResponseEntity<Void> sendEmailVerification(@Valid @RequestBody FirebaseSessionRequest request) {
        firebaseAuthEmailService.sendEmailVerification(request.idToken());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/password-resets")
    public ResponseEntity<ApiResponse> sendPasswordReset(@Valid @RequestBody EmailAddressRequest request) {
        firebaseAuthEmailService.sendPasswordReset(request.email());
        return ResponseEntity.accepted()
                .body(new ApiResponse(true, "If an account exists, a password reset link has been sent."));
    }

    @PostMapping("/email-changes")
    public ResponseEntity<Void> confirmEmailChange(@RequestParam String token) {
        pendingEmailChangeService.confirm(token);
        return ResponseEntity.noContent().build();
    }
}
