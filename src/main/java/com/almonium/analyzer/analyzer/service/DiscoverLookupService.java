package com.almonium.analyzer.analyzer.service;

import com.almonium.analyzer.analyzer.dto.DiscoverFrequencyResponse;
import com.almonium.analyzer.analyzer.dto.DiscoverLookupResponse;
import com.almonium.analyzer.analyzer.dto.DiscoverSenseResponse;
import com.almonium.analyzer.analyzer.mapper.DiscoverLexiconMapper;
import com.almonium.analyzer.translator.dto.DefinitionDto;
import com.almonium.analyzer.translator.dto.TranslationCardDto;
import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse;
import com.almonium.learning.lexicon.service.LexiconLookupService;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Builds the Discover sheet for a word. The lexicon is asked first: when it has the word, its lexemes make the
 * sheet and the old translation provider is not called; otherwise the provider answers as before. Frequency comes
 * from ngrams either way. The route is public, so nothing here looks at a user.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DiscoverLookupService {
    private final LanguageProcessor languageProcessor;
    private final FrequencyService frequencyService;
    private final LexiconLookupService lexiconLookupService;
    private final DiscoverLexiconMapper lexiconMapper;

    public DiscoverLookupResponse lookup(
            String rawEntry, String rawContext, Language language, Language translationLanguage) {
        String entry = normalizeRequired(rawEntry);
        String context = normalizeOptional(rawContext);
        Integer score = lookupFrequency(entry, language);
        DiscoverFrequencyResponse frequency = score == null
                ? null
                : new DiscoverFrequencyResponse(score, frequencyBand(score), frequencyProvenance(language));

        Optional<LexiconEntryResponse> lexiconEntry = lexiconLookupService.lookup(code(language), entry);
        if (lexiconEntry.isPresent() && !lexiconEntry.get().lexemes().isEmpty()) {
            LexiconEntryResponse found = lexiconEntry.get();
            return new DiscoverLookupResponse(
                    entry,
                    context,
                    language,
                    translationLanguage,
                    DiscoverLexiconMapper.PROVIDER,
                    frequency,
                    lexiconMapper.toSenses(found, code(translationLanguage)),
                    lexiconMapper.toPronunciations(found));
        }

        TranslationCardDto translation = lookupTranslation(entry, language, translationLanguage);
        return new DiscoverLookupResponse(
                entry,
                context,
                language,
                translationLanguage,
                translation == null ? null : translation.getProvider(),
                frequency,
                mapSenses(entry, translation),
                List.of());
    }

    /** The lexicon keys languages by the lowercase code the {@link Language} constant spells. */
    private static String code(Language language) {
        return language.name().toLowerCase(Locale.ROOT);
    }

    private TranslationCardDto lookupTranslation(String entry, Language language, Language translationLanguage) {
        try {
            return languageProcessor.translate(entry, language, translationLanguage);
        } catch (RuntimeException exception) {
            log.warn(
                    "Discover translation unavailable for {} in {}: {}",
                    loggable(entry),
                    language,
                    exception.getMessage());
            return null;
        }
    }

    private Integer lookupFrequency(String entry, Language language) {
        try {
            return frequencyService.getFrequency(language, entry).orElse(null);
        } catch (RuntimeException exception) {
            log.warn(
                    "Discover frequency unavailable for {} in {}: {}",
                    loggable(entry),
                    language,
                    exception.getMessage());
            return null;
        }
    }

    /** The entry is typed by the visitor; a line break in it must not forge a second log line. */
    private static String loggable(String entry) {
        return entry.replaceAll("[\\r\\n]", " ");
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
                translations,
                null,
                null,
                List.of());
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
