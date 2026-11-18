"""The state of everything the v4 writers have written, by rules only: no model call, safe to run during a write.

Per language it says how many entries there are, under which prompt, what they cost, and then sorts every
fault a program can see into the two kinds that matter:

  fixed by ingest_v4.py for free
      a lexeme that is not the headword's ("hash out" under "hash"); a word written twice under two
      spellings ("color" and "colour"); a reject that names its word and becomes an alias; an open place
      of a frame spelled another dictionary's way; a frame with no open place, which is a collocation
  needs a paid patch, of that field only
      an entry that failed the schema or holds an error; a definition in the wrong language; a missing
      stress reading (Ukrainian, Italian); a variety the language does not have
  to settle by a rule or a patch
      IPA with a symbol outside the variety's list (often one symbol written another way, which a rule
      maps; sometimes a sound the list lacks); a stress mark that Wiktionary's forms do not have (Ukrainian,
      Russian); a Japanese pitch accent that an accent table does not have for that word and reading

and the faults of style that are worth watching but not worth a call: dictionary formulas ("the act of"),
the headword inside its own definition, word limits, examples that open alike, senses per lexeme.

Usage: check_v4.py [--langs en,de] [--show 5]   (run from the backend repository root)
"""
import argparse, json, re, statistics, sys, unicodedata
from collections import Counter, defaultdict
from functools import lru_cache
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
import jsonschema  # noqa: E402
import ingest_v4  # noqa: E402
from prompting import HERE, REGIONS, SCHEMA_PATH, TARGET_LANGUAGES, VARIETIES  # noqa: E402

SCHEMA = json.loads(SCHEMA_PATH.read_text())
FORMULAS = re.compile(r"\b(the act of|the state of being|the process of|relating to|characteristic of|according to|"
                      r"der Vorgang des|das Tun|action de|le fait de|relatif à|el acto de|relativo a|l'atto di|"
                      r"o ato de|дія за значенням|стан за значенням|стосується)\b", re.I)
ARTICLES = {"en": {"the", "a", "an"}, "de": {"der", "die", "das", "ein", "eine"}, "fr": {"le", "la", "les", "un", "une", "l"},
            "es": {"el", "la", "los", "las", "un", "una"}, "it": {"il", "lo", "la", "i", "gli", "le", "un", "una", "l"},
            "pt": {"o", "a", "os", "as", "um", "uma"}, "uk": set(), "ru": set(), "ja": set()}
ENGLISH = {"the", "of", "and", "that", "which", "who", "with", "from", "for", "is", "are", "or"}
NEEDS_READING = {"uk", "it", "ru"}
# How many words a definition may have. A language without articles says the same in fewer.
DEFINITION_WORDS = {"ru": (4, 15), "uk": (4, 15)}
# A language written without spaces counts characters: (definition, example), as prompting.LIMITS_V4 tells the writer.
CHARACTERS = {"ja": ((10, 35), (12, 40))}
VOWELS = "aeiouàèéìíòóùаеєиіїоуюяыэё"
ACUTE = "́"
FREE = ("lexeme of another lemma", "word written under two spellings", "reject that becomes an alias", "syllable dot missing before a stress mark",
        "pronominal sense without its auxiliary", "double consonant not written twice", "ll of Spain written ʎ",
        "word-level grammar a sense overrides", "stress mark in a form", "open place of a frame spelled another way",
        "frame without an open place", "two patterns or phrases in one string")
SOUND_FAULTS = {"syllable dots added": "syllable dot missing before a stress mark", "double consonants rewritten": "double consonant not written twice",
                "ll of Spain rewritten": "ll of Spain written ʎ"}
PAID = ("error or not JSON", "schema", "definition not in the language", "no stress reading", "variety outside the language")
SETTLE = ("IPA outside the symbol list", "stress Wiktionary does not know", "pitch accent the table does not know")


def words(text):
    return re.findall(r"[^\W\d_]+(?:['’-][^\W\d_]+)*", (text or "").lower())


def symbols(variety):
    if not (HERE / "inventories" / f"{variety}.json").exists():
        return []  # a variety that is not transcribed in IPA has no symbols to check
    d = json.loads((HERE / "inventories" / f"{variety}.json").read_text())
    # "also": sounds the audit accepts although the prompt's list, which must not change under written entries, lacks them
    return sorted(d["consonants"] + d["vowels"] + d["diacritics"] + d.get("also", []), key=len, reverse=True)


def outside(ipa, inventory):
    """The characters of a transcription that no symbol of the inventory accounts for."""
    body, left, i = unicodedata.normalize("NFC", ipa.strip("/")).replace(" ", ""), [], 0
    while i < len(body):
        hit = next((s for s in inventory if body.startswith(s, i)), None)
        if hit:
            i += len(hit)
        else:
            left.append(body[i]); i += 1
    return "".join(left)


