package com.almonium.learning.lexicon.repository;

import com.almonium.learning.lexicon.model.entity.Lexeme;
import java.math.BigDecimal;
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
