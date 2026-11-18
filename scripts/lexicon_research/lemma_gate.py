"""The gate before a paid write: is the headword a lemma at all?

A frequency list is full of inflected forms (German "entwicklern", "festlichen"; French
"comparaient"). Sent to a writer, each one is a paid call that ends in a reject, or worse, in a
full entry for its lemma that overwrites the one already published under that lemma. Wiktionary
already knows which spellings are only forms; lemma_index.py keeps its verdict per spelling, and
this module reads it.

  write   a lemma (also when it is a form too, like English "parking"), an alternative or regional
          spelling (also when it is a form too, like Brazilian "controle"), or a word Wiktionary
          does not know: the writer decides, as before
  form    every sense is "form of X": no call; the lemmas X are recorded under out/gate/ so they
          can be queued (--lemma-list)
  name    only the name of a person, a place below a country or a firm (Renate, Leverkusen, Google):
          no call, unless the writer is run with --names
  reject  only an abbreviation or misspelling of another word: no call; the writer would refuse it

A contraction (German "zum", French "au") is written, as an entry of kind "contraction".

A spelling that a written entry already lists ("colour" under "color", "у" under "в") is not stopped,
because it may be another word as well ("tire", to make tired, beside the American spelling of
"tyre"). covered() says which lexemes already have it, the writer passes that to the model as a
"Covered:" line, and the model returns only what is not yet written, or rejects it as a spelling.

German capitalises nouns and wordfreq lowercases everything, so for German the capitalised
spelling is looked up too ("entwicklern" is the dative plural "Entwicklern"). A spelling that is a
lemma in any of its casings is written.

Usage: lemma_gate.py --headwords NAME [--langs de,fr] [--lemma-list NEWNAME]
       (run from the backend repository root)
  Prints how many words of headwords/NAME-<lang>.json the gate would stop, with samples. With
  --lemma-list it also writes headwords/NEWNAME-<lang>.json: the lemmas those forms point at
  (and the ones recorded by earlier runs under out/gate/) that no writer has written and no list holds.
"""
import argparse, json, sys, threading
from collections import Counter
from dataclasses import dataclass, field
from functools import lru_cache
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
from lemma_index import plain  # noqa: E402

HERE = Path("scripts/lexicon_research")
INDEX = HERE / "wiktionary"
OUT = HERE / "out"
RECORDS = OUT / "gate"
V4_DIRS = "*-v4"  # every writer's directory of schema-3 entries
CAPITALISED_NOUNS = {"de"}

_record_lock = threading.Lock()
_covered, _covered_lock = {}, threading.Lock()


_names = False


def names(allowed):
    """Whether a headword that is only a proper name is written (a writer's --names) or stopped, which is the default."""
    global _names
    _names = bool(allowed)


@dataclass(frozen=True)
class Verdict:
    action: str  # write | form | name | reject
    reason: str
    lemmas: tuple = field(default=())

    @property
    def skip(self):
        return self.action != "write"


class MissingIndex(SystemExit):
    pass


@lru_cache(maxsize=None)
def load(lang):
    path = INDEX / f"{lang}.tsv"
    if not path.exists():
        raise MissingIndex(f"no Wiktionary index for {lang}: run scripts/lexicon_research/lemma_index.py --langs {lang}, "
                           f"or pass --no-gate to write without it")
    table = {}
    with path.open(encoding="utf-8") as f:
        for line in f:
            word, flags, aims = (line.rstrip("\n").split("\t") + ["", ""])[:3]
            table[word] = (flags, tuple(dict.fromkeys(plain(a) for a in aims.split("|") if a)))
    return table


def require(langs):
    """Stop before the first call when a language has no index, instead of after hours of writing."""
    for lang in langs:
        load(lang)


def spellings(lang, headword):
    out = [headword]
    for v in (headword.lower(), headword[:1].upper() + headword[1:] if lang in CAPITALISED_NOUNS else None):
        if v and v not in out:
            out.append(v)
    return out


def known(lang, headword):
    """How Wiktionary has the headword: [(spelling, (flags, form targets))]. A frequency list writes everything in
    lower case, so a word found under no spelling is looked up with its capital too ("american" is "American",
    "россии" a form of "Россия"); a word found in lower case is not, or "states", a form, would pass for "the States"."""
    table = load(lang)
    found = [(s, table[s]) for s in spellings(lang, headword) if s in table]
    twin = headword[:1].upper() + headword[1:]
    if not found and twin in table:
        found = [(twin, table[twin])]
    return found


