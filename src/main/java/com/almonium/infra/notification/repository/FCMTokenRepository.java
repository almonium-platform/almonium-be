package com.almonium.infra.notification.repository;

import com.almonium.infra.notification.model.entity.FCMToken;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FCMTokenRepository extends JpaRepository<FCMToken, UUID> {

    List<FCMToken> findByUserId(UUID userId);

    Optional<FCMToken> findByToken(String token);
}
