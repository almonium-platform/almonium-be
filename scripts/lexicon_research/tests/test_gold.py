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
    ("en", "v4.5"): "01e3c8b0c49c", ("de", "v4.5"): "3a6bbf9411f3", ("fr", "v4.5"): "cb91c0a2c441", ("es", "v4.5"): "49554113c543", ("it", "v4.5"): "1696c5fba6f0", ("pt", "v4.5"): "a55595cc20b8",
    ("en", "v4.6"): "646cc0969f58", ("de", "v4.6"): "46fa8ef481dd", ("fr", "v4.6"): "64d5e683fb3a", ("es", "v4.6"): "a760a331ee23", ("it", "v4.6"): "fc32ca7cc518", ("pt", "v4.6"): "b00e003b42a2",
    ("en", "v4.7"): "7f06d29d61de", ("de", "v4.7"): "a48e9cbea96f", ("fr", "v4.7"): "ecf8f8c27d3b", ("es", "v4.7"): "8a253d1aa72f", ("it", "v4.7"): "01f677cf0983", ("pt", "v4.7"): "635fcd1314f9",
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


def test_our_own_correction_is_applied_on_top_of_the_reviewers_and_must_lead_somewhere(tmp_path, monkeypatch):
    monkeypatch.setattr(gold_candidates, "GOLD", tmp_path)
    (tmp_path / "candidates").mkdir()
    (tmp_path / "candidates" / "en.json").write_text("[]")
    (tmp_path / "own").mkdir()
    answer = tmp_path / "answer.md"
    answer.write_text(ANSWER, encoding="utf-8")
    own = [{"headword": "hill", "path": "lexemes.0.senses.0.cefr", "value": "A1", "why": "a test"}]
    (tmp_path / "own" / "en.json").write_text(json.dumps(own))
    assert gold_candidates.take("en", answer, ["hill"])[0]["lexemes"][0]["senses"][0]["cefr"] == "A1"
    own[0]["path"] = "lexemes.0.senses.0.grammar.note"
    (tmp_path / "own" / "en.json").write_text(json.dumps(own))
    with pytest.raises(KeyError):
        gold_candidates.take("en", answer, ["hill"])


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
    assert "# Examples" not in prompting.system_prompt("fr", "v4.4") and '"headword": "hôpital"' in prompting.system_prompt("fr", "v4.5")
    assert "# Examples" not in prompting.system_prompt("it", "v4.5") and '"headword": "museo"' in prompting.system_prompt("it", "v4.6")
    assert "# Examples" not in prompting.system_prompt("pt", "v4.5") and '"headword": "computador"' in prompting.system_prompt("pt", "v4.6")
    assert set(prompting.GOLD_SINCE) == set(prompting.TARGET_LANGUAGES)  # from v4.6 every language sees its own examples
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
    for lang, word in (("fr", "être"), ("it", "essere")):
        assert f"is conjugated with {word}" in prompting.system_prompt(lang, "v4.5")
        assert f"is conjugated with {word}" not in prompting.system_prompt(lang, "v4.4")


def test_a_variety_keeps_its_old_sound_conventions_in_the_older_prompts():
    assert "ll as ʎ" in prompting.sounds_v4("es-ES", "v4.5") and " ʎ " in prompting.sounds_v4("es-ES", "v4.5")
    assert "ll and y are both ʝ" in prompting.sounds_v4("es-ES", "v4.6") and " ʎ " not in prompting.sounds_v4("es-ES", "v4.6").split(";")[0]
    assert "/ˈfatːo/" in prompting.sounds_v4("it-IT", "v4.5") and "/ˈfat.to/" in prompting.sounds_v4("it-IT", "v4.6")
    assert "ː" not in prompting.sounds_v4("it-IT", "v4.6") and prompting.sounds_v4("en-US", "v4.5") == prompting.sounds_v4("en-US", "v4.6")
    assert "unstressed o is u" in prompting.sounds_v4("pt-PT", "v4.6") and "never the rule alone" in prompting.sounds_v4("pt-PT", "v4.7")
    assert " i u; marks" in prompting.sounds_v4("en-GB", "v4.7") and "happy" not in prompting.sounds_v4("en-GB", "v4.6")
    assert '"mainly": ["position"]' in prompting.system_prompt("en", "v4.7") and '"mainly": [' not in prompting.system_prompt("en", "v4.6")


def test_a_prompt_that_entries_were_written_with_still_renders_to_the_letter():
    """The hash is in every entry's meta. A change to a prompt file, a rule table or an example file that moves
    one of these belongs in a new version."""
    assert {(lang, v): prompting.prompt_sha(lang, v) for lang, v in PINNED} == PINNED


def test_a_language_without_an_alphabet_gets_a_reading_and_limits_in_characters():
    ja = prompting.system_prompt("ja")
    assert "Broad IPA" not in ja and "Use only these symbols" not in ja and '"notation": "kana"' in ja
    assert "10 to 35 characters" in ja and "of 12 to 40 characters" in ja and "6 to 15 words" not in ja
    assert ja.index("# Spellings") < ja.index("# Pronunciation") < ja.index("# Grammar and forms") < ja.index("# Senses")
    assert "Broad IPA" in prompting.system_prompt("ru") and "6 to 15 words" in prompting.system_prompt("en")
    assert set(prompting.PILOT_LANGUAGES).isdisjoint(prompting.TARGET_LANGUAGES)
