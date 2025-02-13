package com.almonium.user.core.dto.request;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.subscription.constant.AppLimits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Set;

public record LanguageSetupRequest(
        @NotEmpty @Size(max = AppLimits.MAX_FLUENT_LANGS) Set<Language> fluentLangs,
        @NotEmpty List<TargetLanguageWithProficiency> targetLangsData) {}
