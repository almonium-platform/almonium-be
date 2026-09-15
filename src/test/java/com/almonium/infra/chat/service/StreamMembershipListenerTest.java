package com.almonium.infra.chat.service;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import com.almonium.subscription.event.EntitlementChangedEvent;
import com.almonium.user.core.events.UserProfileUpdatedEvent;
import java.util.UUID;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = PRIVATE)
class StreamMembershipListenerTest {
    @Mock
    ApplicationEventPublisher eventPublisher;

    @Captor
    ArgumentCaptor<UserProfileUpdatedEvent> publishedEvent;

    @InjectMocks
    StreamMembershipListener listener;

    @Test
    @DisplayName("Should refresh the Stream profile of the user whose entitlement moved")
    void givenEntitlementChanged_whenHandled_thenProfileUpdateIsPublishedForThatUser() {
        UUID userId = UUID.randomUUID();

        listener.onEntitlementChanged(new EntitlementChangedEvent(userId));

        verify(eventPublisher).publishEvent(publishedEvent.capture());
        assertThat(publishedEvent.getValue().userId()).isEqualTo(userId);
    }
}
