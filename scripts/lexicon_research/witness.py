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
import difflib
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

# What Wiktionary calls the place a transcription belongs to, for each variety of ours. A transcription is of
# the place when it is named for it and for no place outside COMPANY: "Canada+General-American" is American,
# "Northumbria+UK" is not Britain's standard, and one named for both standards is of both.
REGIONS = {"en-US": {"General-American", "US"}, "en-GB": {"Received-Pronunciation", "UK"},
           "pt-BR": {"Brazil", "São-Paulo", "Rio-de-Janeiro"}, "pt-PT": {"Portugal"}, "de-DE": {"Germany", "Standard"},
           "fr-FR": {"France", "Europe", "Paris"}, "es-ES": {"Spain"}, "es-MX": {"Latin-America", "Mexico"}}
COMPANY = {"en": {"General-American", "US", "Received-Pronunciation", "UK", "Canada"}}
# Where Wiktionary names a place for half the words and none for the rest, and the rest may be of any place:
# such a transcription can confirm what we wrote and cannot contradict one variety of ours by itself.
PLACE_UNSAID = {"en"}
VOWELS = set("aeiouyɑɐɒæɛɜɞəɘɵɤɯɨʉɪʏʊøœɶɔʌɚɝ")
SYLLABIC = "̩"  # under a consonant that is a syllable by itself: [ˈmɛːtçn̩]
STRESS = "ˈ"
# Both sides are brought to one spelling before they are compared, in this order: a consonant that is a
# syllable by itself becomes schwa and the consonant, marks finer than our symbol lists are dropped, the
# symbols of SAME_SOUND are replaced (longest first), then the rules of BY_PLACE. Every line was read off
# the disagreements themselves: a pair that was the whole difference in dozens of right words.
SYLLABLE_OF_ITS_OWN = re.compile("([mnŋɱlrɹ])[\u0329\u030d]")
ASSIMILATED = {"de": {"ŋ": "n", "ɱ": "n"}}  # German -en said as [ŋ̩] after g and [ɱ̩] after f is still /ən/
FINER = {"en": "\u032f\u031f\u0308\u0325\u031e\u031d\u032a\u032c\u0303ʰˑ", "de": "\u032f\u0325\u0320ʰʼˑʔ", "ru": "\u032f\u0361",
         "fr": "", "es": "\u031e\u032a\u0303\u032f", "it": "", "pt": ""}
SAME_SOUND = {"": {"ɡ": "g", "ɹ": "r", "‿": ""},
              # iː and uː are kept apart from the weak i of "happy" and u of "situation", which Wiktionary writes ɪ and ʊ
              "en": {"iː": "I", "uː": "U", "ɛː": "eə", "ɛ": "e", "ɜːr": "ər", "ɜr": "ər", "ɝ": "ər", "ɚ": "ər", "əː": "ɜ", "ɫ": "l", "ʍ": "w",
                     "ɾ": "t", "oʊ": "əʊ", "ou": "əʊ", "ʌɪ": "aɪ", "ː": ""},
              "en-US": {"ɒ": "ɑ"},  # no variety of the United States has the vowel; a transcription with it is Britain's
              "de": {"ʁ": "r", "ʀ": "r", "χ": "x", "ɔɪ": "ɔʏ", "ɔy": "ɔʏ", "ae": "aɪ", "ao": "aʊ", "ai": "aɪ", "au": "aʊ"},
              "fr": {"ʁ": "r", "ɑ": "a", "œ̃": "ɛ̃", "ː": ""},
              # the Spanish Wikcionario writes narrowly: the soft b, d, g between vowels, s voiced before m, n as it meets the next sound
              "es": {"ʎ": "ʝ", "j": "i", "w": "u", "ː": "", "ɟʝ": "ʝ", "β": "b", "ð": "d", "ɣ": "g", "z": "s", "ŋ": "n", "ɱ": "n", "ɲʝ": "nʝ"},
              "it": {"aw": "au", "ew": "eu", "tts": "ts", "ddz": "dz"},  # Wiktionary writes the z of "precauzione" double and "causa" with w
              "pt": {"x": "ʁ", "h": "ʁ", "ɦ": "ʁ", "χ": "ʁ", "r": "ʁ", "ɻ": "ɾ", "ɫ": "l", "w̃": "u", "j̃": "i", "j": "i", "w": "u", "ʷ": "u", "ː": ""},
              # Wiktionary's Russian is narrow: the vowels it writes between soft consonants and the dark l are ours without the detail
              "ru": {"ɫ": "l", "ʊ": "u", "ɛ": "e", "æ": "a", "ɵ": "o", "ʉ": "u", "ɔ": "o", "ɣ": "g", "ː": ""}}
