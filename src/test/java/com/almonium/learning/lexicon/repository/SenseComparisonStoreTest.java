package com.almonium.learning.lexicon.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.almonium.config.PostgresContainer;
import com.almonium.learning.lexicon.dto.SenseComparisonSpendLine;
import com.almonium.learning.lexicon.repository.SenseComparisonStore.NewComparison;
import com.almonium.learning.lexicon.repository.SenseComparisonStore.SenseFacts;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

/** The comparison ledger against the real schema: a sense's facts, one row per content, the ceiling's count. */
@DataJpaTest
@ImportTestcontainers(PostgresContainer.class)
@Sql(scripts = "classpath:db/add-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class SenseComparisonStoreTest {
    static final UUID JOHN = UUID.fromString("01956ce2-34fa-71f2-97b1-dacd41dbaba1");
    static final UUID JANE = UUID.fromString("01956cde-a541-7ac1-8b32-2896d096ecdf");

    @Autowired
    JdbcTemplate jdbc;

    SenseComparisonStore store;
    final Instant now = Instant.parse("2026-11-25T12:00:00Z");

    @BeforeEach
    void setUp() {
        store = new SenseComparisonStore(new NamedParameterJdbcTemplate(jdbc));
    }

    @Test
    void readsASenseWithItsWordAndItsFirstExamples() {
        UUID lexeme = UUID.randomUUID();
        jdbc.update("""
                insert into lexicon.lexeme (id, language, headword, lemma, pos, homograph_no, kind, grammar,
                    entry_version, status, created_at, updated_at)
                values (?, 'en', 'big', 'big', 'ADJ', 1, 'word', '{"comparable": true}'::jsonb, 1, 'published',
                    now(), now())
                """, lexeme);
        UUID sense = UUID.randomUUID();
        UUID key = UUID.randomUUID();
        jdbc.update("""
                insert into lexicon.sense (id, lexeme_id, position, definition, cefr, sense_key, labels, collocations)
                values (?, ?, 1, 'large in size', 'A1', ?, '{"formality": "informal"}'::jsonb, '["big deal"]'::jsonb)
                """, sense, lexeme, key);
        for (int position = 1; position <= 3; position++) {
            jdbc.update(
                    "insert into lexicon.sense_example (id, sense_id, position, text, origin) values (?, ?, ?, ?, 'ai')",
                    UUID.randomUUID(),
                    sense,
                    position,
                    "Example " + position);
        }

        SenseFacts facts = store.facts(key).orElseThrow();

        assertThat(facts.headword()).isEqualTo("big");
        assertThat(facts.partOfSpeech()).isEqualTo("ADJ");
        assertThat(facts.wordGrammar()).contains("comparable");
        assertThat(facts.definition()).isEqualTo("large in size");
        assertThat(facts.labels()).contains("informal");
        assertThat(facts.collocations()).contains("big deal");
        assertThat(facts.frames()).isNull();
        assertThat(facts.examples()).containsExactly("Example 1", "Example 2");
        assertThat(store.facts(UUID.randomUUID())).isEmpty();
    }

    @Test
    void keepsOneRowPerContentAndCountsWhatAMemberBoughtInTheWindow() {
        store.insert(comparison("hash-1", "First.", JOHN, 300, 40), now.minusSeconds(3600));
        store.insert(comparison("hash-1", "Second, dropped.", JANE, 300, 40), now);
        store.insert(comparison("hash-2", "Another.", JOHN, 200, 30), now.minusSeconds(2 * 86_400));

        assertThat(store.summary("hash-1")).contains("First.");
        assertThat(store.summary("nope")).isEmpty();
        assertThat(store.boughtSince(JOHN, now.minusSeconds(86_400))).isEqualTo(1);
        assertThat(store.boughtSince(JANE, now.minusSeconds(86_400))).isZero();
        assertThat(store.spendBetween(now.minusSeconds(3 * 86_400), now.plusSeconds(1)))
                .isEqualTo(List.of(new SenseComparisonSpendLine("gpt-4.1-mini", 2, 500, 70)));
    }

    private static NewComparison comparison(String hash, String summary, UUID user, int prompt, int completion) {
        return new NewComparison(
                hash, UUID.randomUUID(), UUID.randomUUID(), "uk", summary, "gpt-4.1-mini", prompt, completion, user);
    }
}
