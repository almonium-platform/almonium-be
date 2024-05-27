package linguarium.card.core.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import linguarium.card.core.model.entity.Card;
import linguarium.engine.translator.model.enums.Language;
import linguarium.user.core.model.entity.Learner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findAllByOwner(Learner owner);

    List<Card> findAllByOwnerAndLanguage(Learner owner, Language language);

    List<Card> findAllByOwnerAndEntryLikeIgnoreCase(Learner user, String entry);

    Optional<Card> getByPublicId(UUID id);
}
