package com.almonium.subscription.repository;

import com.almonium.subscription.model.entity.PaddleEventLog;
import java.time.Instant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaddleEventLogRepository extends JpaRepository<PaddleEventLog, String> {
    void deleteByReceivedAtBefore(Instant receivedAt);
}
