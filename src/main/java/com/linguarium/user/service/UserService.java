package com.linguarium.user.service;

import com.linguarium.auth.dto.UserInfo;
import com.linguarium.auth.dto.request.LoginRequest;
import com.linguarium.auth.dto.request.RegistrationRequest;
import com.linguarium.auth.dto.response.JwtAuthenticationResponse;
import com.linguarium.auth.model.LocalUser;
import com.linguarium.user.model.User;
import java.util.Map;
import java.util.Optional;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

public interface UserService {
    User register(RegistrationRequest registrationRequest);

    User findUserByEmail(String email);

    Optional<User> findUserById(Long id);

    JwtAuthenticationResponse login(LoginRequest loginRequest);

    LocalUser processAuthenticationFromProvider(
            String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo);

    UserInfo buildUserInfo(User user);

    void deleteAccount(User user);

    void changeUsername(String username, Long id);

    boolean isUsernameAvailable(String username);
}
