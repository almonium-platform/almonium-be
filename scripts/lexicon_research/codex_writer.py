"""Write entries through the Codex CLI on the owner's ChatGPT subscription.

One `codex exec` call per headword: a fresh ephemeral session in an empty directory, read-only
sandbox, the bake-off system prompt and the headword in one message, no schema flag (a schema
flag made the Claude CLI write fewer senses, so the output is validated here instead). The JSON
event stream gives the token usage of every call; the owner reads the plan's meter before and
after a batch of known size, which is how subscription cost is measured.

A word that already has an entry from any model is skipped, so no call is spent twice.

Output: out/codex-<model>/<lang>/<headword>.json and .meta.json, the bake-off layout.

Usage: codex_writer.py <headwords name> [--model gpt-6.1-sol] [--langs en,de,uk] [--limit N] [--workers 4]
       (run from the backend repository root; headwords/<name>-<lang>.json must exist)
"""
import argparse, json, subprocess, sys, time
from concurrent.futures import ThreadPoolExecutor
from pathlib import Path

sys.path.insert(0, "scripts/lexicon_research")
from prompting import HERE, PROMPT_VERSION, system_prompt, user_prompt

OUT = HERE / "out"
EMPTY = Path("/tmp/claude-1000/codex-empty")
EMPTY.mkdir(parents=True, exist_ok=True)


def has_entry(lang, headword):
    return any((d / f"{headword}.json").exists() for d in OUT.glob(f"*/{lang}"))


def parse(text):
    t = text.strip()
    if t.startswith("```"):
        t = t.split("\n", 1)[1].rsplit("```", 1)[0]
    return json.loads(t)


def write_one(model, lang, system, row):
    if has_entry(lang, row["headword"]):
        return "skip"
    d = OUT / f"codex-{model}" / lang
    d.mkdir(parents=True, exist_ok=True)
    prompt = f"{system}\n\nDo not run commands or read files; answer from your own knowledge.\n\n{user_prompt(lang, row)}"
    t0 = time.time()
    r = subprocess.run(["codex", "exec", "-m", model, "--skip-git-repo-check", "--ephemeral", "--json", "--sandbox", "read-only", prompt],
                       capture_output=True, text=True, timeout=900, cwd=EMPTY)
    wall = round(time.time() - t0, 1)
    text, usage, error = "", {}, ""
    for line in r.stdout.splitlines():
        try:
            ev = json.loads(line)
        except json.JSONDecodeError:
            continue
        if ev.get("type") == "item.completed" and (ev.get("item") or {}).get("type") == "agent_message":
            text = ev["item"].get("text", "")
        elif ev.get("type") == "turn.completed":
            usage = ev.get("usage") or {}
        elif ev.get("type") in ("error", "turn.failed"):
            error = str(ev.get("message") or ev.get("error"))[:300]
    meta = {"wall_s": wall, "prompt_version": PROMPT_VERSION, "headword": row["headword"], "set": row["set"], "model": model,
            "input_tokens": usage.get("input_tokens"), "cache_read": usage.get("cached_input_tokens"),
            "output_tokens": usage.get("output_tokens"), "reasoning_tokens": usage.get("reasoning_output_tokens")}
    try:
        entry = parse(text) if text else {"error": error or "no agent message", "raw": r.stdout[-3000:]}
    except Exception as e:
        entry = {"error": f"not JSON: {e}", "raw": text}
    (d / f"{row['headword']}.json").write_text(json.dumps(entry, ensure_ascii=False, indent=1) + "\n")
    (d / f"{row['headword']}.meta.json").write_text(json.dumps(meta, ensure_ascii=False, indent=1) + "\n")
    status = "error" if "error" in entry else f"{len(entry.get('lexemes', []))} lexemes"
    print(f"codex-{model} {lang} {row['headword']:20} {status:12} {wall}s in={meta['input_tokens']} cached={meta['cache_read']} out={meta['output_tokens']} reasoning={meta['reasoning_tokens']}", flush=True)
    return status


ap = argparse.ArgumentParser()
ap.add_argument("name")
ap.add_argument("--model", default="gpt-6.1-sol")
ap.add_argument("--langs", default="en,de,uk")
ap.add_argument("--limit", type=int, default=0)
ap.add_argument("--workers", type=int, default=4)
a = ap.parse_args()
for lang in a.langs.split(","):
    rows = json.loads((HERE / "headwords" / f"{a.name}-{lang}.json").read_text())
    if a.limit:
        rows = rows[:a.limit]
    system = system_prompt(lang)
    with ThreadPoolExecutor(a.workers) as ex:
        list(ex.map(lambda r: write_one(a.model, lang, system, r), rows))
print("done")