def stress_witness(lang):
    path = HERE / "wiktionary" / f"{lang}.stress.tsv"
    if not path.exists():
        return None
    table = {}
    with path.open(encoding="utf-8") as f:
        for line in f:
            plain, _, forms = line.rstrip("\n").partition("\t")
            table[plain] = set(forms.split("|"))
    return table


KANA = re.compile(r"[\u3041-\u3096\u30a1-\u30fa\u30fc]+")


@lru_cache(maxsize=None)
def accent_witness(lang):
    """What a pitch-accent table gives a word: (spelling, reading) -> its numbers, and reading -> the numbers of
    every word read so. None without the table. The table is Kanjium's accents.txt (CC BY-SA 4.0), kept as
    wiktionary/ja.accent.tsv and, like Wiktionary's index, never committed and never copied into an entry:
      curl -o scripts/lexicon_research/wiktionary/ja.accent.tsv \
        https://raw.githubusercontent.com/mifunetoshiro/kanjium/master/data/source_files/raw/accents.txt"""
    path = HERE / "wiktionary" / f"{lang}.accent.tsv"
    if not path.exists():
        return None
    table = defaultdict(set)
    with path.open(encoding="utf-8") as f:
        for line in f:
            word, reading, numbers = (line.rstrip("\n").split("\t") + ["", ""])[:3]
            numbers = {int(n) for n in re.findall(r"\d+", re.sub(r"\(.*?\)", "", numbers))}
            table[(word, reading or word)] |= numbers
            table[reading or word] |= numbers
    return table


def unknown_accents(lang, lexeme):
    """(reading, accent, what the table has) for every accent the table contradicts. A word written with kanji is
    held to its own row; a word written in kana to every word read so, because the table files most of them
    under a spelling with kanji (これ under 此れ)."""
    table = accent_witness(lang)
    lemma = lexeme.get("lemma") or ""
    for p in (lexeme.get("pronunciations") or []) if table else []:
        if "accent" in p and p.get("reading"):
            known = table.get(p["reading"]) if KANA.fullmatch(lemma) else table.get((lemma, p["reading"]))
            if known and p["accent"] not in known:
                yield p["reading"], p["accent"], sorted(known)


def readings(lexeme):
    for p in lexeme.get("pronunciations") or []:
        if p.get("reading"):
            yield p["reading"]
    for f in lexeme.get("forms") or []:
        if f.get("reading"):
            yield f["reading"]


