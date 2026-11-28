package com.almonium.learning.play.repository;

import com.almonium.learning.play.dto.response.HigherLowerWord;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Words for Higher or Lower, read from the lexicon. A lemma's rank counts every word of the language with a known
 * frequency, function words and names included, so it reads as "the 2,300th most common word"; the words handed out
 * are only nouns, verbs, adjectives and adverbs between {@link #FIRST_RANK} and {@link #LAST_RANK} that carry a
 * meaning in the reader's language. Of a lemma written as several lexemes, the one with the most senses speaks for it.
 * Languages are lower-case, as the lexicon spells them.
 */
@Repository
@RequiredArgsConstructor
public class HigherLowerWordStore {
    public static final int FIRST_RANK = 50;
    public static final int LAST_RANK = 20_000;

    private final NamedParameterJdbcTemplate jdbc;

    /** A random sample of at most {@code limit} playable words. */
    public List<HigherLowerWord> sample(String language, String fluent, int limit) {
        return jdbc.query(
                """
                with ranked as (
                    select lower(l.lemma) as lemma_key, max(l.zipf) as zipf,
                           rank() over (order by max(l.zipf) desc) as rank
                    from lexicon.lexeme l
                    where l.language = :language and l.zipf > 0 and l.kind = 'word'
                    group by lower(l.lemma)
                ), glossed as (
                    select distinct on (s.lexeme_id) s.lexeme_id, s.id as sense_id
                    from lexicon.sense s join lexicon.lexeme l on l.id = s.lexeme_id
                    where l.language = :language
                      and exists (select 1 from lexicon.sense_gloss g
                                  where g.sense_id = s.id and g.fluent_language = :fluent)
                    order by s.lexeme_id, s.position
                ), candidates as (
                    select distinct on (r.lemma_key) l.id, l.lemma, r.zipf, r.rank, gl.sense_id
                    from ranked r
                    join lexicon.lexeme l
                      on l.language = :language and lower(l.lemma) = r.lemma_key and l.kind = 'word'
                    join glossed gl on gl.lexeme_id = l.id
                    where r.rank between :firstRank and :lastRank and l.pos in ('NOUN', 'VERB', 'ADJ', 'ADV')
                    order by r.lemma_key,
                             (select count(*) from lexicon.sense s where s.lexeme_id = l.id) desc, l.homograph_no
                ), sample as (
                    select * from candidates order by random() limit :limit
                )
                select c.lemma, c.zipf, c.rank,
                       (select string_agg(g.gloss, ', ' order by g.rank) from lexicon.sense_gloss g
                        where g.sense_id = c.sense_id and g.fluent_language = :fluent and g.rank <= 2) as gloss
                from sample c
                """,
                new MapSqlParameterSource("language", language)
                        .addValue("fluent", fluent)
                        .addValue("firstRank", FIRST_RANK)
                        .addValue("lastRank", LAST_RANK)
                        .addValue("limit", limit),
                (row, index) -> new HigherLowerWord(
                        row.getString("lemma"),
                        row.getString("gloss"),
                        perMillion(row.getDouble("zipf")),
                        row.getInt("rank")));
    }

    /** Zipf is log10 of uses per billion words, so uses per million is 10 to the power of Zipf minus three. */
    static double perMillion(double zipf) {
        return Math.pow(10, zipf - 3);
    }
}
