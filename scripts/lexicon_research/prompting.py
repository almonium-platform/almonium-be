"""The writer prompt, shared by every runner: a fixed system block per language (rules, inventory,
schema example) so provider caches can serve it, and a one-line user message with the headword.

Prompts v4 and v4.1 (schema 4) write the core of an entry only: lexemes, spellings, pronunciation, grammar and
the forms a learner must be told, senses with definition, example, frames, level, labels, regions and
antonyms, and typed relations. Glosses and review cues are left to later passes that run per sense,
and the regular forms come from Wiktionary's tables. A v4 entry is written beside the older ones, in
writer directories named with WRITER_SUFFIX, so a v3 entry never blocks or mixes with it.

The directory is named after the shape (schema 4 is "-v4"), not after the prompt: v4.1 and every later
prompt of the same shape write into the same directories, so a word written under v4 is never bought
again because the prompt's wording moved. What an entry was written with is in its meta: the prompt
version and prompt_sha, the hash of the exact system prompt. A prompt change is applied to written
entries by selecting the ones it concerns, never by starting a new directory."""
import hashlib
import json
import os
from pathlib import Path

HERE = Path("scripts/lexicon_research")
# Which shape each prompt writes. Three shapes exist, and their numbers are the schema versions:
#   2  the first dictionary (prompts v1 to v3): glosses and review cues inside every sense, one register label
#   3  October 2026 (v4oct, v5, v6): schema 2 plus regions by country or UN area with an extent, domains,
#      pronunciation notes and, from v5, the Wiktionary senses each sense was written from ("from")
#   4  November 2026 (v4, v4.1, v4.2, v4.3): the core only, without glosses and cues; labels on separate axes,
#      frames, spellings by variety, multiword and contraction lexemes, typed relations. v4.2 adds October's
#      rules for a headword that is also an inflected form, its regions (a country or UN area with an
#      extent) and pronunciation notes, the parts of a compound, and the politeness label. From v4.3 a
#      language its own reviewed example entries (gold/<lang>.json) and states five rules a reviewer of
#      another model family found the entries bending
# Entries written between 11 and 17 November 2026 call shape 4 "schema 3" in their meta, because the two
# lines were numbered apart; schema_of_meta() reads them right. The file prompts/entry_writer/v4.md is
# November's; October's prompt of that name is v4oct.md.
SCHEMA_OF = {"v1": "2", "v2": "2", "v3": "2", "v4oct": "3", "v5": "3", "v6": "3", "v4": "4", "v4.1": "4", "v4.2": "4", "v4.3": "4", "v4.4": "4", "v4.5": "4", "v4.6": "4", "v4.7": "4"}
# LEXICON_PROMPT_VERSION=v3 writes the old shape (with glosses and cues) into the old directories
PROMPT_VERSION = os.environ.get("LEXICON_PROMPT_VERSION", "v4.7")
SCHEMA_VERSION = SCHEMA_OF[PROMPT_VERSION]
SCHEMA_PATH = HERE / ("entry.schema.v4.json" if SCHEMA_VERSION == "4" else "entry.schema.json")
WRITER_SUFFIX = "-v4" if SCHEMA_VERSION == "4" else ""
# The languages a learner studies, the ones entries are written for. Ukrainian is a bridge language
# only: glosses are written in it, entries are not (owner, 2026-10 and again 16 November 2026).
TARGET_LANGUAGES = ("en", "de", "fr", "es", "it", "pt")
# Languages being tried: they have their settings and can be written by naming them, but no list is written for
# them by default and nothing is published until a pilot has been read and their examples reviewed.
PILOT_LANGUAGES = ("ru", "ja")


def schema_of_meta(meta):
    """The shape an entry's meta names, with November's first week read as schema 4."""
    version = str(meta.get("schema_version") or "")
    return "4" if version == "3" and str(meta.get("prompt_version") or "").startswith("v4") else version


LANG_NAME = {"en": "English", "de": "German", "uk": "Ukrainian", "fr": "French", "es": "Spanish", "it": "Italian", "pt": "Portuguese", "ru": "Russian", "ja": "Japanese"}
VARIETIES = {"en": ["en-US", "en-GB"], "de": ["de-DE"], "uk": ["uk-UA"], "ru": ["ru-RU"], "ja": ["ja-JP"], "fr": ["fr-FR"], "es": ["es-ES", "es-MX"],
             "it": ["it-IT"], "pt": ["pt-BR", "pt-PT"]}
# A region is the language with a country (es-EC) or one of these UN M.49 areas, so that a word can be Canarian
# or Andean without the product offering that accent. The backend's EntrySchema holds the same list.
UN_REGIONS = {"419": "Latin America", "029": "the Caribbean", "005": "South America", "013": "Central America",
              "021": "Northern America", "150": "Europe", "002": "Africa", "142": "Asia", "009": "Oceania"}
