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
#      extent) and pronunciation notes, the parts of a compound, and the politeness label. v4.3 shows a
#      language its own reviewed example entries (gold/<lang>.json) and states five rules a reviewer of
#      another model family found the entries bending
# Entries written between 11 and 17 November 2026 call shape 4 "schema 3" in their meta, because the two
# lines were numbered apart; schema_of_meta() reads them right. The file prompts/entry_writer/v4.md is
# November's; October's prompt of that name is v4oct.md.
SCHEMA_OF = {"v1": "2", "v2": "2", "v3": "2", "v4oct": "3", "v5": "3", "v6": "3", "v4": "4", "v4.1": "4", "v4.2": "4", "v4.3": "4"}
# LEXICON_PROMPT_VERSION=v3 writes the old shape (with glosses and cues) into the old directories
PROMPT_VERSION = os.environ.get("LEXICON_PROMPT_VERSION", "v4.3")
SCHEMA_VERSION = SCHEMA_OF[PROMPT_VERSION]
SCHEMA_PATH = HERE / ("entry.schema.v4.json" if SCHEMA_VERSION == "4" else "entry.schema.json")
WRITER_SUFFIX = "-v4" if SCHEMA_VERSION == "4" else ""
# The languages a learner studies, the ones entries are written for. Ukrainian is a bridge language
# only: glosses are written in it, entries are not (owner, 2026-10 and again 16 November 2026).
TARGET_LANGUAGES = ("en", "de", "fr", "es", "it", "pt")


def schema_of_meta(meta):
    """The shape an entry's meta names, with November's first week read as schema 4."""
    version = str(meta.get("schema_version") or "")
    return "4" if version == "3" and str(meta.get("prompt_version") or "").startswith("v4") else version


LANG_NAME = {"en": "English", "de": "German", "uk": "Ukrainian", "fr": "French", "es": "Spanish", "it": "Italian", "pt": "Portuguese"}
VARIETIES = {"en": ["en-US", "en-GB"], "de": ["de-DE"], "uk": ["uk-UA"], "fr": ["fr-FR"], "es": ["es-ES", "es-MX"],
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
           "es": ["es-ES", "es-MX", "es-AR", "es-CO"], "it": ["it-IT"], "pt": ["pt-BR", "pt-PT"], "uk": ["uk-UA"]}

ORTHOGRAPHY_V4 = {
    "en": "",
    "de": "German spelling was reformed in 1996: daß, muß and Schiffahrt are pre-reform spellings of dass, muss and Schifffahrt. "
          "Swiss German writes ss for ß (Strasse, de-CH). ",
    "fr": "Since the 1990 rectifications some words have two accepted spellings (événement and évènement, nénuphar and nénufar): "
          "give both, the traditional one first. ",
    "es": "The 2010 orthography dropped some accents (solo, este, guion): sólo as an adverb, guión and fué are pre-reform spellings. ",
    "it": "",
    "pt": "The 1990 Orthographic Agreement removed silent consonants and some accents: acção, óptimo, idéia, vôo and freqüente are "
          "pre-reform spellings. Where Brazil and Portugal still write a word differently (econômico pt-BR and económico pt-PT; "
          "fato pt-BR and facto pt-PT for \"fact\"), give both spellings with their varieties. ",
    "uk": "Ukrainian spelling was revised in 2019 (проєкт, not проект) and in 1990, when ґ came back after Soviet spelling had replaced "
          "it with г: проект and ганок (for ґанок) are pre-reform spellings. ",
}

STRESS_V4 = {
    "uk": "Ukrainian does not write stress, so each pronunciation also carries a reading: the word with an acute accent after its "
          "stressed vowel (рука́, за́мок, замо́к). A word of one syllable has no mark.\n",
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
}

FORM_SOUND_V4 = {
    "en": "A listed form carries its own ipa for the same varieties.",
    "de": "A listed form carries its own ipa.",
    "fr": "A listed form carries its own ipa.",
    "pt": "A listed form carries its own ipa for the same varieties.",
    "es": "Listed forms carry no ipa: it follows from the spelling.",
    "it": "A listed form of two or more syllables carries its own reading (pàrlano).",
    "uk": "Each listed form of two or more syllables carries its own reading (руки́, ру́ку).",
}

# v4.1: a variant chosen by the neighbouring sounds is a spelling of the same lexeme, so that "у" is not bought after "в".
POSITIONAL_V4 = {
    "en": "A variant chosen by the next sound is a spelling of the same lexeme too (a and an). ",
    "fr": "A variant chosen by the next sound is a spelling of the same lexeme too (je and j', le and l', de and d', que and qu'). ",
    "es": "A variant chosen by the next sound is a spelling of the same lexeme too (y and e, o and u). ",
    "it": "A variant chosen by the next sound is a spelling of the same lexeme too (e and ed, a and ad, lo and l'). ",
    "uk": "A variant chosen by the neighbouring sounds is a spelling of the same lexeme too (у and в, і and й, з, із and зі, же and ж, "
          "учитель and вчитель): list them without varieties. ",
}

