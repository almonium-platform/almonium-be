package com.almonium.infra.notification.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.user.core.model.entity.User;
import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "fcm_token")
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class FCMToken {

    @Id
    @UuidV7
    UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    String token;

    String deviceType;

    boolean active;

    Instant lastUsedAt;

    public FCMToken(User user, String token, String deviceType) {
        this.user = user;
        this.token = token;
        this.deviceType = deviceType;
        this.active = true;
        this.lastUsedAt = Instant.now();
    }
}
