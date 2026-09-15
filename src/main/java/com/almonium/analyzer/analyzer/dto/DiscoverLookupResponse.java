package com.almonium.analyzer.analyzer.dto;

import com.almonium.analyzer.translator.model.enums.Language;
import java.util.List;

public record DiscoverLookupResponse(
        String entry,
        String sourceContext,
        Language language,
        Language translationLanguage,
        String provider,
        DiscoverFrequencyResponse frequency,
        List<DiscoverSenseResponse> senses) {}
