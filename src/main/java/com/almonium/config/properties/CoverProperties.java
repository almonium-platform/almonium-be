package com.almonium.config.properties;

import static lombok.AccessLevel.PRIVATE;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Cover art from Wikimedia Commons: who looks at the candidates, and how old an artist's death must be before a file
 * is taken. Without a key nothing is fetched or judged, and every book keeps its typographic cover.
 */
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "covers")
@FieldDefaults(level = PRIVATE)
public class CoverProperties {
    /** An OpenRouter key; blank turns the whole feature off. */
    String apiKey = "";

    @NotBlank
    String url = "https://openrouter.ai/api/v1/chat/completions";

    /** A vision model by its OpenRouter id. */
    @NotBlank
    String model = "anthropic/claude-sonnet-5.5";

    /**
     * A file is taken only when Commons marks its author as dead at least this long (PD-old-N) or the file as CC0.
     * Seventy years is the term where editions are made and in most of the world, and the bar the texts themselves
     * are published under; a hundred would clear every term in force anywhere and leave most books without art.
     */
    @Min(70)
    int minYearsSinceDeath = 70;

    /** How many candidates that pass the licence rule are shown to the model for one book. */
    @Min(1)
    int maxCandidates = 12;

    /** Commons asks automated clients to say who they are. */
    @NotBlank
    String userAgent = "Almonium/1.0 (https://almonium.com)";

    public boolean enabled() {
        return apiKey != null && !apiKey.isBlank();
    }
}
