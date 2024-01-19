package com.linguarium.auth.controller;

import com.linguarium.auth.dto.request.LoginRequest;
import com.linguarium.auth.dto.request.SignUpRequest;
import com.linguarium.auth.dto.response.ApiResponse;
import com.linguarium.auth.dto.response.JwtAuthenticationResponse;
import com.linguarium.auth.exception.UserAlreadyExistsAuthenticationException;
import com.linguarium.auth.model.LocalUser;
import com.linguarium.configuration.security.jwt.TokenProvider;
import com.linguarium.user.service.ProfileService;
import com.linguarium.user.service.UserService;
import jakarta.validation.Valid;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class AuthController {
    AuthenticationManager authenticationManager;
    UserService userService;
    ProfileService profileService;
    TokenProvider tokenProvider;
    Environment environment;

    public AuthController(AuthenticationManager authenticationManager, UserService userService, ProfileService profileService, TokenProvider tokenProvider, Environment environment) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.profileService = profileService;
        this.tokenProvider = tokenProvider;
        this.environment = environment;
    }

    @GetMapping("/profile")
    public ResponseEntity<List<String>> getCurrentActiveProfiles() {
        return ResponseEntity.ok(Arrays.asList(environment.getActiveProfiles()));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication);
        LocalUser localUser = (LocalUser) authentication.getPrincipal();
        profileService.updateLoginStreak(localUser.getUser().getProfile());
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, userService.buildUserInfo(localUser.getUser())));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        try {
            userService.registerNewUser(signUpRequest);
        } catch (UserAlreadyExistsAuthenticationException e) {
            log.error("User already exists: {}", e.getMessage());
            return new ResponseEntity<>(new ApiResponse(false, "Email or username already in use!"), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(new ApiResponse(true, "User registered successfully"));
    }
}
