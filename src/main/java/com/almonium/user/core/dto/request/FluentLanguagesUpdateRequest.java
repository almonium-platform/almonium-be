package com.almonium.user.core.dto.request;

import com.almonium.analyzer.translator.model.enums.Language;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

public record FluentLanguagesUpdateRequest(@NotEmpty Set<Language> langCodes) {}
