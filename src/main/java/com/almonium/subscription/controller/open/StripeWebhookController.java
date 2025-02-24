package com.almonium.subscription.controller.open;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.config.properties.StripeProperties;
import com.almonium.subscription.exception.StripeIntegrationException;
import com.almonium.subscription.service.StripeWebhookService;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Infra")
@Slf4j
@RestController
@RequestMapping("/public/webhooks/stripe")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class StripeWebhookController {
    StripeWebhookService stripeWebhookService;
    StripeProperties stripeProperties;

    @PostMapping
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            Event event = Webhook.constructEvent(
                    payload, sigHeader, stripeProperties.getWebhook().getSecret());
            stripeWebhookService.handleWebhook(event);
            return ResponseEntity.ok("Webhook handled successfully");
        } catch (StripeException e) {
            log.error("Error processing webhook", e);
            throw new StripeIntegrationException("Webhook Error: " + e.getMessage(), e);
        }
    }
}
