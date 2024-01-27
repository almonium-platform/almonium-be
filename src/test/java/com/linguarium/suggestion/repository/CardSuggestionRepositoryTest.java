package com.linguarium.suggestion.repository;

import com.linguarium.card.model.Card;
import com.linguarium.suggestion.model.CardSuggestion;
import com.linguarium.user.model.Learner;
import com.linguarium.user.model.User;
import com.linguarium.util.TestDataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class CardSuggestionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CardSuggestionRepository cardSuggestionRepository;

    private Learner sender;
    private Learner recipient;
    private Card card;

    @BeforeEach
    public void setUp() {
        User senderUser = TestDataGenerator.buildTestUser();
        User recipientUser = TestDataGenerator.buildAnotherTestUser();
        card = TestDataGenerator.buildTestCard();

        entityManager.persist(senderUser);
        entityManager.persist(recipientUser);

        sender = senderUser.getLearner();
        recipient = recipientUser.getLearner();

        entityManager.persist(card);
        entityManager.flush();
    }

    @Test
    @DisplayName("Should find a CardSuggestion by sender, recipient, and card")
    public void givenSenderRecipientCard_whenGetBySenderAndRecipientAndCard_thenShouldReturnCardSuggestion() {
        CardSuggestion cardSuggestion = new CardSuggestion(sender, recipient, card);
        entityManager.persist(cardSuggestion);
        entityManager.flush();

        CardSuggestion foundCardSuggestion = cardSuggestionRepository.getBySenderAndRecipientAndCard(sender, recipient, card);

        assertThat(foundCardSuggestion)
                .isNotNull()
                .extracting(CardSuggestion::getSender, CardSuggestion::getRecipient, CardSuggestion::getCard)
                .containsExactly(sender, recipient, card);
    }

    @Test
    @DisplayName("Should find all CardSuggestions for a recipient")
    public void givenRecipient_whenGetByRecipient_thenShouldReturnListOfCardSuggestions() {
        Card secondCard = TestDataGenerator.buildTestCard();
        entityManager.persist(secondCard);
        entityManager.flush();

        CardSuggestion cardSuggestion1 = new CardSuggestion(sender, recipient, card);
        CardSuggestion cardSuggestion2 = new CardSuggestion(sender, recipient, secondCard);

        entityManager.persist(cardSuggestion1);
        entityManager.persist(cardSuggestion2);
        entityManager.flush();

        List<CardSuggestion> foundCardSuggestions = cardSuggestionRepository.getByRecipient(recipient);

        assertThat(foundCardSuggestions)
                .isNotEmpty()
                .hasSize(2)
                .extracting(CardSuggestion::getRecipient)
                .containsOnly(recipient);
    }

}
