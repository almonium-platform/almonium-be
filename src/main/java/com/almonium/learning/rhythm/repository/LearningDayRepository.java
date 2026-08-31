package com.almonium.learning.rhythm.repository;

import com.almonium.analyzer.translator.model.enums.Language;
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
     * The first day each language was ever learned on, which is where its weeks start counting. A learner three
     * weeks old must not read as having missed the nine weeks before it existed.
     */
    @Query("select d.language as language, min(d.day) as firstDay from LearningDay d "
            + "where d.user.id = :userId group by d.language")
    List<LanguageFirstDay> findFirstDays(@Param("userId") UUID userId);

    interface LanguageFirstDay {
        Language getLanguage();

        LocalDate getFirstDay();
    }

    /**
     * Adds activity to one language's day, creating it when absent. Concurrent heartbeats from several tabs are
     * common, so the accumulation happens in the database rather than in a read-modify-write.
     *
     * <p>The daily cap is spent across the whole account, not per language, so studying seven languages does not
     * buy seven days' worth of hours.
     */
    @Modifying
    @Query(
            nativeQuery = true,
            value =
                    """
                    INSERT INTO learning_day (id, user_id, activity_date, language, seconds_learned, met)
                    VALUES (:id, :userId, :day, :language,
                            LEAST(:seconds, GREATEST(:dailyCap - COALESCE(
                                (SELECT SUM(spent.seconds_learned) FROM learning_day spent
                                 WHERE spent.user_id = :userId AND spent.activity_date = :day), 0), 0))::int,
                            :met)
                    ON CONFLICT (user_id, activity_date, language) DO UPDATE
                    SET seconds_learned = learning_day.seconds_learned + EXCLUDED.seconds_learned,
                        met = learning_day.met OR EXCLUDED.met
                    """)
    void accumulate(
            @Param("id") UUID id,
            @Param("userId") UUID userId,
            @Param("day") LocalDate day,
            @Param("language") String language,
            @Param("seconds") int seconds,
            @Param("met") boolean met,
            @Param("dailyCap") int dailyCap);
}
