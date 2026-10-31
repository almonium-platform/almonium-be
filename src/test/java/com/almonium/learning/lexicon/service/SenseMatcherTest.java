package com.almonium.learning.lexicon.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.almonium.learning.lexicon.service.SenseMatcher.Candidate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class SenseMatcherTest {
    private static final UUID RIBBON = UUID.randomUUID();
    private static final UUID VOLUME = UUID.randomUUID();
    private static final UUID TIE = UUID.randomUUID();

    @Test
    void aSenseCitingTheSameEvidenceKeepsItsKeyEvenWhenItMovesToAnotherLexemeAndIsReworded() {
        Map<UUID, Candidate> old = new LinkedHashMap<>();
        old.put(RIBBON, sense("Band", "NOUN", "ein schmaler Streifen aus Stoff", "ribbon", "1.1"));
        old.put(VOLUME, sense("Band", "NOUN", "ein einzelnes Buch einer Reihe", "volume", "2.1"));

        SenseMatcher.Result result = SenseMatcher.match(
                old,
                List.of(
                        sense("Band", "NOUN", "ein Buch, das zu einer Reihe gehört", "volume", "2.1"),
                        sense("Band", "NOUN", "schmaler Streifen aus Stoff zum Binden", "ribbon", "1.1")));

        assertThat(result.keys()).containsExactly(VOLUME, RIBBON);
        assertThat(result.retired()).isEmpty();
    }

    @Test
    void withoutCitationsTheSameGlossesAndWordingCarryTheKey() {
        Map<UUID, Candidate> old = Map.of(
                RIBBON,
                new Candidate(
                        "chuckle",
                        "VERB",
                        "to laugh quietly, especially because something is funny",
                        Map.of("uk", "посміюватися", "de", "leise lachen"),
                        List.of()));

        SenseMatcher.Result result = SenseMatcher.match(
                old,
                List.of(new Candidate(
                        "chuckle",
                        "VERB",
                        "to laugh quietly at something funny",
                        Map.of("uk", "посміюватися", "de", "leise lachen"),
                        List.of())));

        assertThat(result.keys()).containsExactly(RIBBON);
    }

    @Test
    void aNewMeaningGetsAFreshKeyAndADroppedOneIsRetiredTowardsItsNearestSuccessor() {
        Map<UUID, Candidate> old = new LinkedHashMap<>();
        old.put(RIBBON, sense("Band", "NOUN", "ein schmaler Streifen aus Stoff", "ribbon", "1.1"));
        old.put(TIE, sense("Band", "NOUN", "eine enge Verbindung zwischen Menschen", "bond", "3.1"));

        SenseMatcher.Result result = SenseMatcher.match(
                old,
                List.of(
                        sense("Band", "NOUN", "ein schmaler Streifen aus Stoff", "ribbon", "1.1"),
                        sense("Band", "NOUN", "eine Gruppe, die zusammen Musik macht", "band", "4.1")));

        assertThat(result.keys().getFirst()).isEqualTo(RIBBON);
        assertThat(result.keys().get(1)).isNotIn(RIBBON, TIE);
        assertThat(result.retired()).containsOnlyKeys(TIE);
    }

    @Test
    void twoNewSensesNeverShareAKey() {
        Map<UUID, Candidate> old = Map.of(RIBBON, sense("Band", "NOUN", "Streifen aus Stoff", "ribbon", "1.1"));

        SenseMatcher.Result result = SenseMatcher.match(
                old,
                List.of(
                        sense("Band", "NOUN", "Streifen aus Stoff", "ribbon", "1.1"),
                        sense("Band", "NOUN", "Streifen aus Stoff", "ribbon", "1.1")));

        assertThat(result.keys()).doesNotHaveDuplicates().contains(RIBBON);
    }

    private static Candidate sense(String lemma, String pos, String definition, String english, String cited) {
        return new Candidate(lemma, pos, definition, Map.of("en", english), List.of(cited));
    }
}
