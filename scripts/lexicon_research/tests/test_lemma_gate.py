"""The Wiktionary gate before a paid write, on a hand-made index and hand-made kaikki records. Run from the repository root:
    scripts/.venv/bin/python -m pytest scripts/lexicon_research/tests -q
"""

import json
import sys
from pathlib import Path

import pytest

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
import lemma_gate  # noqa: E402
import lemma_index  # noqa: E402


def record(word, *senses):
    return json.dumps({"word": word, "pos": "x", "senses": list(senses)}, ensure_ascii=False)


def form_of(*lemmas):
    return {"form_of": [{"word": w} for w in lemmas], "tags": ["form-of"], "glosses": ["inflection of"]}


ALT = {"alt_of": [{"word": "dass"}], "tags": ["alt-of", "obsolete"], "glosses": ["obsolete spelling of dass"]}
ABBREVIATION = {"alt_of": [{"word": "Ampere"}], "tags": ["abbreviation", "alt-of"], "glosses": ["abbreviation of Ampere"]}
OWN = {"glosses": ["a sense of its own"]}


# ---------------------------------------------------------------- the index


def test_a_spelling_with_a_sense_of_its_own_is_a_lemma_even_when_it_is_also_a_form():
    verdicts = lemma_index.classify([record("parking", form_of("park")), record("parking", OWN)])
    assert verdicts["parking"] == ("FL", ["park"])


def test_alternative_spellings_abbreviations_and_empty_senses_are_told_apart():
    verdicts = lemma_index.classify([
        record("daß", ALT),
        record("A", ABBREVIATION),
        record("leer", {"tags": ["no-gloss"]}),
        "not json",
    ])
    assert verdicts["daß"] == ("A", [])
    assert verdicts["A"] == ("X", [])
    assert "leer" not in verdicts


def test_words_built_from_another_word_are_lemmas_though_wiktionary_files_them_as_forms():
    verdicts = lemma_index.classify([
        record("хлопчик", {"form_of": [{"word": "хло́пець"}], "tags": ["diminutive", "form-of"], "glosses": ["diminutive of хлопець"]}),
        record("виготовлення", {"form_of": [{"word": "ви́готовити pf"}], "tags": ["form-of", "noun-from-verb"], "glosses": ["verbal noun of"]}),
        record("Lehrerin", {"form_of": [{"word": "Lehrer"}], "tags": ["feminine", "form-of"], "glosses": ["female equivalent of Lehrer"]}),
        record("pública", {"form_of": [{"word": "público"}], "tags": ["feminine", "form-of", "singular"], "glosses": ["feminine singular of público"]}),
    ])
    assert verdicts["хлопчик"][0] == verdicts["виготовлення"][0] == verdicts["Lehrerin"][0] == "L"
    assert verdicts["pública"] == ("F", ["público"])


def test_acronyms_and_clippings_are_words_but_bare_abbreviations_are_not():
    verdicts = lemma_index.classify([
        record("sida", {"alt_of": [{"word": "syndrome"}], "tags": ["abbreviation", "acronym", "alt-of"], "glosses": ["acronym of"]}),
        record("info", {"alt_of": [{"word": "information"}], "tags": ["abbreviation", "alt-of", "clipping"], "glosses": ["clipping of"]}),
        record("млрд", {"alt_of": [{"word": "мільярд"}], "tags": ["abbreviation", "alt-of"], "glosses": ["abbreviation of"]}),
    ])
    assert (verdicts["sida"][0], verdicts["info"][0], verdicts["млрд"][0]) == ("A", "A", "X")


def test_cyrillic_stress_marks_leave_the_lemma_but_latin_accents_stay():
    verdicts = lemma_index.classify([record("серці", form_of("се́рце")), record("está", form_of("estar"))])
    assert verdicts["серці"] == ("F", ["серце"])
    assert lemma_index.plain("avó") == "avó"
    assert lemma_index.plain("каза́ти") == "казати"


# ---------------------------------------------------------------- the gate


@pytest.fixture
def index(tmp_path, monkeypatch):
    (tmp_path / "wiktionary").mkdir()
    (tmp_path / "wiktionary" / "de.tsv").write_text(
        "Entwicklern\tF\tEntwickler\n"
        "festlichen\tF\tfestlich\n"
        "arme\tF\tarm\n"
        "Arme\tF\tArm\n"
        "morgen\tL\t\n"
        "Morgen\tL\t\n"
        "daß\tA\t\n"
        "A\tX\t\n", encoding="utf-8")
    (tmp_path / "wiktionary" / "en.tsv").write_text("parking\tFL\tpark\ngrumpier\tF\tgrumpy\n", encoding="utf-8")
    (tmp_path / "wiktionary" / "pt.tsv").write_text("controle\tAF\tcontrolar\n", encoding="utf-8")
    monkeypatch.setattr(lemma_gate, "INDEX", tmp_path / "wiktionary")
    monkeypatch.setattr(lemma_gate, "RECORDS", tmp_path / "gate")
    lemma_gate.load.cache_clear()
    yield tmp_path
    lemma_gate.load.cache_clear()


def test_an_inflected_form_is_stopped_and_names_its_lemma(index):
    v = lemma_gate.verdict("de", "festlichen")
    assert (v.action, v.lemmas) == ("form", ("festlich",))
    assert lemma_gate.verdict("en", "grumpier").lemmas == ("grumpy",)


def test_a_lowercased_german_noun_form_is_found_under_its_capital(index):
    assert lemma_gate.verdict("de", "entwicklern").lemmas == ("Entwickler",)


def test_both_casings_of_a_german_form_give_both_lemmas(index):
    assert lemma_gate.verdict("de", "arme").lemmas == ("arm", "Arm")


def test_a_lemma_in_any_casing_is_written(index):
    assert not lemma_gate.verdict("de", "morgen").skip
    assert not lemma_gate.verdict("en", "parking").skip


def test_unknown_words_and_alternative_spellings_still_go_to_the_writer(index):
    assert lemma_gate.verdict("de", "Haustürschlüssel") == lemma_gate.Verdict("write", "not in Wiktionary")
    assert lemma_gate.verdict("de", "daß").action == "write"
    # Brazil's spelling of controlo, and a form of controlar: the noun is a word, so it is written
    assert lemma_gate.verdict("pt", "controle").action == "write"


def test_an_abbreviation_is_refused_without_a_call(index):
    assert lemma_gate.verdict("de", "A").action == "reject"


def test_english_is_not_looked_up_capitalised(index):
    assert lemma_gate.spellings("en", "may") == ["may"]
    assert lemma_gate.spellings("de", "haus") == ["haus", "Haus"]


def test_stop_records_what_it_stopped_and_lets_lemmas_through(index):
    assert lemma_gate.stop("de", {"headword": "festlichen"}, "cc-opus")
    assert not lemma_gate.stop("de", {"headword": "morgen"}, "cc-opus")
    assert not lemma_gate.stop("de", {"headword": "festlichen"}, "cc-opus", enabled=False)
    lines = (index / "gate" / "de.jsonl").read_text(encoding="utf-8").splitlines()
    assert [json.loads(line)["lemmas"] for line in lines] == [["festlich"]]
    assert lemma_gate.recorded_lemmas("de") == ["festlich"]


def test_a_missing_index_stops_the_run_before_any_call(index):
    with pytest.raises(lemma_gate.MissingIndex, match="lemma_index.py --langs fr"):
        lemma_gate.require(["de", "fr"])
