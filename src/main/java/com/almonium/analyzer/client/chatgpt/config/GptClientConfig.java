package com.almonium.analyzer.client.chatgpt.config;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.client.chatgpt.client.GptClient;
import com.almonium.auth.token.util.BearerTokenUtil;
import com.almonium.config.properties.OpenAIProperties;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
class GptClientConfig {
    OpenAIProperties openAiProperties;

    @Bean
    public GptClient gptClient() {
        WebClient webClient = WebClient.builder()
                .baseUrl(openAiProperties.getGpt().getUrl())
                .defaultHeader(
                        HttpHeaders.AUTHORIZATION,
                        BearerTokenUtil.bearerOf(openAiProperties.getGpt().getKey()))
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient))
                .build();
        return factory.createClient(GptClient.class);
    }
}
