package com.almonium.learning.rights.engine;

import static org.assertj.core.api.Assertions.assertThat;

import com.almonium.learning.rights.engine.CountryTerms.CandidateWindow;
import org.junit.jupiter.api.Test;

class CountryTermsTest {
    @Test
    void everyListedCountryNamesItsRuleAndTheRestFallBackToLifePlusSeventy() {
        assertThat(CountryTerms.rule("MX")).isEqualTo("Author's life + 100 years");
        assertThat(CountryTerms.rule("us")).startsWith("95 years after publication");
        assertThat(CountryTerms.rule("CA")).contains("1971");
        assertThat(CountryTerms.rule("DE")).isEqualTo(CountryTerms.DEFAULT_RULE);
        assertThat(CountryTerms.rule("ZZ")).isEqualTo(CountryTerms.DEFAULT_RULE);
    }

    @Test
    void aRuleIsNamedForEveryCountryThatHasItsOwnTerm() {
        for (String country : CountryTerms.countries()) {
            boolean listed = CountryTerms.of(country) != CountryTerms.LIFE_PLUS_70;
            assertThat(CountryTerms.rule(country).equals(CountryTerms.DEFAULT_RULE))
                    .as(country)
                    .isEqualTo(!listed);
        }
    }

    @Test
    void candidateWindowsReflectEachCountrysTerm() {
        assertThat(CountryTerms.candidateWindow("UA", 2027))
                .isEqualTo(new CandidateWindow(java.util.List.of(2027), null));
        assertThat(CountryTerms.candidateWindow("DE", 2027))
                .isEqualTo(new CandidateWindow(java.util.List.of(2027), null));
        assertThat(CountryTerms.candidateWindow("CA", 2027))
                .isEqualTo(new CandidateWindow(java.util.List.of(2027), null));
        assertThat(CountryTerms.candidateWindow("US", 2027))
                .isEqualTo(new CandidateWindow(java.util.List.of(2027), 1931));
        assertThat(CountryTerms.candidateWindow("US", 2074))
                .isEqualTo(new CandidateWindow(java.util.List.of(2074), null));
        assertThat(CountryTerms.candidateWindow("ES", 2027))
                .isEqualTo(new CandidateWindow(java.util.List.of(2017), null));
        assertThat(CountryTerms.candidateWindow("ES", 2065))
                .isEqualTo(new CandidateWindow(java.util.List.of(2055, 2065), null));
        assertThat(CountryTerms.candidateWindow("MX", 2027))
                .isEqualTo(new CandidateWindow(java.util.List.of(1997), null));
        assertThat(CountryTerms.candidateWindow("RU", 2027))
                .isEqualTo(new CandidateWindow(java.util.List.of(2023, 2027), null));
        assertThat(CountryTerms.candidateWindow("FR", 2027))
                .isEqualTo(new CandidateWindow(java.util.List.of(2027, 2017, 2010, 2008, 2001), null));
    }
}
