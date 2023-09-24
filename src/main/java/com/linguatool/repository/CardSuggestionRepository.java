package com.linguatool.repository;

import com.linguatool.model.entity.lang.Card;
import com.linguatool.model.entity.lang.CardSuggestion;
import com.linguatool.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardSuggestionRepository extends JpaRepository<CardSuggestion, Long> {

    CardSuggestion getBySenderAndRecipientAndCard(User sender, User recipient, Card card);
    void deleteBySenderIdAndRecipientIdAndCardId(Long senderId, Long recipientId, Long cardId);
    List<CardSuggestion> getByRecipient(User recipient);
}
