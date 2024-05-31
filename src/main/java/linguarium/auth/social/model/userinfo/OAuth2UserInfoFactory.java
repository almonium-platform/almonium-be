package linguarium.auth.social.model.userinfo;

import java.util.Map;
import java.util.function.Function;
import linguarium.auth.common.enums.AuthProviderType;
import linguarium.auth.social.exception.OAuth2AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class OAuth2UserInfoFactory {
    private final Map<AuthProviderType, Function<Map<String, Object>, OAuth2UserInfo>> strategies = Map.of(
            AuthProviderType.GOOGLE, GoogleOAuth2UserInfo::new,
            AuthProviderType.FACEBOOK, FacebookOAuth2UserInfo::new);

    public OAuth2UserInfo getOAuth2UserInfo(AuthProviderType provider, Map<String, Object> attributes) {
        Function<Map<String, Object>, OAuth2UserInfo> strategy = strategies.get(provider);
        if (strategy == null) {
            throw new OAuth2AuthenticationException("Sorry! Login with " + provider + " is not supported yet.");
        }
        return strategy.apply(attributes);
    }
}
