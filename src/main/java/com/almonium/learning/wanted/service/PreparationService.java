package com.almonium.learning.wanted.service;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.book.dto.response.ProcessorIngestStatus;
import com.almonium.learning.book.service.BookProcessorClient;
import com.almonium.learning.catalog.dto.response.TextSourceView;
import com.almonium.learning.catalog.model.entity.CatalogWork;
import com.almonium.learning.catalog.repository.CatalogWorkRepository;
import com.almonium.learning.catalog.service.TextSources;
import com.almonium.learning.rights.model.RightsStatus;
import com.almonium.learning.wanted.model.entity.Preparation;
import com.almonium.learning.wanted.repository.PreparationRepository;
import com.almonium.user.core.exception.ResourceConflictException;
import com.almonium.user.core.model.entity.User;
import jakarta.persistence.EntityNotFoundException;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Preparing a wanted work from a free text with one click in /ops: the book processor fetches the chosen source
 * (Gutenberg's EPUB, Wikisource's export) and ingests it, and its progress comes back here every few minutes for the
 * Wanted page's stages. A source under copyright where we make books cannot be chosen; an unknown one can, since
 * /ops is the human check, and the processor checks the rights again before it spends anything.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PreparationService {
    static final List<String> FINISHED = List.of("published", "failed");

    private final PreparationRepository preparationRepository;
    private final CatalogWorkRepository catalogWorkRepository;
    private final TextSources textSources;
    private final BookProcessorClient bookProcessorClient;
    private final Clock clock;

    public Preparation start(User operator, UUID catalogWorkId, Language language, UUID sourceId) {
        CatalogWork work = catalogWorkRepository
                .findById(catalogWorkId)
                .orElseThrow(() -> new EntityNotFoundException("Work not found"));
        TextSourceView source = textSources
                .find(sourceId)
                .filter(found -> textSources.belongsTo(found, catalogWorkId))
                .orElseThrow(() -> new EntityNotFoundException("That source is not one of this work's"));
        if (source.status() == RightsStatus.PROTECTED) {
            throw new ResourceConflictException("That text is under copyright where we make books.");
        }
        Language sourceLanguage = language(source.language())
                .orElseThrow(() -> new ResourceConflictException(
                        "The book processor does not read " + source.language() + " yet."));
        Preparation preparation = preparationRepository
                .findByCatalogWorkIdAndLanguage(catalogWorkId, language)
                .orElseGet(Preparation::new);
        if (preparation.getId() != null && !"failed".equals(preparation.getPhase())) {
            throw new ResourceConflictException("This one is already being prepared.");
        }
        preparation.setCatalogWork(work);
        preparation.setLanguage(language);
        preparation.setSourceKind(source.kind());
        preparation.setSourceReference(source.pageUrl());
        preparation.setSourceLanguage(source.language());
        preparation.setStartedBy(operator);
        preparation.setError(null);
        preparation = preparationRepository.saveAndFlush(preparation);
        ProcessorIngestStatus status = bookProcessorClient.startSourceIngest(
                preparation.getId(),
                source.downloadUrl(),
                source.kind().toLowerCase(Locale.ROOT),
                source.pageUrl(),
                work.getTitle(),
                work.getAuthorDisplay(),
                sourceLanguage,
                work.getPublicationYear());
        apply(preparation, status);
        return preparation;
    }

    /** The processor's latest word on every preparation still under way. */
    @Scheduled(fixedDelayString = "${app.books.preparation-check-interval:PT3M}", initialDelay = 60_000)
    public void refresh() {
        for (Preparation preparation : preparationRepository.findByProcessorEditionIdNotNullAndPhaseNotIn(FINISHED)) {
            try {
                apply(preparation, bookProcessorClient.sourceIngestStatus(preparation.getProcessorEditionId()));
            } catch (RuntimeException exception) {
                log.warn("Preparation {} not refreshed: {}", preparation.getId(), exception.getMessage());
            }
        }
    }

    @Transactional(readOnly = true)
    public Optional<Preparation> find(UUID catalogWorkId, Language language) {
        return preparationRepository.findByCatalogWorkIdAndLanguage(catalogWorkId, language);
    }

    @Transactional(readOnly = true)
    public List<Preparation> forWorks(List<UUID> catalogWorkIds) {
        return catalogWorkIds.isEmpty() ? List.of() : preparationRepository.findByCatalogWorkIdIn(catalogWorkIds);
    }

    private void apply(Preparation preparation, ProcessorIngestStatus status) {
        preparation.setProcessorEditionId(status.editionId());
        preparation.setPhase(status.phase());
        preparation.setStage(status.currentStage());
        preparation.setProgress(PreparationStages.done(status));
        preparation.setError(status.error() == null || status.error().isBlank() ? null : truncate(status.error()));
        preparation.setCheckedAt(Instant.now(clock));
        preparationRepository.save(preparation);
    }

    /** A source's language code as the product knows it; empty for one the product has no language for. */
    static Optional<Language> language(String code) {
        if (code == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(Language.valueOf(code.toUpperCase(Locale.ROOT)));
        } catch (IllegalArgumentException exception) {
            return Optional.empty();
        }
    }

    private static String truncate(String text) {
        return text.length() <= 2000 ? text : text.substring(0, 2000);
    }
}
