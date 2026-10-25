"""Whole-entry review experiment, using plan CLIs only (no model API fallback).

Run from any directory with Python, jsonschema and the signed-in CLIs available:
  python scripts/lexicon_research/review_pilot.py generate --limit 8 --workers 2
  python scripts/lexicon_research/review_pilot.py review --limit 8 --workers 2
  python scripts/lexicon_research/review_pilot.py report

Each request is bought once by provider/model/effort, exact prompts and schema.
Pricing, run names and file paths do not enter that identity. Raw CLI output is
saved before parsing, so an interrupted parser can adopt the paid answer. An
ambiguous interruption, refusal or malformed answer is held, never repurchased
automatically. This experiment does not publish entries or alter the writer.
"""

import argparse
import fcntl
import hashlib
import json
import os
import re
import shutil
import subprocess
import tempfile
import threading
from concurrent.futures import ThreadPoolExecutor
from contextlib import contextmanager
from datetime import UTC, datetime
from pathlib import Path

import jsonschema
import prompting

HERE = Path(__file__).resolve().parent
prompting.HERE = HERE
OUT = HERE / "out" / "review-pilot-v1"
MODELS = {
    "opus": ("claude", "claude-opus-5-5", "high"),
    "sol": ("codex", "gpt-6.1-sol", "medium"),
    "astra": ("codex", "gpt-6-astra", "medium"),
}
# Owner's 2026-10-15 measurement, documented in local memory on 2026-10-16.
# These are allocated subscription cost estimates, not incremental API bills.
PLAN_SHARE = {"claude": 0.03, "codex": 0.07}
PRICES = {"opus": (4, 0.2, 20), "sol": (2, 0.1, 10), "astra": (10, 1, 50)}
AREAS = [
    "lemma_and_lexemes",
    "sense_coverage",
    "definitions",
    "glosses",
    "examples_and_cues",
    "grammar_and_forms",
    "pronunciation",
    "register_and_regions",
    "level_and_relations",
]
LIMIT_RE = re.compile(
    r"usage limit|limit reached|usage_limit|quota|hit your (?:weekly |session )?limit"
    r"|out of credits",
    re.I,
)


def obj(properties):
    return {
        "type": "object",
        "additionalProperties": False,
        "required": list(properties),
        "properties": properties,
    }


def enum(*values):
    return {"type": "string", "enum": list(values)}


STRING = {"type": "string"}
FINDING = obj(
    {
        "id": STRING,
        "area": enum(*AREAS),
        "severity": enum("material", "minor", "uncertain"),
        "action": enum("correct", "add", "split", "merge", "remove", "investigate"),
        "path": STRING,
        "claim": STRING,
        "reason": STRING,
        "proposed_change": STRING,
        "needs_external_evidence": {"type": "boolean"},
    }
)
REVIEW_SCHEMA = obj(
    {
        "verdict": enum("pass", "revise", "uncertain"),
        "checks": obj({area: enum("pass", "issue", "uncertain") for area in AREAS}),
        "findings": {"type": "array", "items": FINDING, "maxItems": 12},
        "coverage_note": STRING,
    }
)
REVIEW_PROMPT = """You are independently reviewing a whole learner-dictionary entry.
The author and other reviewers are hidden. Review every area in the output schema.
Check lemma validity and homographs; common and useful missing senses; spurious,
over-split or merged senses; factual, non-circular, appropriately broad definitions;
gloss accuracy; natural examples that actually demonstrate their sense; equivalent
cues; grammatical gender, valency, countability and forms; IPA, stress, variety and
pronunciation-to-sense association; register, regional scope, CEFR and relations.
Enrichment is allowed: propose useful missing content, not just repairs. Do not
reward length or demand every historical/technical sense. Ordinary national-variety
senses count as ordinary reading, not excluded dialect trivia. Regional evidence
does not automatically mean 'only'; missing labels are not evidence of universality.
The current output schema cannot express everything. Flag representation limitations
instead of inventing fields or pretending a fact is false because it cannot fit.
Distinguish material learner-facing errors from editorial preference and reasonable
IPA conventions. French citation stress marks required by this house style are not
by themselves a lexical-stress error. Minor syllabification differences are not
material unless they change the sounds. Don't invent errors to fill the checklist.
Use only the supplied entry and your knowledge; no tools, commands or browsing.
Return JSON matching the supplied schema. At most 12 actionable findings, concise
reasons and concrete proposed changes. A finding path is a JSON Pointer into the
entry, or the parent array for a missing item. Assign unique IDs F1, F2, etc.
When uncertain, say so. 'pass' means no material issue identified, not proven correct.
"""

