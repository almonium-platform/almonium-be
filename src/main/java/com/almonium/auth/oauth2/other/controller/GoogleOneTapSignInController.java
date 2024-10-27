package com.almonium.auth.oauth2.other.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.model.entity.Principal;
import com.almonium.auth.common.model.enums.AuthProviderType;
import com.almonium.auth.common.service.impl.UserAuthenticationService;
import com.almonium.auth.oauth2.other.model.entity.OAuth2Principal;
import com.almonium.auth.oauth2.other.model.enums.OAuth2Intent;
import com.almonium.auth.oauth2.other.model.userinfo.OAuth2UserInfo;
import com.almonium.auth.oauth2.other.model.userinfo.OAuth2UserInfoFactory;
import com.almonium.auth.oauth2.other.service.OAuth2AuthenticationService;
import com.almonium.util.dto.ApiResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/auth")
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class GoogleOneTapSignInController {
    OAuth2AuthenticationService authService;
    UserAuthenticationService userAuthenticationService;
    OAuth2UserInfoFactory userInfoFactory;

    @NonFinal
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    String googleClientId;

    @PostMapping("/google/one-tap")
    public ResponseEntity<?> loginWithGoogle(
            @RequestBody Map<String, String> requestBody, HttpServletResponse response) {
        String idTokenString = requestBody.get("token");

        try {
            GoogleIdToken idToken = verifyGoogleToken(idTokenString);
            if (idToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Invalid token."));
            }

            OAuth2UserInfo userInfo = userInfoFactory.getOAuth2UserInfo(AuthProviderType.GOOGLE, idToken.getPayload());
            OAuth2Principal principal = authService.authenticate(userInfo, OAuth2Intent.SIGN_IN);

            Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, Principal.ROLES);

            userAuthenticationService.authenticateUser(principal, response, authentication);

            return ResponseEntity.ok(new ApiResponse(true, "User authenticated successfully."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, "Authentication failed."));
        }
    }

    private GoogleIdToken verifyGoogleToken(String idTokenString) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                        new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(googleClientId))
                .build();
        return verifier.verify(idTokenString);
    }
}
