package com.almonium.learning.book.service;

import static com.almonium.subscription.model.entity.enums.PlanFeature.MAX_BOOK_IMPORTS_PER_MONTH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.infra.notification.service.NotificationService;
import com.almonium.learning.book.dto.response.BookImportDto;
import com.almonium.learning.book.dto.response.BookImportQuotaDto;
import com.almonium.learning.book.model.entity.UserBookImport;
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
