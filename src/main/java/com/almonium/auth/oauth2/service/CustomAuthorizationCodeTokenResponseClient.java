package com.almonium.auth.oauth2.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.enums.AuthProviderType;
import com.almonium.auth.oauth2.client.AppleTokenClient;
import com.almonium.auth.oauth2.dto.AppleTokenResponse;
import com.almonium.auth.oauth2.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CustomAuthorizationCodeTokenResponseClient
        implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {
    OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> defaultClient =
            new DefaultAuthorizationCodeTokenResponseClient();
    AppleTokenClient appleTokenClient;
    JwtUtil jwtUtil;
    ThreadLocalStore threadLocalStore;

    @NonFinal
    @Value("${spring.security.oauth2.client.registration.apple.client-secret}")
    String clientSecret;

    @NonFinal
    @Value("${spring.security.oauth2.client.registration.apple.authorization-grant-type}")
    String grantType;

    @Override
    public OAuth2AccessTokenResponse getTokenResponse(
            OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {
        String registrationId =
                authorizationCodeGrantRequest.getClientRegistration().getRegistrationId();

        if (AuthProviderType.APPLE.name().equalsIgnoreCase(registrationId)) {
            log.info("Intercepting Apple authentication request");
            return handleAppleAuth(authorizationCodeGrantRequest);
        }

        log.info("Delegating token request to default client for provider: {}", registrationId);
        return defaultClient.getTokenResponse(authorizationCodeGrantRequest);
    }

    @SneakyThrows
    private OAuth2AccessTokenResponse handleAppleAuth(
            OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {

        AppleTokenResponse response = appleTokenClient.getToken(
                grantType,
                authorizationCodeGrantRequest
                        .getAuthorizationExchange()
                        .getAuthorizationResponse()
                        .getCode(),
                authorizationCodeGrantRequest.getClientRegistration().getRedirectUri(),
                authorizationCodeGrantRequest.getClientRegistration().getClientId(),
                clientSecret);

        threadLocalStore.setAttributes(jwtUtil.verifyAndParseToken(response.idToken()));

        return OAuth2AccessTokenResponse.withToken(response.accessToken())
                .tokenType(OAuth2AccessToken.TokenType.BEARER)
                .expiresIn(response.expiresIn())
                .refreshToken(response.refreshToken())
                .build();
    }
}
