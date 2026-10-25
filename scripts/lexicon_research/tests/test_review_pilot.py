"""Paid-call safety checks; no test invokes a model or a network endpoint."""

import json
import sys
from concurrent.futures import ThreadPoolExecutor
from pathlib import Path

import pytest

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
import review_pilot as pilot


def fake_codex(request, folder):
    events = [
        {"type": "item.completed", "item": {"type": "agent_message", "text": '{"ok":true}'}},
        {
            "type": "turn.completed",
            "usage": {"input_tokens": 100, "cached_input_tokens": 20, "output_tokens": 30},
        },
    ]
    (folder / "stdout.txt").write_text("\n".join(json.dumps(e) for e in events))
    return 0


SCHEMA = pilot.obj({"ok": {"type": "boolean"}})


def call(tmp_path, **kwargs):
    return pilot.paid_call(
        "sol",
        "review",
        "system",
        "entry",
        SCHEMA,
        root=tmp_path,
        runner=kwargs.pop("runner", fake_codex),
        **kwargs,
    )


def test_concurrent_identical_requests_are_bought_once(tmp_path):
    calls = []

    def runner(request, folder):
        calls.append(1)
        return fake_codex(request, folder)

    with ThreadPoolExecutor(2) as pool:
        results = list(pool.map(lambda _: call(tmp_path, runner=runner), range(2)))
    assert len(calls) == 1
    assert results[0] == results[1]


def test_parser_interruption_adopts_saved_paid_output(tmp_path):
    key, first = call(tmp_path)
    (tmp_path / "calls" / key / "result.json").unlink()

    def forbidden(*args):
        pytest.fail("Paid result was purchased again")

    _, adopted = call(tmp_path, runner=forbidden)
    assert first == adopted


def test_price_change_does_not_repurchase(tmp_path, monkeypatch):
    key, first = call(tmp_path)
    monkeypatch.setitem(pilot.PLAN_SHARE, "codex", 0.9)
    key2, second = call(tmp_path, runner=lambda *args: pytest.fail("repurchased"))
    assert key == key2 and first == second


def test_changed_content_changes_identity(tmp_path):
    key, _ = call(tmp_path)
    key2, _ = pilot.paid_call(
        "sol", "review", "system", "changed entry", SCHEMA, root=tmp_path, runner=fake_codex
    )
    assert key != key2


def test_ambiguous_failure_is_not_retried(tmp_path):
    calls = []

    def broken(request, folder):
        calls.append(1)
        (folder / "stdout.txt").write_text("")
        return 1

    for _ in range(2):
        with pytest.raises(pilot.Held):
            call(tmp_path, runner=broken)
    assert calls == [1]


def test_malformed_paid_answer_keeps_usage(tmp_path):
    def malformed(request, folder):
        fake_codex(request, folder)
        p = folder / "stdout.txt"
        p.write_text(p.read_text().replace('{\\"ok\\":true}', "not-json"))
        return 0

    _, result = call(tmp_path, runner=malformed)
    assert result["answer"] is None
    assert result["cost"]["estimated_plan_usd"] > 0


def test_answer_without_completion_receipt_is_held_not_priced_as_free(tmp_path):
    bought = []

    def interrupted(request, folder):
        bought.append(1)
        fake_codex(request, folder)
        p = folder / "stdout.txt"
        p.write_text(p.read_text().splitlines()[0])
        return 1

    for _ in range(2):
        with pytest.raises(pilot.Held, match="incomplete"):
            call(tmp_path, runner=interrupted)
    assert bought == [1]
    assert not list(tmp_path.glob("calls/*/result.json"))


def test_budget_hold_does_not_start_call(tmp_path):
    with pytest.raises(pilot.Held, match="budget"):
        call(tmp_path, budget=0.01, runner=lambda *args: pytest.fail("called over budget"))
    assert not list(tmp_path.glob("calls/*/request.json"))


def test_every_area_is_required_in_review():
    answer = {
        "verdict": "pass",
        "checks": {a: "pass" for a in pilot.AREAS},
        "findings": [],
        "coverage_note": "no issues identified",
    }
    pilot.jsonschema.validate(answer, pilot.REVIEW_SCHEMA)
    del answer["checks"]["sense_coverage"]
    with pytest.raises(pilot.jsonschema.ValidationError):
        pilot.jsonschema.validate(answer, pilot.REVIEW_SCHEMA)


def test_syntax_repair_preserves_commas_inside_strings():
    source = '{"text":"literal ,} and \\"quoted\\"", "values":[1,],}'
    repaired = json.loads(pilot.without_trailing_commas(source))
    assert repaired == {"text": 'literal ,} and "quoted"', "values": [1]}


