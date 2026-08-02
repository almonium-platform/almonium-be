package com.almonium.subscription.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.subscription.model.entity.enums.Entitlement;
import com.almonium.user.core.model.entity.User;
import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/** An operator-issued access override. It never creates or changes a billing-provider subscription. */
@Getter
@Setter
@Entity
@Table(name = "access_grant")
@FieldDefaults(level = PRIVATE)
public class AccessGrant {

    @Id
    @UuidV7
    UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "granted_by_user_id", nullable = false)
    User grantedBy;

    @Enumerated(EnumType.STRING)
    Entitlement entitlement;

    String reason;
    Instant startsAt;
    Instant expiresAt;
    Instant revokedAt;
}
