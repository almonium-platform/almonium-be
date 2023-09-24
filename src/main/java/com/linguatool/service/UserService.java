package com.linguatool.service;

import com.linguatool.exception.auth.UserAlreadyExistsAuthenticationException;
import com.linguatool.model.dto.LangCodeDto;
import com.linguatool.model.dto.LocalUser;
import com.linguatool.model.dto.SignUpRequest;
import com.linguatool.model.dto.UserInfo;
import com.linguatool.model.entity.lang.Tag;
import com.linguatool.model.entity.user.User;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

import java.util.Map;
import java.util.Optional;

public interface UserService {
    User registerNewUser(SignUpRequest signUpRequest) throws UserAlreadyExistsAuthenticationException;

    User findUserByEmail(String email);

    Optional<User> findUserById(Long id);

    LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo);

    void renameTagForUser(User user, Tag tag, String proposedName);

    UserInfo buildUserInfo(LocalUser localUser);

    void deleteAccount(User user);

    void setTargetLangs(LangCodeDto dto, User user);

    void setFluentLangs(LangCodeDto dto, User user);

    void updateLoginStreak(User user);

    void changeUsername(String username, Long id);

    boolean existsByUsername(String username);
}
