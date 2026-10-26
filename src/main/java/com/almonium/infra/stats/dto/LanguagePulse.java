package com.almonium.infra.stats.dto;

import java.time.Instant;
import java.util.List;

/**
 * Which language's lexicon to warm up next: per language, who learns it, who looked words up in Discover, how many of
 * those lookups the lexicon answered, and the words it most often lacked. Lookups and learners count since
 * {@code since}, and separately since the start of today's UTC day.
 */
public record LanguagePulse(Instant generatedAt, int days, Instant since, List<Line> languages) {

    /**
     * One language. {@code learners} hold it as an active language; {@code learnersSeen*} of them made an authenticated
     * request in the window, in whatever language. {@code searchers} are distinct lookers, signed in or not;
     * {@code answeredByLexicon} of the {@code lookups} were served from the lexicon, which holds {@code lemmas}.
     */
    public record Line(
            String language,
            long learners,
            long learnersSeenToday,
            long learnersSeen,
            long lookupsToday,
            long lookups,
            long searchers,
            long answeredByLexicon,
            long lemmas,
            List<Miss> topMisses) {}

    /** A word the lexicon could not answer, by how often and by how many people it was looked up. */
    public record Miss(String query, long lookups, long searchers) {}
}
