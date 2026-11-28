package com.almonium.learning.cover.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class CoverLicenceTest {
    @Test
    void takesAFileWhoseAuthorDiedLongEnoughAgo() {
        assertThat(CoverLicence.basis(List.of("CC-PD-Mark", "PD-old-100-expired"), 100))
                .contains("PD-old-100-expired");
        assertThat(CoverLicence.basis(List.of("PD-Art (PD-old-100)"), 100)).contains("PD-Art (PD-old-100)");
    }

    @Test
    void leavesAFileWhoseAuthorDiedTooRecentlyForTheRule() {
        assertThat(CoverLicence.basis(List.of("CC-PD-Mark", "PD-old-80-expired"), 100))
                .isEmpty();
        assertThat(CoverLicence.basis(List.of("PD-old-80-expired"), 70)).contains("PD-old-80-expired");
    }

    @Test
    void leavesAFileThatIsOnlyFreeInTheUnitedStatesOrAsksForAttribution() {
        assertThat(CoverLicence.basis(List.of("PD US", "PD US expired", "PD Old"), 100))
                .isEmpty();
        assertThat(CoverLicence.basis(List.of("CC-BY-SA-4.0", "Self-published work"), 100))
                .isEmpty();
    }

    @Test
    void readsThePlainTagAsSeventyYearsAndAnAssumedOneAsNothing() {
        assertThat(CoverLicence.basis(List.of("CC-PD-Mark", "PD Old"), 70)).contains("PD Old");
        assertThat(CoverLicence.basis(List.of("PD Old"), 100)).isEmpty();
        assertThat(CoverLicence.basis(List.of("PD Old (assumed)", "PD-old missing SDC copyright status"), 70))
                .isEmpty();
    }

    @Test
    void readsTheNationalTagsOfASeventyYearTerm() {
        assertThat(CoverLicence.basis(List.of("PD US expired", "PD-UK"), 70)).contains("PD-UK");
        assertThat(CoverLicence.basis(List.of("PD France", "PD US expired"), 70))
                .contains("PD France");
        assertThat(CoverLicence.basis(List.of("PD-anon-70-EU", "PD-anon-expired"), 70))
                .contains("PD-anon-70-EU");
        assertThat(CoverLicence.basis(List.of("PD-UK"), 100)).isEmpty();
    }

    @Test
    void aFileFreeOnlyInTheUnitedStatesIsLeftForItsArtistsDeathToSettle() {
        assertThat(CoverLicence.freeInTheUnitedStates(List.of("PD US expired", "PD-US missing SDC copyright status")))
                .isTrue();
        assertThat(CoverLicence.freeInTheUnitedStates(List.of("PD US not renewed")))
                .isTrue();
        assertThat(CoverLicence.freeInTheUnitedStates(List.of("PD US Government", "CC-BY-4.0")))
                .isFalse();
    }

    @Test
    void takesAFileGivenToThePublicDomainByItsMaker() {
        assertThat(CoverLicence.basis(List.of("CC-zero"), 100)).contains("CC-zero");
    }
}
