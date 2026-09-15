package com.almonium.infra.notification.event;

import com.almonium.infra.notification.model.enums.NotificationType;
import java.util.UUID;

/**
 * A bell row was read. The thing it referred to may want to know: the fulfilment card on the Read page clears
 * when its row is read, and the reverse.
 */
public record NotificationReadEvent(UUID userId, NotificationType type, UUID referenceId) {}
