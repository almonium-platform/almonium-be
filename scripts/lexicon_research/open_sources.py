#!/usr/bin/env python3
"""Open dictionaries beside Wiktionary, brought into the tables witness.py and check_v4.py read.

Wiktionary is one witness, edited by volunteers. These are others, each made for one language by
people who do nothing else, each free to load whole, and none of them is copied into an entry:

  CMUdict      English of the United States: 134,000 words with every accepted way of saying each
               and its stress (cmudict.dict; BSD-style licence)
  Britfone     English of Britain: 16,000 words in IPA with stress (britfone.main.3.0.1.csv; MIT)
  OpenRussian  Russian: 60,000 words with their stress in every form, and the gender of nouns
               (nouns.csv, verbs.csv, adjectives.csv, others.csv; CC BY-SA)

  Each language's own Wiktionary  the German Wiktionary for German, the Wiktionnaire for French, and so on: fuller
               for its own language than the English edition the index is built from, and written by its
               speakers (kaikki.org's <lang>-extract.jsonl.gz; CC BY-SA). Read for gender, plural and sounds, and
               for the meanings it numbers (<lang>.native.senses.tsv, which a writer given evidence is shown).

    open_sources.py --from FOLDER [--fetch]     (run from the backend repository root; FOLDER holds the files named
                                                above, and --fetch downloads the ones it lacks, 1.6 GB in all)

writes wiktionary/en.cmudict.facts.tsv, en.britfone.facts.tsv, ru.openrussian.facts.tsv (the shape of
<lang>.facts.tsv) and ru.openrussian.stress.tsv (the shape of ru.stress.tsv). witness.facts() and
check_v4.stress_witness() take in every such file of a language, so a word is held against all of them
and agrees when any has what we wrote. Where to get the files:
    https://raw.githubusercontent.com/cmusphinx/cmudict/master/cmudict.dict
    https://raw.githubusercontent.com/JoseLlarena/Britfone/master/britfone.main.3.0.1.csv
    https://raw.githubusercontent.com/Badestrand/russian-dictionary/master/{nouns,verbs,adjectives,others}.csv
    https://kaikki.org/dictionary/downloads/<lang>/<lang>-extract.jsonl.gz     for de, fr, es, it, pt, ru (1.5 GB in all)
and for those: wiktionary/<lang>.native.facts.tsv, and ru.native.stress.tsv from the stressed forms of its tables.
The Japanese Wiktionary (ja-extract.jsonl.gz, 65 MB) gives ja.native.readings.tsv, how each spelling is read; its
pitch accents are mostly Kyoto's and Osaka's and are not taken.
"""
import argparse
import csv
import gzip
import json
import re
import sys
import unicodedata
from collections import defaultdict
from pathlib import Path

HERE = Path("scripts/lexicon_research")
sys.path.insert(0, str(HERE))
from lemma_index import plain  # noqa: E402

# CMUdict's letters for a sound, as our list for the United States writes it. AH and ER are ə and ɚ without stress.
ARPABET = {"AA": "ɑː", "AE": "æ", "AH": "ʌ", "AO": "ɔː", "AW": "aʊ", "AY": "aɪ", "EH": "ɛ", "ER": "ɝ", "EY": "eɪ", "IH": "ɪ", "IY": "iː",
           "OW": "oʊ", "OY": "ɔɪ", "UH": "ʊ", "UW": "uː", "B": "b", "CH": "tʃ", "D": "d", "DH": "ð", "F": "f", "G": "ɡ", "HH": "h",
           "JH": "dʒ", "K": "k", "L": "l", "M": "m", "N": "n", "NG": "ŋ", "P": "p", "R": "ɹ", "S": "s", "SH": "ʃ", "T": "t", "TH": "θ",
           "V": "v", "W": "w", "Y": "j", "Z": "z", "ZH": "ʒ"}
WEAK = {"AH": "ə", "ER": "ɚ", "IY": "i"}  # without stress: the vowel of "about", of "letter", of "happy"
STRESS = {"1": "ˈ", "2": "ˌ"}
ACUTE = "́"
GENDER = {"m": "Masc", "f": "Fem", "n": "Neut"}


def cmudict(path):
    """word -> its transcriptions in IPA, the stress mark before the stressed vowel."""
    said = defaultdict(list)
    for line in path.read_text(encoding="latin-1").splitlines():
        word, _, phones = line.partition("#")[0].strip().partition(" ")
        word = re.sub(r"\(\d+\)$", "", word)
        if not phones or not word.replace("'", "").replace("-", "").isalpha():
            continue
        ipa = ""
        for phone in phones.split():
            letters, digit = re.match(r"([A-Z]+)(\d?)", phone).groups()
            ipa += STRESS.get(digit, "") + (WEAK[letters] if digit == "0" and letters in WEAK else ARPABET[letters])
        if f"/{ipa}/" not in said[word]:
            said[word].append(f"/{ipa}/")
    return said


