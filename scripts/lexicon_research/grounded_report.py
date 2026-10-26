"""Compare writers over the same words: what the free checks and the Wiktionary comparison find, and what it costs.

Per writer (a folder under out/grounded/<arm>/): entries written and unparsable, entries with a structural error
(entry_checks), senses carrying citations, fact disagreements with the evidence by check, common evidence lexemes
left out, lexeme splits that match the reference writer's, and dollars per 1,000 entries at the price each call
reported. Only words every compared writer has an entry for are counted, so the columns compare like with like.

Usage: grounded_report.py --arm v5-evidence --reference claude-opus-5-5-low [--writers a,b,c]
"""

import argparse
import json
import sys
from collections import Counter
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
import entry_checks
import entry_repair
import evidence_checks
import wiktionary

HERE = Path(__file__).resolve().parent


def load(folder):
    entries = {}
    for path in folder.glob("*/*.json"):
        if path.name.endswith(".meta.json"):
            continue
        meta = path.with_name(path.stem + ".meta.json")
        entries[(path.parent.name, path.stem)] = (
            json.loads(path.read_text()),
            json.loads(meta.read_text()) if meta.exists() else {},
        )
    return entries


def split(entry):
    """The lexeme split as part of speech and gender, in order: what a reader would see as separate words."""
    return sorted(
        (lexeme.get("pos"), (lexeme.get("grammar") or {}).get("gender")) for lexeme in entry.get("lexemes") or []
    )


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--arm", default="v5-evidence")
    parser.add_argument("--reference", default="claude-opus-5-5-low")
    parser.add_argument("--writers", help="writer folders, each optionally arm:writer")
    parser.add_argument("--raw", action="store_true", help="score entries as written, before repair")
    args = parser.parse_args()
    root = HERE / "out" / "grounded" / args.arm
    writers = args.writers.split(",") if args.writers else sorted(p.name for p in root.iterdir() if p.is_dir())
    data = {
        writer: load(HERE / "out" / "grounded" / writer.replace(":", "/") if ":" in writer else root / writer)
        for writer in writers
    }
    if not args.raw:
        data = {
            writer: {
                word: ((entry if "error" in entry else entry_repair.repair(entry)[0]), meta)
                for word, (entry, meta) in entries.items()
            }
            for writer, entries in data.items()
        }
    words = set.intersection(*(set(entries) for entries in data.values()))
    print(f"{len(words)} words every writer has\n")
    reference = data.get(args.reference, {})
    rows = []
    for writer in writers:
        stats = Counter()
        facts = Counter()
        dollars = []
        for word in sorted(words):
            entry, meta = data[writer][word]
            dollars.append(meta.get("usd") or 0)
            stats["output_tokens"] += meta.get("output_tokens") or 0
            if "error" in entry:
                stats["unparsable"] += 1
                continue
            if any(f["severity"] == "error" for f in entry_checks.check(entry)):
                stats["structural"] += 1
            found = wiktionary.evidence(*word)
            senses = [s for lexeme in entry.get("lexemes") or [] for s in lexeme.get("senses") or []]
            stats["senses"] += len(senses)
            stats["cited"] += sum(1 for s in senses if s.get("from"))
            stats["lexemes"] += len(entry.get("lexemes") or [])
            for finding in evidence_checks.compare(entry, found):
                if finding["kind"] == "fact":
                    facts[finding["check"]] += 1
                elif finding["check"] == "lexeme":
                    stats["missed_lexemes"] += 1
            if word in reference and "error" not in reference[word][0]:
                stats["same_split"] += split(entry) == split(reference[word][0])
                stats["compared"] += 1
        n = len(words)
        rows.append((writer, stats, facts, sum(dollars) / n * 1000 if n else 0))
    for writer, stats, facts, per_k in rows:
        print(f"== {writer}")
        print(
            f"  unparsable {stats['unparsable']}, structural errors {stats['structural']}, "
            f"lexemes {stats['lexemes']}, senses {stats['senses']}, cited {stats['cited']}/{stats['senses']}"
        )
        print(f"  facts against evidence: {dict(facts) or 'none'}")
        print(f"  common evidence lexemes left out: {stats['missed_lexemes']}")
        print(f"  lexeme split same as {args.reference}: {stats['same_split']}/{stats['compared']}")
        print(
            f"  ${per_k:.2f} per 1,000 entries at the reported price; "
            f"{stats['output_tokens'] / max(len(words), 1):.0f} output tokens an entry"
        )


if __name__ == "__main__":
    main()
