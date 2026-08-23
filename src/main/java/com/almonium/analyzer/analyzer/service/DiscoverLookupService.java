package com.almonium.analyzer.analyzer.service;

import com.almonium.analyzer.analyzer.dto.DiscoverFrequencyResponse;
import com.almonium.analyzer.analyzer.dto.DiscoverLookupResponse;
import com.almonium.analyzer.analyzer.dto.DiscoverSenseResponse;
import com.almonium.analyzer.translator.dto.DefinitionDto;
import com.almonium.analyzer.translator.dto.TranslationCardDto;
import com.almonium.analyzer.translator.model.enums.Language;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscoverLookupService {
    private final LanguageProcessor languageProcessor;
    private final FrequencyService frequencyService;

    public DiscoverLookupResponse lookup(
            String rawEntry, String rawContext, Language language, Language translationLanguage) {
        String entry = normalizeRequired(rawEntry);
        String context = normalizeOptional(rawContext);
        TranslationCardDto translation = lookupTranslation(entry, language, translationLanguage);
        Integer score = lookupFrequency(entry, language);

        return new DiscoverLookupResponse(
                entry,
                context,
                language,
                translationLanguage,
                translation == null ? null : translation.getProvider(),
                score == null
                        ? null
                        : new DiscoverFrequencyResponse(score, frequencyBand(score), frequencyProvenance(language)),
                mapSenses(entry, translation));
    }

    private TranslationCardDto lookupTranslation(String entry, Language language, Language translationLanguage) {
        try {
            return languageProcessor.translate(entry, language, translationLanguage);
        } catch (RuntimeException exception) {
            log.warn("Discover translation unavailable for {} in {}: {}", entry, language, exception.getMessage());
            return null;
        }
    }

    private Integer lookupFrequency(String entry, Language language) {
        try {
            return frequencyService.getFrequency(language, entry).orElse(null);
        } catch (RuntimeException exception) {
            log.warn("Discover frequency unavailable for {} in {}: {}", entry, language, exception.getMessage());
            return null;
        }
    }

    private List<DiscoverSenseResponse> mapSenses(String entry, TranslationCardDto translation) {
        if (translation == null || translation.getDefinitions() == null) {
            return List.of();
        }
        DefinitionDto[] definitions = translation.getDefinitions();
        return java.util.stream.IntStream.range(0, definitions.length)
                .mapToObj(index -> mapSense(entry, index, definitions[index]))
                .toList();
    }

    private DiscoverSenseResponse mapSense(String entry, int index, DefinitionDto definition) {
        List<String> translations = definition.getTranslations() == null
                ? List.of()
                : Arrays.stream(definition.getTranslations())
                        .map(com.almonium.analyzer.translator.dto.TranslationDto::getText)
                        .filter(text -> text != null && !text.isBlank())
                        .distinct()
                        .toList();
        return new DiscoverSenseResponse(
                index + 1,
                definition.getText() == null || definition.getText().isBlank() ? entry : definition.getText(),
                definition.getPos(),
                definition.getTranscription(),
                translations);
    }

    private String normalizeRequired(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Discover entry must not be blank");
        }
        return value.strip().replaceAll("\\s+", " ");
    }

    private String normalizeOptional(String value) {
        return value == null || value.isBlank() ? null : value.strip().replaceAll("\\s+", " ");
    }

    private String frequencyBand(int score) {
        if (score >= 75) return "Very common in books";
        if (score >= 50) return "Common in books";
        if (score >= 25) return "Less common in books";
        return "Rare in books";
    }

    private String frequencyProvenance(Language language) {
        String corpus =
                switch (language) {
                    case DE -> "ger";
                    case RU -> "rus";
                    default -> "eng";
                };
        return "ngrams.dev · " + corpus + " corpus · relative frequency";
    }
}
