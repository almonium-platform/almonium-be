package com.almonium.learning.lexicon.service;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.analyzer.translator.model.enums.LanguageVariety;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * The writers' entry schema (version 3, draft 2020-12), the same file the research scripts validate against. A body
 * that does not pass it is refused before anything is written, with the schema's own message naming the field. What
 * the schema cannot say is checked after it: every variety the entry names, a transcription's or a region's, is one
 * the product's catalogue lists for the entry's language ({@link LanguageVariety}).
 */
@Component
public class EntrySchema {
    public static final String VERSION = "3";
    private static final String LOCATION = "lexicon/entry.schema.json";

    private final JsonSchema schema;

    /** The validator reads Jackson 2 trees, so the entry is parsed once more with its own mapper. */
    private final ObjectMapper trees = new ObjectMapper();

    public EntrySchema() {
        try (InputStream source = new ClassPathResource(LOCATION).getInputStream()) {
            schema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012)
                    .getSchema(source);
        } catch (IOException exception) {
            throw new UncheckedIOException("Cannot read " + LOCATION, exception);
        }
    }

    /** Empty when the entry passes; otherwise every violation, sorted, on one line. */
    public Optional<String> violations(byte[] entryJson) {
        JsonNode entry;
        try {
            entry = trees.readTree(entryJson);
        } catch (IOException exception) {
            return Optional.of("entry is not JSON: " + exception.getMessage());
        }
        Set<ValidationMessage> messages = schema.validate(entry);
        if (!messages.isEmpty()) {
            return Optional.of(messages.stream()
                    .map(ValidationMessage::getMessage)
                    .sorted()
                    .collect(Collectors.joining("; ")));
        }
        List<String> strays = strayVarieties(entry);
        return strays.isEmpty() ? Optional.empty() : Optional.of(String.join("; ", strays));
    }

    /** Each variety named in the entry that its language's catalogue lacks, with where it stands. */
    private static List<String> strayVarieties(JsonNode entry) {
        String code = entry.path("language").asText();
        Set<String> known = Arrays.stream(Language.values())
                .filter(language -> language.name().equalsIgnoreCase(code))
                .flatMap(language -> LanguageVariety.forLanguage(language).stream())
                .map(LanguageVariety::getTag)
                .collect(Collectors.toSet());
        Map<String, String> named = new LinkedHashMap<>();
        JsonNode lexemes = entry.path("lexemes");
        for (int l = 0; l < lexemes.size(); l++) {
            JsonNode lexeme = lexemes.get(l);
            String at = "/lexemes/" + l;
            regions(lexeme, at, named);
            JsonNode spoken = lexeme.path("pronunciations");
            for (int p = 0; p < spoken.size(); p++) {
                named.put(
                        at + "/pronunciations/" + p,
                        spoken.get(p).path("variety").asText());
            }
            JsonNode forms = lexeme.path("forms");
            for (int f = 0; f < forms.size(); f++) {
                regions(forms.get(f), at + "/forms/" + f, named);
                for (Iterator<String> varieties = forms.get(f).path("ipa").fieldNames(); varieties.hasNext(); ) {
                    String variety = varieties.next();
                    named.put(at + "/forms/" + f + "/ipa/" + variety, variety);
                }
            }
            JsonNode senses = lexeme.path("senses");
            for (int s = 0; s < senses.size(); s++) {
                regions(senses.get(s), at + "/senses/" + s, named);
            }
        }
        return named.entrySet().stream()
                .filter(place -> !known.contains(place.getValue()))
                .map(place -> place.getKey() + ": " + place.getValue() + " is not a variety of " + code)
                .toList();
    }

    private static void regions(JsonNode scoped, String at, Map<String, String> named) {
        JsonNode regions = scoped.path("regions");
        for (int r = 0; r < regions.size(); r++) {
            named.put(at + "/regions/" + r, regions.get(r).path("variety").asText());
        }
    }
}
