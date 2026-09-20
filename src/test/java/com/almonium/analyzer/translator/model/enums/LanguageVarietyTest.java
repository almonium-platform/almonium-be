package com.almonium.analyzer.translator.model.enums;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class LanguageVarietyTest {

    @Test
    void theFirstRowOfALanguageIsItsDefault() {
        assertThat(LanguageVariety.defaultFor(Language.EN)).contains(LanguageVariety.EN_US);
        assertThat(LanguageVariety.defaultFor(Language.DE)).contains(LanguageVariety.DE_DE);
        assertThat(LanguageVariety.defaultFor(Language.PT)).contains(LanguageVariety.PT_BR);
        assertThat(LanguageVariety.EN_US.isDefault()).isTrue();
        assertThat(LanguageVariety.EN_GB.isDefault()).isFalse();
    }

    @Test
    void aOneVarietyLanguageHasNoRows() {
        assertThat(LanguageVariety.forLanguage(Language.IT)).isEmpty();
        assertThat(LanguageVariety.defaultFor(Language.UK)).isEmpty();
    }

    @Test
    void theWireFormIsTheTag() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        assertThat(mapper.writeValueAsString(LanguageVariety.DE_CH)).isEqualTo("\"de-CH\"");
        assertThat(mapper.readValue("\"de-ch\"", LanguageVariety.class)).isEqualTo(LanguageVariety.DE_CH);
        assertThatThrownBy(() -> LanguageVariety.fromTag("de-XX")).isInstanceOf(IllegalArgumentException.class);
    }
}
