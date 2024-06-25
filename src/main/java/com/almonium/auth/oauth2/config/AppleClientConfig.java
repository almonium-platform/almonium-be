package com.almonium.auth.oauth2.config;

import com.almonium.auth.oauth2.client.AppleTokenClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;

@Configuration
public class AppleClientConfig {

    @Value("${app.auth.oauth2.apple-token-url}")
    private String appleTokenUrl;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, resp -> resp.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new RuntimeException("Client error: " + errorBody))))
                .defaultStatusHandler(
                        HttpStatusCode::is5xxServerError,
                        resp -> Mono.just(new RuntimeException("Server error: " + resp.statusCode())))
                .baseUrl(appleTokenUrl)
                .build();
    }

    @Bean
    public HttpServiceProxyFactory httpServiceProxyFactory(WebClient webClient) {
        return HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient))
                .build();
    }

    @Bean
    public AppleTokenClient appleTokenClient(HttpServiceProxyFactory factory) {
        return factory.createClient(AppleTokenClient.class);
    }
}