DOMAINS = json.loads((Path(__file__).parent / "entry.schema.json").read_text())["$defs"]["sense"]["properties"]["domain"]["enum"]
GLOSS = {"en": "uk, de, pl", "de": "en, uk, pl", "uk": "en, de, pl", "fr": "en, uk, de", "es": "en, uk, de",
         "it": "en, uk, de", "pt": "en, uk, de"}
# What the writer lists under "forms". English, German and Ukrainian give the whole paradigm, as the
# v1 prompt did. The Romance languages give the principal parts only: a full verb paradigm is fifty
# forms that follow from the conjugation class by rule, so listing them would cost tokens and invite
# slips where a table does the job.
ROMANCE_FORMS = ("The principal parts, not the whole paradigm: nouns give singular and plural with gender in grammar; "
                 "adjectives give masculine and feminine, singular and plural; verbs give the infinitive, the present "
                 "indicative 1sg 2sg 3sg 1pl 2pl 3pl, the past participle and the {past} 1sg, with the conjugation "
                 "class and whether it is irregular in grammar. Clitic and compound forms are not listed.")
FORMS_RULE = {
    "en": "The complete paradigm for the language: verbs give base, 3sg present, past, past participle, present participle; nouns give singular and plural.",
    "de": "The complete paradigm for the language: nouns give nominative singular and plural with gender in grammar; verbs give infinitive, 3sg present, 3sg preterite, past participle.",
    "uk": "The complete paradigm for the language: nouns give all seven cases singular and plural; verbs give infinitive, present or future 1sg 2sg 3sg 1pl 2pl 3pl, past masculine feminine neuter plural, and the aspect pair in grammar.",
    "fr": ROMANCE_FORMS.format(past="passé composé (auxiliary and participle) and the imparfait"),
    "es": ROMANCE_FORMS.format(past="pretérito indefinido"),
    "it": ROMANCE_FORMS.format(past="passato prossimo (auxiliary and participle)"),
    "pt": ROMANCE_FORMS.format(past="pretérito perfeito"),
}

SCHEMA_EXAMPLE = json.dumps({
    "language": "en", "headword": "lead",
    "lexemes": [{
        "lemma": "lead", "pos": "VERB", "kind": "word", "grammar": {"irregular": True},
        "forms": [{"form": "lead", "features": {"VerbForm": "Inf"}},
                  {"form": "leads", "features": {"Tense": "Pres", "Person": 3, "Number": "Sing"}},
                  {"form": "led", "features": {"Tense": "Past"}, "ipa": {"en-US": "/ˈlɛd/", "en-GB": "/ˈlɛd/"}}],
        "pronunciations": [{"variety": "en-US", "ipa": "/ˈliːd/"}, {"variety": "en-GB", "ipa": "/ˈliːd/"}],
        "senses": [{
            "definition": "to show someone the way by going in front of them",
            "grammar": {"valency": "Tr"}, "register": "neutral", "cefr": "A2",
            "glosses": {"uk": "вести", "de": "führen", "pl": "prowadzić"},
            "example": "She led us through the dark corridor to the exit.",
            "cues": [{"kind": "wording", "text": "to go first so that others can follow you"},
                     {"kind": "cloze", "text": "She ___ us through the dark corridor to the exit."}]}],
        "relations": [{"type": "family", "lemma": "leader", "pos": "NOUN"},
                      {"type": "confusable", "lemma": "lead", "pos": "NOUN", "why": "same spelling, different vowel"}]
    }]}, ensure_ascii=False, indent=1)


def prompt_sha(lang, version=None):
    """Twelve hex digits of the exact system prompt a language is written with; kept in each entry's meta."""
    return hashlib.sha256(system_prompt(lang, version).encode("utf-8")).hexdigest()[:12]


def system_prompt(lang, version=None):
    version = version or PROMPT_VERSION
    if SCHEMA_OF[version] == "4":
        return system_prompt_v4(lang, version)
    inv = []
    for v in VARIETIES[lang]:
        d = json.loads((HERE / "inventories" / f"{v}.json").read_text())
        inv.append(f"{v}: consonants {' '.join(d['consonants'])}; vowels {' '.join(d['vowels'])}; marks {' '.join(d['diacritics'])}. {d['conventions']}")
    tpl = (HERE / "prompts" / "entry_writer" / f"{version}.md").read_text()
    return (tpl.replace("{language_name}", LANG_NAME[lang]).replace("{gloss_languages}", GLOSS[lang]).replace("{forms_rule}", FORMS_RULE[lang])
            .replace("{inventory}", "\n".join(inv)).replace("{schema_example}", SCHEMA_EXAMPLE)
            .replace("{varieties}", ", ".join(VARIETIES[lang])).replace("{un_regions}", ", ".join(f"{lang}-{code} {name}" for code, name in UN_REGIONS.items()))
            .replace("{domains}", ", ".join(DOMAINS)))


REMINDER = ('Every sense you write carries "from": the ids of the evidence senses it covers. '
            "Write IPA in the house symbols only, whatever notation the evidence uses.")