@pytest.mark.parametrize("seat", ["opus", "sol"])
def test_transport_is_headless_plan_cli_without_inherited_api_keys(tmp_path, monkeypatch, seat):
    for key in ["OPENAI_API_KEY", "CODEX_API_KEY", "ANTHROPIC_API_KEY", "ANTHROPIC_AUTH_TOKEN"]:
        monkeypatch.setenv(key, "test-secret-not-a-real-key")
    captured = {}

    def fake_run(command, **kwargs):
        captured.update(command=command, **kwargs)
        return type("Completed", (), {"returncode": 0})()

    monkeypatch.setattr(pilot.subprocess, "run", fake_run)
    monkeypatch.setenv("CODEX_BIN", "/test/codex")
    provider, model, effort = pilot.MODELS[seat]
    pilot.invoke(
        {
            "provider": provider,
            "model": model,
            "effort": effort,
            "stage": "review",
            "system": "rules",
            "user": "entry",
            "schema": SCHEMA,
        },
        tmp_path,
    )
    assert not any(
        k in captured["env"]
        for k in ["OPENAI_API_KEY", "CODEX_API_KEY", "ANTHROPIC_API_KEY", "ANTHROPIC_AUTH_TOKEN"]
    )
    assert model in captured["command"]
    assert captured["input"]
    if seat == "opus":
        assert captured["command"][0:2] == ["claude", "-p"]
        assert "--strict-mcp-config" in captured["command"]
    else:
        assert captured["command"][0:2] == ["/test/codex", "exec"]
        assert "--ephemeral" in captured["command"]
        assert "read-only" in captured["command"]


def test_report_compares_only_matched_completed_panels(tmp_path):
    import review_report

    first, second = pilot.cases()[:2]

    def record(case, stage, cost, judge=None):
        key = case["id"] + stage + str(judge)
        answer = {"lexemes": []} if stage == "generate" else {"verdict": "pass", "findings": []}
        pilot.save(
            tmp_path / "calls" / key / "result.json",
            {"answer": answer, "cost": {"estimated_plan_usd": cost}, "validation_errors": []},
        )
        pilot.save(pilot.ref_path(tmp_path, stage, case, "opus", judge), {"key": key})

    record(first, "generate", 2)
    record(first, "review", 1, "sol")
    record(first, "review", 1, "astra")
    record(second, "generate", 100)
    result = review_report.build(tmp_path)
    assert result["writers"]["opus"]["complete_panels"] == 1
    assert result["writers"]["opus"]["matched_cost_multiplier"] == 2
    assert result["total_estimated_plan_usd"] == 104


def fake_router(body):
    return json.dumps(
        {
            "choices": [
                {
                    "finish_reason": "stop",
                    "message": {
                        "content": json.dumps(
                            {
                                "verdict": "pass",
                                "checks": {a: "pass" for a in pilot.AREAS},
                                "findings": [],
                                "coverage_note": "fixture",
                            }
                        )
                    },
                }
            ],
            "usage": {"cost": 0.002},
        }
    )


def test_api_challenger_bought_once_and_adopts_saved_raw(tmp_path):
    import review_challengers as challenger

    bought = []

    def sender(body):
        bought.append(1)
        assert body["provider"]["allow_fallbacks"] is False
        return fake_router(body)

    def call():
        return challenger.api_call("qwen", "entry", root=tmp_path, sender=sender)

    with ThreadPoolExecutor(2) as pool:
        results = list(pool.map(lambda _: call(), range(2)))
    assert bought == [1]
    key, result = results[0]
    assert result["api_usd"] == 0.002
    assert "estimated_plan_usd" not in result
    (tmp_path / "api-calls" / key / "result.json").unlink()
    assert call()[1] == result
    assert bought == [1]


def test_api_challenger_timeout_never_repurchased(tmp_path):
    import review_challengers as challenger

    bought = []

    def sender(body):
        bought.append(1)
        raise TimeoutError()

    for _ in range(2):
        with pytest.raises(pilot.Held):
            challenger.api_call("qwen", "entry", root=tmp_path, sender=sender)
    assert bought == [1]


def test_api_challenger_budget_holds_before_request(tmp_path):
    import review_challengers as challenger

    with pytest.raises(pilot.Held, match="budget"):
        challenger.api_call(
            "qwen", "entry", root=tmp_path, budget=0, sender=lambda _: pytest.fail("spent")
        )
    assert not list(tmp_path.glob("api-calls/*/request.json"))


def test_api_challenger_retains_malformed_raw_without_retry(tmp_path):
    import review_challengers as challenger

    bought = []

    def sender(body):
        bought.append(1)
        return "broken paid response"

    for _ in range(2):
        with pytest.raises(pilot.Held, match="Malformed"):
            challenger.api_call("qwen", "entry", root=tmp_path, sender=sender)
    assert bought == [1]
    assert next(tmp_path.glob("api-calls/*/response.txt")).read_text() == "broken paid response"
