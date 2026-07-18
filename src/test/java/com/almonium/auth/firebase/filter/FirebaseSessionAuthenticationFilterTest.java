package com.almonium.auth.firebase.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.auth.firebase.exception.FirebaseAuthenticationException;
import com.almonium.auth.firebase.gateway.FirebaseAuthGateway;
import com.almonium.auth.firebase.model.FirebaseIdentity;
import com.almonium.auth.firebase.service.FirebaseSessionCookieService;
import com.almonium.auth.firebase.service.FirebaseSessionService;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class FirebaseSessionAuthenticationFilterTest {
    @Mock
    FirebaseAuthGateway gateway;

    @Mock
    FirebaseSessionCookieService cookieService;

    @Mock
    FirebaseSessionService sessionService;

    @Mock
    UserRepository userRepository;

    @Mock
    FilterChain chain;

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void authenticatesMappedFirebaseUidWithoutRevocationNetworkCheck() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FirebaseIdentity identity = identity();
        User user =
                User.builder().id(UUID.randomUUID()).firebaseUid(identity.uid()).build();
        var authentication =
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken("principal", null);
        when(cookieService.read(request)).thenReturn(Optional.of("session-cookie"));
        when(gateway.verifySessionCookie("session-cookie", false)).thenReturn(identity);
        when(userRepository.findByFirebaseUid(identity.uid())).thenReturn(Optional.of(user));
        when(sessionService.authentication(identity, user)).thenReturn(authentication);

        filter().doFilter(request, response, chain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isSameAs(authentication);
        verify(chain).doFilter(request, response);
    }

    @Test
    void clearsInvalidSessionAndContinuesUnauthenticated() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie(FirebaseSessionCookieService.COOKIE_NAME, "invalid"));
        MockHttpServletResponse response = new MockHttpServletResponse();
        when(cookieService.read(request)).thenReturn(Optional.of("invalid"));
        when(gateway.verifySessionCookie("invalid", false)).thenThrow(new FirebaseAuthenticationException("invalid"));

        filter().doFilter(request, response, chain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(cookieService).clear(response);
        verify(chain).doFilter(request, response);
    }

    private FirebaseSessionAuthenticationFilter filter() {
        return new FirebaseSessionAuthenticationFilter(gateway, cookieService, sessionService, userRepository);
    }

    private FirebaseIdentity identity() {
        return new FirebaseIdentity("firebase-uid", "user@example.com", true, Instant.now(), "password");
    }
}