def verdict(lang, headword):
    found = known(lang, headword)
    if not found:
        return Verdict("write", "not in Wiktionary")
    flags = set("".join(fl for _, (fl, _) in found))
    if "L" not in flags and "C" not in flags and "N" in flags:
        # a person, a town, a firm: the writers refused a third of them and wrote the rest as entries no list asked
        # for. names(True) lets them through, for a list that wants them (the places of a book).
        return Verdict("write" if _names else "name", "proper name")
    if flags & set("LPS"):  # a piece of a word and a letter are written when a list names them, as they always were
        return Verdict("write", "lemma" if "L" in flags else "piece of a word or sign")
    if "C" in flags:
        return Verdict("write", "contraction")
    if "A" in flags:
        return Verdict("write", "alternative spelling")
    if "F" in flags:
        lemmas = []
        for _, (_, aims) in found:
            lemmas += [a for a in aims if a not in lemmas]
        return Verdict("form", "inflected form", tuple(lemmas))
    return Verdict("reject", "abbreviation or misspelling")


@lru_cache(maxsize=None)
def kinds(lang):
    """spelling -> the parts of speech of its senses of its own, as Wiktionary names them (noun, verb, name)."""
    table = {}
    with (INDEX / f"{lang}.tsv").open(encoding="utf-8") as f:
        for line in f:
            cells = line.rstrip("\n").split("\t")
            if len(cells) > 3 and cells[3]:
                table[cells[0]] = frozenset(cells[3].split(","))
    return table


# How often the writers refused a word of each kind, of the 32,500 they were given up to 18 November 2026. A refusal
# costs half an entry ($0.0137 against $0.0294), so a list of words to write holds only the kinds they mostly write.
UNFIT = {
    "not in Wiktionary": "a name, a misspelling or a word of another language far more often than a word (refused 47% to 82%)",
    "proper name": "a person, a place or a brand: no list asks for those",
    "inflected form": "the gate stops it and its lemma is queued",
    "alternative spelling": "the entry of the usual spelling lists it (refused 51% in English)",
    "abbreviation or misspelling": "refused nine times in ten",
    "piece of a word or sign": "a prefix, a suffix, a letter",
    "only old, rare or slang senses": "the spelling is frequent for another reason: a name, a form, another language",
}


def unfit(lang, headword):
    """Why a headword does not belong in a list of words to write, or None when it does: one of the keys of UNFIT.
    A word that is a lemma and a form as well (FL) is fit here; whether to queue it is the list maker's question,
    because the answer differs by language."""
    table = load(lang)
    found = [(s, entry[0]) for s, entry in known(lang, headword)]
    capital = table.get(headword[:1].upper() + headword[1:], ("",))[0]  # "robert" beside "Robert"
    if not found:
        return "not in Wiktionary"
    flags = set("".join(fl for _, fl in found))
    if "C" in flags:
        return None
    if "L" in flags:
        if all("M" in fl for _, fl in found if "L" in fl):
            return "proper name"  # a name first and a word after: its frequency is the name's
        if lang not in CAPITALISED_NOUNS and "N" in flags and all(s[:1].isupper() for s, _ in found):
            # a capital outside German means a name or a word made from one. It is a word to write when it is an
            # adjective too (American) or a name that is first of all no person or place (April, England); "William",
            # a given name with a noun for the letter W in radio code, is not
            own = set().union(*(kinds(lang).get(s, frozenset()) for s, _ in found))
            if "adj" not in own and own != {"name"}:
                return "proper name"
        marginal = all("R" in fl for _, fl in found if "L" in fl)  # of the spellings that are words, every one
        twin = "N" in flags or "N" in capital or "F" in flags
        return "only old, rare or slang senses" if marginal and twin else None
    return ("proper name" if "N" in flags else "inflected form" if "F" in flags else "alternative spelling" if "A" in flags
            else "piece of a word or sign" if flags & set("PS") else "abbreviation or misspelling")


def record(lang, headword, v, writer):
    """Keep what the gate stopped, so its lemmas can be queued and the saving counted."""
    RECORDS.mkdir(parents=True, exist_ok=True)
    line = json.dumps({"headword": headword, "action": v.action, "reason": v.reason, "lemmas": list(v.lemmas), "writer": writer},
                      ensure_ascii=False)
    with _record_lock, (RECORDS / f"{lang}.jsonl").open("a", encoding="utf-8") as f:
        f.write(line + "\n")


