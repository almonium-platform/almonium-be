package com.almonium.analyzer.analyzer.service;

import com.almonium.analyzer.translator.model.enums.Language;
import java.lang.Character.UnicodeScript;
import java.util.EnumSet;
import java.util.Set;

/**
 * Checks whether a word's letters belong to a writing system the given language uses, so a Cyrillic or CJK query on a
 * Latin-script profile (or vice versa) never calls external frequency or dictionary providers under the wrong language.
 */
public final class LanguageScripts {
    private static final Set<UnicodeScript> LATIN = EnumSet.of(UnicodeScript.LATIN);
    private static final Set<UnicodeScript> CYRILLIC = EnumSet.of(UnicodeScript.CYRILLIC);
    private static final Set<UnicodeScript> ARABIC = EnumSet.of(UnicodeScript.ARABIC);
    private static final Set<UnicodeScript> DEVANAGARI = EnumSet.of(UnicodeScript.DEVANAGARI);
    private static final Set<UnicodeScript> BENGALI = EnumSet.of(UnicodeScript.BENGALI);
    private static final Set<UnicodeScript> HAN = EnumSet.of(UnicodeScript.HAN);
    private static final Set<UnicodeScript> JAPANESE =
            EnumSet.of(UnicodeScript.HAN, UnicodeScript.HIRAGANA, UnicodeScript.KATAKANA);
    private static final Set<UnicodeScript> KOREAN = EnumSet.of(UnicodeScript.HANGUL, UnicodeScript.HAN);

    private LanguageScripts() {}

    public static boolean matches(Language language, String text) {
        Set<UnicodeScript> expected = expectedScripts(language);
        if (expected.isEmpty()) {
            return true;
        }
        Set<UnicodeScript> seen = EnumSet.noneOf(UnicodeScript.class);
        text.codePoints().filter(Character::isLetter).forEach(codePoint -> {
            UnicodeScript script = UnicodeScript.of(codePoint);
            if (script != UnicodeScript.COMMON
                    && script != UnicodeScript.INHERITED
                    && script != UnicodeScript.UNKNOWN) {
                seen.add(script);
            }
        });
        if (seen.isEmpty()) {
            return true;
        }
        return seen.stream().anyMatch(expected::contains);
    }

    private static Set<UnicodeScript> expectedScripts(Language language) {
        return switch (language) {
            case RU, UK, BE, BG, MK, CV, CE, BA, TT, KK, KY, TG, MN -> CYRILLIC;
            case SR -> EnumSet.of(UnicodeScript.CYRILLIC, UnicodeScript.LATIN);
            case EL -> EnumSet.of(UnicodeScript.GREEK);
            case HE, YI -> EnumSet.of(UnicodeScript.HEBREW);
            case AR, FA, UR, PS, CKB, SD, UG, ARQ, ARZ, AYH, MEY, PRS, SHU, BAL, BQI, GLK -> ARABIC;
            case KU, UZ, AZ, TK -> EnumSet.of(UnicodeScript.LATIN, UnicodeScript.CYRILLIC, UnicodeScript.ARABIC);
            case JA -> JAPANESE;
            case ZH, YUE, GAN, HAK, CDO -> HAN;
            case KO -> KOREAN;
            case HI, MR, NE, SA, BHO, MAI, AWA, BFY, BGQ, BHB, DHD, GOM, HNE, RMT -> DEVANAGARI;
            case BN, AS, CTG -> BENGALI;
            case PA -> EnumSet.of(UnicodeScript.GURMUKHI, UnicodeScript.ARABIC);
            case GU -> EnumSet.of(UnicodeScript.GUJARATI);
            case OR -> EnumSet.of(UnicodeScript.ORIYA);
            case TA -> EnumSet.of(UnicodeScript.TAMIL);
            case TE -> EnumSet.of(UnicodeScript.TELUGU);
            case KN -> EnumSet.of(UnicodeScript.KANNADA);
            case ML -> EnumSet.of(UnicodeScript.MALAYALAM);
            case SI -> EnumSet.of(UnicodeScript.SINHALA);
            case TH -> EnumSet.of(UnicodeScript.THAI);
            case LO -> EnumSet.of(UnicodeScript.LAO);
            case KM -> EnumSet.of(UnicodeScript.KHMER);
            case MY -> EnumSet.of(UnicodeScript.MYANMAR);
            case KA -> EnumSet.of(UnicodeScript.GEORGIAN);
            case HY -> EnumSet.of(UnicodeScript.ARMENIAN);
            case AM, TI -> EnumSet.of(UnicodeScript.ETHIOPIC);
            default -> LATIN;
        };
    }
}
