package com.almonium.learning.book.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.client.exception.ApiIntegrationException;
import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.book.dto.request.ApproveTranslationRequest;
import com.almonium.learning.book.dto.response.ProcessorTranslationEstimate;
import com.almonium.learning.book.dto.response.ProcessorTranslationStatus;
import com.almonium.learning.book.dto.response.TranslationJobDto;
import com.almonium.learning.book.dto.response.TranslationQueueDto;
import com.almonium.learning.book.dto.response.TranslationQueueRow;
import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.model.entity.TranslationJob;
import com.almonium.learning.book.model.entity.TranslationOrder;
import com.almonium.learning.book.model.enums.TranslationJobPhase;
import com.almonium.learning.book.model.enums.TranslationOrderStatus;
import com.almonium.learning.book.repository.TranslationJobRepository;
import com.almonium.learning.book.repository.TranslationOrderRepository;
import com.almonium.subscription.model.entity.enums.Entitlement;
import com.almonium.subscription.service.EffectiveAccessService;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.exception.ResourceConflictException;
import com.almonium.user.core.model.entity.User;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The /ops side of translation requests. Operators decide and watch; the processor does the work. Approving writes
 * the estimate to the ledger and posts one job; everything after that is the processor's phase, mirrored read-only,
 * until the publication webhook settles the job and the requests together.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class TranslationJobService {
    private static final int FINISHED_WINDOW_DAYS = 30;

    BookService bookService;
    BookProcessorClient processorClient;
    EffectiveAccessService effectiveAccessService;
    TranslationJobRepository jobRepository;
    TranslationOrderRepository orderRepository;

    @NonFinal
    @Value("${app.books.translation-budget-usd}")
    BigDecimal monthBudgetUsd;

    @Transactional
    public TranslationQueueDto queue() {
        List<String> warnings = new ArrayList<>();
        Instant now = Instant.now();
        Instant monthStart = monthStart(now);

        List<TranslationJob> liveJobs = jobRepository.findByPhaseIn(TranslationJobPhase.LIVE);
        liveJobs.forEach(job -> sync(job, warnings));

        Map<PairKey, List<TranslationOrder>> asks = orderRepository.findByStatus(TranslationOrderStatus.ASKED).stream()
                .collect(Collectors.groupingBy(
                        order -> new PairKey(order.getBook().getId(), order.getLanguage()),
                        LinkedHashMap::new,
                        Collectors.toList()));
        Map<UUID, Entitlement> entitlements = effectiveAccessService.entitlementsFor(asks.values().stream()
                .flatMap(List::stream)
                .map(order -> order.getUser().getId())
                .collect(Collectors.toSet()));

        Map<PairKey, TranslationJob> jobsByPair = new LinkedHashMap<>();
        liveJobs.forEach(job -> jobsByPair.put(PairKey.of(job), job));
        jobRepository.findByFinishedAtGreaterThanEqual(now.minus(FINISHED_WINDOW_DAYS, ChronoUnit.DAYS)).stream()
                .filter(job -> !job.getPhase().isLive())
                .sorted(Comparator.comparing(TranslationJob::getFinishedAt).reversed())
                .forEach(job -> jobsByPair.putIfAbsent(PairKey.of(job), job));

        List<TranslationQueueRow> rows = new ArrayList<>();
        Set<PairKey> keys = new java.util.LinkedHashSet<>(asks.keySet());
        keys.addAll(jobsByPair.keySet());
        for (PairKey key : keys) {
            List<TranslationOrder> orders = asks.getOrDefault(key, List.of());
            TranslationJob job = jobsByPair.get(key);
            Book book = job != null ? job.getBook() : orders.getFirst().getBook();
            long premium = orders.stream()
                    .filter(order ->
                            entitlements.getOrDefault(order.getUser().getId(), Entitlement.FREE) != Entitlement.FREE)
                    .count();
            BigDecimal estimate;
            if (job != null && job.getPhase().isLive()) {
                estimate = job.getEstimatedCostUsd();
            } else if (!orders.isEmpty()) {
                estimate = estimateQuietly(book, key.language(), warnings);
            } else {
                estimate = job.getEstimatedCostUsd();
            }
            rows.add(new TranslationQueueRow(
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getEditionSlug(),
                    book.getLanguage(),
                    key.language(),
                    orders.size(),
                    Math.toIntExact(premium),
                    orders.size() - Math.toIntExact(premium),
                    estimate,
                    job == null ? null : toDto(job)));
        }
        rows.sort(Comparator.comparingInt(this::rowRank)
                .thenComparing(
                        Comparator.comparingInt(TranslationQueueRow::asks).reversed()));

        BigDecimal spend = monthSpend(monthStart);
        int open = (int) rows.stream()
                .filter(row -> row.asks() > 0
                        && (row.job() == null || !row.job().phase().isLive()))
                .count();
        return new TranslationQueueDto(
                open,
                liveJobs.size(),
                Math.toIntExact(jobRepository.countByPhase(TranslationJobPhase.PUBLISHED)),
                Math.toIntExact(orderRepository.countByStatus(TranslationOrderStatus.DECLINED)),
                spend,
                monthBudgetUsd,
                spend.compareTo(monthBudgetUsd) >= 0,
                monthStart,
                rows,
                warnings);
    }

    /** Running rows first, then open ones, then what finished lately. */
    private int rowRank(TranslationQueueRow row) {
        if (row.job() != null && row.job().phase().isLive()) return 0;
        if (row.asks() > 0) return 1;
        return 2;
    }

    @Transactional
    public TranslationJobDto approve(User operator, UUID bookId, Language language, ApproveTranslationRequest request) {
        Book book = bookService.getBookById(bookId);
        if (book.getOriginalBook() != null) {
            throw new BadUserRequestActionException("Approve the original edition, not a translation of it");
        }
        if (book.getLanguage() == language) {
            throw new BadUserRequestActionException("%s is the book's own language".formatted(language));
        }
        if (bookService.getAvailableLanguagesForBook(book.getId()).contains(language)) {
            throw new BadUserRequestActionException("%s is already available for this book".formatted(language));
        }
        if (!jobRepository
                .findByBookIdAndLanguageAndPhaseIn(book.getId(), language, TranslationJobPhase.LIVE)
                .isEmpty()) {
            throw new ResourceConflictException("A translation of this pair is already running");
        }

        String tier = request == null ? "quality" : request.tierOrDefault();
        String mode = request == null ? "batch" : request.modeOrDefault();
        ProcessorTranslationEstimate estimate =
                processorClient.estimateTranslation(book.getEditionSlug(), language, tier, mode);

        Instant now = Instant.now();
        BigDecimal spend = monthSpend(monthStart(now));
        if (spend.compareTo(monthBudgetUsd) >= 0) {
            throw new ResourceConflictException("This month's translation budget is spent; the queue keeps collecting");
        }
        if (spend.add(estimate.estimatedCostUsd()).compareTo(monthBudgetUsd) > 0) {
            throw new ResourceConflictException("This translation would take the month past its budget");
        }

        TranslationJob job = new TranslationJob();
        job.setBook(book);
        job.setLanguage(language);
        job.setPhase(TranslationJobPhase.QUEUED);
        job.setEstimatedCostUsd(estimate.estimatedCostUsd());
        job.setTier(tier);
        job.setMode(mode);
        job.setApprovedBy(operator);
        job.setApprovedAt(now);
        job.setError("");
        job = jobRepository.save(job);

        try {
            ProcessorTranslationStatus status =
                    processorClient.startTranslation(job.getId(), book.getEditionSlug(), language, tier, mode);
            apply(job, status, now);
        } catch (ApiIntegrationException exception) {
            // The estimate stays on the ledger only for jobs that exist; a job the processor never took is not spend.
            job.setPhase(TranslationJobPhase.FAILED);
            job.setError("The book processor did not accept the job");
            job.setFinishedAt(now);
            jobRepository.save(job);
            throw exception;
        }
        return toDto(jobRepository.save(job));
    }

    /** The requests stay open: cancelling a job puts the pair back in the queue, it does not answer anyone. */
    @Transactional
    public TranslationJobDto cancel(UUID jobId) {
        TranslationJob job = jobRepository
                .findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Translation job not found"));
        if (!job.getPhase().isLive()) {
            throw new ResourceConflictException("Only a running job can be cancelled");
        }
        if (job.getProcessorEditionId() != null) {
            try {
                ProcessorTranslationStatus status = processorClient.cancelTranslation(job.getProcessorEditionId());
                job.setActualCostUsd(status.estimatedCostUsd());
            } catch (ApiIntegrationException exception) {
                log.warn("Processor could not cancel translation job {}", job.getId(), exception);
            }
        }
        job.setPhase(TranslationJobPhase.CANCELLED);
        job.setFinishedAt(Instant.now());
        return toDto(jobRepository.save(job));
    }

    /** The publication webhook: the promise is kept, so the job is done whatever phase we last saw. */
    @Transactional
    public void settlePublished(Book translation) {
        if (translation.getOriginalBook() == null) {
            return;
        }
        List<TranslationJob> jobs = jobRepository.findByBookIdAndLanguageAndPhaseIn(
                translation.getOriginalBook().getId(), translation.getLanguage(), TranslationJobPhase.LIVE);
        Instant now = Instant.now();
        for (TranslationJob job : jobs) {
            job.setPhase(TranslationJobPhase.PUBLISHED);
            job.setPublishedBook(translation);
            job.setFinishedAt(now);
            if (job.getProcessorEditionId() != null) {
                try {
                    job.setActualCostUsd(processorClient
                            .translationStatus(job.getProcessorEditionId())
                            .estimatedCostUsd());
                } catch (ApiIntegrationException exception) {
                    log.warn("Could not read the final cost of translation job {}", job.getId(), exception);
                }
            }
        }
        jobRepository.saveAll(jobs);
    }

    private void sync(TranslationJob job, List<String> warnings) {
        if (job.getProcessorEditionId() == null) {
            return;
        }
        try {
            apply(job, processorClient.translationStatus(job.getProcessorEditionId()), Instant.now());
            jobRepository.save(job);
        } catch (ApiIntegrationException exception) {
            warnings.add("The book processor could not be reached for %s → %s; showing what it last said"
                    .formatted(job.getBook().getTitle(), job.getLanguage()));
        }
    }

    private void apply(TranslationJob job, ProcessorTranslationStatus status, Instant now) {
        if (status.editionId() != null) job.setProcessorEditionId(status.editionId());
        if (!status.editionSlug().isBlank()) job.setProcessorEditionSlug(status.editionSlug());
        TranslationJobPhase phase = TranslationJobPhase.fromProcessor(status.phase());
        job.setPhase(phase);
        job.setProgressCompleted(status.progressCompleted());
        job.setProgressTotal(status.progressTotal());
        job.setActualCostUsd(status.estimatedCostUsd());
        job.setError(status.error() == null ? "" : status.error());
        job.setLastSyncedAt(now);
        if (phase == TranslationJobPhase.PUBLISHED || phase == TranslationJobPhase.FAILED) {
            job.setFinishedAt(status.publishedAt() != null ? status.publishedAt() : now);
        }
        if (phase == TranslationJobPhase.PUBLISHED
                && job.getPublishedBook() == null
                && status.publishedBookId() != null) {
            try {
                job.setPublishedBook(bookService.getBookById(status.publishedBookId()));
            } catch (EntityNotFoundException unknown) {
                log.warn("Processor reports unknown published book {}", status.publishedBookId());
            }
        }
    }

    private BigDecimal estimateQuietly(Book book, Language language, List<String> warnings) {
        try {
            return processorClient
                    .estimateTranslation(book.getEditionSlug(), language, "quality", "batch")
                    .estimatedCostUsd();
        } catch (ApiIntegrationException exception) {
            warnings.add("No estimate for %s → %s: the book processor could not be reached"
                    .formatted(book.getTitle(), language));
            return null;
        }
    }

    private BigDecimal monthSpend(Instant monthStart) {
        return jobRepository
                .findByApprovedAtGreaterThanEqualAndPhaseNot(monthStart, TranslationJobPhase.CANCELLED)
                .stream()
                .map(TranslationJob::getEstimatedCostUsd)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    static Instant monthStart(Instant now) {
        return LocalDate.ofInstant(now, ZoneOffset.UTC)
                .withDayOfMonth(1)
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant();
    }

    static TranslationJobDto toDto(TranslationJob job) {
        Book published = job.getPublishedBook();
        return new TranslationJobDto(
                job.getId(),
                job.getPhase(),
                job.getProgressCompleted(),
                job.getProgressTotal(),
                job.getEstimatedCostUsd(),
                job.getActualCostUsd(),
                job.getTier(),
                job.getMode(),
                job.getProcessorEditionSlug(),
                job.getApprovedAt(),
                job.getFinishedAt(),
                job.getError(),
                published == null ? null : published.getId(),
                published == null ? null : published.getEditionSlug());
    }

    private record PairKey(UUID bookId, Language language) {
        static PairKey of(TranslationJob job) {
            return new PairKey(job.getBook().getId(), job.getLanguage());
        }
    }
}