AMEND_SCHEMA = obj(
    {
        "decisions": {
            "type": "array",
            "items": obj(
                {
                    "reviewer": enum("R1", "R2"),
                    "finding_id": STRING,
                    "decision": enum("accept", "reject", "uncertain"),
                    "reason": STRING,
                    "source_ids": {"type": "array", "items": STRING},
                }
            ),
        },
        "checkpoint_results": {
            "type": "array",
            "items": obj(
                {
                    "id": STRING,
                    "before": enum("met", "missed", "wrong", "uncertain"),
                    "after": enum("met", "missed", "wrong", "uncertain"),
                    "reason": STRING,
                }
            ),
        },
        "additional_changes": {"type": "array", "items": STRING},
        "unresolved": {"type": "array", "items": STRING},
        "entry_json": STRING,
    }
)
AMEND_PROMPT = """You are the evidence-aware editor of a learner dictionary.
Review the entire supplied entry and BOTH blind reviews, even if they agree or pass.
Decide on EVERY finding using IDs R1/F1 etc. Reviewers and external dictionaries
can be wrong or incomplete. Source notes are limited observations, not exhaustive
truth: absence of a sense is never evidence that it is false. Distinguish standard
national varieties from obscure dialect trivia. Never convert 'mainly' to 'only'.
For each curated checkpoint assess the original and revised entry separately.
Reject stylistic preferences and unsupported additions; explicitly retain uncertainty.
You may enrich missing senses, split/merge mistaken senses, or correct any field.
Every new definition, example, cue and gloss must be your original wording. Preserve
correct existing content. Make the smallest coherent changes and repair structural
schema errors without changing linguistic content unnecessarily. If the schema cannot
represent a distinction, report that limitation instead of adding unsupported fields.
Return the full revised entry encoded as entry_json, obeying the supplied entry schema.
Return a decision for every supplied finding exactly once, cite source IDs only when
their supplied evidence actually supports the decision, and list unresolved issues.
No tools, commands, web browsing, or invented citations. No further correction loop.
"""


def canonical(value):
    return json.dumps(value, ensure_ascii=False, sort_keys=True, separators=(",", ":"))


def request_key(request):
    return hashlib.sha256(canonical(request).encode()).hexdigest()


def save(path, value):
    path.parent.mkdir(parents=True, exist_ok=True)
    with tempfile.NamedTemporaryFile(mode="w", dir=path.parent, delete=False) as f:
        json.dump(value, f, ensure_ascii=False, indent=2)
        f.flush()
        os.fsync(f.fileno())
    os.replace(f.name, path)


@contextmanager
def locked(path):
    path.parent.mkdir(parents=True, exist_ok=True)
    with path.open("a+") as f:
        fcntl.flock(f, fcntl.LOCK_EX)
        yield


def parse_json(text):
    text = text.strip()
    if text.startswith("```"):
        text = text.split("\n", 1)[1].rsplit("```", 1)[0]
    return json.loads(text)


def without_trailing_commas(text):
    """Remove JSON trailing commas only outside strings; never change text content."""
    quoted, escaped, out = False, False, []
    for i, char in enumerate(text):
        if not quoted and char == "," and text[i + 1 :].lstrip().startswith(("}", "]")):
            continue
        out.append(char)
        if escaped:
            escaped = False
        elif char == "\\" and quoted:
            escaped = True
        elif char == '"':
            quoted = not quoted
    return "".join(out)


