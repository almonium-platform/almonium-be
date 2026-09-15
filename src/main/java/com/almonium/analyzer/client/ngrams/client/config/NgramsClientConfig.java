package com.almonium.analyzer.client.ngrams.client.config;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.client.ngrams.client.NgramsClient;
import com.almonium.analyzer.client.ngrams.exception.NgramsApiIntegrationException;
import com.almonium.config.properties.ExternalApiProperties;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.netty.channel.ChannelOption;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.util.retry.Retry;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class NgramsClientConfig {
    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(5);
    private static final Duration RESPONSE_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration INITIAL_RETRY_BACKOFF = Duration.ofMillis(200);
    private static final int MAX_RETRIES = 2;
    private static final int MAX_RESPONSE_BYTES = 256 * 1024;

    ExternalApiProperties externalApiProperties;
    MeterRegistry meterRegistry;

    @Bean
    public NgramsClient ngramsClient() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, Math.toIntExact(CONNECT_TIMEOUT.toMillis()))
                .responseTimeout(RESPONSE_TIMEOUT);

        WebClient webClient = WebClient.builder()
                .baseUrl(externalApiProperties.getNgrams().getUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(MAX_RESPONSE_BYTES))
                .filter(this::recordMetrics)
                .filter(this::retryTransientFailures)
                .filter(this::mapProviderStatus)
                .filter(this::mapTransportFailure)
                .build();

        NgramsClient client = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient))
                .build()
                .createClient(NgramsClient.class);
        return (corpus, query) -> {
            try {
                return client.searchWord(corpus, query);
            } catch (NgramsApiIntegrationException exception) {
                throw exception;
            } catch (RuntimeException exception) {
                throw new NgramsApiIntegrationException("Ngrams service is temporarily unavailable", true, exception);
            }
        };
    }

    Mono<org.springframework.web.reactive.function.client.ClientResponse> recordMetrics(
            org.springframework.web.reactive.function.client.ClientRequest request,
            org.springframework.web.reactive.function.client.ExchangeFunction next) {
        Timer.Sample sample = Timer.start(meterRegistry);
        return next.exchange(request)
                .doOnSuccess(ignored -> sample.stop(
                        meterRegistry.timer("external.client.request", "client", "ngrams", "outcome", "success")))
                .doOnError(ignored -> sample.stop(
                        meterRegistry.timer("external.client.request", "client", "ngrams", "outcome", "failure")));
    }

    Mono<org.springframework.web.reactive.function.client.ClientResponse> retryTransientFailures(
            org.springframework.web.reactive.function.client.ClientRequest request,
            org.springframework.web.reactive.function.client.ExchangeFunction next) {
        return Mono.defer(() -> next.exchange(request))
                .retryWhen(Retry.backoff(MAX_RETRIES, INITIAL_RETRY_BACKOFF)
                        .maxBackoff(Duration.ofSeconds(1))
                        .filter(error ->
                                error instanceof NgramsApiIntegrationException exception && exception.isRetryable())
                        .onRetryExhaustedThrow((specification, signal) -> signal.failure()));
    }

    Mono<org.springframework.web.reactive.function.client.ClientResponse> mapProviderStatus(
            org.springframework.web.reactive.function.client.ClientRequest request,
            org.springframework.web.reactive.function.client.ExchangeFunction next) {
        return next.exchange(request).flatMap(response -> {
            HttpStatusCode status = response.statusCode();
            if (status.value() == 429 || status.is5xxServerError()) {
                return response.releaseBody()
                        .then(Mono.error(new NgramsApiIntegrationException(
                                "Ngrams service is temporarily unavailable", true, null)));
            }
            if (status.is4xxClientError()) {
                return response.releaseBody()
                        .then(Mono.error(
                                new NgramsApiIntegrationException("Ngrams request was rejected", false, null)));
            }
            return Mono.just(response);
        });
    }

    Mono<org.springframework.web.reactive.function.client.ClientResponse> mapTransportFailure(
            org.springframework.web.reactive.function.client.ClientRequest request,
            org.springframework.web.reactive.function.client.ExchangeFunction next) {
        return next.exchange(request)
                .onErrorMap(
                        error -> !(error instanceof NgramsApiIntegrationException),
                        error -> new NgramsApiIntegrationException(
                                "Ngrams service is temporarily unavailable", true, error));
    }
}
