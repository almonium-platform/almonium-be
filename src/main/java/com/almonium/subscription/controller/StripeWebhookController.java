package com.almonium.subscription.controller;

import com.almonium.subscription.exception.StripeIntegrationException;
import com.almonium.subscription.service.StripeWebhookService;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/webhooks/stripe")
@RequiredArgsConstructor
public class StripeWebhookController {
    private final StripeWebhookService stripeWebhookService;

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @PostMapping
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            stripeWebhookService.handleWebhook(event);
            return ResponseEntity.ok("Webhook handled successfully");
        } catch (StripeException e) {
            log.error("Error processing webhook", e);
            throw new StripeIntegrationException("Webhook Error: " + e.getMessage(), e);
        }
    }
}
