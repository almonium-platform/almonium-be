package com.almonium.learning.lexicon.repository;

import com.almonium.learning.lexicon.model.entity.Lexeme;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LexemeRepository extends JpaRepository<Lexeme, UUID> {
    List<Lexeme> findByLanguageAndHeadword(String language, String headword);

    Optional<Lexeme> findByLanguageAndLemmaAndPosAndHomographNo(
            String language, String lemma, String pos, int homographNo);

    /**
     * Deletes the row and, through the database's cascades, everything under it, in one statement. Deleting the
     * entity instead would have Hibernate null the form of each pronunciation before removing it, and a pronunciation
     * with no form collides with the lexeme's own row for that variety.
     */
    @Modifying
    @Query("delete from Lexeme l where l.id = :id")
    void deleteTree(@Param("id") UUID id);

    /**
     * Words said exactly like the given lexemes: another lemma of the language whose own transcription in the same
     * variety is the same, slashes and syllable dots aside. Not a homophone: one of the lemma's own forms (French
     * arrivée of arriver), a family member, or a spelling of the same word, which shares the glosses of one sense in
     * two reader languages or more (humour, humor; plain and plane share only German Ebene and stay). One row per
     * lexeme asked about, homophone lexeme and variety. The candidates are gathered first and each test looks one
     * way, so every lookup stays on an index (one query with both ways in an OR scanned whole tables).
     */
    @Query(nativeQuery = true, value = """
            with candidate as materialized (
                select distinct me.id as lexeme_id, me.language as language, lower(me.lemma) as said, o.lemma as lemma,
                       lower(o.lemma) as alike, o.pos as pos, p.variety as variety
                from lexicon.lexeme me
                join lexicon.pronunciation mp on mp.lexeme_id = me.id and mp.lexeme_form_id is null
                join lexicon.pronunciation p on p.variety = mp.variety and p.lexeme_form_id is null
                     and translate(p.phonemic, '/.', '') = translate(mp.phonemic, '/.', '')
                join lexicon.lexeme o on o.id = p.lexeme_id and o.language = me.language and lower(o.lemma) <> lower(me.lemma)
                where me.id in (:ids))
            select cast(c.lexeme_id as varchar) as lexeme, c.lemma as lemma, c.pos as pos, c.variety as variety
            from candidate c
            where not exists (
                    select 1 from lexicon.lexeme x join lexicon.lexeme_form f on f.lexeme_id = x.id
                    where x.language = c.language and lower(x.lemma) = c.said and f.normalized_form = c.alike)
              and not exists (
                    select 1 from lexicon.lexeme x join lexicon.lexeme_form f on f.lexeme_id = x.id
                    where x.language = c.language and lower(x.lemma) = c.alike and f.normalized_form = c.said)
              and not exists (
                    select 1 from lexicon.lexeme x join lexicon.lexeme_relation r on r.lexeme_id = x.id
                    where x.language = c.language and lower(x.lemma) = c.said and r.type = 'family'
                      and lower(r.target_lemma) = c.alike)
              and not exists (
                    select 1 from lexicon.lexeme x join lexicon.lexeme_relation r on r.lexeme_id = x.id
                    where x.language = c.language and lower(x.lemma) = c.alike and r.type = 'family'
                      and lower(r.target_lemma) = c.said)
              and not exists (
                    select 1
                    from lexicon.lexeme xa
                    join lexicon.sense sa on sa.lexeme_id = xa.id
                    join lexicon.sense_gloss ga on ga.sense_id = sa.id
                    join lexicon.lexeme xb on xb.language = c.language and lower(xb.lemma) = c.alike
                    join lexicon.sense sb on sb.lexeme_id = xb.id
                    join lexicon.sense_gloss gb on gb.sense_id = sb.id
                         and gb.fluent_language = ga.fluent_language and lower(gb.gloss) = lower(ga.gloss)
                    where xa.language = c.language and lower(xa.lemma) = c.said
                    group by sa.id, sb.id
                    having count(distinct ga.fluent_language) >= 2)
            order by c.lemma, c.pos, c.variety
            """)
    List<SpokenAlike> findSpokenAlike(@Param("ids") Collection<UUID> ids);

    /** A row of {@link #findSpokenAlike}: the lexeme asked about (its id as text), and a word said like it where. */
    interface SpokenAlike {
        String getLexeme();

        String getLemma();

        String getPos();

        String getVariety();
    }

    /** The lexemes a reader's word names directly: by headword or by lemma, case-insensitively. */
    @Query("""
            select l from Lexeme l
            where l.language = :language and (lower(l.headword) = :normalized or lower(l.lemma) = :normalized)
            order by l.lemma, l.pos, l.homographNo
            """)
    List<Lexeme> findByLanguageAndNormalizedWord(
            @Param("language") String language, @Param("normalized") String normalized);

    /**
     * Headwords beginning with a prefix, one row per headword, for a field being typed into. A headword that begins
     * with the prefix comes before one reached through an inflected form (ging finds gehen), then the more frequent
     * spelling, then the shorter. Of a headword's lexemes, the one with the most senses speaks for it. The prefix is
     * normalised and LIKE-escaped by the caller.
     */
    @Query(nativeQuery = true, value = """
            with matches as (
                select l.id, l.headword, l.pos, l.grammar, l.zipf, 0 as via_form
                from lexicon.lexeme l
                where l.language = :language and lower(l.headword) like :pattern
                union all
                select l.id, l.headword, l.pos, l.grammar, l.zipf, 1
                from lexicon.lexeme_form f join lexicon.lexeme l on l.id = f.lexeme_id
                where f.language = :language and f.normalized_form like :pattern
            ), best as (
                select distinct on (lower(m.headword)) m.*
                from matches m
                order by lower(m.headword), m.via_form,
                         (select count(*) from lexicon.sense s where s.lexeme_id = m.id) desc
            )
            select b.headword as headword, b.pos as pos, b.grammar ->> 'gender' as gender, b.zipf as zipf,
                   (select g.gloss from lexicon.sense s join lexicon.sense_gloss g on g.sense_id = s.id
                    where s.lexeme_id = b.id and g.fluent_language = :fluent
                    order by s.position, g.rank limit 1) as gloss
            from best b
            order by b.via_form, b.zipf desc nulls last, length(b.headword), b.headword
            limit :limit
            """)
    List<SuggestionRow> suggest(
            @Param("language") String language,
            @Param("pattern") String pattern,
            @Param("fluent") String fluent,
            @Param("limit") int limit);

    interface SuggestionRow {
        String getHeadword();

        String getPos();

        String getGender();

        String getGloss();

        BigDecimal getZipf();
    }
}
