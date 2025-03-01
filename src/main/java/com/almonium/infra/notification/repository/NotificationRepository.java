package com.almonium.infra.notification.repository;

import com.almonium.infra.notification.model.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {}
