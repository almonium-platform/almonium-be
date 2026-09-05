package com.almonium.subscription.service;

import com.almonium.config.properties.PaddleProperties;
import com.almonium.subscription.exception.PaddleIntegrationException;
import com.almonium.subscription.model.entity.Plan;
import com.almonium.subscription.model.entity.enums.ProrationBillingMode;
import com.almonium.user.core.model.entity.User;
import java.time.Instant;
import java.util.LinkedHashMap;
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
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaddleApiService {
    private final RestClient paddleRestClient;
    private final PaddlePriceCatalog priceCatalog;
    private final ObjectMapper objectMapper;
    private final PaddleProperties properties;

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
        Map<String, Object> request = new LinkedHashMap<>(Map.of(
                "items", List.of(Map.of("price_id", priceId, "quantity", 1)),
                "customer_id", user.getPaddleCustomerId(),
                "custom_data", customData));
        // Only when configured. Paddle enforces its approved-domains list against a URL stated here but not against
        // the account's default payment link, so stating one unconditionally breaks checkout everywhere the domain
        // has not been approved - which is every developer machine, and staging on a sandbox account.
        String checkoutUrl = properties.getCheckoutUrl();
        if (checkoutUrl != null && !checkoutUrl.isBlank()) {
            request.put("checkout", Map.of("url", checkoutUrl));
        }

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

    public SubscriptionSnapshot getSubscription(String subscriptionId) {
        try {
            JsonNode response = paddleRestClient
                    .get()
                    .uri("/subscriptions/" + subscriptionId)
                    .retrieve()
                    .body(JsonNode.class);
            if (response == null) {
                throw new PaddleIntegrationException("Paddle returned an empty subscription response");
            }
            return new SubscriptionSnapshot(
                    requiredText(response, "/data/id", "subscription ID"),
                    requiredText(response, "/data/items/0/price/id", "subscription price ID"),
                    requiredText(response, "/data/status", "subscription status"),
                    optionalText(response, "/data/scheduled_change/action"),
                    optionalInstant(response, "/data/current_billing_period/starts_at"),
                    optionalInstant(response, "/data/current_billing_period/ends_at"));
        } catch (RestClientException exception) {
            log.error("Failed to get Paddle subscription {}", subscriptionId, exception);
            throw new PaddleIntegrationException("Failed to get Paddle subscription", exception);
        }
    }

    private void cancelSubscription(String subscriptionId, String effectiveFrom) {
        post(
                "/subscriptions/" + subscriptionId + "/cancel",
                Map.of("effective_from", effectiveFrom),
                "cancel subscription");
    }

    /**
     * What the cadence change would cost, without committing to it. Every figure the confirmation screen prints comes
     * from here rather than from arithmetic of our own: whatever Paddle actually does with a billing mode, the member
     * is shown the truth about it before they confirm.
     */
    public CadenceChangePreview previewCadenceChange(String subscriptionId, String priceId, ProrationBillingMode mode) {
        JsonNode response = patch(
                "/subscriptions/" + subscriptionId + "/preview",
                cadenceChangeRequest(priceId, mode),
                "preview subscription cadence change");
        return new CadenceChangePreview(
                money(response, "/data/immediate_transaction/details/totals/grand_total", "/data/currency_code"),
                money(response, "/data/recurring_transaction_details/totals/total", "/data/currency_code"),
                money(response, "/data/update_summary/credit/amount", "/data/currency_code"),
                optionalInstant(response, "/data/current_billing_period/ends_at"),
                optionalInstant(response, "/data/next_billed_at"));
    }

    public void changeCadence(String subscriptionId, String priceId, ProrationBillingMode mode) {
        patch("/subscriptions/" + subscriptionId, cadenceChangeRequest(priceId, mode), "change subscription cadence");
    }

    /**
     * The most recent payment on this subscription, which is the one a guarantee refund targets. Paddle orders newest
     * first, so a single page of one is enough.
     */
    public Optional<PaidTransaction> latestPaidTransaction(String subscriptionId) {
        try {
            JsonNode response = paddleRestClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/transactions")
                            .queryParam("subscription_id", subscriptionId)
                            .queryParam("status", "completed")
                            .queryParam("order_by", "billed_at[DESC]")
                            .queryParam("per_page", 1)
                            .build())
                    .retrieve()
                    .body(JsonNode.class);
            if (response == null) {
                throw new PaddleIntegrationException("Paddle returned an empty transaction list");
            }
            if (!response.at("/data/0/id").isString()) {
                return Optional.empty();
            }
            return Optional.of(new PaidTransaction(
                    requiredText(response, "/data/0/id", "transaction ID"),
                    money(response, "/data/0/details/totals/grand_total", "/data/0/currency_code"),
                    optionalInstant(response, "/data/0/billed_at")
                            .orElseThrow(() ->
                                    new PaddleIntegrationException("Paddle transaction is missing its billing date"))));
        } catch (RestClientException exception) {
            log.error("Failed to list Paddle transactions for subscription {}", subscriptionId, exception);
            throw new PaddleIntegrationException("Failed to list Paddle transactions", exception);
        }
    }

    public void refundTransactionInFull(String transactionId, String reason) {
        post(
                "/adjustments",
                Map.of("action", "refund", "type", "full", "transaction_id", transactionId, "reason", reason),
                "refund transaction");
    }

    private Map<String, Object> cadenceChangeRequest(String priceId, ProrationBillingMode mode) {
        return Map.of(
                "items", List.of(Map.of("price_id", priceId, "quantity", 1)),
                "proration_billing_mode", mode.wireValue());
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
            return "customer_already_exists".equals(response.at("/error/code").stringValue(null));
        } catch (JacksonException parsingException) {
            log.warn("Could not parse Paddle customer conflict response", parsingException);
            return false;
        }
    }

    private JsonNode patch(String path, Object request, String operation) {
        try {
            JsonNode response =
                    paddleRestClient.patch().uri(path).body(request).retrieve().body(JsonNode.class);
            if (response == null) {
                throw new PaddleIntegrationException(
                        "Paddle returned an empty response while attempting to " + operation);
            }
            return response;
        } catch (RestClientException exception) {
            log.error("Failed to {} through Paddle", operation, exception);
            throw new PaddleIntegrationException("Failed to " + operation, exception);
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
        if (!value.isString() || value.stringValue().isBlank()) {
            throw new PaddleIntegrationException("Paddle response did not include " + label);
        }
        return value.stringValue();
    }

    private Optional<String> optionalText(JsonNode response, String pointer) {
        JsonNode value = response.at(pointer);
        if (value.isMissingNode() || value.isNull()) {
            return Optional.empty();
        }
        if (!value.isString() || value.stringValue().isBlank()) {
            throw new PaddleIntegrationException("Paddle response has an invalid value at " + pointer);
        }
        return Optional.of(value.stringValue());
    }

    private Optional<Instant> optionalInstant(JsonNode response, String pointer) {
        return optionalText(response, pointer).map(value -> {
            try {
                return Instant.parse(value);
            } catch (RuntimeException exception) {
                throw new PaddleIntegrationException(
                        "Paddle response has an invalid timestamp at " + pointer, exception);
            }
        });
    }

    /**
     * Paddle states every amount as a minor-unit string beside a currency code. It stays that way through our own
     * layers: rounding a currency we do not know the exponent of is how a screen ends up disagreeing with an invoice.
     */
    private Money money(JsonNode response, String amountPointer, String currencyPointer) {
        JsonNode amount = response.at(amountPointer);
        if (amount.isMissingNode() || amount.isNull()) {
            return new Money(0L, optionalText(response, currencyPointer).orElse("USD"));
        }
        String raw = amount.isString() ? amount.stringValue() : amount.asString();
        try {
            return new Money(Long.parseLong(raw), requiredText(response, currencyPointer, "currency code"));
        } catch (NumberFormatException exception) {
            throw new PaddleIntegrationException("Paddle response has a non-numeric amount at " + amountPointer);
        }
    }

    public record Money(long minorUnits, String currencyCode) {}

    /**
     * @param recurring what the subscription costs every period once the change has landed. Taken from Paddle rather
     *     than from the plan table, which holds list prices in USD and knows nothing of the member's own currency.
     */
    public record CadenceChangePreview(
            Money dueNow,
            Money recurring,
            Money credit,
            Optional<Instant> currentPeriodEndsAt,
            Optional<Instant> nextBilledAt) {}

    public record PaidTransaction(String id, Money total, Instant billedAt) {}

    public record CheckoutTransaction(String id, String checkoutUrl) {}

    public record SubscriptionSnapshot(
            String id,
            String priceId,
            String status,
            Optional<String> scheduledChangeAction,
            Optional<Instant> billingPeriodStartsAt,
            Optional<Instant> billingPeriodEndsAt) {}
}
