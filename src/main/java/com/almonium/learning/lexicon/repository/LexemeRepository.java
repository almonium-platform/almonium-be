package com.almonium.learning.lexicon.repository;

import com.almonium.learning.lexicon.model.entity.Lexeme;
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
}
