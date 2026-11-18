"""Measure the entries: a sample drawn at random, read against a fixed list of faults, counted.

A review that corrects entries always finds something to correct, so it cannot say when the entries
are good enough. This asks a narrower question of a reviewer of another model family: for each
entry of a random sample, which of six kinds of fault does it have? The answer is a rate, and the
same question can be asked again after any change.

The sample of a language has two parts, mixed so that the reviewer cannot tell them apart:
  have   entries the dictionary already holds (any v4 writer), as ingest would publish them, drawn
         evenly from four bands of frequency above Zipf 3.5: what a reader meets most
  new    words with no entry yet, written with the prompt of the day for this measurement and kept
         as entries; they come from the rarer bands, because every commoner word is written or
         waits for the mender

Steps (run from the backend repository root):
  measure.py --name v46 --sample             draw both parts; writes headwords/measure-<name>-<lang>.json
                                             and measure/<name>/sample-<lang>.json
  claude_code_writer.py --headwords measure-<name> ...     write the new words
  measure.py --name v46 --review             measure/<name>/review/<lang>-<n>.md (--per-file 50 for a reviewer that tires)
  measure.py --name v46 --tally              reads measure/<name>/answers/*.md and counts
  measure.py --name ja-top --langs ja --list top-ja --sample --new 0     a count of one list's entries as written

The reviewer answers with one JSON object per entry; REVIEW below says how. Its answers are kept as
they came under measure/<name>/answers/.
"""
import argparse, json, random, re, sys
from collections import Counter, defaultdict
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
import ingest_v4  # noqa: E402
import lemma_gate  # noqa: E402
import v3_entries  # noqa: E402
from prompting import HERE, LANG_NAME, PROMPT_VERSION, TARGET_LANGUAGES  # noqa: E402

MEASURE = HERE / "measure"
OUT = HERE / "out"
BANDS = ((5.0, 9.9), (4.5, 5.0), (4.0, 4.5), (3.5, 4.0))  # the commoner words, most frequent band first
FAULTS = ("fact", "claim", "missing-core", "missing-minor", "split", "wording")
# What is aimed at, written down before the first count: of a hundred entries, at most this many with the fault
BAR = {"fact": 2, "claim": 5, "missing-core": 10}

REVIEW = """# {name}: entries to check ({part} of {parts})

These are entries of a learner's dictionary of {name}, written by a model. Do not correct them and do
not rank them. For each entry say which of the faults below it has, if any. Most entries of a good
dictionary have none: report a fault only when you could show it from a dictionary of {name}.

## The faults

- **fact**: something the entry states is false. A gender, a plural or another form, a part of
  speech, an auxiliary, a transcription that gives the wrong sound (not a matter of notation), a
  definition that gives a meaning the word does not have.
- **claim**: a restriction stated too strongly or wrongly. A region ("only" where the sense is used
  elsewhere too), a position (only before a noun), a number or countability, a label (formal,
  dated, offensive) the sense does not deserve.
- **missing-core**: the entry lacks one of the two or three commonest meanings or parts of speech of
  the word, one a learner at B1 or B2 meets in ordinary text.
- **missing-minor**: it lacks a meaning that a learner's dictionary lists but that is rarer than
  those the entry has. Not technical, dialectal or obsolete meanings: those are left out on purpose.
- **split**: two numbered senses are one meaning, or a mere construction or an idiom is numbered as
  a sense.
- **wording**: a definition is narrower or wider than its sense, an example does not show its
  sense, or an example is not natural {name}.

Do not report: the order of senses, a CEFR level off by one step, how syllables are divided, a
definition you would have worded differently when the given one is true, or anything about style.

These are this dictionary's own definitions, not faults:

- valency Refl stands for every use with a reflexive pronoun, whatever a grammar of {name} calls it
  (reflexive, reciprocal, pronominal); Tr means the verb takes a direct object, so a verb whose
  object needs a preposition or the dative is Intr.
- a grammar value followed by "mainly" holds for most uses, not all ("position Attr, mainly"
  is "usually before a noun"); without it the value is claimed without exception.
- a headword that is also an inflected form of another word (given, ladies), an abbreviation or a
  name has only the lexemes it is the lemma of. The reader reaches the other word by a link that
  is not shown here.
{ours}
## The answer

One line of JSON for every entry, in the order given, inside a single ```json block. An entry
without a fault has an empty list:

```json
{{"id": 1, "findings": []}}
{{"id": 2, "findings": [{{"fault": "claim", "where": "sense 2 regions", "what": "also used in Austria, so not 'only' Germany", "misleads": true}}]}}
```

"where" names the place (lexeme and sense by number, and the field). "what" is one sentence.
"misleads" is true when a learner would come away believing something false or would not find the
meaning in front of them, false when the fault is a blemish.

## The entries

{entries}
"""


