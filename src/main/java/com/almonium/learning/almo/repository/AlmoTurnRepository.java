package com.almonium.learning.almo.repository;

import com.almonium.learning.almo.model.AlmoTurn;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlmoTurnRepository extends JpaRepository<AlmoTurn, UUID> {
    Optional<AlmoTurn> findByUserMessageId(String userMessageId);

    long countByUserIdAndCreatedAtAfter(UUID userId, Instant after);
}
