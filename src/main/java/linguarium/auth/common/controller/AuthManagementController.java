package linguarium.auth.common.controller;

import static lombok.AccessLevel.PRIVATE;

import jakarta.validation.Valid;
import linguarium.auth.common.enums.AuthProviderType;
import linguarium.auth.common.model.entity.Principal;
import linguarium.auth.common.service.AuthManagementService;
import linguarium.auth.local.dto.request.LocalAuthRequest;
import linguarium.util.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth/manage")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class AuthManagementController {
    AuthManagementService authManagementService;

    @PutMapping("/local")
    public ResponseEntity<?> addLocalLogin(
            @CurrentUser Principal auth, @Valid @RequestBody LocalAuthRequest localAuthRequest) {
        authManagementService.linkLocalAuth(auth.getUser().getId(), localAuthRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/provider/{provider}")
    public ResponseEntity<?> unlinkProvider(@CurrentUser Principal auth, @PathVariable AuthProviderType provider) {
        Long userId = auth.getUser().getId();
        authManagementService.unlinkProviderAuth(userId, provider);
        return ResponseEntity.ok().build();
    }
}
