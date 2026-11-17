"""Schema 4 as it grows: what was added must not refuse what is already written. Run from the repository root:
    scripts/.venv/bin/python -m pytest scripts/lexicon_research/tests -q
"""

import copy
import json
import sys
from pathlib import Path

import jsonschema

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
import check_v4  # noqa: E402
import prompting  # noqa: E402

SCHEMA = json.loads((Path(__file__).resolve().parents[1] / "entry.schema.v4.json").read_text())
VALID = jsonschema.Draft202012Validator(SCHEMA)


def entry():
    return copy.deepcopy(json.loads(prompting.SCHEMA_EXAMPLE_V4))


def test_the_prompts_own_example_is_valid():
    VALID.validate(entry())


def test_a_region_is_a_variety_or_a_place_with_an_extent():
    e = entry()
    sense = e["lexemes"][0]["senses"][0]
    sense["regions"] = ["en-GB", {"variety": "en-US", "extent": "mainly"}, {"variety": "en-029", "extent": "only"}]
    VALID.validate(e)
    assert check_v4.check("en", e, {}, None) == []
    sense["regions"] = [{"variety": "en-GB"}]
    assert not VALID.is_valid(e)
    sense["regions"] = [{"variety": "es-CU", "extent": "only"}]
    assert ("variety outside the language", "es-CU") in check_v4.check("en", e, {}, None)


def test_a_pronunciation_needs_ipa_or_a_reading():
    e = entry()
    e["lexemes"][0]["pronunciations"] = [{"variety": "zh-CN", "reading": "diànnǎo", "notation": "pinyin"}]
    VALID.validate(e)
    e["lexemes"][0]["pronunciations"] = [{"variety": "zh-CN"}]
    assert not VALID.is_valid(e)
    e["lexemes"][0]["pronunciations"] = [{"variety": "en-GB", "ipa": "/ˈliːd/", "note": "older"}]
    VALID.validate(e)


def test_scripts_politeness_classifiers_cases_and_citations_are_accepted():
    e = entry()
    lexeme = e["lexemes"][1]
    lexeme["spellings"] = [{"text": "电脑", "script": "Hans", "varieties": ["zh-CN"]}, {"text": "電腦", "script": "Hant", "varieties": ["zh-TW"]}]
    lexeme["grammar"]["classifier"] = "台"
    lexeme["forms"] = [{"form": "talossa", "features": {"Case": "Ine", "Number": "Sing"}}]
    lexeme["senses"][0]["labels"] = {"politeness": "honorific"}
    lexeme["senses"][0]["from"] = ["1.2", "2.1"]
    VALID.validate(e)
    lexeme["senses"][0]["labels"] = {"politeness": "rude"}
    assert not VALID.is_valid(e)
    lexeme["senses"][0]["labels"] = {}
    lexeme["spellings"][0]["script"] = "hans"
    assert not VALID.is_valid(e)


def test_arabic_and_swahili_have_what_they_need():
    e = entry()
    lexeme = e["lexemes"][1]
    lexeme["grammar"] = {"gender": "Masc", "root": "ك ت ب"}
    lexeme["spellings"] = [{"text": "كتاب", "script": "Arab"}, {"text": "كِتَاب", "script": "Arab"}]
    lexeme["pronunciations"] = [{"variety": "ar-EG", "reading": "كِتَاب", "notation": "vocalized"}]
    lexeme["forms"] = [{"form": "كتابان", "features": {"Number": "Dual", "Case": "Nom"}},
                       {"form": "كتب", "features": {"Number": "Plur", "Definite": "Cons"}},
                       {"form": "يكتب", "features": {"Mood": "Jus", "Person": "3", "Polite": "Form"}}]
    VALID.validate(e)
    lexeme["grammar"] = {"noun_class": "7/8"}
    VALID.validate(e)
    lexeme["forms"] = [{"form": "x", "features": {"Number": "Trial"}}]
    assert not VALID.is_valid(e)


