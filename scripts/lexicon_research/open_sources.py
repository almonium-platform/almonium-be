#!/usr/bin/env python3
"""Open dictionaries beside Wiktionary, brought into the tables witness.py and check_v4.py read.

Wiktionary is one witness, edited by volunteers. These are others, each made for one language by
people who do nothing else, each free to load whole, and none of them is copied into an entry:

  CMUdict      English of the United States: 134,000 words with every accepted way of saying each
               and its stress (cmudict.dict; BSD-style licence)
  Britfone     English of Britain: 16,000 words in IPA with stress (britfone.main.3.0.1.csv; MIT)
  OpenRussian  Russian: 60,000 words with their stress in every form, and the gender of nouns
               (nouns.csv, verbs.csv, adjectives.csv, others.csv; CC BY-SA)

    open_sources.py --from FOLDER      (run from the backend repository root; FOLDER holds the files named above)

writes wiktionary/en.cmudict.facts.tsv, en.britfone.facts.tsv, ru.openrussian.facts.tsv (the shape of
<lang>.facts.tsv) and ru.openrussian.stress.tsv (the shape of ru.stress.tsv). witness.facts() and
check_v4.stress_witness() take in every such file of a language, so a word is held against all of them
and agrees when any has what we wrote. Where to get the files:
    https://raw.githubusercontent.com/cmusphinx/cmudict/master/cmudict.dict
    https://raw.githubusercontent.com/JoseLlarena/Britfone/master/britfone.main.3.0.1.csv
    https://raw.githubusercontent.com/Badestrand/russian-dictionary/master/{nouns,verbs,adjectives,others}.csv
"""
import argparse
import csv
import re
import sys
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


def write_facts(name, said=None, genders=None, place=""):
    path = HERE / "wiktionary" / name
    words = sorted(set(said or {}) | set(genders or {}))
    with path.open("w", encoding="utf-8") as f:
        for word in words:
            f.write(f"{word}\t{','.join(sorted((genders or {}).get(word, ())))}\t{'|'.join(f'{place}:{ipa}' for ipa in (said or {}).get(word, ()))}\t\n")
    print(f"{len(words)} words -> {path}")


def main():
    ap = argparse.ArgumentParser(description=__doc__.split("\n")[0])
    ap.add_argument("--from", dest="folder", required=True, help="the folder that holds the downloaded files")
    folder = Path(ap.parse_args().folder)
    if (folder / "cmudict.dict").exists():
        write_facts("en.cmudict.facts.tsv", cmudict(folder / "cmudict.dict"), place="General-American")
    if (folder / "britfone.main.3.0.1.csv").exists():
        write_facts("en.britfone.facts.tsv", britfone(folder / "britfone.main.3.0.1.csv"), place="Received-Pronunciation")
    if (folder / "nouns.csv").exists():
        genders, stressed = openrussian(folder)
        write_facts("ru.openrussian.facts.tsv", genders=genders)
        path = HERE / "wiktionary" / "ru.openrussian.stress.tsv"
        with path.open("w", encoding="utf-8") as f:
            for word in sorted(stressed):
                f.write(f"{word}\t{'|'.join(sorted(stressed[word]))}\n")
        print(f"{len(stressed)} stressed spellings -> {path}")


if __name__ == "__main__":
    main()
