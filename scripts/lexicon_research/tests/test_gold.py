"""The example entries: read from a reviewer's answer, checked, and shown by the prompt. Run from the repository root:
    scripts/.venv/bin/python -m pytest scripts/lexicon_research/tests -q
"""

import json
import re
import sys
from pathlib import Path

import jsonschema
import pytest

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
import check_v4  # noqa: E402
import gold_candidates  # noqa: E402
import ingest_v4  # noqa: E402
import measure  # noqa: E402
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
    ("ru", "v4.7"): "1810f2bff60b",  # Russian is a target language from 18 November 2026; its trial entries carry the hash of the text they saw
    ("en", "v4.8"): "f298d9db6658", ("de", "v4.8"): "b59c0058756f", ("fr", "v4.8"): "486527168595", ("es", "v4.8"): "3230c0c6651d", ("it", "v4.8"): "f4d69f46053b", ("pt", "v4.8"): "8aed3b320562",
    ("ru", "v4.8"): "db13d034811d",
    ("en", "v4.9"): "5e602bd929d2", ("de", "v4.9"): "d7c709f42cfb", ("fr", "v4.9"): "43f042aac05d", ("es", "v4.9"): "c6178b955f4a", ("it", "v4.9"): "b7d74e48cacd", ("pt", "v4.9"): "04fee836e73f",
    ("ru", "v4.9"): "ff654aec68b6",
    ("en", "v4.10"): "4b174049fe16", ("de", "v4.10"): "3b83da400282", ("fr", "v4.10"): "a507e377f954", ("es", "v4.10"): "f8933652c84d", ("it", "v4.10"): "39270e0050b8", ("pt", "v4.10"): "6cbc964f09ff",
    ("ru", "v4.10"): "6e0a52496465",
    # v4.10e is v4.10 with a section on the evidence a request may carry: a trial of 280 words, 19 November 2026
    ("en", "v4.10e"): "9cdb06b4b41b", ("de", "v4.10e"): "a411b3cf6fad", ("fr", "v4.10e"): "d5c21856aa6a", ("es", "v4.10e"): "149125cb9743", ("it", "v4.10e"): "db1186932c70", ("pt", "v4.10e"): "78120b967066",
    ("ru", "v4.10e"): "0e5bf0d7ef22",
    # v4.11 is v4.10 with a clause on a way of saying the word: when it is the one said. 19 November 2026
    ("en", "v4.11"): "545c3ad5b3a0", ("de", "v4.11"): "538c084de06c", ("fr", "v4.11"): "04c9a50402d4", ("es", "v4.11"): "7155181f910f", ("it", "v4.11"): "6f801896c2cf", ("pt", "v4.11"): "46b24b58a24d",
    ("ru", "v4.11"): "fd77e02de637",
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
    for first in sorted((HERE / "gold").glob("??.json")):
        lang = first.stem
        inventories = {v: check_v4.symbols(v) for v in prompting.VARIETIES[lang]}
        for entry in json.loads(first.read_text(encoding="utf-8")):
            jsonschema.validate(entry, schema)
        shown = prompting.gold_path(lang, prompting.PROMPT_VERSION)  # what a writer is shown today has no fault at all
        for entry in json.loads(shown.read_text(encoding="utf-8")):
            jsonschema.validate(entry, schema)
            assert check_v4.check(lang, entry, inventories, None) == [], entry["headword"]


