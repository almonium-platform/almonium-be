package com.linguarium.user.service;

import com.linguarium.auth.dto.UserInfo;
import com.linguarium.auth.dto.request.RegistrationRequest;
import com.linguarium.auth.exception.UserAlreadyExistsAuthenticationException;
import com.linguarium.auth.model.LocalUser;
import com.linguarium.user.model.User;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

import java.util.Map;
import java.util.Optional;

public interface UserService {
    User registerNewUser(RegistrationRequest registrationRequest) throws UserAlreadyExistsAuthenticationException;

    User findUserByEmail(String email);

    Optional<User> findUserById(Long id);

    LocalUser processUserRegistration(String registrationId,
                                      Map<String, Object> attributes,
                                      OidcIdToken idToken,
                                      OidcUserInfo userInfo);

    UserInfo buildUserInfo(User user);

    void deleteAccount(User user);

    void changeUsername(String username, Long id);

    boolean isUsernameAvailable(String username);
}
