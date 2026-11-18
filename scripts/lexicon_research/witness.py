#!/usr/bin/env python3
"""Hold what the writers wrote against what Wiktionary states, fact by fact, with no model in between.

    scripts/lexicon_research/witness.py                 # every language with a facts table
    scripts/lexicon_research/witness.py --langs pt,es --show 20     (run from the backend repository root)

A writer states four kinds of fact that a reference states too and that a reader cannot check by eye
without knowing the language: the gender of a noun, the syllable its stress falls on, the sounds of
its transcription, and its plural. lemma_index.py copies those out of Wiktionary into
wiktionary/<lang>.facts.tsv; this script compares, entry by entry, and prints for each kind how many
could be compared, how many agree, and the ones that do not.

A disagreement is a lead and not a verdict. Wiktionary is edited by volunteers, a word of two
genders or two stresses has both on one line here, and the two sides may write one sound with two
symbols. So the comparison is as lenient as each fact allows (ours must be among theirs), and the
transcriptions are compared sound by sound only after both are brought to one spelling. What is left
is short enough to read, and it is the list a paid check should be spent on.

Nothing here is copied into an entry: a witness is asked, it does not write.
"""
import argparse
import json
import re
import sys
import unicodedata
from collections import Counter, defaultdict
from pathlib import Path

HERE = Path("scripts/lexicon_research")
sys.path.insert(0, str(HERE))
import ingest_v4  # noqa: E402
from lemma_index import plain  # noqa: E402

# What Wiktionary calls the place a transcription belongs to, for each variety of ours. A word none of
# whose transcriptions is named for the place is compared with those named for no place at all.
REGIONS = {"en-US": {"General-American", "US"}, "en-GB": {"Received-Pronunciation", "UK", "British"},
           "pt-BR": {"Brazil", "São-Paulo", "Rio-de-Janeiro"}, "pt-PT": {"Portugal"}, "de-DE": {"Germany", "Standard"},
           "fr-FR": {"France", "Europe", "Paris"}, "es-ES": {"Spain"}, "es-MX": {"Latin-America", "Mexico"}}
VOWELS = set("aeiouyɑɐɒæɛɜɞəɘɵɤɯɨʉɪʏʊøœɶɔʌɚɝ")
SYLLABIC = "̩"  # under a consonant that is a syllable by itself: [ˈmɛːtçn̩]
STRESS = "ˈ"
# One spelling for sounds the two sides write differently without meaning a different sound. Read
# from the mismatches themselves: every pair here was the whole difference in dozens of words.
SAME_SOUND = {"": {"ɡ": "g", "ɹ": "r", "‿": ""},
              "en": {"ɛ": "e", "ɚ": "ər", "ɝ": "ɜr", "ɫ": "l", "oʊ": "əʊ", "ɜː": "ɜ", "iː": "i", "uː": "u", "ɑː": "ɑ", "ɔː": "ɔ"},
              "de": {"ʁ": "r", "ʀ": "r", "ɐ̯": "ɐ", "ʊ̯": "ʊ", "ɪ̯": "ɪ", "ʏ̯": "ʏ", "i̯": "i", "ʔ": ""},
              "fr": {"ʁ": "r", "ɑ": "a", "œ̃": "ɛ̃"},
              "es": {"ʎ": "ʝ", "j": "i", "w": "u"},
              "it": {},
              "pt": {"x": "ʁ", "h": "ʁ", "ɦ": "ʁ", "χ": "ʁ", "r": "ʁ", "ɻ": "ɾ", "ɫ": "l", "w̃": "u", "j̃": "i", "j": "i", "w": "u", "ʷ": "u"},
              "ru": {"ɫ": "l", "ɕː": "ɕɕ"}}
# A sound written two ways by its place in the word and not by what is said: Wiktionary writes a Spanish stop
# before a consonant as its voiced one (/ˈaɡto/ for "acto") and n before b as m, a Portuguese vowel before m or n as a nasal one,
# and the first vowel of "estrada" in Portugal as /(i)/ where a dictionary of Portugal prints /ɨ/.
BY_PLACE = {"es": [(re.compile(r"[pb](?=[^aeiou])"), "b"), (re.compile(r"[td](?=[^aeiouɾ])"), "d"), (re.compile(r"[kg](?=[^aeiouɾl])"), "g"),
                   (re.compile(r"n(?=[bpm])"), "m")],
            "pt": [(re.compile("\u0303(?=[mnɲ])"), ""), (re.compile(r"^ɨ(?=ʃ)"), "i")]}
# Where the two sides write sounds alike once SAME_SOUND is applied, so that a difference is mostly a fault.
# English, German and Russian are not there yet: Wiktionary's weak vowels, its syllabic consonants and its
# narrow Russian transcription differ from ours in thousands of words that are right on both sides.
SOUNDS_COMPARABLE = {"es", "it", "fr", "pt"}
DROPPED = set("/[]().ˈˌ ͜͡-")


