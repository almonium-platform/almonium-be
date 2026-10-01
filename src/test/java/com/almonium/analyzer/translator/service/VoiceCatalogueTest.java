package com.almonium.analyzer.translator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.almonium.analyzer.translator.model.enums.LanguageVariety;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

class VoiceCatalogueTest {
    @Test
    void coversEveryVarietyAndAllowsOnlyDocumentedProviderAliases() throws Exception {
        var catalogue = new VoiceCatalogue();
        assertThat(catalogue.list(null)).hasSize(LanguageVariety.values().length);
        for (var language : com.almonium.analyzer.translator.model.enums.Language.values()) {
            assertThat(catalogue.list(language).stream()
                            .filter(row -> row.defaultVariety())
                            .count())
                    .isEqualTo(1);
        }
        assertThat(catalogue.requireVoice(LanguageVariety.NO).languageCode()).isEqualTo("nb-NO");
        assertThat(catalogue.requireVoice(LanguageVariety.TL).languageCode()).isEqualTo("fil-PH");
        assertThat(catalogue.requireVoice(LanguageVariety.FIL).voiceId()).isEqualTo("fil-ph-Neural2-D");
        assertThat(catalogue.requireVoice(LanguageVariety.AR).languageCode()).isEqualTo("ar-XA");
        assertThat(catalogue.requireVoice(LanguageVariety.SW).languageCode()).isEqualTo("sw-KE");
        assertThat(catalogue.requireVoice(LanguageVariety.AF).gender()).isEqualTo(VoiceCatalogue.Gender.FEMALE);
        var invalid = new VoiceCatalogue.Voice(
                LanguageVariety.NO,
                VoiceCatalogue.Provider.GOOGLE,
                true,
                "sv-SE",
                "sv-SE-Chirp3-HD-Charon",
                VoiceCatalogue.Gender.MALE);
        assertThatThrownBy(() -> new VoiceCatalogue(List.of(invalid))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void packagedCatalogueUsesNamedVoicesAndNeverSubstitutesUnavailableVarieties() throws Exception {
        VoiceCatalogue catalogue = new VoiceCatalogue();
        assertThat(catalogue.requireVoice(LanguageVariety.EN_GB).voiceId()).isEqualTo("en-GB-Chirp3-HD-Charon");
        assertThat(catalogue.requireVoice(LanguageVariety.IT).languageCode()).isEqualTo("it-IT");
        assertThat(catalogue.requireVoice(LanguageVariety.ZH_CN).languageCode()).isEqualTo("cmn-CN");
        assertThatThrownBy(() -> catalogue.requireVoice(LanguageVariety.DE_CH))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("422")
                .hasMessageContaining("de-CH");
        assertThatThrownBy(() -> catalogue.requireVoice(LanguageVariety.LA))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("422");
    }

    @Test
    void rejectsDuplicateRoutesAndCrossVarietyVoicesAtStartup() {
        var british = new VoiceCatalogue.Voice(
                LanguageVariety.EN_GB,
                VoiceCatalogue.Provider.GOOGLE,
                true,
                "en-GB",
                "en-GB-Chirp3-HD-Charon",
                VoiceCatalogue.Gender.MALE);
        assertThatThrownBy(() -> new VoiceCatalogue(List.of(british, british)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Duplicate");
        var substituted = new VoiceCatalogue.Voice(
                LanguageVariety.DE_CH,
                VoiceCatalogue.Provider.GOOGLE,
                true,
                "de-DE",
                "de-DE-Chirp3-HD-Charon",
                VoiceCatalogue.Gender.MALE);
        assertThatThrownBy(() -> new VoiceCatalogue(List.of(substituted)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("de-CH");
        var unnamed = new VoiceCatalogue.Voice(
                LanguageVariety.EN_GB, VoiceCatalogue.Provider.GOOGLE, true, "en-GB", null, VoiceCatalogue.Gender.MALE);
        assertThatThrownBy(() -> new VoiceCatalogue(List.of(unnamed))).isInstanceOf(IllegalArgumentException.class);
    }
}
