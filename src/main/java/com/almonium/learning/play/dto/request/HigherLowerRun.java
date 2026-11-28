package com.almonium.learning.play.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/** A finished run: how many words were placed before the miss. */
public record HigherLowerRun(@Min(0) @Max(10_000) int length) {}
