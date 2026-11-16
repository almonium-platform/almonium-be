"""Where the dictionary stands, as one JSON file the book processor's Operations page shows.

For every language: v4 entries written and rejected, by writer (model and prompt), the faults
check_v4.py finds, senses per lexeme, entries written in the last day, the words of the queue no
writer has tried yet, the words with only an older (v3) entry still to be mended, and unusable
answers kept under out/failed/. For every writer directory: model, effort, prompt and schema
version, entries, cost and output tokens per entry, and when it first and last wrote.

check_v4 is run once per file and remembered by the file's modification time, so a rerun reads only
what changed. Nothing here calls a model.

Output: out/status/status.json (out/status/cache.json holds the remembered checks).

Usage: lexicon_status.py [--queue v4-new]   (run from the backend repository root; the host timer
       runs it every quarter of an hour, see the book processor's RUNBOOK)
"""
import argparse, json, re, statistics, sys, time
from collections import Counter, defaultdict
from datetime import datetime, timedelta, timezone
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
import check_v4  # noqa: E402
import v3_entries  # noqa: E402
from prompting import HERE, REGIONS  # noqa: E402

OUT = HERE / "out"
STATUS = OUT / "status"


def fault_kind(problem):
    return problem.split(":")[0] if ":" in problem else re.sub(r"\d+", "N", problem)


def read_entry(path, lang, cache):
    """(rejected, senses per lexeme, fault kinds) of one entry, from the cache when the file has not changed."""
    key, mtime = str(path), path.stat().st_mtime
    hit = cache.get(key)
    if hit and hit["mtime"] == mtime:
        return hit
    entry = json.loads(path.read_text())
    rejected = bool(entry.get("reject")) and not entry.get("lexemes")
    hit = {"mtime": mtime, "rejected": rejected,
           "senses": [len(x.get("senses", [])) for x in entry.get("lexemes", [])],
           "faults": sorted({fault_kind(p) for p in check_v4.check(lang, entry)})}
    cache[key] = hit
    return hit


def build(queue):
    STATUS.mkdir(parents=True, exist_ok=True)
    cache_path = STATUS / "cache.json"
    cache = json.loads(cache_path.read_text()) if cache_path.exists() else {}
    now = datetime.now(timezone.utc)
    day_ago = (now - timedelta(days=1)).timestamp()
    languages, writers = [], defaultdict(lambda: {"entries": 0, "rejected": 0, "costs": [], "out": [], "models": Counter(),
                                                   "efforts": Counter(), "prompts": Counter(), "schemas": Counter(),
                                                   "first": None, "last": None})
    for lang in REGIONS:
        row = {"lang": lang, "entries": 0, "rejected": 0, "by_writer": Counter(), "faults": Counter(), "with_faults": 0,
               "last_day": 0, "senses": []}
        written = set()
        for folder in sorted(OUT.glob(f"*-v4/{lang}")):
            writer = folder.parent.name
            for path in folder.glob("*.json"):
                if path.name.endswith(".meta.json"):
                    continue
                written.add(path.stem)
                seen = read_entry(path, lang, cache)
                row["entries"] += 1
                row["by_writer"][writer] += 1
                row["rejected"] += seen["rejected"]
                row["senses"] += seen["senses"]
                row["faults"].update(seen["faults"])
                row["with_faults"] += bool(seen["faults"])
                row["last_day"] += seen["mtime"] >= day_ago
                w = writers[writer]
                w["entries"] += 1
                w["rejected"] += seen["rejected"]
                w["first"] = min(w["first"] or seen["mtime"], seen["mtime"])
                w["last"] = max(w["last"] or seen["mtime"], seen["mtime"])
                meta_path = path.with_name(f"{path.stem}.meta.json")
                if meta_path.exists():
                    meta = cache.get(str(meta_path))
                    if not meta or meta["mtime"] != meta_path.stat().st_mtime:
                        m = json.loads(meta_path.read_text())
                        meta = {"mtime": meta_path.stat().st_mtime, "model": m.get("model"), "effort": m.get("effort"),
                                "prompt": m.get("prompt_version"), "schema": m.get("schema_version"),
                                "cost": m.get("total_cost_usd"), "out": m.get("output_tokens")}
                        cache[str(meta_path)] = meta
                    for key, counter in (("model", "models"), ("effort", "efforts"), ("prompt", "prompts"), ("schema", "schemas")):
                        if meta.get(key):
                            w[counter][str(meta[key])] += 1
                    if meta.get("cost"):
                        w["costs"].append(meta["cost"])
                    if meta.get("out"):
                        w["out"].append(meta["out"])
        queue_file = HERE / "headwords" / f"{queue}-{lang}.json"
        queued = [r["headword"] for r in json.loads(queue_file.read_text())] if queue_file.exists() else []
        row["queue"] = len(queued)
        row["queue_left"] = sum(1 for h in queued if h not in written)
        row["to_mend"] = len(v3_entries.headwords(lang) - written)
        failed = OUT / "failed"
        row["failed"] = sum(1 for _ in failed.glob(f"*/{lang}/*.json")) if failed.is_dir() else 0
        senses = row.pop("senses")
        row["senses_per_lexeme"] = round(statistics.mean(senses), 2) if senses else None
        row["by_writer"] = dict(row["by_writer"].most_common())
        row["faults"] = dict(row["faults"].most_common())
        languages.append(row)
    stamp = lambda t: datetime.fromtimestamp(t, timezone.utc).isoformat() if t else None
    top = lambda c: c.most_common(1)[0][0] if c else None
    status = {
        "built_at": now.isoformat(),
        "queue": queue,
        "languages": languages,
        "writers": [{"writer": name, "entries": w["entries"], "rejected": w["rejected"], "model": top(w["models"]),
                     "effort": top(w["efforts"]), "prompt_version": top(w["prompts"]), "schema_version": top(w["schemas"]),
                     "list_usd_per_entry": round(statistics.mean(w["costs"]), 4) if w["costs"] else None,
                     "output_tokens_per_entry": round(statistics.mean(w["out"])) if w["out"] else None,
                     "first": stamp(w["first"]), "last": stamp(w["last"])}
                    for name, w in sorted(writers.items(), key=lambda kv: -kv[1]["entries"])],
    }
    cache_path.write_text(json.dumps(cache))
    target = STATUS / "status.json"
    partial = target.with_suffix(".json.part")
    partial.write_text(json.dumps(status, ensure_ascii=False, indent=1))
    partial.replace(target)  # the page never reads half a file
    return status


if __name__ == "__main__":
    ap = argparse.ArgumentParser()
    ap.add_argument("--queue", default="v4-new", help="the headword lists whose untried words are counted")
    a = ap.parse_args()
    t0 = time.time()
    s = build(a.queue)
    print(f"{sum(l['entries'] for l in s['languages'])} entries in {len(s['languages'])} languages, "
          f"{len(s['writers'])} writers; {time.time() - t0:.1f}s -> {STATUS / 'status.json'}")
