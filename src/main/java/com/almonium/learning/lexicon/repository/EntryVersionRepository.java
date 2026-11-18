package com.almonium.learning.lexicon.repository;

import com.almonium.learning.lexicon.model.entity.EntryVersion;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EntryVersionRepository extends JpaRepository<EntryVersion, UUID> {
    Optional<EntryVersion> findFirstByLanguageAndHeadwordOrderByVersionDesc(String language, String headword);

    Optional<EntryVersion> findByLanguageAndHeadwordAndVersion(String language, String headword, int version);

    /** Every version that has the sense, oldest first. */
    @Query(nativeQuery = true, value = """
            select * from lexicon.entry_version v
            where v.document @> cast(:live as jsonb)
            order by v.language, v.headword, v.version
            """)
    List<EntryVersion> findContaining(@Param("live") String live);
}
