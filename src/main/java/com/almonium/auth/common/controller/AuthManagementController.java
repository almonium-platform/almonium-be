package com.almonium.auth.common.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.auth.common.dto.request.EmailRequestDto;
import com.almonium.auth.common.dto.response.PrincipalDto;
import com.almonium.auth.common.service.AuthMethodManagementService;
import com.almonium.auth.token.service.AuthTokenService;
import com.almonium.user.core.model.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("/providers")
    public ResponseEntity<List<PrincipalDto>> getAuthProviders(@Auth User user) {
        return ResponseEntity.ok(authMethodManagementService.getAuthProviders(user.getEmail()));
    }

    @PostMapping("/email/availability")
    public ResponseEntity<Boolean> checkEmailAvailability(@RequestBody EmailRequestDto request) {
        boolean isAvailable = authMethodManagementService.isEmailAvailable(request.email());
        return ResponseEntity.ok(isAvailable);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Auth User user, HttpServletResponse response) {
        authTokenService.revokeRefreshTokensByUser(user);
        authTokenService.clearTokenCookies(response);
        return ResponseEntity.ok().build();
    }
}
