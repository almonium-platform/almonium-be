package com.almonium.infra.stats.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.almonium.analyzer.analyzer.repository.DiscoverLookupLog;
import com.almonium.analyzer.analyzer.repository.DiscoverLookupLog.AnsweredBy;
import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.config.PostgresContainer;
import com.almonium.infra.stats.repository.LanguagePulseStore.LearnerCounts;
import com.almonium.infra.stats.repository.LanguagePulseStore.LemmaCount;
import com.almonium.infra.stats.repository.LanguagePulseStore.LookupCounts;
import com.almonium.infra.stats.repository.LanguagePulseStore.MissRow;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

/** The languages pulse's counts against the real schema: the lookup log as Discover writes it, learners, lexicon. */
@DataJpaTest
@ImportTestcontainers(PostgresContainer.class)
@Sql(scripts = "classpath:db/add-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class LanguagePulseStoreTest {
    static final UUID JOHN = UUID.fromString("01956ce2-34fa-71f2-97b1-dacd41dbaba1");
    static final UUID JANE = UUID.fromString("01956cde-a541-7ac1-8b32-2896d096ecdf");

    @Autowired
    JdbcTemplate jdbc;

    LanguagePulseStore store;
    DiscoverLookupLog lookupLog;
    final Instant now = Instant.parse("2026-10-26T12:00:00Z");
    final Instant today = Instant.parse("2026-10-26T00:00:00Z");
    final Instant weekAgo = now.minus(Duration.ofDays(7));

    @BeforeEach
    void setUp() {
        store = new LanguagePulseStore(new NamedParameterJdbcTemplate(jdbc));
        lookupLog = new DiscoverLookupLog(jdbc);
    }

    @Test
    void countsLookupsTodayAndInTheWindowWithDistinctLookersAndLexiconAnswers() {
        lookup("Haus", Language.DE, AnsweredBy.LEXICON, "m:" + JOHN, now.minusSeconds(60));
        lookup("Ausgabe", Language.DE, AnsweredBy.PROVIDER, "m:" + JOHN, now.minus(Duration.ofDays(2)));
        lookup("ausgabe", Language.DE, AnsweredBy.NOTHING, "g:abc", now.minus(Duration.ofDays(3)));
        lookup("bloke", Language.EN, AnsweredBy.LEXICON, "g:abc", now.minus(Duration.ofDays(8)));

        assertThat(store.lookups(today, weekAgo)).containsExactly(new LookupCounts("DE", 1, 3, 2, 1));
    }

    @Test
    void ranksMissesByDistinctLookersAndLeavesLexiconAnswersOut() {
        lookup("Ausgabe", Language.DE, AnsweredBy.PROVIDER, "m:" + JOHN, now.minusSeconds(60));
        lookup("Ausgabe", Language.DE, AnsweredBy.PROVIDER, "g:abc", now.minusSeconds(60));
        lookup("Kerl", Language.DE, AnsweredBy.NOTHING, "g:abc", now.minusSeconds(60));
        lookup("Kerl", Language.DE, AnsweredBy.NOTHING, "g:abc", now.minusSeconds(60));
        lookup("Kerl", Language.DE, AnsweredBy.NOTHING, "g:abc", now.minusSeconds(60));
        lookup("Haus", Language.DE, AnsweredBy.LEXICON, "g:abc", now.minusSeconds(60));
        lookup("Brot", Language.DE, AnsweredBy.NOTHING, "g:abc", now.minusSeconds(60));

        assertThat(store.topMisses(weekAgo, 2))
                .containsExactly(new MissRow("DE", "ausgabe", 2, 2), new MissRow("DE", "kerl", 3, 1));
    }

    @Test
    void countsActiveLearnersAndThoseSeenRecently() {
        profile(JOHN, now.minusSeconds(60));
        profile(JANE, now.minus(Duration.ofDays(10)));
        learner(JOHN, "DE", true);
        learner(JANE, "DE", true);
        learner(JANE, "EN", false);

        assertThat(store.learners(today, weekAgo)).containsExactly(new LearnerCounts("DE", 2, 1, 1));
    }

    @Test
    void countsLemmasPerLanguageOnceAcrossPartsOfSpeech() {
        lexeme("fr", "clair", "ADJ");
        lexeme("fr", "clair", "VERB");
        lexeme("it", "casa", "NOUN");

        assertThat(store.lemmas()).contains(new LemmaCount("FR", 1), new LemmaCount("IT", 1));
    }

    void lookup(String word, Language language, AnsweredBy answeredBy, String requester, Instant at) {
        UUID member = requester.startsWith("m:") ? UUID.fromString(requester.substring(2)) : null;
        lookupLog.log(word, language, Language.EN, answeredBy, member, requester, at);
    }

    void profile(UUID user, Instant lastSeen) {
        jdbc.update(
                "insert into profile (id, last_login, last_seen_at) values (?, now(), ?)"
                        + " on conflict (id) do update set last_seen_at = excluded.last_seen_at",
                user,
                Timestamp.from(lastSeen));
    }

    void learner(UUID user, String language, boolean active) {
        jdbc.update(
                "insert into learner (id, user_id, language, self_reported_level, active, created_at)"
                        + " values (?, ?, ?, 'A1', ?, now())",
                UUID.randomUUID(),
                user,
                language,
                active);
    }

    void lexeme(String language, String lemma, String pos) {
        jdbc.update("""
                insert into lexicon.lexeme (id, language, headword, lemma, pos, homograph_no, kind, grammar,
                                            entry_version, status, created_at, updated_at)
                values (?, ?, ?, ?, ?, 0, 'word', '{}'::jsonb, 1, 'provisional', now(), now())
                """, UUID.randomUUID(), language, lemma, lemma, pos);
    }
}
