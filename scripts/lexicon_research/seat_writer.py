"""Write entries with any model that sits in a seat (review_v4.py), into a trial folder: a writer on trial.

The plans' writers are claude_code_writer.py and codex_writer.py. This one is for a model paid by the call
(openrouter:<vendor/model>:<effort>, gemini:<model>:<effort>) that is to be held against them on the same words,
under the same prompt, before anyone decides it should write the dictionary. It writes only into a folder whose
name begins review-, which no ingest reads; a word it has written is not bought again, and it stops for good
at --budget-usd, at the stop file, or when the account is empty.

  seat_writer.py --seat openrouter:google/gemini-4-carbon:high --headwords v411e --out review-writer-gemini-4-carbon
                 [--langs en,de] [--words reaction,top] [--limit N] [--budget-usd 5] [--workers 2] [--loose] [--stop-file PATH]

Run from the backend repository's root. --loose puts the schema in the request's words for a provider that will
not hold an answer to it.
"""
import argparse
import hashlib
import json
import sys
import threading
import time
from concurrent.futures import ThreadPoolExecutor
from pathlib import Path

HERE = Path("scripts/lexicon_research")
sys.path.insert(0, str(HERE))
import jsonschema  # noqa: E402
import review_v4  # noqa: E402
from prompting import PROMPT_VERSION, SCHEMA_PATH, SCHEMA_VERSION, TARGET_LANGUAGES, system_prompt, user_prompt  # noqa: E402

OUT = HERE / "out"
SCHEMA = json.loads(SCHEMA_PATH.read_text(encoding="utf-8"))
_lock, _spent, _stop = threading.Lock(), [0.0], []


def write_one(a, lang, system, row):
    folder = OUT / a.out / lang
    folder.mkdir(parents=True, exist_ok=True)
    target = folder / f"{row['headword']}.json"
    if target.exists():
        return "have"
    if _stop or _spent[0] >= a.budget_usd or (a.stop_file and Path(a.stop_file).exists()):
        return "stopped"
    started = time.time()
    try:
        # what the provider let through against the schema is kept and marked: an entry that does not fit is a result
        entry, usage = review_v4.ask(a.seat, user_prompt(lang, row), system, SCHEMA, strict=not a.loose, check=False)
    except review_v4.Wall as wall:
        _stop.append(str(wall)[:120])
        return "wall"
    except RuntimeError as broken:  # not an entry and not a file: the next run asks again
        print(f"{a.out} {lang} {row['headword']:20} {str(broken)[:200]}", flush=True)
        return "error"
    with _lock:
        _spent[0] += usage.get("cost_usd") or 0
    problems = [error.message[:120] for error in jsonschema.Draft202012Validator(SCHEMA).iter_errors(entry)]
    meta = {"wall_s": round(time.time() - started, 1), "prompt_version": PROMPT_VERSION, "prompt_sha": hashlib.sha256(system.encode("utf-8")).hexdigest()[:12],
            "schema_version": SCHEMA_VERSION, "headword": row["headword"], "set": row.get("set"), "seat": a.seat, "model": a.seat.split(":")[1],
            "effort": (a.seat.split(":") + ["medium"])[2], "total_cost_usd": usage.get("cost_usd"), "input_tokens": usage.get("input"),
            "output_tokens": usage.get("output"), "reasoning_tokens": usage.get("reasoning"), "cache_read": usage.get("cache_read"), "schema_problems": problems}
    target.write_text(json.dumps(entry, ensure_ascii=False, indent=1) + "\n", encoding="utf-8")
    target.with_name(target.stem + ".meta.json").write_text(json.dumps(meta, ensure_ascii=False, indent=1) + "\n", encoding="utf-8")
    print(f"{a.out} {lang} {row['headword']:20} {len(entry.get('lexemes') or [])} lexemes {meta['wall_s']:6.1f}s out={usage.get('output')} thought={usage.get('reasoning')} "
          f"cached={usage.get('cache_read')} cost~${usage.get('cost_usd') or 0:.4f}" + (f"  NOT VALID: {problems[0]}" if problems else ""), flush=True)
    return "written"


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--seat", required=True)
    ap.add_argument("--headwords", required=True, help="headwords/<name>-<lang>.json")
    ap.add_argument("--out", required=True)
    ap.add_argument("--langs", default=",".join(TARGET_LANGUAGES))
    ap.add_argument("--words", default="", help="only these headwords of the lists")
    ap.add_argument("--limit", type=int, default=0)
    ap.add_argument("--budget-usd", type=float, default=5.0)
    ap.add_argument("--workers", type=int, default=2)
    ap.add_argument("--max-tokens", type=int, default=32000, help="the longest answer, thinking included")
    ap.add_argument("--loose", action="store_true")
    ap.add_argument("--stop-file", default="")
    a = ap.parse_args()
    if not a.out.startswith("review-"):
        sys.exit("--out takes a folder name that begins review-: any other would be read as the dictionary's entries")
    review_v4.MAX_TOKENS = a.max_tokens
    only, got = set(filter(None, a.words.split(","))), []
    for lang in a.langs.split(","):
        rows = json.loads((HERE / "headwords" / f"{a.headwords}-{lang}.json").read_text(encoding="utf-8"))
        rows = [row for row in rows if not only or row["headword"] in only][:a.limit or None]
        if any(not {"headword", "zipf"} <= set(row) for row in rows):
            sys.exit(f"{a.headwords}-{lang}.json: a row without its headword or zipf")
        system = system_prompt(lang)
        with ThreadPoolExecutor(a.workers) as pool:
            got += list(pool.map(lambda row: write_one(a, lang, system, row), rows))
    print(", ".join(f"{kind} {got.count(kind)}" for kind in sorted(set(got))) + f"; spent ${_spent[0]:.2f} of ${a.budget_usd:.2f}"
          + (f"; stopped at a wall ({_stop[0]})" if _stop else ""))


if __name__ == "__main__":
    main()
