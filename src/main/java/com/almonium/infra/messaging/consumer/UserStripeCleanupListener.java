package com.almonium.infra.messaging.consumer;

import com.almonium.infra.messaging.exception.EventProcessingException;
import com.almonium.subscription.exception.StripeIntegrationException;
import com.almonium.subscription.service.StripeApiService;
import com.almonium.user.core.events.UserDeletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserStripeCleanupListener {
    private final StripeApiService stripeApiService;

    @RabbitListener(queues = "${rabbitmq.queue.user-deleted-stripe.name}")
    public void handleUserDeletedForStripe(UserDeletedEvent event) {
        log.info("Received user deleted event for Stripe cleanup, userId: {}", event.userId());

        event.stripeSubscriptionId()
                .ifPresentOrElse(
                        subId -> {
                            try {
                                stripeApiService.cancelSubImmediately(subId);
                                log.info(
                                        "Successfully canceled Stripe subscription {} for deleted user {}",
                                        subId,
                                        event.userId());
                            } catch (StripeIntegrationException e) {
                                log.error(
                                        "Stripe cancellation failed for subId {} (user {}). Error: {}",
                                        subId,
                                        event.userId(),
                                        e.getMessage(),
                                        e);
                                throw e;
                            } catch (Exception e) {
                                log.error(
                                        "Unexpected error during Stripe cleanup for user {}. Error: {}",
                                        event.userId(),
                                        e.getMessage(),
                                        e);
                                throw new EventProcessingException(
                                        "Stripe cleanup failed unexpectedly for user " + event.userId(), e);
                            }
                        },
                        () -> log.info(
                                "No Stripe subscription ID present for user {}, skipping Stripe cleanup.",
                                event.userId()));
    }
}
