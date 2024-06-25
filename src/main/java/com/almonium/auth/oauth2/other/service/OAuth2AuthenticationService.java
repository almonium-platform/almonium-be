package com.almonium.auth.oauth2.other.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.local.exception.EmailMismatchException;
import com.almonium.auth.oauth2.other.model.entity.OAuth2Principal;
import com.almonium.auth.oauth2.other.model.enums.OAuth2Intent;
import com.almonium.auth.oauth2.other.model.userinfo.OAuth2UserInfo;
import com.almonium.auth.oauth2.other.repository.OAuth2PrincipalRepository;
import com.almonium.user.core.mapper.UserMapper;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import java.util.Optional;
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
    UserMapper userMapper;
    OAuth2PrincipalRepository principalRepository;

    @Transactional
    public OAuth2Principal authenticate(OAuth2UserInfo userInfo, OAuth2Intent intent) {
        log.debug("Starting authentication process for email: {}", userInfo.getEmail());

        return userRepository
                .findByEmail(userInfo.getEmail())
                .map(user -> handleExistingUser(user, userInfo))
                .orElseGet(() -> createNewUserAndPrincipal(userInfo, intent));
    }

    private OAuth2Principal handleExistingUser(User user, OAuth2UserInfo userInfo) {
        Optional<OAuth2Principal> existingAccountOptional =
                principalRepository.findByProviderAndProviderUserId(userInfo.getProvider(), userInfo.getId());

        if (existingAccountOptional.isEmpty()) {
            return createAndSaveProviderAuth(user, userInfo);
        }

        OAuth2Principal existingPrincipal = existingAccountOptional.get();

        log.debug("Updating existing user: {}", userInfo.getEmail());
        user.getProfile().setAvatarUrl(userInfo.getImageUrl());
        userRepository.save(user);

        userMapper.updatePrincipalFromUserInfo(existingPrincipal, userInfo);
        return principalRepository.save(existingPrincipal);
    }

    private OAuth2Principal createNewUserAndPrincipal(OAuth2UserInfo userInfo, OAuth2Intent intent) {
        if (intent == OAuth2Intent.LINK) {
            log.error("User not found for email: {}", userInfo.getEmail());
            throw new EmailMismatchException("No user found for email " + userInfo.getEmail() + " to link account.");
        }
        log.debug("Creating new user for email: {}", userInfo.getEmail());
        User user = new User();
        user.setEmail(userInfo.getEmail());
        return createAndSaveProviderAuth(user, userInfo);
    }

    private OAuth2Principal createAndSaveProviderAuth(User user, OAuth2UserInfo userInfo) {
        log.debug("Creating new principal for user: {}", userInfo.getEmail());
        OAuth2Principal account = userMapper.providerUserInfoToPrincipal(userInfo);
        account.setUser(user);
        user.getPrincipals().add(account);
        userRepository.save(user);
        return principalRepository.save(account);
    }
}