def user_prompt(lang, row, evidence=None, remind=False, covered=()):
    """The request for one headword. evidence is Wiktionary's record rendered by wiktionary.render (the
    evidence prompts v5 and v6); covered lists (lemma, pos) of written lexemes that already have this
    spelling (lemma_gate.covered), so that a schema-4 writer does not write them a second time."""
    text = f"Language: {lang}\nHeadword: {row['headword']}\nZipf frequency: {row['zipf']}"
    if covered and SCHEMA_VERSION == "4":
        text += "\nCovered: " + "; ".join(f"{lemma} ({pos})" for lemma, pos in covered)
    if evidence is None:
        return text
    return f"{text}\n\nEvidence (Wiktionary):\n{evidence}" + (f"\n\n{REMINDER}" if remind else "")




# ---------------------------------------------------------------------------------------------- prompt v4

# The varieties a spelling, form or sense may be tagged with: the backend's LanguageVariety for the language.
REGIONS = {"en": ["en-US", "en-GB", "en-AU", "en-IN"], "de": ["de-DE", "de-AT", "de-CH"], "fr": ["fr-FR", "fr-CA", "fr-BE", "fr-CH"],
           "es": ["es-ES", "es-MX", "es-AR", "es-CO"], "it": ["it-IT"], "pt": ["pt-BR", "pt-PT"], "uk": ["uk-UA"], "ru": ["ru-RU"], "ja": ["ja-JP"]}

ORTHOGRAPHY_V4 = {
    "en": "",
    "de": "German spelling was reformed in 1996: daß, muß and Schiffahrt are pre-reform spellings of dass, muss and Schifffahrt. "
          "Swiss German writes ss for ß (Strasse, de-CH). ",
    "fr": "Since the 1990 rectifications some words have two accepted spellings (événement and évènement, nénuphar and nénufar): "
          "give both, the traditional one first. ",
    "es": "The 2010 orthography dropped some accents (solo, este, guion): sólo as an adverb, guión and fué are pre-reform spellings. ",
    # from v4.4: sólo and éste are still allowed, and fué went in 1959, not 2010
    ("es", "v4.4"): "The 2010 orthography writes guion, truhan and fie without an accent: guión is a pre-reform spelling, and so are fué, "
                    "dió and vió, which lost theirs in 1959. Solo as an adverb and the demonstratives este, ese and aquel are written "
                    "without an accent, but sólo and éste are still allowed where the writer sees an ambiguity: list them as spellings, "
                    "not as pre-reform. ",
    "it": "",
    "pt": "The 1990 Orthographic Agreement removed silent consonants and some accents: acção, óptimo, idéia, vôo and freqüente are "
          "pre-reform spellings. Where Brazil and Portugal still write a word differently (econômico pt-BR and económico pt-PT; "
          "fato pt-BR and facto pt-PT for \"fact\"), give both spellings with their varieties. ",
    "uk": "Ukrainian spelling was revised in 2019 (проєкт, not проект) and in 1990, when ґ came back after Soviet spelling had replaced "
          "it with г: проект and ганок (for ґанок) are pre-reform spellings. ",
    "ru": "Most Russian print writes ё as е. Write the lemma with ё where the word has it (ёлка, ребёнок, ещё) and list the spelling "
          "with е beside it, without varieties; the headword may arrive in either. The spelling of before 1918 (міръ, хлѣбъ) is turned "
          "into today's by a rule and is not listed. ",
    "ja": "Write the lemma as Japanese dictionaries head it: in kanji with its okurigana where the word is normally written so (食べる, "
          "高い), in kana where it normally is (する, これ, テレビ). The headword may arrive in kana or in kanji. List another way of "
          "writing the word as a spelling only when print uses it too, each with its \"script\": Jpan for kanji with or without kana, "
          "Hira, Kana (綺麗 Jpan and きれい Hira; コンピューター and コンピュータ, both Kana). A character of before the 1946 reform "
          "(國 for 国) is a spelling with \"pre_reform\": true. ",
}

STRESS_V4 = {
    "uk": "Ukrainian does not write stress, so each pronunciation also carries a reading: the word with an acute accent after its "
          "stressed vowel (рука́, за́мок, замо́к). A word of one syllable has no mark.\n",
    "ru": "Russian does not write stress, so each pronunciation also carries a reading: the word with an acute accent after its "
          "stressed vowel (рука́, за́мок, замо́к). The letter ё is always stressed and takes no accent (ёлка, ребёнок). A word of one "
          "syllable has no mark.\n",
    "it": "Italian does not write stress inside a word, so each pronunciation also carries a reading: the word with an accent on its "
          "stressed vowel as Italian dictionaries print it, grave or acute on e and o for an open or a close vowel (àncora, ancóra, "
          "pèsca, pésca, telèfono). A word of one syllable has no mark.\n",
}

