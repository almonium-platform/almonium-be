package com.almonium.subscription.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class StripeEventLog {

    @Id
    private String eventId;

    private String eventType;
    private Timestamp created;
}
