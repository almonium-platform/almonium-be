package com.linguarium.user.dto;

import com.linguarium.translator.model.Language;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record LanguageUpdateRequest(@NotEmpty List<Language> langCodes) {
}
