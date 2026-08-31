package com.almonium.learning.book.repository;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.config.PostgresContainer;
import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.model.entity.TranslationOrder;
import com.almonium.learning.book.model.enums.TranslationOrderStatus;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;

@FieldDefaults(level = PRIVATE)
@DataJpaTest
@ImportTestcontainers(PostgresContainer.class)
@Sql(scripts = "classpath:db/add-translation-requests.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class TranslationOrderRepositoryTest {
    private static final UUID ASKER_ID = UUID.fromString("01956ce2-34fa-71f2-97b1-dacd41dbab00");
    private static final UUID BOOK_ID = UUID.fromString("01956ce2-34fa-71f2-97b1-dacd41dbab01");

    @Autowired
    TranslationOrderRepository translationOrderRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserRepository userRepository;

    @DisplayName("Should hold one request per book and language, not one per book")
    @Test
    void givenTwoLanguagesOnOneBook_whenSaving_thenBothRequestsArePersisted() {
        translationOrderRepository.saveAndFlush(order(Language.UK));
        translationOrderRepository.saveAndFlush(order(Language.PL));

        assertThat(translationOrderRepository.findByUserIdAndStatusInOrderByCreatedAtDesc(
                        ASKER_ID, List.of(TranslationOrderStatus.ASKED)))
                .hasSize(2);
    }

    @DisplayName("Should still refuse the same book and language twice")
    @Test
    void givenTheSameLanguageTwice_whenSaving_thenTheUniqueConstraintRejectsIt() {
        translationOrderRepository.saveAndFlush(order(Language.ES));

        assertThatThrownBy(() -> translationOrderRepository.saveAndFlush(order(Language.ES)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    private TranslationOrder order(Language language) {
        User user = userRepository.findById(ASKER_ID).orElseThrow();
        Book book = bookRepository.findById(BOOK_ID).orElseThrow();
        TranslationOrder order = new TranslationOrder(user, book, language);
        order.setCreatedAt(Instant.now());
        return order;
    }
}