def stop(lang, row, writer, enabled=True):
    """True when the row must not be written; the reason is recorded and printed."""
    if not enabled:
        return False
    v = verdict(lang, row["headword"])
    if not v.skip:
        return False
    record(lang, row["headword"], v, writer)
    print(f"gate    {lang} {row['headword']:22} {v.reason}{' of ' + ', '.join(v.lemmas) if v.lemmas else ''}: not written", flush=True)
    return True


def other_spellings(entry):
    """(spelling, lemma, pos) for every spelling a written entry lists besides its own headword."""
    own = (entry.get("headword") or "").lower()
    for lexeme in entry.get("lexemes") or []:
        for spelling in lexeme.get("spellings") or []:
            text = (spelling.get("text") or "").strip()
            if text and text.lower() != own:
                yield text.lower(), lexeme.get("lemma"), lexeme.get("pos")


def _spelling_table(lang):
    with _covered_lock:
        if lang not in _covered:
            table = {}
            for f in sorted(OUT.glob(f"{V4_DIRS}/{lang}/*.json")):
                if f.name.endswith(".meta.json"):
                    continue
                text = f.read_text(encoding="utf-8")
                if '"spellings"' not in text:
                    continue
                try:
                    entry = json.loads(text)
                except json.JSONDecodeError:
                    continue
                for spelling, lemma, pos in other_spellings(entry):
                    if (lemma, pos) not in table.setdefault(spelling, []):
                        table[spelling].append((lemma, pos))
            _covered[lang] = table
        return _covered[lang]


def register(lang, entry):
    """A writer calls this after each write: the new entry's other spellings are covered from now on."""
    if not isinstance(entry, dict) or "error" in entry:
        return
    table = _spelling_table(lang)
    with _covered_lock:
        for spelling, lemma, pos in other_spellings(entry):
            if (lemma, pos) not in table.setdefault(spelling, []):
                table[spelling].append((lemma, pos))


def covered(lang, headword):
    """The lexemes of written entries that list this headword as one of their spellings: [(lemma, pos), ...]."""
    return list(_spelling_table(lang).get(headword.lower(), []))


def taken(lang):
    """Every headword written by any writer or held by any list, lowercased."""
    words = {f.stem.lower() for d in (HERE / "out").glob(f"*/{lang}")
             for f in d.glob("*.json") if not f.name.endswith(".meta.json")}
    for f in (HERE / "headwords").glob(f"*{lang}.json"):
        words |= {r["headword"].lower() for r in json.loads(f.read_text())}
    return words


def recorded_lemmas(lang):
    path = RECORDS / f"{lang}.jsonl"
    if not path.exists():
        return []
    return [lemma for line in path.read_text(encoding="utf-8").splitlines() for lemma in json.loads(line).get("lemmas", [])]


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--headwords", required=True, help="a tranche name: reads headwords/<name>-<lang>.json")
    ap.add_argument("--langs", default="en,de,fr,es,it,pt,ru,uk")
    ap.add_argument("--lemma-list", help="write the forms' lemmas that nobody has written or queued to headwords/<this>-<lang>.json")
    ap.add_argument("--samples", type=int, default=12)
    a = ap.parse_args()
    for lang in a.langs.split(","):
        path = HERE / "headwords" / f"{a.headwords}-{lang}.json"
        if not path.exists():
            continue
        rows = json.loads(path.read_text())
        verdicts = [(r["headword"], verdict(lang, r["headword"])) for r in rows]
        counts = Counter(v.reason for _, v in verdicts)
        stopped = [(h, v) for h, v in verdicts if v.skip]
        print(f"{lang}: {len(rows)} headwords, the gate stops {len(stopped)} ({100 * len(stopped) / max(1, len(rows)):.0f}%): {dict(counts)}")
        for h, v in stopped[:a.samples]:
            print(f"    {h} -> {v.reason}{' of ' + ', '.join(v.lemmas) if v.lemmas else ''}")
        if a.lemma_list:
            from wordfreq import zipf_frequency
            used, rows_out = taken(lang), []
            for lemma in [l for _, v in stopped for l in v.lemmas] + recorded_lemmas(lang):
                if lemma.lower() in used or verdict(lang, lemma).skip:
                    continue
                used.add(lemma.lower())
                rows_out.append({"headword": lemma, "zipf": round(zipf_frequency(lemma, lang), 2), "set": a.lemma_list})
            out = HERE / "headwords" / f"{a.lemma_list}-{lang}.json"
            out.write_text(json.dumps(rows_out, ensure_ascii=False, indent=1) + "\n")
            print(f"    {len(rows_out)} lemmas nobody has written or queued -> {out}")


if __name__ == "__main__":
    sys.exit(main())
