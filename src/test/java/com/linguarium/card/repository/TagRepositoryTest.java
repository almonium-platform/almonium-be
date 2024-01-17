package com.linguarium.card.repository;

import com.linguarium.card.model.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class TagRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TagRepository tagRepository;

    private Tag managedTag;

    @BeforeEach
    public void setup() {
        // Create and persist a Tag entity
        managedTag = new Tag("Sample_Tag");
        entityManager.persist(managedTag);
        entityManager.flush();
    }

    @Test
    @DisplayName("Should find tag by text ignoring case and space")
    public void whenFindByTextNormalized_thenShouldReturnTag() {
        Optional<Tag> foundTag = tagRepository.findByTextWithNormalization(" sample tag ");
        assertThat(foundTag).isPresent();
        assertThat(foundTag.get().getText()).isEqualTo(managedTag.getText());
    }

    @Test
    @DisplayName("Should find tag by exact text")
    public void whenFindByText_thenShouldReturnTag() {
        Optional<Tag> foundTag = tagRepository.findByText("sample_tag");
        assertThat(foundTag).isPresent();
        assertThat(foundTag.get().getText()).isEqualTo(managedTag.getText());
    }
}