def parse_response(provider, stdout):
    """Return the paid answer text and usage even when its JSON is malformed."""
    if provider == "claude":
        result = json.loads(stdout)
        usage = result.get("usage") or {}
        return (
            (
                canonical(result["structured_output"])
                if result.get("structured_output") is not None
                else result.get("result", "")
            ),
            {
                "input": usage.get("input_tokens", 0),
                "cached": usage.get("cache_read_input_tokens", 0),
                "cache_write": usage.get("cache_creation_input_tokens", 0),
                "output": usage.get("output_tokens", 0),
                "list_usd": result.get("total_cost_usd"),
            },
            result.get("is_error", False),
        )
    answer, usage, failed, completed = "", {}, False, False
    for line in stdout.splitlines():
        try:
            event = json.loads(line)
        except ValueError:
            continue
        if event.get("type") == "item.completed":
            item = event.get("item") or {}
            if item.get("type") == "agent_message":
                answer = item.get("text", "")
        elif event.get("type") == "turn.completed":
            completed = True
            u = event.get("usage") or {}
            usage = {
                "input": u.get("input_tokens", 0),
                "cached": u.get("cached_input_tokens", 0),
                "output": u.get("output_tokens", 0),
                "reasoning": u.get("reasoning_output_tokens"),
            }
        elif event.get("type") == "turn.failed":
            failed = True
    return answer, usage, failed or not completed


def costs(seat, usage):
    provider = MODELS[seat][0]
    listed = usage.get("list_usd")
    if listed is None:
        fresh, cached, output = PRICES[seat]
        inp = usage.get("input", 0)
        if provider == "codex":
            inp = max(0, inp - usage.get("cached", 0))
        listed = (
            inp * fresh
            + usage.get("cached", 0) * cached
            + usage.get("cache_write", 0) * fresh * 1.25
            + usage.get("output", 0) * output
        ) / 1e6
    return {"list_usd": listed, "estimated_plan_usd": listed * PLAN_SHARE[provider]}


class Held(RuntimeError):
    pass


def invoke(request, folder):
    """Exactly one CLI attempt. Output stays on disk even if this process dies."""
    provider, model, effort = request["provider"], request["model"], request["effort"]
    empty = Path(tempfile.gettempdir()) / "almonium-lexicon-review-empty"
    empty.mkdir(exist_ok=True)
    env = os.environ.copy()
    # The CLI must use its signed-in plan, never an inherited API credential.
    for key in ("OPENAI_API_KEY", "CODEX_API_KEY", "ANTHROPIC_API_KEY", "ANTHROPIC_AUTH_TOKEN"):
        env.pop(key, None)
    if provider == "claude":
        schema = dict(request["schema"])
        schema.pop("$schema", None)
        if "$defs" in schema:
            schema["definitions"] = schema.pop("$defs")
        command = [
            "claude",
            "-p",
            "--model",
            model,
            "--effort",
            effort,
            "--tools",
            "",
            "--system-prompt",
            request["system"],
            "--json-schema",
            canonical(schema).replace("#/$defs/", "#/definitions/"),
            "--output-format",
            "json",
            "--no-session-persistence",
            "--strict-mcp-config",
            "--mcp-config",
            '{"mcpServers": {}}',
        ]
        prompt = request["user"]
    else:
        candidates = sorted(Path.home().glob(".nvm/versions/node/*/bin/codex"), reverse=True)
        binary = os.environ.get("CODEX_BIN") or (
            str(candidates[0]) if candidates else shutil.which("codex")
        )
        if not binary:
            raise Held("Codex CLI unavailable; no API fallback")
        # Like the original lexicon runner, generation uses the house schema in
        # validation; Codex's strict-output subset rejects its optional fields.
        command = [
            binary,
            "exec",
            "-m",
            model,
            "--skip-git-repo-check",
            "--ephemeral",
            "--json",
            "--sandbox",
            "read-only",
            "-c",
            "mcp_servers={}",
            "-c",
            f"model_reasoning_effort={effort}",
        ]
        if request["stage"] != "generate":
            save(folder / "schema.json", request["schema"])
            command += ["--output-schema", str(folder / "schema.json")]
        command += ["-"]
        prompt = request["system"] + "\n\nDo not run commands or read files.\n\n" + request["user"]
    with (folder / "stdout.txt").open("w") as stdout, (folder / "stderr.txt").open("w") as stderr:
        completed = subprocess.run(
            command,
            input=prompt,
            text=True,
            stdout=stdout,
            stderr=stderr,
            cwd=empty,
            env=env,
            timeout=900,
        )
    return completed.returncode


