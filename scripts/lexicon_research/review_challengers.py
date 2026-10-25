"""Explicitly authorized extra judges; plan and API spending stay separate.

Fable uses Claude Code. DeepSeek and Qwen use OpenRouter, with one attempt per
content identity, no SDK retries or provider fallbacks. No production integration.
"""

import argparse
import json
import urllib.error
import urllib.request
from collections import Counter
from datetime import UTC, datetime

import review_pilot as pilot

OUT = pilot.HERE / "out" / "review-challengers-v1"
MODELS = {
    "deepseek": "deepseek/deepseek-v4.1-flash-1017",
    "deepseek-thinking": "deepseek/deepseek-v4.1-flash-1017",
    "qwen": "qwen/qwen3.7-plus",
}


def body_for(seat, user):
    return {
        "model": MODELS[seat],
        "messages": [
            {"role": "system", "content": pilot.REVIEW_PROMPT},
            {"role": "user", "content": user},
        ],
        "max_tokens": 8192 if seat == "deepseek-thinking" else 4096,
        "temperature": 0.2,
        "reasoning": {"effort": "low"} if seat == "deepseek-thinking" else {"enabled": False},
        "response_format": {
            "type": "json_schema",
            "json_schema": {"name": "review", "strict": True, "schema": pilot.REVIEW_SCHEMA},
        },
        "provider": {
            "require_parameters": True,
            "allow_fallbacks": False,
            "max_price": {"prompt": 1, "completion": 3},
        },
        "usage": {"include": True},
    }


def post(body):
    env_path = pilot.HERE.parents[1] / ".env"
    values = {}
    for line in env_path.read_text().splitlines():
        if line.startswith("OPENROUTER_API_KEY="):
            values["key"] = line.split("=", 1)[1].strip().strip("\"'")
    if not values.get("key"):
        raise pilot.Held("OpenRouter credential unavailable")
    request = urllib.request.Request(
        "https://openrouter.ai/api/v1/chat/completions",
        data=pilot.canonical(body).encode(),
        headers={"Authorization": "Bearer " + values["key"], "Content-Type": "application/json"},
    )
    # urllib performs no automatic retry of a failed POST.
    with urllib.request.urlopen(request, timeout=240) as response:
        return response.read().decode()


def api_call(seat, user, *, root=OUT, budget=0.5, sender=post):
    body = body_for(seat, user)
    identity = {"provider": "openrouter", "stage": "review", "body": body, "transport_version": 1}
    key = pilot.request_key(identity)
    folder = root / "api-calls" / key
    with pilot.locked(folder / "lock"):
        result_path = folder / "result.json"
        if result_path.exists():
            return key, json.loads(result_path.read_text())
        if not (folder / "request.json").exists():
            # Conservative byte-based input allowance + output ceiling at routing
            # price caps. Launch gate, not a guarantee about provider tokenization.
            reservation = (
                len(pilot.canonical(body).encode()) + 2048 + body["max_tokens"] * 3
            ) / 1e6
            with pilot.locked(root / "api-budget.lock"):
                spent = 0
                for p in (root / "api-calls").glob("*/started.json"):
                    result = p.parent / "result.json"
                    actual = (
                        json.loads(result.read_text()).get("api_usd") if result.exists() else None
                    )
                    spent += (
                        actual if actual is not None else json.loads(p.read_text())["reserved_usd"]
                    )
                if spent + reservation > budget:
                    raise pilot.Held("OpenRouter budget launch gate reached")
                pilot.save(
                    folder / "started.json",
                    {"at": datetime.now(UTC).isoformat(), "reserved_usd": reservation},
                )
                pilot.save(folder / "request.json", identity)
            try:
                raw = sender(body)
                (folder / "response.txt").write_text(raw)
            except Exception as error:
                # Never expose credentials or an HTTP response that may echo headers.
                pilot.save(folder / "hold.json", {"reason": type(error).__name__})
                raise pilot.Held(
                    "OpenRouter call incomplete; retained, never automatically retried"
                ) from error
        if not (folder / "response.txt").exists():
            raise pilot.Held("Ambiguous previous OpenRouter call; no automatic retry")
        try:
            response = json.loads((folder / "response.txt").read_text())
        except ValueError as error:
            raise pilot.Held(
                "Malformed OpenRouter response retained; no automatic retry"
            ) from error
        choice = (response.get("choices") or [{}])[0]
        text = (choice.get("message") or {}).get("content") or ""
        repaired = False
        try:
            answer = pilot.parse_json(text)
        except ValueError:
            try:
                answer = pilot.parse_json(pilot.without_trailing_commas(text))
                repaired = True
            except ValueError:
                answer = None
        errors = [
            e.validator
            for e in pilot.jsonschema.Draft202012Validator(pilot.REVIEW_SCHEMA).iter_errors(answer)
        ]
        if choice.get("finish_reason") != "stop":
            errors.append("incomplete_completion")
        result = {
            "seat": seat,
            "model": response.get("model"),
            "provider": response.get("provider"),
            "response_id": response.get("id"),
            "answer": answer,
            "syntax_repair": repaired,
            "validation_errors": errors,
            "usage": response.get("usage", {}),
            "api_usd": (response.get("usage") or {}).get("cost"),
        }
        pilot.save(result_path, result)
        return key, result


