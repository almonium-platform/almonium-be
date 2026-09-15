package com.almonium.subscription.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaddleEventLog {
    @Id
    String eventId;

    String eventType;
    Instant occurredAt;
    Instant receivedAt;
}
