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
PINNED = {"v4.1": "13c05636bb59", "v4.2": "fc17d9bf16b5", "v4.3": "e6615ab40a6b"}
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
    for lang in prompting.TARGET_LANGUAGES:
        if lang not in prompting.GOLD_SINCE:
            assert "# Examples" not in prompting.system_prompt(lang, "v4.3")
        else:
            assert (HERE / "gold" / f"{lang}.json").exists()


def test_a_prompt_that_entries_were_written_with_still_renders_to_the_letter():
    """The hash is in every entry's meta. A change to a prompt file, a rule table or an example file that moves
    one of these belongs in a new version."""
    assert {v: prompting.prompt_sha("en", v) for v in PINNED} == PINNED
