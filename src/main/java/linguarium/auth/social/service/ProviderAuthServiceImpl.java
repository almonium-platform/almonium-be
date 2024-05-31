package linguarium.auth.social.service;

import static lombok.AccessLevel.PRIVATE;

import java.util.Map;
import java.util.Optional;
import linguarium.auth.common.entity.Principal;
import linguarium.auth.common.repository.PrincipalRepository;
import linguarium.auth.social.model.userinfo.OAuth2UserInfo;
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
    private static final String PLACEHOLDER = "OAUTH2_PLACEHOLDER";

    UserRepository userRepository;
    UserMapper userMapper;
    PrincipalRepository principalRepository;

    @Transactional
    public Principal authenticate(OAuth2UserInfo userInfo, Map<String, Object> attributes) {
        return userRepository
                .findByEmail(userInfo.getEmail())
                .map(user -> handleExistingUser(user, userInfo, attributes))
                .orElseGet(() -> createNewUserAndPrincipal(userInfo, attributes));
    }

    private Principal handleExistingUser(User user, OAuth2UserInfo userInfo, Map<String, Object> attributes) {
        Optional<Principal> existingAccountOptional =
                principalRepository.findByProviderAndProviderUserId(userInfo.getProvider(), userInfo.getId());

        if (existingAccountOptional.isEmpty()) {
            return createAndSaveProviderAuth(user, userInfo, attributes);
        }
        log.debug("Updating avatar URL for existing user: {}", userInfo.getEmail());
        user.getProfile().setAvatarUrl(userInfo.getImageUrl());
        userRepository.save(user);
        return existingAccountOptional.get();
    }

    private Principal createNewUserAndPrincipal(OAuth2UserInfo userInfo, Map<String, Object> attributes) {
        log.debug("Creating new user for email: {}", userInfo.getEmail());
        User user = new User();
        user.setEmail(userInfo.getEmail());
        user.setUsername(String.format("%s-%s", userInfo.getProvider(), userInfo.getId()));
        return createAndSaveProviderAuth(user, userInfo, attributes);
    }

    private Principal createAndSaveProviderAuth(User user, OAuth2UserInfo userInfo, Map<String, Object> attributes) {
        log.debug("Creating new principal for user: {}", userInfo.getEmail());
        Principal account = userMapper.providerUserInfoToPrincipal(userInfo);
        account.setAttributes(attributes);
        account.setUser(user);
        account.setPassword(PLACEHOLDER);
        user.getPrincipals().add(account);
        userRepository.save(user);
        return principalRepository.save(account);
    }
}