# What else is this dictionary's own in one language, said to its reviewer in the same list
OURS = {
    "ja": """- a word is said as a reading in hiragana, not in IPA. "accent" after it is the pitch accent of Tokyo as
  dictionaries number it: the mora after which the pitch drops, 0 when it does not drop. A reading without
  one is not a fault: the writer gives it only when sure. A wrong one is a fact.
- a frame is a pattern with 〜 for an open place and its particle written (〜に行く). A use after the て form
  with a meaning of its own (〜ている, 〜てくる) is numbered as a sense on purpose, and so is a particle's or
  a formal noun's use in grammar (〜ことがある): do not report these as split.
- suru true means the noun makes a verb with する. A noun has no countability. A definition is one
  predicate or one noun phrase of 10 to 35 characters.
""",
}


def rows_of(lang, only=None):
    """headword -> zipf over every list of the language, or over the one list named."""
    best = {}
    for f in sorted((HERE / "headwords").glob(f"*-{lang}.json")):
        if f.name.startswith("measure-") or (only and f.name != f"{only}-{lang}.json"):
            continue
        for row in json.loads(f.read_text(encoding="utf-8")):
            if isinstance(row.get("zipf"), (int, float)):
                best[row["headword"]] = max(best.get(row["headword"], 0), row["zipf"])
    return best


