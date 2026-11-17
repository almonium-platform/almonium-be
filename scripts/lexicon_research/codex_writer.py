"""Write entries through the Codex CLI on the owner's ChatGPT subscription.

One `codex exec` call per headword: a fresh ephemeral session in an empty directory, read-only
sandbox, the bake-off system prompt and the headword in one message, no schema flag (a schema
flag made the Claude CLI write fewer senses, so the output is validated here instead). The JSON
event stream gives the token usage of every call; the owner reads the plan's meter before and
after a batch of known size, which is how subscription cost is measured.

A word that already has an entry from any model is skipped, so no call is spent twice.

Output: out/codex-<model>/<lang>/<headword>.json and .meta.json, the bake-off layout.

An inflected form ("festlichen") is not written either: lemma_gate.py asks the Wiktionary index first.

Usage: codex_writer.py <headwords name> [--model gpt-6.1-sol] [--effort medium] [--langs en,de,uk] [--offset N] [--limit N] [--workers 4] [--no-gate]
       (run from the backend repository root; headwords/<name>-<lang>.json must exist; build the index once with lemma_index.py)
"""
import argparse, hashlib, re, json, os, subprocess, sys, threading, time
from concurrent.futures import ThreadPoolExecutor
from pathlib import Path

sys.path.insert(0, "scripts/lexicon_research")
import lemma_gate
import v3_entries
from prompting import HERE, PROMPT_VERSION, TARGET_LANGUAGES, SCHEMA_VERSION, WRITER_SUFFIX, system_prompt, user_prompt

OUT = HERE / "out"
GATE = True  # --no-gate turns off the Wiktionary check that keeps inflected forms from being written
# the snap codex first on PATH is too old to refresh the login or know current models
CODEX = os.environ.get("CODEX_BIN") or next((str(p) for p in sorted(Path.home().glob(".nvm/versions/node/*/bin/codex"), reverse=True)), "codex")
EMPTY = Path("/tmp/claude-1000/codex-empty")
EMPTY.mkdir(parents=True, exist_ok=True)
# set while the plan refuses calls: the other workers hold until one worker's probe gets through
LIMIT_HIT = threading.Event()
LIMIT_LOCK = threading.Lock()
REWRITE_OLDER = False  # --rewrite-older: write words with an older entry too (the mender's rewrite list)


def has_entry(lang, headword):
    return any((d / f"{headword}.json").exists() for d in OUT.glob(f"*{WRITER_SUFFIX}/{lang}"))


def parse(text):
    t = text.strip()
    if t.startswith("```"):
        t = t.split("\n", 1)[1].rsplit("```", 1)[0]
    try:
        return json.loads(t)
    except json.JSONDecodeError:
        # the model's one habitual slip is a trailing comma before a closing bracket
        return json.loads(re.sub(r",(\s*[}\]])", r"\1", t))


