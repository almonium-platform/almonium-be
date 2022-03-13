package com.linguatool.service;

import com.linguatool.dto.LocalUser;
import com.linguatool.dto.SignUpRequest;
import com.linguatool.exception.UserAlreadyExistAuthenticationException;
import com.linguatool.model.User;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

import java.util.Map;
import java.util.Optional;


public interface UserService {

	User registerNewUser(SignUpRequest signUpRequest) throws UserAlreadyExistAuthenticationException;

	User findUserByEmail(String email);

	Optional<User> findUserById(Long id);

	LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo);
}
