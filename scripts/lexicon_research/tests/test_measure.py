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


def noun(lemma, ipa, grammar, reading=None, senses=1):
    said = {"variety": "it-IT", "ipa": ipa, **({"reading": reading} if reading else {})}
    return {"headword": lemma, "lexemes": [{"lemma": lemma, "pos": "NOUN", "grammar": dict(grammar), "pronunciations": [said],
                                           "senses": [{"definition": f"d{n}", "example": f"e{n}", "cefr": "A2"} for n in range(1, senses + 1)]}]}


def test_a_planted_fault_is_one_a_reader_can_show():
    """A fault put in to measure a reader must be a fault beyond dispute: a gender only where Wiktionary has the
    entry's own and no other, a stress only where it has ours and not the one planted, Mass only on a noun it
    gives a plural, two examples changed over only between two senses."""
    word = noun("catena", "/ka.ˈte.na/", {"gender": "Fem", "countability": "Count"}, "caténa", senses=2)
    facts = {"catena": ({"Fem"}, [(set(), "/kaˈte.na/", False)], {"catene"}, {"Wiktionary"})}
    planted, what = measure.plant("it", word, "gender", facts)
    assert planted["lexemes"][0]["grammar"]["gender"] == "Masc" and what == {"kind": "gender", "where": "lexeme 1 grammar", "was": "Fem", "now": "Masc"}
    assert word["lexemes"][0]["grammar"]["gender"] == "Fem"  # the entry itself is not touched
    assert measure.plant("it", word, "gender", {"catena": ({"Fem", "Masc"}, [], set())}) is None  # a noun of two genders is no test
    planted, what = measure.plant("it", word, "stress", facts)
    assert planted["lexemes"][0]["pronunciations"][0] == {"variety": "it-IT", "ipa": "/ka.te.ˈna/", "reading": "catenà"}
    assert what["was"] == "/ka.ˈte.na/" and what["now"] == "/ka.te.ˈna/"
    two_ways = {"catena": ({"Fem"}, [(set(), "/kaˈte.na/", False), (set(), "/ka.teˈna/", False)], set())}
    assert measure.plant("it", word, "stress", two_ways) is None  # Wiktionary has the planted stress too
    assert measure.plant("it", word, "countability", facts)[0]["lexemes"][0]["grammar"]["countability"] == "Mass"
    assert measure.plant("it", word, "countability", {"catena": ({"Fem"}, [], set())}) is None  # no plural known: Mass may be right
    planted, what = measure.plant("it", word, "example", facts)
    assert [s["example"] for s in planted["lexemes"][0]["senses"]] == ["e2", "e1"] and what["where"] == "lexeme 1 senses 1 and 2"
    assert measure.plant("it", noun("re", "/ˈre/", {"gender": "Masc"}), "example", facts) is None  # one sense: nothing to change over
    assert measure.moved_stress("/ˈre/") is None and measure.moved_stress("/ˌa.ɡɾo.ˈpe/") is None
    assert measure.moved_reading("ru", "ме́бель", 0, 1, 2) == "мебе́ль" and measure.moved_reading("ru", "ёлка", 0, 1, 2) is None


def test_the_tally_measures_the_reader_and_keeps_its_test_entries_out_of_the_count(tmp_path, monkeypatch, capsys):
    monkeypatch.setattr(measure, "MEASURE", tmp_path)
    (tmp_path / "t" / "answers").mkdir(parents=True)
    (tmp_path / "t" / "answers-2").mkdir()
    items = [{"id": 1, "headword": "a", "part": "new", "witness": ["sounds: a"]}, {"id": 2, "headword": "b", "part": "planted"},
             {"id": 3, "headword": "c", "part": "planted"}, {"id": 4, "headword": "d", "part": "gold"}, {"id": 5, "headword": "e", "part": "new"}]
    (tmp_path / "t" / "sample-it.json").write_text(json.dumps(items))
    (tmp_path / "t" / "planted-it.json").write_text(json.dumps({"b": {"kind": "gender", "was": "Fem", "now": "Masc"}, "c": {"kind": "stress", "was": "/ˈa.b/", "now": "/a.ˈb/"}}))
    (tmp_path / "t" / "answers" / "it-1.md").write_text("\n".join([
        '{"id": 1, "findings": [{"fault": "fact", "where": "lexeme 1 said", "what": "the vowel is open", "misleads": true}]}',
        '{"id": 2, "findings": [{"fault": "fact", "where": "lexeme 1 grammar", "what": "the noun is feminine", "misleads": true}]}',
        '{"id": 3, "findings": [{"fault": "wording", "where": "sense 1", "what": "definition too narrow", "misleads": false}]}',
        '{"id": 4, "findings": [{"fault": "claim", "where": "sense 1 labels", "what": "not formal", "misleads": false}]}',
        '{"id": 5, "findings": []}']))
    (tmp_path / "t" / "answers-2" / "it-1.md").write_text('{"id": 1, "findings": [{"fault": "fact", "where": "lexeme 1 said", "what": "open e"}]}\n'
                                                          '{"id": 5, "findings": [{"fault": "wording", "where": "sense 1", "what": "narrow"}]}')
    measure.tally("t", ["it"])
    out = capsys.readouterr().out
    assert "all  both  n=  2" in out and "fact 50%" in out  # the two real entries only: a planted or checked entry is not the dictionary
    assert "planted gender        found 1 of 1" in out and "planted stress        found 0 of 1" in out
    assert "missed: it c: stress /ˈa.b/ -> /a.ˈb/; reported something else" in out
    assert "entries checked by hand: a finding in 1 of 1" in out and "it d: claim, not formal" in out
    assert "entries Wiktionary contradicts: a fact reported in 1 of 1" in out
    assert "two readers, fact          named by both in 1 of the 1" in out and "two readers, wording       named by both in 0 of the 1" in out


def test_the_same_words_written_a_second_way_are_read_and_compared_entry_by_entry(tmp_path, monkeypatch, capsys):
    monkeypatch.setattr(measure, "MEASURE", tmp_path)
    for name in ("a", "b"):
        (tmp_path / name / "answers").mkdir(parents=True)
    first = [{"id": 1, "headword": "x", "zipf": 5.0, "part": "planted"}, {"id": 2, "headword": "y", "zipf": 5.0, "part": "new"},
             {"id": 3, "headword": "z", "zipf": 4.0, "part": "new"}, {"id": 4, "headword": "g", "zipf": 4.0, "part": "gold"}]
    (tmp_path / "a" / "sample-it.json").write_text(json.dumps(first))
    measure.sample_like("b", "a", "review-trial", ["it"], gold=5)
    second = json.loads((tmp_path / "b" / "sample-it.json").read_text())
    assert [(i["id"], i["headword"], i["part"], i["pair"], i.get("folder")) for i in second] == [
        (1, "y", "new", 2, "review-trial"), (2, "z", "new", 3, "review-trial"), (3, "g", "gold", 4, None)]  # no planted entry a second time
    (tmp_path / "a" / "answers" / "it-1.md").write_text('{"id": 2, "findings": [{"fault": "fact", "what": "w"}]}\n{"id": 3, "findings": []}')
    (tmp_path / "b" / "answers" / "it-1.md").write_text('{"id": 1, "findings": []}\n{"id": 2, "findings": [{"fault": "wording", "what": "w"}]}')
    measure.pair("b", "a", ["it"])
    out = capsys.readouterr().out
    assert "2 entries read in both" in out and "fact           50.0 ->   0.0   gone in 1, new in 0" in out and "wording         0.0 ->  50.0   gone in 0, new in 1" in out
