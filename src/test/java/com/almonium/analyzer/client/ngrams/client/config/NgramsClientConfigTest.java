package com.almonium.analyzer.client.ngrams.client.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.almonium.analyzer.client.ngrams.exception.NgramsApiIntegrationException;
import com.almonium.config.properties.ExternalApiProperties;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import java.net.URI;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

class NgramsClientConfigTest {
    private SimpleMeterRegistry meterRegistry;
    private NgramsClientConfig config;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        config = new NgramsClientConfig(new ExternalApiProperties(), meterRegistry);
    }

    @AfterEach
    void tearDown() {
        meterRegistry.close();
    }

    @Test
    void givenTransientFailure_whenRequestingNgrams_thenRetriesTwiceBeforeSucceeding() {
        AtomicInteger attempts = new AtomicInteger();

        ClientResponse response = config.retryTransientFailures(request(), ignored -> {
                    if (attempts.incrementAndGet() < 3) {
                        return Mono.error(new NgramsApiIntegrationException("unavailable", true, null));
                    }
                    return Mono.just(ClientResponse.create(HttpStatus.OK).build());
                })
                .block();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(attempts).hasValue(3);
    }

    @Test
    void givenRejectedProviderRequest_whenRequestingNgrams_thenMapsToStableIntegrationFailure() {
        assertThatThrownBy(() -> config.mapProviderStatus(
                                request(),
                                ignored -> Mono.just(ClientResponse.create(HttpStatus.BAD_REQUEST)
                                        .build()))
                        .block())
                .isInstanceOf(NgramsApiIntegrationException.class)
                .hasMessage("Ngrams request was rejected");
    }

    @Test
    void givenSuccessfulRequest_whenRecordingMetrics_thenRecordsClientTimer() {
        config.recordMetrics(
                        request(),
                        ignored ->
                                Mono.just(ClientResponse.create(HttpStatus.OK).build()))
                .block();

        assertThat(meterRegistry
                        .find("external.client.request")
                        .tags("client", "ngrams", "outcome", "success")
                        .timer())
                .isNotNull();
    }

    private ClientRequest request() {
        return ClientRequest.create(HttpMethod.GET, URI.create("https://ngrams.example.test/search"))
                .build();
    }
}
