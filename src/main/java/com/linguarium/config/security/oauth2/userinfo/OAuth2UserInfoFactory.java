package com.linguarium.config.security.oauth2.userinfo;

import com.linguarium.auth.dto.AuthProvider;
import com.linguarium.auth.exception.OAuth2AuthenticationProcessingException;
import java.util.Map;
import java.util.function.Function;
import org.springframework.stereotype.Service;

@Service
public class OAuth2UserInfoFactory {
    private final Map<AuthProvider, Function<Map<String, Object>, OAuth2UserInfo>> strategies = Map.of(
            AuthProvider.GOOGLE, GoogleOAuth2UserInfo::new,
            AuthProvider.FACEBOOK, FacebookOAuth2UserInfo::new);

    public OAuth2UserInfo getOAuth2UserInfo(AuthProvider provider, Map<String, Object> attributes) {
        Function<Map<String, Object>, OAuth2UserInfo> strategy = strategies.get(provider);
        if (strategy == null) {
            throw new OAuth2AuthenticationProcessingException(
                    "Sorry! Login with " + provider + " is not supported yet.");
        }
        return strategy.apply(attributes);
    }
}
