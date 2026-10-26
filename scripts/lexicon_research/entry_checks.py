"""Deterministic checks of a written entry, free to run on every entry before any model reviews it.

The schema says what shape an entry has; these say what the schema cannot: that every variety belongs to the
entry's language and is one the product knows, that a transcription uses only its variety's phoneme inventory,
that no word mixes alphabets (the Polish gloss 'łyżка' with Cyrillic final letters), that an example shows its
word, that a region or field has not slipped into "usage" or a definition, and that two lexemes are not one.

Each finding is {"path": JSON Pointer, "check": name, "severity": "error" | "warn", "message": text}. An error
is a structural fault the entry must not be published with; a warning marks something for review.
Usage: entry_checks.py out/<writer>/<lang> [...]   (prints findings per file, then totals by check)
"""

import json
import re
import sys
import unicodedata
from collections import Counter
from pathlib import Path

import jsonschema

from prompting import UN_REGIONS, VARIETIES

HERE = Path(__file__).resolve().parent
SCHEMA = json.loads((HERE / "entry.schema.json").read_text())
INVENTORY = {
    variety: json.loads((HERE / "inventories" / f"{variety}.json").read_text())
    for variety in {variety for varieties in VARIETIES.values() for variety in varieties}
}
COUNTRIES = set(json.loads((HERE / "countries.json").read_text()))
# What a transcription may hold besides its phonemes: slashes, stress, syllable dots, spaces between words, and
# the tie bars that only say how an affricate is written.
IPA_FRAME = set("/ˈˌ. \u0361\u035c")
REGION_WORDS = re.compile(
    r"\b(British|American|North American|Australian|Canadian|Irish|Scottish|Indian|UK|US|USA|Brasil|Brazil(?:ian)?|"
    r"Portugal|European Portuguese|Spain|Mexic(?:o|an)|Latin America(?:n)?|Argentin(?:a|e|ian)|Colombia(?:n)?|"
    r"Austria(?:n)?|Swiss|Switzerland|Belgi(?:um|an)|Quebec|Canada)\b"
)


def check(entry):
    """Every finding for one entry, schema violations first."""
    findings = []

    def find(path, name, severity, message):
        findings.append({"path": path, "check": name, "severity": severity, "message": message})

    for error in jsonschema.Draft202012Validator(SCHEMA).iter_errors(entry):
        find("/" + "/".join(str(part) for part in error.absolute_path), "schema", "error", error.message)
    lang = entry.get("language")
    voiced = VARIETIES.get(lang, [])
    seen_lexemes = {}
    for li, lexeme in enumerate(entry.get("lexemes") or []):
        base = f"/lexemes/{li}"
        check_regions(lexeme.get("regions"), lang, f"{base}/regions", find)
        for pi, spoken in enumerate(lexeme.get("pronunciations") or []):
            check_transcription(spoken.get("variety"), spoken.get("ipa"), voiced, f"{base}/pronunciations/{pi}", find)
        words = {lexeme.get("lemma", "")}
        for fi, form in enumerate(lexeme.get("forms") or []):
            words.add(form.get("form", ""))
            check_script(form.get("form", ""), lang, f"{base}/forms/{fi}/form", find)
            check_regions(form.get("regions"), lang, f"{base}/forms/{fi}/regions", find)
            for variety, ipa in (form.get("ipa") or {}).items():
                check_transcription(variety, ipa, voiced, f"{base}/forms/{fi}/ipa/{variety}", find)
        for si, sense in enumerate(lexeme.get("senses") or []):
            path = f"{base}/senses/{si}"
            check_regions(sense.get("regions"), lang, f"{path}/regions", find)
            for field in ("definition", "example"):
                check_script(sense.get(field) or "", lang, f"{path}/{field}", find)
            for gloss_lang, gloss in (sense.get("glosses") or {}).items():
                text = gloss if isinstance(gloss, str) else (gloss or {}).get("text", "")
                check_script(text, gloss_lang, f"{path}/glosses/{gloss_lang}", find)
            if lang != "en" and in_english(sense.get("definition") or "", lang):
                find(f"{path}/definition", "definition-language", "error", f"written in English, not {lang}")
            for field in ("usage", "definition"):
                match = REGION_WORDS.search(sense.get(field) or "")
                if match:
                    find(
                        f"{path}/{field}",
                        "region-in-text",
                        "warn",
                        f'names a region ({match.group(0)}); a region belongs in "regions"',
                    )
            example = sense.get("example") or ""
            if example and not shows_word(example, words):
                find(
                    f"{path}/example", "example-word", "warn", "the example uses neither the lemma nor any listed form"
                )
        # Inflection is part of identity: "lie" (lay, lain) and "lie" (lied) are two verbs said alike.
        identity = (
            lexeme.get("pos"),
            (lexeme.get("grammar") or {}).get("gender"),
            tuple(sorted((p.get("variety"), p.get("ipa")) for p in lexeme.get("pronunciations") or [])),
            tuple(sorted(form.get("form", "") for form in lexeme.get("forms") or [])),
        )
        if identity in seen_lexemes:
            find(
                base,
                "duplicate-lexeme",
                "error",
                f"same part of speech, gender, pronunciation and forms as /lexemes/{seen_lexemes[identity]}: one lexeme",
            )
        seen_lexemes.setdefault(identity, li)
    return findings


