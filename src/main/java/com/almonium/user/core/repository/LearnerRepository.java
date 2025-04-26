package com.almonium.user.core.repository;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.user.core.model.entity.Learner;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LearnerRepository extends JpaRepository<Learner, UUID> {
    void deleteAllByUserId(UUID userId);

    @Query("select l.language from Learner l where l.user.id = :userId")
    List<Language> findAllLanguagesByUserId(UUID userId);

    Optional<Learner> findByUserIdAndLanguage(UUID userId, Language language);

    @Query("select count(l) from Learner l where l.user.id = :userId")
    int countLearnersByUserId(UUID userId);

    @Query("select count(l) from Learner l where l.user.id = :userId and l.active = true")
    int countActiveLearnersByUserId(UUID userId);

    boolean existsByUserIdAndLanguage(UUID userId, Language code);
}
