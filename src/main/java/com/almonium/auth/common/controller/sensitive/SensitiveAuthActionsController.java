package com.almonium.auth.common.controller.sensitive;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.auth.common.annotation.RequireRecentLogin;
import com.almonium.auth.common.dto.request.EmailRequestDto;
import com.almonium.auth.common.dto.response.UnlinkProviderResponse;
import com.almonium.auth.common.model.entity.Principal;
import com.almonium.auth.common.model.enums.AuthProviderType;
import com.almonium.auth.common.service.SensitiveAuthActionsService;
import com.almonium.auth.local.dto.request.LocalAuthRequest;
import com.almonium.auth.local.dto.request.PasswordRequestDto;
import com.almonium.auth.token.service.AuthTokenService;
import com.almonium.user.core.model.entity.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequireRecentLogin
public class SensitiveAuthActionsController {
    SensitiveAuthActionsService sensitiveAuthActionsService;
    AuthTokenService authTokenService;

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(@Auth UUID id, @Valid @RequestBody PasswordRequestDto request) {
        sensitiveAuthActionsService.changePassword(id, request.password());
        return ResponseEntity.ok().build();
    }

    // used when the user doesn't have a local auth method
    @PostMapping("/local/migrate")
    public ResponseEntity<Void> linkLocalWithNewEmail(@Auth UUID id, @Valid @RequestBody LocalAuthRequest request) {
        sensitiveAuthActionsService.linkLocalWithNewEmail(id, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/email/change")
    public ResponseEntity<Void> requestEmailChange(@Auth UUID id, @Valid @RequestBody EmailRequestDto request) {
        sensitiveAuthActionsService.requestEmailChange(id, request.email());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/local/link")
    public ResponseEntity<Void> addLocalLogin(
            @Auth UUID id, @Valid @RequestBody PasswordRequestDto passwordRequestDto) {
        sensitiveAuthActionsService.linkLocal(id, passwordRequestDto.password());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/providers/{provider}")
    public ResponseEntity<UnlinkProviderResponse> unlinkProvider(
            @Auth Principal principal, @PathVariable AuthProviderType provider) {
        sensitiveAuthActionsService.unlinkAuthMethod(principal.getUser().getId(), provider);
        boolean isCurrentPrincipalBeingUnlinked = provider == principal.getProvider();
        return ResponseEntity.ok(new UnlinkProviderResponse(isCurrentPrincipalBeingUnlinked));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteCurrentUserAccount(@Auth User user, HttpServletResponse response) {
        sensitiveAuthActionsService.deleteAccount(user);
        authTokenService.clearTokenCookies(response);
        return ResponseEntity.noContent().build();
    }
}
