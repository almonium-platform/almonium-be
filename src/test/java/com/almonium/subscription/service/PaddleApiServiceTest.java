package com.almonium.subscription.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.almonium.config.properties.PaddleProperties;
import com.almonium.subscription.model.entity.Plan;
import com.almonium.subscription.model.entity.enums.ProrationBillingMode;
import com.almonium.user.core.model.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

class PaddleApiServiceTest {
    private static final String LATEST_TRANSACTION_URI = "https://sandbox-api.paddle.com/transactions"
            + "?subscription_id=sub_01test&status=completed&order_by=billed_at%5BDESC%5D&per_page=1";

    private MockRestServiceServer server;
    private PaddleApiService service;

    @BeforeEach
    void setUp() {
        PaddleProperties properties = properties();
        PaddlePriceCatalog priceCatalog = new PaddlePriceCatalog(properties);
        priceCatalog.validate();
        RestClient.Builder builder = RestClient.builder()
                .baseUrl("https://sandbox-api.paddle.com")
                .defaultHeader("Authorization", "Bearer api-key")
                .defaultHeader("Paddle-Version", "1");
        server = MockRestServiceServer.bindTo(builder).build();
        service = new PaddleApiService(builder.build(), priceCatalog, new ObjectMapper());
    }

    @Test
    void reusesExistingCustomerAfterCreateConflict() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .email("existing@example.com")
                .build();
        server.expect(requestTo("https://sandbox-api.paddle.com/customers"))
                .andExpect(method(POST))
                .andRespond(
                        withStatus(HttpStatus.CONFLICT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(
                                        """
                                {"error":{"code":"customer_already_exists"}}
                                """));
        server.expect(requestTo("https://sandbox-api.paddle.com/customers?email=existing@example.com"))
                .andExpect(method(GET))
                .andRespond(withSuccess(
                        """
                        {"data":[{"id":"ctm_01existing"}]}
                        """,
                        MediaType.APPLICATION_JSON));

        assertThat(service.createCustomerIdForUser(user)).isEqualTo("ctm_01existing");
        server.verify();
    }

    @Test
    void createsFounderTransactionWithServerOwnedPriceAndUserMetadata() {
        UUID userId = UUID.fromString("01989ef4-f02f-7000-8000-000000000001");
        User user = User.builder()
                .id(userId)
                .email("founder@example.com")
                .paddleCustomerId("ctm_01founder")
                .build();
        Plan plan =
                Plan.builder().id(2L).name("PREMIUM").type(Plan.Type.MONTHLY).build();
        server.expect(requestTo("https://sandbox-api.paddle.com/transactions"))
                .andExpect(method(POST))
                .andExpect(header("Authorization", "Bearer api-key"))
                .andExpect(header("Paddle-Version", "1"))
                .andExpect(
                        content()
                                .json(
                                        """
                        {
                          "items": [{"price_id": "pri_founder_monthly", "quantity": 1}],
                          "customer_id": "ctm_01founder",
                          "custom_data": {
                            "user_id": "01989ef4-f02f-7000-8000-000000000001",
                            "plan_id": 2,
                            "founding_member_slot": 7
                          }
                        }
                        """))
                .andRespond(withSuccess(
                        """
                        {"data":{"id":"txn_01test","checkout":{"url":"https://almonium.com/payment/checkout?_ptxn=txn_01test"}}}
                        """,
                        MediaType.APPLICATION_JSON));

        PaddleApiService.CheckoutTransaction transaction = service.createPaymentTransaction(user, plan, Optional.of(7));

        assertThat(transaction.id()).isEqualTo("txn_01test");
        assertThat(transaction.checkoutUrl()).endsWith("_ptxn=txn_01test");
        server.verify();
    }

    @Test
    void readsSubscriptionSnapshotForReconciliation() {
        server.expect(requestTo("https://sandbox-api.paddle.com/subscriptions/sub_01test"))
                .andExpect(method(GET))
                .andRespond(withSuccess(
                        """
                        {
                          "data": {
                            "id": "sub_01test",
                            "status": "active",
                            "items": [{"price": {"id": "pri_regular_monthly"}}],
                            "scheduled_change": {"action": "cancel"},
                            "current_billing_period": {
                              "starts_at": "2026-08-01T00:00:00Z",
                              "ends_at": "2026-09-01T00:00:00Z"
                            }
                          }
                        }
                        """,
                        MediaType.APPLICATION_JSON));

        PaddleApiService.SubscriptionSnapshot snapshot = service.getSubscription("sub_01test");

        assertThat(snapshot.status()).isEqualTo("active");
        assertThat(snapshot.scheduledChangeAction()).contains("cancel");
        assertThat(snapshot.billingPeriodEndsAt()).contains(Instant.parse("2026-09-01T00:00:00Z"));
        server.verify();
    }

