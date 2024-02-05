package com.linguarium.config.security.oauth2.userinfo;

import com.linguarium.auth.dto.SocialProvider;
import com.linguarium.auth.exception.OAuth2AuthenticationProcessingException;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.function.Function;
import org.springframework.stereotype.Service;

@Service
public class OAuth2UserInfoFactory {
    private final Map<String, Function<Map<String, Object>, OAuth2UserInfo>> strategies = Map.of(
            SocialProvider.GOOGLE.getProviderType(), GoogleOAuth2UserInfo::new,
            SocialProvider.FACEBOOK.getProviderType(), FacebookOAuth2UserInfo::new);

    public OAuth2UserInfo getOAuth2UserInfo(@NotNull String registrationId, Map<String, Object> attributes) {
        Function<Map<String, Object>, OAuth2UserInfo> strategy = strategies.get(registrationId.toLowerCase());
        if (strategy == null) {
            throw new OAuth2AuthenticationProcessingException(
                    "Sorry! Login with " + registrationId + " is not supported yet.");
        }
        return strategy.apply(attributes);
    }
}