def paid_call(seat, stage, system, user, schema, *, root=OUT, budget=2.0, runner=invoke):
    provider, model, effort = MODELS[seat]
    request = {
        "provider": provider,
        "model": model,
        "effort": effort,
        "stage": stage,
        "system": system,
        "user": user,
        "schema": schema,
        "transport_version": 1,
    }
    key = request_key(request)
    folder = root / "calls" / key
    folder.mkdir(parents=True, exist_ok=True)
    with locked(folder / "lock"):
        if (folder / "result.json").exists():
            cached = json.loads((folder / "result.json").read_text())
            if cached.get("parse_version") == 2:
                return key, cached
        started = (folder / "request.json").exists()
        if not started:
            with locked(root / "budget.lock"):
                spent = 0
                for p in (root / "calls").glob("*/request.json"):
                    result_path = p.parent / "result.json"
                    spent += (
                        json.loads(result_path.read_text())["cost"]["estimated_plan_usd"]
                        if result_path.exists()
                        else 0.15
                    )
                # Conservatively reserve $0.15 of estimated plan spend per turn.
                # A plan CLI has no hard monetary token cap; this is a launch gate.
                if spent + 0.15 > budget:
                    raise Held("Estimated plan budget reached")
                save(folder / "request.json", request)
                save(folder / "started.json", {"at": datetime.now(UTC).isoformat()})
            try:
                code = runner(request, folder)
                save(folder / "exit.json", {"returncode": code})
            except subprocess.TimeoutExpired:
                save(
                    folder / "hold.json",
                    {"reason": "timeout; paid output retained, no automatic retry"},
                )
        stdout = (folder / "stdout.txt").read_text() if (folder / "stdout.txt").exists() else ""
        stderr = (folder / "stderr.txt").read_text() if (folder / "stderr.txt").exists() else ""
        try:
            answer_text, usage, failed = parse_response(provider, stdout)
        except ValueError:
            answer_text, usage, failed = "", {}, True
        if not answer_text or failed:
            reason = (
                "allowance hold" if LIMIT_RE.search(stdout + stderr) else "incomplete CLI result"
            )
            save(
                folder / "hold.json", {"reason": reason, "usage": usage, "cost": costs(seat, usage)}
            )
            raise Held(reason + "; saved request will not be repurchased")
        syntax_repair = False
        try:
            answer = parse_json(answer_text)
        except ValueError:
            try:
                answer = parse_json(without_trailing_commas(answer_text))
                syntax_repair = True
            except ValueError:
                answer = None
        errors = (
            [
                {"path": "/" + "/".join(map(str, e.absolute_path)), "rule": e.validator}
                for e in jsonschema.Draft202012Validator(schema).iter_errors(answer)
            ]
            if answer is not None
            else [{"path": "/", "rule": "json"}]
        )
        result = {
            "parse_version": 2,
            "syntax_repair": syntax_repair,
            "answer": answer,
            "validation_errors": errors,
            "usage": usage,
            "cost": costs(seat, usage),
            "seat": seat,
            "stage": stage,
        }
        save(folder / "result.json", result)
        return key, result


def cases(limit=0):
    rows = json.loads((HERE / "review_cases.json").read_text())["pilot"]
    return rows[:limit] if limit else rows


def ref_path(root, stage, case, writer, judge=None):
    return (
        root / "index" / stage / case["id"] / (writer + ("--" + judge if judge else "") + ".json")
    )


def load_ref(path, root=OUT):
    ref = json.loads(path.read_text())
    return json.loads((root / "calls" / ref["key"] / "result.json").read_text())


def generate(case, writer, args):
    user = prompting.user_prompt(case["language"], case)
    key, result = paid_call(
        writer,
        "generate",
        prompting.system_prompt(case["language"]),
        user,
        json.loads((HERE / "entry.schema.json").read_text()),
        budget=args.budget,
    )
    save(ref_path(OUT, "generate", case, writer), {"key": key})
    return (
        f"{case['id']} {writer}: generated, schema errors={len(result['validation_errors'])} "
        f"plan~${result['cost']['estimated_plan_usd']:.4f}"
    )


