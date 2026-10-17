package com.almonium.learning.lexicon.repository;

import com.almonium.learning.lexicon.model.entity.LexemeRelation;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LexemeRelationRepository extends JpaRepository<LexemeRelation, UUID> {
    /**
     * Points every relation that names this lemma and pos in this language, and has no target yet, at the lexeme
     * that has just arrived. The first homograph takes them; the writer does not number homographs in relations.
     */
    @Modifying
    @Query(value = """
                    update lexicon.lexeme_relation r
                    set target_lexeme_id = :lexemeId
                    from lexicon.lexeme l
                    where l.id = r.lexeme_id
                      and l.language = :language
                      and r.target_lemma = :lemma
                      and r.target_pos = :pos
                      and r.target_lexeme_id is null
                    """, nativeQuery = true)
    int pointDanglingAt(
            @Param("lexemeId") UUID lexemeId,
            @Param("language") String language,
            @Param("lemma") String lemma,
            @Param("pos") String pos);
}
