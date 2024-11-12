package com.almonium.user.core.dto;

import com.almonium.analyzer.translator.model.enums.Language;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

public record LanguageUpdateRequest(@NotEmpty Set<Language> langCodes) {}
