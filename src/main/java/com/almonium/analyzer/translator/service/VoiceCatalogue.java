package com.almonium.analyzer.translator.service;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.analyzer.translator.model.enums.LanguageVariety;
import java.util.Map;
import java.util.Optional;

/**
 * The one place a speech vendor is named (design V6, VoiceConfiguration). A variety's identity is its tag; what
 * Google is asked to synthesise is decided here, so a variety the current Google catalogue cannot voice falls back
 * to the nearest code it does have rather than failing the play.
 */
public final class VoiceCatalogue {

    /**
     * Google Cloud Text-to-Speech language codes, looked up on 2026-09-20. Austria, Switzerland (German and French),
     * Belgium (French) and the Latin American Spanish varieties have no voice of their own there and borrow the
     * default's; the design names Azure candidates for them, unlistened, which is why they are not wired yet.
     */
    private static final Map<LanguageVariety, String> GOOGLE_LANGUAGE_CODES = Map.ofEntries(
            Map.entry(LanguageVariety.EN_US, "en-US"),
            Map.entry(LanguageVariety.EN_GB, "en-GB"),
            Map.entry(LanguageVariety.EN_AU, "en-AU"),
            Map.entry(LanguageVariety.EN_IN, "en-IN"),
            Map.entry(LanguageVariety.ES_ES, "es-ES"),
            Map.entry(LanguageVariety.ES_MX, "es-US"),
            Map.entry(LanguageVariety.ES_AR, "es-US"),
            Map.entry(LanguageVariety.ES_CO, "es-US"),
            Map.entry(LanguageVariety.PT_BR, "pt-BR"),
            Map.entry(LanguageVariety.PT_PT, "pt-PT"),
            Map.entry(LanguageVariety.FR_FR, "fr-FR"),
            Map.entry(LanguageVariety.FR_CA, "fr-CA"),
            Map.entry(LanguageVariety.FR_BE, "fr-FR"),
            Map.entry(LanguageVariety.FR_CH, "fr-FR"),
            Map.entry(LanguageVariety.DE_DE, "de-DE"),
            Map.entry(LanguageVariety.DE_AT, "de-DE"),
            Map.entry(LanguageVariety.DE_CH, "de-DE"),
            Map.entry(LanguageVariety.NL_NL, "nl-NL"),
            Map.entry(LanguageVariety.NL_BE, "nl-BE"),
            Map.entry(LanguageVariety.ZH_CN, "cmn-CN"),
            Map.entry(LanguageVariety.ZH_TW, "cmn-TW"));

    private VoiceCatalogue() {}

    /** The code Google synthesises for what a learner chose, or for the bare language when there was no choice. */
    public static String googleLanguageCode(Language language, Optional<LanguageVariety> variety) {
        return variety.map(GOOGLE_LANGUAGE_CODES::get)
                .orElseGet(() -> language.name().toLowerCase());
    }
}
