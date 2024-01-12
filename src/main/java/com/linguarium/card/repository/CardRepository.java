package com.linguarium.card.repository;

import com.linguarium.card.model.Card;
import com.linguarium.translator.model.Language;
import com.linguarium.user.model.Learner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findAllByOwner(Learner owner);
    List<Card> findAllByOwnerAndLanguage(Learner owner, Language language);
    List<Card> findAllByOwnerAndEntryLikeIgnoreCase(Learner user, String entry);
    Optional<Card> getByPublicId(UUID id);
}
