"""Hard cases: words chosen because an entry for them is easy to get wrong, each with what a right entry must show.

A random sample measures the ordinary word, and one reader's count of it moves by a third from one reading to the
next. This set measures a writer where writers differ, and most of it without a reader: a homograph has two
lexemes that are said differently, an inflected form is refused, a noun with two plurals has both, a sense used in
one country says so. The words were chosen before any writer saw them, by kind:

  homograph   two words under one spelling, told apart by sound, stress or gender (lead, Band, ancora, замок)
  form        an inflected form that is no lemma, or is the lemma of something else only (went, left, été, стали)
  noun        a gender or a plural a learner gets wrong (E-Mail, uovo, agua, год)
  verb        impersonal, pronominal, two auxiliaries (falloir, monter, gustar, bisognare)
  region      a sense of one country (pants, septante, coger, fato)
  register    informal, vulgar or offensive, to be labelled as such (bloody, con, figo, блин)
  sound       a word said in more than one way, with when (the, plus, used)
  particle    a small word of many uses, with no key: only a reader can judge it (doch, soler, ficar, нельзя)

  hard_cases.py --lists                         writes headwords/hard-<lang>.json for the writers
  hard_cases.py --score FOLDER [FOLDER ...]     each writer's folder under out/ held against the key
  hard_cases.py --pairs FOLDER_A FOLDER_B       review files in which a reader sees each word's two entries side by
                                                side, not told whose is whose: measure/hard/review/<lang>-1.md
  hard_cases.py --tally                         the reader's choices, from measure/hard/answers/

Run from the backend repository's root.
"""
import argparse
import json
import random
import re
import sys
import unicodedata
from collections import Counter
from pathlib import Path

HERE = Path("scripts/lexicon_research")
sys.path.insert(0, str(HERE))
import ingest_v4  # noqa: E402
import measure  # noqa: E402
from prompting import LANG_NAME  # noqa: E402

OUT = HERE / "out"
NAME = "hard"
REFUSED = ("reject",)
ANY_FORMAL = ("label", "informal", "slang")
ROUGH = ("label", "vulgar", "offensive", "derogatory")

