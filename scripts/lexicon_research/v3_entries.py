"""The older entries (prompt v3 and before, schema 2) a word already has, so it is mended, not bought again.

Every writer directory under out/ whose name does not end in a prompt suffix (-v4) holds entries of the
older shape; the bake-off's side folders (audio, logs, judge output) hold none. A word with an older entry
is not written from scratch by a v4 writer: mend_v4.py carries its definitions, examples, levels,
pronunciations and glosses into the new shape and asks a model only for what v4 adds.

The main writer comes first, so a word written by several older writers is mended from that one.
"""
import json
from functools import lru_cache
from pathlib import Path

HERE = Path("scripts/lexicon_research")
OUT = HERE / "out"
# folders under out/ that are not writers
NOT_WRITERS = {"audio", "burn", "carry", "cross", "failed", "gate", "grounded", "kaikki", "logs", "mend", "pipeline", "reconcile", "status"}
# the older writers in the order their entries are trusted; any other older writer folder follows
PREFERRED = ["cc-claude-opus-5-5", "codex-gpt-6-astra-high", "claude-opus-5-5", "cc-claude-fable-5-1", "cc-opus", "cc-fable"]


def is_older_writer(name):
    # lexicon-*: ingest_v4.py's clean lexicon, derived from the v4 entries, not an older writer
    return not name.endswith("-v4") and name not in NOT_WRITERS and not name.startswith(("review-", "mend-", "lexicon-"))


@lru_cache(maxsize=None)
def writers():
    found = sorted(d.name for d in OUT.iterdir() if d.is_dir() and is_older_writer(d.name)) if OUT.is_dir() else []
    return [w for w in PREFERRED if w in found] + [w for w in found if w not in PREFERRED]


@lru_cache(maxsize=None)
def headwords(lang):
    """Every headword of the language with a usable older entry (not a failed write)."""
    return {f.name[:-5] for w in writers() for f in (OUT / w / lang).glob("*.json") if not f.name.endswith(".meta.json")}


def has_older(lang, headword):
    return headword in headwords(lang)


def older_entry(lang, headword):
    """(writer, entry) of the first trusted older writer with a usable entry for the word, or (None, None)."""
    for w in writers():
        f = OUT / w / lang / f"{headword}.json"
        if f.exists():
            entry = json.loads(f.read_text())
            if "error" not in entry:
                return w, entry
    return None, None
