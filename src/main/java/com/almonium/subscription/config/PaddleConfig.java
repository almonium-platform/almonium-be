package com.almonium.subscription.config;

import com.almonium.config.properties.PaddleProperties;
import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class PaddleConfig {
    /**
     * The request factory is pinned rather than detected because cadence changes go out as PATCH, and the
     * HttpURLConnection-backed factory detection can fall back to cannot send one. That would fail at runtime, on the
     * subscription update, and nowhere earlier.
     */
    @Bean
    RestClient paddleRestClient(RestClient.Builder builder, PaddleProperties properties) {
        return builder.requestFactory(new JdkClientHttpRequestFactory())
                .baseUrl(properties.apiBaseUrl())
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
