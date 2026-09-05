package com.almonium.subscription.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.learning.review.repository.ReviewSessionRepository;
import com.almonium.subscription.dto.response.CadenceChangeOptionDto;
import com.almonium.subscription.dto.response.CadenceChangePreviewDto;
import com.almonium.subscription.model.entity.Plan;
import com.almonium.subscription.model.entity.PlanSubscription;
import com.almonium.subscription.model.entity.enums.CadenceChangeOption;
import com.almonium.subscription.model.entity.enums.ProrationBillingMode;
import com.almonium.subscription.repository.PlanRepository;
import com.almonium.subscription.repository.PlanSubscriptionRepository;
import com.almonium.subscription.service.PaddleApiService.CadenceChangePreview;
import com.almonium.subscription.service.PaddleApiService.Money;
import com.almonium.subscription.service.PaddleApiService.PaidTransaction;
import com.almonium.subscription.service.PaddleApiService.SubscriptionSnapshot;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.model.entity.User;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CadenceChangeServiceTest {
    private static final Instant NOW = Instant.parse("2026-09-05T12:00:00Z");
    private static final Instant PERIOD_ENDS = Instant.parse("2027-09-04T12:00:00Z");
    private static final String SUBSCRIPTION_ID = "sub_01annual";
    private static final String FOUNDER_MONTHLY = "pri_founder_monthly";
    private static final String FOUNDER_ANNUAL = "pri_founder_annual";

    @Mock
    PaddleApiService paddleApiService;

    @Mock
    PaddlePriceCatalog paddlePriceCatalog;

    @Mock
    PlanSubscriptionService planSubscriptionService;

    @Mock
    PlanSubscriptionRepository planSubRepository;

    @Mock
    PlanRepository planRepository;

    @Mock
    ReviewSessionRepository reviewSessionRepository;

    CadenceChangeService service;
    User user;
    Plan annual;
    Plan monthly;
    PlanSubscription subscription;

    @BeforeEach
    void setUp() {
        service = new CadenceChangeService(
                paddleApiService,
                paddlePriceCatalog,
                planSubscriptionService,
                planSubRepository,
                planRepository,
                reviewSessionRepository,
                Clock.fixed(NOW, ZoneOffset.UTC));
        user = User.builder().id(UUID.randomUUID()).email("founder@example.com").build();
        annual = Plan.builder()
                .id(3L)
                .name("PREMIUM")
                .type(Plan.Type.YEARLY)
                .founderPrice(80.0)
                .build();
        monthly = Plan.builder()
                .id(2L)
                .name("PREMIUM")
                .type(Plan.Type.MONTHLY)
                .founderPrice(8.0)
                .build();
        subscription = PlanSubscription.builder()
                .plan(annual)
                .user(user)
                .paddleSubscriptionId(SUBSCRIPTION_ID)
                .status(PlanSubscription.Status.ACTIVE)
                .build();
    }

    @Test
    void offersOnlyASchedulingOptionOnceTheGuaranteeWindowHasPassed() {
        annualToMonthly();
        billedAt(NOW.minus(java.time.Duration.ofDays(40)));

        CadenceChangePreviewDto preview = service.preview(user, Plan.Type.MONTHLY);

        assertThat(preview.options())
                .extracting(CadenceChangeOptionDto::kind)
                .containsExactly(CadenceChangeOption.SCHEDULED);
        CadenceChangeOptionDto scheduled = preview.options().getFirst();
        assertThat(scheduled.dueNowMinorUnits()).isZero();
        assertThat(scheduled.refundMinorUnits()).isNull();
        assertThat(scheduled.effectiveAt()).isEqualTo(PERIOD_ENDS);
        assertThat(preview.founderPrice()).isTrue();
        assertThat(preview.targetPriceMinorUnits()).isEqualTo(800L);
    }

    @Test
    void leadsWithTheRefundWhileTheAnnualPaymentIsStillInsideTheGuarantee() {
        annualToMonthly();
        billedAt(NOW.minus(java.time.Duration.ofDays(6)));

        CadenceChangePreviewDto preview = service.preview(user, Plan.Type.MONTHLY);

        assertThat(preview.options())
                .extracting(CadenceChangeOptionDto::kind)
                .containsExactly(CadenceChangeOption.REFUND_AND_SWITCH, CadenceChangeOption.SCHEDULED);
        CadenceChangeOptionDto refund = preview.options().getFirst();
        assertThat(refund.recommended()).isTrue();
        assertThat(refund.refundMinorUnits()).isEqualTo(8000L);
        assertThat(refund.dueNowMinorUnits()).isEqualTo(800L);
        assertThat(refund.effectiveAt()).isEqualTo(NOW);
    }

    @Test
    void schedulingADowngradeBillsNothingNowAndRecordsWhatIsPending() {
        annualToMonthly();
        billedAt(NOW.minus(java.time.Duration.ofDays(40)));

        service.apply(user, Plan.Type.MONTHLY, CadenceChangeOption.SCHEDULED);

        verify(paddleApiService)
                .changeCadence(SUBSCRIPTION_ID, FOUNDER_MONTHLY, ProrationBillingMode.FULL_NEXT_BILLING_PERIOD);
        assertThat(subscription.getScheduledPlan()).isEqualTo(monthly);
        assertThat(subscription.getScheduledChangeAt()).isEqualTo(PERIOD_ENDS);
        // The plan they paid for is untouched: the member is annual until the date above.
        assertThat(subscription.getPlan()).isEqualTo(annual);
    }

    @Test
    void refusesAnImmediateProrationOnTheDowngradeDirection() {
        annualToMonthly();
        billedAt(NOW.minus(java.time.Duration.ofDays(40)));

        assertThatThrownBy(() -> service.apply(user, Plan.Type.MONTHLY, CadenceChangeOption.PRORATED_NOW))
                .isInstanceOf(BadUserRequestActionException.class);
        verify(paddleApiService, never()).changeCadence(any(), any(), any());
    }

    @Test
    void chargesBeforeRefundingSoADeclineLeavesTheYearIntact() {
        annualToMonthly();
        billedAt(NOW.minus(java.time.Duration.ofDays(6)));

        service.apply(user, Plan.Type.MONTHLY, CadenceChangeOption.REFUND_AND_SWITCH);

        var order = org.mockito.Mockito.inOrder(paddleApiService);
        order.verify(paddleApiService)
                .changeCadence(SUBSCRIPTION_ID, FOUNDER_MONTHLY, ProrationBillingMode.FULL_IMMEDIATELY);
        order.verify(paddleApiService).refundTransactionInFull(eq("txn_01annual"), any());
        assertThat(subscription.getScheduledPlan()).isNull();
    }

    @Test
    void upgradingProratesImmediatelyAndKeepsTheFounderPrice() {
        subscription.setPlan(monthly);
        when(planSubscriptionService.getActiveSub(user)).thenReturn(subscription);
        when(paddleApiService.getSubscription(SUBSCRIPTION_ID)).thenReturn(snapshot(FOUNDER_MONTHLY));
        when(paddlePriceCatalog.isFounderPrice(FOUNDER_MONTHLY)).thenReturn(true);
        when(paddlePriceCatalog.priceIdFor(Plan.Type.YEARLY, true)).thenReturn(FOUNDER_ANNUAL);
        when(planRepository.findByNameAndType("PREMIUM", Plan.Type.YEARLY)).thenReturn(Optional.of(annual));
        when(paddleApiService.previewCadenceChange(
                        SUBSCRIPTION_ID, FOUNDER_ANNUAL, ProrationBillingMode.PRORATED_IMMEDIATELY))
                .thenReturn(new CadenceChangePreview(
                        new Money(7626L, "USD"),
                        new Money(8000L, "USD"),
                        new Money(374L, "USD"),
                        Optional.of(PERIOD_ENDS),
                        Optional.of(PERIOD_ENDS)));

        CadenceChangePreviewDto preview = service.preview(user, Plan.Type.YEARLY);
        service.apply(user, Plan.Type.YEARLY, CadenceChangeOption.PRORATED_NOW);

        CadenceChangeOptionDto option = preview.options().getFirst();
        assertThat(option.kind()).isEqualTo(CadenceChangeOption.PRORATED_NOW);
        assertThat(option.creditMinorUnits()).isEqualTo(374L);
        assertThat(option.dueNowMinorUnits()).isEqualTo(7626L);
        assertThat(option.nextBilledAt()).isEqualTo(PERIOD_ENDS);
        verify(paddleApiService)
                .changeCadence(SUBSCRIPTION_ID, FOUNDER_ANNUAL, ProrationBillingMode.PRORATED_IMMEDIATELY);
    }

    @Test
    void doesNotPrintACreditRowWhenThereIsNoCredit() {
        annualToMonthly();
        billedAt(NOW.minus(java.time.Duration.ofDays(40)));

        assertThat(service.preview(user, Plan.Type.MONTHLY).options().getFirst().creditMinorUnits())
                .isNull();
    }

    @Test
    void offersAnnualOnlyAfterEightFinishedSessions() {
        subscription.setPlan(monthly);
        when(planSubscriptionService.getActiveSub(user)).thenReturn(subscription);
        when(reviewSessionRepository.countByOwnerIdAndCompletedAtIsNotNull(user.getId()))
                .thenReturn(7L, 8L);

        assertThat(service.annualNudge(user).eligible()).isFalse();
        assertThat(service.annualNudge(user).eligible()).isTrue();
    }

    @Test
    void neverOffersAnnualToAMemberWhoIsAlreadyOnIt() {
        when(planSubscriptionService.getActiveSub(user)).thenReturn(subscription);
        when(reviewSessionRepository.countByOwnerIdAndCompletedAtIsNotNull(user.getId()))
                .thenReturn(40L);

        assertThat(service.annualNudge(user).eligible()).isFalse();
    }

    @Test
    void holdsTheAnnualOfferBackWhileAChangeIsAlreadyPending() {
        subscription.setPlan(monthly);
        subscription.setScheduledPlan(annual);
        subscription.setScheduledChangeAt(PERIOD_ENDS);
        when(planSubscriptionService.getActiveSub(user)).thenReturn(subscription);
        when(reviewSessionRepository.countByOwnerIdAndCompletedAtIsNotNull(user.getId()))
                .thenReturn(40L);

        assertThat(service.annualNudge(user).eligible()).isFalse();
    }

    @Test
    void undoingAPendingChangeBillsNothing() {
        subscription.setScheduledPlan(monthly);
        subscription.setScheduledChangeAt(PERIOD_ENDS);
        when(planSubscriptionService.getActiveSub(user)).thenReturn(subscription);
        when(paddleApiService.getSubscription(SUBSCRIPTION_ID)).thenReturn(snapshot(FOUNDER_ANNUAL));
        when(paddlePriceCatalog.isFounderPrice(FOUNDER_ANNUAL)).thenReturn(true);
        when(paddlePriceCatalog.priceIdFor(Plan.Type.YEARLY, true)).thenReturn(FOUNDER_ANNUAL);

        service.undo(user);

        verify(paddleApiService).changeCadence(SUBSCRIPTION_ID, FOUNDER_ANNUAL, ProrationBillingMode.DO_NOT_BILL);
        assertThat(subscription.getScheduledPlan()).isNull();
        assertThat(subscription.getScheduledChangeAt()).isNull();
    }

    @Test
    void refusesToUndoWhenNothingIsPending() {
        when(planSubscriptionService.getActiveSub(user)).thenReturn(subscription);

        assertThatThrownBy(() -> service.undo(user)).isInstanceOf(BadUserRequestActionException.class);
    }

    private void annualToMonthly() {
        when(planSubscriptionService.getActiveSub(user)).thenReturn(subscription);
        when(paddleApiService.getSubscription(SUBSCRIPTION_ID)).thenReturn(snapshot(FOUNDER_ANNUAL));
        when(paddlePriceCatalog.isFounderPrice(FOUNDER_ANNUAL)).thenReturn(true);
        when(paddlePriceCatalog.priceIdFor(Plan.Type.MONTHLY, true)).thenReturn(FOUNDER_MONTHLY);
        when(planRepository.findByNameAndType("PREMIUM", Plan.Type.MONTHLY)).thenReturn(Optional.of(monthly));
        lenient()
                .when(paddleApiService.previewCadenceChange(
                        SUBSCRIPTION_ID, FOUNDER_MONTHLY, ProrationBillingMode.FULL_NEXT_BILLING_PERIOD))
                .thenReturn(new CadenceChangePreview(
                        new Money(0L, "USD"),
                        new Money(800L, "USD"),
                        new Money(0L, "USD"),
                        Optional.of(PERIOD_ENDS),
                        Optional.of(PERIOD_ENDS)));
        lenient()
                .when(paddleApiService.previewCadenceChange(
                        SUBSCRIPTION_ID, FOUNDER_MONTHLY, ProrationBillingMode.FULL_IMMEDIATELY))
                .thenReturn(new CadenceChangePreview(
                        new Money(800L, "USD"),
                        new Money(800L, "USD"),
                        new Money(0L, "USD"),
                        Optional.of(PERIOD_ENDS),
                        Optional.of(NOW.plus(java.time.Duration.ofDays(30)))));
    }

    private void billedAt(Instant billedAt) {
        when(paddleApiService.latestPaidTransaction(SUBSCRIPTION_ID))
                .thenReturn(Optional.of(new PaidTransaction("txn_01annual", new Money(8000L, "USD"), billedAt)));
    }

    private SubscriptionSnapshot snapshot(String priceId) {
        return new SubscriptionSnapshot(
                SUBSCRIPTION_ID,
                priceId,
                "active",
                Optional.empty(),
                Optional.of(NOW.minus(java.time.Duration.ofDays(6))),
                Optional.of(PERIOD_ENDS));
    }

    @Test
    void rejectsACadenceItCannotBill() {
        subscription.setPlan(
                Plan.builder().id(1L).name("FREE").type(Plan.Type.LIFETIME).build());
        when(planSubscriptionService.getActiveSub(user)).thenReturn(subscription);

        assertThatThrownBy(() -> service.preview(user, Plan.Type.MONTHLY))
                .isInstanceOf(BadUserRequestActionException.class);
    }
}