# word: (kind, what a right entry shows). A check is (name, values...); "any" holds when one of its checks does.
HARD = {
    "en": {
        "lead": ("homograph", [("lexemes_min", 2), ("ipa", "liːd", "l[eɛ]d")]),
        "tear": ("homograph", [("lexemes_min", 2), ("ipa", "tɪ[ərɹ]", "t[eɛ][ərɹ]")]),
        "wind": ("homograph", [("ipa", "wɪnd", "waɪnd")]),
        "left": ("form", [("pos", "ADJ"), ("no_pos", "VERB")]),
        "went": ("form", [REFUSED]),
        "NASA": ("form", [REFUSED]),
        "news": ("noun", [("grammar", "countability", "Mass")]),
        "pants": ("region", [("regions", "en-US", "en-GB")]),
        "biscuit": ("region", [("regions", "en-GB", "en-US")]),
        "bloody": ("register", [("label", "informal", "slang", "vulgar", "offensive"), ("regions", "en-GB")]),
        "the": ("sound", [("weak",)]),
        "used": ("sound", [("ipa", "juːst")]),
    },
    "de": {
        "Band": ("homograph", [("lexemes_min", 3), ("genders", "Masc", "Neut", "Fem")]),
        "See": ("homograph", [("genders", "Masc", "Fem")]),
        "umfahren": ("homograph", [("lexemes_min", 2), ("stress", 2), ("grammar", "separable", True), ("grammar", "separable", False)]),
        "modern": ("homograph", [("pos", "ADJ", "VERB"), ("stress", 2)]),
        "gehört": ("form", [REFUSED]),
        "Wort": ("noun", [("plural", "Wörter", "Worte")]),
        "Bank": ("noun", [("plural", "Bänke", "Banken")]),
        "E-Mail": ("noun", [("genders", "Fem", "Neut")]),
        "Joghurt": ("noun", [("genders", "Masc", "Neut")]),
        "erinnern": ("verb", [("any", ("grammar", "valency", "Refl"), ("grammar", "reflexive", True))]),
        "geil": ("register", [ANY_FORMAL]),
        "doch": ("particle", []),
    },
    "fr": {
        "tour": ("homograph", [("genders", "Masc", "Fem")]),
        "fils": ("form", [("ipa", "fis"), ("lexemes_max", 1)]),
        "couvent": ("form", [("pos", "NOUN"), ("no_pos", "VERB")]),
        "est": ("form", [("ipa", "ɛst"), ("no_pos", "VERB", "AUX")]),
        "été": ("form", [("pos", "NOUN"), ("no_pos", "VERB", "AUX")]),
        "œil": ("noun", [("plural", "yeux")]),
        "gens": ("noun", [("grammar", "number", "Plur")]),
        "falloir": ("verb", [("grammar", "valency", "Impers")]),
        "monter": ("verb", [("any", ("grammar", "auxiliary", "both"), ("all", ("grammar", "auxiliary", "be"), ("grammar", "auxiliary", "have")))]),
        "septante": ("region", [("regions", "fr-BE", "fr-CH")]),
        "con": ("register", [ROUGH]),
        "plus": ("sound", [("ipa", "ply(?!s)", "plys"), ("context",)]),
    },
    "es": {
        "capital": ("homograph", [("genders", "Masc", "Fem")]),
        "cura": ("homograph", [("genders", "Masc", "Fem")]),
        "fui": ("form", [REFUSED]),
        "vino": ("form", [("pos", "NOUN"), ("no_pos", "VERB")]),
        "agua": ("noun", [("genders_only", "Fem")]),
        "problema": ("noun", [("genders_only", "Masc")]),
        "gustar": ("verb", [("grammar", "valency", "Intr")]),
        "coger": ("region", [("regions_any", "es-MX", "es-AR", "es-419", "es-CO", "es-UY", "es-005"), ROUGH]),
        "ordenador": ("region", [("regions", "es-ES")]),
        "vos": ("region", [("regions_any", "es-AR", "es-419", "es-UY", "es-005", "es-013")]),
        "tío": ("register", [ANY_FORMAL, ("regions", "es-ES")]),
        "soler": ("particle", []),
    },
    "it": {
        "ancora": ("homograph", [("pos", "NOUN", "ADV"), ("stress", 2)]),
        "pesca": ("homograph", [("lexemes_min", 2), ("ipa", "pes", "pɛs")]),
        "principi": ("form", [REFUSED]),
        "stato": ("form", [("pos", "NOUN"), ("no_pos", "VERB", "AUX")]),
        "porto": ("form", [("pos", "NOUN"), ("no_pos", "VERB")]),
        "uovo": ("noun", [("plural", "uova")]),
        "braccio": ("noun", [("plural", "bracci", "braccia")]),
        "problema": ("noun", [("genders_only", "Masc"), ("plural", "problemi")]),
        "piacere": ("verb", [("pos", "VERB", "NOUN"), ("grammar", "auxiliary", "be")]),
        "bisognare": ("verb", [("grammar", "valency", "Impers")]),
        "figo": ("register", [ANY_FORMAL]),
        "casino": ("register", [ANY_FORMAL]),
    },
    "pt": {
        "sede": ("homograph", [("lexemes_min", 2), ("ipa", "sɛd", "sed")]),
        "colher": ("homograph", [("pos", "NOUN", "VERB"), ("ipa", "ʎɛ", "ʎe")]),
        "foi": ("form", [REFUSED]),
        "avô": ("noun", [("genders_only", "Masc"), ("any", ("plural", "avós"), ("plural", "avôs"))]),
        "cidadão": ("noun", [("plural", "cidadãos")]),
        "óculos": ("noun", [("grammar", "number", "Plur")]),
        "haver": ("verb", [("grammar", "valency", "Impers")]),
        "meio": ("particle", [("pos_min", 3)]),
        "rapariga": ("region", [("regions", "pt-PT"), ROUGH]),
        "ônibus": ("region", [("regions", "pt-BR"), ("any", ("grammar", "invariable", True), ("plural", "ônibus"))]),
        "fato": ("region", [("regions", "pt-PT", "pt-BR")]),
        "ficar": ("particle", []),
    },
    "ru": {
        "замок": ("homograph", [("lexemes_min", 2), ("stress", 2)]),
        "мука": ("homograph", [("lexemes_min", 2), ("stress", 2)]),
        "стоит": ("form", [REFUSED]),
        "стали": ("form", [REFUSED]),
        "печь": ("form", [("pos", "NOUN", "VERB"), ("genders", "Fem")]),
        "три": ("form", [("pos", "NUM"), ("no_pos", "VERB")]),
        "кофе": ("noun", [("genders", "Masc")]),
        "человек": ("noun", [("plural", "люди")]),
        "год": ("noun", [("form", "лет")]),
        "нравиться": ("verb", [("grammar", "aspect", "Imp")]),
        "блин": ("register", [("pos", "NOUN"), ("any", ("pos", "INTJ"), ("label", "informal", "slang", "euphemistic"))]),
        "нельзя": ("particle", []),
    },
}