def check(lang, entry, inventories, witness):
    """(kind, detail) for every fault of one written entry that a paid patch or an eye should look at."""
    problems = []
    try:
        jsonschema.validate(entry, SCHEMA)
    except jsonschema.ValidationError as e:
        problems.append(("schema", e.message[:90]))
    allowed = set(REGIONS[lang])
    for lx in entry.get("lexemes", []):
        lemma = (lx.get("lemma") or "").lower()
        tags = [v for s in lx.get("spellings", []) for v in s.get("varieties", [])]
        tags += [v for f in lx.get("forms", []) for v in f.get("varieties", [])]
        for sense in lx.get("senses", []):
            for region in sense.get("regions", []):
                if isinstance(region, str):
                    tags.append(region)
                elif not str(region.get("variety", "")).startswith(f"{lang}-"):
                    tags.append(str(region.get("variety")))  # a country or UN area must be one of the entry's language
        if bad := sorted(set(tags) - allowed):
            problems.append(("variety outside the language", ", ".join(bad)))
        for p in lx.get("pronunciations", []):
            if p.get("variety") in inventories and (left := outside(p.get("ipa", ""), inventories[p["variety"]])):
                problems.append(("IPA outside the symbol list", f"{p['ipa']} has {left}"))
        if ingest_v4.plain_forms(json.loads(json.dumps(lx))):
            problems.append(("stress mark in a form", lx.get("lemma")))
        respelled = json.loads(json.dumps(lx))
        if times := ingest_v4.settle_frames(lang, respelled):
            problems.append(("open place of a frame spelled another way", f"{lx.get('lemma')}: {times} frame{'s' if times > 1 else ''}"))
        if times := ingest_v4.split_frames(lang, respelled):
            problems.append(("two patterns or phrases in one string", f"{lx.get('lemma')}: {times}"))
        if times := ingest_v4.sort_frames(lang, respelled):
            problems.append(("frame without an open place", f"{lx.get('lemma')}: {times} frame{'s' if times > 1 else ''}"))
        for sense in respelled.get("senses") or []:
            for frame in sense.get("frames") or []:
                if why := ingest_v4.unread(lang, lx.get("lemma") or "", frame, ingest_v4.names(lx)):
                    problems.append(("frame a program cannot read", f"{frame}: {why}"))
        if ingest_v4.settle_defaults(json.loads(json.dumps(lx))):
            problems.append(("word-level grammar a sense overrides", lx.get("lemma")))
        if ingest_v4.pronominal_auxiliary(lang, json.loads(json.dumps(lx))):
            problems.append(("pronominal sense without its auxiliary", lx.get("lemma")))
        for fix, times in ingest_v4.settle_sounds(lang, json.loads(json.dumps(lx))).items():
            problems.append((SOUND_FAULTS[fix], f"{lx.get('lemma')}: {times} transcription{'s' if times > 1 else ''}"))
        if lang in NEEDS_READING and " " not in lemma and sum(c in VOWELS for c in lemma) > 1 and not any(p.get("reading") for p in lx.get("pronunciations", [])):
            problems.append(("no stress reading", lx.get("lemma")))
        if witness is not None:
            for reading in readings(lx):
                reading = unicodedata.normalize("NFC", reading)
                known = witness.get(reading.replace(ACUTE, ""))
                if known and ACUTE in reading and reading not in known:
                    problems.append(("stress Wiktionary does not know", f"{reading}, Wiktionary has {', '.join(sorted(known))}"))
        for reading, accent, known in unknown_accents(lang, lx):
            problems.append(("pitch accent the table does not know", f"{lx.get('lemma')} {reading} {accent}, the table has {', '.join(map(str, known))}"))
        for s in lx.get("senses", []):
            d = s.get("definition", "")
            dw = words(d)
            if lang != "en" and len(set(dw) & ENGLISH) >= 2:
                problems.append(("definition not in the language", d[:60]))
            if FORMULAS.search(d):
                problems.append(("dictionary formula", d[:60]))
            if lemma and lemma in dw:
                problems.append(("headword in its definition", d[:60]))
            if lang in CHARACTERS:
                for kind, text, (fewest, most) in zip(("definition outside 6 to 15 words", "example outside 6 to 14 words"),
                                                      (d, s.get("example", "")), CHARACTERS[lang]):
                    if not fewest <= (size := sum(not ch.isspace() for ch in text)) <= most:  # punctuation counts, as the prompt says
                        problems.append((kind, f"{size} characters"))
                continue
            fewest, most = DEFINITION_WORDS.get(lang, (6, 15))
            if not fewest <= len(dw) <= most:
                problems.append(("definition outside 6 to 15 words", f"{len(dw)} words"))
            if not 6 <= len(words(s.get("example", ""))) <= 14:
                problems.append(("example outside 6 to 14 words", f"{len(words(s.get('example', '')))} words"))
    return problems


def reject_kind(text):
    t = text.lower()
    for kind, pattern in (("inflected form", r"inflect|form of|plural of|participle|conjugat"), ("misspelling", r"misspell|typo"),
                          ("spelling of another word", r"spelling of|variant of"), ("contraction", r"contraction"),
                          ("abbreviation", r"abbreviat|acronym|initialism"), ("name", r"proper n|name|surname|place"),
                          ("another language", r"foreign|not a[n]? \w+ word|another language|english word|borrow")):
        if re.search(pattern, t):
            return kind
    return "other"


