package com.almonium.analyzer.client.gemini.service

import com.almonium.analyzer.client.gemini.client.GeminiClient
import com.almonium.analyzer.client.gemini.dto.request.GeminiRequestDto
import com.almonium.config.properties.AiProperties
import org.springframework.stereotype.Service

@Service
class GeminiService(
    private val geminiClient: GeminiClient,
    private val aiProperties: AiProperties,
) {
    fun getGeminiContent(prompt: String): String? {
        val geminiProps = aiProperties.gemini
        val request = GeminiRequestDto.fromPrompt(prompt)

        val response =
            geminiClient.generateContent(
                modelName = geminiProps.model,
                apiKey = geminiProps.key,
                request = request,
            )

        return response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
    }
}
