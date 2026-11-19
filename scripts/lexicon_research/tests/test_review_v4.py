"""The review loop: a second model's findings, the writer's answers, a third model's rulings, and the patches."""
import copy
import json
import sys
from pathlib import Path

import pytest

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
import entry_patch  # noqa: E402
import ingest_v4  # noqa: E402
import review_v4  # noqa: E402

ENTRY = {"headword": "Haus", "lexemes": [{"lemma": "Haus", "pos": "NOUN", "grammar": {"gender": "Masc", "countability": "Count"},
                                         "pronunciations": [{"variety": "de-DE", "ipa": "/ˈhaʊs/"}],
                                         "senses": [{"definition": "Gebäude, in dem Menschen wohnen", "example": "Wir wohnen in einem alten Haus.", "cefr": "A1",
                                                     "collocations": ["zu Hause", "ein Haus bauen", "nach Hause"]},
                                                    {"definition": "Familie von hohem Rang", "example": "Das Haus Habsburg regierte lange.", "cefr": "C1"}]}]}
FINDINGS = [{"fault": "fact", "where": "lexeme 1 grammar gender", "what": "Haus is neuter.", "misleads": True},
            {"fault": "missing-core", "where": "lexeme 1 senses", "what": "Haus as a firm is missing.", "misleads": True},
            {"fault": "wording", "where": "lexeme 1 sense 1", "what": "Too narrow.", "misleads": False},
            {"fault": "claim", "where": "lexeme 1 grammar countability", "what": "Also Mass.", "misleads": True}]
NEW_SENSE = {"definition": "Firma oder Einrichtung", "example": "Unser Haus liefert seit 1900.", "cefr": "B2"}


def item(entry=ENTRY, **more):
    return {"lang": "de", "headword": entry["headword"], "entry": entry, "entry_sha": entry_patch.sha_of(entry), "read": FINDINGS, "prompt_version": "v4.10", **more}


class Models:
    """Three seats that answer from a script and count their calls."""

    def __init__(self, reply, rulings=(), mend=()):
        self.reply, self.rulings, self.mend, self.calls = reply, list(rulings), list(mend), []

    def __call__(self, seat, user, system=None, schema=None):
        self.calls.append(seat.split(":")[0] + ("" if schema is not review_v4.RULINGS else " rules") + (" mends" if "A third reader has ruled" in user else ""))
        if schema is review_v4.RULINGS:
            return {"rulings": self.rulings}, {"cost_usd": 0.05}
        return {"answers": self.mend if "A third reader has ruled" in user else self.reply}, {"cost_usd": 0.03}


@pytest.fixture
def out(tmp_path, monkeypatch):
    monkeypatch.setattr(review_v4, "OUT", tmp_path)
    monkeypatch.setattr(ingest_v4, "REVIEWED", tmp_path / "review-loop")
    monkeypatch.setattr(ingest_v4, "_reviewed", {})
    return tmp_path


def run(models, entry=ENTRY, table=None, **settings):
    loop = review_v4.Loop(asker=models, answers="chat", **settings)
    it = item(entry)
    review_v4.read(loop, "de", [it])
    got = review_v4.one(loop, it, table)
    record = json.loads((loop.dir("de") / f"{entry['headword']}.{it['entry_sha']}.outcome.json").read_text(encoding="utf-8"))
    return loop, got, record


ACCEPT_GENDER = {"n": 1, "verdict": "accept", "reason": "Haus is neuter.", "patch": [{"op": "replace", "path": "/lexemes/0/grammar/gender", "value": "\"Neut\""}]}
REJECT = [{"n": 2, "verdict": "reject", "reason": "A firm's name for itself is formal and rare.", "patch": []},
          {"n": 4, "verdict": "reject", "reason": "A learner's dictionary prints it as countable.", "patch": []}]


