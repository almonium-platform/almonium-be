package com.linguarium.auth.service.impl;

import static com.linguarium.util.GeneralUtils.generateId;
import static lombok.AccessLevel.PRIVATE;

import com.linguarium.auth.dto.AuthProvider;
import com.linguarium.auth.exception.OAuth2AuthenticationProcessingException;
import com.linguarium.auth.service.ProviderAuthService;
import com.linguarium.config.security.oauth2.userinfo.OAuth2UserInfo;
import com.linguarium.user.mapper.UserMapper;
import com.linguarium.user.model.User;
import com.linguarium.user.repository.UserRepository;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ProviderAuthServiceImpl implements ProviderAuthService {
    private static final String PLACEHOLDER = "OAUTH2_PLACEHOLDER";
    UserRepository userRepository;
    UserMapper userMapper;

    public ProviderAuthServiceImpl(
            UserRepository userRepository,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
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

    private void validateUserProviderMatch(User user, AuthProvider provider) {
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
