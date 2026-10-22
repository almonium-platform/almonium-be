"""Write entries through the Anthropic API, on prepaid credits, beside the plan writers.

The same Opus model, system prompt and headword lists as claude_code_writer.py, so the entries are
the same entries; only the bill differs. The API is not bound to the plan's five-hour window, so
this adds throughput on top of the writers. Each entry is one streamed Messages call with the
system prompt cached; the usage comes back on the message and is priced here at list, and the run
stops for good once the budget is spent.

Output goes into out/cc-claude-opus-5-5/<lang>/ like the CLI writer's, with "via": "api" and the
cost in the meta, so publish_entries.py and the CLI writer's skip both see it. A word either writer
has written is skipped, so the two can run at once on the same list.

Usage: api_writer.py --headwords NAME --langs fr [--model claude-opus-5-5] [--workers 6]
           [--budget-usd 240] [--limit N]        (run from the backend repository root)
"""
import argparse, json, sys, threading, time
from concurrent.futures import ThreadPoolExecutor
from pathlib import Path

sys.path.insert(0, "scripts/lexicon_research")
import jsonschema
from prompting import HERE, PROMPT_VERSION, system_prompt, user_prompt

OUT = HERE / "out"
ROOT = Path(".")
ENV = {l.split("=", 1)[0]: l.split("=", 1)[1].strip() for l in (ROOT / ".env").read_text().splitlines() if "=" in l and not l.startswith("#")}
SCHEMA = json.loads((HERE / "entry.schema.json").read_text())
# list prices per million tokens: input, cache read, cache write, output
PRICES = {"claude-opus-5-5": (4.0, 0.4, 5.0, 20.0), "claude-sonnet-5-5": (3.0, 0.3, 3.75, 15.0)}

_lock = threading.Lock()
_spent = 0.0
_stopped = False


def cost_usd(model, u):
    i, r, w, o = PRICES[model]
    return (u["input_tokens"] * i + u["cache_read"] * r + u["cache_write"] * w + u["output_tokens"] * o) / 1e6


def call(client, model, system, user):
    t0 = time.time()
    with client.messages.stream(model=model, max_tokens=16000,
                                system=[{"type": "text", "text": system, "cache_control": {"type": "ephemeral"}}],
                                output_config={"effort": "high"},
                                messages=[{"role": "user", "content": user}]) as s:
        m = s.get_final_message()
    u = m.usage
    usage = {"input_tokens": u.input_tokens, "output_tokens": u.output_tokens,
             "cache_read": getattr(u, "cache_read_input_tokens", 0) or 0, "cache_write": getattr(u, "cache_creation_input_tokens", 0) or 0}
    text = "".join(b.text for b in m.content if b.type == "text")
    return text, {"latency_s": round(time.time() - t0, 1), **usage, "stop_reason": m.stop_reason, "model": m.model}


def parse(text):
    t = text.strip()
    if t.startswith("```"):
        t = t.split("\n", 1)[1].rsplit("```", 1)[0]
    entry = json.loads(t)
    jsonschema.validate(entry, SCHEMA)
    return entry


def write_one(client, model, lang, system, row, budget):
    global _spent, _stopped
    d = OUT / "cc-claude-opus-5-5" / lang
    d.mkdir(parents=True, exist_ok=True)
    f = d / f"{row['headword']}.json"
    if f.exists():
        return "skip"
    with _lock:
        if _stopped or _spent >= budget:
            _stopped = True
            return "stopped"
    text, meta, entry = "", {}, None
    for attempt in range(3):
        try:
            text, meta = call(client, model, system, user_prompt(lang, row))
            with _lock:
                _spent += cost_usd(model, meta)
            entry = parse(text)
            break
        except (json.JSONDecodeError, jsonschema.ValidationError) as e:
            entry = {"error": f"not valid: {str(e)[:200]}", "raw": text}
        except Exception as e:  # rate limits, transient errors
            entry = {"error": f"{type(e).__name__}: {str(e)[:300]}"}
            time.sleep(15 * (attempt + 1))
    meta.update({"prompt_version": PROMPT_VERSION, "headword": row["headword"], "set": row["set"], "model": model, "effort": "high",
                 "via": "api", "cost_usd": round(cost_usd(model, meta), 5) if "input_tokens" in meta else None})
    f.write_text(json.dumps(entry, ensure_ascii=False, indent=1) + "\n")
    (d / f"{row['headword']}.meta.json").write_text(json.dumps(meta, ensure_ascii=False, indent=1) + "\n")
    status = "error" if "error" in entry else f"{len(entry.get('lexemes', []))} lexemes"
    print(f"api-{model:16} {lang} {row['headword']:22} {status:12} {meta.get('latency_s', '?')}s cache_read={meta.get('cache_read', 0)} "
          f"out={meta.get('output_tokens', '?')} cost=${meta.get('cost_usd')} spent=${_spent:.2f}", flush=True)
    return status


ap = argparse.ArgumentParser()
ap.add_argument("--headwords", required=True, help="a tranche name: reads headwords/<name>-<lang>.json")
ap.add_argument("--langs", default="fr")
ap.add_argument("--model", default="claude-opus-5-5")
ap.add_argument("--workers", type=int, default=6)
ap.add_argument("--budget-usd", type=float, default=240.0, help="stop for good once this much is spent at list price")
ap.add_argument("--limit", type=int, default=0)
a = ap.parse_args()
import anthropic  # noqa: E402

client = anthropic.Anthropic(api_key=ENV["ANTHROPIC_API_KEY"], max_retries=3)
for lang in a.langs.split(","):
    rows = json.loads((HERE / "headwords" / f"{a.headwords}-{lang}.json").read_text())
    if a.limit:
        rows = rows[:a.limit]
    system = system_prompt(lang)
    pending = [r for r in rows if not (OUT / "cc-claude-opus-5-5" / lang / f"{r['headword']}.json").exists()]
    if not pending:
        continue
    # the first call warms the prompt cache before the rest run in parallel
    write_one(client, a.model, lang, system, pending[0], a.budget_usd)
    with ThreadPoolExecutor(a.workers) as ex:
        list(ex.map(lambda r: write_one(client, a.model, lang, system, r, a.budget_usd), pending[1:]))
    if _stopped:
        print(f"budget of ${a.budget_usd} reached; ${_spent:.2f} spent", flush=True)
        break
print(f"done; ${_spent:.2f} spent", flush=True)
