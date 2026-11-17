"""Pick candidates for the example entries a writer is shown, and write them out for review.

Today every language's prompt shows one English entry. Example entries in the language itself set the
house style and how finely senses are split, so each language gets three to five of them, chosen from
what the writers already wrote and checked by a reviewer of another model family (or a person) before
any becomes an example: an error in an example is copied into thousands of entries.

A candidate is a written schema-4 entry of a common word (Zipf 4.2 to 5.6) in which check_v4.py finds
nothing at all, a plain word of the language (Wiktionary knows it as a lemma only, not also as a form
of another word, and it is not more at home in English), small enough to show (at most two lexemes and
five senses), and one of the kinds a
prompt should show: a plain noun, a verb with frames, an adjective, an adverb, a headword with two
parts of speech, a labelled sense, a regional sense, a figurative sense. Regions are put in the shape
prompt v4.2 writes.

Output, per language, under gold/:
  candidates/<lang>.json   the candidates, each {"kind", "zipf", "entry"}
  review/<lang>.md         the same for a reviewer: what to check, the rules the entries were written
                           to, the candidates, and the form of the answer

The reviewer's answer is kept under gold/answers/<lang>.md. --take reads it: for each headword named
with --pick, in that order, the corrected entry the answer gives, or the candidate itself where the
verdict was "use as it is". An entry that fails the schema or any check of check_v4.py stops the run;
nothing becomes an example unchecked. The result is gold/<lang>.json, which the prompt shows.

Where a taken entry breaks a rule the prompt got after the review, the correction is ours and is kept
apart from the answer, in gold/own/<lang>.json: a list of {"headword", "path", "value", "why"}, the path
being keys and list positions from the entry down ("lexemes.1.grammar.note").

For a language on trial the reviewer is also asked about the decisions taken for it (QUESTIONS), and a
pilot too small to pick from by kind is reviewed whole, or in the part named: --words.

Usage: gold_candidates.py [--langs en,de] [--per-kind 2] [--rules v4.7]
       gold_candidates.py --langs ja --words 食べる,行く,辛い          (these entries, whatever their kind)
       gold_candidates.py --take gold/answers/en.md --lang en --pick lake,afford,aware,institute,hall
       (run from the backend repository root)
"""
import argparse, json, re, sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
import check_v4  # noqa: E402
import ingest_v4  # noqa: E402
import lemma_gate  # noqa: E402
from prompting import HERE, LANG_NAME, LIMITS_V4, TARGET_LANGUAGES, VARIETIES, system_prompt  # noqa: E402

GOLD = HERE / "gold"
WRITER = "cc-claude-opus-5-5-v4"
KINDS = ("noun", "verb with frames", "adjective", "two parts of speech", "labelled sense", "regional sense", "figurative sense", "adverb")
TWICE = {"noun", "verb with frames"}  # the kinds most entries are: two candidates each


def to_v42(entry):
    """The entry as prompt v4.2 would write it: where a sense is used becomes a place with an extent."""
    entry = json.loads(json.dumps(entry))
    for lexeme in entry.get("lexemes", []):
        for sense in lexeme.get("senses", []):
            if "regions" in sense:
                sense["regions"] = [{"variety": r, "extent": "only"} if isinstance(r, str) else r for r in sense["regions"]]
    return entry


def kinds_of(entry):
    """Every kind of example an entry could serve as."""
    lexemes = entry.get("lexemes") or []
    senses = [s for lx in lexemes for s in lx.get("senses") or []]
    if not lexemes or len(lexemes) > 2 or not 1 <= len(senses) <= 5:
        return []
    found = []
    labels = [s.get("labels") or {} for s in senses]
    if any(s.get("regions") for s in senses):
        found.append("regional sense")
    if any(l.get("figurative") for l in labels):
        found.append("figurative sense")
    if any(set(l) & {"formality", "attitude", "time", "style", "normativity"} for l in labels):
        found.append("labelled sense")
    if len(lexemes) == 2 and lexemes[0].get("pos") != lexemes[1].get("pos"):
        found.append("two parts of speech")
    if len(lexemes) == 1:
        pos = lexemes[0].get("pos")
        if pos == "NOUN" and len(senses) <= 2:
            found.append("noun")
        if pos == "VERB" and 2 <= len(senses) <= 3 and any(s.get("frames") for s in senses):
            found.append("verb with frames")
        if pos == "ADJ" and len(senses) <= 2:
            found.append("adjective")
        if pos == "ADV" and len(senses) <= 2:
            found.append("adverb")
    return found


