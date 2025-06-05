package com.almonium.config.properties

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.URL
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty
import org.springframework.validation.annotation.Validated

@ConfigurationProperties(prefix = "ai")
@Validated
data class AiProperties(
    @field:NotNull
    @field:Valid
    @NestedConfigurationProperty
    val gpt: GptProperties = GptProperties(),
    @field:NotNull
    @field:Valid
    @NestedConfigurationProperty
    val gemini: GeminiProperties = GeminiProperties(),
)

data class GptProperties(
    @field:NotBlank
    val model: String = "",
    @field:NotBlank
    @field:URL
    val url: String = "",
    @field:NotBlank
    val key: String = "",
)

data class GeminiProperties(
    @field:NotBlank
    val model: String = "",
    @field:NotBlank
    @field:URL
    val url: String = "",
    @field:NotBlank
    val key: String = "",
)
