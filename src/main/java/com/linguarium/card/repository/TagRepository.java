package com.linguarium.card.repository;

import com.linguarium.card.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    default Optional<Tag> findByTextNormalized(String text) {
        return findByText(Tag.normalizeText(text));
    }

    Optional<Tag> findByText(String text);
}
