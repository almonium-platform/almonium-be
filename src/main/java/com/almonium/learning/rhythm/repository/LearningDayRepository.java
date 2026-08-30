package com.almonium.learning.rhythm.repository;

import com.almonium.learning.rhythm.model.LearningDay;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LearningDayRepository extends JpaRepository<LearningDay, UUID> {

    List<LearningDay> findAllByUserIdAndDayBetweenOrderByDayAsc(UUID userId, LocalDate from, LocalDate to);

    /**
     * Adds activity to the learner's day, creating it when absent. Concurrent heartbeats from several tabs are
     * common, so the accumulation happens in the database rather than in a read-modify-write.
     */
    @Modifying
    @Query(
            nativeQuery = true,
            value =
                    """
                    INSERT INTO learning_day (id, user_id, activity_date, seconds_learned, met)
                    VALUES (:id, :userId, :day, LEAST(:seconds, :dailyCap), :met)
                    ON CONFLICT (user_id, activity_date) DO UPDATE
                    SET seconds_learned = LEAST(learning_day.seconds_learned + EXCLUDED.seconds_learned, :dailyCap),
                        met = learning_day.met OR EXCLUDED.met
                    """)
    void accumulate(
            @Param("id") UUID id,
            @Param("userId") UUID userId,
            @Param("day") LocalDate day,
            @Param("seconds") int seconds,
            @Param("met") boolean met,
            @Param("dailyCap") int dailyCap);
}
