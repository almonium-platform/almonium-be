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
  measure.py --name v49 --sample --have 0 --new 40 --mended 20          new words and older words to mend, no held ones
  mend_v4.py --headwords measure-<name>-mend ...             mend those; a word a draw should not have is named in
                                                             measure/<name>/skip.json and the sample drawn again
  measure.py --name v410 --sample --written v4.10 --have 0 --new 40 --planted 8 --gold 5
                                                             entries already written under a version, nothing to buy,
                                                             with the reader measured (below)

  measure.py --name v410e --sample --like v410 --folder review-evidence-v410e --gold 5
                                                             the same words written a second way (a trial folder), to
                                                             be read by the same reader; --pair v410 then compares

The reader is measured too, by entries it cannot tell from the rest:
  planted  entries of the same pool with one fault put in by code (a gender, the stressed syllable, Count for
           Mass, two examples changed over), each one a fault Wiktionary contradicts or the entry itself shows.
           The share found is the reader's hit rate. What was planted is in measure/<name>/planted-<lang>.json.
  gold     the example entries a person has checked. What the reader reports there is its false alarms.
And every drawn entry is held against Wiktionary first (witness.py): what differs is kept in the sample file,
unseen by the reader, so the count can say how many of those the reader found. A second reader's answers go
under measure/<name>/answers-2/ and the tally says how often the two name the same fault.

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
FAULTS = ("fact", "claim", "missing-core", "missing-minor", "split", "wording", "collocation")
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
  elsewhere too), a label (formal, dated, offensive) the sense does not deserve, a grammar value
  that is wrong for the word's ordinary use. A grammar value is not a claim because an exception
  to it exists: see below what it states.
- **missing-core**: the entry lacks one of the two or three commonest meanings or parts of speech of
  the word, one a learner at B1 or B2 meets in ordinary text.
- **missing-minor**: it lacks a meaning that a learner's dictionary lists but that is rarer than
  those the entry has. Not technical, dialectal or obsolete meanings: those are left out on purpose.
- **split**: two numbered senses are one meaning, or a mere construction or an idiom is numbered as
  a sense.
- **wording**: a definition is narrower or wider than its sense, an example does not show its
  sense, or an example is not natural {name}.
- **collocation**: a collocation that is not said that way in {name}; or one that does not belong
  among the ten a learner is shown for the sense, because anyone would build it from the meanings
  or because it is rare; or a list that lacks the commonest partner of the sense while it has
  rarer ones. Quote the collocation in "what". A frame with the wrong preposition or case is a
  fact, not this.

Do not report: the order of senses, a CEFR level off by one step, how syllables are divided, a
definition you would have worded differently when the given one is true, or anything about style.

These are this dictionary's own definitions, not faults:

- valency Refl stands for every use with a reflexive pronoun, whatever a grammar of {name} calls it
  (reflexive, reciprocal, pronominal); Tr means the verb takes a direct object, so a verb whose
  object needs a preposition or the dative is Intr.
- a grammar value (countability, valency, position, number) says how the word is ordinarily used, as
  a learner's dictionary of {name} prints it: "Mass" is "uncountable" in that sense, although kinds
  of the thing can be counted. Followed by "mainly", the other use is common too. Report a value
  only when a learner's dictionary would not print it for this word or sense.
- position Attr means the adjective is used joined to a noun and not after a verb such as "be",
  wherever {name} puts it (before the noun in English, mostly after it in French); Pred means only
  after such a verb. Neither says on which side of the noun the adjective stands.
- a lexeme is one word with one way of saying and inflecting it. Two homonyms that are said and
  inflected alike are senses of one lexeme here, although dictionaries of {name} number them apart.
- a headword that is also an inflected form of another word (given, ladies), an abbreviation or a
  name has only the lexemes it is the lemma of. The reader reaches the other word by a link that
  is not shown here.