def in_english(text, lang):
    """Whether a definition meant to be in lang reads as English: most of its words are commoner in English."""
    from wordfreq import zipf_frequency

    words = [word.lower() for word in re.findall(r"[^\W\d_]+", text) if len(word) > 2]
    if len(words) < 4:
        return False
    english = sum(1 for word in words if zipf_frequency(word, "en") > zipf_frequency(word, lang) + 0.5)
    return english / len(words) >= 0.7


def check_regions(regions, lang, path, find):
    """A region is the entry's language with a country or a known UN region (es-EC, es-419), not another language."""
    for ri, region in enumerate(regions or []):
        tag = region.get("variety", "")
        language, _, area = tag.partition("-")
        if language != lang or not (area in COUNTRIES or area in UN_REGIONS):
            find(f"{path}/{ri}/variety", "region", "error", f"{tag} is not {lang} with a country or a UN region")


def check_transcription(variety, ipa, voiced, path, find):
    if variety not in voiced:
        find(path, "variety", "error", f"{variety} is not transcribed for this language ({', '.join(voiced)})")
        return
    inventory = INVENTORY[variety]
    allowed = IPA_FRAME | {
        char
        for symbol in inventory["consonants"] + inventory["vowels"] + inventory["diacritics"]
        for char in unicodedata.normalize("NFD", symbol)
    }
    stray = sorted({char for char in unicodedata.normalize("NFD", ipa or "") if char not in allowed})
    if stray:
        find(path, "ipa-inventory", "error", f"symbols outside the {variety} inventory: {' '.join(stray)}")


SCRIPTS = {"LATIN": "Latin", "CYRILLIC": "Cyrillic", "GREEK": "Greek"}
EXPECTED_SCRIPT = {"uk": "Cyrillic", "ru": "Cyrillic", "be": "Cyrillic", "bg": "Cyrillic", "el": "Greek"}


def check_script(text, lang, path, find):
    """A word that mixes alphabets is always a slip. A text with no letter of its language's alphabet is only worth a
    look: a Ukrainian gloss may rightly be a brand (WhatsApp) or a Latin phrase (sic)."""
    for word in re.findall(r"\w+", text):
        scripts = {script_of(char) for char in word if char.isalpha()} - {None}
        if len(scripts) > 1:
            find(path, "mixed-script", "error", f"'{word}' mixes {' and '.join(sorted(scripts))} letters")
            return
    letters = {script_of(char) for char in text if char.isalpha()} - {None}
    expected = EXPECTED_SCRIPT.get(lang, "Latin")
    if letters and expected not in letters:
        find(path, "script", "warn", f"written in {', '.join(sorted(letters))}; {lang} is written in {expected}")


def script_of(char):
    name = unicodedata.name(char, "")
    return next((script for key, script in SCRIPTS.items() if name.startswith(key)), None)


def shows_word(example, words):
    """Whether the example holds the lemma or a form, ignoring case and accents, or one ending a phrase's word."""
    text = fold(example)
    for word in filter(None, words):
        parts = fold(word).split()
        if all(re.search(rf"(?<!\w){re.escape(part)}", text) for part in parts):
            return True
    return False


def fold(text):
    return "".join(char for char in unicodedata.normalize("NFD", text.lower()) if not unicodedata.combining(char))


def main(folders):
    totals = Counter()
    for folder in folders:
        for path in sorted(Path(folder).glob("*.json")):
            if path.name.endswith(".meta.json"):
                continue
            entry = json.loads(path.read_text())
            if "error" in entry:
                continue
            for finding in check(entry):
                totals[(finding["check"], finding["severity"])] += 1
                print(f"{path}: {finding['severity']} {finding['check']} {finding['path']}: {finding['message']}")
    for (name, severity), count in sorted(totals.items()):
        print(f"{severity:5} {name:18} {count}", file=sys.stderr)


if __name__ == "__main__":
    main(sys.argv[1:])
