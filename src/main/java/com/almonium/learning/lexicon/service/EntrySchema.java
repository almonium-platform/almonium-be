package com.almonium.learning.lexicon.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * The writers' entry schema (version 2, draft 2020-12), the same file the research scripts validate against. A body
 * that does not pass it is refused before anything is written, with the schema's own message naming the field.
 */
@Component
public class EntrySchema {
    public static final String VERSION = "2";
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
        if (messages.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(
                messages.stream().map(ValidationMessage::getMessage).sorted().collect(Collectors.joining("; ")));
    }
}
