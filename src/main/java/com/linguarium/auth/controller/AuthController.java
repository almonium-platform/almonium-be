package com.linguarium.auth.controller;

import static lombok.AccessLevel.PRIVATE;

import com.linguarium.auth.dto.request.LoginRequest;
import com.linguarium.auth.dto.request.RegistrationRequest;
import com.linguarium.auth.dto.response.ApiResponse;
import com.linguarium.auth.dto.response.JwtAuthenticationResponse;
import com.linguarium.user.service.impl.AuthService;
import jakarta.validation.Valid;
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
    public ResponseEntity<JwtAuthenticationResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegistrationRequest registrationRequest) {
        authService.register(registrationRequest);
        return ResponseEntity.ok().body(new ApiResponse(true, "User registered successfully"));
    }
}
