package com.almonium.user.core.repository;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.almonium.config.PostgresContainer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import java.util.UUID;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.test.context.jdbc.Sql;

/**
 * What Postgres does when a user row goes, checked against the real schema: every table that points at user_core has
 * to either go with the user or let go of them, or deleting the account fails. Plain SQL on purpose, so the database's
 * delete rules are what is under test and not Hibernate's cascades.
 */
@DataJpaTest
@ImportTestcontainers(PostgresContainer.class)
@FieldDefaults(level = PRIVATE)
@Sql(scripts = "classpath:db/add-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class AccountDeletionConstraintsTest {
    private static final UUID READER_ID = UUID.fromString("01956ce2-34fa-71f2-97b1-dacd41dbaba1");
    private static final UUID OPERATOR_ID = UUID.fromString("01956cde-a541-7ac1-8b32-2896d096ecdf");
    private static final UUID BOOK_ID = UUID.fromString("01956ce2-34fa-71f2-97b1-dacd41dbac01");
    private static final UUID IMPORT_ID = UUID.fromString("01956ce2-34fa-71f2-97b1-dacd41dbac02");

    @Autowired
    EntityManager entityManager;

    @BeforeEach
    void givenAReaderWithSomethingInEveryTableThatPointsAtThem() {
        execute("""
                INSERT INTO book (id, edition_slug, work_slug, source_hash, title, author, publication_year,
                                  language, original_language, word_count, cefr_level, edition_type)
                VALUES ('%s', 'effi-briest-de', 'effi-briest', 'sha-effi', 'Effi Briest', 'Theodor Fontane', 1895,
                        'DE', 'DE', 120000, 'B2', 'original')
                """.formatted(BOOK_ID));
        execute("""
                INSERT INTO user_book_import (id, user_id, title, status, created_at, updated_at)
                VALUES ('%s', '%s', 'My upload', 'READY', now(), now())
                """.formatted(IMPORT_ID, READER_ID));
        execute("""
                INSERT INTO library_suggestion (id, book_import_id, user_id, title, author, language, status,
                                                created_at, decided_by_user_id)
                VALUES (gen_random_uuid(), '%s', '%s', 'My upload', 'Someone', 'DE', 'ACCEPTED', now(), '%s')
                """.formatted(IMPORT_ID, READER_ID, OPERATOR_ID));
        execute("""
                INSERT INTO book_request (id, user_id, title, author, normalized_key, language, status, created_at,
                                          decided_by_user_id)
                VALUES (gen_random_uuid(), '%s', 'Der Stechlin', 'Theodor Fontane', 'der-stechlin', 'DE',
                        'DECLINED', now(), '%s')
                """.formatted(READER_ID, OPERATOR_ID));
        execute("""
                INSERT INTO book_certificate (id, user_id, book_id, finished_at, words, words_read, words_saved)
                VALUES (gen_random_uuid(), '%s', '%s', now(), '[]'::jsonb, 120000, 40)
                """.formatted(READER_ID, BOOK_ID));
        execute("""
                INSERT INTO access_grant (id, user_id, granted_by_user_id, entitlement, reason, starts_at)
                VALUES (gen_random_uuid(), '%s', '%s', 'PREMIUM', 'beta tester', now())
                """.formatted(READER_ID, OPERATOR_ID));
        execute("""
                INSERT INTO translation_job (id, book_id, language, phase, estimated_cost_usd, tier, mode,
                                             approved_at, created_at, approved_by_user_id)
                VALUES (gen_random_uuid(), '%s', 'EN', 'QUEUED', 1.50, 'STANDARD', 'FULL', now(), now(), '%s')
                """.formatted(BOOK_ID, OPERATOR_ID));
    }

    @DisplayName("Deleting a reader takes their imports, suggestions, requests, certificates and grants with them")
    @Test
    void whenTheReaderIsDeleted_thenWhatTheyOwnedGoesToo() {
        execute("DELETE FROM user_core WHERE id = '%s'".formatted(READER_ID));

        for (String table : new String[] {
            "user_book_import", "library_suggestion", "book_request", "book_certificate", "access_grant"
        }) {
            assertThat(count("SELECT count(*) FROM %s WHERE user_id = '%s'".formatted(table, READER_ID)))
                    .as(table)
                    .isZero();
        }
    }

    @DisplayName("Deleting an operator keeps what they decided and forgets that it was them")
    @Test
    void whenTheOperatorIsDeleted_thenTheirDecisionsStayWithoutThem() {
        execute("DELETE FROM user_core WHERE id = '%s'".formatted(OPERATOR_ID));

        assertThat(count("SELECT count(*) FROM library_suggestion WHERE decided_by_user_id IS NULL"))
                .isOne();
        assertThat(count("SELECT count(*) FROM book_request WHERE decided_by_user_id IS NULL"))
                .isOne();
        assertThat(count("SELECT count(*) FROM access_grant WHERE granted_by_user_id IS NULL"))
                .isOne();
        assertThat(count("SELECT count(*) FROM translation_job WHERE approved_by_user_id IS NULL"))
                .isOne();
    }

    @DisplayName("A founding slot still naming the user refuses the delete, so the release in code cannot be skipped")
    @Test
    void whenAFoundingSlotStillNamesTheReader_thenTheDeleteIsRefused() {
        execute("UPDATE founding_member SET user_id = '%s', status = 'CONFIRMED' WHERE slot_number = 1"
                .formatted(READER_ID));

        assertThatThrownBy(() -> execute("DELETE FROM user_core WHERE id = '%s'".formatted(READER_ID)))
                .isInstanceOf(PersistenceException.class);
    }

    private void execute(String sql) {
        entityManager.createNativeQuery(sql).executeUpdate();
    }

    private long count(String sql) {
        return ((Number) entityManager.createNativeQuery(sql).getSingleResult()).longValue();
    }
}
