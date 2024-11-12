package com.almonium.analyzer.client.ngrams.client.config;

import com.almonium.analyzer.client.ngrams.client.NgramsClient;
import com.almonium.analyzer.client.ngrams.exception.NgramsApiIntegrationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;

@Configuration
public class NgramsClientConfig {

    @Value("${external.api.ngrams.url}")
    private String ngramsBaseUrl;

    @Bean
    public NgramsClient ngramsClient() {
        WebClient webClient = WebClient.builder()
                .baseUrl(ngramsBaseUrl)
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, resp -> resp.bodyToMono(String.class)
                        .flatMap(errorBody ->
                                Mono.error(new NgramsApiIntegrationException("Client error: " + errorBody))))
                .defaultStatusHandler(
                        HttpStatusCode::is5xxServerError,
                        resp -> Mono.just(new NgramsApiIntegrationException("Server error: " + resp.statusCode())))
                .build();

        return HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient))
                .build()
                .createClient(NgramsClient.class);
    }
}
