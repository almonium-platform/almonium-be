package com.almonium.learning.book.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.almonium.infra.notification.event.NotificationReadEvent;
import com.almonium.infra.notification.model.enums.NotificationType;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TranslationOrderNotificationListenerTest {
    @Mock
    TranslationOrderService translationOrderService;

    @InjectMocks
    TranslationOrderNotificationListener listener;

    @Test
    void aReadFulfilmentRowMarksItsRequestSeen() {
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        listener.onNotificationRead(
                new NotificationReadEvent(userId, NotificationType.TRANSLATION_ORDER_COMPLETED, orderId));

        verify(translationOrderService).markSeenFromNotification(userId, orderId);
    }

    @Test
    void otherRowsAreNoneOfItsBusiness() {
        listener.onNotificationRead(
                new NotificationReadEvent(UUID.randomUUID(), NotificationType.FRIENDSHIP_ACCEPTED, UUID.randomUUID()));

        verify(translationOrderService, never()).markSeenFromNotification(any(), any());
    }
}
