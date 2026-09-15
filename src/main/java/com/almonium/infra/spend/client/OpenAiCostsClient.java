package com.almonium.infra.spend.client;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.client.exception.ApiIntegrationException;
import com.almonium.config.properties.OpenAiProperties;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.databind.JsonNode;

/**
 * Reads what OpenAI actually charged, a day at a time, split by project and line item. This is the invoice side of
 * the spend page; it knows nothing about which feature spent the money.
 */
@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class OpenAiCostsClient {
    /** The most daily buckets one page may carry; the API refuses more. */
    public static final int MAX_DAYS = 180;

    RestTemplate restTemplate;
    OpenAiProperties properties;

    /** One day's charge for one line item in one project. */
    public record DailyCost(LocalDate day, String projectId, String lineItem, BigDecimal usd) {}

    public List<DailyCost> dailyCosts(Instant since, int days) {
        if (days < 1 || days > MAX_DAYS) {
            throw new IllegalArgumentException("A costs window spans 1 to " + MAX_DAYS + " days");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(properties.getAdminKey());
        HttpEntity<Void> request = new HttpEntity<>(headers);

        List<DailyCost> costs = new ArrayList<>();
        String page = null;
        try {
            do {
                UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(properties.getCostsUrl())
                        .queryParam("start_time", since.getEpochSecond())
                        .queryParam("bucket_width", "1d")
                        .queryParam("group_by", "project_id", "line_item")
                        .queryParam("limit", days);
                if (page != null) {
                    uri.queryParam("page", page);
                }
                JsonNode body = restTemplate
                        .exchange(uri.toUriString(), HttpMethod.GET, request, JsonNode.class)
                        .getBody();
                if (body == null) {
                    throw new ApiIntegrationException("OpenAI answered the costs request with nothing");
                }
                body.path("data").forEach(bucket -> {
                    LocalDate day = Instant.ofEpochSecond(
                                    bucket.path("start_time").asLong())
                            .atZone(ZoneOffset.UTC)
                            .toLocalDate();
                    bucket.path("results")
                            .forEach(result -> costs.add(new DailyCost(
                                    day,
                                    result.path("project_id").asString(""),
                                    result.path("line_item").asString(""),
                                    result.path("amount").path("value").asDecimal(BigDecimal.ZERO))));
                });
                page = body.path("has_more").asBoolean(false)
                        ? body.path("next_page").asString(null)
                        : null;
            } while (page != null);
        } catch (RestClientException e) {
            throw new ApiIntegrationException("OpenAI's costs could not be read: " + e.getMessage(), e);
        }
        return costs;
    }
}