def test_two_spellings_said_differently_keep_their_readings_and_senses_apart():
    e = entry()
    lexeme = e["lexemes"][0]
    lexeme["spellings"] = [{"text": "período"}, {"text": "periodo"}]
    lexeme["pronunciations"] = [{"variety": "es-ES", "ipa": "/pe.ˈɾi.o.do/", "spelling": "período"}, {"variety": "es-ES", "ipa": "/pe.ˈɾjo.do/", "spelling": "periodo"}]
    lexeme["senses"][0]["spellings"] = ["periodo"]
    VALID.validate(e)
    lexeme["senses"][0]["spellings"] = []
    assert not VALID.is_valid(e)


def test_a_form_may_say_where_it_is_used():
    e = entry()
    e["lexemes"][0]["forms"] = [{"form": "electo", "features": {"VerbForm": "Part", "Tense": "Past"}, "varieties": ["es-MX"], "note": "only in the passive"}]
    VALID.validate(e)


def test_a_relation_may_hold_for_some_senses_only():
    e = entry()
    e["lexemes"][0]["relations"] = [{"type": "derived_from", "lemma": "porter", "pos": "VERB", "senses": [2, 3]}]
    VALID.validate(e)
    e["lexemes"][0]["relations"][0]["senses"] = [0]
    assert not VALID.is_valid(e)


def test_the_audit_counts_a_pronominal_sense_without_its_auxiliary():
    e = entry()
    e["language"] = "fr"
    lexeme = e["lexemes"][0]
    lexeme["pos"], lexeme["grammar"] = "VERB", {"auxiliary": "have"}
    lexeme["senses"][0]["grammar"] = {"valency": "Refl"}
    assert ("pronominal sense without its auxiliary", lexeme["lemma"]) in check_v4.check("fr", e, {}, None)
    lexeme["senses"][0]["grammar"]["auxiliary"] = "be"
    assert "pronominal sense without its auxiliary" not in [kind for kind, _ in check_v4.check("fr", e, {}, None)]


def test_a_form_shared_by_several_persons_joins_them_as_any_other_feature():
    e = entry()
    e["lexemes"][0]["forms"] = [{"form": "mantenga", "features": {"Mood": "Sub", "Tense": "Pres", "Person": "1,2,3", "Number": "Sing"}},
                                {"form": "va", "features": {"Person": "3"}}, {"form": "vai", "features": {"Person": 2}}]
    VALID.validate(e)
    for wrong in ("4", "1,", 0, "1;3"):
        e["lexemes"][0]["forms"][1]["features"]["Person"] = wrong
        assert not VALID.is_valid(e), wrong


def test_the_audit_counts_the_sounds_ingest_puts_in_house_form():
    e = entry()
    e["language"] = "it"
    e["lexemes"][0]["pronunciations"] = [{"variety": "it-IT", "ipa": "/ˈfatːo/", "reading": "fàtto"}]
    assert "double consonant not written twice" in [kind for kind, _ in check_v4.check("it", e, {}, None)]
    e["lexemes"][0]["pronunciations"] = [{"variety": "es-ES", "ipa": "/ˈka.ʎe/"}]
    assert "ll of Spain written ʎ" in [kind for kind, _ in check_v4.check("es", e, {}, None)]


def test_a_japanese_entry_has_a_kana_reading_a_class_and_limits_counted_in_characters():
    e = {"language": "ja", "headword": "食べる", "lexemes": [{"lemma": "食べる", "pos": "VERB", "kind": "word",
         "grammar": {"conjugation": "ichidan", "valency": "Tr"},
         "pronunciations": [{"variety": "ja-JP", "reading": "たべる", "notation": "kana"}],
         "senses": [{"definition": "食べ物を口に入れて、かんで飲み込む", "example": "毎朝七時に家族と朝ご飯を食べます。", "cefr": "A1", "frames": ["〜を食べる"]}]}]}
    VALID.validate(e)
    assert check_v4.check("ja", e, {"ja-JP": check_v4.symbols("ja-JP")}, None) == []
    e["lexemes"][0]["senses"][0]["definition"] = "食べること"
    assert ("definition outside 6 to 15 words", "5 characters") in check_v4.check("ja", e, {}, None)
