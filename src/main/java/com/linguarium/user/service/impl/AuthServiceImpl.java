package com.linguarium.user.service.impl;

import static com.linguarium.util.GeneralUtils.generateId;
import static lombok.AccessLevel.PRIVATE;

import com.linguarium.auth.dto.SocialProvider;
import com.linguarium.auth.dto.request.LoginRequest;
import com.linguarium.auth.dto.request.RegistrationRequest;
import com.linguarium.auth.dto.response.JwtAuthenticationResponse;
import com.linguarium.auth.exception.OAuth2AuthenticationProcessingException;
import com.linguarium.auth.exception.UserAlreadyExistsAuthenticationException;
import com.linguarium.auth.model.LocalUser;
import com.linguarium.config.security.jwt.TokenProvider;
import com.linguarium.config.security.oauth2.userinfo.OAuth2UserInfo;
import com.linguarium.config.security.oauth2.userinfo.OAuth2UserInfoFactory;
import com.linguarium.translator.model.Language;
import com.linguarium.user.mapper.UserMapper;
import com.linguarium.user.model.Learner;
import com.linguarium.user.model.Profile;
import com.linguarium.user.model.User;
import com.linguarium.user.repository.UserRepository;
import com.linguarium.user.service.ProfileService;
import com.linguarium.user.service.UserService;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
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
    private static final String OAUTH2_PLACEHOLDER = "OAUTH2_PLACEHOLDER";
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
    public JwtAuthenticationResponse login(LoginRequest loginRequest) {
        String password = loginRequest.password();

        Authentication authentication =
                manager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        LocalUser localUser = (LocalUser) authentication.getPrincipal();
        profileService.updateLoginStreak(localUser.getUser().getProfile());
        String jwt = tokenProvider.createToken(authentication);
        return new JwtAuthenticationResponse(jwt, userService.buildUserInfoFromUser(localUser.getUser()));
    }

    @Override
    @Transactional
    public User register(RegistrationRequest request) {
        validateRegistrationRequest(request);
        SocialProvider provider = request.getSocialProvider();
        String password = getEncodedPasswordOrPlaceholder(provider, request.getPassword());

        LocalDateTime now = LocalDateTime.now();
        User user = userMapper.registrationRequestToUser(request);
        user.setPassword(password);

        Learner learner = Learner.builder()
                .user(user)
                .targetLangs(Set.of(Language.EN.name())) // TODO temporary
                .build();

        Profile profile = Profile.builder()
                .user(user)
                .lastLogin(now)
                .profilePicLink(request.getProfilePicLink()) // TODO null?
                .build();

        user.setRegistered(now);
        user.setUsername(generateId()); // TODO set real username
        user.setLearner(learner);
        user.setProfile(profile);

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public LocalUser processProviderAuth(
            String provider, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo) {
        OAuth2UserInfo oAuth2UserInfo = userInfoFactory.getOAuth2UserInfo(provider, attributes);
        validateOAuth2UserInfo(oAuth2UserInfo);

        User user = userService.findUserByEmail(oAuth2UserInfo.getEmail());

        if (user == null) {
            RegistrationRequest request = createRegistrationRequestFromProviderInfo(provider, oAuth2UserInfo);
            user = register(request);
        } else {
            validateUserProviderMatch(user, provider);
            user = updateExistingUserWithProviderInfo(user, oAuth2UserInfo);
        }

        return new LocalUser(user, attributes, idToken, userInfo);
    }

    private void validateUserProviderMatch(User user, String registrationId) {
        if (!user.getProvider().equals(registrationId)) {
            throw new OAuth2AuthenticationProcessingException(
                    "Looks like you're signed up with " + user.getProvider() + " account. Please use it to login.");
        }
    }

    private String getEncodedPasswordOrPlaceholder(SocialProvider provider, String password) {
        if (!SocialProvider.LOCAL.equals(provider)) {
            return OAUTH2_PLACEHOLDER;
        }
        return passwordEncoder.encode(password);
    }

    private RegistrationRequest createRegistrationRequestFromProviderInfo(
            String registrationId, OAuth2UserInfo oAuth2UserInfo) {
        return RegistrationRequest.builder()
                .providerUserId(oAuth2UserInfo.getId())
                .email(oAuth2UserInfo.getEmail())
                .profilePicLink(oAuth2UserInfo.getImageUrl())
                .socialProvider(SocialProvider.toSocialProvider(registrationId))
                .password(OAUTH2_PLACEHOLDER)
                .build();
    }

    private User updateExistingUserWithProviderInfo(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.getProfile().setProfilePicLink(oAuth2UserInfo.getImageUrl());
        return userRepository.save(existingUser);
    }

    private void validateOAuth2UserInfo(OAuth2UserInfo oAuth2UserInfo) {
        if (!StringUtils.hasLength(oAuth2UserInfo.getName())) {
            throw new OAuth2AuthenticationProcessingException("Name not found from OAuth2 provider");
        }

        if (!StringUtils.hasLength(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }
    }

    private void validateRegistrationRequest(RegistrationRequest registrationRequest) {
        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            throw new UserAlreadyExistsAuthenticationException(
                    "User with email id " + registrationRequest.getEmail() + " already exists");
        }
        if (userRepository.existsByUsername(registrationRequest.getUsername())) {
            throw new UserAlreadyExistsAuthenticationException(
                    "User with username " + registrationRequest.getUsername() + " already exists");
        }
    }
}
