package com.almonium.card.suggestion.repository;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.core.model.entity.Card;
import com.almonium.card.suggestion.model.entity.CardSuggestion;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.model.entity.User;
import com.almonium.util.TestDataGenerator;
import java.util.List;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@FieldDefaults(level = PRIVATE)
class CardSuggestionRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    CardSuggestionRepository cardSuggestionRepository;

    Learner sender;
    Learner recipient;
    Card card;

    @BeforeEach
    void setUp() {
        // Create and persist Users
        User senderUser = TestDataGenerator.buildTestUser();
        User recipientUser = TestDataGenerator.buildAnotherTestUser();
        entityManager.persist(senderUser);
        entityManager.persist(recipientUser);

        // Explicitly create Learners and associate with Users
        sender = new Learner();
        sender.setUser(senderUser);
        sender.setLanguage(Language.EN); // Assuming Language is an Enum
        entityManager.persist(sender);

        recipient = new Learner();
        recipient.setUser(recipientUser);
        recipient.setLanguage(Language.FR); // Example: different language
        entityManager.persist(recipient);

        // Persist a test Card
        card = TestDataGenerator.buildTestCard();
        entityManager.persist(card);

        entityManager.flush();
    }

    @DisplayName("Should find a CardSuggestion by sender, recipient, and card")
    @Test
    void givenSenderRecipientCard_whenGetBySenderAndRecipientAndCard_thenShouldReturnCardSuggestion() {
        CardSuggestion cardSuggestion = new CardSuggestion(sender, recipient, card);
        entityManager.persist(cardSuggestion);
        entityManager.flush();

        CardSuggestion foundCardSuggestion =
                cardSuggestionRepository.getBySenderAndRecipientAndCard(sender, recipient, card);

        assertThat(foundCardSuggestion)
                .isNotNull()
                .extracting(CardSuggestion::getSender, CardSuggestion::getRecipient, CardSuggestion::getCard)
                .containsExactly(sender, recipient, card);
    }

    @DisplayName("Should find all CardSuggestions for a recipient")
    @Test
    void givenRecipient_whenGetByRecipient_thenShouldReturnListOfCardSuggestions() {
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