def facts(lang):
    """word -> (genders, [(regions, transcription)], plurals), or None when the table has not been built."""
    path = HERE / "wiktionary" / f"{lang}.facts.tsv"
    if not path.exists():
        return None
    table = {}
    with path.open(encoding="utf-8") as f:
        for line in f:
            word, genders, sounds, plurals = line.rstrip("\n").split("\t")
            heard = []
            for sound in filter(None, sounds.split("|")):
                region, _, ipa = sound.partition(":")
                if not ipa.strip("/[]").endswith("-") and not ipa.strip("/[]").startswith("-"):  # /ɹə-/ is a piece of a word
                    heard.append((set(filter(None, region.split("+"))), ipa))
            table[word] = (set(filter(None, genders.split(","))), heard, set(filter(None, plurals.split(","))))
    return table


def nuclei(ipa):
    """How many syllables a transcription has, counted by their vowels: a run of vowel letters is one
    (a diphthong), and a consonant marked as a syllable is one too."""
    count, inside = 0, False
    for ch in unicodedata.normalize("NFD", ipa):
        if ch in VOWELS:
            count += not inside
            inside = True
        elif ch == SYLLABIC:
            count += 1
        elif unicodedata.combining(ch) or ch in "ːˑ":
            continue
        else:
            inside = False
    return count


def stress_place(ipa):
    """(syllables before the stressed one, syllables from it to the end), or None when the transcription
    does not say: more than one syllable and no stress mark, or a mark on the second of two words."""
    ipa = ipa.strip("/[]")
    if " " in ipa:
        return None
    if STRESS not in ipa:
        return (0, 1) if nuclei(ipa) == 1 else None
    before, _, after = ipa.partition(STRESS)
    return (nuclei(before), nuclei(after)) if nuclei(after) else None


def same_stress(ours, theirs):
    """A place agrees when it is the same counted from the start or from the end: two transcriptions of one
    word may divide a run of vowels differently on one side of the stress, hardly on both."""
    return ours[0] == theirs[0] or ours[1] == theirs[1]


def sounds(lang, ipa):
    """A transcription as its sounds alone, in one spelling: no slashes, dots, stress marks or tie bars, and
    every symbol of SAME_SOUND brought to one."""
    text = unicodedata.normalize("NFC", "".join(ch for ch in unicodedata.normalize("NFC", ipa) if ch not in DROPPED))
    for table in (SAME_SOUND[lang], SAME_SOUND[""]):
        for old in sorted(table, key=len, reverse=True):
            text = text.replace(old, table[old])
    for pattern, new in BY_PLACE.get(lang, ()):
        text = unicodedata.normalize("NFC", pattern.sub(new, unicodedata.normalize("NFD", text)))
    return text.replace("ː", "") if lang in ("en", "es", "pt", "ru") else text


def heard_in(variety, heard):
    """The transcriptions of a word that belong to a variety of ours: those Wiktionary names for its place, else
    those it names for no place at all; the ones between slashes when there are any. Spanish is named for
    no place, so Spain is told from Mexico by the sound itself: /θ/ where the word has one."""
    here = [ipa for regions, ipa in heard if regions & REGIONS.get(variety, set())] or [ipa for regions, ipa in heard if not regions]
    if any(ipa.startswith("/") for ipa in here):
        here = [ipa for ipa in here if ipa.startswith("/")]
    if variety.startswith("es-") and any("θ" in ipa for ipa in here) and any("θ" not in ipa for ipa in here):
        here = [ipa for ipa in here if ("θ" in ipa) == (variety == "es-ES")]
    return here


def differences(ours, theirs):
    """The sounds two spellings of one word differ in, as (ours, theirs) pairs: the nearest of theirs is taken."""
    import difflib
    best = max(theirs, key=lambda t: difflib.SequenceMatcher(None, ours, t).ratio())
    return [(ours[a:b] or "-", best[c:d] or "-") for tag, a, b, c, d in difflib.SequenceMatcher(None, ours, best).get_opcodes() if tag != "equal"]


def plurals_of(lang, lexeme):
    """The plural a lexeme states among its forms: the nominative one where the language has cases."""
    found = set()
    for form in lexeme.get("forms") or []:
        features = form.get("features") or {}
        if features.get("Number") == "Plur" and features.get("Case", "Nom") == "Nom" and set(features) <= {"Number", "Case"}:
            found.add(plain(form.get("form") or ""))
    return found


