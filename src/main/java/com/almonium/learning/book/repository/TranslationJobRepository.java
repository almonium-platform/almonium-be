package com.almonium.learning.book.repository;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.book.model.entity.TranslationJob;
import com.almonium.learning.book.model.enums.TranslationJobPhase;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TranslationJobRepository extends JpaRepository<TranslationJob, UUID> {
    List<TranslationJob> findByBookIdAndLanguageAndPhaseIn(
            UUID bookId, Language language, Collection<TranslationJobPhase> phases);

    List<TranslationJob> findByPhaseIn(Collection<TranslationJobPhase> phases);

    List<TranslationJob> findByFinishedAtGreaterThanEqual(Instant since);

    List<TranslationJob> findByApprovedAtGreaterThanEqualAndPhaseNot(Instant since, TranslationJobPhase phase);

    long countByPhase(TranslationJobPhase phase);
}