def test_a_patch_names_places_in_the_entry_as_it_was_before_the_patch():
    ops = [{"op": "remove", "path": "/lexemes/0/senses/0/collocations/0"}, {"op": "replace", "path": "/lexemes/0/senses/0/collocations/2", "value": "nach Hause gehen"},
           {"op": "add", "path": "/lexemes/0/senses/1", "value": NEW_SENSE}, {"op": "add", "path": "/lexemes/0/senses/-", "value": {"definition": "letzter"}},
           {"op": "replace", "path": "/lexemes/0/senses/1/cefr", "value": "C2"}, {"op": "add", "path": "/lexemes/0/grammar/note", "value": "x"}]
    lexeme = entry_patch.apply_ops(ENTRY, ops)["lexemes"][0]
    assert lexeme["senses"][0]["collocations"] == ["ein Haus bauen", "nach Hause gehen"]
    assert [s["definition"] for s in lexeme["senses"]] == ["Gebäude, in dem Menschen wohnen", "Firma oder Einrichtung", "Familie von hohem Rang", "letzter"]
    assert lexeme["senses"][2]["cefr"] == "C2" and lexeme["grammar"]["note"] == "x"  # the sense that stood second, wherever it stands now
    assert ENTRY["lexemes"][0]["senses"][0]["collocations"][0] == "zu Hause"  # the entry given is not touched
    for bad in ({"op": "replace", "path": "/lexemes/3/pos", "value": "VERB"}, {"op": "remove", "path": "/lexemes/0/grammar/aspect"},
                {"op": "replace", "path": "/lexemes/0/grammar/gender", "value": "Neut", "was": "Fem"}, {"op": "add", "path": "/lexemes/0/grammar/gender", "value": "Neut"},
                {"op": "move", "path": "/lexemes/0"}, {"op": "replace", "path": "lexemes/0/pos", "value": "VERB"}):
        with pytest.raises(entry_patch.PatchError):
            entry_patch.apply_ops(ENTRY, [bad])


def test_what_both_models_call_a_fault_is_mended_and_what_the_third_dismisses_is_not(out):
    """Only the faults the bar counts go to the writer; an accepted one is patched; a rejected one goes to the third
    model, whose ruling for the writer leaves the entry alone and whose ruling for the reader sends it back to be mended."""
    models = Models([ACCEPT_GENDER] + REJECT, rulings=[{"n": 2, "upheld": True, "reason": "Every learner's dictionary has it."}, {"n": 4, "upheld": False, "reason": "Count is what is printed."}],
                    mend=[{"n": 2, "verdict": "accept", "reason": "Adds the sense.", "patch": [{"op": "add", "path": "/lexemes/0/senses/-", "value": json.dumps(NEW_SENSE)}]}])
    loop, got, record = run(models)
    assert got == "settled" and models.calls == ["claude", "claude rules", "claude mends"]
    assert [(o["fault"], o["end"], o.get("by")) for o in record["outcomes"]] == [("fact", "mended", "both"), ("missing-core", "mended", "judge"), ("claim", "dismissed", None)]
    assert [(op["n"], op["op"], op["path"], op.get("was")) for op in record["ops"]] == [(1, "replace", "/lexemes/0/grammar/gender", "Masc"), (2, "add", "/lexemes/0/senses/-", None)]
    assert "[wording]" not in json.loads(next(loop.dir("de").glob("*.reply.*.json")).read_text(encoding="utf-8"))["asked"]  # not looped unless asked for
    assert loop.bought == {"reply": 1, "rule": 1, "mend": 1} and round(sum(loop.cost.values()), 2) == 0.11


def test_a_second_run_buys_nothing_and_another_text_is_another_entry(out):
    models = Models([ACCEPT_GENDER] + REJECT, rulings=[{"n": 2, "upheld": False, "reason": "rare"}, {"n": 4, "upheld": False, "reason": "printed so"}])
    run(models)
    assert len(models.calls) == 2
    loop, got, record = run(models)
    assert got == "settled" and len(models.calls) == 2 and not loop.bought and record["ops"]  # every answer was on file
    other = copy.deepcopy(ENTRY)
    other["lexemes"][0]["senses"][0]["example"] = "Das Haus ist alt."
    run(models, other)
    assert len(models.calls) == 4  # written again, read again


