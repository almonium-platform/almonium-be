package com.almonium.analyzer.analyzer.service;

import com.almonium.analyzer.analyzer.dto.DiscoverFrequencyResponse;
import com.almonium.analyzer.analyzer.dto.DiscoverLookupResponse;
import com.almonium.analyzer.analyzer.dto.DiscoverSenseResponse;
import com.almonium.analyzer.analyzer.mapper.DiscoverLexiconMapper;
import com.almonium.analyzer.analyzer.repository.DiscoverLookupLog.AnsweredBy;
import com.almonium.analyzer.analyzer.service.DiscoverMemberLanguages.MemberLanguages;
import com.almonium.analyzer.translator.dto.DefinitionDto;
import com.almonium.analyzer.translator.dto.TranslationCardDto;
import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse;
import com.almonium.learning.lexicon.service.LexiconLookupService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Builds the Discover sheet for a word. The lexicon is asked first: when it has the word, its lexemes make the
 * sheet and the old translation provider is not called; otherwise the provider answers as before. When a standalone
 * lookup (no sentence context) misses the requested language altogether, the lexicon is checked across other languages
 * so a reader who forgot which language profile was active still gets their entry or a clear explanation instead of a
 * dead end.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DiscoverLookupService {
    private final LanguageProcessor languageProcessor;
    private final FrequencyService frequencyService;
    private final LexiconLookupService lexiconLookupService;
    private final DiscoverLexiconMapper lexiconMapper;
    private final DiscoverLookupRecorder lookupRecorder;
    private final DiscoverMemberLanguages memberLanguages;

    public DiscoverLookupResponse lookup(
            String rawEntry, String rawContext, Language language, Language translationLanguage) {
        String entry = normalizeRequired(rawEntry);
        String context = normalizeOptional(rawContext);
        boolean scriptMatches = LanguageScripts.matches(language, entry);

        DiscoverFrequencyResponse frequency = null;
        TranslationCardDto translation = null;
        List<DiscoverSenseResponse> senses = List.of();

        if (scriptMatches) {
            frequency = frequencyResponse(entry, language);
            Optional<LexiconEntryResponse> lexiconEntry = lexiconLookupService.lookup(code(language), entry);
            if (lexiconEntry.isPresent() && !lexiconEntry.get().lexemes().isEmpty()) {
                LexiconEntryResponse found = lexiconEntry.get();
                lookupRecorder.record(entry, language, translationLanguage, AnsweredBy.LEXICON);
                return lexiconResponse(
                        entry, context, language, translationLanguage, frequency, found, null, List.of());
            }

            translation = lookupTranslation(entry, language, translationLanguage);
            senses = mapSenses(entry, translation);
            if (!senses.isEmpty()) {
                lookupRecorder.record(entry, language, translationLanguage, AnsweredBy.PROVIDER);
                return new DiscoverLookupResponse(
                        entry,
                        context,
                        language,
                        translationLanguage,
                        translation == null ? null : translation.getProvider(),
                        frequency,
                        senses,
                        List.of(),
                        List.of());
            }
        }

        if (context == null) {
            Optional<DiscoverLookupResponse> redirected =
                    resolveCrossLanguageMiss(entry, language, translationLanguage, frequency, scriptMatches);
            if (redirected.isPresent()) {
                return redirected.get();
            }
        }

        if (scriptMatches) {
            lookupRecorder.record(entry, language, translationLanguage, AnsweredBy.NOTHING);
        }
        return new DiscoverLookupResponse(
                entry,
                context,
                language,
                translationLanguage,
                translation == null ? null : translation.getProvider(),
                frequency,
                senses,
                List.of(),
                List.of(),
                List.of(),
                null,
                List.of(),
                !scriptMatches);
    }

    private Optional<DiscoverLookupResponse> resolveCrossLanguageMiss(
            String entry,
            Language requestedLanguage,
            Language requestedTranslationLanguage,
            DiscoverFrequencyResponse requestedFrequency,
            boolean scriptMatches) {
        List<Language> matches = toLanguages(lexiconLookupService.matchingLanguages(code(requestedLanguage), entry));
        if (matches.isEmpty()) {
            return Optional.empty();
        }

        MemberLanguages member = memberLanguages.current();
        List<Language> redirectCandidates = member.signedIn()
                ? matches.stream().filter(member.activeTargets()::contains).toList()
                : matches;

        if (!redirectCandidates.isEmpty()) {
            Language resolved = redirectCandidates.getFirst();
            List<Language> others = List.copyOf(redirectCandidates.subList(1, redirectCandidates.size()));
            Language resolvedTranslation =
                    resolveTranslationLanguage(resolved, requestedTranslationLanguage, member.fluent());
            Optional<LexiconEntryResponse> found = lexiconLookupService.lookup(code(resolved), entry);
            if (found.isPresent() && !found.get().lexemes().isEmpty()) {
                lookupRecorder.record(entry, resolved, resolvedTranslation, AnsweredBy.LEXICON);
                return Optional.of(lexiconResponse(
                        entry,
                        null,
                        resolved,
                        resolvedTranslation,
                        frequencyResponse(entry, resolved),
                        found.get(),
                        requestedLanguage,
                        others));
            }
        }

        List<Language> orderedAvailable = new ArrayList<>(matches);
        orderedAvailable.sort(
                Comparator.comparing((Language candidate) -> !member.fluent().contains(candidate)));
        return Optional.of(new DiscoverLookupResponse(
                entry,
                null,
                requestedLanguage,
                requestedTranslationLanguage,
                null,
                requestedFrequency,
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                null,
                List.copyOf(orderedAvailable),
                !scriptMatches));
    }

    private DiscoverLookupResponse lexiconResponse(
            String entry,
            String context,
            Language language,
            Language translationLanguage,
            DiscoverFrequencyResponse frequency,
            LexiconEntryResponse found,
            Language requestedLanguage,
            List<Language> availableLanguages) {
        return new DiscoverLookupResponse(
                entry,
                context,
                language,
                translationLanguage,
                DiscoverLexiconMapper.PROVIDER,
                frequency,
                lexiconMapper.toSenses(found, code(translationLanguage)),
                lexiconMapper.toPronunciations(found),
                lexiconMapper.toHomophones(found),
                lexiconMapper.toLexemes(found),
                requestedLanguage,
                availableLanguages,
                false);
    }

    private static Language resolveTranslationLanguage(
            Language resolvedLanguage, Language requestedTranslationLanguage, Set<Language> fluentLanguages) {
        if (requestedTranslationLanguage != resolvedLanguage) {
            return requestedTranslationLanguage;
        }
        return fluentLanguages.stream()
                .filter(fluent -> fluent != resolvedLanguage)
                .min(Comparator.comparing(Enum::name))
                .orElse(resolvedLanguage == Language.EN ? Language.UK : Language.EN);
    }

    private static List<Language> toLanguages(List<String> codes) {
        List<Language> languages = new ArrayList<>(codes.size());
        for (String raw : codes) {
            if (raw == null || raw.isBlank()) {
                continue;
            }
            try {
                languages.add(Language.valueOf(raw.trim().toUpperCase(Locale.ROOT)));
            } catch (IllegalArgumentException ignored) {
                // Unknown language code in lexicon table: skip rather than fail the lookup.
            }
        }
        return languages;
    }

    /** The lexicon keys languages by the lowercase code the {@link Language} constant spells. */
    private static String code(Language language) {
        return language.name().toLowerCase(Locale.ROOT);
    }

    private DiscoverFrequencyResponse frequencyResponse(String entry, Language language) {
        Integer score = lookupFrequency(entry, language);
        return score == null
                ? null
                : new DiscoverFrequencyResponse(score, frequencyBand(score), frequencyProvenance(language));
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
                null,
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