EXTRA_GRAMMAR_V4 = {"fr": " h_aspire (true for a word whose initial h blocks elision and liaison: le héros, les héros),"}

FORMS_V4 = {
    "en": "Verbs: the past tense and the past participle when irregular. Nouns: the plural when irregular (mouse, mice; sheep, sheep). "
          "Adjectives and adverbs: the comparative and superlative when irregular (good, better, best). A form spelled differently in "
          "different varieties is listed in each spelling with its varieties (travelled en-GB, traveled en-US).",
    "de": "Nouns: the genitive singular and the nominative plural, with gender and plural_class in grammar. Verbs: the third person "
          "singular present when its vowel changes (fährt), the third person singular preterite and the past participle, with "
          "auxiliary in grammar; a separable verb's forms are written as a sentence shows them (fängt an, fing an, angefangen). "
          "Adjectives: the comparative and superlative when they take an umlaut or are irregular (alt, älter, am ältesten).",
    "fr": "Nouns: the plural when irregular (œil, yeux; travail, travaux). Adjectives: the feminine singular and the plurals when "
          "irregular (beau, belle, beaux, and bel before a vowel). Verbs: nothing beyond grammar for a regular verb of the first or "
          "second group; for any other verb the present indicative 1sg, 3sg, 1pl and 3pl, the past participle, the passé simple 3sg, "
          "the future 1sg and the present subjunctive 1sg, with auxiliary in grammar.",
    "es": "Nouns: the plural when irregular (régimen, regímenes; carácter, caracteres). Adjectives: the feminine and plural when "
          "irregular. Verbs: nothing beyond grammar for a regular verb; for an irregular or stem-changing verb the present indicative "
          "1sg, 2sg, 3sg, 1pl and 3pl, the pretérito indefinido 1sg and 3sg, the future 1sg, the present subjunctive 1sg, the past "
          "participle and the gerund.",
    "it": "Nouns: the plural when irregular, including a plural that changes gender (il braccio, le braccia; l'uovo, le uova). "
          "Adjectives: the forms when irregular. Verbs: nothing beyond grammar for a regular verb; for an irregular verb the present "
          "indicative 1sg, 2sg, 3sg, 1pl and 3pl, the passato remoto 1sg and 3sg, the future 1sg, the present subjunctive 1sg and "
          "the past participle, with auxiliary in grammar.",
    "pt": "Nouns: the plural when irregular (pão, pães; cidadão, cidadãos). Adjectives: the feminine and plural when irregular. "
          "Verbs: nothing beyond grammar for a regular verb; for an irregular or stem-changing verb the present indicative 1sg, 2sg, "
          "3sg, 1pl and 3pl, the pretérito perfeito 1sg and 3sg, the present subjunctive 1sg and every past participle (aceitado, aceito).",
    "uk": "Nouns: the genitive singular, the nominative plural and the genitive plural, and any other form whose stress or stem a "
          "learner would not expect from these (руці́, ру́ку). Verbs: the present or future 1sg, 2sg and 3pl, the past masculine and "
          "feminine, and the imperative 2sg. Adjectives: the comparative when irregular (добрий, кращий).",
    "ru": "Nouns: the genitive singular, the nominative plural and the genitive plural, and any other form whose stress or stem a "
          "learner would not expect from these (ру́ку, во́ду). A form that only some words have is listed with a note saying when it is "
          "used: the second locative with Case=Loc (лесу́: after в and на, в лесу́ but о ле́се), the partitive with Case=Par beside the "
          "ordinary genitive (ча́ю), a form of its own after два, три, четыре with Number=Count (часа́: два часа́ but о́коло ча́са), a "
          "genitive plural used after numbers beside the ordinary one (челове́к: пять челове́к; лет: пять лет), the short form of address "
          "with Case=Voc (пап). Verbs: the present of an imperfective or the future of a perfective (Tense=Fut) in the 1sg, 2sg and 3pl, "
          "the past masculine and feminine, the past neuter or plural when its stress could not be guessed, and the imperative 2sg. Adjectives: the short forms when the stress moves in them (мо́лод, "
          "молода́, мо́лодо) and the comparative when irregular (хороший, лучше).",
    "ja": "Verbs: nothing for a regular godan or ichidan verb, whose class gives every form; for an irregular verb or form the polite "
          "non-past (Polite=Form), the て form (VerbForm=Conv) and the plain negative (Polarity=Neg): する, 来る, 行く (行って), ある "
          "(ない), いらっしゃる (いらっしゃいます). Adjectives: nothing, except where the class does not give the form (いい: よく, "
          "よかった).",
}

FORM_SOUND_V4 = {
    "en": "A listed form carries its own ipa for the same varieties.",
    "de": "A listed form carries its own ipa.",
    "fr": "A listed form carries its own ipa.",
    "pt": "A listed form carries its own ipa for the same varieties.",
    "es": "Listed forms carry no ipa: it follows from the spelling.",
    "it": "A listed form of two or more syllables carries its own reading (pàrlano).",
    "uk": "Each listed form of two or more syllables carries its own reading (руки́, ру́ку).",
    "ru": "Each listed form of two or more syllables carries its own reading (руки́, ру́ку), and no ipa.",
    "ja": "Each listed form carries its own reading in hiragana with \"notation\": \"kana\", and no ipa.",
}

