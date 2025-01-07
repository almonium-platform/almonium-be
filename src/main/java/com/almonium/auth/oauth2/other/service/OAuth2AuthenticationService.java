package com.almonium.auth.oauth2.other.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.local.exception.EmailMismatchException;
import com.almonium.auth.oauth2.other.model.entity.OAuth2Principal;
import com.almonium.auth.oauth2.other.model.enums.OAuth2Intent;
import com.almonium.auth.oauth2.other.model.userinfo.OAuth2UserInfo;
import com.almonium.auth.oauth2.other.repository.OAuth2PrincipalRepository;
import com.almonium.user.core.factory.UserFactory;
import com.almonium.user.core.mapper.UserMapper;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import com.almonium.user.core.service.AvatarService;
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
    UserRepository userRepository;
    UserFactory userFactory;
    UserMapper userMapper;
    OAuth2PrincipalRepository principalRepository;
    AvatarService avatarService;

    @Transactional
    public OAuth2Principal authenticate(OAuth2UserInfo userInfo, OAuth2Intent intent) {
        log.debug("Starting authentication process for email: {}", userInfo.getEmail());

        // check if we have this provider account
        return principalRepository
                .findByProviderAndProviderUserId(userInfo.getProvider(), userInfo.getId())
                .map(existingPrincipal -> updateExistingPrincipal(existingPrincipal, userInfo))
                .orElseGet(() -> handleNewProviderAccount(userInfo, intent));
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

    private OAuth2Principal handleNewProviderAccount(OAuth2UserInfo userInfo, OAuth2Intent intent) {
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
        if (intent == OAuth2Intent.LINK) {
            log.error("Can't link account registered with email: {}", userInfo.getEmail());
            throw new EmailMismatchException("We don't have an account with email: " + userInfo.getEmail());
        }

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
