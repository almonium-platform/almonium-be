package com.almonium.auth.oauth2.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.enums.AuthProviderType;
import com.almonium.auth.oauth2.exception.OAuth2AuthenticationException;
import com.almonium.auth.oauth2.model.enums.OAuth2Intent;
import com.almonium.auth.oauth2.model.userinfo.OAuth2UserInfo;
import com.almonium.auth.oauth2.model.userinfo.OAuth2UserInfoFactory;
import com.almonium.auth.oauth2.util.CookieUtil;
import com.almonium.auth.oauth2.util.ThreadLocalStore;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class OAuth2UserDetailsService extends DefaultOAuth2UserService {
    OAuth2AuthenticationService authService;
    OAuth2UserInfoFactory userInfoFactory;
    ThreadLocalStore threadLocalStore;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        AuthProviderType provider = AuthProviderType.valueOf(
                oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase());

        Map<String, Object> attributes = provider == AuthProviderType.APPLE
                ? threadLocalStore.getAttributesAndClearContext()
                : new HashMap<>(super.loadUser(oAuth2UserRequest).getAttributes());

        OAuth2UserInfo userInfo = userInfoFactory.getOAuth2UserInfo(provider, attributes);
        validateProviderUserInfo(userInfo);

        try {
            return authService.authenticate(userInfo, getIntent());
        } catch (Exception ex) {
            throw new OAuth2AuthenticationException("Authentication failed", ex);
        }
    }

    private OAuth2Intent getIntent() {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        return CookieUtil.getCookie(request, CookieUtil.INTENT_PARAM_COOKIE_NAME)
                .map(cookie -> OAuth2Intent.valueOf(cookie.getValue().toUpperCase()))
                .orElse(OAuth2Intent.SIGN_IN);
    }

    private void validateProviderUserInfo(OAuth2UserInfo oAuth2UserInfo) {
        if (!StringUtils.hasLength(oAuth2UserInfo.getName())) {
            throw new OAuth2AuthenticationException("Name not found from OAuth2 provider");
        }

        if (!StringUtils.hasLength(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
        }
    }
}