# v4.1: a variant chosen by the neighbouring sounds is a spelling of the same lexeme, so that "у" is not bought after "в".
POSITIONAL_V4 = {
    "en": "A variant chosen by the next sound is a spelling of the same lexeme too (a and an). ",
    "fr": "A variant chosen by the next sound is a spelling of the same lexeme too (je and j', le and l', de and d', que and qu'). ",
    "es": "A variant chosen by the next sound is a spelling of the same lexeme too (y and e, o and u). ",
    "it": "A variant chosen by the next sound is a spelling of the same lexeme too (e and ed, a and ad, lo and l'). ",
    "uk": "A variant chosen by the neighbouring sounds is a spelling of the same lexeme too (у and в, і and й, з, із and зі, же and ж, "
          "учитель and вчитель): list them without varieties. ",
    "ru": "A variant chosen by the neighbouring sounds is a spelling of the same lexeme too (в and во, с and со, к and ко, о, об and "
          "обо, же and ж): list them without varieties. ",
}

LANGUAGE_NOTES_V4 = {
    "de": "\nThe headword may come in lower case (fallschirm): write each lemma as German writes it (Fallschirm).\n",
    # from v4.4: with capitals not separating entries, "sterben" would otherwise bring "das Sterben" along
    ("de", "v4.4"): "\nThe headword may come in lower case (fallschirm): write each lemma as German writes it (Fallschirm). An infinitive "
                    "used as a noun (das Sterben, das Laufen) is not a lexeme unless it has a meaning the verb lacks (das Essen, food; "
                    "das Leben, life; das Schreiben, a letter).\n",
    # from v4.5: three in five pronominal senses were written with the auxiliary of the plain verb
    ("fr", "v4.5"): "\nA pronominal sense (s'expliquer, se permettre) is conjugated with être: give \"auxiliary\": \"be\" in that "
                    "sense's grammar, whatever the verb takes elsewhere.\n",
    ("it", "v4.5"): "\nA pronominal sense (lavarsi, accorgersi) is conjugated with essere: give \"auxiliary\": \"be\" in that "
                    "sense's grammar, whatever the verb takes elsewhere.\n",
    "uk": "\nWrite definitions and examples in standard Ukrainian, free of Russianisms and calques. A word or sense that Ukrainian "
          "normative dictionaries reject as a Russianism or a calque is proscribed, with the standard word in standard_alternative "
          "(являтися in the sense \"to be\": бути). A verb lists its aspect partner as a relation.\n",
    "ru": "\nA verb lists its aspect partner as a relation (делать and сделать). A verb in -ся is a lemma of its own, written under "
          "its own spelling, not a sense of the verb without it (учиться is not under учить). A word used only as a predicate "
          "(нельзя, надо, рад) has position Pred. A noun gives its declension and a verb its conjugation as Russian school grammar "
          "numbers them (declension 1 for папа and вода, 2 for стол and окно, 3 for ночь; conjugation 1 or 2). A frame shows what the word "
          "governs with the question words (искать кого/что, платить кому за что, чемпионат по чему), not one collocation filled in. "
          "Russian has no articles, so a definition may have as few as four words.\n",
    "ja": "\nDefinitions and examples are in standard Japanese: the definition in the plain style of a dictionary, the example a "
          "natural sentence in the plain or the polite style, both with kanji as a newspaper uses them. A definition is one thing "
          "said once: one predicate for a verb or an adjective, one noun phrase for a noun, a short statement of its use for a "
          "particle, never two sentences. Characters are counted with their punctuation. A verb gives its class in "
          "\"conjugation\": godan, ichidan, or irregular (する, 来る), and its valency; a transitive verb and its intransitive partner "
          "(開ける, 開く) name each other as a relation of type confusable, with why. A use after the て form with a meaning of its own "
          "(〜ていく, 〜てくる, 〜ていらっしゃる) is a sense, with its frame. An adjective gives \"conjugation\": i or na (高い: i; 静か: na); "
          "its に form (静かに) is not an adverb of its own, but a use in which it takes particles as a noun does (好きを仕事にする) is a "
          "noun lexeme. A word that only stands before a noun and never changes (大きな, 来る read きたる) is ADJ with position Attr and "
          "invariable true. A prefix or suffix that is not a word on its own (本〜 for \"this\", 〜的) is not a lexeme. Japanese nouns "
          "take no countability. A noun gives its usual counter in \"classifier\" (鉛筆: 本; 犬: 匹; 本: 冊), and a counter for another "
          "unit in the note; a counter (本, 匹, 人) is a lexeme itself, whose forms give the readings that change with the number (一本 "
          "いっぽん, 三本 さんぼん). A noun that makes a verb with する (勉強) has \"suru\": true, in the grammar of the word or of the "
          "senses it holds for, and shows the verb in a frame (勉強する). A frame is a pattern with its particles and an open place "
          "(〜が好き, 〜に行く, 〜を食べる), not a usual pairing of words (鉛筆で書く). "
          "A particle (は, が, を, に, ね) is an entry: its part of speech is ADP for a case particle and PART otherwise, and its "
          "definition says what it is used for. An honorific or a humble word (いらっしゃる, 参る) has the politeness label honorific "
          "or humble, a word of the polite style (です, ございます) polite, a word used only with friends and family (俺) familiar. A "
          "word or sense of one region of Japan (おおきに) names the region in the sense's grammar note, Japan being one variety.\n",
}

