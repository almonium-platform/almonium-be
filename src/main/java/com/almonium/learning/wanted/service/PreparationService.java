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
import java.time.Duration;
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
    /** Progress is polled from this deployment's processor. */
    public static final String REPORTER_PROCESSOR = "processor";
    /** Progress is pushed by the factory on the host that makes the books; nothing is polled. */
    public static final String REPORTER_FACTORY = "factory";
    /** A reader is waiting: this deployment's processor is asked whenever the reader asks, and polled besides. */
    public static final String REPORTER_LIVE = "live";

    /** How fresh a waiting reader's answer must be before the processor is asked again. */
    static final Duration LIVE_FRESH = Duration.ofSeconds(1);

    private final PreparationRepository preparationRepository;
    private final CatalogWorkRepository catalogWorkRepository;
    private final TextSources textSources;
    private final BookProcessorClient bookProcessorClient;
    private final Clock clock;

    public Preparation start(User operator, UUID catalogWorkId, Language language, UUID sourceId) {
        Preparation preparation = prepare(operator, REPORTER_PROCESSOR, catalogWorkId, language, sourceId);
        TextSourceView source = textSources.find(sourceId).orElseThrow();
        CatalogWork work = preparation.getCatalogWork();
        ProcessorIngestStatus status = bookProcessorClient.startSourceIngest(
                preparation.getId(),
                source.downloadUrl(),
                source.kind().toLowerCase(Locale.ROOT),
                source.pageUrl(),
                work.getTitle(),
                work.getAuthorDisplay(),
                language(source.language()).orElseThrow(),
                work.getPublicationYear());
        apply(preparation, status);
        return preparation;
    }

    /**
     * A reader opens a work that is not on the shelf: the processor fetches {@code source} with the reader waiting,
     * falls back on {@code fallbacks} in order when its library cannot be had, and shelves the text as it arrives.
     * The reader is the one who started it; the moment counts as the preparation's start, also when it follows a
     * failed one.
     */
    public Preparation startLive(
            User reader, UUID catalogWorkId, TextSourceView source, List<TextSourceView> fallbacks) {
        Language language = language(source.language()).orElseThrow();
        Preparation preparation = prepare(reader, REPORTER_LIVE, catalogWorkId, language, source.id());
        preparation.setCreatedAt(Instant.now(clock));
        preparation.setProcessorEditionId(null);
        preparation.setEditionSlug(null);
        CatalogWork work = preparation.getCatalogWork();
        ProcessorIngestStatus status = bookProcessorClient.startSourceIngest(
                // a new id for every try: the processor keeps a failed edition under the old one
                UUID.randomUUID(),
                sourceText(source),
                work.getTitle(),
                work.getAuthorDisplay(),
                language,
                work.getPublicationYear(),
                true,
                fallbacks.stream().map(PreparationService::sourceText).toList());
        apply(preparation, status);
        return preparation;
    }

    private static BookProcessorClient.SourceText sourceText(TextSourceView source) {
        return new BookProcessorClient.SourceText(
                source.downloadUrl(), source.kind().toLowerCase(Locale.ROOT), source.pageUrl());
    }

    /**
     * The latest word on a preparation a reader is watching: the processor is asked unless it was asked within the
     * second or the book is finished. A processor that does not answer leaves the last word standing.
     */
    public Optional<Preparation> watch(UUID preparationId) {
        Optional<Preparation> found = preparationRepository.findById(preparationId);
        found.filter(preparation -> REPORTER_LIVE.equals(preparation.getReporter()))
                .filter(preparation -> preparation.getProcessorEditionId() != null)
                .filter(preparation -> !FINISHED.contains(preparation.getPhase()))
                .filter(preparation -> preparation.getCheckedAt() == null
                        || preparation
                                .getCheckedAt()
                                .isBefore(Instant.now(clock).minus(LIVE_FRESH)))
                .ifPresent(preparation -> {
                    try {
                        apply(preparation, bookProcessorClient.sourceIngestStatus(preparation.getProcessorEditionId()));
                    } catch (RuntimeException exception) {
                        log.warn("Live preparation {} not refreshed: {}", preparation.getId(), exception.getMessage());
                    }
                });
        return found;
    }

    /** How many books readers are waiting for right now: started live within {@code window} and not finished. */
    @Transactional(readOnly = true)
    public long arriving(Duration window) {
        return preparationRepository.countByReporterAndPhaseNotInAndCreatedAtAfter(
                REPORTER_LIVE, FINISHED, Instant.now(clock).minus(window));
    }

    /** Whether this reader has a book of their own arriving: started live within {@code window} and not finished. */
    @Transactional(readOnly = true)
    public boolean arrivingFor(User reader, Duration window) {
        return preparationRepository.existsByStartedByIdAndReporterAndPhaseNotInAndCreatedAtAfter(
                reader.getId(), REPORTER_LIVE, FINISHED, Instant.now(clock).minus(window));
    }

    /**
     * The factory's reservation: the same checks as a click in /ops, but the host that makes the books fetches the
     * text itself and pushes the progress here ({@link #report}); this deployment's processor is not asked.
     */
    public Preparation reserve(UUID catalogWorkId, Language language, UUID sourceId) {
        Preparation preparation = prepare(null, REPORTER_FACTORY, catalogWorkId, language, sourceId);
        preparation.setPhase("queued");
        preparation.setProgress(0);
        preparation.setCheckedAt(Instant.now(clock));
        return preparationRepository.save(preparation);
    }

    /** The factory's word on one of its preparations. */
    public Preparation report(UUID preparationId, ProcessorIngestStatus status) {
        Preparation preparation = preparationRepository
                .findById(preparationId)
                .filter(found -> REPORTER_FACTORY.equals(found.getReporter()))
                .orElseThrow(() -> new EntityNotFoundException("No preparation of the factory's has this id"));
        apply(preparation, status);
        return preparation;
    }

    private Preparation prepare(User operator, String reporter, UUID catalogWorkId, Language language, UUID sourceId) {
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
        if (language(source.language()).isEmpty()) {
            throw new ResourceConflictException("The book processor does not read " + source.language() + " yet.");
        }
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
        preparation.setReporter(reporter);
        preparation.setError(null);
        return preparationRepository.saveAndFlush(preparation);
    }

    /** The processor's latest word on every preparation it was asked for and that is still under way. */
    @Scheduled(fixedDelayString = "${app.books.preparation-check-interval:PT3M}", initialDelay = 60_000)
    public void refresh() {
        for (Preparation preparation : preparationRepository.findByReporterInAndProcessorEditionIdNotNullAndPhaseNotIn(
                List.of(REPORTER_PROCESSOR, REPORTER_LIVE), FINISHED)) {
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
        if (status.slug() != null && !status.slug().isBlank()) {
            preparation.setEditionSlug(status.slug());
        }
        if (status.sourceKind() != null && status.sourcePageUrl() != null) {
            // after a fallback the edition is made from another text than the one asked for
            preparation.setSourceKind(status.sourceKind().toUpperCase(Locale.ROOT));
            preparation.setSourceReference(status.sourcePageUrl());
        }
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
