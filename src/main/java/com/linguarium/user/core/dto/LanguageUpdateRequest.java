package com.linguarium.user.core.dto;

import com.linguarium.engine.translator.model.Language;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record LanguageUpdateRequest(@NotEmpty List<Language> langCodes) {
}