def audit(lang, show):
    files = [f for d in sorted((HERE / "out").glob(f"*-v4/{lang}")) for f in sorted(d.glob("*.json")) if not f.name.endswith(".meta.json")]
    if not files:
        return None
    inventories = {v: symbols(v) for v in VARIETIES[lang]}
    witness = stress_witness(lang)
    stats, kinds, shown, prompts, rejects = Counter(), Counter(), defaultdict(list), Counter(), Counter()
    out_tokens, costs, senses, openings = [], [], [], Counter()

    def note(kind, headword, detail, entries_hit):
        kinds[kind] += 1
        entries_hit.add(kind)
        if len(shown[kind]) < show:
            shown[kind].append(f"{headword}: {detail}")

    faulty = Counter()
    for f in files:
        meta_f = f.with_name(f.stem + ".meta.json")
        meta = json.loads(meta_f.read_text()) if meta_f.exists() else {}
        version = meta.get("prompt_version", "?")
        prompts[f"{version} {meta.get('prompt_sha') or (ingest_v4.V4_SHA.get(lang, '') if version == 'v4' else '')}".strip()] += 1
        if meta.get("output_tokens"):
            out_tokens.append(meta["output_tokens"])
        if meta.get("total_cost_usd") or meta.get("cost_usd"):
            costs.append(meta.get("total_cost_usd") or meta.get("cost_usd"))
        hit = set()
        try:
            entry = json.loads(f.read_text())
        except json.JSONDecodeError:
            entry = {"error": "not JSON"}
        if "error" in entry:
            stats["errors"] += 1
            note("error or not JSON", f.stem, str(entry["error"])[:70], hit)
        elif entry.get("reject") and not entry.get("lexemes"):
            stats["rejected"] += 1
            rejects[reject_kind(entry["reject"])] += 1
        else:
            stats["written"] += 1
            for lx in entry.get("lexemes", []):
                if not ingest_v4.is_own(entry.get("headword", f.stem), lx):
                    note("lexeme of another lemma", f.stem, f"{lx.get('lemma')} ({lx.get('pos')})", hit)
                    continue
                senses.append(len(lx.get("senses", [])))
                for s in lx.get("senses", []):
                    if w := words(s.get("example", "")):
                        openings[w[0] in ARTICLES[lang]] += 1
            for kind, detail in check(lang, entry, inventories, witness):
                note(kind, f.stem, detail, hit)
        for kind in hit:
            faulty[kind] += 1
    raw = ingest_v4.load_raw(lang)
    duplicates, aliases = ingest_v4.find_duplicates(raw, ingest_v4.zipf_of(lang))
    for headword, lexemes in duplicates.items():
        for index, canonical in lexemes.items():
            kinds["word written under two spellings"] += 1
            if len(shown["word written under two spellings"]) < show:
                shown["word written under two spellings"].append(f"{headword}: {raw[headword][0]['lexemes'][index].get('pos')} is also under {canonical}")
    faulty["word written under two spellings"] = len(duplicates)
    by_reject = [h for h, a in aliases.items() if not raw[h][0].get("lexemes")]
    kinds["reject that becomes an alias"] = faulty["reject that becomes an alias"] = len(by_reject)
    shown["reject that becomes an alias"] = [f"{h} -> {aliases[h]['to']} ({aliases[h]['as']})" for h in by_reject[:show]]

    n = sum(stats.values())
    print(f"\n{lang}: {n} entries: {stats['written']} written, {stats['rejected']} rejected, {stats['errors']} errors; prompts {dict(prompts)}")
    if costs:
        print(f"    cost ${sum(costs):.0f} at list price, ${statistics.mean(costs):.4f} an entry; output tokens median {statistics.median(out_tokens):.0f}")
    if senses:
        print(f"    senses per lexeme {statistics.mean(senses):.2f}, {sum(1 for x in senses if x >= 3) / len(senses):.0%} of lexemes have 3 or more; "
              f"examples opening with an article {openings[True] / max(1, sum(openings.values())):.0%}")
    if rejects:
        print(f"    rejects: {dict(rejects.most_common())}")
    for title, group in (("fixed by ingest_v4.py, free", FREE), ("needs a paid patch of that field", PAID), ("to settle by a rule or a patch", SETTLE),
                         ("style, worth watching", [k for k in kinds if k not in FREE + PAID + SETTLE])):
        rows = [(k, kinds[k]) for k in group if kinds.get(k)]
        if not rows:
            continue
        print(f"    {title}:")
        for kind, count in rows:
            print(f"      {count:6} {kind} ({faulty[kind]} entries, {faulty[kind] / n:.1%})")
            for line in shown[kind]:
                print(f"             {line}")
    if lang in NEEDS_READING and witness is None and lang in ("uk", "ru"):
        print(f"    no stress witness: run lemma_index.py --langs {lang} to compare the readings with Wiktionary")
    if lang == "ja" and accent_witness(lang) is None:
        print("    no accent table: see accent_witness() in check_v4.py to compare the pitch accents with one")
    return {"entries": n, "cost": sum(costs), "paid": sum(faulty[k] for k in PAID), "settle": sum(faulty[k] for k in SETTLE),
            "free": sum(faulty[k] for k in FREE)}


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--langs", default=",".join(TARGET_LANGUAGES))
    ap.add_argument("--show", type=int, default=5)
    a = ap.parse_args()
    totals = Counter()
    for lang in a.langs.split(","):
        if result := audit(lang, a.show):
            totals.update(result)
    if totals["entries"]:
        print(f"\nall: {totals['entries']} entries, ${totals['cost']:.0f} at list price; at most {totals['paid']} entries "
              f"({totals['paid'] / totals['entries']:.1%}) need a paid patch of one field, {totals['settle']} have IPA or stress to settle, "
              f"{totals['free']} are put right by ingest_v4.py for free")


if __name__ == "__main__":
    main()
