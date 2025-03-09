package com.almonium.user.core.dto;

import com.almonium.analyzer.analyzer.model.enums.CEFR;
import com.almonium.analyzer.translator.model.enums.Language;
import com.google.firebase.database.annotations.NotNull;

public record TargetLanguageWithProficiency(@NotNull Language language, @NotNull CEFR cefrLevel) {}
