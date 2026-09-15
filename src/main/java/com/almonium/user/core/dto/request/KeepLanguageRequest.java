package com.almonium.user.core.dto.request;

import com.almonium.analyzer.translator.model.enums.Language;
import jakarta.validation.constraints.NotNull;

/** Which language the user wants to keep active when the plan ends. */
public record KeepLanguageRequest(@NotNull Language language) {}