LANGUAGE_NOTES_V4 = {
    "de": "\nThe headword may come in lower case (fallschirm): write each lemma as German writes it (Fallschirm).\n",
    "uk": "\nWrite definitions and examples in standard Ukrainian, free of Russianisms and calques. A word or sense that Ukrainian "
          "normative dictionaries reject as a Russianism or a calque is proscribed, with the standard word in standard_alternative "
          "(являтися in the sense \"to be\": бути). A verb lists its aspect partner as a relation.\n",
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
                      "es": "es-ES, es-MX, es-AR, es-CU", "it": "it-IT, it-CH", "pt": "pt-BR, pt-PT, pt-AO", "uk": "uk-UA"}


def schema_example_v4(version):
    if version in ("v4", "v4.1"):
        return SCHEMA_EXAMPLE_V4
    entry = json.loads(SCHEMA_EXAMPLE_V4)
    for lexeme in entry["lexemes"]:
        for sense in lexeme["senses"]:
            if "regions" in sense:
                sense["regions"] = [{"variety": variety, "extent": "only"} for variety in sense["regions"]]
    return json.dumps(entry, ensure_ascii=False, indent=1)


# The prompt version from which a language's writers are shown its reviewed example entries (gold/<lang>.json).
# A version's prompt for a language never changes once entries are written with it, so a language whose
# examples arrive later gets them in the next version, and a gold file in use is not edited: a better set
# of examples is a new version too.
GOLD_SINCE = {"en": "v4.3"}


def examples_v4(lang, version):
    """The language's reviewed example entries: one entry to a line, so that five cost little to read.
    A language without them yet shows none and keeps the one English entry under "Output"."""
    order = [v for v, schema in SCHEMA_OF.items() if schema == "4"]
    if lang not in GOLD_SINCE or order.index(version) < order.index(GOLD_SINCE[lang]):
        return ""
    entries = json.loads((HERE / "gold" / f"{lang}.json").read_text(encoding="utf-8"))
    return ("# Examples\n\nEntries of this dictionary that an editor has checked. Write every entry the way these are written: the same "
            "care in which senses are numbered, the same plain definitions, the same use of frames, labels, regions and grammar.\n\n"
            + "\n".join(json.dumps(entry, ensure_ascii=False) for entry in entries) + "\n\n")


def system_prompt_v4(lang, version=None):
    """The system prompt of a schema-4 writer: the production prompt unless another schema-4 version is named."""
    version = version or (PROMPT_VERSION if SCHEMA_VERSION == "4" else "v4.3")
    schema_path = HERE / "entry.schema.v4.json"
    inv = []
    for v in VARIETIES[lang]:
        d = json.loads((HERE / "inventories" / f"{v}.json").read_text())
        inv.append(f"{v}: consonants {' '.join(d['consonants'])}; vowels {' '.join(d['vowels'])}; marks {' '.join(d['diacritics'])}. {d['conventions']}")
    domains = json.loads(schema_path.read_text())["$defs"]["labels"]["properties"]["domain"]["enum"]
    tpl = (HERE / "prompts" / "entry_writer" / f"{version}.md").read_text()
    fills = {"{language_name}": LANG_NAME[lang], "{orthography_rule}": ORTHOGRAPHY_V4[lang], "{regions}": ", ".join(REGIONS[lang]), "{ipa_varieties}": ", ".join(VARIETIES[lang]),
             "{inventory}": "\n".join(inv), "{stress_rule}": STRESS_V4.get(lang, ""), "{extra_grammar}": EXTRA_GRAMMAR_V4.get(lang, ""),
             "{forms_rule}": FORMS_V4[lang], "{form_sound_rule}": FORM_SOUND_V4[lang], "{domains}": ", ".join(domains),
             "{language_notes}": LANGUAGE_NOTES_V4.get(lang, ""), "{schema_example}": schema_example_v4(version),
             "{positional_rule}": POSITIONAL_V4.get(lang, ""), "{region_examples}": REGION_EXAMPLES_V4[lang],
             "{examples}": examples_v4(lang, version),
             "{un_regions}": "".join(" or with a UN area (" + ", ".join(f"{lang}-{code} {UN_REGIONS[code]}" for code in UN_AREAS_V4[lang]) + ")"
                                     for _ in [0] if lang in UN_AREAS_V4)}
    for k, v in fills.items():
        tpl = tpl.replace(k, v)
    return tpl
