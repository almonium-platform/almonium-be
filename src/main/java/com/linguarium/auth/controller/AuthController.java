package com.linguarium.auth.controller;

import static lombok.AccessLevel.PRIVATE;

import com.linguarium.auth.dto.request.LocalRegisterRequest;
import com.linguarium.auth.dto.request.LoginRequest;
import com.linguarium.auth.dto.response.ApiResponse;
import com.linguarium.auth.dto.response.JwtAuthResponse;
import com.linguarium.user.service.AuthService;
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
    public ResponseEntity<JwtAuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody LocalRegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok().body(new ApiResponse(true, "User registered successfully"));
    }
}