SCHEMA_EXAMPLE_V4 = json.dumps({
    "language": "en", "headword": "lead",
    "lexemes": [
        {"lemma": "lead", "pos": "VERB", "kind": "word", "grammar": {"irregular": True, "valency": "Both"},
         "pronunciations": [{"variety": "en-US", "ipa": "/ˈliːd/"}, {"variety": "en-GB", "ipa": "/ˈliːd/"}],
         "forms": [{"form": "led", "features": {"Tense": "Past", "VerbForm": "Fin,Part"}, "ipa": {"en-US": "/ˈlɛd/", "en-GB": "/ˈlɛd/"}}],
         "senses": [
             {"definition": "to show someone the way by going in front of them", "example": "Follow me and I'll lead you to the exit.",
              "cefr": "A2", "frames": ["lead sb to/into sth"], "grammar": {"valency": "Tr"}},
             {"definition": "to be in charge of a group of people or an organization",
              "example": "She has led the research team for ten years.", "cefr": "B1", "grammar": {"valency": "Tr"}},
             {"definition": "to be winning in a race or a competition", "example": "We were leading by two goals at half-time.",
              "cefr": "B2", "frames": ["lead by sth"], "grammar": {"valency": "Intr"}, "antonyms": [{"lemma": "trail", "pos": "VERB"}]}]},
        {"lemma": "lead", "pos": "NOUN", "kind": "word", "grammar": {"countability": "Count"},
         "pronunciations": [{"variety": "en-US", "ipa": "/ˈliːd/"}, {"variety": "en-GB", "ipa": "/ˈliːd/"}],
         "senses": [
             {"definition": "the first position in a race or a competition", "example": "Our runner took the lead in the last lap.",
              "cefr": "B2", "frames": ["in the lead", "take the lead"]},
             {"definition": "a long strap or chain fixed to a dog's collar", "example": "Please keep your dog on a lead near the road.",
              "cefr": "B2", "regions": ["en-GB", "en-AU"]}]},
        {"lemma": "lead", "pos": "NOUN", "kind": "word", "grammar": {"countability": "Mass"},
         "pronunciations": [{"variety": "en-US", "ipa": "/ˈlɛd/"}, {"variety": "en-GB", "ipa": "/ˈlɛd/"}],
         "senses": [
             {"definition": "a soft heavy grey metal, once used for water pipes",
              "example": "Old houses sometimes still have pipes made of lead.", "cefr": "B2"}],
         "relations": [{"type": "confusable", "lemma": "lead", "pos": "VERB", "why": "sounds like led, the past of the verb lead"}]},
    ]}, ensure_ascii=False, indent=1)


# v4.2 says where a sense is used as October's prompts do; its example entry shows that shape. The example of
# v4 and v4.1 is kept as it is, because their prompt hashes are in the metas of the entries they wrote.
# The UN M.49 areas worth offering a writer of each language; a language spoken in one part of the world has none.
UN_AREAS_V4 = {"en": ("021", "029", "002", "142", "009"), "es": ("419", "029", "005", "013"), "fr": ("002", "021"), "pt": ("002",)}
REGION_EXAMPLES_V4 = {"en": "en-GB, en-US, en-AU, en-IE", "de": "de-DE, de-AT, de-CH", "fr": "fr-FR, fr-CA, fr-BE, fr-CH",
                      "es": "es-ES, es-MX, es-AR, es-CU", "it": "it-IT, it-CH", "pt": "pt-BR, pt-PT, pt-AO", "uk": "uk-UA",
                      "ru": "ru-RU, ru-BY, ru-KZ", "ja": "ja-JP"}


def schema_example_v4(version):
    if version in ("v4", "v4.1"):
        return SCHEMA_EXAMPLE_V4
    entry = json.loads(SCHEMA_EXAMPLE_V4)
    if since_v4(version, "v4.7"):  # "to be winning" is nearly always intransitive, but one leads a race too
        entry["lexemes"][0]["senses"][2]["grammar"] = {"valency": "Intr", "mainly": ["valency"]}
    for lexeme in entry["lexemes"]:
        for sense in lexeme["senses"]:
            if "regions" in sense:
                sense["regions"] = [{"variety": variety, "extent": "only"} for variety in sense["regions"]]
    return json.dumps(entry, ensure_ascii=False, indent=1)


