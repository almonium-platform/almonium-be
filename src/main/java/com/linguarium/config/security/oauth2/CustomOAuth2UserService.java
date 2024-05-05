package com.linguarium.config.security.oauth2;

import static lombok.AccessLevel.PRIVATE;

import com.linguarium.auth.exception.OAuth2AuthenticationProcessingException;
import com.linguarium.user.service.impl.AuthService;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    AuthService authService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        try {
            Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
            String provider = oAuth2UserRequest.getClientRegistration().getRegistrationId();
            return authService.processProviderAuth(provider, attributes, null, null);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new OAuth2AuthenticationProcessingException(ex.getMessage(), ex.getCause());
        }
    }
}
