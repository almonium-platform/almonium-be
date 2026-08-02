package com.almonium.infra.messaging.consumer;

import com.almonium.infra.messaging.exception.EventProcessingException;
import com.almonium.subscription.event.PaddleUserCleanupRequestedEvent;
import com.almonium.subscription.exception.PaddleIntegrationException;
import com.almonium.subscription.service.PaddleApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserPaddleCleanupListener {
    private final PaddleApiService paddleApiService;

    @RabbitListener(queues = "${rabbitmq.queue.user-deleted-paddle.name}")
    public void handleUserDeletedForPaddle(PaddleUserCleanupRequestedEvent event) {
        event.subscriptionId()
                .ifPresentOrElse(
                        subscriptionId -> cancelSubscription(subscriptionId, event),
                        () -> log.info("No Paddle subscription for deleted user {}, skipping cleanup", event.userId()));
    }

    private void cancelSubscription(String subscriptionId, PaddleUserCleanupRequestedEvent event) {
        try {
            paddleApiService.cancelSubscriptionImmediately(subscriptionId);
            log.info("Canceled Paddle subscription for deleted user {}", event.userId());
        } catch (PaddleIntegrationException exception) {
            log.error("Paddle cancellation failed for deleted user {}", event.userId(), exception);
            throw exception;
        } catch (Exception exception) {
            throw new EventProcessingException(
                    "Paddle cleanup failed unexpectedly for user " + event.userId(), exception);
        }
    }
}
