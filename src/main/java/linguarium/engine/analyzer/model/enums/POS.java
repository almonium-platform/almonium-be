package linguarium.engine.analyzer.model.enums;

import lombok.Getter;

@Getter
public enum POS {
    HYPHEN("HYPH"),
    COORDINATING_CONJUNCTION("CC"),
    CARDINAL_NUMBER("CD"),
    DETERMINER("DT"),
    EXISTENTIAL_THERE("EX"),
    FOREIGN_WORD("FW"),
    SUBORDINATING_CONJUNCTION("IN"),
    ADJECTIVE("JJ"),
    ADJECTIVE_COMPARATIVE("JJR"),
    ADJECTIVE_SUPERLATIVE("JJS"),
    LIST_ITEM_MARKER("LS"),
    MODAL("MD"),
    NOUN("NN"),
    NOUN_PLURAL("NNS"),
    PROPER_NOUN_SINGULAR("NNP"),
    PROPER_NOUN_PLURAL("NNPS"),
    PREDETERMINER("PDT"),
    POSSESSIVE_ENDING("POS"),
    PERSONAL_PRONOUN("PRP"),
    POSSESSIVE_PRONOUN("PRP$"),
    ADVERB("RB"),
    ADVERB_COMPARATIVE("RBR"),
    ADVERB_SUPERLATIVE("RBS"),
    PARTICLE("RP"),
    SYMBOL("SYM"),
    TO("TO"),
    INTERJECTION("UH"),
    VERB("VB"),
    VERB_PAST_TENSE("VBD"),
    VERB_GERUND_OR_PRESENT_PARTICIPLE("VBG"),
    VERB_PAST_PARTICIPLE("VBN"),
    VERB_NON_3RD_PERSONAL_SINGULAR_PRESENT("VBP"),
    VERB_3RD_PERSONAL_SINGULAR_PRESENT("VBZ"),
    WH_DETERMINER("WDT"),
    WH_PRONOUN("WP"),
    POSSESSIVE_WH_PRONOUN("WP$"),
    WH_ADVERB("WRB");

    private final String code;

    POS(String code) {
        this.code = code;
    }

    public static POS fromString(String text) {
        for (POS pos : POS.values()) {
            if (pos.code.equalsIgnoreCase(text)) {
                return pos;
            }
        }
        throw new IllegalArgumentException("Can't find POS for: " + text);
    }

    public boolean isAdjective() {
        return this.equals(ADJECTIVE) || this.equals(ADJECTIVE_COMPARATIVE) || this.equals(ADJECTIVE_SUPERLATIVE);
    }
}
