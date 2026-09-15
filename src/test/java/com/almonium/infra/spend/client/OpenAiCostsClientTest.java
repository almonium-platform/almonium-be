package com.almonium.infra.spend.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.almonium.analyzer.client.exception.ApiIntegrationException;
import com.almonium.config.properties.OpenAiProperties;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

class OpenAiCostsClientTest {
    private static final String FIRST_PAGE = "https://api.openai.com/v1/organization/costs"
            + "?start_time=1756684800&bucket_width=1d&group_by=project_id&group_by=line_item&limit=7";

    private MockRestServiceServer server;
    private OpenAiCostsClient client;

    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        server = MockRestServiceServer.bindTo(restTemplate).build();
        OpenAiProperties properties = new OpenAiProperties();
        properties.setAdminKey("admin-key");
        client = new OpenAiCostsClient(restTemplate, properties);
    }

    @Test
    void readsEveryPageAndDatesBucketsInUtc() {
        server.expect(requestTo(FIRST_PAGE))
                .andExpect(method(GET))
                .andExpect(header("Authorization", "Bearer admin-key"))
                .andRespond(withSuccess("""
                        {"object":"page","data":[{"object":"bucket","start_time":1756684800,"end_time":1756771200,
                          "results":[
                            {"object":"organization.costs.result","amount":{"value":0.0625,"currency":"usd"},
                             "line_item":"gpt-4.1-mini, input","project_id":"proj_a"},
                            {"object":"organization.costs.result","amount":{"value":0.5,"currency":"usd"},
                             "line_item":"gpt-5.6-terra, output","project_id":"proj_b"}]}],
                         "has_more":true,"next_page":"cursor2"}
                        """, MediaType.APPLICATION_JSON));
        server.expect(requestTo(FIRST_PAGE + "&page=cursor2")).andRespond(withSuccess("""
                        {"object":"page","data":[{"object":"bucket","start_time":1756771200,"end_time":1756857600,
                          "results":[{"object":"organization.costs.result","amount":{"value":1.25,"currency":"usd"},
                             "line_item":"gpt-4.1-mini, output","project_id":null}]}],
                         "has_more":false,"next_page":null}
                        """, MediaType.APPLICATION_JSON));

        var costs = client.dailyCosts(Instant.ofEpochSecond(1756684800L), 7);

        assertThat(costs)
                .containsExactly(
                        new OpenAiCostsClient.DailyCost(
                                LocalDate.of(2025, 9, 1), "proj_a", "gpt-4.1-mini, input", new BigDecimal("0.0625")),
                        new OpenAiCostsClient.DailyCost(
                                LocalDate.of(2025, 9, 1), "proj_b", "gpt-5.6-terra, output", new BigDecimal("0.5")),
                        new OpenAiCostsClient.DailyCost(
                                LocalDate.of(2025, 9, 2), "", "gpt-4.1-mini, output", new BigDecimal("1.25")));
        server.verify();
    }

    @Test
    void aFailedReadIsAnIntegrationFailure() {
        server.expect(requestTo(FIRST_PAGE)).andRespond(withServerError());

        assertThatThrownBy(() -> client.dailyCosts(Instant.ofEpochSecond(1756684800L), 7))
                .isInstanceOf(ApiIntegrationException.class);
    }

    @Test
    void refusesAWindowTheApiWouldRefuse() {
        assertThatThrownBy(() -> client.dailyCosts(Instant.EPOCH, 181)).isInstanceOf(IllegalArgumentException.class);
    }
}
