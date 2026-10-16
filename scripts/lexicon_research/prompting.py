"""The writer prompt, shared by every runner: a fixed system block per language (rules, inventory,
schema example) so provider caches can serve it, and a one-line user message with the headword."""
import json
from pathlib import Path

HERE = Path("scripts/lexicon_research")
PROMPT_VERSION = "v3"
LANG_NAME = {"en": "English", "de": "German", "uk": "Ukrainian", "fr": "French", "es": "Spanish", "it": "Italian", "pt": "Portuguese"}
VARIETIES = {"en": ["en-US", "en-GB"], "de": ["de-DE"], "uk": ["uk-UA"], "fr": ["fr-FR"], "es": ["es-ES", "es-MX"],
             "it": ["it-IT"], "pt": ["pt-BR", "pt-PT"]}
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


def system_prompt(lang):
    inv = []
    for v in VARIETIES[lang]:
        d = json.loads((HERE / "inventories" / f"{v}.json").read_text())
        inv.append(f"{v}: consonants {' '.join(d['consonants'])}; vowels {' '.join(d['vowels'])}; marks {' '.join(d['diacritics'])}. {d['conventions']}")
    tpl = (HERE / "prompts" / "entry_writer" / f"{PROMPT_VERSION}.md").read_text()
    return (tpl.replace("{language_name}", LANG_NAME[lang]).replace("{gloss_languages}", GLOSS[lang]).replace("{forms_rule}", FORMS_RULE[lang])
            .replace("{inventory}", "\n".join(inv)).replace("{schema_example}", SCHEMA_EXAMPLE))


def user_prompt(lang, row):
    return f"Language: {lang}\nHeadword: {row['headword']}\nZipf frequency: {row['zipf']}"


