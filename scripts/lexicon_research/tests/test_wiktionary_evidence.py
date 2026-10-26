"""Wiktionary evidence from cached kaikki records, and the comparison of an entry with it."""

import sys
from pathlib import Path

import pytest

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
import evidence_checks
import wiktionary


@pytest.fixture(autouse=True)
def cached(monkeypatch):
    monkeypatch.setattr(wiktionary, "CACHE", Path(__file__).resolve().parent / "fixtures" / "kaikki")


def test_band_is_three_nouns_by_gender_with_their_plurals():
    found = wiktionary.evidence("de", "Band")
    nouns = [(lexeme["gender"], lexeme["plural"]) for lexeme in found["lexemes"]]
    assert nouns == [(["Neut"], ["Bänder"]), (["Masc"], ["Bände"]), (["Fem"], ["Bands"])]
    assert found["lexemes"][2]["ipa"] == {"*": ["/bɛnt/", "/bɛːnt/"]}
    assert found["lexemes"][0]["senses"][0]["id"] == "1.1"


def test_a_place_label_becomes_a_region_code_and_a_subsense_keeps_its_own_gloss():
    found = wiktionary.evidence("es", "guagua")
    bus = found["lexemes"][0]["senses"][1]
    assert sorted(bus["regions"]) == ["029", "ES", "GQ", "GT"]
    assert bus["gloss"].endswith("bus")
    baby = found["lexemes"][1]
    assert baby["gender"] == ["Fem", "Masc"]
    assert "EC" in baby["senses"][0]["regions"]


def test_stress_is_carried_by_the_canonical_form_when_the_sounds_are_phonetic():
    found = wiktionary.evidence("uk", "замок")
    assert [lexeme["stressed"] for lexeme in found["lexemes"]] == ["за́мок", "замо́к"]
    assert "L2 NOUN · stressed замо́к · Masc" in wiktionary.render(found)


def entry(**sense):
    return {
        "language": "es",
        "headword": "guagua",
        "lexemes": [
            {
                "lemma": "guagua",
                "pos": "NOUN",
                "kind": "word",
                "grammar": {"gender": "Fem"},
                "forms": [
                    {"form": "guagua", "features": {"Number": "Sing"}},
                    {"form": "guaguas", "features": {"Number": "Plur"}},
                ],
                "pronunciations": [{"variety": "es-ES", "ipa": "/ˈɡwa.ɡwa/"}],
                "senses": [{"definition": "vehículo", "from": ["1.2"], **sense}],
            }
        ],
    }


def test_a_cited_regional_sense_takes_its_places_and_the_facts_agree():
    found = wiktionary.evidence("es", "guagua")
    facts = [f for f in evidence_checks.compare(entry(), found) if f["kind"] == "fact"]
    assert facts == []
    filled, patches = evidence_checks.fill(entry(), found)
    regions = {region["variety"] for region in filled["lexemes"][0]["senses"][0]["regions"]}
    assert regions == {"es-ES", "es-GQ", "es-GT", "es-029"}
    assert patches[0]["path"] == "/lexemes/0/senses/0/regions"


def test_regions_the_writer_gave_are_left_alone():
    found = wiktionary.evidence("es", "guagua")
    mine = entry(regions=[{"variety": "es-CU", "extent": "mainly"}])
    filled, patches = evidence_checks.fill(mine, found)
    assert patches == [] and filled == mine


def test_a_wrong_gender_and_plural_are_facts_and_an_unknown_citation_is_provenance():
    found = wiktionary.evidence("es", "guagua")
    bad = entry()
    lexeme = bad["lexemes"][0]
    lexeme["grammar"]["gender"] = "Masc"
    lexeme["forms"][1]["form"] = "guaguaes"
    lexeme["senses"][0]["from"] = ["9.9"]
    findings = evidence_checks.compare(bad, found)
    assert {f["check"] for f in findings if f["kind"] == "fact"} == set()  # nothing cited aligns, nothing to compare
    assert {f["check"] for f in findings if f["kind"] == "provenance"} == {"citation"}
    lexeme["senses"][0]["from"] = ["1.2"]
    checks = {f["check"] for f in evidence_checks.compare(bad, found) if f["kind"] == "fact"}
    assert checks == {"gender", "plural"}


def test_ipa_compares_sounds_not_notation():
    assert evidence_checks.sounds_like("/ˈteɪ.bəl/", ["/ˈteɪbl̩/"]) is None
    assert evidence_checks.sounds_like("/ˈbant/", ["/bant/"]) is None
    assert evidence_checks.sounds_like("/ˈnɪtʃ/", ["/niːʃ/"]) == "sounds differ from"
    assert evidence_checks.sounds_like("/rɪˈsɜːtʃ/", ["/ˈriːsɜːtʃ/", "/ˈrɪsɜːtʃ/"]) == "stress differs from"


def test_an_uncovered_common_lexeme_is_reported_as_coverage_not_fact():
    found = wiktionary.evidence("es", "guagua")
    findings = evidence_checks.compare(entry(), found)
    coverage = [f["message"] for f in findings if f["kind"] == "coverage"]
    assert any(message.startswith("L2 NOUN") for message in coverage)


def test_an_inflected_form_points_at_its_lemma():
    found = {"lexemes": [], "form_of": ["plural of Koordinate"]}
    assert wiktionary.lemma_of(found) == "Koordinate"
    assert wiktionary.lemma_of({"lexemes": [], "form_of": ["past participle of expresar"]}) == "expresar"


def test_a_region_comes_from_the_gloss_labels_with_their_qualifiers_set_aside():
    assert wiktionary.labels({"raw_glosses": ["(chiefly US) To postpone"], "tags": ["US"]}) == {"US"}
    assert wiktionary.labels({"raw_glosses": ["(childish) poo"], "tags": ["Louisiana"]}) == {"childish"}


def test_a_german_compound_takes_gender_and_plural_from_its_last_part():
    heads = {"Träger": {"lexemes": [{"pos": "NOUN", "gender": ["Masc"], "plural": ["Träger"]}]}}

    def evidence(word):
        return heads.get(word, {"lexemes": []})

    assert evidence_checks.german_head("Verankerung", evidence) == ("Fem", "Verankerungen")
    assert evidence_checks.german_head("Leistungsträger", evidence) == ("Masc", "Leistungsträger")
    entry = {
        "language": "de",
        "lexemes": [
            {
                "lemma": "Leistungsträger",
                "pos": "NOUN",
                "grammar": {"gender": "Fem"},
                "forms": [{"form": "Leistungsträgerinnen", "features": {"Number": "Plur"}}],
            }
        ],
    }
    checks = {f["check"] for f in evidence_checks.compare_without_evidence(entry, evidence)}
    assert checks == {"gender", "plural"}
