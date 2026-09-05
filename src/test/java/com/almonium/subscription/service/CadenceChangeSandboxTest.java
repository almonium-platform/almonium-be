package com.almonium.subscription.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.almonium.config.properties.PaddleProperties;
import com.almonium.learning.review.repository.ReviewSessionRepository;
import com.almonium.subscription.model.entity.Plan;
import com.almonium.subscription.model.entity.PlanSubscription;
import com.almonium.subscription.repository.PlanRepository;
import com.almonium.subscription.repository.PlanSubscriptionRepository;
import java.time.Clock;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

/**
 * Talks to the real Paddle sandbox, and is skipped unless it is pointed at one.
 *
 * <p>It exists for the parts of this integration that cannot be asserted against a mock: whether the endpoint paths
 * and field names Paddle documents are the ones it actually answers to, and whether a billing mode does what its name
 * suggests. Those are the assumptions that broke twice while this was being written - a preview endpoint that only
 * accepts PATCH, a proration credit stated as a negative, a next-billing-period mode Paddle refuses outright the
 * moment the interval changes.
 *
 * <p>Run it with the sandbox credentials in the environment plus PADDLE_SANDBOX_SUBSCRIPTION_ID naming an active
 * subscription. It moves that subscription's cadence, so point it at a fixture and not at anything precious.
 */
@EnabledIfEnvironmentVariable(named = "PADDLE_SANDBOX_SUBSCRIPTION_ID", matches = ".+")
class CadenceChangeSandboxTest {
    private static final String SUBSCRIPTION_ID = System.getenv("PADDLE_SANDBOX_SUBSCRIPTION_ID");

    private PaddleApiService paddleApiService;
    private PaddlePriceCatalog priceCatalog;
    private CadenceChangeService service;
    private PlanSubscriptionRepository planSubRepository;

    @BeforeEach
    void setUp() {
        PaddleProperties properties = new PaddleProperties();
        properties.setEnvironment(PaddleProperties.Environment.SANDBOX);
        properties.getApi().setKey(System.getenv("PADDLE_API_KEY"));
        properties.getPrices().setPremiumMonthly(System.getenv("PADDLE_PRICE_PREMIUM_MONTHLY"));
        properties.getPrices().setPremiumAnnual(System.getenv("PADDLE_PRICE_PREMIUM_ANNUAL"));
        properties.getPrices().setFounderMonthly(System.getenv("PADDLE_PRICE_FOUNDER_MONTHLY"));
        properties.getPrices().setFounderAnnual(System.getenv("PADDLE_PRICE_FOUNDER_ANNUAL"));

        priceCatalog = new PaddlePriceCatalog(properties);
        priceCatalog.validate();
        RestClient restClient = RestClient.builder()
                .requestFactory(new JdkClientHttpRequestFactory())
                .baseUrl(properties.apiBaseUrl())
                .defaultHeader(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + properties.getApi().getKey())
                .defaultHeader("Paddle-Version", "1")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        paddleApiService = new PaddleApiService(
                restClient, priceCatalog, new com.fasterxml.jackson.databind.ObjectMapper(), properties);
        planSubRepository = mock(PlanSubscriptionRepository.class);
        service = new CadenceChangeService(
                paddleApiService,
                priceCatalog,
                mock(PlanSubscriptionService.class),
                planSubRepository,
                mock(PlanRepository.class),
                mock(ReviewSessionRepository.class),
                Clock.systemUTC());
    }

    @Test
    void everyFieldThePreviewReadsIsActuallyThere() {
        PaddleApiService.SubscriptionSnapshot snapshot = paddleApiService.getSubscription(SUBSCRIPTION_ID);
        Plan.Type target = priceCatalog.planTypeFor(snapshot.priceId()) == Plan.Type.MONTHLY
                ? Plan.Type.YEARLY
                : Plan.Type.MONTHLY;
        String targetPriceId = priceCatalog.priceIdFor(target, priceCatalog.isFounderPrice(snapshot.priceId()));

        PaddleApiService.CadenceChangePreview preview = paddleApiService.previewCadenceChange(
                SUBSCRIPTION_ID,
                targetPriceId,
                com.almonium.subscription.model.entity.enums.ProrationBillingMode.DO_NOT_BILL);

        assertThat(preview.recurring().minorUnits()).isPositive();
        assertThat(preview.recurring().currencyCode()).isNotBlank();
        assertThat(snapshot.billingPeriodEndsAt()).isPresent();
    }

    /**
     * Paddle rejects a deferred billing mode across a change of interval, which is the whole reason a pending change
     * is held locally. If this ever starts passing, the local scheduling can be handed back to Paddle.
     */
    @Test
    void paddleStillRefusesToDeferAChangeOfInterval() {
        PaddleApiService.SubscriptionSnapshot snapshot = paddleApiService.getSubscription(SUBSCRIPTION_ID);
        Plan.Type target = priceCatalog.planTypeFor(snapshot.priceId()) == Plan.Type.MONTHLY
                ? Plan.Type.YEARLY
                : Plan.Type.MONTHLY;
        String targetPriceId = priceCatalog.priceIdFor(target, priceCatalog.isFounderPrice(snapshot.priceId()));

        assertThat(org.assertj.core.api.Assertions.catchThrowable(() -> paddleApiService.previewCadenceChange(
                        SUBSCRIPTION_ID,
                        targetPriceId,
                        com.almonium.subscription.model.entity.enums.ProrationBillingMode.FULL_NEXT_BILLING_PERIOD)))
                .isNotNull();
    }

    @Test
    void applyingADueChangeMovesTheSubscription() {
        PaddleApiService.SubscriptionSnapshot before = paddleApiService.getSubscription(SUBSCRIPTION_ID);
        Plan.Type current = priceCatalog.planTypeFor(before.priceId());
        Plan.Type target = current == Plan.Type.MONTHLY ? Plan.Type.YEARLY : Plan.Type.MONTHLY;
        Plan targetPlan = Plan.builder().id(9L).name("PREMIUM").type(target).build();
        PlanSubscription subscription = PlanSubscription.builder()
                .plan(Plan.builder().id(8L).name("PREMIUM").type(current).build())
                .paddleSubscriptionId(SUBSCRIPTION_ID)
                .status(PlanSubscription.Status.ACTIVE)
                .scheduledPlan(targetPlan)
                .scheduledChangeAt(Instant.now())
                .build();

        service.applyDueChange(subscription);

        assertThat(priceCatalog.planTypeFor(
                        paddleApiService.getSubscription(SUBSCRIPTION_ID).priceId()))
                .isEqualTo(target);
        assertThat(subscription.getPlan()).isEqualTo(targetPlan);
        assertThat(subscription.getScheduledPlan()).isNull();
        assertThat(subscription.getScheduledChangeAt()).isNull();
    }
}
