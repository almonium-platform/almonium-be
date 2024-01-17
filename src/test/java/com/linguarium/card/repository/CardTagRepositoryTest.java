package com.linguarium.card.repository;

import com.linguarium.card.model.Card;
import com.linguarium.card.model.CardTag;
import com.linguarium.card.model.Tag;
import com.linguarium.translator.model.Language;
import com.linguarium.user.model.Learner;
import com.linguarium.user.model.User;
import com.linguarium.util.TestEntityGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class CardTagRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CardTagRepository cardTagRepository;
    private User managedUser;

    private Learner managedLearner;
    private Card managedCard;
    private Tag managedTag;

    @BeforeEach
    public void setup() {
        // Set up the Learner, Card, and Tag entities
        managedUser = TestEntityGenerator.buildTestUser();
        managedLearner = buildTestLearner();
        managedCard = buildTestCard();
        managedTag = buildTestTag();

        entityManager.persist(managedUser);
        entityManager.persist(managedLearner);
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

    @Test
    @DisplayName("Should retrieve a set of learner's tags")
    public void whenGetLearnersTags_thenShouldReturnTags() {
        Set<Long> tags = cardTagRepository.getLearnersTags(managedLearner);
        assertThat(tags).isNotEmpty();
        assertThat(tags).contains(managedTag.getId());
    }

    @Test
    @DisplayName("Should get CardTag by card and tag text")
    public void givenCardAndText_whenGetByCardAndText_thenShouldReturnCardTag() {
        CardTag cardTag = cardTagRepository.getByCardAndText(managedCard, managedTag.getText());
        assertThat(cardTag).isNotNull();
        assertThat(cardTag.getCard()).isEqualTo(managedCard);
        assertThat(cardTag.getTag()).isEqualTo(managedTag);
    }

    private Learner buildTestLearner() {
        Learner learner = new Learner();
        learner.setUser(managedUser);
        return learner;
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