# A sense marked for a UN area says as much as one marked for the country the key names: Northern America for the
# United States, South or Latin America for Brazil.
COVERS = {"en-US": ("en-021", "en-019"), "pt-BR": ("pt-005", "pt-419", "pt-019"), "pt-PT": ("pt-150", "pt-039")}


def plain(text):
    """A spelling without the marks a writer adds to show stress."""
    return unicodedata.normalize("NFC", "".join(ch for ch in unicodedata.normalize("NFD", text or "") if ch not in "́̀"))


def grammars(entry):
    for lexeme in entry.get("lexemes") or []:
        for holder in [lexeme] + list(lexeme.get("senses") or []):
            grammar = holder.get("grammar") or {}
            yield grammar
            yield from (grammar.get("by_variety") or {}).values()


def stressed_syllable(lexeme):
    """Which syllable of the lexeme's first transcription carries the stress, counted from 1; None when unmarked."""
    for said in lexeme.get("pronunciations") or []:
        parts = (said.get("ipa") or "").strip("/").split(".")
        marked = [n for n, part in enumerate(parts, 1) if "ˈ" in part]
        if marked:
            return marked[0]
    return None


def holds(entry, check):
    kind, *values = check
    lexemes = entry.get("lexemes") or []
    if kind == "any":
        return any(holds(entry, c) for c in values)
    if kind == "all":
        return all(holds(entry, c) for c in values)
    if kind == "reject":
        return not lexemes and bool(entry.get("reject"))
    if kind == "pos":
        return all(any(lx.get("pos") == pos for lx in lexemes) for pos in values)
    if kind == "no_pos":
        return bool(lexemes) and not any(lx.get("pos") in values for lx in lexemes)
    if kind == "pos_min":
        return len({lx.get("pos") for lx in lexemes}) >= values[0]
    if kind == "lexemes_min":
        return len(lexemes) >= values[0]
    if kind == "lexemes_max":
        return 0 < len(lexemes) <= values[0]
    if kind in ("genders", "genders_only"):
        found = {g.get("gender") for g in grammars(entry) if g.get("gender")}
        return found == set(values) if kind == "genders_only" else set(values) <= found
    if kind == "grammar":
        key, *wanted = values
        return any(g.get(key) in wanted for g in grammars(entry) if key in g)
    if kind in ("plural", "form"):
        forms = [(plain(f.get("form")), (f.get("features") or {}).get("Number") or "") for lx in lexemes for f in lx.get("forms") or []]
        return all(any(form == value and (kind == "form" or "Plur" in number) for form, number in forms) for value in values)
    if kind == "ipa":
        said = [re.sub(r"[.ˈˌ]", "", p.get("ipa") or "") for lx in lexemes for p in lx.get("pronunciations") or []]
        return all(any(re.search(pattern, ipa) for ipa in said) for pattern in values)
    if kind == "stress":
        return len({stressed_syllable(lx) for lx in lexemes} - {None}) >= values[0]
    if kind in ("regions", "regions_any"):
        found = {r if isinstance(r, str) else r.get("variety") for lx in lexemes for s in lx.get("senses") or [] for r in s.get("regions") or []}
        return (any if kind == "regions_any" else all)(bool(found & {value, *COVERS.get(value, ())}) for value in values)
    if kind == "label":
        found = {v for lx in lexemes for s in lx.get("senses") or [] for value in (s.get("labels") or {}).values() for v in (value if isinstance(value, list) else [value])}
        return bool(found & set(values))
    if kind == "weak":
        return any(p.get("context") and "ˈ" not in (p.get("ipa") or "ˈ") for lx in lexemes for p in lx.get("pronunciations") or [])
    if kind == "context":
        return any(p.get("context") for lx in lexemes for p in lx.get("pronunciations") or [])
    raise ValueError(f"unknown check {kind!r}")


def told(check):
    kind, *values = check
    if kind in ("any", "all"):
        return (" or " if kind == "any" else " and ").join(told(c) for c in values)
    return {"reject": "refused as no lemma"}.get(kind, f"{kind} {', '.join(str(v) for v in values)}")


def written(folder, lang):
    """headword -> (entry, meta) of a writer's folder."""
    return measure.folder_entries(folder, lang)


