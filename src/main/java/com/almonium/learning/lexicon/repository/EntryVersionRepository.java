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

    /** Every version that has the sense, live or retired, oldest first. */
    @Query(nativeQuery = true, value = """
            select * from lexicon.entry_version v
            where v.document @> cast(:live as jsonb) or jsonb_exists(v.document -> 'retired', :key)
            order by v.built_at, v.version
            """)
    List<EntryVersion> findContaining(@Param("live") String live, @Param("key") String key);
}
