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
    void handsOutContentWordsInTheRankBandWithTheirFigureRankAndMeaningWhenTheyHaveOne() {
        // Ranks 2 to 50 are filler words that only push the playable ones past the first rank.
        for (int i = 0; i < 49; i++) {
            lexeme("de", "wort" + i, "DET", "6." + String.format("%02d", 99 - i));
        }
        UUID heimat = lexeme("de", "Heimat", "NOUN", "4.60");
        gloss(sense(heimat, 1), "uk", 1, "батьківщина");
        gloss(sense(heimat, 1), "uk", 2, "дім");
        sense(lexeme("de", "Brücke", "NOUN", "4.65"), 1);
        gloss(sense(lexeme("de", "Berlin", "PROPN", "4.70"), 1), "uk", 1, "Берлін");
        gloss(sense(lexeme("de", "Zeit", "NOUN", "7.00"), 1), "uk", 1, "час");
        gloss(sense(lexeme("de", "Angst", "NOUN", "4.50"), 1), "en", 1, "fear");
        lexeme("de", "n", "NOUN", "4.55");
        lexeme("de", "z.B.", "ADV", "4.40");
        lexeme("de", "Kaiser-Wilhelm", "NOUN", "4.30");

        List<HigherLowerWord> batch = words.sample("de", "uk", 10);

        assertThat(batch)
                .extracting(HigherLowerWord::headword)
                .containsExactlyInAnyOrder("Heimat", "Brücke", "Angst", "Kaiser-Wilhelm");
        HigherLowerWord placed = batch.stream()
                .filter(word -> word.headword().equals("Heimat"))
                .findFirst()
                .orElseThrow();
        assertThat(placed.gloss()).isEqualTo("батьківщина, дім");
        assertThat(placed.perMillion()).isCloseTo(39.81, within(0.01));
        // Zeit, the 49 fillers, Berlin and Brücke are all more common.
        assertThat(placed.rank()).isEqualTo(53);
        assertThat(batch)
                .filteredOn(word -> !word.headword().equals("Heimat"))
                .extracting(HigherLowerWord::gloss)
                .containsOnlyNulls();
    }

    @Test
    void letsTheLexemeWithAMeaningSpeakForItsLemmaThenTheOneWithMostSenses() {
        for (int i = 0; i < 49; i++) {
            lexeme("de", "wort" + i, "DET", "6.50");
        }
        UUID verb = lexeme("de", "können", "VERB", "4.00");
        sense(verb, 1);
        gloss(sense(verb, 2), "uk", 1, "могти");
        sense(verb, 3);
        gloss(sense(lexeme("de", "können", "NOUN", "4.00"), 1), "uk", 1, "уміння");
        UUID bare = lexeme("de", "Lauf", "NOUN", "3.90");
        sense(bare, 1);
        sense(bare, 2);
        gloss(sense(lexeme("de", "Lauf", "VERB", "3.90"), 1), "uk", 1, "біг");

        assertThat(words.sample("de", "uk", 10))
                .extracting(HigherLowerWord::headword, HigherLowerWord::gloss)
                .containsExactlyInAnyOrder(
                        org.assertj.core.groups.Tuple.tuple("können", "могти"),
                        org.assertj.core.groups.Tuple.tuple("Lauf", "біг"));
    }

    @Test
    void takesAtMostSoManyWordsFromEachQuarterZipfBand() {
        for (int i = 0; i < 49; i++) {
            lexeme("en", "filler" + i, "DET", "7.00");
        }
        for (int i = 0; i < 6; i++) {
            lexeme("en", "rare" + "abcdef".charAt(i), "NOUN", "3.1" + i);
        }
        lexeme("en", "common", "NOUN", "5.10");

        List<HigherLowerWord> batch = words.sample("en", "uk", 2);

        assertThat(batch).extracting(HigherLowerWord::headword).contains("common");
        assertThat(batch).filteredOn(word -> word.headword().startsWith("rare")).hasSize(2);
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
