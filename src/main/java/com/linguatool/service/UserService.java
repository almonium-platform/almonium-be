package com.linguatool.service;

import com.linguatool.exception.auth.UserAlreadyExistsAuthenticationException;
import com.linguatool.model.dto.LocalUser;
import com.linguatool.model.dto.SignUpRequest;
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
}
