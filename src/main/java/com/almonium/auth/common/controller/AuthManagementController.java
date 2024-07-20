package com.almonium.auth.common.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.auth.common.model.entity.Principal;
import com.almonium.auth.common.model.enums.AuthProviderType;
import com.almonium.auth.common.service.AuthManagementService;
import com.almonium.auth.local.dto.request.LocalAuthRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/manage")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class AuthManagementController {
    AuthManagementService authManagementService;

    @PutMapping("/local")
    public ResponseEntity<?> addLocalLogin(
            @Auth Principal auth, @Valid @RequestBody LocalAuthRequest localAuthRequest) {
        authManagementService.linkLocalAuth(auth.getUser().getId(), localAuthRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/provider/{provider}")
    public ResponseEntity<?> unlinkProvider(@Auth Principal auth, @PathVariable AuthProviderType provider) {
        Long userId = auth.getUser().getId();
        authManagementService.unlinkAuthMethod(userId, provider);
        return ResponseEntity.ok().build();
    }
}
