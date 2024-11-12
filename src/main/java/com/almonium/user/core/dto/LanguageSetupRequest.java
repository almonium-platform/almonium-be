package com.almonium.user.core.dto;

import com.almonium.analyzer.translator.model.enums.Language;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.Set;

public record LanguageSetupRequest(
        @NotEmpty @Size(max = 3) Set<Language> fluentLangs, @NotEmpty @Size(max = 3) Set<Language> targetLangs) {}
