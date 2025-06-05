package com.almonium.analyzer.client.gemini.dto.request

import com.almonium.analyzer.client.gemini.dto.common.ContentDto
import com.almonium.analyzer.client.gemini.dto.common.PartDto

data class GeminiRequestDto(
    val contents: List<ContentDto>,
) {
    companion object {
        @JvmStatic
        fun fromPrompt(prompt: String): GeminiRequestDto {
            return GeminiRequestDto(listOf(ContentDto(listOf(PartDto(prompt)))))
        }
    }
}
