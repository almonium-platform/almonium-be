package com.almonium.subscription.config;

import com.almonium.config.properties.PaddleProperties;
import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class PaddleConfig {
    @Bean
    RestClient paddleRestClient(RestClient.Builder builder, PaddleProperties properties) {
        return builder.baseUrl(properties.apiBaseUrl())
                .defaultHeader(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + properties.getApi().getKey())
                .defaultHeader("Paddle-Version", "1")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }
}
