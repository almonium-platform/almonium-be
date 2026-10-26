"""Repair the structural slips a writer makes that have one obvious meaning, before any check or review.

A model writing a large JSON entry slips in ways that are not linguistic: a number where the schema wants a string
("conjugation": 1), a value in the word's own language where the schema has an English one ("auxiliary": "sein"),
a key the schema does not have, a gloss object shaped {"word", "exact"}, or Wiktionary's notation copied into a
transcription (the non-syllabic mark of aɪ̯, the glottal stop, which the house inventories do not write). Each
repair here is safe because it has one reading; anything without one is left for the checks to report. repair()
returns the repaired entry and a list of what it changed, so the changes are counted, not hidden.
"""

import copy
import json
import re
import unicodedata
from pathlib import Path

HERE = Path(__file__).resolve().parent
SCHEMA = json.loads((HERE / "entry.schema.json").read_text())
LEXEME_GRAMMAR = set(SCHEMA["$defs"]["lexeme"]["properties"]["grammar"]["properties"])
SENSE_GRAMMAR = set(SCHEMA["$defs"]["sense"]["properties"]["grammar"]["properties"])
SENSE_KEYS = set(SCHEMA["$defs"]["sense"]["properties"])
FORM_KEYS = set(SCHEMA["$defs"]["form"]["properties"])
GENDERLESS = {"en"}
DOMAINS = set(SCHEMA["$defs"]["sense"]["properties"]["domain"]["enum"])
DOMAIN_SAME = {
    "marketing": "business",
    "medical": "medicine",
    "computers": "computing",
    "sports": "sport",
    "nautical": "nautical",
    "commerce": "business",
    "ecology": "biology",
    "zoology": "zoology",
}
CUE_KEYS = set(SCHEMA["$defs"]["sense"]["properties"]["cues"]["items"]["properties"])
AUXILIARIES = {
    "sein": "be",
    "haben": "have",
    "essere": "be",
    "avere": "have",
    "être": "be",
    "avoir": "have",
    "ser": "be",
    "estar": "be",
    "haber": "have",
    "ter": "have",
}
# Notation Wiktionary writes that the house inventories do not: it says nothing the phonemes do not.
IPA_DROP = {"\u032f", "\u0294"}
# One sound the house writes one way in a variety and models often write another: (pattern, house), in order.
HOUSE = {
    "en-GB": [("r", "\u0279"), ("\u025b", "e")],
    "en-US": [("r", "\u0279"), ("e(?!\u026a)", "\u025b")],
    "uk-UA": [("o", "\u0254")],
    "*": [("([nlm])\u0329", "\u0259\\1")],
}


def repair(entry):
    fixed = copy.deepcopy(entry)
    changes = []
    for li, lexeme in enumerate(fixed.get("lexemes") or []):
        base = f"/lexemes/{li}"
        grammar(lexeme.get("grammar"), LEXEME_GRAMMAR, f"{base}/grammar", changes, keep_note=True)
        if fixed.get("language") in GENDERLESS and (lexeme.get("grammar") or {}).pop("gender", None):
            changes.append(f"{base}/grammar: dropped gender, which {fixed['language']} does not have")
        plural_class = (lexeme.get("grammar") or {}).get("plural_class")
        if isinstance(plural_class, str) and plural_class and not plural_class.startswith("-"):
            lexeme["grammar"]["plural_class"] = "-" + plural_class
            changes.append(f"{base}/grammar/plural_class: wrote it as an ending")
        for pi, spoken in enumerate(lexeme.get("pronunciations") or []):
            spoken["ipa"] = transcription(
                spoken.get("ipa", ""), spoken.get("variety"), f"{base}/pronunciations/{pi}", changes
            )
        for fi, form in enumerate(lexeme.get("forms") or []):
            for key in [key for key in form if key not in FORM_KEYS]:
                changes.append(f"{base}/forms/{fi}: dropped {key}")
                del form[key]
            for variety, ipa in (form.get("ipa") or {}).items():
                form["ipa"][variety] = transcription(ipa, variety, f"{base}/forms/{fi}/ipa/{variety}", changes)
        for si, sense in enumerate(lexeme.get("senses") or []):
            path = f"{base}/senses/{si}"
            for key in [key for key in sense if key not in SENSE_KEYS]:
                changes.append(f"{path}: dropped {key}")
                del sense[key]
            grammar(sense.get("grammar"), SENSE_GRAMMAR, f"{path}/grammar", changes, keep_note=False)
            domain = sense.get("domain")
            if domain is not None and domain not in DOMAINS:
                if DOMAIN_SAME.get(domain) in DOMAINS:
                    sense["domain"] = DOMAIN_SAME[domain]
                else:
                    del sense["domain"]
                changes.append(f"{path}/domain: {domain} is not a domain")
            for ci, cue in enumerate(sense.get("cues") or []):
                for key in [key for key in cue if key not in CUE_KEYS]:
                    del cue[key]
                    changes.append(f"{path}/cues/{ci}: dropped {key}")
            for language, gloss in list((sense.get("glosses") or {}).items()):
                if not isinstance(gloss, (str, dict)) or len(language) > 3:
                    del sense["glosses"][language]
                    changes.append(f"{path}/glosses: dropped {language}, which is not a language")
                    continue
                if isinstance(gloss, dict) and "text" not in gloss and isinstance(gloss.get("word"), str):
                    sense["glosses"][language] = {"text": gloss["word"], "exact": bool(gloss.get("exact", True))}
                    changes.append(f"{path}/glosses/{language}: word -> text")
    return fixed, changes


def grammar(values, allowed, path, changes, keep_note):
    if not isinstance(values, dict):
        return
    for key, value in list(values.items()):
        if key not in allowed:
            del values[key]
            if keep_note and isinstance(value, (str, int, bool)):
                values["note"] = f"{values['note']}; {key}: {value}" if values.get("note") else f"{key}: {value}"
            changes.append(f"{path}: dropped {key}")
        elif isinstance(value, int) and not isinstance(value, bool) and key in ("conjugation", "declension"):
            values[key] = str(value)
            changes.append(f"{path}/{key}: number -> string")
        elif key == "auxiliary" and value in AUXILIARIES:
            values[key] = AUXILIARIES[value]
            changes.append(f"{path}/{key}: {value} -> {values[key]}")


def transcription(ipa, variety, path, changes):
    decomposed = unicodedata.normalize("NFD", ipa)
    cleaned = "".join(char for char in decomposed if char not in IPA_DROP)
    for pattern, house in HOUSE.get(variety, []) + HOUSE["*"]:
        cleaned = re.sub(pattern, house, cleaned)
    if cleaned != decomposed:
        changes.append(f"{path}: wrote the transcription in house notation")
    return unicodedata.normalize("NFC", cleaned)
