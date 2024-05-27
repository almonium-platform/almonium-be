package linguarium.auth.oauth2.service;

import static lombok.AccessLevel.PRIVATE;

import java.util.HashMap;
import java.util.Map;
import linguarium.auth.oauth2.exception.OAuth2AuthenticationProcessingException;
import linguarium.auth.oauth2.model.AuthProviderType;
import linguarium.auth.oauth2.model.userinfo.OAuth2UserInfo;
import linguarium.auth.oauth2.model.userinfo.OAuth2UserInfoFactory;
import linguarium.user.core.model.User;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    ProviderAuthServiceImpl authService;
    OAuth2UserInfoFactory userInfoFactory;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        AuthProviderType provider = AuthProviderType.valueOf(
                oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase());

        OAuth2UserInfo userInfo = userInfoFactory.getOAuth2UserInfo(provider, attributes);

        validateProviderUserInfo(userInfo);

        try {
            User user = authService.authenticate(userInfo);
            user.setAttributes(attributes);
            return user;
        } catch (Exception ex) {
            throw new OAuth2AuthenticationProcessingException("Authentication failed", ex);
        }
    }

    private void validateProviderUserInfo(OAuth2UserInfo oAuth2UserInfo) {
        if (!StringUtils.hasLength(oAuth2UserInfo.getName())) {
            throw new OAuth2AuthenticationProcessingException("Name not found from OAuth2 provider");
        }

        if (!StringUtils.hasLength(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }
    }
}
