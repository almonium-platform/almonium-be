package com.almonium.auth.oauth2.apple.config;

import com.almonium.auth.oauth2.apple.client.AppleTokenClient;
import com.almonium.auth.oauth2.other.exception.OAuth2AuthenticationException;
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
    public AppleTokenClient appleTokenClient() {
        WebClient webClient = WebClient.builder()
                .baseUrl(appleTokenUrl)
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, resp -> resp.bodyToMono(String.class)
                        .flatMap(errorBody ->
                                Mono.error(new OAuth2AuthenticationException("Client error: " + errorBody))))
                .defaultStatusHandler(
                        HttpStatusCode::is5xxServerError,
                        resp -> Mono.just(new OAuth2AuthenticationException("Server error: " + resp.statusCode())))
                .build();

        return HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient))
                .build()
                .createClient(AppleTokenClient.class);
    }
}
