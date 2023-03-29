package com.linguatool.repository;

import com.linguatool.model.entity.lang.Card;
import com.linguatool.model.entity.lang.CardTag;
import com.linguatool.model.entity.lang.CardTagPK;
import com.linguatool.model.entity.lang.Tag;
import com.linguatool.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CardTagRepository extends JpaRepository<CardTag, CardTagPK> {

    Set<CardTag> getByUserAndTag(User user, Tag tag);

    @Query(value = "select distinct tag_id from card_tag where user_id = ?1", nativeQuery = true)
    Set<Long> getUsersTags(User user);

    @Query(value = "from CardTag CT where CT.card = :card and CT.tag.text = :text")
    CardTag getByCardAndText(Card card, String text);

    void deleteByUserAndTag(User user, Tag tag);

}
