package com.almonium.auth.oauth2.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.enums.AuthProviderType;
import com.almonium.auth.oauth2.exception.OAuth2AuthenticationException;
import com.almonium.auth.oauth2.model.enums.OAuth2Intent;
import com.almonium.auth.oauth2.model.userinfo.OAuth2UserInfo;
import com.almonium.auth.oauth2.model.userinfo.OAuth2UserInfoFactory;
import com.almonium.auth.oauth2.util.CookieUtils;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import java.text.ParseException;
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
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    ProviderAuthServiceImpl authService;
    OAuth2UserInfoFactory userInfoFactory;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        log.info("OAuth2User: {}", oAuth2User);

        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        log.info("Attributes: {}", attributes);

        AuthProviderType provider = AuthProviderType.valueOf(
                oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase());
        log.info("Provider: {}", provider);

        if (provider == AuthProviderType.APPLE) {
            // Extract ID token from userRequest
            String idToken =
                    (String) oAuth2UserRequest.getAdditionalParameters().get("id_token");
            log.info("ID Token: {}", idToken);

            // Parse the ID token
            try {
                SignedJWT signedJWT = SignedJWT.parse(idToken);
                JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
                attributes.put("sub", claims.getSubject());
                attributes.put("email", claims.getStringClaim("email"));
                Map<String, Object> name = new HashMap<>();
                name.put("firstName", claims.getStringClaim("firstName"));
                name.put("lastName", claims.getStringClaim("lastName"));
                attributes.put("name", name);
                log.info("Parsed ID Token: {}", attributes);
            } catch (ParseException e) {
                log.error("Failed to parse ID token", e);
                throw new OAuth2AuthenticationException("Failed to parse ID token", e);
            }
        }

        OAuth2UserInfo userInfo = userInfoFactory.getOAuth2UserInfo(provider, attributes);

        validateProviderUserInfo(userInfo);

        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        OAuth2Intent intent = CookieUtils.getCookie(request, CookieUtils.INTENT_PARAM_COOKIE_NAME)
                .map(cookie -> OAuth2Intent.valueOf(cookie.getValue().toUpperCase()))
                .orElse(OAuth2Intent.SIGN_IN);

        try {
            return authService.authenticate(userInfo, attributes, intent);
        } catch (Exception ex) {
            throw new OAuth2AuthenticationException("Authentication failed", ex);
        }
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
