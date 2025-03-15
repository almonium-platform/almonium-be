package com.almonium.infra.notification.repository;

import com.almonium.infra.notification.model.entity.Notification;
import com.almonium.user.core.model.entity.User;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByRecipientOrderByReadAtDescCreatedAtDesc(User user);

    void deleteByIdAndRecipient(UUID id, User user);

    @Modifying
    @Query("update Notification n set n.readAt = current_timestamp where n.recipient = :user and n.readAt is null")
    void readAllUnreadNotifications(User user);

    @Modifying
    @Query(
            "update Notification n set n.readAt = current_timestamp where n.recipient = :user and n.id = :id and n.readAt is null")
    void readNotification(User user, UUID id);

    @Modifying
    @Query(
            "update Notification n set n.readAt = null where n.recipient = :user and n.id = :id and n.readAt is not null")
    void unreadNotification(User user, UUID id);

    @Modifying
    @Query("delete from Notification n where n.readAt is not null and n.readAt < :cutoff")
    void deleteOldReadNotifications(Instant cutoff);
}
