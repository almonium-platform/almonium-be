package com.almonium.analyzer.client.gemini.dto.response

import com.almonium.analyzer.client.gemini.dto.common.ContentDto
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class GeminiResponseDto(
    val candidates: List<CandidateDto>? = null,
    val promptFeedback: PromptFeedbackDto? = null,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class CandidateDto(
    val content: ContentDto? = null,
    val finishReason: String? = null,
    val index: Int? = null,
    val safetyRatings: List<SafetyRatingDto>? = null,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class SafetyRatingDto(
    val category: String?,
    val probability: String?,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class PromptFeedbackDto(
    val safetyRatings: List<SafetyRatingDto>? = null,
)
