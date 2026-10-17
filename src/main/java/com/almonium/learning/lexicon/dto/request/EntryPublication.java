package com.almonium.learning.lexicon.dto.request;

import com.almonium.learning.lexicon.model.EntryDocument;
import java.time.Instant;
import java.util.Map;

/**
 * One entry as a writer publishes it: the entry itself, the writer's name (cc-claude-opus-5-5), its metadata record
 * (model, prompt version, cost, tokens) and when it was built. {@code schemaVersion} is the version of the entry
 * schema the body passed.
 */
public record EntryPublication(
        String writer, EntryDocument entry, Map<String, Object> meta, Instant builtAt, String schemaVersion) {

    public Map<String, Object> meta() {
        return meta == null ? Map.of() : meta;
    }
}
