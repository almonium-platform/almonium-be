"""The example entries: read from a reviewer's answer, checked, and shown by the prompt. Run from the repository root:
    scripts/.venv/bin/python -m pytest scripts/lexicon_research/tests -q
"""

import json
import sys
from pathlib import Path

import jsonschema
import pytest

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
import check_v4  # noqa: E402
import gold_candidates  # noqa: E402
import prompting  # noqa: E402

HERE = Path(__file__).resolve().parents[1]
PINNED = {
    ("en", "v4.1"): "13c05636bb59", ("de", "v4.1"): "37779ccdeab0", ("fr", "v4.1"): "11b311efafbd", ("es", "v4.1"): "b7ae78b7d106", ("it", "v4.1"): "46fc6f88d993", ("pt", "v4.1"): "a51ff1e5df59",
    ("en", "v4.2"): "fc17d9bf16b5", ("de", "v4.2"): "b6d36680966c", ("fr", "v4.2"): "6253b994ff5b", ("es", "v4.2"): "2a49019b3872", ("it", "v4.2"): "2fec220b2a1d", ("pt", "v4.2"): "21f761ff10d5",
    ("en", "v4.3"): "e6615ab40a6b", ("de", "v4.3"): "bc8cfbca1124", ("fr", "v4.3"): "5418c0d89d60", ("es", "v4.3"): "7cf0386d9950", ("it", "v4.3"): "0c8f05818753", ("pt", "v4.3"): "58417a4d2947",
    ("en", "v4.4"): "ea3693cc8e6c", ("de", "v4.4"): "264072a754c8", ("fr", "v4.4"): "b42f719d2bef", ("es", "v4.4"): "9f4ac7938249", ("it", "v4.4"): "e0a358f349be", ("pt", "v4.4"): "fb3233860b89",
}
ANSWER = """# Review

### 1. hill

**use with corrections** — valley is not a dependable antonym.

```json
{"language": "en", "headword": "hill", "lexemes": [{"lemma": "hill", "pos": "NOUN", "kind": "word",
 "components": [{"text": "hi", "lemma": "hi", "inflection": "fixed"}, {"text": "ll", "lemma": "ll", "inflection": "fixed"}],
 "pronunciations": [{"variety": "en-US", "ipa": "/ˈhɪl/"}],
 "senses": [{"definition": "an area of land that is higher than the land around it", "example": "We climbed to the top of the hill to watch the sunset.", "cefr": "A2"}]}]}
```

### 2. lake (noun)

**use as it is** — clear.

### 3. write

**do not use** — needs rebuilding.
"""


def test_a_reviewers_answer_is_read_by_headword_with_its_verdict():
    answers = gold_candidates.reviewed(ANSWER)
    assert {k: v[0] for k, v in answers.items()} == {"hill": "use with corrections", "lake": "use as it is", "write": "do not use"}
    assert answers["lake"][1] is None and answers["hill"][1]["headword"] == "hill"
    assert gold_candidates.settle(answers["hill"][1])["lexemes"][0]["components"][0] == {"text": "hi", "lemma": "hi", "inflects": "fixed"}


def test_an_answer_with_larger_headings_and_a_closing_section_is_read_too():
    answer = """# Review

| # | Candidate | Verdict |
|---|---|---|
| 1 | lunes | **use as it is** |

## 1. lunes

**use as it is** — clear.

## 2. matar

**do not use** — rebuild.

## Final demonstration set

**use with corrections** is what the others need.

```json
{"not": "an entry"}
```
"""
    assert gold_candidates.reviewed(answer) == {"lunes": ("use as it is", None), "matar": ("do not use", None)}


