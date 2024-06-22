package com.almonium.auth.oauth2.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.enums.AuthProviderType;
import com.almonium.auth.oauth2.client.AppleTokenClient;
import com.almonium.auth.oauth2.dto.AppleTokenResponse;
import com.almonium.auth.oauth2.util.JwtUtil;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
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
    AppleUserStore appleUserStore;

    @NonFinal
    @Value("${spring.security.oauth2.client.registration.apple.client-secret}")
    String clientSecret;

    @Override
    public OAuth2AccessTokenResponse getTokenResponse(
            OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {
        String registrationId =
                authorizationCodeGrantRequest.getClientRegistration().getRegistrationId();

        if (AuthProviderType.APPLE.name().equalsIgnoreCase(registrationId)) {
            return handleAppleAuth(authorizationCodeGrantRequest);
        }
        log.info("Delegating token request to default client for provider: {}", registrationId);
        return defaultClient.getTokenResponse(authorizationCodeGrantRequest);
    }

    private OAuth2AccessTokenResponse handleAppleAuth(
            OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {
        String tokenUri = authorizationCodeGrantRequest
                .getClientRegistration()
                .getProviderDetails()
                .getTokenUri();
        Map<String, String> formParameters = parseAuthorizationRequest(authorizationCodeGrantRequest);

        log.info("Sending request to {} with parameters {}", tokenUri, formParameters);

        AppleTokenResponse response = appleTokenClient.getToken(
                formParameters.get("grant_type"),
                formParameters.get("code"),
                formParameters.get("redirect_uri"),
                formParameters.get("client_id"),
                formParameters.get("client_secret"));

        log.info("Token response: {}", response);

        OAuth2AccessTokenResponse tokenResponse = convertToOAuth2AccessTokenResponse(response);

        String idToken = response.idToken();
        if (idToken != null) {
            try {
                Map<String, Object> userInfo = jwtUtil.parseAndVerifyToken(idToken);
                log.info("User info: {}", userInfo);
                appleUserStore.setAppleUser(userInfo);
            } catch (Exception e) {
                log.error("Failed to parse and verify id token", e);
            }
        }

        return tokenResponse;
    }

    private Map<String, String> parseAuthorizationRequest(
            OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {
        Map<String, String> formParameters = new HashMap<>();
        formParameters.put("grant_type", "authorization_code");
        formParameters.put(
                "code",
                authorizationCodeGrantRequest
                        .getAuthorizationExchange()
                        .getAuthorizationResponse()
                        .getCode());
        formParameters.put(
                "redirect_uri",
                authorizationCodeGrantRequest.getClientRegistration().getRedirectUri());
        formParameters.put(
                "client_id",
                authorizationCodeGrantRequest.getClientRegistration().getClientId());
        formParameters.put("client_secret", clientSecret);
        return formParameters;
    }

    private OAuth2AccessTokenResponse convertToOAuth2AccessTokenResponse(AppleTokenResponse response) {
        return OAuth2AccessTokenResponse.withToken(response.accessToken())
                .tokenType(OAuth2AccessToken.TokenType.BEARER)
                .expiresIn(response.expiresIn())
                .refreshToken(response.refreshToken())
                .additionalParameters(Map.of("id_token", response.idToken()))
                .build();
    }
}