- a frame is the word with what it governs, every open place marked (depend on sth); a collocation
  is the word with a particular word it usually goes with, or a set phrase, with no open place
  (heavy rain, in the lead). A sense holds at most five frames and ten collocations, so a missing one
  is not a fault.
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


# The faults planted to measure a reader, the faults it may rightly call each, and the words by which a
# finding is known to be about the planted place and not about something else in the entry
PLANTS = ("gender", "stress", "countability", "example")
PLANT_FOUND = {"gender": ({"fact"}, r"gender|masc|fem|neut|article|genre|género|genere|gênero|genus|род"),
               "stress": ({"fact"}, r"stress|accent|syllab|transcri|ipa|pronunc|said|reading|ударен"),
               "countability": ({"fact", "claim"}, r"count|mass|plural|number"),
               "example": ({"wording", "fact"}, r"example|sense")}
READING_VOWELS = {"ru": "аеиоуыэюя", "it": "aeiou"}
STRESS_MARK = {"ru": "\u0301", "it": "\u0300"}


def moved_stress(ipa):
    """A transcription with its stress on the next syllable (the one before, from the last), and the two places:
    (text, from, to), or None when it has not exactly one stress mark and two syllables or more."""
    body = ipa.strip("/")
    syllables = body.split(".")
    marked = [i for i, syllable in enumerate(syllables) if "ˈ" in syllable]
    if len(syllables) < 2 or len(marked) != 1 or "ˌ" in body or " " in body:
        return None
    at = marked[0]
    to = at + 1 if at + 1 < len(syllables) else at - 1
    syllables[at] = syllables[at].replace("ˈ", "")
    syllables[to] = "ˈ" + syllables[to]
    return "/" + ".".join(syllables) + "/", at, to


def moved_reading(lang, reading, at, to, syllables):
    """A stressed spelling (ме́бель, bestiàme) with its mark on the vowel of another syllable, or None when
    its vowels are not as many as the syllables or the stress is the letter ё, which cannot lose it."""
    import unicodedata
    text = unicodedata.normalize("NFD", reading)
    if "\u0308" in text or lang not in READING_VOWELS:
        return None
    bare = text.replace("\u0301", "").replace("\u0300", "")
    places = [i for i, ch in enumerate(bare) if ch.lower() in READING_VOWELS[lang]]
    if len(places) != syllables:
        return None
    return unicodedata.normalize("NFC", bare[:places[to] + 1] + STRESS_MARK[lang] + bare[places[to] + 1:])


