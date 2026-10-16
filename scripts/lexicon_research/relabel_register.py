"""Relabel the register of every sense of entries already written, with the closed label set of prompt v3.

A cheap pass: the model sees only each sense's definition and example and returns a label per sense,
eight entries per call, so the write stays as it was and only "register" (and an empty "usage") change.
Usage: relabel_register.py [--model claude-opus-5-5] [--effort medium] [--langs fr,es] [--workers 8] [--limit N]
(run from the backend repository root)
"""
import argparse, json, re, subprocess, sys, threading, time
from concurrent.futures import ThreadPoolExecutor
from pathlib import Path

sys.path.insert(0, "scripts/lexicon_research")
from prompting import HERE, LANG_NAME

OUT = HERE / "out"
EMPTY = Path("/tmp/claude-1000/lexicon-writer-empty")
EMPTY.mkdir(parents=True, exist_ok=True)
VERSION = "v3"
REGISTERS = json.loads((HERE / "entry.schema.json").read_text())["$defs"]["sense"]["properties"]["register"]["enum"]
RULE = next(p for p in (HERE / "prompts" / "entry_writer" / "v3.md").read_text().split("\n\n") if p.startswith("Register."))
SCHEMA = json.dumps({"type": "object", "required": ["labels"], "additionalProperties": False, "properties": {"labels": {"type": "array", "items": {
    "type": "object", "required": ["id", "register"], "additionalProperties": False, "properties": {
        "id": {"type": "string"}, "register": {"type": "string", "enum": REGISTERS}, "usage": {"type": "string", "maxLength": 60}}}}}})


def system(lang):
    return (f"You are the editor of a learner's dictionary of {LANG_NAME[lang]}. For each sense below, given its lemma, part of speech, definition "
            f"and example, choose the register label a learner dictionary would print, and where the label is technical, or a region or "
            f"construction is essential to the sense, a short printed usage label. Return every id you were given, once.\n\n{RULE}")


_gate, _checked = threading.Lock(), 0.0


def allowed(pause_session):
    global _checked
    with _gate:
        if time.time() - _checked < 180:
            return True
        while True:
            try:
                r = subprocess.run(["claude", "-p", "--output-format", "json", "--no-session-persistence", "--strict-mcp-config", "--mcp-config",
                                    "{\"mcpServers\": {}}"], input="/usage", capture_output=True, text=True, timeout=120, cwd=EMPTY)
                m = re.search(r"Current session: (\d+)% used", json.loads(r.stdout)["result"])
                session = int(m.group(1)) if m else None
            except Exception:
                session = None
            _checked = time.time()
            if session is not None and session >= pause_session:
                print(f"waiting: {session}% of the session is used", flush=True)
                time.sleep(300)
                continue
            return True


def senses_of(path):
    e = json.loads(path.read_text())
    rows = []
    for i, lx in enumerate(e.get("lexemes", [])):
        for j, s in enumerate(lx.get("senses", [])):
            rows.append({"id": f"{path.stem}/{i}/{j}", "lemma": lx.get("lemma"), "pos": lx.get("pos"), "definition": s.get("definition"),
                         "example": s.get("example"), "current": s.get("register")})
    return e, rows


def relabel(model, effort, lang, batch, pause_session):
    if not allowed(pause_session):
        return
    loaded = {p: senses_of(p) for p in batch}
    rows = [r for _, (_, rs) in loaded.items() for r in rs]
    if not rows:
        return
    t0 = time.time()
    cmd = ["claude", "-p", json.dumps(rows, ensure_ascii=False), "--model", model, "--effort", effort, "--tools", "", "--system-prompt", system(lang),
           "--json-schema", SCHEMA, "--output-format", "json", "--no-session-persistence", "--strict-mcp-config", "--mcp-config", "{\"mcpServers\": {}}"]
    r = subprocess.run(cmd, capture_output=True, text=True, timeout=600, cwd=EMPTY)
    try:
        res = json.loads(r.stdout)
        if res.get("is_error") and "limit" in (res.get("result") or "").lower():
            print("limit reached; waiting ten minutes", flush=True)
            time.sleep(600)
            return relabel(model, effort, lang, batch, pause_session)
        labels = {x["id"]: x for x in res["structured_output"]["labels"]}
    except Exception as e:
        print(f"error on {[p.stem for p in batch]}: {type(e).__name__} {str(e)[:120]} | {(r.stdout or '')[:200]}", flush=True)
        return
    changed = 0
    for p, (e, rs) in loaded.items():
        for row in rs:
            lab = labels.get(row["id"])
            if not lab:
                continue
            _, i, j = row["id"].rsplit("/", 2)
            s = e["lexemes"][int(i)]["senses"][int(j)]
            changed += s.get("register") != lab["register"]
            s["register"] = lab["register"]
            if lab.get("usage") and not s.get("usage"):
                s["usage"] = lab["usage"]
        p.write_text(json.dumps(e, ensure_ascii=False, indent=1) + "\n")
        m = p.with_name(p.stem + ".meta.json")
        meta = json.loads(m.read_text()) if m.exists() else {}
        meta["register_version"] = VERSION
        meta.setdefault("relabel_cost_usd", 0)
        meta["relabel_cost_usd"] += (res.get("total_cost_usd") or 0) / len(batch)
        m.write_text(json.dumps(meta, ensure_ascii=False, indent=1) + "\n")
    print(f"{lang} {len(batch)} entries {len(rows)} senses, {changed} labels changed, {round(time.time() - t0, 1)}s, cost~${res.get('total_cost_usd')}", flush=True)


if __name__ == "__main__":
    ap = argparse.ArgumentParser()
    ap.add_argument("--model", default="claude-opus-5-5")
    ap.add_argument("--effort", default="medium")
    ap.add_argument("--langs", default="en,de,uk,fr,es,it,pt")
    ap.add_argument("--writer", default="cc-claude-opus-5-5", help="which writer's output directory to relabel")
    ap.add_argument("--workers", type=int, default=8)
    ap.add_argument("--batch", type=int, default=8)
    ap.add_argument("--limit", type=int, default=0)
    ap.add_argument("--pause-session", type=int, default=80)
    a = ap.parse_args()
    for lang in a.langs.split(","):
        todo = []
        for p in sorted((OUT / a.writer / lang).glob("*.json")):
            if p.name.endswith(".meta.json"):
                continue
            m = p.with_name(p.stem + ".meta.json")
            if m.exists() and json.loads(m.read_text()).get("register_version") == VERSION:
                continue
            if "lexemes" in json.loads(p.read_text()):
                todo.append(p)
        if a.limit:
            todo = todo[:a.limit]
        batches = [todo[i:i + a.batch] for i in range(0, len(todo), a.batch)]
        print(f"{lang}: {len(todo)} entries in {len(batches)} calls", flush=True)
        with ThreadPoolExecutor(a.workers) as ex:
            list(ex.map(lambda b: relabel(a.model, a.effort, lang, b, a.pause_session), batches))
    print("done")