def britfone(path):
    said = defaultdict(list)
    for line in path.read_text(encoding="utf-8").splitlines():
        word, _, sounds = line.partition(", ")
        word = re.sub(r"\(\d+\)$", "", word).lower()
        if not sounds or not word.replace("'", "").replace("-", "").isalpha():
            continue
        ipa = "/" + "".join(sounds.split()).replace("ɐ", "ʌ") + "/"  # Britfone writes the vowel of "cup" ɐ
        if ipa not in said[word]:
            said[word].append(ipa)
    return said


def openrussian(folder):
    """(word -> gender of a noun, plain spelling -> its stressed spellings) over the four tables. The data marks
    the stress with an apostrophe after the vowel (челове'к), which is written here as the acute our readings use."""
    genders, stressed = {}, defaultdict(set)
    for name in ("nouns", "verbs", "adjectives", "others"):
        path = folder / f"{name}.csv"
        if not path.exists():
            continue
        with path.open(encoding="utf-8", newline="") as f:
            for row in csv.DictReader(f, delimiter="\t"):
                if name == "nouns" and (row.get("gender") or "") in GENDER and row.get("bare"):
                    genders.setdefault(row["bare"], set()).add(GENDER[row["gender"]])
                for key, cell in row.items():
                    if key and not key.startswith("translations") and cell and "'" in cell:
                        for form in re.split(r"[,;/]\s*", cell):
                            form = form.strip()
                            if "'" in form and " " not in form and re.fullmatch(r"[а-яёА-ЯЁ'-]+", form):
                                marked = form.replace("'", ACUTE)
                                stressed[plain(marked)].add(marked)
    return genders, stressed


GENDER_TAGS = {"masculine": "Masc", "feminine": "Fem", "neuter": "Neut"}
# What an edition says of a transcription, as (the place the witness knows, set apart from the standard). German
# names Austria and Switzerland; Portuguese names the country and the city; Spanish tells its two standards apart
# by the sound itself, so "seseante" is no place; the French and Italian editions mark a regional recording.
PLACES = {"de": {"Austrian German": "Austria", "Swiss Standard German": "Switzerland"},
          "pt": {"Portugal": "Portugal", "Brazil": "Brazil", "Carioca": "Rio-de-Janeiro", "Paulista": "São-Paulo", "Paulistana": "São-Paulo"}}
NO_MARK = {"de": {"auch", "IPA"}, "es": {"seseante", "no seseante", "yeísta", "no yeísta", "America", "IPA"}, "pt": {"IPA"}, "ru": {"singular"},
           "it": {"italiano standard", "IPA"}, "fr": {"IPA"}}


def place_of(lang, marks):
    """(place, aside) for a transcription with these tags and notes."""
    place = next((PLACES[lang][m] for m in marks if m in PLACES.get(lang, {})), "")
    rest = {m for m in marks if m not in PLACES.get(lang, {}) and m not in NO_MARK.get(lang, set()) and "standard" not in m}
    return place, bool(rest)


# What an edition files as a word of its own that is none a learner's entry numbers, and what it writes where a
# definition is still to be written.
NOT_WORDS = {"name", "character", "symbol", "phrase", "proverb", "suffix", "prefix", "infix", "abbrev", "punct"}
NO_DEFINITION = re.compile(r"définition manquante|definizione mancante|definición faltante|definição em falta|отсутствует определение|fehlende bedeutung", re.I)


def meanings_of(record):
    """(the edition's own name for the part of speech, [(labels, definition)]) of one lemma record: the meanings it
    numbers, in its own words, without the inflected forms and the definitions still to be written."""
    senses = []
    for sense in record.get("senses") or []:
        gloss = " ".join(" — ".join(g.strip() for g in sense.get("glosses") or [] if g and g.strip()).split())
        if not gloss or sense.get("form_of") or "form-of" in (sense.get("tags") or []) or NO_DEFINITION.search(gloss):
            continue
        labels = [" ".join(t.split()) for t in (sense.get("raw_tags") or []) + (sense.get("tags") or []) if t and t.strip()][:3]
        senses.append((labels, gloss[:240]))
    return " ".join((record.get("pos_title") or record.get("pos") or "").split()), senses


