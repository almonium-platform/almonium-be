package com.almonium.analyzer.translator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.almonium.analyzer.translator.model.enums.LanguageVariety;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

class VoiceCatalogueTest {
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
                LanguageVariety.EN_GB, VoiceCatalogue.Provider.GOOGLE, true, "en-GB", "en-GB-Chirp3-HD-Charon");
        assertThatThrownBy(() -> new VoiceCatalogue(List.of(british, british)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Duplicate");
        var substituted = new VoiceCatalogue.Voice(
                LanguageVariety.DE_CH, VoiceCatalogue.Provider.GOOGLE, true, "de-DE", "de-DE-Chirp3-HD-Charon");
        assertThatThrownBy(() -> new VoiceCatalogue(List.of(substituted)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("de-CH");
        var unnamed =
                new VoiceCatalogue.Voice(LanguageVariety.EN_GB, VoiceCatalogue.Provider.GOOGLE, true, "en-GB", null);
        assertThatThrownBy(() -> new VoiceCatalogue(List.of(unnamed))).isInstanceOf(IllegalArgumentException.class);
    }
}
