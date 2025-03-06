package com.almonium.infra.notification.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.infra.notification.model.enums.NotificationType;
import com.almonium.user.core.model.entity.User;
import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class Notification {

    @Id
    @UuidV7
    UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    String title;

    @Enumerated(EnumType.STRING)
    NotificationType type;

    String message;

    String pictureUrl;

    UUID referenceId;

    Instant readAt;

    @CreatedDate
    Instant createdAt;
}
