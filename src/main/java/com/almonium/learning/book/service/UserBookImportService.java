package com.almonium.learning.book.service;

import static com.almonium.subscription.model.entity.enums.PlanFeature.MAX_BOOK_IMPORTS_PER_MONTH;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.infra.notification.service.NotificationService;
import com.almonium.learning.book.dto.request.BookImportEventRequest;
import com.almonium.learning.book.dto.response.BookImportDto;
import com.almonium.learning.book.dto.response.BookImportQuotaDto;
import com.almonium.learning.book.model.entity.BookImportQuotaAdjustment;
import com.almonium.learning.book.model.entity.UserBookImport;
import com.almonium.learning.book.model.enums.BookImportStatus;
import com.almonium.learning.book.repository.BookImportQuotaAdjustmentRepository;
import com.almonium.learning.book.repository.UserBookImportRepository;
import com.almonium.subscription.service.BillingPeriodService;
import com.almonium.subscription.service.BillingPeriodService.BillingPeriod;
import com.almonium.subscription.service.PlanSubscriptionService;
import com.almonium.subscription.service.PlanValidationService;
import com.almonium.user.core.model.entity.User;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class UserBookImportService {
    private static final List<String> SUPPORTED_EXTENSIONS = List.of(".epub", ".xml");

    private final UserBookImportRepository repository;
    private final BookImportQuotaAdjustmentRepository quotaAdjustmentRepository;
    private final PlanValidationService planValidationService;
    private final PlanSubscriptionService subscriptionService;
    private final BillingPeriodService billingPeriodService;
    private final BookProcessorClient processorClient;
    private final PublishedBookContentService contentService;
    private final NotificationService notificationService;

    public BookImportDto create(
            User user,
            MultipartFile source,
            String title,
            String author,
            String description,
            Language language,
            Integer publicationYear) {
        validateSource(source);
        BookImportQuotaDto quota = quota(user);
        planValidationService.validatePlanFeature(user, MAX_BOOK_IMPORTS_PER_MONTH, quota.used() + 1);

        UserBookImport bookImport = new UserBookImport();
        bookImport.setId(UUID.randomUUID());
        bookImport.setUser(user);
        bookImport.setTitle(title.trim());
        bookImport.setAuthor(author.trim());
        bookImport.setDescription(description == null ? "" : description.trim());
        bookImport.setLanguage(language);
        bookImport.setPublicationYear(publicationYear);
        bookImport.setStatus(BookImportStatus.QUEUED);
        bookImport.setError("");
        repository.save(bookImport);

        processorClient.createPrivateImport(
                bookImport.getId(),
                user.getId(),
                user.getUsername(),
                source,
                bookImport.getTitle(),
                bookImport.getAuthor(),
                bookImport.getDescription(),
                language,
                publicationYear);
        return toDto(bookImport);
    }

    @Transactional(readOnly = true)
    public List<BookImportDto> list(User user) {
        return repository.findByUserIdOrderByCreatedAtDesc(user.getId()).stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public BookImportQuotaDto quota(User user) {
        BillingPeriod period =
                billingPeriodService.currentPeriod(subscriptionService.getActiveSub(user), Instant.now());
        int limit = planValidationService.effectiveLimit(user, MAX_BOOK_IMPORTS_PER_MONTH);
        long imported = repository.countByUserIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
                user.getId(), period.startsAt(), period.endsAt());
        long adjustment =
                quotaAdjustmentRepository.totalForPeriod(user.getId(), MAX_BOOK_IMPORTS_PER_MONTH, period.startsAt());
        long used = Math.max(0, imported + adjustment);
        return new BookImportQuotaDto(limit, Math.toIntExact(used), period.startsAt(), period.endsAt());
    }

    /** Grants enough credit to make the current-period effective usage zero without deleting imports. */
    public void resetCurrentPeriodQuota(User user, User operator, String reason) {
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("An operator reason is required");
        }
        BookImportQuotaDto quota = quota(user);
        if (quota.used() == 0) {
            return;
        }
        BookImportQuotaAdjustment adjustment = new BookImportQuotaAdjustment();
        adjustment.setId(UUID.randomUUID());
        adjustment.setUser(user);
        adjustment.setPerformedBy(operator);
        adjustment.setFeatureKey(MAX_BOOK_IMPORTS_PER_MONTH);
        adjustment.setPeriodStartsAt(quota.periodStartsAt());
        adjustment.setAdjustment(-quota.used());
        adjustment.setReason(reason.trim());
        quotaAdjustmentRepository.save(adjustment);
    }

    @Transactional(readOnly = true)
    public BookImportDto get(User user, UUID id) {
        return toDto(findOwned(user.getId(), id));
    }

    @Transactional(readOnly = true)
    public byte[] text(User user, UUID id) {
        UserBookImport bookImport = findOwned(user.getId(), id);
        if (bookImport.getStatus() != BookImportStatus.READY) {
            throw new IllegalStateException("Private book is not ready");
        }
        return contentService.privateTextFor(id, user.getId());
    }

    public void applyEvent(BookImportEventRequest event) {
        UserBookImport bookImport = repository
                .findByIdAndUserId(event.importId(), event.ownerId())
                .orElseThrow(EntityNotFoundException::new);
        BookImportStatus previous = bookImport.getStatus();
        BookImportStatus next = BookImportStatus.valueOf(event.status().toUpperCase(Locale.ROOT));
        bookImport.setStatus(next);
        bookImport.setProgress(event.progress());
        bookImport.setWordCount(event.wordCount());
        bookImport.setError(event.error() == null ? "" : event.error());
        repository.save(bookImport);

        if (previous != next && (next == BookImportStatus.READY || next == BookImportStatus.FAILED)) {
            notificationService.notifyOfBookImport(
                    bookImport.getUser(), bookImport.getId(), bookImport.getTitle(), next == BookImportStatus.READY);
        }
    }

    private UserBookImport findOwned(UUID userId, UUID id) {
        return repository.findByIdAndUserId(id, userId).orElseThrow(EntityNotFoundException::new);
    }

    private void validateSource(MultipartFile source) {
        String filename = source.getOriginalFilename();
        String lower = filename == null ? "" : filename.toLowerCase(Locale.ROOT);
        if (source.isEmpty() || SUPPORTED_EXTENSIONS.stream().noneMatch(lower::endsWith)) {
            throw new IllegalArgumentException("Only EPUB and TEI XML files are supported");
        }
    }

    private BookImportDto toDto(UserBookImport bookImport) {
        return new BookImportDto(
                bookImport.getId(),
                bookImport.getTitle(),
                bookImport.getAuthor(),
                bookImport.getDescription(),
                bookImport.getLanguage(),
                bookImport.getPublicationYear(),
                bookImport.getStatus(),
                bookImport.getProgress(),
                bookImport.getWordCount(),
                bookImport.getError(),
                bookImport.getCreatedAt(),
                bookImport.getUpdatedAt());
    }
}
