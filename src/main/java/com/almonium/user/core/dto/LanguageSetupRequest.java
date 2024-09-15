package com.almonium.user.core.dto;

import com.almonium.engine.translator.model.enums.Language;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

public record LanguageSetupRequest(@NotEmpty Set<Language> fluentLangs, @NotEmpty Set<Language> targetLangs) {
}
