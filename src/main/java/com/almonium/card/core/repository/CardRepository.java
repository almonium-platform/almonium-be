package com.almonium.card.core.repository;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.core.model.entity.Card;
import com.almonium.user.core.model.entity.Learner;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findAllByOwner(Learner owner);

    List<Card> findAllByOwnerAndLanguage(Learner owner, Language language);

    List<Card> findAllByOwnerAndEntryLikeIgnoreCase(Learner user, String entry);

    Optional<Card> getByPublicId(UUID id);
}
