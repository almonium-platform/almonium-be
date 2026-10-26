"""Blind comparison of two writers' entries for the same words: material errors in each, and which serves a learner.

Each pair is shown as A and B in an order fixed per word by a hash, so neither writer is always first. The judge lists
material learner-facing errors only (a wrong meaning, gloss or grammar fact, a common sense missing, an example that
does not show its sense) and then prefers A, B or neither. Results go to out/grounded/pairs-<name>.jsonl; a word
already judged is not judged again.

Usage: blind_pair.py --name NAME --a arm/writer[/run] --b arm/writer --words FILE [--limit 30] [--judge ...]
       A side may be pipeline:<run> to read out/pipeline/<run>/.
"""

import argparse
import hashlib
import json
import sys
from concurrent.futures import ThreadPoolExecutor
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
import entry_repair
import grounded_writer

HERE = Path(__file__).resolve().parent
PROMPT = """You compare two learner-dictionary entries for the same headword, written independently. For each entry
list its material errors: a definition or gloss that is wrong for its sense, a wrong gender, plural, form or
pronunciation, a common everyday sense or lexeme that is missing, a sense that is not real, or an example that does
not show its sense. Ignore style, length and choices a good dictionary could make either way. Then say which entry
would serve a learner better, or "same" when neither is materially better.
Return JSON only: {"a_errors": [<short>], "b_errors": [<short>], "better": "a"|"b"|"same", "why": <short>}"""


def folder(side):
    if side.startswith("pipeline:"):
        return HERE / "out" / "pipeline" / side.split(":", 1)[1]
    return HERE / "out" / "grounded" / side


def entry_at(side, lang, word):
    path = folder(side) / lang / f"{word}.json"
    if not path.exists():
        return None
    entry = json.loads(path.read_text())
    return entry if "error" in entry else entry_repair.repair(entry)[0]


def judge(args, row):
    lang, word = row["language"], row["headword"]
    a, b = entry_at(args.a, lang, word), entry_at(args.b, lang, word)
    if a is None or b is None:
        return None
    flip = int(hashlib.sha1(f"{lang}/{word}".encode()).hexdigest(), 16) % 2 == 1
    first, second = (b, a) if flip else (a, b)
    user = f"Entry A:\n{json.dumps(first, ensure_ascii=False)}\n\nEntry B:\n{json.dumps(second, ensure_ascii=False)}"
    text, meta = grounded_writer.call_openrouter(args.judge, PROMPT, user, "low")
    try:
        verdict = grounded_writer.parse(text)
    except ValueError:
        return None
    if flip:  # back to the caller's a and b
        verdict["a_errors"], verdict["b_errors"] = verdict.get("b_errors", []), verdict.get("a_errors", [])
        verdict["better"] = {"a": "b", "b": "a"}.get(verdict.get("better"), verdict.get("better"))
    return {"language": lang, "headword": word, **verdict, "usd": meta["usd"]}


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--name", required=True)
    parser.add_argument("--a", required=True)
    parser.add_argument("--b", required=True)
    parser.add_argument("--words", required=True)
    parser.add_argument("--limit", type=int, default=30)
    parser.add_argument("--judge", default="claude-sonnet-5-5")
    args = parser.parse_args()
    out = HERE / "out" / "grounded" / f"pairs-{args.name}.jsonl"
    done = (
        {(r["language"], r["headword"]) for r in map(json.loads, out.read_text().splitlines())}
        if out.exists()
        else set()
    )
    rows = [r for r in grounded_writer.words(args.words) if (r["language"], r["headword"]) not in done][: args.limit]
    with ThreadPoolExecutor(4) as pool:
        results = [r for r in pool.map(lambda row: judge(args, row), rows) if r]
    with out.open("a") as file:
        for result in results:
            file.write(json.dumps(result, ensure_ascii=False) + "\n")
    rows = [json.loads(line) for line in out.read_text().splitlines()]
    better = {key: sum(1 for r in rows if r.get("better") == key) for key in ("a", "b", "same")}
    print(
        f"{len(rows)} pairs; better {better}; errors a {sum(len(r.get('a_errors', [])) for r in rows)}, "
        f"b {sum(len(r.get('b_errors', [])) for r in rows)}; ${sum(r['usd'] or 0 for r in rows):.3f}"
    )
    for r in rows:
        if r.get("a_errors") or r.get("b_errors"):
            print(
                f"  {r['language']}/{r['headword']} [{r.get('better')}] a: {'; '.join(r.get('a_errors', []))[:150]}"
                f" | b: {'; '.join(r.get('b_errors', []))[:150]}"
            )


if __name__ == "__main__":
    main()
