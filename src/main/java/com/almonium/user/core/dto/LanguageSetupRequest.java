package com.almonium.user.core.dto;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.subscription.constant.PlanLimitations;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.Set;

public record LanguageSetupRequest(
        @NotEmpty @Size(max = PlanLimitations.MAX_FLUENT_LANGS) Set<Language> fluentLangs,
        @NotEmpty @Size(max = PlanLimitations.MAX_TARGET_LANGS) Set<Language> targetLangs) {}
