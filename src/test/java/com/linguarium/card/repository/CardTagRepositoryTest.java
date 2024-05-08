package com.linguarium.card.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.linguarium.card.model.Card;
import com.linguarium.card.model.CardTag;
import com.linguarium.card.model.Tag;
import com.linguarium.translator.model.Language;
import com.linguarium.user.model.Learner;
import com.linguarium.user.model.User;
import com.linguarium.util.TestDataGenerator;
import java.util.Set;
import java.util.UUID;
import lombok.AccessLevel;
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
@FieldDefaults(level = AccessLevel.PRIVATE)
class CardTagRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    CardTagRepository cardTagRepository;

    User managedUser;

    Learner managedLearner;
    Card managedCard;
    Tag managedTag;

    @BeforeEach
    void setup() {
        // Set up the Learner, Card, and Tag entities
        managedUser = TestDataGenerator.buildTestUser();
        managedCard = buildTestCard();
        managedTag = buildTestTag();

        entityManager.persist(managedUser);
        managedLearner = managedUser.getLearner();
        entityManager.persist(managedCard);
        entityManager.persist(managedTag);
        entityManager.flush();

        // Create and persist a CardTag
        CardTag cardTag = new CardTag();
        cardTag.setCard(managedCard);
        cardTag.setTag(managedTag);
        cardTag.setLearner(managedLearner);
        entityManager.persist(cardTag);
        entityManager.flush();
    }

    @DisplayName("Should retrieve a set of learner's tags")
    @Test
    void whenGetLearnersTags_thenShouldReturnTags() {
        Set<Long> tags = cardTagRepository.getLearnersTags(managedLearner);
        assertThat(tags).isNotEmpty();
        assertThat(tags).contains(managedTag.getId());
    }

    @DisplayName("Should get CardTag by card and tag text")
    @Test
    void givenCardAndText_whenGetByCardAndText_thenShouldReturnCardTag() {
        CardTag cardTag = cardTagRepository.getByCardAndText(managedCard, managedTag.getText());
        assertThat(cardTag).isNotNull();
        assertThat(cardTag.getCard()).isEqualTo(managedCard);
        assertThat(cardTag.getTag()).isEqualTo(managedTag);
    }

    private Card buildTestCard() {
        Card card = new Card();
        card.setPublicId(UUID.randomUUID());
        card.setEntry("Sample Entry");
        card.setOwner(managedLearner);
        card.setLanguage(Language.EN);
        return card;
    }

    private Tag buildTestTag() {
        Tag tag = new Tag();
        tag.setText("tag");
        return tag;
    }
}