# A sound written two ways by its place in the word and not by what is said. Spanish: Wiktionary writes a stop
# before a consonant as its voiced one (/ˈaɡto/ for "acto") and n before b as m. Portuguese: a vowel before m
# or n as a nasal one, and the first vowel of "estrada" in Portugal as /(i)/ where a dictionary there prints /ɨ/.
# English: the trap vowel as /a/ in newer transcriptions of Britain, /or/ for /ɔr/, and the schwa some write
# between a vowel and r ("sheer" as /ʃɪɚ/ or /ʃɪɹ/). German: r after a vowel is the vowel ɐ whichever side
# writes it so, -er and ver- are ɐ, and ver- before a vowel is /fɛr/ ("Verein"). Russian: a consonant softened only by the soft one after it, and a
# double consonant, which Wiktionary writes long or single.
BY_PLACE = {"es": [(re.compile(r"[pb](?=[^aeiou])"), "b"), (re.compile(r"[td](?=[^aeiouɾ])"), "d"), (re.compile(r"[kg](?=[^aeiouɾl])"), "g"),
                   (re.compile(r"n(?=[bpm])"), "m")],
            "pt": [(re.compile("\u0303(?=[mnɲ])"), ""), (re.compile(r"^ɨ(?=ʃ)"), "i")],
            # French: o and e outside the last syllable are written open or closed by turns ("opération", "rétroviseur"),
            # and b or d before a voiceless sound as said (/ap.sɛ̃t/) or as spelt
            "fr": [(re.compile("ɔ(?!\u0303)(?=.*[aeiouyøœɛɔə])"), "o"), (re.compile("ɛ(?!\u0303)(?=.*[aeiouyøœɛɔə])"), "e"),
                   (re.compile(r"b(?=[stkfʃ])"), "p"), (re.compile(r"d(?=[stkfʃ])"), "t"),
                   (re.compile(r"(?<=i)j(?=[aeiouyøœɛɔə])"), "")],  # the glide the Wiktionnaire writes after i: /bi.bli.jɔ/
            "en": [(re.compile(r"a(?![ɪʊ])"), "æ"), (re.compile(r"o(?=r)"), "ɔ"), (re.compile(r"(?<=[ɪeʊ])ə(?=r)"), "")],
            "de": [(re.compile(r"r(?![aeiouyøœɛɔɪʊʏəɐj])"), "ɐ"), (re.compile(r"[əɛe]ɐ"), "ɐ"), (re.compile(r"ɐ(?=[aeiouyøœɛɔɪʊʏ])"), "ɛr")],
            "ru": [(re.compile(r"ʲ(?=[^aeiouɨɐəɪ])"), ""), (re.compile(r"([^aeiouɨɐəɪ])\1"), r"\1")]}
