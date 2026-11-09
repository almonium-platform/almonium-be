"""Wiktionary's verdict on every spelling of a language, for the gate before a paid write.

Streams kaikki.org's per-language extract of the English Wiktionary (wiktextract JSONL) and keeps
one line per spelling in wiktionary/<lang>.tsv: the spelling, what its senses are, and the lemmas
its inflected senses point at. Nothing else of the dump is kept. The data is CC BY-SA; it is used
here only to decide whether a headword is worth a call, never published, and the index is not
committed.

A sense is one of
  L  a sense of its own: the spelling is a lemma (also every proper-name sense). A word built
     from another is a lemma too, although Wiktionary files it as "form of": a diminutive
     (хлопчик of хлопець), a verbal noun (виготовлення), a female equivalent (Lehrerin)
  F  "form of X": an inflected form; X is kept as the target lemma
  A  an alternative, obsolete or regional spelling, an acronym or a clipping (French "sida",
     "info"): a word a learner meets, written under its own spelling until entries carry spellings
  X  an abbreviation or misspelling of another word ("млрд", "нажаль")
and a spelling's flags are the union over every entry and part of speech it has ("FL" for
English "parking": a noun of its own and the participle of park).

Usage: lemma_index.py [--langs en,de,fr,es,it,pt,uk]   (run from the backend repository root)
       About 8 GB across the seven languages, streamed; English alone is 3.4 GB.
"""
import argparse, io, json, sys, unicodedata, urllib.request
from collections import defaultdict
from pathlib import Path

HERE = Path("scripts/lexicon_research")
INDEX = HERE / "wiktionary"
KAIKKI = {"en": "English", "de": "German", "fr": "French", "es": "Spanish", "it": "Italian", "pt": "Portuguese", "uk": "Ukrainian"}
URL = "https://kaikki.org/dictionary/{name}/kaikki.org-dictionary-{name}.jsonl"
DERIVED_TAGS = {"diminutive", "augmentative", "endearing", "pejorative", "noun-from-verb", "nominalization", "frequentative"}
DERIVED_GLOSSES = ("female equivalent of", "male equivalent of")
REJECTED_ALT = {"abbreviation", "misspelling"}
KEPT_ALT = {"acronym", "initialism", "clipping"}


def sense_kind(sense):
    """L, F, A or X for one sense; None for an empty placeholder sense that says nothing."""
    tags = set(sense.get("tags") or [])
    gloss = ((sense.get("glosses") or [""])[0] or "").lower()
    if sense.get("form_of") or "form-of" in tags:
        if tags & DERIVED_TAGS or gloss.startswith(DERIVED_GLOSSES):
            return "L"
        return "F"
    if sense.get("alt_of") or "alt-of" in tags:
        return "X" if tags & REJECTED_ALT and not tags & KEPT_ALT else "A"
    if "no-gloss" in tags and not sense.get("glosses"):
        return None
    return "L"


def plain(word):
    """The spelling without the stress marks Wiktionary writes in Cyrillic lemmas (каза́ти -> казати).
    After NFC a Latin vowel with an accent is one character, so only marks no letter absorbs are dropped:
    Spanish "está" and Portuguese "avó" keep theirs."""
    return "".join(c for c in unicodedata.normalize("NFC", word) if c not in "̀́")


def targets(sense):
    return [plain(t["word"]) for t in sense.get("form_of") or [] if t.get("word")]


def classify(lines):
    """Spelling -> (flags, form-of targets) over every record of a JSONL stream."""
    flags, aims = defaultdict(set), defaultdict(list)
    for line in lines:
        try:
            record = json.loads(line)
        except json.JSONDecodeError:
            continue
        word = record.get("word")
        if not word:
            continue
        for sense in record.get("senses") or []:
            kind = sense_kind(sense)
            if kind is None:
                continue
            flags[word].add(kind)
            if kind == "F":
                for t in targets(sense):
                    if t not in aims[word]:
                        aims[word].append(t)
    return {w: ("".join(sorted(f)), aims.get(w, [])) for w, f in flags.items()}


def write_index(lang, verdicts):
    INDEX.mkdir(parents=True, exist_ok=True)
    out = INDEX / f"{lang}.tsv"
    tmp = out.with_suffix(".tsv.part")
    with tmp.open("w", encoding="utf-8") as f:
        for word in sorted(verdicts):
            fl, aims = verdicts[word]
            clean = [a for a in aims if "\t" not in a and "|" not in a][:8]
            f.write(f"{word}\t{fl}\t{'|'.join(clean)}\n")
    tmp.replace(out)  # a run cut off halfway leaves the old index, never half a new one
    return out


def build(lang):
    req = urllib.request.Request(URL.format(name=KAIKKI[lang]), headers={"User-Agent": "almonium-lexicon-research/1.0"})
    with urllib.request.urlopen(req, timeout=120) as response:
        verdicts = classify(io.TextIOWrapper(response, encoding="utf-8"))
    out = write_index(lang, verdicts)
    counts = defaultdict(int)
    for fl, _ in verdicts.values():
        counts["lemma" if "L" in fl else "form only" if "F" in fl else "spelling only" if "A" in fl else "abbreviation or misspelling"] += 1
    print(f"{lang}: {len(verdicts)} spellings -> {out} {dict(counts)}", flush=True)


if __name__ == "__main__":
    ap = argparse.ArgumentParser()
    ap.add_argument("--langs", default="en,de,fr,es,it,pt,uk")
    a = ap.parse_args()
    for lang in a.langs.split(","):
        if lang not in KAIKKI:
            sys.exit(f"no kaikki extract is named for {lang}")
        build(lang)
