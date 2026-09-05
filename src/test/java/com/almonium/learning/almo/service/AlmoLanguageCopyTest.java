package com.almonium.learning.almo.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.almo.dto.AlmoOpenerDto;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class AlmoLanguageCopyTest {
    private final AlmoLanguageCopy copy = new AlmoLanguageCopy();

    @Test
    void namesTheChannelAfterTheLanguageInItsOwnName() {
        assertThat(copy.channelName(Language.DE)).isEqualTo("Almo · Deutsch");
        assertThat(copy.channelName(Language.FR)).isEqualTo("Almo · Français");
        assertThat(copy.channelName(Language.EN)).isEqualTo("Almo · English");
    }

    @Test
    void placeholderIsInTheTargetLanguage() {
        assertThat(copy.placeholder(Language.DE)).isEqualTo("Schreib auf Deutsch");
        assertThat(copy.placeholder(Language.IT)).isEqualTo("Scrivi in italiano");
    }

    @Test
    void unsupportedLanguageFallsBackToEnglishCopy() {
        assertThat(copy.placeholder(Language.NL)).isEqualTo("Write in English");
        assertThat(copy.channelName(Language.NL)).isEqualTo("Almo · Nederlands");
    }

    @Test
    void openersCarryOneQueueWordAndMarkIt() {
        List<AlmoOpenerDto> openers = copy.openers(Language.DE, Optional.of("dennoch"));

        assertThat(openers)
                .containsExactly(
                        new AlmoOpenerDto("Erzähl mir von deinem Tag", null),
                        new AlmoOpenerDto("Ein Satz mit dennoch", "dennoch"),
                        new AlmoOpenerDto("Frag mich meine Wörter ab", null));
    }

    @Test
    void openerWithNoWordToOfferIsLeftOutRatherThanLeftBlank() {
        assertThat(copy.openers(Language.DE, Optional.empty()))
                .extracting(AlmoOpenerDto::text)
                .containsExactly("Erzähl mir von deinem Tag", "Frag mich meine Wörter ab");
    }
}
