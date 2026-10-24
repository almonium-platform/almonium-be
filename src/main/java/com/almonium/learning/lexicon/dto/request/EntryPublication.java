package com.almonium.learning.lexicon.dto.request;

import com.almonium.learning.lexicon.model.EntryDocument;
import java.time.Instant;
import java.util.Map;

/**
 * One entry as a writer publishes it: the entry itself, the writer's name (cc-claude-opus-5-5), its metadata record
 * (model, prompt version, cost, tokens) and when it was built. {@code schemaVersion} is the version of the entry
 * schema the body passed. {@code zipf} is how often the headword's spelling occurs, on the Zipf scale (log10 of uses
 * per billion words) from wordfreq; null when the publisher did not measure it.
 */
public record EntryPublication(
        String writer,
        EntryDocument entry,
        Map<String, Object> meta,
        Instant builtAt,
        String schemaVersion,
        Double zipf) {

    public Map<String, Object> meta() {
        return meta == null ? Map.of() : meta;
    }
}
