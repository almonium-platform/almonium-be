package com.almonium.subscription.webhook;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Watches the outcome of a refund we asked for.
 *
 * <p>A card refund is not final when the API accepts it. Paddle creates it as {@code pending_approval} and may reject
 * it afterwards, which is the one failure in the guarantee switch that nobody would otherwise see: the member has
 * already been moved to monthly and charged for it, and has been told their annual payment is coming back. The switch
 * is not reversed here, because unwinding a subscription from a webhook is how one bad refund becomes two bad states -
 * the refund is re-issued or settled by hand, and this exists so that somebody knows to.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PaddleAdjustmentUpdatedHandler implements PaddleEventHandler {
    /** Terminal states in which the money does not reach the customer. */
    private static final List<String> FAILED_STATUSES = List.of("rejected", "reversed");

    @Override
    public String eventType() {
        return "adjustment.updated";
    }

    @Override
    public void handle(PaddleEvent event) {
        JsonNode data = event.data();
        if (!"refund".equals(data.path("action").textValue())) {
            return;
        }
        String status = data.path("status").textValue();
        String transactionId = data.path("transaction_id").textValue();
        String subscriptionId = data.path("subscription_id").textValue();
        if (FAILED_STATUSES.contains(status)) {
            log.error(
                    "Paddle {} a refund on transaction {} (subscription {}). If this was a guarantee switch the "
                            + "member is already on monthly and has been told the annual payment is coming back; it "
                            + "has to be settled by hand.",
                    status,
                    transactionId,
                    subscriptionId);
            return;
        }
        log.info("Paddle refund on transaction {} is now {}", transactionId, status);
    }
}
