package com.almonium.auth.common.controller.sensitive;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.auth.common.annotation.RequireRecentLogin;
import com.almonium.auth.common.dto.response.UnlinkProviderResponse;
import com.almonium.auth.common.model.entity.Principal;
import com.almonium.auth.common.model.enums.AuthProviderType;
import com.almonium.auth.common.service.SensitiveAuthActionsService;
import com.almonium.auth.local.dto.request.LocalAuthRequest;
import com.almonium.auth.local.dto.request.PasswordRequestDto;
import jakarta.validation.Valid;
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

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequireRecentLogin
public class SensitiveAuthActionsController {
    SensitiveAuthActionsService sensitiveAuthActionsService;

    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@Auth Principal auth, @Valid @RequestBody PasswordRequestDto request) {
        sensitiveAuthActionsService.changePassword(auth.getUser().getId(), request.password());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/local/migrate")
    public ResponseEntity<?> linkLocalWithNewEmail(@Auth Principal auth, @Valid @RequestBody LocalAuthRequest request) {
        sensitiveAuthActionsService.linkLocalWithNewEmail(auth.getUser().getId(), request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/local/link")
    public ResponseEntity<?> addLocalLogin(
            @Auth Principal auth, @Valid @RequestBody PasswordRequestDto passwordRequestDto) {
        sensitiveAuthActionsService.linkLocal(auth.getUser().getId(), passwordRequestDto.password());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/providers/{provider}")
    public ResponseEntity<UnlinkProviderResponse> unlinkProvider(
            @Auth Principal auth, @PathVariable AuthProviderType provider) {
        sensitiveAuthActionsService.unlinkAuthMethod(auth.getUser().getId(), provider);
        boolean isCurrentPrincipalBeingUnlinked = provider == auth.getProvider();
        return ResponseEntity.ok(new UnlinkProviderResponse(isCurrentPrincipalBeingUnlinked));
    }
}
