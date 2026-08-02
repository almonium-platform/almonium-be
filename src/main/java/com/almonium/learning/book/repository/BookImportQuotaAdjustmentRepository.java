package com.almonium.learning.book.repository;

import com.almonium.learning.book.model.entity.BookImportQuotaAdjustment;
import com.almonium.subscription.model.entity.enums.PlanFeature;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookImportQuotaAdjustmentRepository extends JpaRepository<BookImportQuotaAdjustment, UUID> {

    @Query(
            """
            select coalesce(sum(adjustment.adjustment), 0)
            from BookImportQuotaAdjustment adjustment
            where adjustment.user.id = :userId
              and adjustment.featureKey = :featureKey
              and adjustment.periodStartsAt = :periodStartsAt
            """)
    long totalForPeriod(UUID userId, PlanFeature featureKey, Instant periodStartsAt);
}
