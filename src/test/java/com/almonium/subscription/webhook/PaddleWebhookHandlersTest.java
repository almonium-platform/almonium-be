package com.almonium.subscription.webhook;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.almonium.subscription.service.PlanSubscriptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

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

    @Test
    void aRejectedRefundIsReportedRatherThanSwallowed() throws Exception {
        ListAppender<ILoggingEvent> appender = captureLogsOf(PaddleAdjustmentUpdatedHandler.class);

        new PaddleAdjustmentUpdatedHandler()
                .handle(
                        event(
                                "adjustment.updated",
                                """
                        {"action":"refund","status":"rejected","transaction_id":"txn_01test","subscription_id":"sub_01test"}
                        """));

        assertThat(appender.list).singleElement().satisfies(logged -> {
            assertThat(logged.getLevel()).isEqualTo(Level.ERROR);
            assertThat(logged.getFormattedMessage()).contains("txn_01test", "sub_01test", "rejected");
        });
    }

    @Test
    void anAdjustmentThatIsNotARefundIsIgnored() throws Exception {
        ListAppender<ILoggingEvent> appender = captureLogsOf(PaddleAdjustmentUpdatedHandler.class);

        new PaddleAdjustmentUpdatedHandler()
                .handle(
                        event(
                                "adjustment.updated",
                                """
                        {"action":"credit","status":"approved","transaction_id":"txn_01test"}
                        """));

        assertThat(appender.list).isEmpty();
    }

    private ListAppender<ILoggingEvent> captureLogsOf(Class<?> type) {
        ListAppender<ILoggingEvent> appender = new ListAppender<>();
        appender.start();
        ((Logger) LoggerFactory.getLogger(type)).addAppender(appender);
        return appender;
    }

    private PaddleEvent event(String eventType, String data) throws Exception {
        return new PaddleEvent("evt_01test", eventType, OCCURRED_AT, objectMapper.readTree(data));
    }
}
