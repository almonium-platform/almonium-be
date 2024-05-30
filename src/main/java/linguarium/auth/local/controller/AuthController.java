package linguarium.auth.local.controller;

import static lombok.AccessLevel.PRIVATE;

import jakarta.validation.Valid;
import linguarium.auth.local.dto.request.LocalAuthRequest;
import linguarium.auth.local.dto.response.JwtAuthResponse;
import linguarium.auth.local.service.AuthService;
import linguarium.auth.oauth2.model.entity.Principal;
import linguarium.util.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class AuthController {
    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@Valid @RequestBody LocalAuthRequest localAuthRequest) {
        return ResponseEntity.ok(authService.login(localAuthRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<JwtAuthResponse> register(@Valid @RequestBody LocalAuthRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PutMapping("/local")
    public ResponseEntity<?> addLocalLogin(
            @CurrentUser Principal auth, @Valid @RequestBody LocalAuthRequest localAuthRequest) {
        authService.linkLocalAuth(auth.getUser().getId(), localAuthRequest);
        return ResponseEntity.ok().build();
    }
}
