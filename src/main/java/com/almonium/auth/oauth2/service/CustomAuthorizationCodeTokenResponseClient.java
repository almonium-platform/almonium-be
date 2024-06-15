package com.almonium.auth.oauth2.service;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CustomAuthorizationCodeTokenResponseClient implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {
    private static final Logger log = LoggerFactory.getLogger(CustomAuthorizationCodeTokenResponseClient.class);
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${spring.security.oauth2.client.registration.apple.client-secret}")
    private String clientSecret;

    @Override
    public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {
        // Create the request to Apple's token endpoint
        log.info("Authorization code grant request: {}", authorizationCodeGrantRequest);
        String tokenUri = authorizationCodeGrantRequest.getClientRegistration().getProviderDetails().getTokenUri();
        Map<String, String> formParameters = getStringStringMap(authorizationCodeGrantRequest);

        // Make the POST request
        OAuth2AccessTokenResponse tokenResponse = restTemplate.postForObject(tokenUri, formParameters, OAuth2AccessTokenResponse.class);
        log.info("Token response: {}", tokenResponse);
        // Extract the id_token from the response
        String idToken = (String) tokenResponse.getAdditionalParameters().get("id_token");
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
        formParameters.put("code", authorizationCodeGrantRequest.getAuthorizationExchange().getAuthorizationResponse().getCode());
        formParameters.put("redirect_uri", authorizationCodeGrantRequest.getClientRegistration().getRedirectUri());
        formParameters.put("client_id", authorizationCodeGrantRequest.getClientRegistration().getClientId());
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
}
