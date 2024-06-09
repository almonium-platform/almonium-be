package com.almonium.user.core.dto;

import com.almonium.engine.translator.model.enums.Language;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record LanguageUpdateRequest(@NotEmpty List<Language> langCodes) {}
