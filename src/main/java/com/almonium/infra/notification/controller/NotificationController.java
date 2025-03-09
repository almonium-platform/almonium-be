package com.almonium.infra.notification.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.infra.notification.dto.response.NotificationDto;
import com.almonium.infra.notification.service.NotificationService;
import com.almonium.user.core.model.entity.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Infra")
@Validated
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class NotificationController {
    NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getNotifications(@Auth User user) {
        return ResponseEntity.ok(notificationService.getNotificationsForUser(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@Auth User user, @NotNull @PathVariable UUID id) {
        notificationService.deleteNotification(user, id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/read")
    public ResponseEntity<Void> readAllNotifications(@Auth User user) {
        notificationService.readAllNotifications(user);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> readNotification(@Auth User user, @NotNull @PathVariable UUID id) {
        notificationService.readNotification(user, id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/unread")
    public ResponseEntity<Void> unreadNotification(@Auth User user, @NotNull @PathVariable UUID id) {
        notificationService.unreadNotification(user, id);
        return ResponseEntity.ok().build();
    }
}
