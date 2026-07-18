package com.almonium.config.aspect;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.exception.RecentLoginRequiredException;
import com.almonium.auth.firebase.exception.FirebaseAuthenticationException;
import com.almonium.auth.firebase.gateway.FirebaseAuthGateway;
import com.almonium.auth.firebase.model.FirebaseIdentity;
import com.almonium.auth.firebase.security.FirebasePrincipal;
import com.almonium.auth.firebase.service.FirebaseSessionCookieService;
import com.almonium.config.properties.AppProperties;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RecentLoginAspect {
    FirebaseAuthGateway firebaseAuthGateway;
    FirebaseSessionCookieService cookieService;
    AppProperties appProperties;

    @Around(
            """
            @annotation(com.almonium.auth.common.annotation.RequireRecentLogin)
            || within(@com.almonium.auth.common.annotation.RequireRecentLogin *)
            """)
    public Object validateRecentLogin(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes)
                        Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest();

        String sessionCookie = cookieService.read(request).orElse(null);
        try {
            var authentication = org.springframework.security.core.context.SecurityContextHolder.getContext()
                    .getAuthentication();
            Object principal = authentication == null ? null : authentication.getPrincipal();
            if (sessionCookie == null || !(principal instanceof FirebasePrincipal firebasePrincipal)) {
                throw new FirebaseAuthenticationException("Firebase session is missing");
            }
            FirebaseIdentity identity = firebaseAuthGateway.verifySessionCookie(sessionCookie, true);
            long recentLoginSeconds = appProperties.getAuth().getFirebase().getRecentLoginSeconds();
            boolean stale =
                    identity.authenticatedAt().isBefore(java.time.Instant.now().minusSeconds(recentLoginSeconds));
            if (stale || !identity.uid().equals(firebasePrincipal.firebaseUid())) {
                throw new FirebaseAuthenticationException("Recent Firebase sign-in required");
            }
        } catch (FirebaseAuthenticationException exception) {
            throw new RecentLoginRequiredException(String.format(
                    "User must have signed in with Firebase within the last %d minutes.",
                    appProperties.getAuth().getFirebase().getRecentLoginSeconds() / 60));
        }
        return joinPoint.proceed();
    }
}