def plant(lang, entry, kind, facts=None):
    """(the entry with one fault of the kind put in, what was changed), or None when it has no place for it.
    A planted fault must be one a reader can show: a gender or a stress is planted only where Wiktionary has
    the entry's own and no other, Mass only on a noun Wiktionary gives a plural, and two examples are
    changed over only between two senses of one word."""
    import witness
    entry = json.loads(json.dumps(entry))
    for n, lexeme in enumerate((entry.get("lexemes") or [])[:1], 1):  # the first lexeme: the word a reader came for, not a rare second one
        lemma, grammar = lexeme.get("lemma") or "", lexeme.get("grammar") or {}
        genders, heard, plurals = ((facts or {}).get(lemma) or (set(), [], set()))[:3]
        if " " in lemma:
            continue
        if kind == "gender" and lexeme.get("pos") == "NOUN" and genders == {grammar.get("gender")} and "by_variety" not in grammar:
            now = {"Masc": "Fem", "Fem": "Masc", "Neut": "Masc"}[grammar["gender"]]
            was, grammar["gender"] = grammar["gender"], now
            return entry, {"kind": kind, "where": f"lexeme {n} grammar", "was": was, "now": now}
        if kind == "countability" and lexeme.get("pos") == "NOUN" and grammar.get("countability") == "Count" and plurals \
                and "countability" not in (grammar.get("mainly") or []) and not any("countability" in (s.get("grammar") or {}) for s in lexeme["senses"]):
            grammar["countability"] = "Mass"
            return entry, {"kind": kind, "where": f"lexeme {n} grammar", "was": "Count", "now": "Mass"}
        if kind == "example" and len(lexeme.get("senses") or []) >= 2 and all(s.get("example") for s in lexeme["senses"][:2]):
            first, second = lexeme["senses"][:2]
            first["example"], second["example"] = second["example"], first["example"]
            return entry, {"kind": kind, "where": f"lexeme {n} senses 1 and 2", "was": "each its own example", "now": "the two changed over"}
        said = lexeme.get("pronunciations") or []
        if kind == "stress" and said and not any(p.get("spelling") for p in said) and len({p.get("variety") for p in said}) == len(said):
            moves = [moved_stress(p.get("ipa") or "") for p in said]
            theirs = [t for t in (witness.stress_place(ipa) for _, ipa, _ in heard) if t]
            if not all(moves) or len({(m[1], m[2], len(p["ipa"].split("."))) for m, p in zip(moves, said)}) != 1 or not theirs:
                continue
            # only where Wiktionary has our stress and not the one planted: a word stressed two ways is no test
            if not all(any(witness.same_stress(witness.stress_place(p["ipa"]), t) for t in theirs) for p in said) \
                    or any(witness.same_stress(witness.stress_place(m[0]), t) for m in moves for t in theirs):
                continue
            readings = [moved_reading(lang, p["reading"], m[1], m[2], len(p["ipa"].split("."))) if p.get("reading") else "" for m, p in zip(moves, said)]
            if any(r is None for r in readings):
                continue
            was = said[0]["ipa"]
            for p, move, reading in zip(said, moves, readings):
                p["ipa"] = move[0]
                if reading:
                    p["reading"] = reading
            return entry, {"kind": kind, "where": f"lexeme {n} said", "was": was, "now": said[0]["ipa"]}
    return None


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
- where the headword is a word, its use as a suffix, a prefix or an auxiliary (目 in 二回目, そう in
  降りそう) is a sense or a lexeme of it on purpose; a prefix or suffix that is no word (〜的) has no entry.
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


def japanese_pool(skip):
    """Japanese has no list of words to write: its pool is what core_list reads out of the frequency list, of two
    characters or more, drawn evenly from the four bands; what is not a word is named in the skip file."""
    import core_list
    return {r["headword"]: r["zipf"] for r in core_list.japanese(BANDS[-1][0]) if len(r["headword"]) > 1}


