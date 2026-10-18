package com.almonium.learning.lexicon.dto.request;

import java.math.BigDecimal;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

/**
 * One clip as the audio phase hands it over, bytes and all: the rendering ({@code engine, voice, model, pace,
 * format}), the exact text the engine was given, the verifier's verdict ({@code status}, with {@code transcript} when
 * it heard something else), what it cost in the engine's characters, and the MP3 as base64. Snake case on the wire,
 * as the Python side writes it.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ClipSubmission(
        String engine,
        String voice,
        String model,
        BigDecimal pace,
        String format,
        String engineInput,
        String status,
        Integer durationMs,
        String transcript,
        BigDecimal characterCost,
        String audioBase64) {}
