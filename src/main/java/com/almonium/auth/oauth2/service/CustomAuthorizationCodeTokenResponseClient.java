package com.almonium.auth.oauth2.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.enums.AuthProviderType;
import com.almonium.auth.oauth2.client.AppleTokenClient;
import com.almonium.auth.oauth2.dto.AppleTokenResponse;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
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
@FieldDefaults(level = PRIVATE)
public class CustomAuthorizationCodeTokenResponseClient
        implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {
    final AppleTokenClient appleTokenClient;

    @Value("${spring.security.oauth2.client.registration.apple.client-secret}")
    String clientSecret;

    @Value("${app.oauth2.appleTokenUrl}")
    String appleTokenUrl;

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

    @SneakyThrows
    private Map<String, Object> parseIdToken(String idToken) {
        // Decode the public key components from Base64
        DecodedJWT decodedJWT = JWT.decode(idToken);

        // Get the header from the token
        String headerJson = new String(Base64.getUrlDecoder().decode(decodedJWT.getHeader()));
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> headerMap = mapper.readValue(headerJson, Map.class);

        // Extract the modulus and exponent from the header
        String n = (String) headerMap.get("n");
        String e = (String) headerMap.get("e");

        // Decode the modulus and exponent from Base64
        BigInteger modulus = new BigInteger(1, Base64.getUrlDecoder().decode(n));
        BigInteger exponent = new BigInteger(1, Base64.getUrlDecoder().decode(e));

        // Generate the RSA public key
        RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        RSAPublicKey publicKey = (RSAPublicKey) factory.generatePublic(spec);

        // Verify the token
        Algorithm algorithm = Algorithm.RSA256(publicKey, null);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(appleTokenUrl)
                .withAudience("com.almonium.auth")
                .build();
        DecodedJWT jwt = verifier.verify(idToken);

        String email = jwt.getClaim("email").asString();
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("email", email);
        return userInfo;
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
