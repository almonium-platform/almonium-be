package linguarium.card.core.repository;

import java.util.Set;
import linguarium.card.core.model.Card;
import linguarium.card.core.model.CardTag;
import linguarium.card.core.model.CardTagPK;
import linguarium.user.core.model.Learner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CardTagRepository extends JpaRepository<CardTag, CardTagPK> {
    @Query("select distinct c.tag.id from CardTag c where c.learner = :learner")
    Set<Long> getLearnersTags(@Param("learner") Learner learner);

    @Query(value = "from CardTag CT where CT.card = :card and CT.tag.text = :text")
    CardTag getByCardAndText(Card card, String text);
}
