package com.almonium.learning.lexicon.repository;

import com.almonium.learning.lexicon.model.entity.LexemeForm;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LexemeFormRepository extends JpaRepository<LexemeForm, UUID> {
    List<LexemeForm> findByLanguageAndNormalizedFormOrderByPosition(String language, String normalizedForm);
}
