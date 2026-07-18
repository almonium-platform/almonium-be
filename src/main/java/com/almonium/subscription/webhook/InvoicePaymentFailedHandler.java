package com.almonium.subscription.webhook;

import com.almonium.subscription.service.PlanSubscriptionService;
import com.stripe.model.Event;
import com.stripe.model.Invoice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvoicePaymentFailedHandler implements StripeEventHandler {
    private final StripeEventObjectExtractor extractor;
    private final PlanSubscriptionService planSubscriptionService;

    @Override
    public String eventType() {
        return "invoice.payment_failed";
    }

    @Override
    public void handle(Event event) {
        Invoice invoice = extractor.extract(event, Invoice.class);
        planSubscriptionService.putSubscriptionOnHold(invoice.getSubscription());
    }
}