def test_a_dry_run_and_a_step_left_out_ask_nothing(out):
    models = Models([ACCEPT_GENDER] + REJECT)
    loop, got, record = run(models, dry=True)
    assert got == "waits for reply" and not models.calls and loop.bought == {"reply to ask": 1} and not record["ops"]
    loop, got, record = run(models, steps=("read", "reply"))
    assert got == "waits for rule" and models.calls == ["claude"] and [o["end"] for o in record["outcomes"]] == ["mended", "disputed, not ruled yet", "disputed, not ruled yet"]
    assert record["ops"]  # what both agreed on does not wait for the third model


def test_the_plans_wall_stops_the_run_and_is_not_an_answer(out):
    def wall(seat, user, system=None, schema=None):
        raise review_v4.Wall("You've hit your 5-hour limit · resets 3am")
    loop, got, record = run(wall)
    assert got == "wall" and loop.wall and loop.stopped() and not list(loop.dir("de").glob("*.reply.*.json"))
    assert [o["end"] for o in record["outcomes"]] == ["unanswered"] * 3
    assert review_v4.WALL.search("You've hit your 5-hour limit") and review_v4.WALL.search("You've hit your usage limit") and not review_v4.WALL.search("invalid JSON schema")
    models = Models([ACCEPT_GENDER] + REJECT, rulings=[{"n": 2, "upheld": False, "reason": "r"}, {"n": 4, "upheld": False, "reason": "r"}])
    assert run(models)[1] == "settled"  # the next run asks what the wall kept back


def test_a_patch_that_breaks_the_shape_or_goes_against_the_sources_is_refused(out, monkeypatch):
    broken = {"n": 2, "verdict": "accept", "reason": "Adds it.", "patch": [{"op": "add", "path": "/lexemes/0/senses/-", "value": json.dumps({"definition": "Firma"})}]}
    nowhere = {"n": 4, "verdict": "accept", "reason": "Both.", "patch": [{"op": "replace", "path": "/lexemes/1/grammar/countability", "value": "\"Both\""}]}
    # the sources have Haus as neuter: a patch to feminine moves a fact away from them, one to neuter does not exist to refuse
    right = copy.deepcopy(ENTRY)
    right["lexemes"][0]["grammar"]["gender"] = "Neut"
    table = {"Haus": ({"Neut"}, [], set(), {"wiktionary"})}
    wrong = {"n": 1, "verdict": "accept", "reason": "Feminine.", "patch": [{"op": "replace", "path": "/lexemes/0/grammar/gender", "value": "\"Fem\""}]}
    loop, got, record = run(Models([wrong, broken, nowhere]), right, table)
    ends = [(o["fault"], o["end"], (o.get("refused") or "")[:22]) for o in record["outcomes"]]
    assert ends == [("fact", "patch refused", "against the sources: H"), ("missing-core", "patch refused", "breaks the shape at le"), ("claim", "patch refused", "cannot be applied: /le")]
    assert not record["ops"]


def test_a_planted_fault_counts_as_put_right_when_the_entry_is_what_it_was(out):
    original = copy.deepcopy(ENTRY)
    original["lexemes"][0]["grammar"]["gender"] = "Neut"
    loop = review_v4.Loop(asker=Models([ACCEPT_GENDER] + REJECT, rulings=[{"n": 2, "upheld": False, "reason": "r"}, {"n": 4, "upheld": False, "reason": "r"}]), answers="chat")
    it = item(part="planted", original=original)
    review_v4.read(loop, "de", [it])
    review_v4.one(loop, it, None)
    record = json.loads(next(loop.dir("de").glob("*.outcome.json")).read_text(encoding="utf-8"))
    assert record["restored"] is True and record["part"] == "planted"
    review_v4.report("review-loop", ["de"])


