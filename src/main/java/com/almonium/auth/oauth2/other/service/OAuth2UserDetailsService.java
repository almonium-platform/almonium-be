package com.almonium.auth.oauth2.other.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.model.enums.AuthProviderType;
import com.almonium.auth.common.util.CookieUtil;
import com.almonium.auth.local.exception.EmailMismatchException;
import com.almonium.auth.local.exception.EmailNotVerifiedException;
import com.almonium.auth.local.exception.ReauthException;
import com.almonium.auth.oauth2.apple.util.ThreadLocalStore;
import com.almonium.auth.oauth2.other.exception.OAuth2AuthenticationException;
import com.almonium.auth.oauth2.other.model.enums.OAuth2Intent;
import com.almonium.auth.oauth2.other.model.userinfo.OAuth2UserInfo;
import com.almonium.auth.oauth2.other.model.userinfo.OAuth2UserInfoFactory;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
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

        var attributesMap = provider == AuthProviderType.APPLE
                ? threadLocalStore.getAttributesAndClearContext()
                : new HashMap<>(super.loadUser(oAuth2UserRequest).getAttributes());

        OAuth2UserInfo userInfo = userInfoFactory.getOAuth2UserInfo(provider, attributesMap);
        validateProviderUserInfo(userInfo);

        try {
            return switch (getIntent()) {
                case SIGN_IN -> authService.authenticate(userInfo);
                case REAUTH -> authService.reauthenticate(userInfo, getUserId());
                case LINK -> authService.linkAuthMethod(userInfo);
            };
        } catch (EmailNotVerifiedException
                | EmailMismatchException
                | ReauthException
                | OAuth2AuthenticationException ex) {
            throw new OAuth2AuthenticationException(ex.getMessage(), ex);
        } catch (Exception ex) {
            log.error("Authentication failed with unknown error", ex);
            throw new OAuth2AuthenticationException("Authentication failed", ex);
        }
    }

    private OAuth2Intent getIntent() {
        return CookieUtil.getCookie(getHttpServletRequest(), CookieUtil.INTENT_PARAM_COOKIE_NAME)
                .map(cookie -> OAuth2Intent.valueOf(cookie.getValue().toUpperCase()))
                .orElse(OAuth2Intent.SIGN_IN);
    }

    private long getUserId() {
        return CookieUtil.getCookie(getHttpServletRequest(), CookieUtil.USER_ID_PARAM_COOKIE_NAME)
                .map(cookie -> {
                    String value = cookie.getValue();
                    if (value != null && value.matches("\\d+")) {
                        return Long.parseLong(value);
                    }
                    throw new OAuth2AuthenticationException("Invalid user ID format in request");
                })
                .orElseThrow(() -> new OAuth2AuthenticationException("User ID not found in request"));
    }

    private HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }

    private void validateProviderUserInfo(OAuth2UserInfo userInfo) {
        if (!StringUtils.hasLength(userInfo.getEmail())) {
            throw new OAuth2AuthenticationException(
                    String.format("Email not found in %s provider response", userInfo.getProvider()));
        }

        if (!userInfo.isEmailVerified()) {
            log.error("Email not verified for user: {}", userInfo.getEmail());
            throw new EmailNotVerifiedException(String.format(
                    "Email %s is not verified by provider %s", userInfo.getEmail(), userInfo.getProvider()));
        }
    }
}