def native(lang, path, meanings=None):
    """(word -> genders, word -> transcriptions as the facts table writes them, word -> plurals, plain spelling ->
    stressed spellings) out of a language's own Wiktionary: its lemma entries only, the edition's own language only.
    Given a dict, it is filled as well: spelling -> [(part of speech as the edition names it, its meanings)]."""
    genders, said, plurals, stressed = defaultdict(set), defaultdict(list), defaultdict(set), defaultdict(set)
    needle = f'"lang_code": "{lang}"'
    with gzip.open(path, "rt", encoding="utf-8") as f:
        for line in f:
            if needle not in line:
                continue
            record = json.loads(line)
            tags = set(record.get("tags") or [])
            if record.get("lang_code") != lang or "form-of" in tags or not record.get("word"):
                continue
            word = plain(record["word"])
            if "\t" in word or "\n" in word:
                continue
            if meanings is not None and record.get("pos") not in NOT_WORDS:
                heading, senses = meanings_of(record)
                if senses:
                    meanings.setdefault(record["word"], []).append((heading, senses))
            if record.get("pos") == "noun":
                genders[word] |= {GENDER_TAGS[t] for t in tags if t in GENDER_TAGS}
            for form in record.get("forms") or []:
                text, marks = re.sub(r"[\^△†].*$", "", (form.get("form") or "").strip()), set(form.get("tags") or [])  # донья^△: a footnote mark
                if not text or text.startswith("*") or " " in text:
                    continue
                if "plural" in marks and marks <= {"plural", "nominative"} and record.get("pos") == "noun":
                    plurals[word].add(plain(text))
                if lang == "ru" and ACUTE in unicodedata.normalize("NFD", text):
                    stressed[plain(text)].add(unicodedata.normalize("NFC", text))
            for sound in record.get("sounds") or []:
                ipa = "".join((sound.get("ipa") or "").split())  # an edition's transcription may hold a line break
                marks = set(sound.get("tags") or []) | set(sound.get("raw_tags") or []) | {m for m in (sound.get("note"), sound.get("sense")) if m}
                if not ipa or marks & {"X-SAMPA", "SAMPA", "plural"} or '"' in ipa or any("неправильно" in m for m in marks):
                    continue  # another notation, the sound of the plural, or one the edition calls wrong
                if ipa.startswith("\\"):
                    ipa = "/" + ipa.strip("\\") + "/"  # the Wiktionnaire writes \mɛ.zɔ̃\
                if lang == "pt":
                    ipa = ipa.replace("'", "ˈ").replace("ⁿ", "")
                if ipa[0] not in "/[":
                    ipa = f"/{ipa}/"
                place, aside = place_of(lang, marks)
                row = f"{'?' if aside else ''}{place}:{ipa}"
                if not set("|^\\\t") & set(ipa) and row not in said[word]:  # "^h aspiré" and the like are notes, not sounds
                    said[word].append(row)
            # the Italian edition divides a word into syllables and marks the stressed one (cà-sa): that is its stress,
            # kept as a way of saying the word that can confirm ours and is never compared sound by sound
            for division in record.get("hyphenations") or [] if lang == "it" else []:
                parts = [unicodedata.normalize("NFD", p) for p in division.get("parts") or []]
                marked = [i for i, p in enumerate(parts) if "\u0300" in p or "\u0301" in p]
                if len(parts) > 1 and len(marked) == 1:
                    bare = ["".join(ch for ch in p if not unicodedata.combining(ch)) for p in parts]
                    row = "?:[" + ".".join(("ˈ" if i == marked[0] else "") + p for i, p in enumerate(bare)) + "]"
                    if row not in said[word]:
                        said[word].append(row)
    return genders, said, plurals, stressed


def japanese_readings(path):
    """spelling -> the readings the Japanese Wiktionary gives it, in hiragana: the forms it marks as how the word is read."""
    readings = defaultdict(set)
    with gzip.open(path, "rt", encoding="utf-8") as f:
        for line in f:
            if '"lang_code": "ja"' not in line:
                continue
            record = json.loads(line)
            if record.get("lang_code") != "ja" or record.get("pos") == "character" or not record.get("word"):
                continue
            for form in record.get("forms") or []:
                text = (form.get("form") or "").strip()
                if "transliteration" in (form.get("tags") or []) and text and "\t" not in text and "|" not in text:
                    readings[record["word"]].add(hiragana(text))
    return readings


def hiragana(text):
    """Katakana as hiragana: a reading is the same word whichever of the two it is printed in."""
    return "".join(chr(ord(ch) - 0x60) if "ァ" <= ch <= "ヶ" else ch for ch in text)


def write_facts(name, said=None, genders=None, place="", plurals=None):
    """A facts table. said holds transcriptions; with a place they are all of that place, without one each already
    carries its own ("Portugal:/…/", "?:/…/")."""
    path = HERE / "wiktionary" / name
    words = sorted(set(said or {}) | set(genders or {}) | set(plurals or {}))
    with path.open("w", encoding="utf-8") as f:
        for word in words:
            sounds = "|".join(f"{place}:{ipa}" if place else ipa for ipa in (said or {}).get(word, ())[:40])
            f.write(f"{word}\t{','.join(sorted((genders or {}).get(word, ())))}\t{sounds}\t{','.join(sorted((plurals or {}).get(word, ()))[:4])}\n")
    print(f"{len(words)} words -> {path}")


