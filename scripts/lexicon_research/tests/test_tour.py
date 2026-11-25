"""The tour of schema 4: feature detectors, schema-to-tour contract guard, and published JSON fixture.
Run from the repository root:
    scripts/.venv/bin/python -m pytest scripts/lexicon_research/tests -q
"""

import json
import sys
from pathlib import Path

import jsonschema

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
import ingest_v4  # noqa: E402
import published_schema  # noqa: E402
import tour  # noqa: E402

SCHEMA = json.loads((Path(__file__).resolve().parents[1] / "entry.schema.v4.json").read_text(encoding="utf-8"))
PUBLISHED_VALID = jsonschema.Draft202012Validator(published_schema.published())

# Every property of entry.schema.v4.json accounted for here. When entry.schema.v4.json gains or drops a key,
# update tour.py (detectors and EXPECTED), regenerate docs/LEXICON_TOUR.md, and update this map.
ACCOUNTED_SCHEMA_KEYS = {
    "entry": {"language", "headword", "lexemes", "reject"},
    "lexeme": {"lemma", "pos", "kind", "spellings", "grammar", "pronunciations", "forms", "components", "separable", "senses", "relations"},
    "lexeme.grammar": {
        "gender", "number", "countability", "valency", "irregular", "conjugation", "auxiliary", "separable",
        "separable_prefix", "reflexive", "declension", "plural_class", "aspect", "animacy", "invariable",
        "position", "comparable", "note", "h_aspire", "by_variety", "classifier", "root", "noun_class",
        "mainly", "suru",
    },
    "spelling": {"text", "varieties", "pre_reform", "script"},
    "pronunciation": {"variety", "ipa", "reading", "notation", "note", "context", "spelling", "accent"},
    "form": {"form", "features", "varieties", "senses", "ipa", "reading", "notation", "note"},
    "component": {"text", "lemma", "inflects"},
    "sense": {"definition", "example", "cefr", "frames", "collocations", "labels", "regions", "standard_alternative", "grammar", "antonyms", "from", "spellings"},
    "sense.grammar": {"countability", "valency", "number", "position", "reflexive", "auxiliary", "note", "comparable", "mainly", "suru"},
    "labels": {"formality", "attitude", "time", "style", "normativity", "domain", "figurative", "rare", "politeness"},
    "relation": {"type", "lemma", "pos", "why", "senses"},
}


def base_entry(pronunciations):
    return {
        "language": "en",
        "headword": "test",
        "lexemes": [{
            "lemma": "test",
            "pos": "DET",
            "kind": "word",
            "pronunciations": pronunciations,
            "senses": [{"definition": "a simple test definition for the tour", "example": "This is a simple test sentence here.", "cefr": "A1"}],
        }],
    }


def test_v411_pronunciation_features_are_detected():
    the = base_entry([
        {"variety": "en-US", "ipa": "/ˈðiː/"},
        {"variety": "en-US", "ipa": "/ðə/", "context": "weak form before a consonant sound, usual in running speech"},
        {"variety": "en-GB", "ipa": "/ˈðiː/"},
    ])
    got = tour.features("en", the)
    assert "a pronunciation with when it is said" in got
    assert "a weak form, without a stress mark" in got
    assert "two pronunciations in one variety, neither marked" not in got  # the second has context

    # An elided consonant without "weak" in context is a contextual reading, not a weak form
    elided = base_entry([
        {"variety": "fr-FR", "ipa": "/ˈnə/", "spelling": "ne", "context": "before a consonant sound"},
        {"variety": "fr-FR", "ipa": "/n/", "spelling": "n'", "context": "before a vowel sound"},
    ])
    got_elided = tour.features("fr", elided)
    assert "a pronunciation with when it is said" in got_elided
    assert "a weak form, without a stress mark" not in got_elided

    # A marked second reading (note / spelling / context) is not an unmarked dual pronunciation
    marked = base_entry([
        {"variety": "en-GB", "ipa": "/ˈbɑːθ/"},
        {"variety": "en-GB", "ipa": "/ˈbæθ/", "note": "regional", "context": "north of England"},
    ])
    assert "two pronunciations in one variety, neither marked" not in tour.features("en", marked)

    # Two distinct IPAs in the same variety with no note, context, or spelling
    dual = base_entry([
        {"variety": "en-US", "ipa": "/dɪ.ˈɹɛkt/"},
        {"variety": "en-US", "ipa": "/daɪ.ˈɹɛkt/"},
    ])
    got_dual = tour.features("en", dual)
    assert got_dual["two pronunciations in one variety, neither marked"] == "en-US /dɪ.ˈɹɛkt/, /daɪ.ˈɹɛkt/"
    assert "a pronunciation with when it is said" not in got_dual
    assert "a weak form, without a stress mark" not in got_dual


