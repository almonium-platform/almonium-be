package com.almonium.learning.lexicon.repository;

import com.almonium.learning.lexicon.model.entity.AudioClip;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AudioClipRepository extends JpaRepository<AudioClip, UUID> {
    /** The row for one rendering of one pronunciation, which the table's unique key says is at most one. */
    Optional<AudioClip> findByPronunciationIdAndEngineAndVoiceAndModelAndPaceAndFormat(
            UUID pronunciationId, String engine, String voice, String model, BigDecimal pace, String format);

    /** Every clip of every pronunciation of these lexemes, in one query, so a lookup never fetches them one by one. */
    @Query("""
            select c from AudioClip c
            join fetch c.pronunciation p
            where p.lexeme.id in :lexemeIds
            order by c.createdAt
            """)
    List<AudioClip> findByLexemeIds(@Param("lexemeIds") Collection<UUID> lexemeIds);
}
