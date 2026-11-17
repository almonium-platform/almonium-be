"""Write entries from Wiktionary evidence, through a paid API, for measured comparisons.

Each word is written once per writer and arm into out/grounded/<arm>/<writer>/<lang>/<headword>.json with a
.meta.json beside it (usage, dollars, evidence size). An arm is a prompt version with evidence on or off, so v4
without evidence and v5 with it can be compared word for word. A word already written is skipped: no call is paid
twice. Claude models go through the Anthropic API with the system prompt cached; any other model through
OpenRouter, whose response states its own cost. The run stops for good once --budget-usd is spent.

Usage: grounded_writer.py --words FILE --arm v5-evidence --writer claude-opus-5-5 [--budget-usd 3] [--workers 4]
       (run from anywhere; FILE is a JSON list of {"language", "headword"} or a calibration file with "cases")
"""

import argparse
import json
import re
import subprocess
import sys
import threading
import time
from concurrent.futures import ThreadPoolExecutor
from pathlib import Path

import requests
from wordfreq import zipf_frequency

sys.path.insert(0, str(Path(__file__).resolve().parent))
import prompting
import wiktionary
from backend_env import dotenv_value

HERE = Path(__file__).resolve().parent
prompting.HERE = HERE
OUT = HERE / "out" / "grounded"
# An arm: the prompt version, whether the evidence is given, whether a reminder follows it.
ARMS = {"v4-plain": ("v4oct", False, False), "v5-evidence": ("v5", True, False), "v5r-evidence": ("v5", True, True), "v6r-evidence": ("v6", True, True)}
# Anthropic list prices per million tokens: input, cache read, cache write, output.
PRICES = {
    "claude-opus-5-5": (4.0, 0.2, 5.0, 20.0),
    "claude-sonnet-5-5": (2.0, 0.2, 2.5, 10.0),
    "claude-haiku-5-5": (0.1, 0.01, 0.125, 0.5),
}
MAX_TOKENS = 16000

VIA = "anthropic"
_lock = threading.Lock()
_spent = 0.0


def words(path):
    data = json.loads(Path(path).read_text())
    rows = data["cases"] if isinstance(data, dict) else data
    seen, result = set(), []
    for row in rows:
        key = (row["language"], row["headword"])
        if key not in seen:
            seen.add(key)
            result.append({"language": row["language"], "headword": row["headword"]})
    return result


def call_anthropic(model, system, user, effort):
    import anthropic

    client = anthropic.Anthropic(api_key=dotenv_value("ANTHROPIC_API_KEY"))
    started = time.time()
    with client.messages.stream(
        model=model,
        max_tokens=MAX_TOKENS,
        system=[{"type": "text", "text": system, "cache_control": {"type": "ephemeral"}}],
        messages=[{"role": "user", "content": user}],
        output_config={"effort": effort},
    ) as stream:
        message = stream.get_final_message()
    usage = message.usage
    tokens = {
        "input_tokens": usage.input_tokens,
        "output_tokens": usage.output_tokens,
        "cache_read": usage.cache_read_input_tokens or 0,
        "cache_write": usage.cache_creation_input_tokens or 0,
    }
    i, r, w, o = PRICES[model]
    cost = (
        tokens["input_tokens"] * i + tokens["cache_read"] * r + tokens["cache_write"] * w + tokens["output_tokens"] * o
    ) / 1e6
    text = "".join(block.text for block in message.content if block.type == "text")
    return text, {
        **tokens,
        "usd": round(cost, 6),
        "latency_s": round(time.time() - started, 1),
        "stop": message.stop_reason,
    }


EMPTY = Path("/tmp/claude-1000/lexicon-writer-empty")
NO_MCP = ["--no-session-persistence", "--strict-mcp-config", "--mcp-config", '{"mcpServers": {}}']


def cli_schema():
    """The entry schema as the Claude CLI's validator takes it: draft-07 shapes, definitions instead of $defs."""
    schema = json.loads((HERE / "entry.schema.json").read_text())
    schema.pop("$schema", None)
    schema["definitions"] = schema.pop("$defs")
    return json.dumps(schema).replace("#/$defs/", "#/definitions/")


def meters():
    """Percent used of the plan's five-hour session and of its week, read from /usage, which costs no tokens."""
    EMPTY.mkdir(parents=True, exist_ok=True)
    result = subprocess.run(
        ["claude", "-p", "--output-format", "json", *NO_MCP],
        input="/usage",
        capture_output=True,
        text=True,
        timeout=120,
        cwd=EMPTY,
        check=False,
    )
    screen = json.loads(result.stdout)["result"]

    def used(label):
        match = re.search(label + r": (\d+)% used", screen)
        return int(match.group(1)) if match else None

    return used("Current session"), used(r"Current week \(all models\)")


def call_plan(model, system, user, effort):
    """One headless Claude CLI call on the owner's plan, from an empty directory so no project file rides along.
    usd is the CLI's list-price figure; plan money is a share of it (0.03 measured)."""
    EMPTY.mkdir(parents=True, exist_ok=True)
    started = time.time()
    command = [
        "claude",
        "-p",
        user,
        "--model",
        model,
        "--effort",
        effort,
        "--tools",
        "",
        "--system-prompt",
        system,
        "--json-schema",
        cli_schema(),
        "--output-format",
        "json",
        *NO_MCP,
    ]
    result = subprocess.run(command, capture_output=True, text=True, timeout=900, cwd=EMPTY, check=False)
    body = json.loads(result.stdout)
    if body.get("is_error"):
        raise RuntimeError((body.get("result") or "")[:200])
    usage = body.get("usage") or {}
    structured = body.get("structured_output")
    text = json.dumps(structured, ensure_ascii=False) if structured is not None else body.get("result") or ""
    return text, {
        "input_tokens": usage.get("input_tokens"),
        "output_tokens": usage.get("output_tokens"),
        "cache_read": usage.get("cache_read_input_tokens"),
        "cache_write": usage.get("cache_creation_input_tokens"),
        "usd": body.get("total_cost_usd"),
        "plan_usd": round((body.get("total_cost_usd") or 0) * 0.03, 6),
        "latency_s": round(time.time() - started, 1),
        "via": "plan",
    }


