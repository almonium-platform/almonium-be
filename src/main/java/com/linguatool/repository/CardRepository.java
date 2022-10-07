package com.linguatool.repository;

import com.linguatool.model.entity.lang.Card;
import com.linguatool.model.entity.lang.LanguageEntity;
import com.linguatool.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findAllByOwner(User owner);
    List<Card> findAllByOwnerAndLanguage(User owner, LanguageEntity language);


    List<Card> findAllByOwnerAndEntryLikeIgnoreCase(User user, String entry);

    Optional<Card> getByGeneratedId(String id);
}
