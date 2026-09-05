package com.almonium.auth.firebase.filter;

import com.almonium.auth.firebase.exception.FirebaseAuthenticationException;
import com.almonium.auth.firebase.gateway.FirebaseAuthGateway;
import com.almonium.auth.firebase.model.FirebaseIdentity;
import com.almonium.auth.firebase.security.FirebaseBearerToken;
import com.almonium.auth.firebase.service.FirebaseSessionCookieService;
import com.almonium.auth.firebase.service.FirebaseSessionService;
import com.almonium.auth.firebase.service.FirebaseUserProvisioningService;
import com.almonium.config.aspect.SkipLogging;
import com.almonium.user.core.exception.ResourceConflictException;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@SkipLogging
public class FirebaseSessionAuthenticationFilter extends OncePerRequestFilter {
    private final FirebaseAuthGateway firebaseAuthGateway;
    private final FirebaseSessionCookieService cookieService;
    private final FirebaseSessionService sessionService;
    private final FirebaseUserProvisioningService provisioningService;
    private final UserRepository userRepository;

    public FirebaseSessionAuthenticationFilter(
            FirebaseAuthGateway firebaseAuthGateway,
            FirebaseSessionCookieService cookieService,
            FirebaseSessionService sessionService,
            FirebaseUserProvisioningService provisioningService,
            UserRepository userRepository) {
        this.firebaseAuthGateway = firebaseAuthGateway;
        this.cookieService = cookieService;
        this.sessionService = sessionService;
        this.provisioningService = provisioningService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        FirebaseBearerToken.from(request)
                .ifPresentOrElse(
                        this::authenticateBearer,
                        () -> cookieService.read(request).ifPresent(cookie -> authenticateCookie(cookie, response)));
        filterChain.doFilter(request, response);
    }

    private void authenticateBearer(String token) {
        try {
            FirebaseIdentity identity = firebaseAuthGateway.verifyIdToken(token, false);
            User user = provisioningService.resolveOrCreate(identity);
            SecurityContextHolder.getContext().setAuthentication(sessionService.authentication(identity, user));
        } catch (FirebaseAuthenticationException | ResourceConflictException exception) {
            SecurityContextHolder.clearContext();
        }
    }

    private void authenticateCookie(String cookie, HttpServletResponse response) {
        try {
            FirebaseIdentity identity = firebaseAuthGateway.verifySessionCookie(cookie, false);
            userRepository
                    .findByFirebaseUid(identity.uid())
                    .ifPresentOrElse(
                            user -> SecurityContextHolder.getContext()
                                    .setAuthentication(sessionService.authentication(identity, user)),
                            () -> cookieService.clear(response));
        } catch (FirebaseAuthenticationException exception) {
            SecurityContextHolder.clearContext();
            cookieService.clear(response);
        }
    }
}
