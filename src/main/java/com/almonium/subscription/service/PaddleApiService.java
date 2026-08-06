package com.almonium.subscription.service;

import com.almonium.subscription.exception.PaddleIntegrationException;
import com.almonium.subscription.model.entity.Plan;
import com.almonium.user.core.model.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaddleApiService {
    private final RestClient paddleRestClient;
    private final PaddlePriceCatalog priceCatalog;
    private final ObjectMapper objectMapper;

    public String createCustomerIdForUser(User user) {
        Map<String, Object> request = Map.of(
                "email", user.getEmail(),
                "custom_data", Map.of("user_id", user.getId().toString()));
        try {
            return postForRequiredText("/customers", request, "/data/id", "create customer");
        } catch (PaddleIntegrationException exception) {
            if (!isCustomerAlreadyExists(exception)) {
                throw exception;
            }
            log.info("Paddle customer already exists for user {}; reconciling by email", user.getId());
            return findCustomerIdByEmail(user.getEmail());
        }
    }

    public CheckoutTransaction createPaymentTransaction(User user, Plan plan, Optional<Integer> foundingMemberSlot) {
        boolean founder = foundingMemberSlot.isPresent();
        String priceId = priceCatalog.priceIdFor(plan.getType(), founder);
        Map<String, Object> customData = founder
                ? Map.of(
                        "user_id", user.getId().toString(),
                        "plan_id", plan.getId(),
                        "founding_member_slot", foundingMemberSlot.orElseThrow())
                : Map.of("user_id", user.getId().toString(), "plan_id", plan.getId());
        Map<String, Object> request = Map.of(
                "items", List.of(Map.of("price_id", priceId, "quantity", 1)),
                "customer_id", user.getPaddleCustomerId(),
                "custom_data", customData);

        JsonNode response = post("/transactions", request, "create checkout transaction");
        return new CheckoutTransaction(
                requiredText(response, "/data/id", "transaction ID"),
                requiredText(response, "/data/checkout/url", "checkout URL"));
    }

    public String createCustomerPortalSessionForUser(User user) {
        String path = "/customers/" + user.getPaddleCustomerId() + "/portal-sessions";
        return postForRequiredText(path, Map.of(), "/data/urls/general/overview", "create customer portal session");
    }

    public void cancelSubscriptionImmediately(String subscriptionId) {
        cancelSubscription(subscriptionId, "immediately");
    }

    public void scheduleSubscriptionCancellation(String subscriptionId) {
        cancelSubscription(subscriptionId, "next_billing_period");
    }

    private void cancelSubscription(String subscriptionId, String effectiveFrom) {
        post(
                "/subscriptions/" + subscriptionId + "/cancel",
                Map.of("effective_from", effectiveFrom),
                "cancel subscription");
    }

    private String postForRequiredText(String path, Object request, String pointer, String operation) {
        return requiredText(post(path, request, operation), pointer, operation + " response");
    }

    private String findCustomerIdByEmail(String email) {
        try {
            JsonNode response = paddleRestClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/customers")
                            .queryParam("email", email)
                            .build())
                    .retrieve()
                    .body(JsonNode.class);
            if (response == null) {
                throw new PaddleIntegrationException("Paddle returned an empty response while looking up customer");
            }
            return requiredText(response, "/data/0/id", "existing customer ID");
        } catch (RestClientException exception) {
            log.error("Failed to look up existing Paddle customer", exception);
            throw new PaddleIntegrationException("Failed to look up existing Paddle customer", exception);
        }
    }

    private boolean isCustomerAlreadyExists(PaddleIntegrationException exception) {
        return exception.getCause() instanceof HttpClientErrorException httpException
                && isCustomerAlreadyExists(httpException);
    }

    private boolean isCustomerAlreadyExists(HttpClientErrorException exception) {
        if (exception.getStatusCode() != HttpStatus.CONFLICT) {
            return false;
        }
        try {
            JsonNode response = objectMapper.readTree(exception.getResponseBodyAsString());
            return "customer_already_exists".equals(response.at("/error/code").textValue());
        } catch (JsonProcessingException parsingException) {
            log.warn("Could not parse Paddle customer conflict response", parsingException);
            return false;
        }
    }

    private JsonNode post(String path, Object request, String operation) {
        try {
            JsonNode response =
                    paddleRestClient.post().uri(path).body(request).retrieve().body(JsonNode.class);
            if (response == null) {
                throw new PaddleIntegrationException(
                        "Paddle returned an empty response while attempting to " + operation);
            }
            return response;
        } catch (RestClientException exception) {
            if (exception instanceof HttpClientErrorException httpException && isCustomerAlreadyExists(httpException)) {
                log.debug("Paddle customer already exists while attempting to {}", operation);
            } else {
                log.error("Failed to {} through Paddle", operation, exception);
            }
            throw new PaddleIntegrationException("Failed to " + operation, exception);
        }
    }

    private String requiredText(JsonNode response, String pointer, String label) {
        JsonNode value = response.at(pointer);
        if (!value.isTextual() || value.textValue().isBlank()) {
            throw new PaddleIntegrationException("Paddle response did not include " + label);
        }
        return value.textValue();
    }

    public record CheckoutTransaction(String id, String checkoutUrl) {}
}
