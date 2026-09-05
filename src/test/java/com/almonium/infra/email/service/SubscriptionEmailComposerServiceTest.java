package com.almonium.infra.email.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.almonium.config.properties.AppProperties;
import com.almonium.infra.email.model.dto.EmailContext;
import com.almonium.subscription.model.entity.PlanSubscription;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.thymeleaf.spring6.SpringTemplateEngine;

class SubscriptionEmailComposerServiceTest {

    private final SubscriptionEmailComposerService composer = composer();

    @Test
    void routesBillingEventsToMembership() {
        assertThat(placeholders(PlanSubscription.Event.CANCELED, Map.of()).get("url"))
                .isEqualTo("https://almonium.example/membership?portal=to");
        assertThat(placeholders(PlanSubscription.Event.PAYMENT_FAILED, Map.of()).get("url"))
                .isEqualTo("https://almonium.example/membership?portal=to");
        assertThat(placeholders(PlanSubscription.Event.ENDED, Map.of()).get("url"))
                .isEqualTo("https://almonium.example/membership");
    }

    @Test
    void printsAnUpcomingPeriodEndAsADay() {
        Map<String, String> placeholders = placeholders(
                PlanSubscription.Event.RENEWED,
                Map.of(SubscriptionEmailComposerService.PERIOD_ENDS_AT, "2031-09-12T23:59:59Z"));

        assertThat(placeholders).containsEntry(SubscriptionEmailComposerService.PERIOD_END_DATE, "12 September 2031");
    }

    @Test
    void leavesOutAPeriodEndThatHasAlreadyPassed() {
        String yesterday = Instant.now().minus(1, ChronoUnit.DAYS).toString();

        Map<String, String> placeholders = placeholders(
                PlanSubscription.Event.RENEWED, Map.of(SubscriptionEmailComposerService.PERIOD_ENDS_AT, yesterday));

        assertThat(placeholders).doesNotContainKey(SubscriptionEmailComposerService.PERIOD_END_DATE);
    }

    @Test
    void leavesOutAMissingPeriodEnd() {
        assertThat(placeholders(PlanSubscription.Event.CREATED, Map.of()))
                .doesNotContainKey(SubscriptionEmailComposerService.PERIOD_END_DATE);
    }

    private Map<String, String> placeholders(PlanSubscription.Event event, Map<String, String> extra) {
        Map<String, String> attributes = new java.util.HashMap<>(extra);
        attributes.put(SubscriptionEmailComposerService.PLAN_NAME, "PREMIUM");
        return composer.getCustomPlaceholders(new EmailContext<>(event, attributes));
    }

    private static SubscriptionEmailComposerService composer() {
        AppProperties properties = new AppProperties();
        properties.setWebDomain("https://almonium.example");
        return new SubscriptionEmailComposerService(
                mock(EmailService.class), mock(SpringTemplateEngine.class), properties);
    }
}
