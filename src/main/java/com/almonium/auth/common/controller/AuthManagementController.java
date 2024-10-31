package com.almonium.auth.common.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.auth.common.dto.request.EmailRequestDto;
import com.almonium.auth.common.dto.response.UnlinkProviderResponse;
import com.almonium.auth.common.model.entity.Principal;
import com.almonium.auth.common.model.enums.AuthProviderType;
import com.almonium.auth.common.service.AuthMethodManagementService;
import com.almonium.auth.local.dto.request.LocalAuthRequest;
import com.almonium.auth.local.dto.request.PasswordResetRequest;
import com.almonium.auth.token.service.impl.AuthTokenService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class AuthManagementController {
    AuthMethodManagementService authMethodManagementService;
    AuthTokenService authTokenService;

    @PostMapping("/email-verification/request")
    public ResponseEntity<?> requestEmailVerification(@Auth Principal auth) {
        authMethodManagementService.sendEmailVerification(auth.getUser().getId());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@Auth Principal auth, @Valid @RequestBody PasswordResetRequest request) {
        authMethodManagementService.changePassword(auth.getUser().getId(), request.password());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/email-availability")
    public ResponseEntity<Boolean> checkEmailAvailability(@RequestBody EmailRequestDto request) {
        boolean isAvailable = authMethodManagementService.isEmailAvailable(request.email());
        return ResponseEntity.ok(isAvailable);
    }

    // if user has local account and wants to change email
    @PostMapping("/email-changes/request")
    public ResponseEntity<?> requestEmailChange(@Auth Principal auth, @RequestBody EmailRequestDto request) {
        authMethodManagementService.requestEmailChange(auth.getUser().getId(), request.email());
        return ResponseEntity.ok().build();
    }

    // if user doesn't have local account and wants to change email
    @PostMapping("/email-changes/link-local")
    public ResponseEntity<?> linkLocalWithNewEmail(@Auth Principal auth, @Valid @RequestBody LocalAuthRequest request) {
        authMethodManagementService.linkLocalWithNewEmail(auth.getUser().getId(), request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/providers")
    public ResponseEntity<List<AuthProviderType>> getAuthProviders(@Auth Principal auth) {
        return ResponseEntity.ok(
                authMethodManagementService.getAuthProviders(auth.getUser().getId()));
    }

    @GetMapping("/email-verified")
    public ResponseEntity<?> isEmailVerified(@Auth Principal auth) {
        boolean verified =
                authMethodManagementService.isEmailVerified(auth.getUser().getId());
        return ResponseEntity.ok(verified);
    }

    @PostMapping("/local")
    public ResponseEntity<?> addLocalLogin(
            @Auth Principal auth,
            @Valid @RequestBody LocalAuthRequest localAuthRequest) { // change to accept only password
        authMethodManagementService.linkLocal(auth.getUser().getId(), localAuthRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/providers/{provider}")
    public ResponseEntity<UnlinkProviderResponse> unlinkProvider(
            @Auth Principal auth, @PathVariable AuthProviderType provider) {
        authMethodManagementService.unlinkAuthMethod(auth.getUser().getId(), provider);

        boolean isCurrentPrincipalBeingUnlinked = provider == auth.getProvider();
        return ResponseEntity.ok(new UnlinkProviderResponse(isCurrentPrincipalBeingUnlinked));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Auth Principal auth, HttpServletResponse response) {
        authTokenService.revokeRefreshTokensByUser(auth.getUser());
        authTokenService.clearTokenCookies(response);
        return ResponseEntity.ok().build();
    }
}