# Two sounds either of which a dictionary prints in the same place of a word, so that one for the other is
# no fault ("-" is no sound at all). English: a weak vowel, which one dictionary writes ə and the next ɪ, ʊ
# or the full vowel ("roses", "careful", "hello", "prolonged") or leaves out ("signatory"); the vowel of
# "happy" (i, ɪ) and of "sheep" with its length mark or without (I is iː above); n before k and g as ŋ
# ("incarnation"); in the United States, the vowels of "cot" and "caught". So an English weak vowel is not
# judged at all: the stress is, and a full vowel under it. German: the i of "Aktion" as a vowel or as j, r
# before a vowel that begins the next part of a word, and the schwa of -el and -en. Russian: an unstressed
# vowel after a soft consonant, which Wiktionary writes ɪ or e where our list has i, ɪ or ə.
EITHER = {"en": [("ə", v) for v in "ɪʊ-eæɑɒʌɔ"] + [("i", "ɪ"), ("i", "I"), ("u", "ʊ"), ("u", "U"), ("n", "ŋ")],
          "en-US": [("ɑ", "ɔ")],
          "de": [("i", "j"), ("ɐ", "r"), ("ə", "-")],
          # French: the e that may be said or not ("fuselage", "prendre"), and a close vowel before another vowel,
          # written as a vowel or as its glide ("mutualisme" with /y.a/ or /ɥa/)
          "fr": [("ə", "-"), ("y", "ɥ"), ("u", "w"), ("i", "j")],
          "it": [("s", "z")],  # s between vowels, voiced in the north and in most dictionaries, voiceless in Tuscany
          "ru": [("i", "ɪ"), ("ɪ", "e"), ("ə", "e")]}
LENGTH = frozenset(("ː", "-"))
EITHER = {lang: {frozenset(pair) for pair in pairs} for lang, pairs in EITHER.items()}
FIRST_VOWEL = re.compile("[aeiouyɑɐɒæɛɜəɪʏʊøœɔʌ][\u0300-\u036f]*[aeiouyɑɐɒæɛɜəɪʏʊøœɔʌ]?[\u0300-\u036f]*ː?")
NO_WORD_STRESS = {"fr"}  # French stresses the end of a phrase, not a syllable of the word: the mark our list asks for says nothing
SOURCE_NAMES = {"cmudict": "CMUdict", "britfone": "Britfone", "openrussian": "OpenRussian", "native": "the language's own Wiktionary"}
OPTIONAL = re.compile(r"\(([^()]*)\)|⁽([^⁽⁾]*)⁾")
# Where a sound that differs is, more often than not, another accepted way of saying the word and not a fault,
# by a hand count of sixty such words: English, whose dictionaries give two or three ways for one word in ten
# and each source a different two, and German, where what is left is a short i or u written ɪ or ʊ. Their
# sounds are compared and counted, and the entries are not listed as worth reading for a sound alone. Adding
# CMUdict and Britfone to Wiktionary did not change that for English: of thirty sounds no source had, one was
# a fault (fathom with ɚ) and three the weak i of "happy" written long.
ACCEPTED_VARIANTS = {"en", "de"}
DROPPED = set("/[].ˈˌ ͜͡-")


def facts(lang):
    """word -> (genders, [(regions, transcription, aside)], plurals, sources), or None when no table has been built.
    aside is true of a transcription a source sets apart from the standard: a dialect's, a weak form, a dated one.
    sources names every table that has the word: wiktionary/<lang>.facts.tsv is Wiktionary, and each
    wiktionary/<lang>.<name>.facts.tsv another dictionary (open_sources.py), whose facts are added to its."""
    paths = [HERE / "wiktionary" / f"{lang}.facts.tsv"] + sorted((HERE / "wiktionary").glob(f"{lang}.*.facts.tsv"))
    table = {}
    for path in [p for p in paths if p.exists()]:
        source = SOURCE_NAMES.get(path.name.split(".")[1], "another source") if path.name.count(".") > 2 else "the English Wiktionary"
        with path.open(encoding="utf-8") as f:
            for line in f:
                row = line.rstrip("\n").split("\t")
                if len(row) != 4:
                    continue
                word, genders, sounds, plurals = row
                have = table.setdefault(word, (set(), [], set(), set()))
                for sound in filter(None, sounds.split("|")):
                    region, _, ipa = sound.partition(":")
                    if not ipa.strip("/[]").endswith("-") and not ipa.strip("/[]").startswith("-"):  # /ɹə-/ is a piece of a word
                        have[1].append((set(filter(None, region.lstrip("?").split("+"))), ipa, region.startswith("?")))
                have[0].update(filter(None, genders.split(",")))
                have[2].update(filter(None, plurals.split(",")))
                have[3].add(source)
    return table or None


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


