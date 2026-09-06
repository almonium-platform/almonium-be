package com.almonium.learning.book.service;

import static com.almonium.subscription.model.entity.enums.PlanFeature.MAX_BOOK_IMPORTS_PER_MONTH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.infra.notification.service.NotificationService;
import com.almonium.learning.book.dto.request.BookImportEventRequest;
import com.almonium.learning.book.dto.request.BookImportMetadataRequest;
import com.almonium.learning.book.dto.response.BookImportDto;
import com.almonium.learning.book.dto.response.BookImportQuotaDto;
import com.almonium.learning.book.model.entity.UserBookImport;
import com.almonium.learning.book.model.enums.BookImportMetadataStatus;
import com.almonium.learning.book.model.enums.BookImportStatus;
import com.almonium.learning.book.repository.BookImportQuotaAdjustmentRepository;
import com.almonium.learning.book.repository.UserBookImportRepository;
import com.almonium.subscription.model.entity.Plan;
import com.almonium.subscription.model.entity.PlanLimit;
import com.almonium.subscription.model.entity.PlanSubscription;
import com.almonium.subscription.service.BillingPeriodService;
import com.almonium.subscription.service.PlanSubscriptionService;
import com.almonium.subscription.service.PlanValidationService;
import com.almonium.user.core.model.entity.User;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class UserBookImportServiceTest {
    @Mock
    UserBookImportRepository repository;

    @Mock
    BookImportQuotaAdjustmentRepository quotaAdjustmentRepository;

    @Mock
    PlanValidationService planValidationService;

    @Mock
    PlanSubscriptionService subscriptionService;

    @Spy
    BillingPeriodService billingPeriodService = new BillingPeriodService();

    @Mock
    BookProcessorClient processorClient;

    @Mock
    PublishedBookContentService contentService;

    @Mock
    NotificationService notificationService;

    @InjectMocks
    UserBookImportService service;

    @Test
    void enforcesMonthlyPlanLimitAndQueuesProcessorImport() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("private-reader");
        when(repository.countByUserIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
                        eq(user.getId()), any(Instant.class), any(Instant.class)))
                .thenReturn(2L);
        when(quotaAdjustmentRepository.totalForPeriod(
                        eq(user.getId()), eq(MAX_BOOK_IMPORTS_PER_MONTH), any(Instant.class)))
                .thenReturn(0L);
        when(planValidationService.effectiveLimit(user, MAX_BOOK_IMPORTS_PER_MONTH))
                .thenReturn(3);
        when(subscriptionService.getActiveSub(user)).thenReturn(subscriptionWithImportLimit(3));
        when(repository.save(any(UserBookImport.class))).thenAnswer(invocation -> invocation.getArgument(0));
        MockMultipartFile source =
                new MockMultipartFile("file", "book.epub", "application/epub+zip", new byte[] {1, 2, 3});

        BookImportDto result = service.create(user, source, "My Book", "An Author", "Private", Language.EN, 1920);

        verify(planValidationService).validatePlanFeature(user, MAX_BOOK_IMPORTS_PER_MONTH, 3);
        verify(processorClient)
                .createPrivateImport(
                        eq(result.id()),
                        eq(user.getId()),
                        eq("private-reader"),
                        eq(source),
                        eq("My Book"),
                        eq("An Author"),
                        eq("Private"),
                        eq(Language.EN),
                        eq(1920));
        assertThat(result.status().name()).isEqualTo("QUEUED");
    }

    @Test
    void importsWithoutDetailsUseTheFileNameUntilTheProcessorDetectsMetadata() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("private-reader");
        when(repository.countByUserIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
                        eq(user.getId()), any(Instant.class), any(Instant.class)))
                .thenReturn(0L);
        when(quotaAdjustmentRepository.totalForPeriod(
                        eq(user.getId()), eq(MAX_BOOK_IMPORTS_PER_MONTH), any(Instant.class)))
                .thenReturn(0L);
        when(planValidationService.effectiveLimit(user, MAX_BOOK_IMPORTS_PER_MONTH))
                .thenReturn(3);
        when(subscriptionService.getActiveSub(user)).thenReturn(subscriptionWithImportLimit(3));
        when(repository.save(any(UserBookImport.class))).thenAnswer(invocation -> invocation.getArgument(0));
        MockMultipartFile source =
                new MockMultipartFile("file", "pride_and-prejudice.epub", "application/epub+zip", new byte[] {1, 2, 3});

        BookImportDto result = service.create(user, source, " ", null, null, null, null);

        assertThat(result.title()).isEqualTo("pride and prejudice");
        assertThat(result.author()).isEmpty();
        assertThat(result.language()).isNull();
        assertThat(result.metadataStatus()).isEqualTo(BookImportMetadataStatus.PENDING);
        verify(processorClient)
                .createPrivateImport(
                        eq(result.id()),
                        eq(user.getId()),
                        eq("private-reader"),
                        eq(source),
                        isNull(),
                        isNull(),
                        eq(""),
                        isNull(),
                        isNull());
    }

    @Test
    void adoptsDetectedMetadataUntilTheOwnerConfirms() {
        UserBookImport bookImport = pendingImport();
        when(repository.findByIdAndUserId(
                        bookImport.getId(), bookImport.getUser().getId()))
                .thenReturn(Optional.of(bookImport));
        when(repository.save(any(UserBookImport.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Map<String, String> provenance = Map.of("title", "source", "author", "ai", "language", "source");

        service.applyEvent(event(bookImport, "processing", 40, metadata("Pride and Prejudice", true, provenance)));

        assertThat(bookImport.getTitle()).isEqualTo("Pride and Prejudice");
        assertThat(bookImport.getAuthor()).isEqualTo("Jane Austen");
        assertThat(bookImport.getLanguage()).isEqualTo(Language.EN);
        assertThat(bookImport.getPublicationYear()).isEqualTo(1813);
        assertThat(bookImport.getMetadataStatus()).isEqualTo(BookImportMetadataStatus.PROPOSED);
        assertThat(bookImport.getMetadataProvenance()).isEqualTo(provenance);

        bookImport.setMetadataStatus(BookImportMetadataStatus.CONFIRMED);
        service.applyEvent(event(bookImport, "ready", 100, metadata("Renamed by a later run", true, provenance)));

        assertThat(bookImport.getTitle()).isEqualTo("Pride and Prejudice");
        assertThat(bookImport.getStatus()).isEqualTo(BookImportStatus.READY);
        verify(notificationService)
                .notifyOfBookImport(bookImport.getUser(), bookImport.getId(), "Pride and Prejudice", true);
    }

    @Test
    void ignoresMetadataTheProcessorHasNotDetectedYet() {
        UserBookImport bookImport = pendingImport();
        when(repository.findByIdAndUserId(
                        bookImport.getId(), bookImport.getUser().getId()))
                .thenReturn(Optional.of(bookImport));
        when(repository.save(any(UserBookImport.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.applyEvent(event(bookImport, "processing", 5, metadata("", false, Map.of())));

        assertThat(bookImport.getTitle()).isEqualTo("book");
        assertThat(bookImport.getMetadataStatus()).isEqualTo(BookImportMetadataStatus.PENDING);
    }

    @Test
    void confirmingDetailsRecordsTheOwnerAsSourceAndUpdatesTheProcessor() {
        UserBookImport bookImport = pendingImport();
        bookImport.setTitle("Pride and Prejudice");
        bookImport.setAuthor("Jane Austen");
        bookImport.setLanguage(Language.EN);
        bookImport.setMetadataStatus(BookImportMetadataStatus.PROPOSED);
        bookImport.setMetadataProvenance(Map.of("title", "source", "author", "ai", "language", "source"));
        when(repository.findByIdAndUserId(
                        bookImport.getId(), bookImport.getUser().getId()))
                .thenReturn(Optional.of(bookImport));
        when(repository.save(any(UserBookImport.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookImportDto result = service.confirmMetadata(
                bookImport.getUser(),
                bookImport.getId(),
                new BookImportMetadataRequest("Pride and Prejudice", "Jane Austen", " A novel. ", Language.DE, 1813));

        assertThat(result.metadataStatus()).isEqualTo(BookImportMetadataStatus.CONFIRMED);
        assertThat(result.metadataProvenance())
                .containsEntry("title", "source")
                .containsEntry("author", "ai")
                .containsEntry("language", "user")
                .containsEntry("description", "user")
                .containsEntry("publication_year", "user");
        assertThat(result.description()).isEqualTo("A novel.");
        verify(processorClient)
                .updatePrivateImportMetadata(
                        bookImport.getId(),
                        bookImport.getUser().getId(),
                        "Pride and Prejudice",
                        "Jane Austen",
                        "A novel.",
                        Language.DE,
                        1813);
    }

    @Test
    void reportsUsageForTheSubscriberAnchoredMonth() {
        User user = new User();
        user.setId(UUID.randomUUID());
        when(subscriptionService.getActiveSub(user)).thenReturn(subscriptionWithImportLimit(3));
        when(repository.countByUserIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
                        eq(user.getId()), any(Instant.class), any(Instant.class)))
                .thenReturn(1L);
        when(quotaAdjustmentRepository.totalForPeriod(
                        eq(user.getId()), eq(MAX_BOOK_IMPORTS_PER_MONTH), any(Instant.class)))
                .thenReturn(0L);
        when(planValidationService.effectiveLimit(user, MAX_BOOK_IMPORTS_PER_MONTH))
                .thenReturn(3);

        BookImportQuotaDto quota = service.quota(user);

        assertThat(quota.limit()).isEqualTo(3);
        assertThat(quota.used()).isEqualTo(1);
        assertThat(quota.periodEndsAt()).isAfter(quota.periodStartsAt());
        assertThat(quota.periodEndsAt()).isBeforeOrEqualTo(Instant.now().plus(32, ChronoUnit.DAYS));
    }

    private static UserBookImport pendingImport() {
        User user = new User();
        user.setId(UUID.randomUUID());
        UserBookImport bookImport = new UserBookImport();
        bookImport.setId(UUID.randomUUID());
        bookImport.setUser(user);
        bookImport.setTitle("book");
        bookImport.setAuthor("");
        bookImport.setDescription("");
        bookImport.setStatus(BookImportStatus.QUEUED);
        bookImport.setMetadataStatus(BookImportMetadataStatus.PENDING);
        bookImport.setMetadataProvenance(Map.of());
        bookImport.setError("");
        return bookImport;
    }

    private static BookImportEventRequest event(
            UserBookImport bookImport, String status, int progress, BookImportEventRequest.Metadata metadata) {
        return new BookImportEventRequest(
                bookImport.getId(),
                bookImport.getUser().getId(),
                status,
                metadata.title(),
                progress,
                120,
                null,
                metadata);
    }

    private static BookImportEventRequest.Metadata metadata(
            String title, boolean detected, Map<String, String> provenance) {
        return new BookImportEventRequest.Metadata(
                title, "Jane Austen", "A novel of manners.", "en", 1813, provenance, detected);
    }

    private PlanSubscription subscriptionWithImportLimit(int limit) {
        Plan plan = new Plan();
        PlanLimit planLimit = new PlanLimit();
        planLimit.setFeatureKey(MAX_BOOK_IMPORTS_PER_MONTH);
        planLimit.setLimitValue(limit);
        plan.setLimits(List.of(planLimit));

        PlanSubscription subscription = new PlanSubscription();
        subscription.setPlan(plan);
        subscription.setStartDate(Instant.now().minus(10, ChronoUnit.DAYS));
        subscription.setEndDate(Instant.now().plus(20, ChronoUnit.DAYS));
        return subscription;
    }
}
