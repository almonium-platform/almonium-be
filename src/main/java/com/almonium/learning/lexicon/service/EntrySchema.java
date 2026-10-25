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
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * The writers' entry schema (version 3, draft 2020-12), the same file the research scripts validate against. A body
 * that does not pass it is refused before anything is written, with the schema's own message naming the field. What
 * the schema cannot say is checked after it: a transcription's variety is one the product's catalogue lists for the
 * entry's language ({@link LanguageVariety}), and a region is that language with a country or a UN M.49 area
 * ({@link #UN_REGIONS}), so a word can be Canarian or Andean without the product offering the accent.
 */
@Component
public class EntrySchema {
    public static final String VERSION = "3";

    /** The UN M.49 areas a region may name besides a country; the writer's prompting.py lists the same. */
    public static final Set<String> UN_REGIONS = Set.of("419", "029", "005", "013", "021", "150", "002", "142", "009");

    private static final Set<String> COUNTRIES = Set.of(Locale.getISOCountries());
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
        List<String> strays = strayPlaces(entry);
        return strays.isEmpty() ? Optional.empty() : Optional.of(String.join("; ", strays));
    }

    /** Each transcription variety the catalogue lacks and each region outside the language, with where it stands. */
    private static List<String> strayPlaces(JsonNode entry) {
        String code = entry.path("language").asText();
        Set<String> known = Arrays.stream(Language.values())
                .filter(language -> language.name().equalsIgnoreCase(code))
                .flatMap(language -> LanguageVariety.forLanguage(language).stream())
                .map(LanguageVariety::getTag)
                .collect(Collectors.toSet());
        Map<String, String> named = new LinkedHashMap<>();
        Map<String, String> regions = new LinkedHashMap<>();
        JsonNode lexemes = entry.path("lexemes");
        for (int l = 0; l < lexemes.size(); l++) {
            JsonNode lexeme = lexemes.get(l);
            String at = "/lexemes/" + l;
            regions(lexeme, at, regions);
            JsonNode spoken = lexeme.path("pronunciations");
            for (int p = 0; p < spoken.size(); p++) {
                named.put(
                        at + "/pronunciations/" + p,
                        spoken.get(p).path("variety").asText());
            }
            JsonNode forms = lexeme.path("forms");
            for (int f = 0; f < forms.size(); f++) {
                regions(forms.get(f), at + "/forms/" + f, regions);
                for (Iterator<String> varieties = forms.get(f).path("ipa").fieldNames(); varieties.hasNext(); ) {
                    String variety = varieties.next();
                    named.put(at + "/forms/" + f + "/ipa/" + variety, variety);
                }
            }
            JsonNode senses = lexeme.path("senses");
            for (int s = 0; s < senses.size(); s++) {
                regions(senses.get(s), at + "/senses/" + s, regions);
            }
        }
        Stream<String> varieties = named.entrySet().stream()
                .filter(place -> !known.contains(place.getValue()))
                .map(place -> place.getKey() + ": " + place.getValue() + " is not a variety of " + code);
        Stream<String> places = regions.entrySet().stream()
                .filter(place -> !isRegionOf(code, place.getValue()))
                .map(place -> place.getKey() + ": " + place.getValue() + " is not " + code
                        + " with a country or a UN region");
        return Stream.concat(varieties, places).toList();
    }

    private static boolean isRegionOf(String language, String region) {
        String area = region.startsWith(language + "-") ? region.substring(language.length() + 1) : "";
        return COUNTRIES.contains(area) || UN_REGIONS.contains(area);
    }

    private static void regions(JsonNode scoped, String at, Map<String, String> named) {
        JsonNode regions = scoped.path("regions");
        for (int r = 0; r < regions.size(); r++) {
            named.put(at + "/regions/" + r, regions.get(r).path("variety").asText());
        }
    }
}
