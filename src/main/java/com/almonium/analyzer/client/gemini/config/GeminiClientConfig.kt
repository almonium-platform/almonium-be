package com.almonium.analyzer.client.gemini.config

import com.almonium.analyzer.client.gemini.client.GeminiClient
import com.almonium.config.properties.AiProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.support.WebClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Configuration
class GeminiClientConfig(private val aiProperties: AiProperties) {
    @Bean
    fun geminiClient(): GeminiClient {
        val geminiProps = aiProperties.gemini
        val webClient =
            WebClient.builder()
                .baseUrl(geminiProps.url)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build()

        val factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient)).build()
        return factory.createClient(GeminiClient::class.java)
    }
}
