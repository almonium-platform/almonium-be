package linguarium.auth.oauth2.service;

import static linguarium.util.GeneralUtils.generateId;
import static lombok.AccessLevel.PRIVATE;

import linguarium.auth.oauth2.exception.OAuth2AuthenticationProcessingException;
import linguarium.auth.oauth2.model.enums.AuthProviderType;
import linguarium.auth.oauth2.model.userinfo.OAuth2UserInfo;
import linguarium.user.core.mapper.UserMapper;
import linguarium.user.core.model.entity.User;
import linguarium.user.core.repository.UserRepository;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ProviderAuthServiceImpl {
    private static final String PLACEHOLDER = "OAUTH2_PLACEHOLDER";
    UserRepository userRepository;
    UserMapper userMapper;

    public ProviderAuthServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public User authenticate(OAuth2UserInfo userInfo) {
        User user = userRepository
                .findByEmail(userInfo.getEmail())
                .map(existingUser -> {
                    validateUserProviderMatch(existingUser, userInfo.getProvider());
                    return existingUser;
                })
                .orElseGet(() -> {
                    User newUser = userMapper.providerUserInfoToUser(userInfo);
                    newUser.setUsername(generateId());
                    newUser.setPassword(PLACEHOLDER);
                    userRepository.save(newUser);
                    return newUser;
                });

        return updateUserWithProviderInfo(user, userInfo);
    }

    private void validateUserProviderMatch(User user, AuthProviderType provider) {
        if (!user.getProvider().equals(provider)) {
            throw new OAuth2AuthenticationProcessingException(
                    "Looks like you're signed up with " + user.getProvider() + " account. Please use it to login.");
        }
    }

    private User updateUserWithProviderInfo(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.getProfile().setAvatarUrl(oAuth2UserInfo.getImageUrl()); // todo save, but not update
        return userRepository.save(existingUser);
    }
}