def write_one(model, effort, lang, system, row, shared=False, attempt=1):
    d = OUT / f"codex-{model}-{effort}{WRITER_SUFFIX}" / lang
    # a quality comparison needs the same words another model wrote; everything else skips them
    if (d / f"{row['headword']}.json").exists() or (not shared and has_entry(lang, row["headword"])):
        return "skip"
    # a word with an older entry is mended (mend_v4.py), not bought again
    if WRITER_SUFFIX and not shared and not REWRITE_OLDER and v3_entries.has_older(lang, row["headword"]):
        return "older"
    if lemma_gate.stop(lang, row, f"codex-{model}-{effort}{WRITER_SUFFIX}", GATE):
        return "gated"
    while LIMIT_HIT.is_set():
        time.sleep(60)  # another worker found the plan at its limit and is waiting it out
    d.mkdir(parents=True, exist_ok=True)
    covered = lemma_gate.covered(lang, row["headword"]) if GATE else []
    prompt = f"{system}\n\nDo not run commands or read files; answer from your own knowledge.\n\n{user_prompt(lang, row, covered=covered)}"
    t0 = time.time()
    # no MCP servers: the user's configured ones add tool schemas and startup noise to every call
    try:
        r = subprocess.run([CODEX, "exec", "-m", model, "--skip-git-repo-check", "--ephemeral", "--json", "--sandbox", "read-only",
                            "-c", "mcp_servers={}", "-c", f"model_reasoning_effort={effort}", prompt],
                           capture_output=True, text=True, timeout=900, cwd=EMPTY, stdin=subprocess.DEVNULL)  # an open stdin makes codex wait for it
    except subprocess.TimeoutExpired:
        # a hung call used to end the run; it counts as a call with no answer
        r = subprocess.CompletedProcess([], 1, stdout="", stderr="timed out")
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
    if not text and "usage limit" in error:
        # the plan's wall is waited out, however long, and counts nothing against the word; one worker
        # probes every fifteen minutes while the others hold
        with LIMIT_LOCK:
            if not LIMIT_HIT.is_set():
                print(f"plan limit reached at {row['headword']}: {error}; waiting it out", flush=True)
                LIMIT_HIT.set()
        time.sleep(900)
        LIMIT_HIT.clear()
        return write_one(model, effort, lang, system, row, shared, attempt)
    meta = {"wall_s": wall, "prompt_version": PROMPT_VERSION, "prompt_sha": hashlib.sha256(system.encode("utf-8")).hexdigest()[:12],
            "schema_version": SCHEMA_VERSION, "headword": row["headword"], "set": row["set"], "model": model, "effort": effort,
            "covered": [f"{l} ({p})" for l, p in covered] or None,
            "input_tokens": usage.get("input_tokens"), "cache_read": usage.get("cached_input_tokens"),
            "output_tokens": usage.get("output_tokens"), "reasoning_tokens": usage.get("reasoning_output_tokens")}
    try:
        entry = parse(text) if text else {"error": error or "no agent message", "raw": r.stdout[-3000:]}
    except Exception as e:
        # a malformed answer (a stray comma, a cut-off string) is the model's slip, not the word's: one more try
        if attempt == 1:
            print(f"codex-{model}-{effort} {lang} {row['headword']:20} not JSON, retrying once", flush=True)
            return write_one(model, effort, lang, system, row, shared, attempt=2)
        entry = {"error": f"not JSON: {e}", "raw": text}
    if "error" in entry:
        # kept for a look outside the writer's directory, so the next run writes the word
        failed = OUT / "failed" / d.parent.name / lang
        failed.mkdir(parents=True, exist_ok=True)
        (failed / f"{row['headword']}.json").write_text(json.dumps({**entry, "meta": meta}, ensure_ascii=False, indent=1) + "\n")
        print(f"codex-{model}-{effort} {lang} {row['headword']:20} unusable answer, kept under out/failed/", flush=True)
        return "error"
    (d / f"{row['headword']}.json").write_text(json.dumps(entry, ensure_ascii=False, indent=1) + "\n")
    (d / f"{row['headword']}.meta.json").write_text(json.dumps(meta, ensure_ascii=False, indent=1) + "\n")
    lemma_gate.register(lang, entry)
    status = "error" if "error" in entry else f"{len(entry.get('lexemes', []))} lexemes"
    print(f"codex-{model}-{effort} {lang} {row['headword']:20} {status:12} {wall}s in={meta['input_tokens']} cached={meta['cache_read']} out={meta['output_tokens']} reasoning={meta['reasoning_tokens']}", flush=True)
    return status


ap = argparse.ArgumentParser()
ap.add_argument("name")
ap.add_argument("--model", default="gpt-6.1-sol")
ap.add_argument("--langs", default=",".join(TARGET_LANGUAGES))
ap.add_argument("--effort", default="medium")
ap.add_argument("--offset", type=int, default=0, help="skip this many headwords, so each test cell gets its own words")
ap.add_argument("--limit", type=int, default=0)
ap.add_argument("--only-set", help="keep only headwords whose set has this name, e.g. hard")
ap.add_argument("--shared", action="store_true", help="write words other models already wrote, for a quality comparison")
ap.add_argument("--workers", type=int, default=4)
ap.add_argument("--no-gate", action="store_true", help="write inflected forms too, without asking the Wiktionary index")
ap.add_argument("--rewrite-older", action="store_true", help="write words that have an older entry too (the mender's rewrite list)")
a = ap.parse_args()
for _lang in a.langs.split(","):
    if _lang not in TARGET_LANGUAGES:
        sys.exit(f"{_lang} is not a target language; entries are written for {', '.join(TARGET_LANGUAGES)} only")
REWRITE_OLDER = a.rewrite_older
GATE = not a.no_gate
if GATE:
    lemma_gate.require(a.langs.split(","))
for lang in a.langs.split(","):
    # "bakeoff" names the original shared set, whose files carry no name prefix
    path = HERE / "headwords" / (f"{lang}.json" if a.name == "bakeoff" else f"{a.name}-{lang}.json")
    rows = json.loads(path.read_text())
    if a.only_set:
        rows = [r for r in rows if r["set"] == a.only_set]
    rows = rows[a.offset:]
    if a.limit:
        rows = rows[:a.limit]
    system = system_prompt(lang)
    with ThreadPoolExecutor(a.workers) as ex:
        list(ex.map(lambda r: write_one(a.model, a.effort, lang, system, r, a.shared), rows))
print("done")
