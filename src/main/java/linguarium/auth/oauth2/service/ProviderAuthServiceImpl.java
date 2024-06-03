package linguarium.auth.oauth2.service;

import static lombok.AccessLevel.PRIVATE;

import java.util.Map;
import java.util.Optional;
import linguarium.auth.local.exception.EmailMismatchException;
import linguarium.auth.oauth2.model.OAuth2Principal;
import linguarium.auth.oauth2.model.enums.OAuth2Intent;
import linguarium.auth.oauth2.model.userinfo.OAuth2UserInfo;
import linguarium.auth.oauth2.repository.OAuth2PrincipalRepository;
import linguarium.user.core.mapper.UserMapper;
import linguarium.user.core.model.entity.User;
import linguarium.user.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ProviderAuthServiceImpl {
    UserRepository userRepository;
    UserMapper userMapper;
    OAuth2PrincipalRepository principalRepository;

    @Transactional
    public OAuth2Principal authenticate(OAuth2UserInfo userInfo, Map<String, Object> attributes, OAuth2Intent intent) {
        return userRepository
                .findByEmail(userInfo.getEmail())
                .map(user -> handleExistingUser(user, userInfo, attributes))
                .orElseGet(() -> createNewUserAndPrincipal(userInfo, attributes, intent));
    }

    private OAuth2Principal handleExistingUser(User user, OAuth2UserInfo userInfo, Map<String, Object> attributes) {
        Optional<OAuth2Principal> existingAccountOptional =
                principalRepository.findByProviderAndProviderUserId(userInfo.getProvider(), userInfo.getId());

        if (existingAccountOptional.isEmpty()) {
            return createAndSaveProviderAuth(user, userInfo, attributes);
        }
        log.debug("Updating avatar URL for existing user: {}", userInfo.getEmail());
        user.getProfile().setAvatarUrl(userInfo.getImageUrl());
        userRepository.save(user);
        return existingAccountOptional.get();
    }

    private OAuth2Principal createNewUserAndPrincipal(
            OAuth2UserInfo userInfo, Map<String, Object> attributes, OAuth2Intent intent) {
        if (intent == OAuth2Intent.LINK) {
            log.error("User not found for email: {}", userInfo.getEmail());
            throw new EmailMismatchException("No user found for email " + userInfo.getEmail() + " to link account.");
        }
        log.debug("Creating new user for email: {}", userInfo.getEmail());
        User user = new User();
        user.setEmail(userInfo.getEmail());
        return createAndSaveProviderAuth(user, userInfo, attributes);
    }

    private OAuth2Principal createAndSaveProviderAuth(
            User user, OAuth2UserInfo userInfo, Map<String, Object> attributes) {
        log.debug("Creating new principal for user: {}", userInfo.getEmail());
        OAuth2Principal account = userMapper.providerUserInfoToPrincipal(userInfo);
        account.setUser(user);
        account.setAttributes(attributes);
        user.getPrincipals().add(account);
        userRepository.save(user);
        return principalRepository.save(account);
    }
}