# The prompt version from which a language's writers are shown its reviewed example entries (gold/<lang>.json).
# A version's prompt for a language never changes once entries are written with it, so a language whose
# examples arrive later gets them in the next version, and a gold file in use is not edited: a better set
# of examples is a new version too.
GOLD_SINCE = {"en": "v4.3", "de": "v4.4", "es": "v4.4", "fr": "v4.5", "it": "v4.6", "pt": "v4.6",
              "ru": "v4.7", "ja": "v4.7"}  # languages on trial: their prompts are not pinned until they are target languages


def since_v4(version, first):
    """Whether a schema-4 prompt version is the one named or a later one."""
    order = [v for v, schema in SCHEMA_OF.items() if schema == "4"]
    return order.index(version) >= order.index(first)


# How a variety's sounds are written, where a later version says it differently from inventories/<variety>.json.
# The file keeps every symbol entries were ever written with, so that the audit still reads the old ones;
# ingest_v4.settle_sounds turns what is written into the present form by rule.
SOUNDS_V4 = {
    # most of Spain says ll and y alike; the separate sound is the old norm a few regions keep
    ("es-ES", "v4.6"): {"without": ["ʎ"], "conventions":
        "Castilian Spanish with distinción (c/z before e, i and z are θ) and yeísmo: ll and y are both ʝ, as nearly all of Spain says "
        "them. Voiced stops are written b d ɡ in every position; the approximant allophones are not marked. Stress falls where the "
        "written accent rules put it."},
    # a long mark on the consonant left no place for the syllable dot, and three ways of writing a double were in use
    ("it-IT", "v4.6"): {"without": ["ː"], "conventions":
        "Standard Italian. A double consonant is written twice with the syllable boundary between (fatto is /ˈfat.to/, bello is "
        "/ˈbɛl.lo/); a double affricate doubles its stop (oggi is /ˈɔd.dʒi/, pizza is /ˈpit.tsa/), and ʃ ʎ ɲ between vowels are "
        "double (pesce is /ˈpeʃ.ʃe/, figlio is /ˈfiʎ.ʎo/). Open and close e and o are distinguished (ɛ/e, ɔ/o) in stressed "
        "syllables and are e, o when unstressed. Intervocalic s is z where the standard has it."},
    # the first measurement: seven of a hundred Portuguese entries had the wrong vowel before the stress in pt-PT,
    # where the old text gave reduction as a rule without exception
    ("pt-PT", "v4.7"): {"conventions":
        "European Portuguese, Lisbon standard. Before the stress, written e is usually ɨ, a usually ɐ and o usually u, but not "
        "always: many words keep an open or a close vowel there (normal is /nɔɾ.ˈmal/, esquecer is /ɨʃ.kɛ.ˈseɾ/, padaria is "
        "/pa.dɐ.ˈɾi.ɐ/), and written i stays i. Give each word the vowels European dictionaries give it, never the rule alone. "
        "Written r at word start and rr are ʁ; single r between vowels is ɾ. Syllable-final s is ʃ. Nasal vowels carry the tilde."},
    # the weak vowels the writers used rightly although the lists lacked them (see "also" in the inventory files)
    ("en-US", "v4.7"): {"with": {"vowels": ["i", "u"]}, "add": " The weak vowel at the end of happy and before another vowel is i (/ˈhæp.i/, /ˈɹeɪ.di.oʊ/), and that of situation is u."},
    ("en-GB", "v4.7"): {"with": {"vowels": ["i", "u"]}, "add": " The weak vowel at the end of happy and before another vowel is i (/ˈhæp.i/, /ˈɹeɪ.di.əʊ/), and that of situation is u."},
    ("de-DE", "v4.7"): {"with": {"vowels": ["i", "y", "e", "ø", "o", "u"]}, "add": " A short close vowel in an unstressed syllable, mostly of a loanword, is written without the length mark (Politik is /po.li.ˈtiːk/, marginal is /maʁ.ɡi.ˈnaːl/)."},
}


def sounds_v4(variety, version):
    """The line of the prompt that lists a variety's symbols and conventions."""
    d = json.loads((HERE / "inventories" / f"{variety}.json").read_text())
    for (v, first), change in SOUNDS_V4.items():
        if v == variety and since_v4(version, first):
            d = {**d, **{k: [x for x in d[k] if x not in change.get("without", ())] + change.get("with", {}).get(k, [])
                         for k in ("consonants", "vowels", "diacritics")},
                 "conventions": change.get("conventions", d["conventions"]) + change.get("add", "")}
    return f"{variety}: consonants {' '.join(d['consonants'])}; vowels {' '.join(d['vowels'])}; marks {' '.join(d['diacritics'])}. {d['conventions']}"


