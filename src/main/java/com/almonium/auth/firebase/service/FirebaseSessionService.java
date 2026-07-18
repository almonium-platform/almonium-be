package com.almonium.auth.firebase.service;

import com.almonium.auth.common.security.SecurityRoles;
import com.almonium.auth.firebase.exception.FirebaseAuthenticationException;
import com.almonium.auth.firebase.gateway.FirebaseAuthGateway;
import com.almonium.auth.firebase.model.FirebaseIdentity;
import com.almonium.auth.firebase.security.FirebasePrincipal;
import com.almonium.config.properties.AppProperties;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.service.ProfileService;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.Instant;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class FirebaseSessionService {
    private final FirebaseAuthGateway firebaseAuthGateway;
    private final FirebaseUserProvisioningService provisioningService;
    private final FirebaseSessionCookieService cookieService;
    private final ProfileService profileService;
    private final AppProperties appProperties;

    public FirebaseSessionService(
            FirebaseAuthGateway firebaseAuthGateway,
            FirebaseUserProvisioningService provisioningService,
            FirebaseSessionCookieService cookieService,
            ProfileService profileService,
            AppProperties appProperties) {
        this.firebaseAuthGateway = firebaseAuthGateway;
        this.provisioningService = provisioningService;
        this.cookieService = cookieService;
        this.profileService = profileService;
        this.appProperties = appProperties;
    }

    public User createSession(String idToken, HttpServletResponse response) {
        FirebaseIdentity identity = firebaseAuthGateway.verifyIdToken(idToken, true);
        requireRecentAuthentication(identity);
        User user = provisioningService.resolveOrCreate(identity);
        String sessionCookie = firebaseAuthGateway.createSessionCookie(idToken, sessionLifetime());
        cookieService.write(response, sessionCookie);
        profileService.updateLoginStreak(user.getProfile());
        SecurityContextHolder.getContext().setAuthentication(authentication(identity, user));
        return user;
    }

    public UsernamePasswordAuthenticationToken authentication(FirebaseIdentity identity, User user) {
        FirebasePrincipal principal = new FirebasePrincipal(
                identity.uid(), user.getId(), identity.email(), identity.authenticatedAt(), identity.signInProvider());
        return new UsernamePasswordAuthenticationToken(principal, null, SecurityRoles.USER);
    }

    private void requireRecentAuthentication(FirebaseIdentity identity) {
        Instant oldestAllowed =
                Instant.now().minusSeconds(appProperties.getAuth().getFirebase().getRecentLoginSeconds());
        if (identity.authenticatedAt().isBefore(oldestAllowed)) {
            throw new FirebaseAuthenticationException("Recent Firebase sign-in required");
        }
    }

    private Duration sessionLifetime() {
        return Duration.ofSeconds(appProperties.getAuth().getFirebase().getSessionLifetimeSeconds());
    }
}