def plain_word(lang, headword, known):
    """A word to show as typical: only a lemma to Wiktionary ("selling" and "becoming" are also forms),
    and not an English word that the language borrowed ("cool" in French)."""
    if known.get(headword, ("",))[0] != "L":
        return False
    if lang != "en":
        from wordfreq import zipf_frequency
        if zipf_frequency(headword, "en") > zipf_frequency(headword, lang) + 0.3:
            return False
    return True


def candidates(lang, per_kind=1):
    """{kind: [(zipf, headword, entry)]} of faultless common words, the most ordinary first."""
    zipf = ingest_v4.zipf_of(lang)
    known = lemma_gate.load(lang)
    inventories = {v: check_v4.symbols(v) for v in VARIETIES[lang]}
    witness = check_v4.stress_witness(lang) if lang in check_v4.NEEDS_READING else None
    pool = {kind: [] for kind in KINDS}
    for f in sorted((HERE / "out" / WRITER / lang).glob("*.json")):
        if f.name.endswith(".meta.json"):
            continue
        z = zipf.get(f.stem.lower(), 0)
        if not 4.2 <= z <= 5.6 or not f.stem.isalpha() or len(f.stem) < 3 or not plain_word(lang, f.stem, known):
            continue
        try:
            entry = json.loads(f.read_text(encoding="utf-8"))
        except json.JSONDecodeError:
            continue
        kinds = kinds_of(entry) if "error" not in entry else []
        if not kinds or any(not ingest_v4.is_own(entry["headword"], lx) for lx in entry["lexemes"]):
            continue
        entry = settle(json.loads(json.dumps(entry)))  # as ingest would publish it: what a rule puts right is no fault
        if check_v4.check(lang, entry, inventories, witness):
            continue
        for kind in kinds:
            pool[kind].append((z, f.stem, entry))
    chosen, used = {}, set()
    for kind in KINDS:  # rarer kinds first would starve nothing: a headword is used once, in KINDS order
        ranked = sorted(pool[kind], key=lambda c: (abs(c[0] - 4.8), c[1]))
        picks = [c for c in ranked if c[1] not in used][: per_kind * (2 if kind in TWICE else 1)]
        used |= {c[1] for c in picks}
        chosen[kind] = picks
    return chosen


REVIEW = """# Example entries for {name}: review

These are entries of a learner's dictionary of {name}, written by a model. The best four or five will be
shown to the writing model as examples of the house style, so an error left in one is copied into
thousands of entries. Review each candidate strictly.

For each candidate, check:

1. **Facts.** Is anything wrong: a meaning, a gender, a plural or another form, a transcription, a frame,
   a label, a region, a level that is off by more than one step?
2. **The sense list.** Would a monolingual learner's dictionary of {name} number exactly these senses?
   Name a common sense that is missing, and a sense that is only a shade of another.
3. **Style.** Does every definition keep the rules below (6 to 15 words, plain words, a phrase and not a
   sentence, without the headword, without formulas such as "the act of")? Is every example natural,
   and does it show its own sense?

Answer for each candidate with:

- a verdict: **use as it is**, **use with corrections**, or **do not use**, and one line saying why;
- for "use with corrections", the whole corrected entry as JSON, in the same shape.

Then name the four or five you would show, best first. Between them they should cover a plain noun, a
verb with its frames, an adjective, a headword with two parts of speech, and a labelled or regional sense.

## The rules the entries were written to

{rules}

## Candidates

{entries}
"""


