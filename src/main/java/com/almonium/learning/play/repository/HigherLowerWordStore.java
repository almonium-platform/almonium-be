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
 * are only nouns, verbs, adjectives and adverbs between {@link #FIRST_RANK} and {@link #LAST_RANK}. A word carries its
 * meaning in the reader's language when its entry has one, and plays without one when it does not yet. Of a lemma
 * written as several lexemes, one with a meaning speaks for it, then the one with the most senses. Languages are
 * lower-case, as the lexicon spells them.
 */
@Repository
@RequiredArgsConstructor
public class HigherLowerWordStore {
    public static final int FIRST_RANK = 50;
    public static final int LAST_RANK = 20_000;

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * A random sample of playable words, at most {@code perBand} from each quarter-Zipf band, so common words are as
     * likely to appear as rare ones although rare words far outnumber them, and every gap the game aims for finds a
     * partner.
     */
    public List<HigherLowerWord> sample(String language, String fluent, int perBand) {
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
                    left join glossed gl on gl.lexeme_id = l.id
                    where r.rank between :firstRank and :lastRank and l.pos in ('NOUN', 'VERB', 'ADJ', 'ADV')
                    order by r.lemma_key, gl.sense_id is null,
                             (select count(*) from lexicon.sense s where s.lexeme_id = l.id) desc, l.homograph_no
                ), banded as (
                    select c.*, row_number() over (partition by floor(c.zipf * 4) order by random()) as pick
                    from candidates c
                ), sample as (
                    select * from banded where pick <= :perBand
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
                        .addValue("perBand", perBand),
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
