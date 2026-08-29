package com.almonium.auth.firebase.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.auth.firebase.gateway.FirebaseAuthGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FirebaseAccountLookupServiceTest {
    @Mock
    FirebaseAuthGateway firebaseAuthGateway;

    @InjectMocks
    FirebaseAccountLookupService service;

    @Test
    void normalizesEmailBeforeLookup() {
        when(firebaseAuthGateway.accountExistsByEmail("learner@example.com")).thenReturn(true);

        assertThat(service.accountExists("  Learner@Example.com ")).isTrue();

        verify(firebaseAuthGateway).accountExistsByEmail("learner@example.com");
    }
}
