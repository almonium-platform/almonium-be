package com.almonium.analyzer.analyzer.repository;

import com.almonium.analyzer.translator.model.enums.Language;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Locale;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Every Discover word sheet opened, for the /ops languages pulse: which languages people look words up in and which
 * words the lexicon could not answer. The word is kept lowercased, as the lexicon matches it.
 */
@Repository
@RequiredArgsConstructor
public class DiscoverLookupLog {
    static final int QUERY_LENGTH = 100;

    /** Who answered the sheet: the lexicon, the old translation provider, or nobody. */
    public enum AnsweredBy {
        LEXICON,
        PROVIDER,
        NOTHING
    }

    private final JdbcTemplate jdbc;

    public void log(
            String entry,
            Language language,
            Language translationLanguage,
            AnsweredBy answeredBy,
            UUID memberId,
            String requester,
            Instant at) {
        String query = entry.toLowerCase(Locale.ROOT);
        jdbc.update(
                "insert into discover_lookup"
                        + " (language, translation_language, query, answered_by, member_id, requester, created_at)"
                        + " values (?, ?, ?, ?, ?, ?, ?)",
                language.name(),
                translationLanguage.name(),
                query.length() > QUERY_LENGTH ? query.substring(0, QUERY_LENGTH) : query,
                answeredBy.name(),
                memberId,
                requester,
                Timestamp.from(at));
    }
}
