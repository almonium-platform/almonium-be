package com.linguatool.service;

import com.linguatool.configuration.security.oauth2.user.OAuth2UserInfo;
import com.linguatool.configuration.security.oauth2.user.OAuth2UserInfoFactory;
import com.linguatool.exception.auth.OAuth2AuthenticationProcessingException;
import com.linguatool.exception.auth.UserAlreadyExistsAuthenticationException;
import com.linguatool.model.dto.*;
import com.linguatool.model.entity.lang.CardTag;
import com.linguatool.model.entity.lang.Language;
import com.linguatool.model.entity.lang.LanguageEntity;
import com.linguatool.model.entity.lang.Tag;
import com.linguatool.model.entity.user.Role;
import com.linguatool.model.entity.user.User;
import com.linguatool.repository.*;
import com.linguatool.util.GeneralUtils;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    CardTagRepository cardTagRepository;
    TagRepository tagRepository;
    LanguageRepository languageRepository;

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
        if (!userRepository.existsByUsername(username)) {
            userRepository.changeUsername(username, id);
        }
    }

    @Override
    @Transactional(value = "transactionManager")
    public User registerNewUser(final SignUpRequest signUpRequest) throws UserAlreadyExistsAuthenticationException {
        if (signUpRequest.getUserID() != null && userRepository.existsById(signUpRequest.getUserID())) {
            throw new UserAlreadyExistsAuthenticationException("User with id " + signUpRequest.getUserID() + " already exists");
        } else if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new UserAlreadyExistsAuthenticationException("User with email id " + signUpRequest.getEmail() + " already exists");
        } else if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new UserAlreadyExistsAuthenticationException("User with username " + signUpRequest.getUsername() + " already exists");
        }
        User user = buildUser(signUpRequest);
        LocalDateTime now = LocalDateTime.now();
        user.setCreated(now);
        user.setTargetLanguages(Set.of(languageRepository.getEnglish()));
        user.setModified(now);
        user = userRepository.save(user);
        userRepository.flush();
        return user;
    }

    @Override
    @Transactional
    public LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, attributes);
        if (!StringUtils.hasLength(oAuth2UserInfo.getName())) {
            throw new OAuth2AuthenticationProcessingException("Name not found from OAuth2 provider");
        } else if (!StringUtils.hasLength(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        SignUpRequest userDetails = toUserRegistrationObject(registrationId, oAuth2UserInfo);
        User user = findUserByEmail(oAuth2UserInfo.getEmail());
        if (user != null) {
            if (!user.getProvider().equals(registrationId) && !user.getProvider().equals(SocialProvider.LOCAL.getProviderType())) {
                throw new OAuth2AuthenticationProcessingException(
                        "Looks like you're signed up with " + user.getProvider() + " account. Please use your " + user.getProvider() + " account to login.");
            }
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(userDetails);
        }

        return LocalUser.create(user, attributes, idToken, userInfo);
    }

    @Override
    @Transactional
    public void renameTagForUser(User user, Tag tag, String proposedName) {
        if (tag.getText().equals(Tag.normalizeText(proposedName))) {
            return;
        }
        Set<CardTag> foundTaggedCards = cardTagRepository.getByUserAndTag(user, tag);

        if (foundTaggedCards.isEmpty()) {
            return;
        }

        Optional<Tag> tagOptional = tagRepository.findByText(proposedName);
        Tag proposedTag;
        if (tagOptional.isPresent()) {
            proposedTag = tagOptional.get();
        } else {
            proposedTag = new Tag(proposedName);
            tagRepository.save(proposedTag);
        }
        foundTaggedCards.forEach(cardTag -> {
            cardTag.setTag(proposedTag);
            cardTagRepository.save(cardTag);
        });
    }

    @Override
    @Transactional
    public UserInfo buildUserInfo(LocalUser localUser) {
        List<String> roles = localUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        User user = localUser.getUser();
        List<String> tags = cardTagRepository.getUsersTags(user).stream().map(r -> tagRepository.getById(r).getText()).collect(Collectors.toList());
        List<String> targetLangs = user.getTargetLanguages().stream().map(t -> t.getCode().getCode()).collect(Collectors.toList());
        List<String> fluentLangs = user.getFluentLanguages().stream().map(t -> t.getCode().getCode()).collect(Collectors.toList());
        return new UserInfo(
                user.getId().toString(),
                user.getUsername(),
                user.getEmail(),
                user.getUiLanguage().getCode(),
                user.getProfilePicLink(),
                user.getBackground(),
                user.getStreak(),
                roles,
                tags,
                targetLangs,
                fluentLangs);
    }

    @Override
    @Transactional
    public void setTargetLangs(LangCodeDto dto, User user) {
        Set<LanguageEntity> languages = new HashSet<>();
        Arrays.stream(dto.getCodes()).forEach(
                code -> languages.add(
                        languageRepository.findByCode(Language.fromString(code)).orElseThrow()));
        user.setTargetLanguages(languages);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void setFluentLangs(LangCodeDto dto, User user) {
        Set<LanguageEntity> languages = new HashSet<>();
        Arrays.stream(dto.getCodes()).forEach(
                code -> languages.add(
                        languageRepository.findByCode(Language.fromString(code)).orElseThrow()));
        user.setFluentLanguages(languages);
        userRepository.save(user);
    }

    @Override
    public void updateLoginStreak(User user) {
        LocalDate lastLoginDate = user.getLastLogin().toLocalDate();
        LocalDate currentDate = LocalDate.now();

        user.setStreak(lastLoginDate
                .plusDays(1)
                .isEqual(currentDate)
                ? user.getStreak() + 1 : 1);

        user.setLastLogin(LocalDateTime.now());
    }

    private User buildUser(final SignUpRequest formDTO) {
        User user = new User();
        user.setUsername(formDTO.getUsername());
        user.setEmail(formDTO.getEmail());
        user.setPassword(passwordEncoder.encode(formDTO.getPassword()));
        final HashSet<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(Role.ROLE_USER));
        user.setRoles(roles);
        user.setProfilePicLink(formDTO.getProfilePicLink());
        user.setProvider(formDTO.getSocialProvider().getProviderType());
        user.setEnabled(true);
        user.setProviderUserId(formDTO.getProviderUserId());
        return user;
    }

    private SignUpRequest toUserRegistrationObject(String registrationId, OAuth2UserInfo oAuth2UserInfo) {
        return SignUpRequest.builder()
                .providerUserId(oAuth2UserInfo.getId())
                .email(oAuth2UserInfo.getEmail())
                .profilePicLink(oAuth2UserInfo.getImageUrl())
                .socialProvider(GeneralUtils.toSocialProvider(registrationId))
                .password("changeit")
                .build();
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setProfilePicLink(oAuth2UserInfo.getImageUrl());
        return userRepository.save(existingUser);
    }
}
