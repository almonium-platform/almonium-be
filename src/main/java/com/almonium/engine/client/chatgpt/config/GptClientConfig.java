package com.almonium.engine.client.chatgpt.config;

import com.almonium.auth.token.util.BearerTokenUtil;
import com.almonium.engine.client.chatgpt.client.GptClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
class GptClientConfig {

    @Value("${openai.gpt.url}")
    private String openaiUrl;

    @Value("${openai.gpt.key}")
    private String openaiApiKey;

    @Bean
    public GptClient gptClient() {
        WebClient webClient = WebClient.builder()
                .baseUrl(openaiUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, BearerTokenUtil.bearerOf(openaiApiKey))
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient))
                .build();
        return factory.createClient(GptClient.class);
    }
}
