"""The settling policy, with the model call replaced: what lands, what is refused, what is reverted."""

import copy
import json
import sys
from pathlib import Path

import pytest

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
from test_wiktionary_evidence import entry

import grounded_writer
import patch_facts
import wiktionary


@pytest.fixture(autouse=True)
def cached(monkeypatch):
    monkeypatch.setattr(wiktionary, "CACHE", Path(__file__).resolve().parent / "fixtures" / "kaikki")


def answering(monkeypatch, decisions):
    def call(model, system, user, effort):
        return json.dumps({"decisions": decisions}), {"usd": 0.001}

    monkeypatch.setattr(grounded_writer, "call_openrouter", call)


def wrong_gender():
    bad = entry()
    bad["lexemes"][0]["grammar"]["gender"] = "Masc"
    return bad


def test_a_fix_inside_its_lexeme_lands_when_it_settles_the_fact(monkeypatch):
    answering(
        monkeypatch,
        [
            {
                "finding": 0,
                "decision": "fix",
                "why": "evidence says f",
                "patch": [{"op": "replace", "path": "/lexemes/0/grammar/gender", "value": "Fem"}],
            }
        ],
    )
    patched, report, _ = patch_facts.settle(wrong_gender(), wiktionary.evidence("es", "guagua"))
    assert patched["lexemes"][0]["grammar"]["gender"] == "Fem"
    assert report["fixed"] == 1


def test_a_patch_reaching_outside_its_finding_is_refused(monkeypatch):
    answering(
        monkeypatch,
        [
            {
                "finding": 0,
                "decision": "fix",
                "why": "",
                "patch": [{"op": "replace", "path": "/headword", "value": "autobús"}],
            }
        ],
    )
    original = wrong_gender()
    patched, report, _ = patch_facts.settle(original, wiktionary.evidence("es", "guagua"))
    assert patched == original and report["refused"] == 1


def test_a_patch_that_settles_nothing_is_reverted(monkeypatch):
    answering(
        monkeypatch,
        [
            {
                "finding": 0,
                "decision": "fix",
                "why": "",
                "patch": [{"op": "replace", "path": "/lexemes/0/grammar/gender", "value": "Neut"}],
            }
        ],
    )
    original = wrong_gender()
    patched, report, _ = patch_facts.settle(original, wiktionary.evidence("es", "guagua"))
    assert patched == original and report.get("reverted")


def test_keep_leaves_the_entry_as_written(monkeypatch):
    answering(monkeypatch, [{"finding": 0, "decision": "keep", "why": "Ecuadorian use", "patch": []}])
    original = wrong_gender()
    patched, report, _ = patch_facts.settle(copy.deepcopy(original), wiktionary.evidence("es", "guagua"))
    assert patched == original and report["kept"] == 1


def test_nothing_to_settle_costs_nothing(monkeypatch):
    answering(monkeypatch, [])
    _, report, usd = patch_facts.settle(entry(), wiktionary.evidence("es", "guagua"))
    assert report == {"findings": 0} and usd == 0
