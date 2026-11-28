package com.almonium.learning.play.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.config.PostgresContainer;
import com.almonium.learning.play.dto.response.HigherLowerBest;
import com.almonium.learning.play.dto.response.HigherLowerWord;
import java.math.BigDecimal;
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

/** Higher or Lower against the real schema: which words a batch may hold, their figures and ranks, and the best. */
@DataJpaTest
@ImportTestcontainers(PostgresContainer.class)
@Sql(scripts = "classpath:db/add-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class HigherLowerStoresTest {
    static final UUID JOHN = UUID.fromString("01956ce2-34fa-71f2-97b1-dacd41dbaba1");

    @Autowired
    JdbcTemplate jdbc;

    HigherLowerWordStore words;
    HigherLowerBestStore bests;

    @BeforeEach
    void setUp() {
        words = new HigherLowerWordStore(new NamedParameterJdbcTemplate(jdbc));
        bests = new HigherLowerBestStore(new NamedParameterJdbcTemplate(jdbc));
    }

    @Test
    void handsOutGlossedContentWordsInTheRankBandWithTheirFigureAndRank() {
        // Ranks 1 to 49 are filler words that only push the playable ones past the first rank.
        for (int i = 0; i < 49; i++) {
            lexeme("de", "wort" + i, "DET", "6." + String.format("%02d", 99 - i));
        }
        UUID heimat = lexeme("de", "Heimat", "NOUN", "4.60");
        gloss(sense(heimat, 1), "uk", 1, "батьківщина");
        gloss(sense(heimat, 1), "uk", 2, "дім");
        UUID noMeaning = lexeme("de", "Brücke", "NOUN", "4.65");
        sense(noMeaning, 1);
        UUID name = lexeme("de", "Berlin", "PROPN", "4.70");
        gloss(sense(name, 1), "uk", 1, "Берлін");
        UUID tooCommon = lexeme("de", "Zeit", "NOUN", "7.00");
        gloss(sense(tooCommon, 1), "uk", 1, "час");
        UUID englishOnly = lexeme("de", "Angst", "NOUN", "4.50");
        gloss(sense(englishOnly, 1), "en", 1, "fear");

        List<HigherLowerWord> batch = words.sample("de", "uk", 10);

        assertThat(batch).singleElement().satisfies(word -> {
            assertThat(word.headword()).isEqualTo("Heimat");
            assertThat(word.gloss()).isEqualTo("батьківщина, дім");
            assertThat(word.perMillion()).isCloseTo(39.81, within(0.01));
            // Zeit, the 49 fillers, Berlin and Brücke are all more common.
            assertThat(word.rank()).isEqualTo(53);
        });
    }

    @Test
    void takesTheMeaningOfTheFirstSenseThatHasOneAndTheLexemeWithMostSenses() {
        for (int i = 0; i < 49; i++) {
            lexeme("de", "wort" + i, "DET", "6.50");
        }
        UUID verb = lexeme("de", "können", "VERB", "4.00");
        sense(verb, 1);
        gloss(sense(verb, 2), "uk", 1, "могти");
        sense(verb, 3);
        UUID noun = lexeme("de", "können", "NOUN", "4.00");
        gloss(sense(noun, 1), "uk", 1, "уміння");

        assertThat(words.sample("de", "uk", 10))
                .singleElement()
                .satisfies(word -> assertThat(word.gloss()).isEqualTo("могти"));
    }

    @Test
    void keepsTheLongestRunPerLanguage() {
        assertThat(bests.lockBest(JOHN, Language.DE)).isEmpty();

        bests.keepLonger(JOHN, Language.DE, 6);
        bests.keepLonger(JOHN, Language.DE, 4);
        bests.keepLonger(JOHN, Language.FR, 2);

        assertThat(bests.lockBest(JOHN, Language.DE)).contains(6);
        assertThat(bests.bests(JOHN))
                .containsExactly(new HigherLowerBest(Language.DE, 6), new HigherLowerBest(Language.FR, 2));
    }

    UUID lexeme(String language, String lemma, String pos, String zipf) {
        UUID id = UUID.randomUUID();
        jdbc.update("""
                insert into lexicon.lexeme (id, language, headword, lemma, pos, homograph_no, kind, grammar, zipf,
                                            entry_version, status, created_at, updated_at)
                values (?, ?, ?, ?, ?, 1, 'word', '{}'::jsonb, ?, 1, 'provisional', now(), now())
                """, id, language, lemma, lemma, pos, new BigDecimal(zipf));
        return id;
    }

    UUID sense(UUID lexeme, int position) {
        UUID existing = jdbc
                .query(
                        "select id from lexicon.sense where lexeme_id = ? and position = ?",
                        (row, index) -> row.getObject("id", UUID.class),
                        lexeme,
                        position)
                .stream()
                .findFirst()
                .orElse(null);
        if (existing != null) {
            return existing;
        }
        UUID id = UUID.randomUUID();
        jdbc.update("""
                insert into lexicon.sense (id, lexeme_id, sense_key, position, definition)
                values (?, ?, ?, ?, 'a definition')
                """, id, lexeme, UUID.randomUUID(), position);
        return id;
    }

    void gloss(UUID sense, String fluent, int rank, String text) {
        jdbc.update("""
                insert into lexicon.sense_gloss (id, sense_id, fluent_language, gloss, rank)
                values (?, ?, ?, ?, ?)
                """, UUID.randomUUID(), sense, fluent, text, rank);
    }
}
