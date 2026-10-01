package com.almonium.analyzer.translator.dto;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.analyzer.translator.model.enums.LanguageVariety;
import com.almonium.analyzer.translator.service.VoiceCatalogue;

/** Configuration availability, not a live provider health check. */
public record VoiceAvailabilityDto(
        Language language,
        LanguageVariety variety,
        boolean defaultVariety,
        boolean available,
        String unavailableReason,
        VoiceCatalogue.Provider provider,
        String languageCode,
        String voiceId,
        VoiceCatalogue.Gender gender) {}
