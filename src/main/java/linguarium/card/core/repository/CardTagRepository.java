package linguarium.card.core.repository;

import java.util.Set;
import linguarium.card.core.model.entity.Card;
import linguarium.card.core.model.entity.CardTag;
import linguarium.card.core.model.entity.pk.CardTagPK;
import linguarium.user.core.model.entity.Learner;
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