def readings_of(ipa):
    """Every way a transcription can be read: "a ~ b" and "/a/ [b]" are two, and a sound in brackets may be
    said or not (/ˈkʌl.ə(ɹ)/ is /ˈkʌl.ə/ and /ˈkʌl.əɹ/). At most sixteen to a transcription."""
    found = []
    for text in re.split(r"~|(?<=[/\]])\s+(?=[/\[])", unicodedata.normalize("NFC", ipa)):
        texts = [text.strip().strip("/[]")]
        while texts and OPTIONAL.search(texts[0]) and len(texts) < 16:
            texts = [OPTIONAL.sub(lambda m: (m.group(1) or m.group(2) or "") if keep else "", t, count=1) for t in texts for keep in (True, False)]
        found += [OPTIONAL.sub("", t) for t in texts]
    return found


def long_under_stress(text):
    """German: the length mark kept only on the vowel that carries the main stress. Outside it a long vowel
    is written with the mark or without (ours /po.li.ˈtiːk/, Wiktionary's /ˌhomøopaˈtiː/ and /ˈʔaʊ̯toːɐ̯/)."""
    if STRESS not in text:
        return text
    before, _, after = text.partition(STRESS)
    vowel = FIRST_VOWEL.search(after)
    kept = after[:vowel.end()] + after[vowel.end():].replace("ː", "") if vowel else after.replace("ː", "")
    return before.replace("ː", "") + STRESS + kept


def one_spelling(lang, text, variety=""):
    text = SYLLABLE_OF_ITS_OWN.sub(lambda m: "ə" + ASSIMILATED.get(lang, {}).get(m.group(1), m.group(1)), unicodedata.normalize("NFC", text))
    if lang == "de":
        text = long_under_stress(text)
    # taken apart first, so that a mark is dropped also where it comes inside one character (ĩ in the Wikcionario's [ˈmĩ.na])
    text = unicodedata.normalize("NFC", "".join(ch for ch in unicodedata.normalize("NFD", text) if ch not in DROPPED and ch not in FINER[lang]))
    for table in (SAME_SOUND[""], SAME_SOUND.get(variety, {}), SAME_SOUND[lang]):
        for old in sorted(table, key=len, reverse=True):
            text = text.replace(old, table[old])
    for pattern, new in BY_PLACE.get(lang, ()):
        text = unicodedata.normalize("NFC", pattern.sub(new, unicodedata.normalize("NFD", text)))
    return text


def sounds(lang, ipa, variety=""):
    """A transcription as its sounds alone, in one spelling: no slashes, dots, stress marks or tie bars. A set,
    because a transcription with an optional sound is two."""
    return {one_spelling(lang, text, variety) for text in readings_of(ipa)}


def of_place(variety, heard):
    """(the standard transcriptions of a word for a variety of ours, the others that can confirm one of ours,
    whether the standard ones are named for the variety's place). Standard: those named for the place, else
    those named for no place at all; the ones between slashes when there are any. The others: what Wiktionary
    sets apart from the standard, its narrow transcriptions in square brackets where it has broad ones, and
    beside transcriptions named for the place, those named for none.
    Spanish is named for no place, so Spain is told from Mexico by the sound itself: /θ/ where the word has one."""
    usual = [(regions, ipa) for regions, ipa, aside in heard if not aside]
    company = REGIONS.get(variety, set()) | COMPANY.get(variety[:2], set())
    here = [ipa for regions, ipa in usual if regions & REGIONS.get(variety, set()) and regions <= company]
    named = bool(here)
    unsaid = [ipa for regions, ipa in usual if not regions]
    here = here or unsaid
    narrow = []
    if any(ipa.startswith("/") for ipa in here):
        here, narrow = [ipa for ipa in here if ipa.startswith("/")], [ipa for ipa in here if not ipa.startswith("/")]
    if variety.startswith("es-") and any("θ" in ipa for ipa in here) and any("θ" not in ipa for ipa in here):
        here = [ipa for ipa in here if ("θ" in ipa) == (variety == "es-ES")]
    return here, [ipa for _, ipa, aside in heard if aside] + (unsaid if named else []) + narrow, named