def review(case, writer, judge, args):
    source = ref_path(OUT, "generate", case, writer)
    if not source.exists():
        return f"{case['id']} {writer}: generation not available"
    entry = load_ref(source)["answer"]
    if entry is None:
        return f"{case['id']} {writer}: no parseable entry"
    user = canonical(
        {
            "entry": entry,
            "house_rules": prompting.system_prompt(case["language"]),
            "review_output_schema": REVIEW_SCHEMA,
        }
    )
    key, result = paid_call(judge, "review", REVIEW_PROMPT, user, REVIEW_SCHEMA, budget=args.budget)
    save(ref_path(OUT, "review", case, writer, judge), {"key": key})
    answer = result["answer"] or {}
    return (
        f"{case['id']} {writer} by {judge}: {answer.get('verdict')}, "
        f"findings={len(answer.get('findings', []))} "
        f"plan~${result['cost']['estimated_plan_usd']:.4f}"
    )


def amend(case, writer, args):
    base = ref_path(OUT, "generate", case, writer)
    judges = [j for j in MODELS if j != writer]
    references = [ref_path(OUT, "review", case, writer, j) for j in judges]
    if not base.exists() or not all(p.exists() for p in references):
        raise Held(f"{case['id']} needs generation and both reviews")
    reviews = {f"R{i + 1}": load_ref(p)["answer"] for i, p in enumerate(references)}
    if any(load_ref(p)["validation_errors"] for p in references):
        raise Held("Invalid review output; do not silently treat it as a pass")
    evidence = json.loads((HERE / "review_evidence.json").read_text())["cases"][case["id"]]
    entry_schema = json.loads((HERE / "entry.schema.json").read_text())
    editor = "sol" if writer != "sol" else "opus"
    user = canonical(
        {
            "entry": load_ref(base)["answer"],
            "reviews": reviews,
            "external_evidence": evidence,
            "entry_schema": entry_schema,
            "house_rules": prompting.system_prompt(case["language"]),
        }
    )
    key, result = paid_call(editor, "amend", AMEND_PROMPT, user, AMEND_SCHEMA, budget=args.budget)
    answer = result["answer"]
    if result["validation_errors"]:
        raise Held("Amendment did not satisfy its response schema; paid result kept")
    expected = {(label, f["id"]) for label, review in reviews.items() for f in review["findings"]}
    seen = [(d["reviewer"], d["finding_id"]) for d in answer["decisions"]]
    if set(seen) != expected or len(seen) != len(expected):
        raise Held("Amendment did not decide each finding exactly once")
    source_ids = {s["id"] for s in evidence["sources"]}
    if any(set(d["source_ids"]) - source_ids for d in answer["decisions"]):
        raise Held("Amendment cited an unknown source")
    checkpoints = [c["id"] for c in answer["checkpoint_results"]]
    if set(checkpoints) != {c["id"] for c in evidence["checkpoints"]} or len(checkpoints) != len(
        set(checkpoints)
    ):
        raise Held("Amendment checkpoint coverage incomplete")
    try:
        revised = parse_json(answer["entry_json"])
    except ValueError as error:
        raise Held("Editor returned malformed entry_json; paid answer retained") from error
    errors = [
        e.validator for e in jsonschema.Draft202012Validator(entry_schema).iter_errors(revised)
    ]
    if revised.get("language") != case["language"] or revised.get("headword") != case["headword"]:
        errors.append("entry_identity")
    save(
        ref_path(OUT, "amend", case, writer),
        {"key": key, "editor": editor, "entry_validation_errors": errors, "revised_entry": revised},
    )
    return (
        f"{case['id']} {writer} amended by {editor}: "
        f"{len(answer['unresolved'])} unresolved, {len(errors)} schema errors; "
        f"plan~${result['cost']['estimated_plan_usd']:.4f}"
    )