def compare(lang, entry, table):
    """(fact, verdict, detail) for every fact of one written entry that Wiktionary states too. The verdict is
    "agrees" or "differs"; a fact Wiktionary does not state is not in the list."""
    found = []
    for lexeme in entry.get("lexemes") or []:
        lemma = lexeme.get("lemma") or ""
        known = table.get(lemma) or table.get(plain(lemma))
        if not known or " " in lemma:
            continue
        genders, heard, plurals = known
        gender = (lexeme.get("grammar") or {}).get("gender")
        if lexeme.get("pos") == "NOUN" and gender and genders and gender != "Com":
            found.append(("gender", "agrees" if gender in genders else "differs", f"{lemma}: {gender}, Wiktionary has {', '.join(sorted(genders))}"))
        if (ours := plurals_of(lang, lexeme)) and plurals and lexeme.get("pos") == "NOUN":
            found.append(("plural", "agrees" if ours & plurals else "differs", f"{lemma}: {', '.join(sorted(ours))}, Wiktionary has {', '.join(sorted(plurals))}"))
        for p in lexeme.get("pronunciations") or []:
            ipa, variety = p.get("ipa") or "", p.get("variety") or ""
            if not ipa:
                continue
            anywhere = [t for _, t in heard]
            place = stress_place(ipa)
            theirs = [t for t in (stress_place(i) for i in anywhere) if t]
            if place and theirs and sum(place) > 1:
                same = any(same_stress(place, t) for t in theirs)
                found.append(("stressed syllable", "agrees" if same else "differs", f"{lemma} {variety}: {ipa}, Wiktionary has {' '.join(anywhere[:4])}"))
            here = heard_in(variety, heard)
            if here:
                mine, known = sounds(lang, ipa), {sounds(lang, t) for t in here}
                pairs = "" if mine in known else " ".join(f"{a}>{b}" for a, b in differences(mine, sorted(known)))
                found.append(("sounds", "differs" if pairs else "agrees", f"{lemma} {variety}: {ipa}, Wiktionary has {' '.join(here[:4])}  [{pairs}]"))
    return found


def survey(lang, show, leads):
    """Prints one language's count and adds every disagreement to leads; returns the counts."""
    table = facts(lang)
    if table is None:
        print(f"{lang}: no facts table; run lemma_index.py --langs {lang}")
        return None
    counts, differing, pairs = defaultdict(Counter), defaultdict(list), Counter()
    raw = ingest_v4.load_raw(lang)
    for headword in sorted(raw):
        written, meta = raw[headword]
        if not written.get("lexemes"):
            continue
        entry = ingest_v4.clean_entry(lang, json.loads(json.dumps(written)), meta)  # as it is published, the free rules applied
        for fact, verdict, detail in compare(lang, entry, table):
            counts[fact][verdict] += 1
            if verdict == "differs":
                differing[fact].append(detail)
                leads.append({"language": lang, "headword": headword, "fact": fact, "detail": detail})
                if fact == "sounds":
                    pairs.update(detail.rpartition("[")[2].rstrip("]").split())
    mine = [lead for lead in leads if lead["language"] == lang]
    trusted = {lead["headword"] for lead in mine if lead["fact"] != "sounds" or lang in SOUNDS_COMPARABLE}
    print(f"\n{lang}: {len(raw)} entries held against Wiktionary, {len(trusted)} with a disagreement worth reading")
    for fact in ("gender", "plural", "stressed syllable", "sounds"):
        n = sum(counts[fact].values())
        if not n:
            continue
        print(f"    {fact:18} {n:6} compared, {counts[fact]['agrees'] / n:6.1%} agree, {counts[fact]['differs']:5} differ")
        if fact == "sounds" and pairs:
            print("        differing most often, ours>theirs: " + "  ".join(f"{pair} {times}" for pair, times in pairs.most_common(24)))
        for line in differing[fact][:show]:
            print(f"        {line}")
    return {**{fact: dict(c) for fact, c in counts.items()}, "entries": len(raw), "entries to read": len(trusted)}


def main():
    ap = argparse.ArgumentParser(description=__doc__.split("\n")[0])
    ap.add_argument("--langs", default="en,de,fr,es,it,pt,ru")
    ap.add_argument("--show", type=int, default=8, help="how many disagreements of each kind to print")
    ap.add_argument("--json", help="write the counts here as well")
    ap.add_argument("--leads", help="write every disagreement here: language, headword, fact, detail")
    args = ap.parse_args()
    leads = []
    totals = {lang: survey(lang, args.show, leads) for lang in args.langs.split(",")}
    if args.json:
        Path(args.json).write_text(json.dumps(totals, indent=1) + "\n")
    if args.leads:
        Path(args.leads).write_text(json.dumps(leads, ensure_ascii=False, indent=1) + "\n", encoding="utf-8")


if __name__ == "__main__":
    main()
