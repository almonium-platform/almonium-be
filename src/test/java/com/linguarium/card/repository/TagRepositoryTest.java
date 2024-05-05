package com.linguarium.card.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.linguarium.card.model.Tag;
import java.util.Optional;
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
class TagRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TagRepository tagRepository;

    private Tag managedTag;

    @BeforeEach
    void setup() {
        // Create and persist a Tag entity
        managedTag = new Tag("Sample_Tag");
        entityManager.persist(managedTag);
        entityManager.flush();
    }

    @DisplayName("Should find tag by text ignoring case and space")
    @Test
    void whenFindByTextNormalized_thenShouldReturnTag() {
        Optional<Tag> foundTag = tagRepository.findByTextWithNormalization(" sample tag ");
        assertThat(foundTag).isPresent();
        assertThat(foundTag.get().getText()).isEqualTo(managedTag.getText());
    }

    @DisplayName("Should find tag by exact text")
    @Test
    void whenFindByText_thenShouldReturnTag() {
        Optional<Tag> foundTag = tagRepository.findByText("sample_tag");
        assertThat(foundTag).isPresent();
        assertThat(foundTag.get().getText()).isEqualTo(managedTag.getText());
    }
}
