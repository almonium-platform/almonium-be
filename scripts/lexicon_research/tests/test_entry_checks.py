"""The free checks on a small English entry, one defect at a time."""

import copy
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
import entry_checks

SENSE = {
    "definition": "a part at the back of a car for carrying bags",
    "register": "neutral",
    "cefr": "B1",
    "glosses": {"uk": "багажник", "de": "Kofferraum", "pl": "bagażnik"},
    "example": "She put the shopping in the boot of the car.",
    "cues": [
        {"kind": "wording", "text": "the space at the back of a car for luggage"},
        {"kind": "cloze", "text": "She put the shopping in the ___ of the car."},
    ],
}
ENTRY = {
    "language": "en",
    "headword": "boot",
    "lexemes": [
        {
            "lemma": "boot",
            "pos": "NOUN",
            "kind": "word",
            "grammar": {"countability": "Count"},
            "forms": [
                {"form": "boot", "features": {"Number": "Sing"}},
                {"form": "boots", "features": {"Number": "Plur"}},
            ],
            "pronunciations": [{"variety": "en-US", "ipa": "/ˈbuːt/"}, {"variety": "en-GB", "ipa": "/ˈbuːt/"}],
            "senses": [{**SENSE, "regions": [{"variety": "en-GB", "extent": "only"}]}],
        }
    ],
}


def checks(entry):
    return [(finding["check"], finding["severity"], finding["path"]) for finding in entry_checks.check(entry)]


def changed(edit):
    entry = copy.deepcopy(ENTRY)
    edit(entry["lexemes"][0])
    return checks(entry)


def test_a_clean_entry_has_no_finding():
    assert checks(ENTRY) == []


def test_a_word_mixing_alphabets_is_an_error():
    def edit(lexeme):
        lexeme["senses"][0]["glosses"]["uk"] = "багажникa"

    assert changed(edit) == [("mixed-script", "error", "/lexemes/0/senses/0/glosses/uk")]


def test_a_brand_in_latin_letters_is_only_worth_a_look():
    def edit(lexeme):
        lexeme["senses"][0]["glosses"]["uk"] = "WhatsApp"

    assert changed(edit) == [("script", "warn", "/lexemes/0/senses/0/glosses/uk")]


def test_a_variety_the_language_does_not_transcribe_or_scope_is_an_error():
    def edit(lexeme):
        lexeme["pronunciations"][1]["variety"] = "en-AU"
        lexeme["senses"][0]["regions"][0]["variety"] = "de-AT"

    found = changed(edit)
    assert ("variety", "error", "/lexemes/0/pronunciations/1") in found
    assert ("region", "error", "/lexemes/0/senses/0/regions/0/variety") in found


def test_a_region_may_be_any_country_or_un_area_of_the_language():
    def edit(lexeme):
        lexeme["senses"][0]["regions"] = [
            {"variety": "en-IE", "extent": "only"},
            {"variety": "en-029", "extent": "mainly"},
        ]

    assert changed(edit) == []


def test_a_symbol_outside_the_inventory_is_an_error_but_a_tie_bar_is_notation():
    def edit(lexeme):
        lexeme["pronunciations"][0]["ipa"] = "/ˈbruːt/"
        lexeme["pronunciations"][1]["ipa"] = "/ˈt͡ʃuːt/"

    assert changed(edit) == [("ipa-inventory", "error", "/lexemes/0/pronunciations/0")]


def test_a_region_written_into_usage_is_flagged():
    def edit(lexeme):
        del lexeme["senses"][0]["regions"]
        lexeme["senses"][0]["usage"] = "British"

    assert changed(edit) == [("region-in-text", "warn", "/lexemes/0/senses/0/usage")]


def test_an_example_without_its_word_is_flagged():
    def edit(lexeme):
        lexeme["senses"][0]["example"] = "She put the shopping in the back of the car."

    assert changed(edit) == [("example-word", "warn", "/lexemes/0/senses/0/example")]


def test_two_lexemes_alike_in_every_way_are_one_but_a_different_paradigm_is_another():
    entry = copy.deepcopy(ENTRY)
    entry["lexemes"].append(copy.deepcopy(entry["lexemes"][0]))
    assert ("duplicate-lexeme", "error", "/lexemes/1") in checks(entry)
    entry["lexemes"][1]["forms"][1]["form"] = "booten"
    assert ("duplicate-lexeme", "error", "/lexemes/1") not in checks(entry)