def draw(lang, have, new, seed, only=None):
    """The two parts of a language's sample: [(headword, zipf)] each. The same seed draws the same words.
    With a list named, both parts come from that list alone."""
    rng = random.Random(f"{seed}-{lang}")
    zipf = rows_of(lang, only)
    written = ingest_v4.load_raw(lang)
    whole = sorted(h for h, (entry, _) in written.items() if entry.get("lexemes") and h in zipf)
    part_have = []
    for low, high in BANDS:
        band = [h for h in whole if low <= zipf[h] < high]
        part_have += rng.sample(band, min(len(band), have // len(BANDS)))
    # a band with too few written entries (the commonest words mostly wait for the mender) is made up from the others
    rest = [h for h in whole if zipf[h] >= BANDS[-1][0] and h not in set(part_have)]
    part_have += rng.sample(rest, min(len(rest), have - len(part_have)))
    older, known = set(v3_entries.headwords(lang)), lemma_gate.load(lang)
    taken = {f.stem for d in OUT.glob(f"*-v4/{lang}") for f in d.glob("*.json")}
    free = sorted(h for h in zipf if h not in taken and h not in older and h.isalpha() and len(h) > 2 and h == h.lower()
                  and known.get(h, ("",))[0] == "L")
    free.sort(key=lambda h: -zipf[h])
    pool = free[: max(new * 25, 1000)]  # the commonest of what is free: a rarer word says less about the dictionary
    part_new = rng.sample(pool, min(len(pool), new))
    return [(h, zipf[h]) for h in part_have], [(h, zipf[h]) for h in part_new]


def sample(name, langs, have, new, seed, only=None):
    (MEASURE / name).mkdir(parents=True, exist_ok=True)
    for lang in langs:
        part_have, part_new = draw(lang, have, new, seed, only)
        items = [{"headword": h, "zipf": z, "part": "have"} for h, z in part_have] + [{"headword": h, "zipf": z, "part": "new"} for h, z in part_new]
        random.Random(f"{seed}-{lang}-order").shuffle(items)
        for number, item in enumerate(items, 1):
            item["id"] = number
        (MEASURE / name / f"sample-{lang}.json").write_text(json.dumps(items, ensure_ascii=False, indent=1) + "\n", encoding="utf-8")
        if not part_new:  # --new 0: a count of what is written, nothing to buy
            print(f"{lang}: {len(part_have)} held entries, no words to write")
            continue
        (HERE / "headwords" / f"measure-{name}-{lang}.json").write_text(json.dumps(
            [{"headword": h, "zipf": z, "set": f"measure-{name}"} for h, z in part_new], ensure_ascii=False, indent=1) + "\n", encoding="utf-8")
        print(f"{lang}: {len(part_have)} held entries, {len(part_new)} words to write (Zipf {min(z for _, z in part_new):.2f} to {max(z for _, z in part_new):.2f})")


def stated(grammar):
    """A grammar as a reader takes it in: each value, with "mainly" after the ones that hold for most uses only."""
    mainly = set(grammar.get("mainly") or [])
    return "; ".join(f"{k} {json.dumps(v, ensure_ascii=False) if isinstance(v, (dict, list)) else v}" + (", mainly" if k in mainly else "")
                     for k, v in grammar.items() if k != "mainly")


def show(entry):
    """An entry as a reviewer reads it: everything it states, nothing of how it is stored."""
    lines = []
    for n, lexeme in enumerate(entry.get("lexemes") or [], 1):
        grammar = stated(lexeme.get("grammar") or {})
        said = ", ".join(f"{p.get('ipa') or ''}{' ' + p['reading'] if p.get('reading') else ''}{', accent ' + str(p['accent']) if 'accent' in p else ''}"
                         f" ({p.get('variety')}{', ' + p['spelling'] if p.get('spelling') else ''}{', ' + p['note'] if p.get('note') else ''})"
                         for p in lexeme.get("pronunciations") or [])
        lines.append(f"**Lexeme {n}: {lexeme.get('lemma')}**, {lexeme.get('pos')}" + (f", {lexeme['kind']}" if lexeme.get("kind") not in (None, "word") else ""))
        if grammar:
            lines.append(f"- grammar: {grammar}")
        lines.append(f"- said: {said}")
        if lexeme.get("spellings"):
            lines.append("- spellings: " + "; ".join(f"{s.get('text')}" + (f" ({', '.join(s['varieties'])})" if s.get("varieties") else "") + (" [older books only]" if s.get("pre_reform") else "")
                                                     for s in lexeme["spellings"]))
        if lexeme.get("components"):
            lines.append("- parts: " + " + ".join(f"{c.get('text')} ({c.get('lemma')})" for c in lexeme["components"]))
        if lexeme.get("forms"):
            lines.append("- forms: " + "; ".join(
                f"{f.get('form')} [{', '.join(f'{k}={v}' for k, v in (f.get('features') or {}).items())}]"
                + (f" {' '.join((f.get('ipa') or {}).values())}" if f.get("ipa") else "") + (f" {f['reading']}" if f.get("reading") else "")
                + (f" ({', '.join(f['varieties'])})" if f.get("varieties") else "") + (f" ({f['note']})" if f.get("note") else "") for f in lexeme["forms"]))
        for k, sense in enumerate(lexeme.get("senses") or [], 1):
            lines.append(f"  {k}. [{sense.get('cefr')}] {sense.get('definition')}")
            lines.append(f"     - example: {sense.get('example')}")
            more = []
            if sense.get("frames"):
                more.append("frames: " + "; ".join(sense["frames"]))
            if sense.get("labels"):
                more.append("labels: " + ", ".join(f"{a} {', '.join(v) if isinstance(v, list) else v}" for a, v in sense["labels"].items()))
            if sense.get("regions"):
                more.append("regions: " + ", ".join(r if isinstance(r, str) else f"{r.get('variety')} {r.get('extent')}" for r in sense["regions"]))
            if sense.get("grammar"):
                more.append("grammar: " + stated(sense["grammar"]))
            if sense.get("antonyms"):
                more.append("opposites: " + ", ".join(a.get("lemma", "") for a in sense["antonyms"]))
            if sense.get("standard_alternative"):
                more.append(f"standard word: {sense['standard_alternative']}")
            for line in more:
                lines.append(f"     - {line}")
        if lexeme.get("relations"):
            lines.append("- relations: " + "; ".join(f"{r.get('type')} {r.get('lemma')} ({r.get('pos')})" + (f": {r['why']}" if r.get("why") else "") for r in lexeme["relations"]))
    if entry.get("reject"):
        lines.append(f"Not written as an entry: {entry['reject']}")
    return "\n".join(lines)


def review(name, langs, per_file):
    for lang in langs:
        items = json.loads((MEASURE / name / f"sample-{lang}.json").read_text(encoding="utf-8"))
        written = ingest_v4.load_raw(lang)
        blocks, absent = [], []
        for item in items:
            if item["headword"] not in written:
                absent.append(item["headword"])
                continue
            raw, meta = written[item["headword"]]
            clean = ingest_v4.clean_entry(lang, raw, meta)  # as a reader would get it: the free fixes applied
            item["prompt_version"], item["writer"] = meta.get("prompt_version"), meta.get("writer")
            item["lexemes"], item["senses"] = len(clean["lexemes"]), sum(len(lx.get("senses") or []) for lx in clean["lexemes"])
            item["rejected"] = bool(clean.get("reject")) and not clean["lexemes"]
            blocks.append(f"### {item['id']}. {item['headword']}\n\n{show(clean)}")
        (MEASURE / name / f"sample-{lang}.json").write_text(json.dumps(items, ensure_ascii=False, indent=1) + "\n", encoding="utf-8")
        folder = MEASURE / name / "review"
        folder.mkdir(parents=True, exist_ok=True)
        parts = [blocks[i:i + per_file] for i in range(0, len(blocks), per_file)]
        for n, part in enumerate(parts, 1):
            (folder / f"{lang}-{n}.md").write_text(REVIEW.format(name=LANG_NAME[lang], part=n, parts=len(parts), ours=OURS.get(lang, ""),
                                                                 entries="\n\n".join(part)), encoding="utf-8")
        print(f"{lang}: {len(blocks)} entries in {len(parts)} files" + (f"; not written yet: {', '.join(absent)}" if absent else ""))


def answers_of(text):
    """{id: findings} from a reviewer's answer: every line that is a JSON object with an id."""
    found = {}
    for line in text.splitlines():
        line = line.strip().rstrip(",")
        if line.startswith("{") and '"id"' in line:
            try:
                row = json.loads(line)
            except json.JSONDecodeError:
                continue
            found[int(row["id"])] = [f for f in row.get("findings") or [] if f.get("fault") in FAULTS]
    return found


def tally(name, langs):
    total = defaultdict(Counter)
    for lang in langs:
        if not (MEASURE / name / f"sample-{lang}.json").exists():
            continue
        items = {i["id"]: i for i in json.loads((MEASURE / name / f"sample-{lang}.json").read_text(encoding="utf-8"))}
        read = {}
        for f in sorted((MEASURE / name / "answers").glob(f"{lang}-*.md")):
            read.update(answers_of(f.read_text(encoding="utf-8")))
        if not read:
            continue
        for number, findings in read.items():
            item = items.get(number)
            if item is None or item.get("rejected"):
                continue
            kinds = {f["fault"] for f in findings}
            misleading = {f["fault"] for f in findings if f.get("misleads")}
            for key in ((lang, item["part"]), ("all", item["part"]), (lang, "both"), ("all", "both")):
                total[key]["entries"] += 1
                total[key]["clean"] += not findings
                for fault in FAULTS:
                    total[key][fault] += fault in kinds
                    total[key][fault + " that misleads"] += fault in misleading
        print(f"{lang}: {len(read)} of {len(items)} entries answered")
    print()
    for (lang, part), c in sorted(total.items()):
        n = c["entries"]
        rate = lambda k: f"{100 * c[k] / n:.0f}%"  # noqa: E731
        print(f"{lang:4} {part:5} n={n:3}  clean {rate('clean')}  | " + "  ".join(f"{fault} {rate(fault)} ({rate(fault + ' that misleads')} misleading)" for fault in FAULTS))
    whole = total.get(("all", "both"))
    if whole:
        print("\nagainst the bar (entries in a hundred with the fault):")
        for fault, most in BAR.items():
            got = 100 * whole[fault] / whole["entries"]
            print(f"  {fault:13} {got:4.1f}  aim: at most {most}  {'met' if got <= most else 'not met'}")


if __name__ == "__main__":
    ap = argparse.ArgumentParser()
    ap.add_argument("--name", default=PROMPT_VERSION.replace(".", ""))
    ap.add_argument("--langs", default=",".join(TARGET_LANGUAGES))
    ap.add_argument("--sample", action="store_true")
    ap.add_argument("--review", action="store_true")
    ap.add_argument("--tally", action="store_true")
    ap.add_argument("--have", type=int, default=60)
    ap.add_argument("--new", type=int, default=40)
    ap.add_argument("--per-file", type=int, default=100)
    ap.add_argument("--seed", default="2026-11-17")
    ap.add_argument("--list", default=None, help="draw only from headwords/<list>-<lang>.json")
    a = ap.parse_args()
    langs = a.langs.split(",")
    if a.sample:
        sample(a.name, langs, a.have, a.new, a.seed, a.list)
    if a.review:
        review(a.name, langs, a.per_file)
    if a.tally:
        tally(a.name, langs)
