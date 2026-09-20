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
 * <p>Variety identity is independent of audio availability; the row order is by learner share, and the first entry
 * of each language is its default. Identity is the BCP-47 tag alone: the provider voice behind a variety lives in
 * {@link com.almonium.analyzer.translator.service.VoiceCatalogue}, not here. Every language has a default, including languages with no selector in the clients.
 * A bare language tag means no finer regional distinction is supported yet.
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
    ZH_TW(Language.ZH, "zh-TW"),

    AF(Language.AF, "af"),
    SQ(Language.SQ, "sq"),
    AM(Language.AM, "am"),
    AR(Language.AR, "ar"),
    HY(Language.HY, "hy"),
    AS(Language.AS, "as"),
    AY(Language.AY, "ay"),
    AZ(Language.AZ, "az"),
    BM(Language.BM, "bm"),
    EU(Language.EU, "eu"),
    BE(Language.BE, "be"),
    BN(Language.BN, "bn"),
    BHO(Language.BHO, "bho"),
    BS(Language.BS, "bs"),
    BG(Language.BG, "bg"),
    CA(Language.CA, "ca"),
    CEB(Language.CEB, "ceb"),
    CO(Language.CO, "co"),
    HR(Language.HR, "hr"),
    CS(Language.CS, "cs"),
    DA(Language.DA, "da"),
    DV(Language.DV, "dv"),
    DOI(Language.DOI, "doi"),
    EO(Language.EO, "eo"),
    ET(Language.ET, "et"),
    EE(Language.EE, "ee"),
    FIL(Language.FIL, "fil"),
    FI(Language.FI, "fi"),
    FY(Language.FY, "fy"),
    GL(Language.GL, "gl"),
    KA(Language.KA, "ka"),
    EL(Language.EL, "el"),
    GN(Language.GN, "gn"),
    GU(Language.GU, "gu"),
    HT(Language.HT, "ht"),
    HA(Language.HA, "ha"),
    HAW(Language.HAW, "haw"),
    HE(Language.HE, "he"),
    HI(Language.HI, "hi"),
    HMN(Language.HMN, "hmn"),
    HU(Language.HU, "hu"),
    IS(Language.IS, "is"),
    IG(Language.IG, "ig"),
    ILO(Language.ILO, "ilo"),
    ID(Language.ID, "id"),
    GA(Language.GA, "ga"),
    IT(Language.IT, "it-IT"),
    JA(Language.JA, "ja-JP"),
    JV(Language.JV, "jv"),
    KN(Language.KN, "kn"),
    KK(Language.KK, "kk"),
    KM(Language.KM, "km"),
    RW(Language.RW, "rw"),
    GOM(Language.GOM, "gom"),
    KO(Language.KO, "ko-KR"),
    KRI(Language.KRI, "kri"),
    KU(Language.KU, "ku"),
    CKB(Language.CKB, "ckb"),
    KY(Language.KY, "ky"),
    LO(Language.LO, "lo"),
    LA(Language.LA, "la"),
    LV(Language.LV, "lv"),
    LN(Language.LN, "ln"),
    LT(Language.LT, "lt"),
    LG(Language.LG, "lg"),
    LB(Language.LB, "lb"),
    MK(Language.MK, "mk"),
    MAI(Language.MAI, "mai"),
    MG(Language.MG, "mg"),
    MS(Language.MS, "ms"),
    ML(Language.ML, "ml"),
    MT(Language.MT, "mt"),
    MI(Language.MI, "mi"),
    MR(Language.MR, "mr"),
    LUS(Language.LUS, "lus"),
    MN(Language.MN, "mn"),
    MY(Language.MY, "my"),
    NE(Language.NE, "ne"),
    NO(Language.NO, "no"),
    NY(Language.NY, "ny"),
    OR(Language.OR, "or"),
    OM(Language.OM, "om"),
    PS(Language.PS, "ps"),
    FA(Language.FA, "fa"),
    PL(Language.PL, "pl-PL"),
    PA(Language.PA, "pa"),
    QU(Language.QU, "qu"),
    RO(Language.RO, "ro"),
    RU(Language.RU, "ru-RU"),
    SM(Language.SM, "sm"),
    SA(Language.SA, "sa"),
    GD(Language.GD, "gd"),
    NSO(Language.NSO, "nso"),
    SR(Language.SR, "sr"),
    ST(Language.ST, "st"),
    SN(Language.SN, "sn"),
    SD(Language.SD, "sd"),
    SI(Language.SI, "si"),
    SK(Language.SK, "sk"),
    SL(Language.SL, "sl"),
    SO(Language.SO, "so"),
    SU(Language.SU, "su"),
    SW(Language.SW, "sw"),
    SV(Language.SV, "sv"),
    TL(Language.TL, "tl"),
    TG(Language.TG, "tg"),
    TA(Language.TA, "ta"),
    TT(Language.TT, "tt"),
    TE(Language.TE, "te"),
    TH(Language.TH, "th"),
    TI(Language.TI, "ti"),
    TS(Language.TS, "ts"),
    TR(Language.TR, "tr"),
    TK(Language.TK, "tk"),
    AK(Language.AK, "ak"),
    UK(Language.UK, "uk-UA"),
    UR(Language.UR, "ur"),
    UG(Language.UG, "ug"),
    UZ(Language.UZ, "uz"),
    VI(Language.VI, "vi"),
    CY(Language.CY, "cy"),
    XH(Language.XH, "xh"),
    YI(Language.YI, "yi"),
    YO(Language.YO, "yo"),
    ZU(Language.ZU, "zu");

    private final Language language;

    @JsonValue
    private final String tag;

    LanguageVariety(Language language, String tag) {
        this.language = language;
        this.tag = tag;
    }

    /** The selectable varieties of a language in display order, including the default for a language with no choice. */
    public static List<LanguageVariety> forLanguage(Language language) {
        return Arrays.stream(values())
                .filter(variety -> variety.language == language)
                .toList();
    }

    /** What a learner holds until they say otherwise: the first row for every supported language. */
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
