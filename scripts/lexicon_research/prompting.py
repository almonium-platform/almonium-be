"""The writer prompt, shared by every runner: a fixed system block per language (rules, inventory,
schema example) so provider caches can serve it, and a one-line user message with the headword."""
import json
from pathlib import Path

HERE = Path("scripts/lexicon_research")
PROMPT_VERSION = "v1"
LANG_NAME = {"en": "English", "de": "German", "uk": "Ukrainian"}
VARIETIES = {"en": ["en-US", "en-GB"], "de": ["de-DE"], "uk": ["uk-UA"]}
GLOSS = {"en": "uk, de, pl", "de": "en, uk, pl", "uk": "en, de, pl"}

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
            "pos_detail": "transitive", "register": "neutral", "cefr": "A2",
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
    return (tpl.replace("{language_name}", LANG_NAME[lang]).replace("{gloss_languages}", GLOSS[lang])
            .replace("{inventory}", "\n".join(inv)).replace("{schema_example}", SCHEMA_EXAMPLE))


def user_prompt(lang, row):
    return f"Language: {lang}\nHeadword: {row['headword']}\nZipf frequency: {row['zipf']}"


