package com.linguarium.card.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.linguarium.card.model.Card;
import com.linguarium.card.model.CardTag;
import com.linguarium.card.model.Tag;
import com.linguarium.user.model.Learner;
import com.linguarium.user.model.User;
import com.linguarium.util.TestDataGenerator;
import java.util.Set;
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

    Learner managedLearner;
    Card managedCard;
    Tag managedTag;

    @BeforeEach
    void setup() {
        // Set up the Learner, Card, and Tag entities
        User managedUser = TestDataGenerator.buildTestUser();
        managedTag = buildTestTag();

        entityManager.persist(managedUser);
        managedLearner = managedUser.getLearner();
        entityManager.persist(managedTag);
        entityManager.flush();

        managedCard = TestDataGenerator.buildTestCard(managedLearner);
        entityManager.persist(managedCard);

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

    private Tag buildTestTag() {
        Tag tag = new Tag();
        tag.setText("tag");
        return tag;
    }
}
