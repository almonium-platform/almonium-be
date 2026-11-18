package com.almonium.learning.lexicon.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * One sense across every published version of its entry. {@code cards} is how many learning items were saved from
 * it. In a version, {@code sense} is the sense as published there (with its lexeme's lemma and pos), null when the
 * version no longer had it; {@code retiredTowards} is set only on the version that dropped it, and is "": an entry
 * names no successor for a sense it retires.
 */
public record SenseHistoryResponse(UUID key, long cards, List<SenseVersion> versions) {
    public record SenseVersion(
            String language,
            String headword,
            int version,
            String writer,
            Instant builtAt,
            Map<String, Object> sense,
            String retiredTowards) {}
}
