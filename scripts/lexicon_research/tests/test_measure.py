"""The measurement: a reviewer's answer read into rates. Run from the repository root:
    scripts/.venv/bin/python -m pytest scripts/lexicon_research/tests -q
"""

import json
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
import measure  # noqa: E402

ENTRY = {"language": "de", "headword": "Tor", "lexemes": [
    {"lemma": "Tor", "pos": "NOUN", "kind": "word", "grammar": {"gender": "Neut", "plural_class": "-e"},
     "pronunciations": [{"variety": "de-DE", "ipa": "/ˈtoːɐ/"}],
     "forms": [{"form": "Tore", "features": {"Case": "Nom", "Number": "Plur"}, "ipa": {"de-DE": "/ˈtoː.ʁə/"}}],
     "senses": [{"definition": "große Tür in einer Mauer oder einem Zaun", "example": "Am Abend wird das Tor immer abgeschlossen.", "cefr": "A2",
                 "frames": ["das Tor zu + Dat"], "labels": {"figurative": True, "style": ["literary"]},
                 "regions": [{"variety": "de-AT", "extent": "mainly"}], "antonyms": [{"lemma": "Mauer", "pos": "NOUN"}]}],
     "relations": [{"type": "confusable", "lemma": "Tür", "pos": "NOUN", "why": "a Tor is larger"}]}]}

ANSWER = """Here are the findings.

```json
{"id": 1, "findings": []}
{"id": 2, "findings": [{"fault": "claim", "where": "sense 1 regions", "what": "also used in Germany", "misleads": true},
{"id": 3, "findings": [{"fault": "fact", "where": "lexeme 1 grammar", "what": "the noun is feminine", "misleads": true}, {"fault": "wording", "where": "sense 1", "what": "too narrow", "misleads": false}]}
{"id": 4, "findings": [{"fault": "style", "where": "sense 1", "what": "not a fault we count"}]},
```
"""


def test_an_entry_is_shown_with_everything_it_states():
    text = measure.show(ENTRY)
    for stated in ("**Lexeme 1: Tor**, NOUN", "gender Neut", "/ˈtoːɐ/ (de-DE)", "Tore [Case=Nom, Number=Plur] /ˈtoː.ʁə/", "1. [A2] große Tür",
                   "example: Am Abend", "frames: das Tor zu + Dat", "figurative True", "style literary", "regions: de-AT mainly",
                   "opposites: Mauer", "confusable Tür (NOUN): a Tor is larger"):
        assert stated in text, stated


def test_an_answer_is_read_line_by_line_and_only_the_named_faults_count():
    read = measure.answers_of(ANSWER)
    assert sorted(read) == [1, 3, 4]  # the second line is cut off and is not guessed at
    assert read[1] == [] and [f["fault"] for f in read[3]] == ["fact", "wording"] and read[4] == []


def test_the_tally_counts_entries_with_a_fault_and_holds_them_against_the_bar(tmp_path, monkeypatch, capsys):
    monkeypatch.setattr(measure, "MEASURE", tmp_path)
    (tmp_path / "t" / "answers").mkdir(parents=True)
    items = [{"id": n, "headword": f"w{n}", "zipf": 4.0, "part": "have" if n % 2 else "new"} for n in range(1, 5)]
    items[3]["rejected"] = True  # a word the writer refused is not an entry to find faults in
    (tmp_path / "t" / "sample-de.json").write_text(json.dumps(items))
    (tmp_path / "t" / "answers" / "de-1.md").write_text(ANSWER)
    measure.tally("t", ["de", "fr"])
    out = capsys.readouterr().out
    assert "de: 3 of 4 entries answered" in out
    assert "all  both  n=  2  clean 50%" in out and "fact 50% (50% misleading)" in out
    assert "fact          50.0  aim: at most 2  not met" in out and "claim          0.0  aim: at most 5  met" in out


def test_a_bad_collocation_is_a_fault_of_its_own_and_does_not_move_the_bar():
    assert measure.FAULTS[:6] == ("fact", "claim", "missing-core", "missing-minor", "split", "wording") and "collocation" in measure.FAULTS
    assert set(measure.BAR) == {"fact", "claim", "missing-core"}  # what the earlier counts were held to stays what it was
    text = measure.REVIEW.format(name="German", part=1, parts=1, ours="", entries="")
    assert "- **collocation**: " in text and 'Quote the collocation in "what"' in text
    found = measure.answers_of('{"id": 3, "findings": [{"fault": "collocation", "where": "sense 1", "what": "\\"starker Regen\\" is fine, \\"großer Regen\\" is not said", "misleads": true}]}')
    assert [f["fault"] for f in found[3]] == ["collocation"]
