package com.almonium.infra.messaging;

import java.time.Instant;

public interface DomainEvent {
    Instant occurredAt();
}
