package com.almonium.learning.almo.repository;

import com.almonium.learning.almo.dto.AlmoSpendLine;
import com.almonium.learning.almo.model.AlmoTurn;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AlmoTurnRepository extends JpaRepository<AlmoTurn, UUID> {
    Optional<AlmoTurn> findByUserMessageId(String userMessageId);

    long countByUserIdAndCreatedAtAfter(UUID userId, Instant after);

    @Query("""
            select new com.almonium.learning.almo.dto.AlmoSpendLine(
                t.model, count(t), coalesce(sum(t.promptTokens), 0L), coalesce(sum(t.completionTokens), 0L))
            from AlmoTurn t
            where t.createdAt >= :since and t.createdAt < :until
            group by t.model
            order by t.model
            """)
    List<AlmoSpendLine> spendBetween(Instant since, Instant until);
}
