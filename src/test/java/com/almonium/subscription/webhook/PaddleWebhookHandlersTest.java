package com.almonium.subscription.webhook;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import com.almonium.subscription.service.PlanSubscriptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaddleWebhookHandlersTest {
    private static final Instant OCCURRED_AT = Instant.parse("2026-08-18T10:00:00Z");

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PaddleEventData eventData = new PaddleEventData();

    @Mock
    PlanSubscriptionService planSubscriptionService;

    @Test
    void completedRecurringTransactionEmitsTrueRenewal() throws Exception {
        PaddleTransactionCompletedHandler handler = new PaddleTransactionCompletedHandler(planSubscriptionService);
        PaddleEvent event = event(
                "transaction.completed",
                """
                {"origin":"subscription_recurring","subscription_id":"sub_01test"}
                """);

        handler.handle(event);

        verify(planSubscriptionService).notifyRenewed("sub_01test");
    }

    @Test
    void completedCheckoutTransactionIsNotTreatedAsRenewal() throws Exception {
        PaddleTransactionCompletedHandler handler = new PaddleTransactionCompletedHandler(planSubscriptionService);

        handler.handle(event(
                "transaction.completed",
                """
                {"origin":"web","subscription_id":"sub_01test"}
                """));

        verifyNoInteractions(planSubscriptionService);
    }

    @Test
    void canceledUpdateAcceptsNullBillingPeriod() throws Exception {
        PaddleSubscriptionUpdatedHandler handler =
                new PaddleSubscriptionUpdatedHandler(eventData, planSubscriptionService);

        handler.handle(
                event(
                        "subscription.updated",
                        """
                {
                  "id":"sub_01test",
                  "status":"canceled",
                  "items":[{"price":{"id":"pri_monthly"}}],
                  "scheduled_change":null,
                  "current_billing_period":null
                }
                """));

        verify(planSubscriptionService)
                .reconcileSubscription(
                        "sub_01test",
                        "pri_monthly",
                        "canceled",
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        OCCURRED_AT);
    }

    private PaddleEvent event(String eventType, String data) throws Exception {
        return new PaddleEvent("evt_01test", eventType, OCCURRED_AT, objectMapper.readTree(data));
    }
}
