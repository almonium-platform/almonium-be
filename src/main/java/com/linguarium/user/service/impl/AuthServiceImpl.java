package com.linguarium.user.service.impl;

import static com.linguarium.util.GeneralUtils.generateId;
import static lombok.AccessLevel.PRIVATE;

import com.linguarium.auth.dto.AuthProvider;
import com.linguarium.auth.dto.request.LocalRegisterRequest;
import com.linguarium.auth.dto.request.LoginRequest;
import com.linguarium.auth.dto.request.ProviderRegisterRequest;
import com.linguarium.auth.dto.request.RegisterRequest;
import com.linguarium.auth.dto.response.JwtAuthResponse;
import com.linguarium.auth.exception.OAuth2AuthenticationProcessingException;
import com.linguarium.auth.exception.UserAlreadyExistsAuthenticationException;
import com.linguarium.auth.model.LocalUser;
import com.linguarium.config.security.jwt.TokenProvider;
import com.linguarium.config.security.oauth2.userinfo.OAuth2UserInfo;
import com.linguarium.config.security.oauth2.userinfo.OAuth2UserInfoFactory;
import com.linguarium.user.mapper.UserMapper;
import com.linguarium.user.model.User;
import com.linguarium.user.repository.UserRepository;
import com.linguarium.user.service.AuthService;
import com.linguarium.user.service.ProfileService;
import com.linguarium.user.service.UserService;
import java.util.Map;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {
    UserService userService;
    UserRepository userRepository;
    ProfileService profileService;
    TokenProvider tokenProvider;
    OAuth2UserInfoFactory userInfoFactory;
    PasswordEncoder passwordEncoder;
    AuthenticationManager manager;
    UserMapper userMapper;

    public AuthServiceImpl(
            UserService userService,
            ProfileService profileService,
            TokenProvider tokenProvider,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            OAuth2UserInfoFactory userInfoFactory,
            UserMapper userMapper,
            @Lazy AuthenticationManager manager) {
        this.userService = userService;
        this.profileService = profileService;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userInfoFactory = userInfoFactory;
        this.manager = manager;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public JwtAuthResponse login(LoginRequest request) {
        Authentication authentication =
                manager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        LocalUser localUser = (LocalUser) authentication.getPrincipal();
        profileService.updateLoginStreak(localUser.getUser().getProfile());
        String jwt = tokenProvider.createToken(authentication);
        return new JwtAuthResponse(jwt, userService.buildUserInfoFromUser(localUser.getUser()));
    }

    @Override
    @Transactional
    public void register(LocalRegisterRequest request) {
        validateRegisterRequest(request);
        User user = userMapper.localRegisterRequestToUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public LocalUser processProviderAuth(
            String provider, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo) {
        OAuth2UserInfo oAuth2UserInfo = userInfoFactory.getOAuth2UserInfo(AuthProvider.valueOf(provider), attributes);
        validateProviderUserInfo(oAuth2UserInfo);

        User user = userService.findUserByEmail(oAuth2UserInfo.getEmail());

        if (user == null) {
            ProviderRegisterRequest request = toProviderRegisterRequest(provider, oAuth2UserInfo);
            user = userMapper.providerRegisterRequestToUser(request);
            userRepository.save(user);
        } else {
            validateUserProviderMatch(user, provider);
        }
        user = updateUserWithProviderInfo(user, oAuth2UserInfo);
        return new LocalUser(user, attributes, idToken, userInfo);
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

    private ProviderRegisterRequest toProviderRegisterRequest(String registrationId, OAuth2UserInfo oAuth2UserInfo) {
        return ProviderRegisterRequest.builder()
                .providerUserId(oAuth2UserInfo.getId())
                .email(oAuth2UserInfo.getEmail())
                .username(getUsername())
                .provider(AuthProvider.valueOf(registrationId))
                .build();
    }

    private String getUsername() {
        return generateId(); // TODO set real username
    }

    private void validateUserProviderMatch(User user, String registrationId) {
        if (!user.getProvider().name().equals(registrationId)) {
            throw new OAuth2AuthenticationProcessingException(
                    "Looks like you're signed up with " + user.getProvider() + " account. Please use it to login.");
        }
    }

    private User updateUserWithProviderInfo(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.getProfile().setAvatarUrl(oAuth2UserInfo.getImageUrl()); // todo save, but not update
        return userRepository.save(existingUser);
    }

    private void validateProviderUserInfo(OAuth2UserInfo oAuth2UserInfo) {
        if (!StringUtils.hasLength(oAuth2UserInfo.getName())) {
            throw new OAuth2AuthenticationProcessingException("Name not found from OAuth2 provider");
        }

        if (!StringUtils.hasLength(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }
    }
}