def write_stress(name, stressed):
    path = HERE / "wiktionary" / name
    with path.open("w", encoding="utf-8") as f:
        for word in sorted(stressed):
            f.write(f"{word}\t{'|'.join(sorted(stressed[word]))}\n")
    print(f"{len(stressed)} stressed spellings -> {path}")


SOURCES = {"cmudict.dict": "https://raw.githubusercontent.com/cmusphinx/cmudict/master/cmudict.dict",
           "britfone.main.3.0.1.csv": "https://raw.githubusercontent.com/JoseLlarena/Britfone/master/britfone.main.3.0.1.csv",
           **{f"{kind}.csv": f"https://raw.githubusercontent.com/Badestrand/russian-dictionary/master/{kind}.csv" for kind in ("nouns", "verbs", "adjectives", "others")},
           **{f"{lang}-extract.jsonl.gz": f"https://kaikki.org/dictionary/downloads/{lang}/{lang}-extract.jsonl.gz" for lang in ("de", "fr", "es", "it", "pt", "ru", "ja")}}


def fetch(folder, names=None):
    """Downloads into the folder each source file it does not hold yet. A download that breaks leaves no file."""
    import shutil
    import urllib.request

    folder.mkdir(parents=True, exist_ok=True)
    for name, url in SOURCES.items():
        target = folder / name
        if target.exists() or (names is not None and name not in names):
            continue
        print(f"fetching {url}", flush=True)
        part = target.with_name(target.name + ".part")
        with urllib.request.urlopen(urllib.request.Request(url, headers={"User-Agent": "open-sources/1"}), timeout=120) as response, part.open("wb") as f:
            shutil.copyfileobj(response, f, 1 << 20)
        part.rename(target)


def write_meanings(name, meanings):
    """One line a meaning: spelling, the edition's part of speech, labels, definition. wiktionary.own_meanings reads it."""
    path = HERE / "wiktionary" / name
    count = 0
    with path.open("w", encoding="utf-8") as f:
        for word in sorted(meanings):
            for heading, senses in meanings[word]:
                for labels, gloss in senses[:16]:
                    f.write(f"{word}\t{heading}\t{', '.join(labels)}\t{gloss}\n")
                    count += 1
    print(f"{count} meanings of {len(meanings)} words -> {path}")


def main():
    ap = argparse.ArgumentParser(description=__doc__.split("\n")[0])
    ap.add_argument("--from", dest="folder", required=True, help="the folder that holds the downloaded files")
    ap.add_argument("--own", default="de,fr,es,it,pt,ru", help="the languages whose own Wiktionary is read (each takes a minute or so)")
    ap.add_argument("--fetch", action="store_true", help="download the files the folder does not hold yet")
    args = ap.parse_args()
    folder = Path(args.folder)
    if args.fetch:
        fetch(folder)
    if (folder / "cmudict.dict").exists():
        write_facts("en.cmudict.facts.tsv", cmudict(folder / "cmudict.dict"), place="General-American")
    if (folder / "britfone.main.3.0.1.csv").exists():
        write_facts("en.britfone.facts.tsv", britfone(folder / "britfone.main.3.0.1.csv"), place="Received-Pronunciation")
    if (folder / "nouns.csv").exists():
        genders, stressed = openrussian(folder)
        write_facts("ru.openrussian.facts.tsv", genders=genders)
        write_stress("ru.openrussian.stress.tsv", stressed)
    if (folder / "ja-extract.jsonl.gz").exists():
        readings = japanese_readings(folder / "ja-extract.jsonl.gz")
        path = HERE / "wiktionary" / "ja.native.readings.tsv"
        with path.open("w", encoding="utf-8") as f:
            for word in sorted(readings):
                f.write(f"{word}\t{'|'.join(sorted(readings[word]))}\n")
        print(f"{len(readings)} spellings with a reading -> {path}")
    for lang in filter(None, args.own.split(",")):
        if (folder / f"{lang}-extract.jsonl.gz").exists():
            meanings = {}
            genders, said, plurals, stressed = native(lang, folder / f"{lang}-extract.jsonl.gz", meanings)
            write_facts(f"{lang}.native.facts.tsv", said, genders, plurals=plurals)
            write_meanings(f"{lang}.native.senses.tsv", meanings)
            if stressed:
                write_stress(f"{lang}.native.stress.tsv", stressed)


if __name__ == "__main__":
    main()
