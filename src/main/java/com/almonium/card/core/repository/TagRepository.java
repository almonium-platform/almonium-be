package com.almonium.card.core.repository;

import com.almonium.card.core.model.entity.Tag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByText(String text);

    default Optional<Tag> findByTextWithNormalization(String text) {
        return findByText(Tag.normalizeText(text));
    }
}
