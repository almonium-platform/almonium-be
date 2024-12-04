package com.almonium.subscription.repository;

import com.almonium.subscription.model.entity.StripeEventLog;
import java.time.Instant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StripeEventLogRepository extends JpaRepository<StripeEventLog, String> {
    void deleteByCreatedAtBefore(Instant created);
}
