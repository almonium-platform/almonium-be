package com.almonium.auth.oauth2.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.enums.AuthProviderType;
import com.almonium.auth.oauth2.client.AppleTokenClient;
import com.almonium.auth.oauth2.dto.AppleTokenResponse;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
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

    @Value("${spring.security.oauth2.client.provider.apple.jwk-set-uri}")
    String appleJwkUri;

    @Value("${app.oauth2.appleServiceId}")
    String appleServiceId;

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
        Map<String, String> formParameters = parseAuthorizationRequest(authorizationCodeGrantRequest);

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

    private Map<String, String> parseAuthorizationRequest(OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {
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
        // Decode the JWT token without verification to get the header
        DecodedJWT decodedJWT = JWT.decode(idToken);
        String kid = decodedJWT.getKeyId();

        // Fetch the JWKS from the identity provider
        RSAPublicKey publicKey = getPublicKeyFromJWKS(appleJwkUri, kid);

        // Verify the token
        Algorithm algorithm = Algorithm.RSA256(publicKey, null);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(appleTokenUrl)
                .withAudience(appleServiceId)
                .build();
        DecodedJWT jwt = verifier.verify(idToken);

        String emailClaim = "email";
        String email = jwt.getClaim(emailClaim).asString();
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put(emailClaim, email);
        return userInfo;
    }


    @SneakyThrows
    private RSAPublicKey getPublicKeyFromJWKS(String jwksUrl, String kid) {
        URL url = new URL(jwksUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        InputStream inputStream = connection.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jwks = mapper.readTree(inputStream);

        for (JsonNode key : jwks.get("keys")) {
            if (kid.equals(key.get("kid").asText())) {
                String n = key.get("n").asText();
                String e = key.get("e").asText();

                BigInteger modulus = new BigInteger(1, Base64.getUrlDecoder().decode(n));
                BigInteger exponent = new BigInteger(1, Base64.getUrlDecoder().decode(e));

                RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
                KeyFactory factory = KeyFactory.getInstance("RSA");
                return (RSAPublicKey) factory.generatePublic(spec);
            }
        }

        throw new IllegalStateException("Failed to retrieve public key from JWKS");
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
