package com.almonium.learning.lexicon.dto.response;

import com.almonium.learning.lexicon.model.enums.EntryReportReason;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * One row of the /ops reports queue: the open reports of one reason on one sense, or on the whole word when {@code
 * senseKey} is null. {@code definition} is the sense as it reads now, null when a rewrite dropped it; {@code zipf}
 * is how common the word is, which orders the queue.
 */
public record EntryReportGroup(
        String language,
        String headword,
        UUID senseKey,
        String definition,
        EntryReportReason reason,
        long reports,
        long reporters,
        BigDecimal zipf,
        int entryVersion,
        int currentVersion,
        Instant firstAt,
        Instant lastAt,
        List<Item> items) {

    /** One report in the group, newest first. */
    public record Item(String comment, String context, String book, int entryVersion, Instant createdAt) {}
}
