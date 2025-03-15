package com.almonium.infra.notification.dto.response;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.infra.notification.model.enums.NotificationType;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class NotificationDto {
    UUID id;
    String title;
    NotificationType type;
    String message;
    String pictureUrl;
    UUID referenceId;
    UUID senderId;
    Instant createdAt;
    Instant readAt;
}