def review(case, writer, seat):
    entry = pilot.load_ref(pilot.ref_path(pilot.OUT, "generate", case, writer))["answer"]
    user = pilot.canonical(
        {
            "entry": entry,
            "house_rules": pilot.prompting.system_prompt(case["language"]),
            "review_output_schema": pilot.REVIEW_SCHEMA,
        }
    )
    if seat == "fable":
        pilot.MODELS[seat] = ("claude", "claude-fable-5-1", "high")
        # Local pricing.py reference; normally the Claude CLI supplies total_cost_usd.
        pilot.PRICES[seat] = (10, 1, 50)
        key, result = pilot.paid_call(
            seat,
            "review",
            pilot.REVIEW_PROMPT,
            user,
            pilot.REVIEW_SCHEMA,
            root=OUT / "fable",
            budget=0.5,
        )
        cost = result["cost"]["estimated_plan_usd"]
        billing = "plan estimate"
    else:
        key, result = api_call(seat, user)
        cost = result["api_usd"]
        billing = "API actual"
    pilot.save(
        OUT / "index" / case["id"] / f"{writer}--{seat}.json",
        {"key": key, "seat": seat, "case": case["id"], "writer": writer},
    )
    return (
        f"{case['id']} {writer} by {seat}: {(result['answer'] or {}).get('verdict')}, "
        f"errors={result['validation_errors']}, {billing} ${cost}"
    )


def main():
    ap = argparse.ArgumentParser(description=__doc__)
    ap.add_argument("--judges", default="fable,deepseek,qwen")
    ap.add_argument("--languages", default="en")
    ap.add_argument("--writers", default="opus,sol,astra")
    ap.add_argument("--case-ids", default="", help="Optional comma-separated diagnostic cases")
    ap.add_argument("--report", action="store_true", help="Offline receipts only; no calls")
    args = ap.parse_args()
    if args.report:
        print(json.dumps(report(), indent=2))
        return
    for seat in args.judges.split(","):
        if seat not in {"fable", *MODELS}:
            ap.error("Unknown judge")
    for case in pilot.cases():
        if case["language"] not in args.languages.split(","):
            continue
        if args.case_ids and case["id"] not in args.case_ids.split(","):
            continue
        for writer in args.writers.split(","):
            for seat in args.judges.split(","):
                try:
                    print(review(case, writer, seat), flush=True)
                except pilot.Held as error:
                    print("HELD: " + str(error), flush=True)
                    return


def report(root=OUT):
    summary = {"judges": {}, "plan_estimate_usd": 0, "api_actual_usd": 0}
    for seat in ["fable", *MODELS]:
        pattern = "fable/calls/*/result.json" if seat == "fable" else "api-calls/*/result.json"
        rows = [json.loads(p.read_text()) for p in root.glob(pattern)]
        rows = [r for r in rows if r["seat"] == seat]
        if not rows:
            continue
        costs = [r["cost"]["estimated_plan_usd"] if seat == "fable" else r["api_usd"] for r in rows]
        total = sum(c for c in costs if c is not None)
        summary["plan_estimate_usd" if seat == "fable" else "api_actual_usd"] += total
        summary["judges"][seat] = {
            "n": len(rows),
            "schema_invalid": sum(bool(r["validation_errors"]) for r in rows),
            "cost_kind": "allocated plan estimate" if seat == "fable" else "actual API",
            "mean_usd": total / len(rows) if all(c is not None for c in costs) else None,
            "cost_unknown": sum(c is None for c in costs),
            "verdicts": dict(
                Counter(
                    (r["answer"] or {}).get("verdict") if not r["validation_errors"] else "invalid"
                    for r in rows
                )
            ),
        }
    summary["incomplete_api_requests"] = sum(
        not (p.parent / "result.json").exists() for p in root.glob("api-calls/*/request.json")
    )
    pilot.save(root / "comparison.json", summary)
    return summary


if __name__ == "__main__":
    main()
