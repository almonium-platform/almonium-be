package com.almonium.learning.book.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.infra.notification.event.NotificationReadEvent;
import com.almonium.infra.notification.model.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** Reading the fulfilment row in the bell clears the fulfilment card on the Read page. */
@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class TranslationOrderNotificationListener {
    TranslationOrderService translationOrderService;

    @EventListener
    @Transactional
    public void onNotificationRead(NotificationReadEvent event) {
        if (event.type() != NotificationType.TRANSLATION_ORDER_COMPLETED || event.referenceId() == null) {
            return;
        }
        translationOrderService.markSeenFromNotification(event.userId(), event.referenceId());
    }
}
