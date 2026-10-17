package com.almonium.learning.lexicon.repository;

import com.almonium.learning.lexicon.model.entity.RejectedHeadword;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RejectedHeadwordRepository extends JpaRepository<RejectedHeadword, UUID> {
    Optional<RejectedHeadword> findByLanguageAndHeadword(String language, String headword);
}