def differences(lang, ours, theirs, variety="", lengths=True):
    """The sounds in which a word as we wrote it differs from the nearest of Wiktionary's, as (ours, theirs)
    pairs, a pair of EITHER left out: empty when the two are the same word to a dictionary. Without lengths,
    a length mark on one side only is no difference."""
    either = EITHER.get(lang, set()) | EITHER.get(variety, set()) | (set() if lengths else {LENGTH})
    best = None
    for mine in ours:
        for other in theirs:
            pairs = []
            for tag, a, b, c, d in difflib.SequenceMatcher(None, mine, other, autojunk=False).get_opcodes():
                if tag == "equal":
                    continue
                left, right = mine[a:b], other[c:d]
                if len(left) == len(right):  # sound for sound, so that one fault among two habits is still seen
                    pairs += [(x, y) for x, y in zip(left, right) if frozenset((x, y)) not in either]
                elif not (left and right) and all(frozenset((x, "-")) in either for x in left + right):
                    continue
                else:
                    pairs.append((left or "-", right or "-"))
            if best is None or len(pairs) < len(best):
                best = pairs
    return best or []


def plurals_of(lang, lexeme):
    """The plural a lexeme states among its forms: the nominative one where the language has cases."""
    found = set()
    for form in lexeme.get("forms") or []:
        features = form.get("features") or {}
        if features.get("Number") == "Plur" and features.get("Case", "Nom") == "Nom" and set(features) <= {"Number", "Case"}:
            found.add(plain(form.get("form") or ""))
    return found


def findings(lang, entry, table):
    """Every fact of one written entry that a source states too, each as a dict: fact, verdict ("agrees" or
    "differs"), detail, sources (how many dictionaries have the word at all), and where it is: lexeme (its place
    in the entry), variety and was (the value the entry has: a gender, the plurals, a transcription)."""
    found = []
    for at, lexeme in enumerate(entry.get("lexemes") or []):
        lemma = lexeme.get("lemma") or ""
        known = table.get(lemma) or table.get(plain(lemma))
        if not known or " " in lemma:
            continue
        genders, heard, plurals, sources = known
        whose = " and ".join(sorted(sources))
        gender = (lexeme.get("grammar") or {}).get("gender")
        if lexeme.get("pos") == "NOUN" and gender and genders and gender != "Com":
            found.append({"fact": "gender", "verdict": "agrees" if gender in genders else "differs", "detail": f"{lemma}: {gender}, {whose}: {', '.join(sorted(genders))}",
                          "sources": len(sources), "lexeme": at, "was": gender})
        if (ours := plurals_of(lang, lexeme)) and plurals and lexeme.get("pos") == "NOUN":
            found.append({"fact": "plural", "verdict": "agrees" if ours & plurals else "differs", "detail": f"{lemma}: {', '.join(sorted(ours))}, {whose}: {', '.join(sorted(plurals))}",
                          "sources": len(sources), "lexeme": at, "was": sorted(ours)[0]})
        # Two transcriptions of one spelling in one variety are two ways of saying the word ("unmöglich" stressed on
        # either syllable, "either"). Wiktionary seldom has both, so the pair is right when one of them is.
        ways = {}
        for p in lexeme.get("pronunciations") or []:
            ipa, variety = p.get("ipa") or "", p.get("variety") or ""
            if not ipa:
                continue
            # a transcription of another spelling of the lexeme ("изо" beside "из", "чтоб" beside "чтобы") is held
            # against Wiktionary's entry for that spelling, and against nothing when it has none
            spelt = p.get("spelling") or lemma
            if plain(spelt) != plain(lemma):
                other = table.get(spelt) or table.get(plain(spelt))
                if not other:
                    continue
                heard, whose_way = other[1], " and ".join(sorted(other[3]))
            else:
                heard, whose_way = known[1], whose
            stressed, sounded = ways.setdefault((variety, plain(spelt)), ([], []))
            anywhere = [t for _, t, _ in heard]
            place = stress_place(ipa)
            theirs = [t for t in (stress_place(i) for i in anywhere) if t]
            if place and theirs and sum(place) > 1 and lang not in NO_WORD_STRESS:
                stressed.append((any(same_stress(place, t) for t in theirs), f"{lemma} {variety}: {ipa}, {whose_way}: {' '.join(anywhere[:4])}", variety, ipa))
            here, others, named = of_place(variety, heard)
            if here:
                # where the stress is on another syllable, the length that goes with it is not counted a second time
                lengths = not (place and theirs and not any(same_stress(place, t) for t in theirs))
                mine = sounds(lang, ipa, variety)
                pairs = differences(lang, mine, {text for t in here for text in sounds(lang, t, variety)}, variety, lengths)
                if pairs and others and not differences(lang, mine, {text for t in others for text in sounds(lang, t, variety)}, variety, lengths):
                    pairs = []  # a way of saying it that Wiktionary records, if not as the standard of the place
                said = " ".join(f"{a}>{b}" for a, b in pairs)
                sounded.append((not pairs, named, f"{lemma} {variety}: {ipa}, {whose_way}: {' '.join(here[:4])}  [{said}]", variety, ipa))
        heard_here = []
        for stressed, sounded in ways.values():
            if stressed:
                same, detail, variety, ipa = max(stressed, key=lambda way: way[0])  # the way that agrees, if one does
                found.append({"fact": "stressed syllable", "verdict": "agrees" if same else "differs", "detail": detail, "sources": len(sources),
                              "lexeme": at, "variety": variety, "was": ipa})
            if sounded:
                same, named, detail, variety, ipa = max(sounded, key=lambda way: way[0])
                heard_here.append((named, same, detail, variety, ipa))
        # a transcription named for no place, in a language where the place matters, can confirm and cannot contradict:
        # it is held against a variety of ours only when Wiktionary confirms none of the lexeme's transcriptions
        confirmed = any(way[1] for way in heard_here)
        for named, same, detail, variety, ipa in heard_here:
            if same or named or lang not in PLACE_UNSAID or not confirmed:
                found.append({"fact": "sounds", "verdict": "agrees" if same else "differs", "detail": detail, "sources": len(sources),
                              "lexeme": at, "variety": variety, "was": ipa})
    return found