def lists():
    from wordfreq import zipf_frequency
    for lang, words in HARD.items():
        rows = [{"headword": word, "zipf": round(zipf_frequency(word, lang), 2), "set": NAME} for word in words]
        (HERE / "headwords" / f"{NAME}-{lang}.json").write_text(json.dumps(rows, ensure_ascii=False, indent=1) + "\n", encoding="utf-8")
        print(f"{lang}: {len(rows)} words")


def score(folders):
    """Each folder against the key: checks that hold, words wholly right, by kind; and the words the folders differ on."""
    right, table = {}, {}
    for folder in folders:
        checks, words, kinds, cost, missing = Counter(), Counter(), Counter(), 0.0, []
        for lang, hard in HARD.items():
            entries = written(folder, lang)
            for word, (kind, expected) in hard.items():
                if word not in entries:
                    missing.append(f"{lang} {word}")
                    continue
                entry, meta = entries[word]
                cost += meta.get("total_cost_usd") or 0
                if not expected:
                    continue
                passed = [holds(entry, check) for check in expected]
                checks["held"] += sum(passed)
                checks["all"] += len(passed)
                words["right"] += all(passed)
                words["all"] += 1
                kinds[kind, "right"] += all(passed)
                kinds[kind, "all"] += 1
                right[folder, lang, word] = all(passed)
                table[folder, lang, word] = "; ".join(told(check) for check, ok in zip(expected, passed) if not ok)
        print(f"\n{folder}: {words['right']} of {words['all']} words wholly right, {checks['held']} of {checks['all']} checks, ${cost:.2f}"
              + (f"; not written: {', '.join(missing)}" if missing else ""))
        print("  " + ", ".join(f"{kind} {kinds[kind, 'right']}/{kinds[kind, 'all']}" for kind in sorted({k for k, _ in kinds})))
    print("\nwhere a writer missed the key:")
    for lang, hard in HARD.items():
        for word, (kind, _) in hard.items():
            missed = [(folder, table[folder, lang, word]) for folder in folders if table.get((folder, lang, word))]
            if missed:
                print(f"  {lang} {word} ({kind}): " + " | ".join(f"{folder.removeprefix('review-hard-')}: {what}" for folder, what in missed))
    return right


PAIRS = """# {name}: two entries for each word ({count} words)

Each word below has two entries of a learner's dictionary of {name}, A and B, written by two different writers
under the same rules. You are not told which writer wrote which, and the sides change from word to word. Some of
these words are hard on purpose: two words under one spelling, forms that are not words of their own, senses of
one country only, rude words.

For each word say which entry serves a learner better, and name the faults of each. A writer may have refused a
word as no dictionary word of its own ("refused: inflected form of ..."); that is right for an inflected form, an
abbreviation or a name, and wrong for a word a dictionary of {name} lists.

## The faults

{faults}

These are this dictionary's own definitions, not faults:

{definitions}
## The answer

One line of JSON for every word, in the order given, inside a single ```json block:

```json
{{"id": 1, "better": "A", "why": "B lacks the metal, the commoner of the two words spelt lead", "A": [], "B": [{{"fault": "missing-core", "where": "lexemes", "what": "the metal is missing"}}]}}
```

"better" is "A", "B" or "same": "same" only when you could not say which you would rather give a learner. "why" is
one sentence. "A" and "B" list each entry's faults as above, empty when it has none.

## The words

{entries}
"""


def shown(lang, pair):
    entry, meta = pair
    if not entry.get("lexemes"):
        return f"refused: {entry.get('reject') or 'no reason given'}"
    return measure.show(ingest_v4.clean_entry(lang, json.loads(json.dumps(entry)), meta, review=False))