# A language written without an alphabet is not transcribed in IPA: its pronunciation section is this text, whole.
PRONUNCIATION_V4 = {
    "ja": "Japanese is not given in IPA. Each pronunciation has \"variety\": \"ja-JP\", a \"reading\" in hiragana and \"notation\": "
          "\"kana\" (食べる: たべる; 東京: とうきょう), also for a word written in kana, whose reading repeats it in hiragana (テレビ: "
          "てれび). When a word is read in more than one way with the same meaning, give the most common first (明日: あした, あす); a "
          "reading after the first may carry a \"note\": less_common, older, newer, careful (the formal reading), casual, regional or "
          "disputed. Give a note only when you are sure of it. A reading that changes the meaning is another lexeme, not a second "
          "reading (辛い: からい, hot to the taste, and つらい, hard to bear). The reading is how the word is said, so a particle written "
          "は or を has the reading わ or お. When you are sure of the pitch accent of Tokyo speech, give it as \"accent\", the number "
          "dictionaries print: the mora after which the pitch drops, 0 when it does not drop (箸 はし 1, 橋 はし 2, 端 はし 0), counting "
          "ん, っ and the second half of a long vowel as a mora each. Leave it out when you are not sure.",
}
# ... and its limits count characters, there being no spaces to count words by: what replaces each limit of the prompt.
LIMITS_V4 = {"ja": {"6 to 15 words": "10 to 35 characters", "of 6 to 14 words": "of 12 to 40 characters"}}


def fill_v4(table, lang, version):
    """A language's text from one of the tables above. A text that changed in some version stands beside the old one
    under (language, version), so that an older prompt still renders to the letter; the latest that applies is used."""
    dated = [text for key, text in table.items() if isinstance(key, tuple) and key[0] == lang and since_v4(version, key[1])]
    return dated[-1] if dated else table.get(lang, "")


def examples_v4(lang, version):
    """The language's reviewed example entries: one entry to a line, so that five cost little to read.
    A language without them yet shows none and keeps the one English entry under "Output"."""
    if lang not in GOLD_SINCE or not since_v4(version, GOLD_SINCE[lang]):
        return ""
    entries = json.loads((HERE / "gold" / f"{lang}.json").read_text(encoding="utf-8"))
    return ("# Examples\n\nEntries of this dictionary that an editor has checked. Write every entry the way these are written: the same "
            "care in which senses are numbered, the same plain definitions, the same use of frames, labels, regions and grammar.\n\n"
            + "\n".join(json.dumps(entry, ensure_ascii=False) for entry in entries) + "\n\n")


def system_prompt_v4(lang, version=None):
    """The system prompt of a schema-4 writer: the production prompt unless another schema-4 version is named."""
    version = version or (PROMPT_VERSION if SCHEMA_VERSION == "4" else "v4.7")
    schema_path = HERE / "entry.schema.v4.json"
    inv = [] if lang in PRONUNCIATION_V4 else [sounds_v4(v, version) for v in VARIETIES[lang]]
    domains = json.loads(schema_path.read_text())["$defs"]["labels"]["properties"]["domain"]["enum"]
    tpl = (HERE / "prompts" / "entry_writer" / f"{version}.md").read_text()
    fills = {"{language_name}": LANG_NAME[lang], "{orthography_rule}": fill_v4(ORTHOGRAPHY_V4, lang, version), "{regions}": ", ".join(REGIONS[lang]), "{ipa_varieties}": ", ".join(VARIETIES[lang]),
             "{inventory}": "\n".join(inv), "{stress_rule}": STRESS_V4.get(lang, ""), "{extra_grammar}": EXTRA_GRAMMAR_V4.get(lang, ""),
             "{forms_rule}": FORMS_V4[lang], "{form_sound_rule}": FORM_SOUND_V4[lang], "{domains}": ", ".join(domains),
             "{language_notes}": fill_v4(LANGUAGE_NOTES_V4, lang, version), "{schema_example}": schema_example_v4(version),
             "{positional_rule}": POSITIONAL_V4.get(lang, ""), "{region_examples}": REGION_EXAMPLES_V4[lang],
             "{examples}": examples_v4(lang, version),
             "{un_regions}": "".join(" or with a UN area (" + ", ".join(f"{lang}-{code} {UN_REGIONS[code]}" for code in UN_AREAS_V4[lang]) + ")"
                                     for _ in [0] if lang in UN_AREAS_V4)}
    for k, v in fills.items():
        tpl = tpl.replace(k, v)
    if lang in PRONUNCIATION_V4:
        before, _, rest = tpl.partition("# Pronunciation\n\n")
        tpl = f"{before}# Pronunciation\n\n{PRONUNCIATION_V4[lang]}\n\n# Grammar and forms{rest.partition('# Grammar and forms')[2]}"
    for old, new in LIMITS_V4.get(lang, {}).items():
        assert tpl.count(old) == 1, f"the prompt no longer states the limit {old!r} once"
        tpl = tpl.replace(old, new)
    return tpl
