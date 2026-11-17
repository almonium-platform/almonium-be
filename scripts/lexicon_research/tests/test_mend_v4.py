"""Mending an older entry into the v4 shape, without a model call. Run from the repository root:
    scripts/.venv/bin/python -m pytest scripts/lexicon_research/tests -q
"""

import sys
from pathlib import Path

import jsonschema
import pytest

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
import mend_v4  # noqa: E402

ABILITY = {
    "language": "en", "headword": "ability",
    "lexemes": [{
        "lemma": "ability", "pos": "NOUN", "kind": "word", "grammar": {"countability": "Both"},
        "forms": [{"form": "ability", "features": {"Number": "Sing"}},
                  {"form": "abilities", "features": {"Number": "Plur"}, "ipa": {"en-US": "/əˈbɪl.ə.tiz/", "en-AU": "/x/"}}],
        "pronunciations": [{"variety": "en-US", "ipa": "/əˈbɪl.ə.ti/"}, {"variety": "en-GB", "ipa": "/əˈbɪl.ɪ.ti/"}],
        "senses": [
            {"definition": "the fact that someone or something can do a particular thing", "register": "neutral", "cefr": "B1",
             "glosses": {"uk": "здатність"}, "example": "She has the ability to explain hard ideas very simply.",
             "cues": [{"kind": "cloze", "text": "She has the ___ to explain hard ideas."}], "usage": "followed by 'to do something'"},
            {"definition": "the level of skill or natural talent that someone has", "register": "literary", "cefr": "B2",
             "glosses": {"uk": "здібності"}, "example": "He is a player of great ability and experience.", "cues": []}],
        "relations": [{"type": "family", "lemma": "able", "pos": "ADJ"}]}]}


def test_the_draft_keeps_what_the_writer_wrote_and_drops_what_v4_leaves_to_later_passes():
    draft = mend_v4.draft("en", ABILITY)
    lexeme = draft["lexemes"][0]

    assert [s["definition"] for s in lexeme["senses"]] == [s["definition"] for s in ABILITY["lexemes"][0]["senses"]]
    assert "glosses" not in lexeme["senses"][0] and "cues" not in lexeme["senses"][0]
    # the one register becomes its axis; neutral is the default and is left out
    assert "labels" not in lexeme["senses"][0]
    assert lexeme["senses"][1]["labels"] == {"style": ["literary"]}
    # what has no v4 field rides along for the model to read
    assert lexeme["senses"][0]["_old_usage"] == "followed by 'to do something'"
    assert lexeme["_old_relations"] == [{"type": "family", "lemma": "able", "pos": "ADJ"}]
    # a variety the language does not voice is dropped from a form's ipa
    assert lexeme["forms"][1]["ipa"] == {"en-US": "/əˈbɪl.ə.tiz/"}


def test_an_empty_patch_makes_a_valid_v4_entry_without_the_old_fields():
    entry = mend_v4.apply(mend_v4.draft("en", ABILITY), {"verdict": "keep"})

    jsonschema.validate(entry, mend_v4.SCHEMA)
    assert "_old_relations" not in entry["lexemes"][0]
    assert "_old_usage" not in entry["lexemes"][0]["senses"][0]


def test_a_patch_trims_forms_drops_and_adds_senses_and_types_the_relations():
    added = {"definition": "a natural talent for doing something well", "example": "Music seems to be her one real ability.", "cefr": "B2"}
    patch = {"verdict": "patch", "lexemes": [{
        "i": 0, "keep_forms": [],
        "senses": [{"i": 0, "set": {"frames": ["the ability to do sth"], "antonyms": [{"lemma": "inability", "pos": "NOUN"}]}},
                   {"i": 1, "drop": True}],
        "add_senses": [added],
        "relations": [{"type": "abstract_noun_of", "lemma": "able", "pos": "ADJ"}]}]}

    entry = mend_v4.apply(mend_v4.draft("en", ABILITY), patch)

    jsonschema.validate(entry, mend_v4.SCHEMA)
    lexeme = entry["lexemes"][0]
    assert "forms" not in lexeme
    assert [s["definition"] for s in lexeme["senses"]] == [ABILITY["lexemes"][0]["senses"][0]["definition"], added["definition"]]
    assert lexeme["senses"][0]["frames"] == ["the ability to do sth"]
    assert lexeme["senses"][0]["example"] == ABILITY["lexemes"][0]["senses"][0]["example"]
    assert lexeme["relations"] == [{"type": "abstract_noun_of", "lemma": "able", "pos": "ADJ"}]


def test_a_patch_naming_a_sense_the_draft_lacks_is_refused():
    with pytest.raises(ValueError):
        mend_v4.apply(mend_v4.draft("en", ABILITY), {"verdict": "patch", "lexemes": [{"i": 0, "senses": [{"i": 5, "drop": True}]}]})


def test_an_older_reject_and_a_reject_verdict_become_v4_rejects():
    older = {"language": "en", "headword": "aaa", "lexemes": [], "reject": "abbreviation"}
    assert mend_v4.draft("en", older)["reject"] == "abbreviation"

    entry = mend_v4.apply(mend_v4.draft("en", ABILITY), {"verdict": "reject", "reason": "inflected form of able"})
    jsonschema.validate(entry, mend_v4.SCHEMA)
    assert entry["lexemes"] == [] and entry["reject"] == "inflected form of able"


def test_the_glosses_and_cues_are_carried_beside_the_entry():
    carried = mend_v4.carry(ABILITY)
    assert carried[0]["senses"][0]["glosses"] == {"uk": "здатність"}
    assert carried[0]["senses"][0]["cues"][0]["kind"] == "cloze"


def test_the_patch_schema_is_one_the_cli_can_resolve():
    schema = mend_v4.PATCH_SCHEMA
    assert "$defs" not in str(schema)
    jsonschema.Draft7Validator.check_schema(schema)
    jsonschema.validate({"verdict": "patch", "lexemes": [{"i": 0, "senses": [{"i": 0, "set": {"frames": ["x of sth"]}}]}]}, schema)


def test_derived_folders_under_out_are_not_older_writers():
    import v3_entries

    for name in ("lexicon-s3", "failed", "status", "carry", "mend", "cc-claude-opus-5-5-v4"):
        assert not v3_entries.is_older_writer(name)
    assert v3_entries.is_older_writer("cc-claude-opus-5-5")
