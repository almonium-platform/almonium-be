package com.almonium.auth.oauth2.other.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.local.exception.EmailMismatchException;
import com.almonium.auth.local.exception.ReauthException;
import com.almonium.auth.oauth2.other.exception.OAuth2AuthenticationException;
import com.almonium.auth.oauth2.other.model.entity.OAuth2Principal;
import com.almonium.auth.oauth2.other.model.userinfo.OAuth2UserInfo;
import com.almonium.auth.oauth2.other.repository.OAuth2PrincipalRepository;
import com.almonium.user.core.factory.UserFactory;
import com.almonium.user.core.mapper.UserMapper;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import com.almonium.user.core.service.AvatarService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class OAuth2AuthenticationService {
    AvatarService avatarService;

    OAuth2PrincipalRepository principalRepository;
    UserRepository userRepository;

    UserFactory userFactory;

    UserMapper userMapper;

    @Transactional
    public OAuth2Principal reauthenticate(OAuth2UserInfo userInfo, UUID userId) {
        log.debug("Starting reauthentication process for email: {}", userInfo.getEmail());
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ReauthException("User not found by id: " + userId));

        if (!user.getEmail().equals(userInfo.getEmail())) {
            throw new ReauthException(String.format(
                    "Can't reauthenticate user with email: %s using provider with email: %s",
                    user.getEmail(), userInfo.getEmail()));
        }

        return principalRepository
                .findByProviderAndProviderUserId(userInfo.getProvider(), userInfo.getId())
                .orElseThrow(() -> new ReauthException("Can't reauthenticate via provider account not linked to user"));
    }

    @Transactional
    public OAuth2Principal authenticate(OAuth2UserInfo userInfo) {
        log.debug("Starting authentication process for email: {}", userInfo.getEmail());

        // check if we have this provider account
        return principalRepository
                .findByProviderAndProviderUserId(userInfo.getProvider(), userInfo.getId())
                .map(existingPrincipal -> updateExistingPrincipal(existingPrincipal, userInfo))
                .orElseGet(() -> handleNewProviderAccount(userInfo));
    }

    @Transactional
    public OAuth2Principal linkAuthMethod(OAuth2UserInfo userInfo) {
        if (principalRepository
                .findByProviderAndProviderUserId(userInfo.getProvider(), userInfo.getId())
                .isPresent()) {
            throw new OAuth2AuthenticationException("Provider account already linked to user");
        }

        var user = userRepository
                .findByEmail(userInfo.getEmail())
                .orElseThrow(
                        () -> new OAuth2AuthenticationException("User not found by email: " + userInfo.getEmail()));

        log.debug("Linking provider account to user: {}", userInfo.getEmail());
        return principalRepository.save(createAndSaveNewPrincipalForExistingUser(user, userInfo));
    }

    private OAuth2Principal updateExistingPrincipal(OAuth2Principal existingPrincipal, OAuth2UserInfo userInfo) {
        User user = existingPrincipal.getUser();

        // check if user changed email
        if (!user.getEmail().equals(userInfo.getEmail())) {
            log.error("User changed email from {} to {}", user.getEmail(), userInfo.getEmail());
            // TODO handle more gracefully
            throw new EmailMismatchException(String.format(
                    "User had this provider account registered with email: %s but now has email: %s",
                    user.getEmail(), userInfo.getEmail()));
        }

        // match by provider/providerUserId AND email
        log.debug("Updating existing user: {}", userInfo.getEmail());
        userMapper.updatePrincipalFromUserInfo(existingPrincipal, userInfo);
        return principalRepository.save(existingPrincipal);
    }

    private OAuth2Principal handleNewProviderAccount(OAuth2UserInfo userInfo) {
        log.debug(
                "We don't recognize this provider account. Provider: {}, providerUserId: {}",
                userInfo.getProvider(),
                userInfo.getId());

        var userOptional = userRepository.findByEmail(userInfo.getEmail());

        if (userOptional.isPresent()) {
            log.debug("New provider account for existing user: {}", userInfo.getEmail());
            return createAndSaveNewPrincipalForExistingUser(userOptional.get(), userInfo);
        }

        log.debug("New provider account for new user: {}", userInfo.getEmail());
        return createNewUserAndPrincipal(userInfo);
    }

    private OAuth2Principal createNewUserAndPrincipal(OAuth2UserInfo userInfo) {
        log.debug("Creating new user for email: {}", userInfo.getEmail());
        User user = userFactory.createUserWithDefaultPlan(userInfo.getEmail(), true);
        if (userInfo.getAvatarUrl() != null) {
            avatarService.doAvatarUpload(
                    userInfo.getAvatarUrl(), user.getProfile().getId());
        }
        return createAndSaveNewPrincipalForExistingUser(user, userInfo);
    }

    private OAuth2Principal createAndSaveNewPrincipalForExistingUser(User user, OAuth2UserInfo userInfo) {
        log.debug("Creating new principal for user: {}", userInfo.getEmail());
        OAuth2Principal principal = userMapper.providerUserInfoToPrincipal(userInfo);
        principal.setUser(user);
        user.getPrincipals().add(principal);
        principalRepository.save(principal);
        return principal;
    }
}
