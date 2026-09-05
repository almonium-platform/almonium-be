package com.almonium.subscription.controller.open;

import com.almonium.subscription.exception.PaddleIntegrationException;
import com.almonium.subscription.service.PaddleWebhookService;
import com.almonium.subscription.webhook.PaddleEvent;
import com.almonium.subscription.webhook.PaddleWebhookSignatureVerifier;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Tag(name = "Infra")
@RestController
@RequestMapping("/public/webhooks/paddle")
@RequiredArgsConstructor
public class PaddleWebhookController {
    private final PaddleWebhookSignatureVerifier signatureVerifier;
    private final PaddleWebhookService webhookService;
    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload, @RequestHeader("Paddle-Signature") String signature) {
        signatureVerifier.verify(payload, signature);
        try {
            webhookService.handle(objectMapper.readValue(payload, PaddleEvent.class));
            return ResponseEntity.ok("Webhook handled successfully");
        } catch (JacksonException exception) {
            throw new PaddleIntegrationException("Invalid Paddle webhook payload", exception);
        }
    }
}