def test_an_example_gets_the_dots_its_reviewer_left_out(tmp_path, monkeypatch):
    monkeypatch.setattr(gold_candidates, "GOLD", tmp_path)
    (tmp_path / "candidates").mkdir()
    candidate = {"language": "en", "headword": "allow", "lexemes": [{"lemma": "allow", "pos": "VERB", "kind": "word",
                 "pronunciations": [{"variety": "en-US", "ipa": "/əˈlaʊ/"}],
                 "senses": [{"definition": "to let someone do something they want to do", "example": "My parents never allow me to stay out late.", "cefr": "A2"}]}]}
    (tmp_path / "candidates" / "en.json").write_text(json.dumps([{"kind": "verb", "zipf": 5.0, "entry": candidate}]))
    answer = tmp_path / "answer.md"
    answer.write_text("## 1. allow\n\n**use as it is** — fine.\n", encoding="utf-8")
    assert gold_candidates.take("en", answer, ["allow"])[0]["lexemes"][0]["pronunciations"][0]["ipa"] == "/ə.ˈlaʊ/"


def test_an_entry_the_reviewer_refused_never_becomes_an_example(tmp_path, monkeypatch):
    monkeypatch.setattr(gold_candidates, "GOLD", tmp_path)
    (tmp_path / "candidates").mkdir()
    (tmp_path / "candidates" / "en.json").write_text("[]")
    answer = tmp_path / "answer.md"
    answer.write_text(ANSWER, encoding="utf-8")
    with pytest.raises(SystemExit, match="write: the reviewer's verdict is do not use"):
        gold_candidates.take("en", answer, ["write"])
    with pytest.raises(SystemExit, match="lake: no corrected entry"):
        gold_candidates.take("en", answer, ["lake"])
    assert [e["headword"] for e in gold_candidates.take("en", answer, ["hill"])] == ["hill"]


def test_the_examples_in_the_repository_are_valid_and_faultless():
    schema = json.loads((HERE / "entry.schema.v4.json").read_text())
    for path in sorted((HERE / "gold").glob("??.json")):
        lang = path.stem
        inventories = {v: check_v4.symbols(v) for v in prompting.VARIETIES[lang]}
        for entry in json.loads(path.read_text(encoding="utf-8")):
            jsonschema.validate(entry, schema)
            assert check_v4.check(lang, entry, inventories, None) == [], entry["headword"]


def test_a_language_is_shown_its_examples_from_the_version_that_introduced_them():
    assert "# Examples" in prompting.system_prompt("en", "v4.3") and '"headword": "lake"' in prompting.system_prompt("en", "v4.3")
    assert "# Examples" not in prompting.system_prompt("en", "v4.2")
    assert "# Examples" not in prompting.system_prompt("de", "v4.3") and '"headword": "nötig"' in prompting.system_prompt("de", "v4.4")
    assert "# Examples" not in prompting.system_prompt("es", "v4.3") and '"headword": "lunes"' in prompting.system_prompt("es", "v4.4")
    for lang in prompting.TARGET_LANGUAGES:
        if lang not in prompting.GOLD_SINCE:
            assert "# Examples" not in prompting.system_prompt(lang)
        else:
            assert (HERE / "gold" / f"{lang}.json").exists()


def test_a_text_changed_in_a_version_does_not_reach_the_older_ones():
    assert "das Sterben" in prompting.system_prompt("de", "v4.4") and "das Sterben" not in prompting.system_prompt("de", "v4.3")
    assert "fallschirm" in prompting.system_prompt("de", "v4.3") and "fallschirm" in prompting.system_prompt("de", "v4.4")
    assert "1959" in prompting.system_prompt("es", "v4.4") and "1959" not in prompting.system_prompt("es", "v4.3")
    assert "sólo as an adverb, guión and fué are pre-reform" in prompting.system_prompt("es", "v4.3")


def test_a_prompt_that_entries_were_written_with_still_renders_to_the_letter():
    """The hash is in every entry's meta. A change to a prompt file, a rule table or an example file that moves
    one of these belongs in a new version."""
    assert {(lang, v): prompting.prompt_sha(lang, v) for lang, v in PINNED} == PINNED
