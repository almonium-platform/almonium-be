package com.almonium.infra.email.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.almonium.config.properties.AppProperties;
import com.almonium.infra.email.model.dto.EmailContext;
import com.almonium.subscription.model.entity.PlanSubscription;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.thymeleaf.spring6.SpringTemplateEngine;

class SubscriptionEmailComposerServiceTest {

    @Test
    void routesBillingEventsToMembership() {
        AppProperties properties = new AppProperties();
        properties.setWebDomain("https://almonium.example");
        SubscriptionEmailComposerService composer = new SubscriptionEmailComposerService(
                mock(EmailService.class), mock(SpringTemplateEngine.class), properties);

        assertThat(actionUrl(composer, PlanSubscription.Event.CANCELED))
                .isEqualTo("https://almonium.example/membership?portal=to");
        assertThat(actionUrl(composer, PlanSubscription.Event.PAYMENT_FAILED))
                .isEqualTo("https://almonium.example/membership?portal=to");
        assertThat(actionUrl(composer, PlanSubscription.Event.ENDED)).isEqualTo("https://almonium.example/membership");
    }

    private String actionUrl(SubscriptionEmailComposerService composer, PlanSubscription.Event event) {
        EmailContext<PlanSubscription.Event> context =
                new EmailContext<>(event, Map.of(SubscriptionEmailComposerService.PLAN_NAME, "PREMIUM"));
        return composer.getCustomPlaceholders(context).get("url");
    }
}
