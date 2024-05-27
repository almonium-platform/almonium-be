package com.linguarium.card.core.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.linguarium.card.core.model.Card;
import com.linguarium.engine.translator.model.Language;
import com.linguarium.user.core.model.Learner;
import com.linguarium.user.core.model.User;
import com.linguarium.util.TestDataGenerator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
class CardRepositoryTest {
    private static final Language TEST_LANGUAGE = Language.EN;
    private static final String TEST_ENTRY = "Sample Entry";
    private static final UUID TEST_PUBLIC_ID = UUID.randomUUID();

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CardRepository cardRepository;

    private Learner managedLearner;

    @BeforeEach
    void setup() {
        User user = TestDataGenerator.buildTestUser();

        managedLearner = new Learner();
        managedLearner.setUser(user);
        user.setLearner(managedLearner);

        entityManager.persist(user);
        entityManager.flush();

        Card card = TestDataGenerator.buildTestCard(TEST_PUBLIC_ID, TEST_ENTRY, managedLearner);
        entityManager.persist(card);
        entityManager.flush();
    }

    @DisplayName("Should find all cards by owner with correct details")
    @Test
    void givenOwner_whenFindAllByOwner_thenShouldReturnCardsWithCorrectDetails() {
        List<Card> cards = cardRepository.findAllByOwner(managedLearner);
        assertThat(cards).allMatch(card -> card.getOwner().equals(managedLearner));
    }

    @DisplayName("Should find all cards by owner and language with correct details")
    @Test
    void givenOwnerAndLanguage_whenFindAllByOwnerAndLanguage_thenShouldReturnCardsWithCorrectDetails() {
        List<Card> cards = cardRepository.findAllByOwnerAndLanguage(managedLearner, TEST_LANGUAGE);
        assertThat(cards).isNotEmpty();
        assertThat(cards)
                .allMatch(card -> card.getOwner().equals(managedLearner)
                        && card.getLanguage().equals(TEST_LANGUAGE));
    }

    @DisplayName("Should find cards matching entry pattern with case insensitivity")
    @Test
    void givenOwnerAndEntry_whenFindAllByOwnerAndEntryLikeIgnoreCase_thenShouldReturnMatchingCards() {
        List<Card> cards =
                cardRepository.findAllByOwnerAndEntryLikeIgnoreCase(managedLearner, TEST_ENTRY.toLowerCase());
        assertThat(cards).isNotEmpty();
        assertThat(cards).anyMatch(card -> card.getEntry().equalsIgnoreCase(TEST_ENTRY));
    }

    @DisplayName("Should get card by public ID with correct details")
    @Test
    void givenPublicId_whenGetByPublicId_thenShouldReturnCorrectCard() {
        Optional<Card> card = cardRepository.getByPublicId(TEST_PUBLIC_ID);
        assertThat(card).isPresent();
        assertThat(card.get().getPublicId()).isEqualTo(TEST_PUBLIC_ID);
    }
}
