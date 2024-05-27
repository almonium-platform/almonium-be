package linguarium.auth.local.controller;

import static lombok.AccessLevel.PRIVATE;

import jakarta.validation.Valid;
import linguarium.auth.local.dto.request.LoginRequest;
import linguarium.auth.local.dto.request.RegisterRequest;
import linguarium.auth.local.dto.response.JwtAuthResponse;
import linguarium.auth.local.service.AuthService;
import linguarium.util.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<JwtAuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok().body(new ApiResponse(true, "User registered successfully"));
    }
}