def test_from_v4_8_a_frame_has_an_open_place_and_the_words_company_is_a_collocation():
    assert prompting.gold_path("de", "v4.7") == HERE / "gold" / "de.json" and prompting.gold_path("de", "v4.8") == HERE / "gold" / "v4.8" / "de.json"
    for lang in prompting.TARGET_LANGUAGES + prompting.PILOT_LANGUAGES:
        new, old = prompting.system_prompt(lang, "v4.8"), prompting.system_prompt(lang, "v4.7")
        assert "\nCollocations. " in new and "Collocations. " not in old and '"collocations": [' in new and '"collocations"' not in old
        assert prompting.FRAME_PLACES_V4[lang] in new and prompting.COLLOCATIONS_V4[lang] in new and "{" not in new.split("# Output")[0].split("Frames. ")[1][:1500]
        # the three tables agree: what the prompt tells a writer to write is what ingest reads as an open place and leaves as it is
        text = prompting.FRAME_PLACES_V4[lang]
        frames = re.findall(r'"([^"]+)"', text.split(": ", 1)[1]) if lang != "ja" else text.rsplit(": ", 1)[1].rstrip(".").split(", ")
        assert len(frames) >= 3, lang
        for frame in frames:
            lexeme = {"lemma": "x", "senses": [{"definition": "d", "example": "e", "cefr": "B1", "frames": [frame]}]}
            assert ingest_v4.settle_frames(lang, lexeme) == 0 and ingest_v4.sort_frames(lang, lexeme) == 0, (lang, frame)
        for pair in re.findall(r'"([^"]+)"', prompting.COLLOCATIONS_V4[lang]) or prompting.COLLOCATIONS_V4[lang].strip("()").split(", "):
            lexeme = {"lemma": "x", "senses": [{"definition": "d", "example": "e", "cefr": "B1", "frames": [pair]}]}
            assert ingest_v4.sort_frames(lang, lexeme) == 1 and lexeme["senses"][0] == {"definition": "d", "example": "e", "cefr": "B1", "collocations": [pair]}, (lang, pair)
        # v4.9: one pattern to a frame, the common company up to ten, and a note that hedges; v4.8 is as it was
        newest = prompting.system_prompt(lang, "v4.9")
        assert "One frame is one pattern" in newest and "at most ten" in newest and "One frame is one pattern" not in new and "at most ten" not in new
        assert 'A "note" is held to the same' in newest and 'A "note" is held to the same' not in new
    assert prompting.gold_path("es", "v4.9") == HERE / "gold" / "v4.9" / "es.json" and prompting.gold_path("de", "v4.9") == HERE / "gold" / "v4.8" / "de.json"
    assert "日本人" in prompting.system_prompt("ja", "v4.9") and "日本人" not in prompting.system_prompt("ja", "v4.8")
    for lang in ("ru", "ja"):  # their notes said how a frame is written before every language was told
        assert "not one collocation filled in" not in prompting.system_prompt("ru", "v4.8") and "〜を勉強する" in prompting.system_prompt("ja", "v4.8")


def test_v410_says_what_a_grammar_value_claims_and_what_is_a_frame():
    """The second sample's reviewers reported a value as a claim wherever an exception to it exists, a French adjective
    after its noun as wrongly "before a noun", and phrases that need a complement among the collocations. v4.10 says
    what a value states, what Attr means, and which phrases are frames; v4.9 is as it was written."""
    for lang in prompting.TARGET_LANGUAGES + prompting.PILOT_LANGUAGES:
        new, old = prompting.system_prompt(lang, "v4.10"), prompting.system_prompt(lang, "v4.9")
        for said in ("says how the word is ordinarily used", "on whichever side of the noun", '"in favour of sth", never "in favour of"',
                     "A collocation stands under the sense whose meaning it has"):
            assert said in new and said not in old, (lang, said)
        assert "says what holds without exception" in old and "says what holds without exception" not in new
    assert "Report a value" in measure.REVIEW and "ordinarily used" in measure.REVIEW  # the reviewer is told the same


def test_v410e_is_v410_with_the_evidence_section_and_nothing_else():
    for lang in prompting.TARGET_LANGUAGES:
        plain, shown = prompting.system_prompt(lang, "v4.10"), prompting.system_prompt(lang, "v4.10e")
        head, _, rest = shown.partition("# Evidence\n")
        assert rest and "never its notation" in rest and "do not follow its wording" in rest
        assert head + rest.partition("# Examples")[1] + rest.partition("# Examples")[2] == plain if "# Examples" in plain else head + "# Output" + rest.partition("# Output")[2] == plain
    request = prompting.user_prompt("de", {"headword": "Haus", "zipf": 5.9}, evidence="L1 NOUN · Neut\n  1.1 house")
    assert request.endswith("Evidence (Wiktionary):\nL1 NOUN · Neut\n  1.1 house") and "Evidence" not in prompting.user_prompt("de", {"headword": "Haus", "zipf": 5.9})


