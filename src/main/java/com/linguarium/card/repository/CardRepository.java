package com.linguarium.card.repository;

import com.linguarium.card.model.Card;
import com.linguarium.translator.model.LanguageEntity;
import com.linguarium.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findAllByOwner(User owner);
    List<Card> findAllByOwnerAndLanguage(User owner, LanguageEntity language);
    List<Card> findAllByOwnerAndEntryLikeIgnoreCase(User user, String entry);
    Optional<Card> getByPublicId(UUID id);
}
