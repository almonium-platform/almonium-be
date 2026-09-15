package com.almonium.subscription.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.user.core.model.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/** A permanent, finite allocation record for the founding-member offer. */
@Getter
@Setter
@Entity
@Table(name = "founding_member")
@FieldDefaults(level = PRIVATE)
public class FoundingMember {
    @Id
    Integer slotNumber;

    @ManyToOne
    @JoinColumn(name = "user_id", unique = true)
    User user;

    String paddleTransactionId;
    String paddleSubscriptionId;

    @Enumerated(EnumType.STRING)
    Status status;

    Instant reservedAt;
    Instant confirmedAt;

    public enum Status {
        AVAILABLE,
        RESERVED,
        CONFIRMED
    }
}
