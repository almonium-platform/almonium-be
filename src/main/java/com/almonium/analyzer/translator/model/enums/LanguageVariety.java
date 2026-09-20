package com.almonium.analyzer.translator.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.Getter;

/**
 * Which English, which German (design V, table V5): the catalogue of varieties a learner may pick for a language.
 *
 * <p>A variety exists here when it can be heard and labelled; the row order is by learner share, and the first entry
 * of each language is its default. Identity is the BCP-47 tag alone: the provider voice behind a variety lives in
 * {@link com.almonium.analyzer.translator.service.VoiceCatalogue}, not here. A language with a single supported
 * variety has no rows; {@link #forLanguage} is then empty and {@link #defaultFor} answers with the bare language.
 */
@Getter
public enum LanguageVariety {
    EN_US(Language.EN, "en-US"),
    EN_GB(Language.EN, "en-GB"),
    EN_AU(Language.EN, "en-AU"),
    EN_IN(Language.EN, "en-IN"),

    ES_ES(Language.ES, "es-ES"),
    ES_MX(Language.ES, "es-MX"),
    ES_AR(Language.ES, "es-AR"),
    ES_CO(Language.ES, "es-CO"),

    PT_BR(Language.PT, "pt-BR"),
    PT_PT(Language.PT, "pt-PT"),

    FR_FR(Language.FR, "fr-FR"),
    FR_CA(Language.FR, "fr-CA"),
    FR_BE(Language.FR, "fr-BE"),
    FR_CH(Language.FR, "fr-CH"),

    DE_DE(Language.DE, "de-DE"),
    DE_AT(Language.DE, "de-AT"),
    DE_CH(Language.DE, "de-CH"),

    NL_NL(Language.NL, "nl-NL"),
    NL_BE(Language.NL, "nl-BE"),

    ZH_CN(Language.ZH, "zh-CN"),
    ZH_TW(Language.ZH, "zh-TW");

    private final Language language;

    @JsonValue
    private final String tag;

    LanguageVariety(Language language, String tag) {
        this.language = language;
        this.tag = tag;
    }

    /** The selectable varieties of a language in display order, empty when the language has only one. */
    public static List<LanguageVariety> forLanguage(Language language) {
        return Arrays.stream(values())
                .filter(variety -> variety.language == language)
                .toList();
    }

    /** What a learner holds until they say otherwise: the first row, or nothing for a single-variety language. */
    public static Optional<LanguageVariety> defaultFor(Language language) {
        return forLanguage(language).stream().findFirst();
    }

    public boolean isDefault() {
        return defaultFor(language).filter(this::equals).isPresent();
    }

    @JsonCreator
    public static LanguageVariety fromTag(String tag) {
        return Arrays.stream(values())
                .filter(variety -> variety.tag.equalsIgnoreCase(tag))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown language variety: " + tag));
    }
}
