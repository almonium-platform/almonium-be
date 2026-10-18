package com.almonium.learning.lexicon.repository;

import com.almonium.learning.lexicon.model.entity.Pronunciation;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PronunciationRepository extends JpaRepository<Pronunciation, UUID> {
    /**
     * The pronunciations of a language no clip has been made for yet, with their lexeme and form loaded, in a stable
     * order (headword, rank, then variety and id as tie-breakers) so that paging by offset never skips or repeats.
     */
    @Query("""
            select p from Pronunciation p
            join fetch p.lexeme l
            left join fetch p.form
            where l.language = :language
              and not exists (select c.id from AudioClip c where c.pronunciation = p)
            order by l.headword, p.rank, p.variety, p.id
            limit :limit offset :offset
            """)
    List<Pronunciation> findWithoutClips(
            @Param("language") String language, @Param("limit") int limit, @Param("offset") int offset);
}
