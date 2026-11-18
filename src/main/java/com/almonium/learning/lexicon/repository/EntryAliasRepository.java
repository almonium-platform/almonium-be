package com.almonium.learning.lexicon.repository;

import com.almonium.learning.lexicon.model.entity.EntryAlias;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EntryAliasRepository extends JpaRepository<EntryAlias, UUID> {
    @Modifying
    @Query("delete from EntryAlias a where a.language = :language and a.headword = :headword")
    void deleteOf(@Param("language") String language, @Param("headword") String headword);
}
