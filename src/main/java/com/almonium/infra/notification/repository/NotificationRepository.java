package com.almonium.infra.notification.repository;

import com.almonium.infra.notification.model.entity.Notification;
import com.almonium.user.core.model.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByCreatedAtDesc(User user);
}
