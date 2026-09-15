package com.almonium.auth.firebase.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.auth.firebase.dto.EmailAccountResponse;
import com.almonium.auth.firebase.dto.EmailAddressRequest;
import com.almonium.auth.firebase.service.AccountLookupRateLimiter;
import com.almonium.auth.firebase.service.FirebaseAccountLookupService;
import com.almonium.auth.firebase.service.FirebaseAuthEmailService;
import com.almonium.auth.firebase.service.PendingEmailChangeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

@ExtendWith(MockitoExtension.class)
class PublicFirebaseAuthEmailControllerTest {
    @Mock
    FirebaseAuthEmailService firebaseAuthEmailService;

    @Mock
    PendingEmailChangeService pendingEmailChangeService;

    @Mock
    FirebaseAccountLookupService firebaseAccountLookupService;

    @Mock
    AccountLookupRateLimiter accountLookupRateLimiter;

    @InjectMocks
    PublicFirebaseAuthEmailController controller;

    @Test
    void returnsUncacheableAccountStatusWithinRateLimit() {
        MockHttpServletRequest httpRequest = new MockHttpServletRequest();
        httpRequest.setRemoteAddr("192.0.2.1");
        when(accountLookupRateLimiter.tryAcquire("192.0.2.1")).thenReturn(true);
        when(firebaseAccountLookupService.accountExists("learner@example.com")).thenReturn(true);

        ResponseEntity<EmailAccountResponse> response =
                controller.lookupEmailAccount(new EmailAddressRequest("learner@example.com"), httpRequest);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getHeaders().getCacheControl()).isEqualTo("no-store");
        assertThat(response.getBody()).isEqualTo(new EmailAccountResponse(true));
    }

    @Test
    void rejectsLookupWhenClientExceedsRateLimit() {
        MockHttpServletRequest httpRequest = new MockHttpServletRequest();
        httpRequest.setRemoteAddr("192.0.2.1");
        when(accountLookupRateLimiter.tryAcquire("192.0.2.1")).thenReturn(false);

        ResponseEntity<EmailAccountResponse> response =
                controller.lookupEmailAccount(new EmailAddressRequest("learner@example.com"), httpRequest);

        assertThat(response.getStatusCode().value()).isEqualTo(429);
        assertThat(response.getHeaders().getFirst(HttpHeaders.RETRY_AFTER)).isEqualTo("60");
        verify(firebaseAccountLookupService, never()).accountExists("learner@example.com");
    }
}