def test_v411_says_when_a_way_of_saying_the_word_is_the_one_said():
    """Before v4.11 a second transcription could say how common it is and nothing else, so "the" before a vowel,
    "six" before a consonant and "bath" in the north of England stood beside the first without a word, and a weak
    form was either left out or given a stress mark it does not have."""

    for lang in prompting.TARGET_LANGUAGES:
        new, old = prompting.system_prompt(lang, "v4.11"), prompting.system_prompt(lang, "v4.10")
        head, _, rest = new.partition('Where you know what decides between the ways, say it in "context"')
        clause, _, tail = rest.partition("Use only these symbols:")
        assert "before a vowel sound" in clause and "weak form, usual in running speech" in clause and "without a stress mark" in clause
        assert head + "Use only these symbols:" + tail == old  # and nothing else moved
    validator = jsonschema.Draft202012Validator(json.loads(prompting.SCHEMA_PATH.read_text(encoding="utf-8")))
    entry = json.loads(prompting.schema_example_v4("v4.11"))
    entry["lexemes"][0]["pronunciations"].append({"variety": "en-GB", "ipa": "/liːd/", "context": "weak form, usual in running speech"})
    assert not list(validator.iter_errors(entry))
    entry["lexemes"][0]["pronunciations"][-1]["context"] = "x" * 81  # one short clause, like a form's note
    assert [e.message for e in validator.iter_errors(entry)]
    shown = measure.show({"lexemes": [{"lemma": "the", "pos": "DET", "pronunciations": [
        {"variety": "en-GB", "ipa": "/ˈðiː/"}, {"variety": "en-GB", "ipa": "/ðə/", "context": "weak form, usual in running speech"},
        {"variety": "en-GB", "ipa": "/ˈbæθ/", "note": "regional", "context": "north of England"}], "senses": []}]})
    assert "/ðə/ (en-GB: weak form, usual in running speech)" in shown and "/ˈbæθ/ (en-GB, regional: north of England)" in shown


def test_a_language_is_shown_its_examples_from_the_version_that_introduced_them():
    assert "# Examples" in prompting.system_prompt("en", "v4.3") and '"headword": "lake"' in prompting.system_prompt("en", "v4.3")
    assert "# Examples" not in prompting.system_prompt("en", "v4.2")
    assert "# Examples" not in prompting.system_prompt("de", "v4.3") and '"headword": "nötig"' in prompting.system_prompt("de", "v4.4")
    assert "# Examples" not in prompting.system_prompt("es", "v4.3") and '"headword": "lunes"' in prompting.system_prompt("es", "v4.4")
    assert "# Examples" not in prompting.system_prompt("fr", "v4.4") and '"headword": "hôpital"' in prompting.system_prompt("fr", "v4.5")
    assert "# Examples" not in prompting.system_prompt("it", "v4.5") and '"headword": "museo"' in prompting.system_prompt("it", "v4.6")
    assert "# Examples" not in prompting.system_prompt("pt", "v4.5") and '"headword": "computador"' in prompting.system_prompt("pt", "v4.6")
    assert set(prompting.TARGET_LANGUAGES) <= set(prompting.GOLD_SINCE)  # from v4.6 every target language sees its own examples
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


def test_verdicts_given_only_in_a_table_are_read_and_the_reviewers_numbered_prose_is_not_an_entry():
    answer = """## Verdicts

| Candidate | Verdict | Finding |
|---|---|---|
| 1. **вода** | **do not use** | Misses a sense. |
| 2. **возле** | **use with corrections** | Repeats synonyms. |

| No. | Headword | Verdict |
|---:|---|---|
| 3 | 鉛筆 | **use as it is** |

## Decisions

### 1. Pronunciation

Keep the broad transcription.

### 2. возле

```json
{"language": "ru", "headword": "возле", "lexemes": []}
```
"""
    read = gold_candidates.reviewed(answer)
    assert read == {"вода": ("do not use", None), "возле": ("use with corrections", {"language": "ru", "headword": "возле", "lexemes": []}),
                    "鉛筆": ("use as it is", None)}


def test_an_own_correction_with_a_null_value_takes_the_key_out():
    entry = {"lexemes": [{"grammar": {"countability": "Count", "classifier": "本"}}]}
    gold_candidates.amend(entry, "lexemes.0.grammar.countability", None)
    assert entry == {"lexemes": [{"grammar": {"classifier": "本"}}]}