    @Test
    void previewsACadenceChangeWithoutCommittingToIt() {
        server.expect(requestTo("https://sandbox-api.paddle.com/subscriptions/sub_01test/preview"))
                .andExpect(method(PATCH))
                .andExpect(
                        content()
                                .json(
                                        """
                                {
                                  "items": [{"price_id": "pri_founder_monthly", "quantity": 1}],
                                  "proration_billing_mode": "full_next_billing_period"
                                }
                                """))
                .andRespond(withSuccess(
                        """
                        {
                          "data": {
                            "currency_code": "USD",
                            "immediate_transaction": {"details": {"totals": {"grand_total": "0"}}},
                            "recurring_transaction_details": {"totals": {"total": "800"}},
                            "current_billing_period": {"ends_at": "2027-09-04T00:00:00Z"},
                            "next_billed_at": "2027-09-04T00:00:00Z"
                          }
                        }
                        """,
                        MediaType.APPLICATION_JSON));

        PaddleApiService.CadenceChangePreview preview = service.previewCadenceChange(
                "sub_01test", "pri_founder_monthly", ProrationBillingMode.FULL_NEXT_BILLING_PERIOD);

        assertThat(preview.dueNow().minorUnits()).isZero();
        assertThat(preview.recurring().minorUnits()).isEqualTo(800L);
        assertThat(preview.recurring().currencyCode()).isEqualTo("USD");
        assertThat(preview.nextBilledAt()).contains(Instant.parse("2027-09-04T00:00:00Z"));
        server.verify();
    }

    @Test
    void readsNothingDueWhenPaddleReturnsNoImmediateTransaction() {
        server.expect(requestTo("https://sandbox-api.paddle.com/subscriptions/sub_01test/preview"))
                .andExpect(method(PATCH))
                .andRespond(withSuccess(
                        """
                        {
                          "data": {
                            "currency_code": "EUR",
                            "recurring_transaction_details": {"totals": {"total": "900"}},
                            "current_billing_period": {"ends_at": "2027-09-04T00:00:00Z"}
                          }
                        }
                        """,
                        MediaType.APPLICATION_JSON));

        PaddleApiService.CadenceChangePreview preview = service.previewCadenceChange(
                "sub_01test", "pri_founder_monthly", ProrationBillingMode.FULL_NEXT_BILLING_PERIOD);

        assertThat(preview.dueNow().minorUnits()).isZero();
        assertThat(preview.dueNow().currencyCode()).isEqualTo("EUR");
        assertThat(preview.nextBilledAt()).isEmpty();
        server.verify();
    }

    @Test
    void findsTheLatestPaymentAGuaranteeRefundWouldTarget() {
        server.expect(requestTo(LATEST_TRANSACTION_URI))
                .andExpect(method(GET))
                .andRespond(withSuccess(
                        """
                        {
                          "data": [{
                            "id": "txn_01annual",
                            "currency_code": "USD",
                            "billed_at": "2026-08-30T09:00:00Z",
                            "details": {"totals": {"grand_total": "8000"}}
                          }]
                        }
                        """,
                        MediaType.APPLICATION_JSON));

        PaddleApiService.PaidTransaction transaction =
                service.latestPaidTransaction("sub_01test").orElseThrow();

        assertThat(transaction.id()).isEqualTo("txn_01annual");
        assertThat(transaction.total().minorUnits()).isEqualTo(8000L);
        assertThat(transaction.billedAt()).isEqualTo(Instant.parse("2026-08-30T09:00:00Z"));
        server.verify();
    }

    @Test
    void reportsNoRefundableTransactionWhenTheSubscriptionHasNoneYet() {
        server.expect(requestTo(LATEST_TRANSACTION_URI))
                .andExpect(method(GET))
                .andRespond(withSuccess("{\"data\": []}", MediaType.APPLICATION_JSON));

        assertThat(service.latestPaidTransaction("sub_01test")).isEmpty();
        server.verify();
    }

    private PaddleProperties properties() {
        PaddleProperties properties = new PaddleProperties();
        properties.getPrices().setPremiumMonthly("pri_regular_monthly");
        properties.getPrices().setPremiumAnnual("pri_regular_annual");
        properties.getPrices().setFounderMonthly("pri_founder_monthly");
        properties.getPrices().setFounderAnnual("pri_founder_annual");
        return properties;
    }
}
