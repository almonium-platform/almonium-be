package com.almonium.analyzer.client.gemini.dto.common

data class PartDto(
    val text: String,
)

data class ContentDto(
    val parts: List<PartDto>,
)
