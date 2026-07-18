package com.almonium.analyzer.client.chatgpt.config

import com.almonium.analyzer.client.chatgpt.client.GptClient
import com.almonium.config.properties.AiProperties
import com.almonium.util.http.BearerTokenUtil.bearerOf
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.support.WebClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Configuration
class GptClientConfig(private val aiProperties: AiProperties) {
    @Bean
    fun gptClient(): GptClient {
        val gptProps = aiProperties.gpt
        val webClient =
            WebClient.builder()
                .baseUrl(gptProps.url)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, bearerOf(gptProps.key))
                .build()

        val factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient)).build()
        return factory.createClient(GptClient::class.java)
    }
}