def test_ingest_applies_an_outcome_to_the_written_text_it_was_made_for_and_to_no_other(out):
    raw = copy.deepcopy(ENTRY)
    clean = ingest_v4.clean_entry("de", copy.deepcopy(raw), {}, review=False)
    record = {"headword": "Haus", "raw_sha": entry_patch.sha_of(raw), "ops": [
        {"n": 1, "op": "replace", "path": "/lexemes/0/grammar/gender", "value": "Neut", "was": "Masc"},
        {"n": 2, "op": "add", "path": "/lexemes/0/senses/-", "value": NEW_SENSE},
        {"n": 3, "op": "replace", "path": "/lexemes/0/senses/1/cefr", "value": "B2", "was": "A2"}]}  # the entry no longer has what this one saw
    folder = out / "review-loop" / "de"
    folder.mkdir(parents=True)
    (folder / f"Haus.{entry_patch.sha_of(clean)}.outcome.json").write_text(json.dumps(record))
    published = ingest_v4.clean_entry("de", copy.deepcopy(raw), {})
    lexeme = published["lexemes"][0]
    assert lexeme["grammar"]["gender"] == "Neut" and published["faults_mended"] == 2 and lexeme["senses"][1]["cefr"] == "C1"
    assert lexeme["senses"][2]["definition"] == "Firma oder Einrichtung" and lexeme["senses"][2]["id"] == ingest_v4.sense_id(lexeme["id"], "Firma oder Einrichtung")
    assert raw == ENTRY  # the written entry is as it was
    again = copy.deepcopy(raw)
    again["lexemes"][0]["senses"][0]["example"] = "Das Haus ist alt."  # written again: the outcome is of another text
    assert "faults_mended" not in ingest_v4.clean_entry("de", again, {}) and ingest_v4.clean_entry("de", copy.deepcopy(again), {})["lexemes"][0]["grammar"]["gender"] == "Masc"


def test_a_right_entry_with_a_false_finding_is_the_measure_of_a_writer_that_only_agrees(out, monkeypatch, capsys):
    """The value a planted fault put there, asserted as the truth against the entry as written. The loop is sound
    when the writer rejects it and the third model dismisses it; the report counts where either did not."""
    right = copy.deepcopy(ENTRY)
    right["lexemes"][0]["grammar"]["gender"] = "Neut"
    monkeypatch.setattr(review_v4.measure, "MEASURE", out / "measure")
    (out / "measure" / "s").mkdir(parents=True)
    (out / "measure" / "s" / "sample-de.json").write_text(json.dumps([{"id": 1, "headword": "Haus", "part": "planted"}, {"id": 2, "headword": "Tisch", "part": "new"}]))
    (out / "measure" / "s" / "planted-de.json").write_text(json.dumps({"Haus": {"kind": "gender", "was": "Neut", "now": "Masc", "entry": ENTRY}}))
    monkeypatch.setattr(ingest_v4, "load_raw", lambda lang: {"Haus": (right, {"prompt_version": "v4.10"})})
    rows = review_v4.decoy_items("s", "de")
    assert [(r["headword"], r["part"], r["entry"]["lexemes"][0]["grammar"]["gender"]) for r in rows] == [("Haus", "decoy", "Neut")]
    assert rows[0]["read"][0]["what"] == "Haus is a masculine noun in standard German, where the entry gives neuter."
    loop = review_v4.Loop("review-loop-decoys", asker=Models([{"n": 1, "verdict": "reject", "reason": "Duden: das Haus.", "patch": []}], rulings=[{"n": 1, "upheld": False, "reason": "Neuter."}]), answers="decoys")
    review_v4.read(loop, "de", rows)
    assert review_v4.one(loop, rows[0], None) == "settled"
    review_v4.report("review-loop-decoys", ["de"])
    assert "decoy: 1 entries, 1 with a finding looped, 0 changed; the false finding accepted by the writer in 0, upheld by the third in 0" in capsys.readouterr().out
