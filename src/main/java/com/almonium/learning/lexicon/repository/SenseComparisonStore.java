package com.almonium.learning.lexicon.repository;

import com.almonium.learning.lexicon.dto.SenseComparisonSpendLine;
import com.github.f4b6a3.uuid.UuidCreator;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * The sense_comparison table, and the facts of a sense as the model is given them. The jsonb columns are handed
 * over as the text the lexicon stores, since they are only ever quoted to the model.
 */
@Repository
@RequiredArgsConstructor
public class SenseComparisonStore {
    private final NamedParameterJdbcTemplate jdbc;

    /** One sense with the word it belongs to; a null field is one the lexicon says nothing in. */
    public record SenseFacts(
            UUID senseKey,
            String language,
            String headword,
            String partOfSpeech,
            String wordGrammar,
            String definition,
            String cefr,
            String labels,
            String regions,
            String grammar,
            String frames,
            String collocations,
            String antonyms,
            String standardAlternative,
            List<String> examples) {}

    public record NewComparison(
            String contentHash,
            UUID firstSenseKey,
            UUID secondSenseKey,
            String explanationLanguage,
            String summary,
            String model,
            int promptTokens,
            int completionTokens,
            UUID requestedBy) {}

    public Optional<SenseFacts> facts(UUID senseKey) {
        MapSqlParameterSource key = new MapSqlParameterSource("key", senseKey);
        List<String> examples = jdbc.queryForList("""
                select e.text from lexicon.sense_example e join lexicon.sense s on s.id = e.sense_id
                where s.sense_key = :key order by e.position limit 2
                """, key, String.class);
        return jdbc
                .query(
                        """
                        select l.language, l.headword, l.pos, nullif(l.grammar::text, '{}') as word_grammar,
                               s.definition, s.cefr, s.labels::text as labels, s.regions::text as regions,
                               s.grammar::text as grammar, s.frames::text as frames,
                               s.collocations::text as collocations, s.antonyms::text as antonyms,
                               s.standard_alternative
                        from lexicon.sense s join lexicon.lexeme l on l.id = s.lexeme_id
                        where s.sense_key = :key
                        """,
                        key,
                        (row, index) -> new SenseFacts(
                                senseKey,
                                row.getString("language"),
                                row.getString("headword"),
                                row.getString("pos"),
                                row.getString("word_grammar"),
                                row.getString("definition"),
                                row.getString("cefr"),
                                row.getString("labels"),
                                row.getString("regions"),
                                row.getString("grammar"),
                                row.getString("frames"),
                                row.getString("collocations"),
                                row.getString("antonyms"),
                                row.getString("standard_alternative"),
                                examples))
                .stream()
                .findFirst();
    }

    public Optional<String> summary(String contentHash) {
        return jdbc
                .queryForList(
                        "select summary from lexicon.sense_comparison where content_hash = :hash",
                        new MapSqlParameterSource("hash", contentHash),
                        String.class)
                .stream()
                .findFirst();
    }

    /** Two requests for one pair may both reach the model; the first row stands and the second is dropped. */
    public void insert(NewComparison comparison, Instant now) {
        jdbc.update(
                """
                insert into lexicon.sense_comparison (id, content_hash, first_sense_key, second_sense_key,
                    explanation_language, summary, model, prompt_tokens, completion_tokens, requested_by, created_at)
                values (:id, :hash, :first, :second, :language, :summary, :model, :promptTokens, :completionTokens,
                    :requestedBy, :createdAt)
                on conflict (content_hash) do nothing
                """,
                new MapSqlParameterSource("id", UuidCreator.getTimeOrderedEpoch())
                        .addValue("hash", comparison.contentHash())
                        .addValue("first", comparison.firstSenseKey())
                        .addValue("second", comparison.secondSenseKey())
                        .addValue("language", comparison.explanationLanguage())
                        .addValue("summary", comparison.summary())
                        .addValue("model", comparison.model())
                        .addValue("promptTokens", comparison.promptTokens())
                        .addValue("completionTokens", comparison.completionTokens())
                        .addValue("requestedBy", comparison.requestedBy())
                        .addValue("createdAt", Timestamp.from(now)));
    }

    public long boughtSince(UUID userId, Instant since) {
        Long count = jdbc.queryForObject(
                "select count(*) from lexicon.sense_comparison where requested_by = :user and created_at > :since",
                new MapSqlParameterSource("user", userId).addValue("since", Timestamp.from(since)),
                Long.class);
        return count == null ? 0 : count;
    }

    public List<SenseComparisonSpendLine> spendBetween(Instant since, Instant until) {
        return jdbc.query(
                """
                select model, count(*) as comparisons, coalesce(sum(prompt_tokens), 0) as prompt_tokens,
                       coalesce(sum(completion_tokens), 0) as completion_tokens
                from lexicon.sense_comparison
                where created_at >= :since and created_at < :until
                group by model order by model
                """,
                new MapSqlParameterSource("since", Timestamp.from(since)).addValue("until", Timestamp.from(until)),
                (row, index) -> new SenseComparisonSpendLine(
                        row.getString("model"),
                        row.getLong("comparisons"),
                        row.getLong("prompt_tokens"),
                        row.getLong("completion_tokens")));
    }
}
