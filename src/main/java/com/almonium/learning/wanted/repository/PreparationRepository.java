package com.almonium.learning.wanted.repository;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.wanted.model.entity.Preparation;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreparationRepository extends JpaRepository<Preparation, UUID> {
    Optional<Preparation> findByCatalogWorkIdAndLanguage(UUID catalogWorkId, Language language);

    List<Preparation> findByCatalogWorkIdIn(Collection<UUID> catalogWorkIds);

    List<Preparation> findByReporterAndProcessorEditionIdNotNullAndPhaseNotIn(
            String reporter, Collection<String> phases);
}
