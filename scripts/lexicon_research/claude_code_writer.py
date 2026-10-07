"""Write entries through the Claude Code CLI on the owner's subscription instead of the API.

Each entry is one `claude -p` call: a fresh headless session with no tools and the same system
prompt as the bake-off, so nothing of any other conversation is in context. The CLI's JSON output
gives the usage and the wall time of every call, which is how subscription cost is measured: the
owner reads the weekly meter in /usage before and after a batch of known size.

The subscription may be used this way only through the claude CLI itself, not through the Agent
SDK or another harness.

Output: out/cc-<model>/<lang>/<headword>.json and .meta.json, the same layout as the bake-off,
so score.py and judge.py read it unchanged.

A long run paces itself by the plan's own meters, read from the CLI's /usage screen: it waits
while the five-hour session is nearly used up, so the owner's other sessions are not locked
out, and stops for good at a share of the week.

Usage: claude_code_writer.py [--model opus] [--effort high] [--langs en,de,uk] [--limit N] [--workers 2]
           [--headwords NAME] [--pause-session 70] [--stop-week 0]
       (run from the backend repository root; --headwords NAME reads headwords/NAME-<lang>.json)
"""
import argparse, json, re, subprocess, sys, threading, time
from concurrent.futures import ThreadPoolExecutor
from pathlib import Path

sys.path.insert(0, "scripts/lexicon_research")
from prompting import HERE, PROMPT_VERSION, system_prompt, user_prompt

OUT = HERE / "out"
EMPTY = Path("/tmp/claude-1000/lexicon-writer-empty")
EMPTY.mkdir(parents=True, exist_ok=True)
# the CLI's validator takes draft-07 shapes: no $schema keyword, definitions instead of $defs
_schema = json.loads((HERE / "entry.schema.json").read_text())
_schema.pop("$schema", None)
_schema["definitions"] = _schema.pop("$defs")
SCHEMA = json.dumps(_schema).replace("#/$defs/", "#/definitions/")


_gate, _checked, _stopped = threading.Lock(), 0.0, False


def meters():
    """Percent used of the five-hour session and of the week, as /usage shows them. No tokens."""
    r = subprocess.run(["claude", "-p", "--output-format", "json", "--no-session-persistence", "--strict-mcp-config",
                        "--mcp-config", "{\"mcpServers\": {}}"], input="/usage", capture_output=True, text=True, timeout=120, cwd=EMPTY)
    screen = json.loads(r.stdout)["result"]
    used = lambda label: int(m.group(1)) if (m := re.search(label + r": (\d+)% used", screen)) else None
    return used("Current session"), used(r"Current week \(all models\)")


def allowed(pause_session, stop_week):
    """False once the week's share is reached. Waits, holding every worker, while the session is nearly used up."""
    global _checked, _stopped
    with _gate:
        if _stopped:
            return False
        if time.time() - _checked < 180:
            return True
        while True:
            try:
                session, week = meters()
            except Exception:
                session = week = None  # a meter that cannot be read does not stop the run
            _checked = time.time()
            if stop_week and week is not None and week >= stop_week:
                print(f"stopping: {week}% of the week is used", flush=True)
                _stopped = True
                return False
            if session is not None and session >= pause_session:
                print(f"waiting: {session}% of the session is used", flush=True)
                time.sleep(300)
                continue
            return True


def write_one(model, effort, lang, system, row, pause_session=70, stop_week=0):
    d = OUT / f"cc-{model}" / lang
    d.mkdir(parents=True, exist_ok=True)
    f = d / f"{row['headword']}.json"
    if f.exists():
        return "skip"
    if not allowed(pause_session, stop_week):
        return "stopped"
    t0 = time.time()
    cmd = ["claude", "-p", user_prompt(lang, row), "--model", model, "--effort", effort, "--tools", "",
           "--system-prompt", system, "--json-schema", SCHEMA, "--output-format", "json", "--no-session-persistence",
           "--strict-mcp-config", "--mcp-config", "{\"mcpServers\": {}}"]  # no MCP servers: their tool schemas would ride along in every call
    # run from an empty directory so no CLAUDE.md, AGENTS.md, memory or project settings are loaded
    r = subprocess.run(cmd, capture_output=True, text=True, timeout=600, cwd=EMPTY)
    wall = round(time.time() - t0, 1)
    meta = {"wall_s": wall, "prompt_version": PROMPT_VERSION, "headword": row["headword"], "set": row["set"], "model": model, "effort": effort}
    entry = None
    try:
        res = json.loads(r.stdout)
        meta.update({k: res.get(k) for k in ("duration_ms", "duration_api_ms", "num_turns", "total_cost_usd", "is_error", "subtype")})
        u = res.get("usage") or {}
        meta.update({"input_tokens": u.get("input_tokens"), "output_tokens": u.get("output_tokens"),
                     "cache_read": u.get("cache_read_input_tokens"), "cache_write": u.get("cache_creation_input_tokens")})
        structured = res.get("structured_output")
        text = res.get("result") or ""
        if res.get("is_error") and "limit" in text.lower():
            # the plan's limit: the headword is not spent on an error, the worker waits it out
            print(f"limit reached at {row['headword']}; waiting ten minutes", flush=True)
            time.sleep(600)
            return write_one(model, effort, lang, system, row, pause_session, stop_week)
        entry = structured if structured is not None else json.loads(text.strip().strip("`").removeprefix("json"))
    except Exception as e:
        entry = {"error": f"{type(e).__name__}: {str(e)[:200]}", "raw": (r.stdout or "")[:20000], "stderr": (r.stderr or "")[-2000:]}
    f.write_text(json.dumps(entry, ensure_ascii=False, indent=1) + "\n")
    (d / f"{row['headword']}.meta.json").write_text(json.dumps(meta, ensure_ascii=False, indent=1) + "\n")
    status = "error" if "error" in entry else f"{len(entry.get('lexemes', []))} lexemes"
    print(f"cc-{model:6} {lang} {row['headword']:22} {status:12} {wall}s out={meta.get('output_tokens')} cache_read={meta.get('cache_read')} cost~${meta.get('total_cost_usd')}", flush=True)
    return status


ap = argparse.ArgumentParser()
ap.add_argument("--model", default="opus")
ap.add_argument("--effort", default="high")
ap.add_argument("--langs", default="en,de,uk")
ap.add_argument("--limit", type=int, default=0)
ap.add_argument("--workers", type=int, default=2)
ap.add_argument("--headwords", default="", help="a tranche name: reads headwords/<name>-<lang>.json")
ap.add_argument("--pause-session", type=int, default=70, help="wait while this percent of the 5-hour session is used")
ap.add_argument("--stop-week", type=int, default=0, help="stop for good at this percent of the week; 0 never stops")
a = ap.parse_args()
for lang in a.langs.split(","):
    name = f"{a.headwords}-{lang}.json" if a.headwords else f"{lang}.json"
    rows = json.loads((HERE / "headwords" / name).read_text())
    if a.limit:
        rows = rows[:a.limit]
    system = system_prompt(lang)
    with ThreadPoolExecutor(a.workers) as ex:
        list(ex.map(lambda r: write_one(a.model, a.effort, lang, system, r, a.pause_session, a.stop_week), rows))
print("done")
