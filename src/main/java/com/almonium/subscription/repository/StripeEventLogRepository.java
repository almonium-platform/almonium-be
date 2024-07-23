package com.almonium.subscription.repository;

import com.almonium.subscription.model.entity.StripeEventLog;
import java.sql.Timestamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StripeEventLogRepository extends JpaRepository<StripeEventLog, String> {
    void deleteByCreatedBefore(Timestamp created);
}
