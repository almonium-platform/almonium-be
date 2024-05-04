package com.linguarium.user.service.impl;

import static com.linguarium.util.GeneralUtils.generateId;
import static lombok.AccessLevel.PRIVATE;

import com.linguarium.auth.dto.SocialProvider;
import com.linguarium.auth.dto.UserInfo;
import com.linguarium.auth.dto.request.LoginRequest;
import com.linguarium.auth.dto.request.RegistrationRequest;
import com.linguarium.auth.dto.response.JwtAuthenticationResponse;
import com.linguarium.auth.exception.OAuth2AuthenticationProcessingException;
import com.linguarium.auth.exception.UserAlreadyExistsAuthenticationException;
import com.linguarium.auth.model.LocalUser;
import com.linguarium.card.repository.CardTagRepository;
import com.linguarium.card.repository.TagRepository;
import com.linguarium.config.security.jwt.TokenProvider;
import com.linguarium.config.security.oauth2.userinfo.OAuth2UserInfo;
import com.linguarium.config.security.oauth2.userinfo.OAuth2UserInfoFactory;
import com.linguarium.translator.model.Language;
import com.linguarium.user.model.Learner;
import com.linguarium.user.model.Profile;
import com.linguarium.user.model.User;
import com.linguarium.user.repository.UserRepository;
import com.linguarium.user.service.ProfileService;
import com.linguarium.user.service.UserService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
public class UserServiceImpl implements UserService { // TODO move to AuthService
    private static final String OAUTH2_PLACEHOLDER = "OAUTH2_PLACEHOLDER";
    ProfileService profileService;
    TokenProvider tokenProvider;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    CardTagRepository cardTagRepository;
    TagRepository tagRepository;
    OAuth2UserInfoFactory userInfoFactory;
    AuthenticationManager manager;

    public UserServiceImpl(
            ProfileService profileService,
            TokenProvider tokenProvider,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            CardTagRepository cardTagRepository,
            TagRepository tagRepository,
            OAuth2UserInfoFactory userInfoFactory,
            @Lazy AuthenticationManager manager) {
        this.profileService = profileService;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cardTagRepository = cardTagRepository;
        this.tagRepository = tagRepository;
        this.userInfoFactory = userInfoFactory;
        this.manager = manager;
    }

    @Override
    public User findUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public void deleteAccount(User user) {
        userRepository.delete(user);
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public void changeUsername(String username, Long id) {
        userRepository.changeUsername(username, id);
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }

    @Override
    @Transactional
    public JwtAuthenticationResponse login(LoginRequest loginRequest) {
        String password = loginRequest.password();
        validatePasswordNotStubbed(password);

        Authentication authentication =
                manager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        LocalUser localUser = (LocalUser) authentication.getPrincipal();
        profileService.updateLoginStreak(localUser.getUser().getProfile());
        String jwt = tokenProvider.createToken(authentication);
        return new JwtAuthenticationResponse(jwt, buildUserInfo(localUser.getUser()));
    }

    @Override
    @Transactional
    public User register(RegistrationRequest request) {
        validateRegistrationRequest(request);

        SocialProvider provider = request.getSocialProvider();
        String password = validateAndPreparePassword(provider, request.getPassword());

        LocalDateTime now = LocalDateTime.now();
        User user = User.builder() // TODO mapstruct
                .username(request.getUsername())
                .email(request.getEmail())
                .password(password)
                .provider(provider.getProviderType())
                .providerUserId(request.getProviderUserId())
                .build();

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
    public LocalUser processAuthenticationFromProvider(
            String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo) {
        OAuth2UserInfo oAuth2UserInfo = userInfoFactory.getOAuth2UserInfo(registrationId, attributes);
        validateOAuth2UserInfo(oAuth2UserInfo);

        User user = findUserByEmail(oAuth2UserInfo.getEmail());

        if (user == null) {
            RegistrationRequest request = createRegistrationRequestFromProviderInfo(registrationId, oAuth2UserInfo);
            user = register(request);
        } else {
            validateExistingUser(user, registrationId);
            user = updateExistingUser(user, oAuth2UserInfo);
        }

        return new LocalUser(user, attributes, idToken, userInfo);
    }

    @Override
    @Transactional
    public UserInfo buildUserInfo(User user) {
        Learner learner = user.getLearner();
        Profile profile = user.getProfile();

        List<String> tags = getTags(user);

        return new UserInfo(
                user.getId().toString(),
                user.getUsername(),
                user.getEmail(),
                profile.getUiLang().name(),
                profile.getProfilePicLink(),
                profile.getBackground(),
                profile.getStreak(),
                learner.getTargetLangs(),
                learner.getFluentLangs(),
                tags);
    }

    private List<String> getTags(User user) {
        return cardTagRepository.getLearnersTags(user.getLearner()).stream()
                .map(tagId -> tagRepository.findById(tagId).orElseThrow().getText())
                .collect(Collectors.toList());
    }

    private void validateExistingUser(User user, String registrationId) {
        if (!user.getProvider().equals(registrationId)
                && !user.getProvider().equals(SocialProvider.LOCAL.getProviderType())) {
            throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with "
                    + user.getProvider()
                    + " account. Please use your "
                    + user.getProvider()
                    + " account to login.");
        }
    }

    private String validateAndPreparePassword(SocialProvider provider, String password) {
        if (SocialProvider.LOCAL.equals(provider)) {
            validatePasswordNotStubbed(password);
            return passwordEncoder.encode(password);
        }
        return OAUTH2_PLACEHOLDER;
    }

    private void validatePasswordNotStubbed(String password) {
        if (OAUTH2_PLACEHOLDER.equals(password)) {
            throw new BadCredentialsException("Invalid password");
        }
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

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
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
        if (userRepository.existsById(registrationRequest.getUserId())) { // TODO analyze this case
            throw new UserAlreadyExistsAuthenticationException(
                    "User with id " + registrationRequest.getUserId() + " already exists");
        }
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
