package com.almonium.user.core.repository;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.user.core.model.entity.Learner;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LearnerRepository extends JpaRepository<Learner, Long> {
    Optional<Learner> findByUserIdAndLanguage(long userId, Language language);

    @Query("select count(l) from Learner l where l.user.id = :userId")
    int countLearnersByUserId(long userId);
}
