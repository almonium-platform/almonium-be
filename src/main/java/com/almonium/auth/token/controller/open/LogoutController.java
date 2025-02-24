package com.almonium.auth.token.controller.open;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.token.service.AuthTokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth")
@RestController
@RequestMapping("/public/auth")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class LogoutController {
    AuthTokenService authTokenService;

    @PostMapping("/logout")
    public ResponseEntity<Void> logoutPublic(HttpServletResponse response) {
        authTokenService.clearTokenCookies(response);
        return ResponseEntity.ok().build();
    }
}
