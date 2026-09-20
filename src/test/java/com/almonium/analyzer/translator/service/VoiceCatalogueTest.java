package com.almonium.analyzer.translator.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.analyzer.translator.model.enums.LanguageVariety;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class VoiceCatalogueTest {

    @Test
    void everyVarietyHasAVoice() {
        for (LanguageVariety variety : LanguageVariety.values()) {
            assertThat(VoiceCatalogue.googleLanguageCode(variety.getLanguage(), Optional.of(variety)))
                    .as(variety.getTag())
                    .isNotBlank();
        }
    }

    @Test
    void aVarietyGoogleCannotVoiceBorrowsTheDefaultsCode() {
        assertThat(VoiceCatalogue.googleLanguageCode(Language.DE, Optional.of(LanguageVariety.DE_CH)))
                .isEqualTo("de-DE");
        assertThat(VoiceCatalogue.googleLanguageCode(Language.EN, Optional.of(LanguageVariety.EN_GB)))
                .isEqualTo("en-GB");
    }

    @Test
    void aOneVarietyLanguageIsAskedForByItsBareCode() {
        assertThat(VoiceCatalogue.googleLanguageCode(Language.IT, Optional.empty()))
                .isEqualTo("it");
    }
}