def draw_mended(name, lang, mended, seed, skip=()):
    """Older words to mend for this measurement: [(headword, zipf)]. The words already mended for it come first, so
    that a second draw keeps what the first one paid for; the rest are drawn in turn from the four bands of what
    waits, a word a writer has since written afresh left out (the queue names every word with an older entry)."""
    import mend_v4
    from wordfreq import zipf_frequency
    rng = random.Random(f"{seed}-{lang}-mended")
    done = []
    for meta in sorted(OUT.glob(f"mend-*-v4/{lang}/*.meta.json")):
        if json.loads(meta.read_text()).get("set") == f"measure-{name}-mend" and meta.with_name(meta.name[:-10] + ".json").exists():
            word = meta.name[:-10]
            if word not in skip and json.loads(meta.with_name(word + ".json").read_text()).get("lexemes"):
                done.append((word, round(zipf_frequency(word, lang), 2)))
    out = done[:mended]
    waiting = [(r["headword"], r["zipf"]) for r in mend_v4.queue(lang, "")
               if r["headword"].isalpha() and (lang == "de" or r["headword"] == r["headword"].lower())]
    fresh = {f.stem for d in OUT.glob(f"*-v4/{lang}") if not d.parent.name.startswith("mend-") for f in d.glob("*.json")}
    bands = []
    for low, high in BANDS:
        band = sorted(w for w in waiting if low <= w[1] < high)  # sorted: the queue's order among equals is not the same twice
        drawn = rng.sample(band, min(len(band), 6 * mended // len(BANDS)))
        bands.append([w for w in drawn if w[0] not in skip and w[0] not in fresh and w[0] not in {d[0] for d in done}])
    while len(out) < mended and any(bands):
        for band in bands:
            if band and len(out) < mended:
                out.append(band.pop(0))
    return out


def draw(lang, have, new, seed, only=None, skip=(), name=None):
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
    # the words already written for this measurement come first: a second draw keeps what the first one paid for
    done = []
    for meta in sorted(OUT.glob(f"*-v4/{lang}/*.meta.json")):
        if name and json.loads(meta.read_text()).get("set") == f"measure-{name}":
            word = meta.name[:-10]
            if word not in skip:
                done.append(word)
                if word not in zipf:
                    from wordfreq import zipf_frequency
                    zipf[word] = round(zipf_frequency(word, lang), 2)
    if lang == "ja":
        zipf = {**zipf, **japanese_pool(skip)}
        part_new = []
        for low, high in BANDS:
            band = sorted(h for h in zipf if h not in taken and h not in part_have and low <= zipf[h] < high)
            drawn = rng.sample(band, min(len(band), 3 * new // len(BANDS)))
            part_new += [h for h in drawn if h not in skip][: new // len(BANDS)]
        part_new = (done + [h for h in part_new if h not in done])[:new]
        return [(h, zipf[h]) for h in part_have], [(h, zipf[h]) for h in part_new]
    # German writes its nouns with a capital, so a capital does not make a name there. A capitalised word that English
    # uses as much (Paris, Google, Adam) is left out, and so is what Wiktionary files as a lemma and is a form: an
    # Italian infinitive with its pronoun (farlo, dirgli), a Portuguese past (mandaram). The rest is in the skip file.
    from wordfreq import zipf_frequency
    form = re.compile({"it": r".*r(?:lo|la|li|le|ti|ci|vi|mi|ne|gli|glielo)$", "pt": r".*[aeií]ram$"}.get(lang, r"$^"))
    free = sorted(h for h in zipf if h not in taken and h not in older and h.isalpha() and len(h) > 2
                  and (h == h.lower() or (lang == "de" and zipf_frequency(h.lower(), "en") < 3.0)) and not form.match(h)
                  and known.get(h, ("",))[0] == "L")
    free.sort(key=lambda h: -zipf[h])
    pool = free[: max(new * 25, 1000)]  # the commonest of what is free: a rarer word says less about the dictionary
    part_new = [h for h in rng.sample(pool, min(len(pool), 3 * new)) if h not in skip]  # a skipped word gives its place to the next
    part_new = (done + [h for h in part_new if h not in done])[:new]
    return [(h, zipf[h]) for h in part_have], [(h, zipf[h]) for h in part_new]


def draw_written(lang, version, new, planted, seed, skip=()):
    """Entries already written under a prompt version: ([(headword, zipf)] to read as they are, {headword:
    what was planted and the entry with it}). The planted ones are other words of the same pool, each given
    one fault, the kinds taken in turn."""
    import witness
    from wordfreq import zipf_frequency
    rng = random.Random(f"{seed}-{lang}-written")
    written = ingest_v4.load_raw(lang)
    pool = sorted(h for h, (entry, meta) in written.items() if entry.get("lexemes") and meta.get("prompt_version") == version and h not in skip)
    real = rng.sample(pool, min(len(pool), new))
    rest = [h for h in pool if h not in set(real)]
    rng.shuffle(rest)
    facts, put = witness.facts(lang) or {}, {}
    kinds = [k for k in PLANTS if k != "gender" or lang != "en"]
    for slot in range(planted):
        kind = kinds[slot % len(kinds)]
        for word in rest:
            if word in put:
                continue
            raw, meta = written[word]
            done = plant(lang, ingest_v4.clean_entry(lang, json.loads(json.dumps(raw)), meta), kind, facts)
            if done:
                put[word] = {**done[1], "entry": done[0]}
                break
    return [(h, round(zipf_frequency(h, lang), 2)) for h in real], put


def folder_entries(folder, lang):
    """headword -> (entry, meta) of a trial folder under out/, which holds words written a second way."""
    found = {}
    for f in sorted((OUT / folder / lang).glob("*.json")):
        if not f.name.endswith(".meta.json"):
            meta = f.with_name(f.stem + ".meta.json")
            found[f.stem] = (json.loads(f.read_text(encoding="utf-8")), {**(json.loads(meta.read_text(encoding="utf-8")) if meta.exists() else {}), "writer": folder})
    return found


def sample_like(name, other, folder, langs, gold):
    """The same words as another sample's, in its order, read from a trial folder: the second arm of a comparison.
    Each item names the entry it is paired with (pair: the id in the other sample)."""
    (MEASURE / name).mkdir(parents=True, exist_ok=True)
    for lang in langs:
        theirs = json.loads((MEASURE / other / f"sample-{lang}.json").read_text(encoding="utf-8"))
        items = [{"headword": i["headword"], "zipf": i["zipf"], "part": i["part"], "pair": i["id"], **({"folder": folder} if i["part"] == "new" else {})}
                 for i in theirs if i["part"] == "new" or (i["part"] == "gold" and gold)]
        for number, item in enumerate(items, 1):
            item["id"] = number
        (MEASURE / name / f"sample-{lang}.json").write_text(json.dumps(items, ensure_ascii=False, indent=1) + "\n", encoding="utf-8")
        print(f"{lang}: {sum(i['part'] == 'new' for i in items)} entries of {folder}, paired with {other}, {sum(i['part'] == 'gold' for i in items)} checked by hand")


def pair(name, other, langs):
    """Two samples of the same words, one reader: in how many entries each fault is found in each, and where it went or came.
    The entries checked by hand are the same text in both samples, so what the reader says of them twice is the reader's
    own drift between the two readings: a change in the written entries smaller than that is not a change."""
    there, here, moved = Counter(), Counter(), defaultdict(list)
    same, sizes = Counter(), defaultdict(list)
    n = 0
    for lang in langs:
        ours = {i["id"]: i for i in json.loads((MEASURE / name / f"sample-{lang}.json").read_text(encoding="utf-8"))}
        theirs = {i["id"]: i for i in json.loads((MEASURE / other / f"sample-{lang}.json").read_text(encoding="utf-8"))}
        read_here, read_there = {}, {}
        for f in sorted((MEASURE / name / "answers").glob(f"{lang}-*.md")):
            read_here.update(answers_of(f.read_text(encoding="utf-8")))
        for f in sorted((MEASURE / other / "answers").glob(f"{lang}-*.md")):
            read_there.update(answers_of(f.read_text(encoding="utf-8")))
        for number, item in ours.items():
            if number not in read_here or item["pair"] not in read_there:
                continue
            now, before = {f["fault"] for f in read_here[number]}, {f["fault"] for f in read_there[item["pair"]]}
            if item["part"] == "gold":
                same["entries"] += 1
                same["any", "before"] += bool(before)
                same["any", "now"] += bool(now)
                for fault in FAULTS:
                    same[fault, "before"] += fault in before
                    same[fault, "now"] += fault in now
                continue
            if item["part"] != "new":
                continue
            n += 1
            for fault in FAULTS:
                here[fault] += fault in now
                there[fault] += fault in before
                if (fault in now) != (fault in before):
                    moved[fault, "gone" if fault in before else "new"].append(f"{lang} {item['headword']}")
            first = theirs.get(item["pair"], {})
            if item.get("folder") and "senses" in item and "senses" in first:  # does the second writing follow what it was shown
                sizes[shown_senses(lang, item["headword"])].append((first["senses"], item["senses"]))
    print(f"{n} entries read in both: {other} then {name}, entries in a hundred with the fault")
    for fault in FAULTS:
        if n:
            print(f"  {fault:13} {100 * there[fault] / n:5.1f} -> {100 * here[fault] / n:5.1f}   gone in {len(moved[fault, 'gone'])}, new in {len(moved[fault, 'new'])}")
    if same["entries"]:
        print(f"the reader's own drift, on {same['entries']} entries that are the same text in both:")
        for fault in ("any",) + tuple(FAULTS):
            if same[fault, "before"] or same[fault, "now"]:
                print(f"  {fault:13} found in {same[fault, 'before']:2} -> {same[fault, 'now']:2}")
    if sizes:
        print("senses an entry, by how many senses the writer was shown the second time:")
        for size in sorted(sizes):
            pairs = sizes[size]
            print(f"  shown {SHOWN[size]:12} n={len(pairs):3}  {sum(a for a, _ in pairs) / len(pairs):.2f} -> {sum(b for _, b in pairs) / len(pairs):.2f}"
                  f"   fewer in {sum(b < a for a, b in pairs)}, more in {sum(b > a for a, b in pairs)}")


SHOWN = {0: "none", 1: "one", 2: "two", 3: "three or four", 5: "five to nine", 10: "ten or more"}


def shown_senses(lang, headword):
    """How many senses Wiktionary's record of the word holds, as a step of SHOWN."""
    import wiktionary
    found = wiktionary.evidence(lang, headword)
    count = sum(len(lexeme["senses"]) for lexeme in found["lexemes"]) if found else 0
    return max(step for step in SHOWN if step <= count)


def gold_entries(lang):
    """The language's example entries, which a person has checked: {headword: entry}."""
    from prompting import gold_path
    return {e["headword"]: e for e in json.loads(gold_path(lang, PROMPT_VERSION).read_text(encoding="utf-8"))}


def sample(name, langs, have, new, seed, only=None, mended=0, written=None, planted=0, gold=0):
    (MEASURE / name).mkdir(parents=True, exist_ok=True)
    skip_path = MEASURE / name / "skip.json"  # {"de": ["Alice", ...]}: names and non-words a draw turned up, left out of the next
    skips = json.loads(skip_path.read_text(encoding="utf-8")) if skip_path.exists() else {}
    for lang in langs:
        skip = set(skips.get(lang) or [])
        if written:
            from wordfreq import zipf_frequency
            part_new, put = draw_written(lang, written, new, planted, seed, skip)
            items = [{"headword": h, "zipf": z, "part": "new"} for h, z in part_new]
            items += [{"headword": h, "zipf": round(zipf_frequency(h, lang), 2), "part": "planted"} for h in put]
            taken = {i["headword"] for i in items}
            items += [{"headword": h, "zipf": round(zipf_frequency(h, lang), 2), "part": "gold"} for h in list(gold_entries(lang))[:gold] if h not in taken]
            (MEASURE / name / f"planted-{lang}.json").write_text(json.dumps(put, ensure_ascii=False, indent=1) + "\n", encoding="utf-8")
            random.Random(f"{seed}-{lang}-order").shuffle(items)
            for number, item in enumerate(items, 1):
                item["id"] = number
            (MEASURE / name / f"sample-{lang}.json").write_text(json.dumps(items, ensure_ascii=False, indent=1) + "\n", encoding="utf-8")
            kinds = Counter(p["kind"] for p in put.values())
            print(f"{lang}: {len(part_new)} entries of {written} as written, {len(put)} with a planted fault ({', '.join(f'{k} {v}' for k, v in kinds.items())}), "
                  f"{sum(i['part'] == 'gold' for i in items)} checked by hand")
            continue
        part_have, part_new = draw(lang, have, new, seed, only, skip, name)
        part_mended = draw_mended(name, lang, mended, seed, skip) if mended else []
        items = [{"headword": h, "zipf": z, "part": "have"} for h, z in part_have] + [{"headword": h, "zipf": z, "part": "new"} for h, z in part_new]
        items += [{"headword": h, "zipf": z, "part": "mended"} for h, z in part_mended]
        if part_mended:
            (HERE / "headwords" / f"measure-{name}-mend-{lang}.json").write_text(json.dumps(
                [{"headword": h, "zipf": z, "set": f"measure-{name}-mend"} for h, z in part_mended], ensure_ascii=False, indent=1) + "\n", encoding="utf-8")
        random.Random(f"{seed}-{lang}-order").shuffle(items)
        for number, item in enumerate(items, 1):
            item["id"] = number
        (MEASURE / name / f"sample-{lang}.json").write_text(json.dumps(items, ensure_ascii=False, indent=1) + "\n", encoding="utf-8")
        if not part_new:  # --new 0: a count of what is written, nothing to buy
            print(f"{lang}: {len(part_have)} held entries, no words to write")
            continue
        (HERE / "headwords" / f"measure-{name}-{lang}.json").write_text(json.dumps(
            [{"headword": h, "zipf": z, "set": f"measure-{name}"} for h, z in part_new], ensure_ascii=False, indent=1) + "\n", encoding="utf-8")
        print(f"{lang}: {len(part_have)} held entries, {len(part_new)} words to write (Zipf {min(z for _, z in part_new):.2f} to {max(z for _, z in part_new):.2f})"
              + (f", {len(part_mended)} to mend" if part_mended else ""))


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
                         f" ({p.get('variety')}{', ' + p['spelling'] if p.get('spelling') else ''}{', ' + p['note'] if p.get('note') else ''}{': ' + p['context'] if p.get('context') else ''})"
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
            if sense.get("collocations"):
                more.append("collocations: " + "; ".join(sense["collocations"]))
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
        put_path = MEASURE / name / f"planted-{lang}.json"
        put = json.loads(put_path.read_text(encoding="utf-8")) if put_path.exists() else {}
        checked = gold_entries(lang) if any(i["part"] == "gold" for i in items) else {}
        import witness
        facts = witness.facts(lang)
        blocks, absent = [], []
        for item in items:
            if item["part"] == "gold":
                raw, meta = checked[item["headword"]], {"prompt_version": "gold", "writer": "gold"}
            elif item.get("folder"):
                trial = folder_entries(item["folder"], lang)
                if item["headword"] not in trial:
                    absent.append(item["headword"])
                    continue
                raw, meta = trial[item["headword"]]
            elif item["headword"] not in written:
                absent.append(item["headword"])
                continue
            else:
                raw, meta = written[item["headword"]]
            clean = ingest_v4.clean_entry(lang, json.loads(json.dumps(raw)), meta)  # as a reader would get it: the free fixes applied
            if item["part"] == "planted":
                clean = put[item["headword"]]["entry"]
            elif facts is not None:  # what Wiktionary states otherwise, kept beside the sample and not shown to the reader
                item["witness"] = [f"{fact}: {detail}" for fact, verdict, detail, _ in witness.compare(lang, clean, facts) if verdict == "differs"]
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
    total, reader = defaultdict(Counter), defaultdict(Counter)
    for lang in langs:
        if not (MEASURE / name / f"sample-{lang}.json").exists():
            continue
        items = {i["id"]: i for i in json.loads((MEASURE / name / f"sample-{lang}.json").read_text(encoding="utf-8"))}
        read = {}
        for f in sorted((MEASURE / name / "answers").glob(f"{lang}-*.md")):
            read.update(answers_of(f.read_text(encoding="utf-8")))
        if not read:
            continue
        put_path = MEASURE / name / f"planted-{lang}.json"
        put = json.loads(put_path.read_text(encoding="utf-8")) if put_path.exists() else {}
        second = {}
        for f in sorted((MEASURE / name / "answers-2").glob(f"{lang}-*.md")) if (MEASURE / name / "answers-2").is_dir() else []:
            second.update(answers_of(f.read_text(encoding="utf-8")))
        for number, findings in read.items():
            item = items.get(number)
            if item is None or item.get("rejected"):
                continue
            kinds = {f["fault"] for f in findings}
            misleading = {f["fault"] for f in findings if f.get("misleads")}
            if item["part"] == "planted":  # was the planted fault named, by its kind and its place
                spec = put[item["headword"]]
                faults, words = PLANT_FOUND[spec["kind"]]
                found = any(f["fault"] in faults and re.search(words, f"{f.get('where', '')} {f.get('what', '')}", re.I) for f in findings)
                reader["planted " + spec["kind"]]["found" if found else "missed"] += 1
                if not found:
                    reader["missed"][f"{lang} {item['headword']}: {spec['kind']} {spec['was']} -> {spec['now']}" + ("; reported something else" if findings else "")] += 1
            elif item["part"] == "gold":
                reader["gold"]["with a finding" if findings else "clean"] += 1
                for f in findings:
                    reader["gold findings"][f"{lang} {item['headword']}: {f['fault']}, {f.get('what', '')[:90]}"] += 1
            elif item.get("witness"):  # what Wiktionary contradicts: did the reader see a fact there
                reader["witness"]["a fact reported" if "fact" in kinds else "no fact reported"] += 1
            if item["part"] not in ("planted", "gold") and number in second:
                theirs = {f["fault"] for f in second[number]}
                for fault in FAULTS:
                    if fault in kinds or fault in theirs:
                        reader["two readers, " + fault]["both" if fault in kinds and fault in theirs else "one"] += 1
            measured = item["part"] not in ("planted", "gold")
            for key in ((lang, item["part"]), ("all", item["part"])) + (((lang, "both"), ("all", "both")) if measured else ()):
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
    if reader:
        print("\nthe reader, measured:")
        for kind in PLANTS:
            c = reader.get("planted " + kind)
            if c:
                print(f"  planted {kind:13} found {c['found']} of {c['found'] + c['missed']}")
        for line in reader.get("missed", {}):
            print(f"      missed: {line}")
        if reader.get("gold"):
            c = reader["gold"]
            print(f"  entries checked by hand: a finding in {c['with a finding']} of {c['with a finding'] + c['clean']}")
            for line in reader["gold findings"]:
                print(f"      {line}")
        if reader.get("witness"):
            c = reader["witness"]
            print(f"  entries Wiktionary contradicts: a fact reported in {c['a fact reported']} of {c['a fact reported'] + c['no fact reported']}")
        for fault in FAULTS:
            c = reader.get("two readers, " + fault)
            if c:
                print(f"  two readers, {fault:13} named by both in {c['both']} of the {c['both'] + c['one']} entries either named")


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
    ap.add_argument("--mended", type=int, default=0, help="older words to mend for the measurement, as a third part")
    ap.add_argument("--written", default=None, help="draw the new part from entries already written under this prompt version (v4.10): nothing to buy")
    ap.add_argument("--planted", type=int, default=0, help="with --written: entries given one fault by code, to measure the reader's hit rate")
    ap.add_argument("--gold", type=int, default=0, help="with --written: entries a person has checked, to measure the reader's false alarms")
    ap.add_argument("--like", default=None, help="with --sample and --folder: the sample whose words are read a second time, from a trial folder")
    ap.add_argument("--folder", default=None, help="the trial folder under out/ that holds the second writing of those words")
    ap.add_argument("--pair", default=None, help="compare this sample's count with the named sample's, entry by entry")
    a = ap.parse_args()
    langs = a.langs.split(",")
    if a.sample and a.like:
        sample_like(a.name, a.like, a.folder, langs, a.gold)
    elif a.sample:
        sample(a.name, langs, a.have, a.new, a.seed, a.list, a.mended, a.written, a.planted, a.gold)
    if a.review:
        review(a.name, langs, a.per_file)
    if a.tally:
        tally(a.name, langs)
    if a.pair:
        pair(a.name, a.pair, langs)