def pairs(folder_a, folder_b, seed="2026-11-20"):
    """The review files and, beside them, which side is whose. Two refusals of one word are not shown: nothing to read."""
    home = measure.MEASURE / NAME
    (home / "review").mkdir(parents=True, exist_ok=True)
    for lang, hard in HARD.items():
        one, other = written(folder_a, lang), written(folder_b, lang)
        blocks, sides = [], []
        for word in hard:
            if word not in one or word not in other:
                continue
            if not one[word][0].get("lexemes") and not other[word][0].get("lexemes"):
                continue
            a_first = random.Random(f"{seed}-{lang}-{word}").random() < 0.5
            first, second = (one, other) if a_first else (other, one)
            sides.append({"id": len(sides) + 1, "headword": word, "kind": hard[word][0], "A": folder_a if a_first else folder_b, "B": folder_b if a_first else folder_a})
            blocks.append(f"### {len(sides)}. {word}\n\n#### Entry A\n\n{shown(lang, first[word])}\n\n#### Entry B\n\n{shown(lang, second[word])}")
        text = PAIRS.format(name=LANG_NAME[lang], count=len(blocks), entries="\n\n".join(blocks),
                            faults=measure.REVIEW.partition("## The faults\n\n")[2].partition("\nDo not report")[0].strip("\n").replace("{name}", LANG_NAME[lang]),
                            definitions=measure.REVIEW.partition("not faults:\n\n")[2].partition("{ours}")[0].replace("{name}", LANG_NAME[lang]))
        (home / "review" / f"{lang}-1.md").write_text(text, encoding="utf-8")
        (home / f"pairs-{lang}.json").write_text(json.dumps(sides, ensure_ascii=False, indent=1) + "\n", encoding="utf-8")
        print(f"{lang}: {len(blocks)} words in two entries each, {len(text):,} characters")


def tally():
    """The reader's choices by writer, by kind of word, and the faults it named in each writer's entries."""
    home = measure.MEASURE / NAME
    wins, by_kind, by_lang, shown_first, faults, read = Counter(), Counter(), Counter(), Counter(), Counter(), 0
    for lang in HARD:
        if not (home / f"pairs-{lang}.json").exists():
            continue
        sides = {s["id"]: s for s in json.loads((home / f"pairs-{lang}.json").read_text(encoding="utf-8"))}
        for path in sorted((home / "answers").glob(f"{lang}-*.md")):
            for line in path.read_text(encoding="utf-8").splitlines():
                line = line.strip().rstrip(",")
                if not (line.startswith("{") and '"id"' in line):
                    continue
                try:
                    row = json.loads(line)
                except json.JSONDecodeError:
                    continue
                side = sides.get(row.get("id"))
                if not side:
                    continue
                read += 1
                winner = side.get(row.get("better")) or "same"
                wins[winner] += 1
                by_kind[side["kind"], winner] += 1
                by_lang[lang, winner] += 1
                if winner != "same":  # a reader leans to the entry it reads first: counted, so that it can be taken out
                    shown_first[side["A"], winner] += 1
                for letter in "AB":
                    for finding in row.get(letter) or []:
                        if finding.get("fault") in measure.FAULTS:
                            faults[side[letter], finding["fault"]] += 1
    writers = sorted({w for w in wins if w != "same"} | {w for w, _ in faults})
    print(f"{read} words read: " + ", ".join(f"{w.removeprefix('review-hard-')} better in {wins[w]}" for w in writers) + f", the same in {wins['same']}")
    for kind in sorted({k for k, _ in by_kind}):
        print(f"  {kind:10} " + ", ".join(f"{w.removeprefix('review-hard-')} {by_kind[kind, w]}" for w in writers) + f", same {by_kind[kind, 'same']}")
    print("  by language: " + "; ".join(f"{lang} " + ", ".join(f"{w.removeprefix('review-hard-')} {by_lang[lang, w]}" for w in writers) for lang in HARD if any(by_lang[lang, w] for w in writers)))
    if len(writers) == 2:
        one, other = writers
        rates = [shown_first[first, one] / (shown_first[first, one] + shown_first[first, other]) for first in writers if shown_first[first, one] + shown_first[first, other]]
        for first in writers:
            print(f"  {first.removeprefix('review-hard-')} shown first: " + ", ".join(f"{w.removeprefix('review-hard-')} better in {shown_first[first, w]}" for w in writers))
        if len(rates) == 2:
            print(f"  with the two orders weighted alike, {one.removeprefix('review-hard-')} is the better in {100 * sum(rates) / 2:.0f}% of the words decided")
    for w in writers:
        print(f"faults named in {w.removeprefix('review-hard-')}: " + ", ".join(f"{fault} {faults[w, fault]}" for fault in measure.FAULTS if faults[w, fault]))


if __name__ == "__main__":
    ap = argparse.ArgumentParser()
    ap.add_argument("--lists", action="store_true")
    ap.add_argument("--score", nargs="+")
    ap.add_argument("--pairs", nargs=2)
    ap.add_argument("--tally", action="store_true")
    a = ap.parse_args()
    if a.lists:
        lists()
    if a.score:
        score(a.score)
    if a.pairs:
        pairs(*a.pairs)
    if a.tally:
        tally()
