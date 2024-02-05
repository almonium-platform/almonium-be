package com.linguarium.card.repository;

import com.linguarium.card.model.Tag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByText(String text);

    default Optional<Tag> findByTextWithNormalization(String text) {
        return findByText(Tag.normalizeText(text));
    }
}
