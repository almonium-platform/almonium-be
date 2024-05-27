package com.linguarium.auth.local.service.impl;

import static lombok.AccessLevel.PRIVATE;

import com.linguarium.auth.local.dto.request.LoginRequest;
import com.linguarium.auth.local.dto.request.RegisterRequest;
import com.linguarium.auth.local.dto.response.JwtAuthResponse;
import com.linguarium.auth.local.exception.UserAlreadyExistsAuthenticationException;
import com.linguarium.auth.local.service.AuthService;
import com.linguarium.config.security.jwt.TokenProvider;
import com.linguarium.user.core.mapper.UserMapper;
import com.linguarium.user.core.model.User;
import com.linguarium.user.core.repository.UserRepository;
import com.linguarium.user.core.service.ProfileService;
import com.linguarium.user.core.service.UserService;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {
    UserService userService;
    UserRepository userRepository;
    ProfileService profileService;
    TokenProvider tokenProvider;
    PasswordEncoder passwordEncoder;
    AuthenticationManager manager;
    UserMapper userMapper;

    public AuthServiceImpl(
            UserService userService,
            ProfileService profileService,
            TokenProvider tokenProvider,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UserMapper userMapper,
            @Lazy AuthenticationManager manager) {
        this.userService = userService;
        this.profileService = profileService;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.manager = manager;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public JwtAuthResponse login(LoginRequest request) {
        Authentication authentication =
                manager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        profileService.updateLoginStreak(user.getProfile());
        String jwt = tokenProvider.createToken(authentication);
        return new JwtAuthResponse(jwt, userService.buildUserInfoFromUser(user));
    }

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        validateRegisterRequest(request);
        User user = userMapper.registerRequestToUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    private void validateRegisterRequest(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsAuthenticationException(
                    "User with email id " + request.getEmail() + " already exists");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsAuthenticationException(
                    "User with username " + request.getUsername() + " already exists");
        }
    }
}