def verify(case, writer, args):
    path = ref_path(OUT, "amend", case, writer)
    if not path.exists():
        raise Held(f"{case['id']} amendment unavailable")
    ref = json.loads(path.read_text())
    if ref["entry_validation_errors"]:
        raise Held("Revised entry invalid; retain it for inspection, do not pass it")
    judge = next(j for j in MODELS if j not in [writer, ref["editor"]])
    evidence = json.loads((HERE / "review_evidence.json").read_text())["cases"][case["id"]]
    user = canonical(
        {
            "entry": ref["revised_entry"],
            "external_evidence": evidence,
            "house_rules": prompting.system_prompt(case["language"]),
            "review_output_schema": REVIEW_SCHEMA,
        }
    )
    prompt = (
        REVIEW_PROMPT + "\nSupplied source notes may inform your independent review; "
        "their omissions do not disprove a sense."
    )
    key, result = paid_call(judge, "verify", prompt, user, REVIEW_SCHEMA, budget=args.budget)
    save(ref_path(OUT, "verify", case, writer, judge), {"key": key})
    return f"{case['id']} revised {writer} by {judge}: {(result['answer'] or {}).get('verdict')}"


def report(root=OUT):
    records = [json.loads(p.read_text()) for p in (root / "calls").glob("*/result.json")]
    summary = {
        "pricing_basis": "2026-10-15 owner measurements: Claude 0.03, Codex 0.07 of list",
        "estimated_plan_usd": sum(r["cost"]["estimated_plan_usd"] for r in records),
        "list_reference_usd": sum(r["cost"]["list_usd"] for r in records),
        "completed_calls": len(records),
        "groups": [],
    }
    holds = [
        json.loads(p.read_text())
        for p in (root / "calls").glob("*/hold.json")
        if not (p.parent / "result.json").exists()
    ]
    summary["held_calls"] = len(holds)
    summary["held_known_plan_usd"] = sum(
        h.get("cost", {}).get("estimated_plan_usd", 0) for h in holds
    )
    summary["held_usage_unknown"] = sum(not h.get("usage") for h in holds)
    for stage in sorted({r["stage"] for r in records}):
        for seat in MODELS:
            rows = [r for r in records if r["stage"] == stage and r["seat"] == seat]
            if not rows:
                continue
            summary["groups"].append(
                {
                    "stage": stage,
                    "seat": seat,
                    "n": len(rows),
                    "schema_invalid": sum(bool(r["validation_errors"]) for r in rows),
                    "syntax_repaired": sum(r.get("syntax_repair", False) for r in rows),
                    "mean_plan_usd": sum(r["cost"]["estimated_plan_usd"] for r in rows) / len(rows),
                    "mean_output_tokens": sum(r["usage"].get("output", 0) for r in rows)
                    / len(rows),
                }
            )
    save(root / "summary.json", summary)
    print(json.dumps(summary, indent=2))


def main():
    ap = argparse.ArgumentParser(description=__doc__)
    ap.add_argument("stage", choices=["generate", "review", "amend", "verify", "report"])
    ap.add_argument("--limit", type=int, default=0)
    ap.add_argument("--workers", type=int, default=2, choices=[1, 2])
    ap.add_argument("--writers", default="opus,sol,astra")
    ap.add_argument(
        "--budget", type=float, default=2.0, help="estimated plan dollars, not API dollars"
    )
    args = ap.parse_args()
    if args.stage == "report":
        report()
        return
    jobs = []
    for case in cases(args.limit):
        for writer in args.writers.split(","):
            if writer not in MODELS:
                ap.error("unknown writer")
            if args.stage == "generate":
                jobs.append((case, writer, args))
            elif args.stage == "review":
                jobs.extend((case, writer, judge, args) for judge in MODELS if judge != writer)
            else:
                jobs.append((case, writer, args))
    stopped = threading.Event()

    def work(job):
        if stopped.is_set():
            return "HELD: earlier call held; no further plan calls"
        try:
            return {"generate": generate, "review": review, "amend": amend, "verify": verify}[
                args.stage
            ](*job)
        except Held as error:
            stopped.set()
            return "HELD: " + str(error)

    with ThreadPoolExecutor(args.workers) as pool:
        for result in pool.map(work, jobs):
            print(result, flush=True)
    report()


if __name__ == "__main__":
    main()
