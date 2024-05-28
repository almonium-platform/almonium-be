package linguarium.auth.oauth2.service;

import static lombok.AccessLevel.PRIVATE;

import java.util.Map;
import java.util.Optional;
import linguarium.auth.oauth2.model.entity.ProviderAccount;
import linguarium.auth.oauth2.model.userinfo.OAuth2UserInfo;
import linguarium.auth.oauth2.repository.ProviderAccountRepository;
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
    ProviderAccountRepository providerAccountRepository;

    @Transactional
    public ProviderAccount authenticate(OAuth2UserInfo userInfo, Map<String, Object> attributes) {
        return userRepository
                .findByEmail(userInfo.getEmail())
                .map(user -> handleExistingUser(user, userInfo, attributes))
                .orElseGet(() -> createNewUserAndProviderAccount(userInfo, attributes));
    }

    private ProviderAccount handleExistingUser(User user, OAuth2UserInfo userInfo, Map<String, Object> attributes) {
        Optional<ProviderAccount> existingAccountOptional =
                providerAccountRepository.findByProviderAndProviderUserId(userInfo.getProvider(), userInfo.getId());

        if (existingAccountOptional.isEmpty()) {
            return createAndSaveProviderAccount(user, userInfo, attributes);
        }
        log.debug("Updating avatar URL for existing user: {}", userInfo.getEmail());
        user.getProfile().setAvatarUrl(userInfo.getImageUrl());
        userRepository.save(user);
        return existingAccountOptional.get();
    }

    private ProviderAccount createNewUserAndProviderAccount(OAuth2UserInfo userInfo, Map<String, Object> attributes) {
        log.debug("Creating new user for email: {}", userInfo.getEmail());
        User user = new User();
        user.setPassword(PLACEHOLDER);
        user.setEmail(userInfo.getEmail());
        user.setUsername(String.format("%s-%s", userInfo.getProvider(), userInfo.getId()));
        return createAndSaveProviderAccount(user, userInfo, attributes);
    }

    private ProviderAccount createAndSaveProviderAccount(
            User user, OAuth2UserInfo userInfo, Map<String, Object> attributes) {
        log.debug("Creating new provider account for user: {}", userInfo.getEmail());
        ProviderAccount account = userMapper.providerUserInfoToProviderAccount(userInfo);
        account.setAttributes(attributes);
        account.setUser(user);
        user.getProviderAccounts().add(account);
        userRepository.save(user);
        return providerAccountRepository.save(account);
    }
}
