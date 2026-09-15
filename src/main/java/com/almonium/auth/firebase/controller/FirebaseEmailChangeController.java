package com.almonium.auth.firebase.controller;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.auth.common.annotation.RequireRecentLogin;
import com.almonium.auth.firebase.dto.EmailAddressRequest;
import com.almonium.auth.firebase.security.FirebasePrincipal;
import com.almonium.auth.firebase.service.PendingEmailChangeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/email-changes")
@RequiredArgsConstructor
public class FirebaseEmailChangeController {
    private final PendingEmailChangeService pendingEmailChangeService;

    @RequireRecentLogin
    @PostMapping
    public ResponseEntity<Void> request(
            @Auth FirebasePrincipal principal, @Valid @RequestBody EmailAddressRequest request) {
        pendingEmailChangeService.request(principal.userId(), request.email());
        return ResponseEntity.noContent().build();
    }
}
