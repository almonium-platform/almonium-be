package linguarium.auth.oauth2.service;

import static lombok.AccessLevel.PRIVATE;

import java.util.Map;
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
    UserRepository userRepository;
    UserMapper userMapper;
    ProviderAccountRepository providerAccountRepository;

    @Transactional
    public ProviderAccount authenticate(OAuth2UserInfo userInfo, Map<String, Object> attributes) {
        ProviderAccount account = userMapper.providerUserInfoToProviderAccount(userInfo);
        account.setAttributes(attributes);

        userRepository
                .findByEmail(userInfo.getEmail())
                .ifPresentOrElse(user -> updateUser(user, account, userInfo),
                        () -> createUser(account));

        return account;
    }

    private void createUser(ProviderAccount account) {
        User user = new User(account);
        userRepository.save(user);
        account.setUser(user);
        providerAccountRepository.save(account);
    }

    private void updateUser(User user, ProviderAccount account, OAuth2UserInfo userInfo) {
        if (user.getProviderAccounts().stream()
                .anyMatch(acc -> acc.getProvider().equals(userInfo.getProvider()))) {
            user.getProfile().setAvatarUrl(userInfo.getImageUrl());
        } else {
            user.getProviderAccounts().add(account);
        }
        userRepository.save(user);
    }
}
