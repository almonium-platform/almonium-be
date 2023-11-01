package com.linguarium.card.repository;

import com.linguarium.card.model.Card;
import com.linguarium.card.model.CardTag;
import com.linguarium.card.model.CardTagPK;
import com.linguarium.user.model.Learner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CardTagRepository extends JpaRepository<CardTag, CardTagPK> {

    @Query("SELECT DISTINCT c.tag.id FROM CardTag c WHERE c.learner = :learner")
    Set<Long> getLearnersTags(@Param("learner") Learner learner);

    @Query(value = "FROM CardTag CT WHERE CT.card = :card AND CT.tag.text = :text")
    CardTag getByCardAndText(Card card, String text);
}