def openrouter_id(writer):
    """claude-opus-5-5 is anthropic/claude-opus-5.5 on OpenRouter; any other id is OpenRouter's own."""
    if writer.startswith("claude-"):
        family, major, minor = writer.rsplit("-", 2)
        return f"anthropic/{family}-{major}.{minor}"
    return writer


def call_openrouter(model, system, user, effort):
    started = time.time()
    claude = model.startswith("claude-")
    body = {
        "model": openrouter_id(model),
        "messages": [
            # Anthropic models cache only a block marked for it; the system prompt is the same for a language.
            {"role": "system", "content": [{"type": "text", "text": system, "cache_control": {"type": "ephemeral"}}]},
            {"role": "user", "content": user},
        ],
        "max_tokens": MAX_TOKENS,
        "usage": {"include": True},
        "reasoning": {"effort": effort},
    }
    if not claude:
        body["response_format"] = {"type": "json_object"}
    response = requests.post(
        "https://openrouter.ai/api/v1/chat/completions",
        headers={"Authorization": f"Bearer {dotenv_value('OPENROUTER_API_KEY')}"},
        json=body,
        timeout=900,
    )
    response.raise_for_status()
    body = response.json()
    usage = body.get("usage") or {}
    text = body["choices"][0]["message"]["content"] or ""
    return text, {
        "input_tokens": usage.get("prompt_tokens"),
        "output_tokens": usage.get("completion_tokens"),
        "usd": usage.get("cost"),
        "latency_s": round(time.time() - started, 1),
        "stop": body["choices"][0].get("finish_reason"),
        "provider": body.get("provider"),
        "cache_read": (usage.get("prompt_tokens_details") or {}).get("cached_tokens"),
        "via": "openrouter",
    }


def parse(text):
    text = text.strip()
    if text.startswith("```"):
        text = text.split("\n", 1)[1].rsplit("```", 1)[0]
    start, end = text.find("{"), text.rfind("}")
    return json.loads(text[start : end + 1])


def write_one(arm, writer, row, budget, effort="low"):
    global _spent
    version, with_evidence, remind = ARMS[arm]
    lang, headword = row["language"], row["headword"]
    folder = OUT / arm / f"{writer.replace('/', '--')}-{effort}" / lang
    path = folder / f"{headword}.json"
    if path.exists():
        return "kept"
    with _lock:
        if _spent >= budget:
            return "budget"
    evidence = wiktionary.render(wiktionary.evidence(lang, headword)) if with_evidence else None
    zipf = round(zipf_frequency(headword, lang), 2)
    system = prompting.system_prompt(lang, version)
    user = prompting.user_prompt(lang, {"headword": headword, "zipf": zipf}, evidence, remind)
    if VIA == "plan":
        call = call_plan
    else:
        call = call_anthropic if writer.startswith("claude-") and VIA == "anthropic" else call_openrouter
    try:
        text, meta = call(writer, system, user, effort)
    except Exception as error:  # noqa: BLE001 - any transport failure is retried by the next run, never written
        print(f"{arm} {writer} {lang} {headword}: failed {error}", flush=True)
        return "failed"
    with _lock:
        _spent += meta["usd"] or 0
    folder.mkdir(parents=True, exist_ok=True)
    try:
        entry = parse(text)
    except ValueError:
        entry = {"error": "unparsable", "raw": text}
    meta.update(
        {
            "arm": arm,
            "writer": writer,
            "effort": effort,
            "prompt_version": version,
            "zipf": zipf,
            "evidence_chars": len(evidence) if evidence else 0,
        }
    )
    path.write_text(json.dumps(entry, ensure_ascii=False, indent=1))
    path.with_name(f"{headword}.meta.json").write_text(json.dumps(meta, ensure_ascii=False, indent=1))
    print(
        f"{arm} {writer} {lang} {headword}: ${meta['usd']} out={meta['output_tokens']} spent=${_spent:.2f}", flush=True
    )
    return "written"


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--words", required=True)
    parser.add_argument("--arm", choices=ARMS, required=True)
    parser.add_argument("--writer", required=True)
    parser.add_argument("--budget-usd", type=float, default=3.0)
    parser.add_argument("--workers", type=int, default=4)
    parser.add_argument("--effort", choices=["low", "medium", "high", "xhigh"], default="low")
    parser.add_argument("--via", choices=["anthropic", "openrouter", "plan"], default="anthropic")
    args = parser.parse_args()
    global VIA
    VIA = args.via
    rows = words(args.words)
    if rows and args.writer.startswith("claude-"):
        write_one(args.arm, args.writer, rows[0], args.budget_usd, args.effort)  # warms the prompt cache
    with ThreadPoolExecutor(args.workers) as pool:
        results = list(pool.map(lambda row: write_one(args.arm, args.writer, row, args.budget_usd, args.effort), rows))
    print({status: results.count(status) for status in set(results)}, f"spent=${_spent:.2f}")


if __name__ == "__main__":
    main()
