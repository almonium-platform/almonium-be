package com.almonium.learning.rights.engine;

import static org.assertj.core.api.Assertions.assertThat;

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
}
