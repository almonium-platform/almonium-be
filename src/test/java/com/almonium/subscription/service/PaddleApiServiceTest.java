package com.almonium.subscription.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.almonium.config.properties.PaddleProperties;
import com.almonium.subscription.model.entity.Plan;
import com.almonium.user.core.model.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

class PaddleApiServiceTest {
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
        User user = User.builder().id(UUID.randomUUID()).email("existing@example.com").build();
        server.expect(requestTo("https://sandbox-api.paddle.com/customers"))
                .andExpect(method(POST))
                .andRespond(withStatus(HttpStatus.CONFLICT)
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

    private PaddleProperties properties() {
        PaddleProperties properties = new PaddleProperties();
        properties.getPrices().setPremiumMonthly("pri_regular_monthly");
        properties.getPrices().setPremiumAnnual("pri_regular_annual");
        properties.getPrices().setFounderMonthly("pri_founder_monthly");
        properties.getPrices().setFounderAnnual("pri_founder_annual");
        return properties;
    }
}