def compare(lang, entry, table):
    """(fact, verdict, detail, sources) for every fact of one written entry that a source states too."""
    return [(f["fact"], f["verdict"], f["detail"], f["sources"]) for f in findings(lang, entry, table)]


def survey(lang, show, leads):
    """Prints one language's count and adds every disagreement to leads; returns the counts."""
    table = facts(lang)
    if table is None:
        print(f"{lang}: no facts table; run lemma_index.py --langs {lang}")
        return None
    counts, differing, pairs = defaultdict(Counter), defaultdict(list), Counter()
    raw, kept = ingest_v4.load_raw(lang), ingest_v4.kept(lang)
    for headword in sorted(raw):
        written, meta = raw[headword]
        if not written.get("lexemes"):
            continue
        entry = ingest_v4.clean_entry(lang, json.loads(json.dumps(written)), meta)  # as it is published, the free rules applied
        for finding in findings(lang, entry, table):
            fact, verdict, detail, sources = finding["fact"], finding["verdict"], finding["detail"], finding["sources"]
            counts[fact][verdict] += 1
            if verdict == "differs":
                differing[fact].append(detail)
                # a second look (settle_v4.py) that left the fact as written closes it where one source alone differed
                looked = any(k[1] == finding["lexeme"] and k[3] == finding["was"] and k[2] == finding.get("variety")
                             and (k[0] == fact or {k[0], fact} <= {"sounds", "stressed syllable"}) for k in kept.get(headword, ()))
                leads.append({"language": lang, "headword": headword, "fact": fact, "detail": detail, "sources": sources, "kept": looked})
                if fact == "sounds":
                    pairs.update(detail.rpartition("[")[2].rstrip("]").split())
    mine = [lead for lead in leads if lead["language"] == lang]
    trusted = {lead["headword"] for lead in mine if (lead["fact"] != "sounds" or lang not in ACCEPTED_VARIANTS) and not (lead["kept"] and lead["sources"] < 2)}
    print(f"\n{lang}: {len(raw)} entries held against {' and '.join(sorted({n for row in table.values() for n in row[3]}))}, "
          f"{len(trusted)} with a disagreement worth reading")
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
