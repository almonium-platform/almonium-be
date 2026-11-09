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
  reject  only an abbreviation or misspelling of another word: no call; the writer would refuse it

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
RECORDS = HERE / "out" / "gate"
CAPITALISED_NOUNS = {"de"}

_record_lock = threading.Lock()


@dataclass(frozen=True)
class Verdict:
    action: str  # write | form | reject
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


def verdict(lang, headword):
    table = load(lang)
    found = [(s, table[s]) for s in spellings(lang, headword) if s in table]
    if not found:
        return Verdict("write", "not in Wiktionary")
    flags = set("".join(fl for _, (fl, _) in found))
    if "L" in flags:
        return Verdict("write", "lemma")
    if "A" in flags:
        return Verdict("write", "alternative spelling")
    if "F" in flags:
        lemmas = []
        for _, (_, aims) in found:
            lemmas += [a for a in aims if a not in lemmas]
        return Verdict("form", "inflected form", tuple(lemmas))
    return Verdict("reject", "abbreviation or misspelling")


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
    ap.add_argument("--langs", default="en,de,fr,es,it,pt,uk")
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