# What a reviewer of a language on trial is asked beyond the entries: the decisions its pilot was written to.
QUESTIONS = {
    "ru": """1. The transcription shows reduced vowels, soft consonants and devoicing (молоко /mə.lɐ.ˈko/, хлеб /ˈxlʲep/). Is that
   the right depth for a learner, and is it applied without mistakes?
2. The lemma is written with ё and the spelling with е is listed beside it. Right?
3. A noun lists its genitive singular, nominative plural and genitive plural; a verb its 1sg, 2sg and 3pl, past
   masculine and feminine and imperative. Is anything a learner needs missing, or anything listed needlessly?
4. A form used after numbers (два часа́) has Number=Count, the second locative (в лесу́) Case=Loc, the partitive
   (ча́ю) Case=Par. Are these the cases a learner's dictionary of Russian marks?""",
    "ja": """1. A pronunciation is a reading in hiragana, with no pitch accent and no romanization. Is that enough for a learner's
   dictionary of Japanese, and if not, what should be added and in what notation?
2. The lemma is written as dictionaries head it (kanji with okurigana, or kana); another way of writing it is listed
   only when print uses it, with its script. Are the lemmas and the listed spellings the right ones?
3. A verb is godan, ichidan or irregular; an adjective i or na; a transitive verb and its intransitive partner name each
   other. Does a learner's dictionary of Japanese mark anything else (a noun that takes する, a の-adjective)?
4. A noun's counter is given as its classifier. Are the counters right, and is one counter enough?
5. Politeness is one of honorific, humble, polite, familiar. Are these the right four, and rightly given here?
6. The level is a CEFR estimate. Would a JLPT level serve a learner better?
7. Limits are counted in characters: a definition 10 to 35, an example 12 to 40. Are the definitions written as a
   Japanese learner's dictionary writes them?""",
}


def write(lang, per_kind, rules="v4.2", words=()):
    if words:
        written = ingest_v4.load_raw(lang)
        zipf = ingest_v4.zipf_of(lang)
        flat = [{"kind": "pilot", "zipf": zipf.get(w.lower(), 0), "entry": settle(json.loads(json.dumps(written[w][0])))} for w in words]
    else:
        chosen = candidates(lang, per_kind)
        flat = [{"kind": kind, "zipf": z, "entry": to_v42(entry)} for kind in KINDS for z, _, entry in chosen[kind]]
    (GOLD / "candidates").mkdir(parents=True, exist_ok=True)
    (GOLD / "review").mkdir(parents=True, exist_ok=True)
    (GOLD / "candidates" / f"{lang}.json").write_text(json.dumps(flat, ensure_ascii=False, indent=1) + "\n", encoding="utf-8")
    rules = system_prompt(lang, rules).partition("\n# Output")[0].partition("# Examples")[0].partition("# Lexemes")
    blocks = [f"### {i}. {c['entry']['headword']} ({c['kind']})\n\n```json\n{json.dumps(c['entry'], ensure_ascii=False, indent=1)}\n```"
              for i, c in enumerate(flat, 1)]
    text = REVIEW.format(name=LANG_NAME[lang], rules=(rules[1] + rules[2]).strip(), entries="\n\n".join(blocks))
    for words, characters in LIMITS_V4.get(lang, {}).items():  # a language without spaces is held to its own limits
        text = text.replace(words, characters)
    if lang in QUESTIONS:
        text += (f"\n## Questions about the shape\n\n{LANG_NAME[lang]} is new to this dictionary. After the candidates, answer each of "
                 f"these in a few lines, from what the entries show:\n\n{QUESTIONS[lang]}\n")
    (GOLD / "review" / f"{lang}.md").write_text(text, encoding="utf-8")
    return flat


def reviewed(text):
    """{headword: (verdict, corrected entry or None)} from a reviewer's answer: a "## N. headword" or
    "### N. headword" section each, ending at the next heading, with a verdict in bold and, for a corrected
    entry, one JSON block."""
    found = {}
    for head, body in re.findall(r"^#{2,3} \d+\. (.+?)\n(.*?)(?=^#{1,3} |\Z)", text, re.S | re.M):
        verdict = re.search(r"\*\*(use as it is|use with corrections|do not use)\*\*", body, re.I)
        block = re.search(r"```json\n(.*?)\n```", body, re.S)
        found[head.split(" (")[0].strip()] = (verdict.group(1).lower() if verdict else None, json.loads(block.group(1)) if block else None)
    return found


