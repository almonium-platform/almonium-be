package com.almonium.auth.oauth2.apple.config;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.oauth2.apple.client.AppleTokenClient;
import com.almonium.auth.oauth2.other.exception.OAuth2AuthenticationException;
import com.almonium.config.properties.AppProperties;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class AppleClientConfig {
    AppProperties appProperties;

    @Bean
    public AppleTokenClient appleTokenClient() {
        WebClient webClient = WebClient.builder()
                .baseUrl(appProperties.getAuth().getOauth2().getAppleTokenUrl())
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
