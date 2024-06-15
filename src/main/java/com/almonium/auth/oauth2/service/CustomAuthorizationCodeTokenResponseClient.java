package com.almonium.auth.oauth2.service;

import com.almonium.auth.common.enums.AuthProviderType;
import com.almonium.auth.oauth2.client.AppleTokenClient;
import com.almonium.auth.oauth2.dto.AppleTokenResponse;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
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
public class CustomAuthorizationCodeTokenResponseClient
        implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {
    private final AppleTokenClient appleTokenClient;

    @Value("${spring.security.oauth2.client.registration.apple.client-secret}")
    private String clientSecret;

    private final OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> defaultClient =
            new DefaultAuthorizationCodeTokenResponseClient();

    public CustomAuthorizationCodeTokenResponseClient(AppleTokenClient appleTokenClient) {
        this.appleTokenClient = appleTokenClient;
    }

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
        Map<String, String> formParameters = getStringStringMap(authorizationCodeGrantRequest);

        log.info("Sending request to {} with parameters {}", tokenUri, formParameters);

        AppleTokenResponse response = appleTokenClient.getToken(
                formParameters.get("grant_type"),
                formParameters.get("code"),
                formParameters.get("redirect_uri"),
                formParameters.get("client_id"),
                formParameters.get("client_secret"));

        log.info("Token response: {}", response);

        // Convert AppleTokenResponse to OAuth2AccessTokenResponse
        OAuth2AccessTokenResponse tokenResponse = convertToOAuth2AccessTokenResponse(response);

        // Extract the id_token from the response
        String idToken = response.idToken();
        if (idToken != null) {
            Map<String, Object> userInfo = parseIdToken(idToken);
            log.info("User info: {}", userInfo);
            // Save the user info to session or database
            saveUserInfo(userInfo);
        }

        return tokenResponse;
    }

    private Map<String, String> getStringStringMap(OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {
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

    private Map<String, Object> parseIdToken(String idToken) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(idToken);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            String email = claims.getStringClaim("email");
            String firstName = (String) claims.getJSONObjectClaim("name").get("firstName");
            String lastName = (String) claims.getJSONObjectClaim("name").get("lastName");

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("email", email);
            userInfo.put("firstName", firstName);
            userInfo.put("lastName", lastName);

            return userInfo;
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse ID token", e);
        }
    }

    private void saveUserInfo(Map<String, Object> userInfo) {
        // Implement your logic to save user information
        // Example: save to database or session
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