def settle(entry):
    """What a reviewer without our schema leaves in another form: a component's "inflection" is "inflects", the
    text of a spelling is "text" (reviewers wrote "spelling" and "form"), and a transcription is put in the form the
    dictionary prints (the dot before a stress mark, an Italian double consonant), by the rules ingest uses."""
    for lexeme in entry.get("lexemes", []):
        for component in lexeme.get("components") or []:
            if "inflection" in component and "inflects" not in component:
                component["inflects"] = component.pop("inflection")
        for spelling in lexeme.get("spellings") or []:
            for other in ("spelling", "form"):
                if other in spelling and "text" not in spelling:
                    spelling["text"] = spelling.pop(other)
        ingest_v4.settle_sounds(entry.get("language"), lexeme)
        ingest_v4.plain_forms(lexeme)
        ingest_v4.pronominal_auxiliary(entry.get("language"), lexeme)
        ingest_v4.settle_defaults(lexeme)
    return entry


def amend(entry, path, value):
    """Set one value inside an entry; the path must already lead there."""
    at = entry
    *way, last = [int(step) if step.isdigit() else step for step in path.split(".")]
    for step in way:
        at = at[step]
    at[last]  # a path that leads nowhere is a mistake in the correction, not a new field
    at[last] = value


def take(lang, answer, picks):
    import jsonschema
    schema = json.loads((HERE / "entry.schema.v4.json").read_text())
    inventories = {v: check_v4.symbols(v) for v in VARIETIES[lang]}
    answers = reviewed(Path(answer).read_text(encoding="utf-8"))
    offered = {c["entry"]["headword"]: c["entry"] for c in json.loads((GOLD / "candidates" / f"{lang}.json").read_text(encoding="utf-8"))}
    own_path = GOLD / "own" / f"{lang}.json"
    own = json.loads(own_path.read_text(encoding="utf-8")) if own_path.exists() else []
    gold = []
    for headword in picks:
        verdict, corrected = answers.get(headword, (None, None))
        if verdict == "do not use" or verdict is None:
            sys.exit(f"{headword}: the reviewer's verdict is {verdict or 'missing'}")
        entry = offered.get(headword) if verdict == "use as it is" else corrected  # an entry passed as it is, is ours
        if entry is None:
            sys.exit(f"{headword}: no corrected entry in the answer and no candidate of that name")
        entry = settle(json.loads(json.dumps(entry)))
        for correction in own:
            if correction["headword"] == headword:
                amend(entry, correction["path"], correction["value"])
        jsonschema.validate(entry, schema)
        if faults := check_v4.check(lang, entry, inventories, None):
            sys.exit(f"{headword}: {faults}")
        gold.append(entry)
    (GOLD / f"{lang}.json").write_text(json.dumps(gold, ensure_ascii=False, indent=1) + "\n", encoding="utf-8")
    return gold


if __name__ == "__main__":
    ap = argparse.ArgumentParser()
    ap.add_argument("--langs", default=",".join(TARGET_LANGUAGES))
    ap.add_argument("--per-kind", type=int, default=1)
    ap.add_argument("--rules", default="v4.2", help="the prompt version whose rules the reviewer is shown; the six languages were reviewed against v4.2")
    ap.add_argument("--words", default="", help="review exactly these written entries (a pilot), not candidates picked by kind")
    ap.add_argument("--take", help="a reviewer's answer: build gold/<lang>.json from the entries named with --pick")
    ap.add_argument("--lang")
    ap.add_argument("--pick", default="")
    a = ap.parse_args()
    if a.take:
        gold = take(a.lang, a.take, [h for h in a.pick.split(",") if h])
        print(f"{a.lang}: {len(gold)} example entries -> {GOLD / (a.lang + '.json')}:", ", ".join(e["headword"] for e in gold))
        sys.exit(0)
    for lang in a.langs.split(","):
        flat = write(lang, a.per_kind, a.rules, [w for w in a.words.split(",") if w])
        print(f"{lang}: {len(flat)} candidates:", ", ".join(f"{c['entry']['headword']} ({c['kind']})" for c in flat))
