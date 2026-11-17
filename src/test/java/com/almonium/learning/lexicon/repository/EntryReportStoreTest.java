package com.almonium.learning.lexicon.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.almonium.config.PostgresContainer;
import com.almonium.learning.lexicon.dto.response.EntryReportGroup;
import com.almonium.learning.lexicon.model.enums.EntryReportReason;
import com.almonium.learning.lexicon.repository.EntryReportStore.NewReport;
import com.almonium.learning.lexicon.repository.EntryReportStore.Target;
import java.math.BigDecimal;
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

/** The dictionary reports against the real schema: what a flag attaches, how the queue groups and orders, resolving. */
@DataJpaTest
@ImportTestcontainers(PostgresContainer.class)
@Sql(scripts = "classpath:db/add-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class EntryReportStoreTest {
    static final UUID JOHN = UUID.fromString("01956ce2-34fa-71f2-97b1-dacd41dbaba1");
    static final UUID JANE = UUID.fromString("01956cde-a541-7ac1-8b32-2896d096ecdf");

    @Autowired
    JdbcTemplate jdbc;

    EntryReportStore store;
    final Instant now = Instant.parse("2026-11-17T12:00:00Z");

    @BeforeEach
    void setUp() {
        store = new EntryReportStore(new NamedParameterJdbcTemplate(jdbc));
    }

    @Test
    void attachesTheLexemeAndEntryVersionOfAFlaggedSense() {
        UUID lexeme = lexeme("de", "Bank", "Bank", "4.80", 3);
        UUID key = sense(lexeme, 2, "a place that keeps money");

        assertThat(store.senseTarget("de", key)).contains(new Target(lexeme, "Bank", 3));
        assertThat(store.senseTarget("fr", key)).isEmpty();
    }

    @Test
    void attachesTheMostCommonLexemeOfAFlaggedWordFoundByLemmaToo() {
        lexeme("en", "bow", "bow", "3.10", 1);
        UUID common = lexeme("en", "bow", "bow", "4.20", 2);
        lexeme("en", "running", "run", "5.00", 1);

        assertThat(store.wordTarget("en", "bow")).contains(new Target(common, "bow", 2));
        assertThat(store.wordTarget("en", "run")).map(Target::headword).contains("running");
        assertThat(store.wordTarget("en", "nope")).isEmpty();
    }

    @Test
    void groupsOpenReportsBySenseAndReasonWithTheMostCommonWordFirst() {
        UUID rare = lexeme("de", "Kerl", "Kerl", "3.00", 1);
        UUID common = lexeme("de", "Haus", "Haus", "5.50", 2);
        UUID rareSense = sense(rare, 1, "a bloke");
        UUID commonSense = sense(common, 1, "a building to live in");

        report("de", new Target(rare, "Kerl", 1), rareSense, EntryReportReason.WRONG_DEFINITION, JOHN, now);
        report("de", new Target(common, "Haus", 2), commonSense, EntryReportReason.BAD_EXAMPLE, JOHN, now);
        report(
                "de",
                new Target(common, "Haus", 2),
                commonSense,
                EntryReportReason.BAD_EXAMPLE,
                JANE,
                now.plusSeconds(5));
        report("de", new Target(common, "Haus", 2), null, EntryReportReason.WRONG_SOUND, JANE, now);

        List<EntryReportGroup> groups = store.openGroups();

        assertThat(groups)
                .extracting(EntryReportGroup::headword, EntryReportGroup::reason, EntryReportGroup::reports)
                .containsExactly(
                        org.assertj.core.groups.Tuple.tuple("Haus", EntryReportReason.BAD_EXAMPLE, 2L),
                        org.assertj.core.groups.Tuple.tuple("Haus", EntryReportReason.WRONG_SOUND, 1L),
                        org.assertj.core.groups.Tuple.tuple("Kerl", EntryReportReason.WRONG_DEFINITION, 1L));
        EntryReportGroup first = groups.getFirst();
        assertThat(first.language()).isEqualTo("DE");
        assertThat(first.definition()).isEqualTo("a building to live in");
        assertThat(first.reporters()).isEqualTo(2);
        assertThat(first.zipf()).isEqualByComparingTo(new BigDecimal("5.50"));
        assertThat(first.currentVersion()).isEqualTo(2);
        assertThat(groups.get(1).senseKey()).isNull();
        assertThat(store.openReports()).hasSize(4);
    }

    @Test
    void resolvesOneGroupIncludingAWordLevelOne() {
        UUID lexeme = lexeme("de", "Haus", "Haus", "5.50", 1);
        UUID key = sense(lexeme, 1, "a building");
        Target target = new Target(lexeme, "Haus", 1);
        report("de", target, null, EntryReportReason.WRONG_SOUND, JOHN, now);
        report("de", target, key, EntryReportReason.WRONG_SOUND, JOHN, now);

        assertThat(store.resolve("de", "Haus", null, EntryReportReason.WRONG_SOUND, now))
                .isEqualTo(1);
        assertThat(store.openGroups())
                .singleElement()
                .extracting(EntryReportGroup::senseKey)
                .isEqualTo(key);
        assertThat(store.resolve("de", "Haus", key, EntryReportReason.WRONG_SOUND, now))
                .isEqualTo(1);
        assertThat(store.openGroups()).isEmpty();
    }

    void report(String language, Target target, UUID key, EntryReportReason reason, UUID reporter, Instant at) {
        store.insert(new NewReport(language, target, key, reason, null, "Das Haus ist alt.", "kafka-de", reporter), at);
    }

    UUID lexeme(String language, String headword, String lemma, String zipf, int version) {
        UUID id = UUID.randomUUID();
        jdbc.update("""
                insert into lexicon.lexeme (id, language, headword, lemma, pos, homograph_no, kind, grammar, zipf,
                                            entry_version, status, created_at, updated_at)
                values (?, ?, ?, ?, 'NOUN', (select count(*) from lexicon.lexeme where lemma = ?), 'word',
                        '{}'::jsonb, ?, ?, 'provisional', now(), now())
                """, id, language, headword, lemma, lemma, new BigDecimal(zipf), version);
        return id;
    }

    UUID sense(UUID lexeme, int position, String definition) {
        UUID key = UUID.randomUUID();
        jdbc.update("""
                insert into lexicon.sense (id, lexeme_id, sense_key, position, definition, register)
                values (?, ?, ?, ?, ?, 'neutral')
                """, UUID.randomUUID(), lexeme, key, position, definition);
        return key;
    }
}
