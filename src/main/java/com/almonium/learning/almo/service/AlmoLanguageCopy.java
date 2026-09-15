package com.almonium.learning.almo.service;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.almo.dto.AlmoOpenerDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * 11: the copy that is in the target language rather than the UI language - the channel name, the composer
 * placeholder and the opener chips. They ship verbatim, so they live here as strings, not as a call to the model.
 */
@Component
public class AlmoLanguageCopy {
    private static final String NAME_TEMPLATE = "Almo · %s";
    private static final String WORD_SLOT = "{word}";

    private record Copy(String placeholder, List<String> openers) {}

    private static final Copy ENGLISH = new Copy(
            "Write in English", List.of("Tell me about your day", "A sentence with {word}", "Quiz me on my words"));

    private static final Map<Language, Copy> COPY = Map.of(
            Language.EN, ENGLISH,
            Language.DE,
                    new Copy(
                            "Schreib auf Deutsch",
                            List.of("Erzähl mir von deinem Tag", "Ein Satz mit {word}", "Frag mich meine Wörter ab")),
            Language.FR,
                    new Copy(
                            "Écris en français",
                            List.of("Raconte-moi ta journée", "Une phrase avec {word}", "Interroge-moi sur mes mots")),
            Language.ES,
                    new Copy(
                            "Escribe en español",
                            List.of("Cuéntame tu día", "Una frase con {word}", "Pregúntame mis palabras")),
            Language.IT,
                    new Copy(
                            "Scrivi in italiano",
                            List.of(
                                    "Raccontami la tua giornata",
                                    "Una frase con {word}",
                                    "Interrogami sulle mie parole")));

    /** "Almo · Deutsch": the language in its own name, never the UI language's. */
    public String channelName(Language language) {
        return String.format(NAME_TEMPLATE, ownName(language));
    }

    /** "Deutsch", "Français": what the language calls itself, capitalised for a title. */
    public String ownName(Language language) {
        Locale locale = Locale.forLanguageTag(language.name().toLowerCase(Locale.ROOT));
        String name = locale.getDisplayLanguage(locale);
        if (name.isBlank()) {
            return language.name();
        }
        return name.substring(0, 1).toUpperCase(locale) + name.substring(1);
    }

    public String placeholder(Language language) {
        return COPY.getOrDefault(language, ENGLISH).placeholder();
    }

    /**
     * Three chips at most, written in the target language. The middle one carries a queue word so the first turn
     * already speaks from the deck; without a word to offer, it is left out rather than left blank.
     */
    public List<AlmoOpenerDto> openers(Language language, Optional<String> queueWord) {
        List<AlmoOpenerDto> openers = new ArrayList<>();
        for (String template : COPY.getOrDefault(language, ENGLISH).openers()) {
            if (!template.contains(WORD_SLOT)) {
                openers.add(new AlmoOpenerDto(template, null));
            } else if (queueWord.isPresent()) {
                openers.add(new AlmoOpenerDto(template.replace(WORD_SLOT, queueWord.get()), queueWord.get()));
            }
        }
        return openers;
    }
}