def test_schema_keys_are_accounted_for_by_the_tour():
    defs = SCHEMA["$defs"]
    actual = {
        "entry": set(SCHEMA["properties"]),
        "lexeme": set(defs["lexeme"]["properties"]),
        "lexeme.grammar": set(defs["lexeme"]["properties"]["grammar"]["properties"]),
        "spelling": set(defs["spelling"]["properties"]),
        "pronunciation": set(defs["pronunciation"]["properties"]),
        "form": set(defs["form"]["properties"]),
        "component": set(defs["component"]["properties"]),
        "sense": set(defs["sense"]["properties"]),
        "sense.grammar": set(defs["sense"]["properties"]["grammar"]["properties"]),
        "labels": set(defs["labels"]["properties"]),
        "relation": set(defs["relation"]["properties"]),
    }
    assert actual == ACCOUNTED_SCHEMA_KEYS, (
        "entry.schema.v4.json changed: update scripts/lexicon_research/tour.py, "
        "regenerate docs/LEXICON_TOUR.md, and update ACCOUNTED_SCHEMA_KEYS in test_tour.py"
    )
    assert set(defs["relation"]["properties"]["type"]["enum"]) == set(tour.RELATIONS)


def test_lexicon_tour_doc_covers_all_expected_features():
    text = tour.DOC.read_text(encoding="utf-8")
    assert "No written entry shows these yet" not in text
    missing = [name for name in tour.EXPECTED if f"**{name}**" not in text]
    assert missing == [], f"docs/LEXICON_TOUR.md is missing EXPECTED features: {missing}"


def test_tour_fixture_emits_valid_published_entries(tmp_path, monkeypatch):
    raw = base_entry([
        {"variety": "en-US", "ipa": "/ˈðiː/"},
        {"variety": "en-US", "ipa": "/ðə/", "context": "weak form, usual in running speech"},
        {"variety": "en-GB", "ipa": "/ˈðiː/"},
        {"variety": "en-GB", "ipa": "/ˈðɪ/"},
    ])
    raw["headword"] = "the"
    raw["lexemes"][0]["lemma"] = "the"
    meta = {"writer": "cc-claude-opus-5-5-v4", "model": "claude-opus-5-5", "effort": "high", "prompt_version": "v4.11"}
    monkeypatch.setattr(ingest_v4, "load_raw", lambda lang: {"the": (raw, meta)} if lang == "en" else {})
    monkeypatch.setattr(ingest_v4, "zipf_of", lambda lang: {"the": 7.7})
    monkeypatch.setattr(tour, "DOC", tmp_path / "LEXICON_TOUR.md")
    fixture_path = tmp_path / "tour-fixture.json"
    chosen, everything = tour.write(["en"], fixture=fixture_path)
    assert len(chosen) == 1
    assert {"a pronunciation with when it is said", "a weak form, without a stress mark", "two pronunciations in one variety, neither marked"} <= everything
    entries = json.loads(fixture_path.read_text(encoding="utf-8"))
    assert len(entries) == 1 and entries[0]["headword"] == "the"
    for entry in entries:
        PUBLISHED_VALID.validate(entry)
