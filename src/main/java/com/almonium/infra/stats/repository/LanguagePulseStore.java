package com.almonium.infra.stats.repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * The counts behind the languages pulse, each grouped by language and keyed by the upper-case code the
 * {@code Language} constant spells; the lexicon stores it lower-case.
 */
@Repository
@RequiredArgsConstructor
public class LanguagePulseStore {
    private final NamedParameterJdbcTemplate jdbc;

    public record LearnerCounts(String language, long learners, long seenToday, long seen) {}

    public record LookupCounts(String language, long lookupsToday, long lookups, long searchers, long lexicon) {}

    public record LemmaCount(String language, long lemmas) {}

    public record MissRow(String language, String query, long lookups, long searchers) {}

    public List<LearnerCounts> learners(Instant today, Instant since) {
        return jdbc.query(
                """
                select l.language,
                       count(*) as learners,
                       count(*) filter (where p.last_seen_at >= :today) as seen_today,
                       count(*) filter (where p.last_seen_at >= :since) as seen
                from learner l left join profile p on p.id = l.user_id
                where l.active
                group by l.language
                """,
                window(today, since),
                (row, index) -> new LearnerCounts(
                        row.getString("language"),
                        row.getLong("learners"),
                        row.getLong("seen_today"),
                        row.getLong("seen")));
    }

    public List<LookupCounts> lookups(Instant today, Instant since) {
        return jdbc.query(
                """
                select language,
                       count(*) filter (where created_at >= :today) as lookups_today,
                       count(*) as lookups,
                       count(distinct requester) as searchers,
                       count(*) filter (where answered_by = 'LEXICON') as lexicon
                from discover_lookup
                where created_at >= :since
                group by language
                """,
                window(today, since),
                (row, index) -> new LookupCounts(
                        row.getString("language"),
                        row.getLong("lookups_today"),
                        row.getLong("lookups"),
                        row.getLong("searchers"),
                        row.getLong("lexicon")));
    }

    public List<LemmaCount> lemmas() {
        return jdbc.query(
                "select upper(language) as language, count(distinct lemma) as lemmas from lexicon.lexeme"
                        + " group by upper(language)",
                (row, index) -> new LemmaCount(row.getString("language"), row.getLong("lemmas")));
    }

    /** Per language, the {@code limit} words looked up most without a lexicon answer since {@code since}. */
    public List<MissRow> topMisses(Instant since, int limit) {
        return jdbc.query(
                """
                select language, query, lookups, searchers from (
                    select language, query, count(*) as lookups, count(distinct requester) as searchers,
                           row_number() over (partition by language
                                              order by count(distinct requester) desc, count(*) desc, query) as place
                    from discover_lookup
                    where created_at >= :since and answered_by <> 'LEXICON'
                    group by language, query
                ) ranked
                where place <= :limit
                order by language, place
                """,
                new MapSqlParameterSource()
                        .addValue("since", Timestamp.from(since))
                        .addValue("limit", limit),
                (row, index) -> new MissRow(
                        row.getString("language"),
                        row.getString("query"),
                        row.getLong("lookups"),
                        row.getLong("searchers")));
    }

    private static MapSqlParameterSource window(Instant today, Instant since) {
        return new MapSqlParameterSource()
                .addValue("today", Timestamp.from(today))
                .addValue("since", Timestamp.from(since));
    }
}
