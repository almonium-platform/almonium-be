package com.almonium.analyzer.client.gemini.client

import com.almonium.analyzer.client.gemini.dto.request.GeminiRequestDto
import com.almonium.analyzer.client.gemini.dto.response.GeminiResponseDto
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.PostExchange

interface GeminiClient {
    @PostExchange("/{modelName}:generateContent")
    fun generateContent(
        @PathVariable("modelName") modelName: String,
        @RequestParam("key") apiKey: String,
        @RequestBody request: GeminiRequestDto,
    ): GeminiResponseDto
}
