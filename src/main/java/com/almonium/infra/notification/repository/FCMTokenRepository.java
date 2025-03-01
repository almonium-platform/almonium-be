package com.almonium.infra.notification.repository;

import com.almonium.infra.notification.model.entity.FCMToken;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FCMTokenRepository extends JpaRepository<FCMToken, Long> {

    List<FCMToken> findByUserId(Long userId);

    Optional<FCMToken> findByToken(String token);
}
