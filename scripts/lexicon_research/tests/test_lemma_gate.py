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
    assert verdicts["parking"][:2] == ("FL", ["park"])


def test_alternative_spellings_abbreviations_and_empty_senses_are_told_apart():
    verdicts = lemma_index.classify([
        record("daß", ALT),
        record("A", ABBREVIATION),
        record("leer", {"tags": ["no-gloss"]}),
        "not json",
    ])
    assert verdicts["daß"][:2] == ("A", [])
    assert verdicts["A"][:2] == ("X", [])
    assert "leer" not in verdicts


def test_words_built_from_another_word_are_lemmas_though_wiktionary_files_them_as_forms():
    verdicts = lemma_index.classify([
        record("хлопчик", {"form_of": [{"word": "хло́пець"}], "tags": ["diminutive", "form-of"], "glosses": ["diminutive of хлопець"]}),
        record("виготовлення", {"form_of": [{"word": "ви́готовити pf"}], "tags": ["form-of", "noun-from-verb"], "glosses": ["verbal noun of"]}),
        record("Lehrerin", {"form_of": [{"word": "Lehrer"}], "tags": ["feminine", "form-of"], "glosses": ["female equivalent of Lehrer"]}),
        record("pública", {"form_of": [{"word": "público"}], "tags": ["feminine", "form-of", "singular"], "glosses": ["feminine singular of público"]}),
    ])
    assert verdicts["хлопчик"][0] == verdicts["виготовлення"][0] == verdicts["Lehrerin"][0] == "L"
    assert verdicts["pública"][:2] == ("F", ["público"])


def test_acronyms_and_clippings_are_words_but_bare_abbreviations_are_not():
    verdicts = lemma_index.classify([
        record("sida", {"alt_of": [{"word": "syndrome"}], "tags": ["abbreviation", "acronym", "alt-of"], "glosses": ["acronym of"]}),
        record("info", {"alt_of": [{"word": "information"}], "tags": ["abbreviation", "alt-of", "clipping"], "glosses": ["clipping of"]}),
        record("млрд", {"alt_of": [{"word": "мільярд"}], "tags": ["abbreviation", "alt-of"], "glosses": ["abbreviation of"]}),
    ])
    assert (verdicts["sida"][0], verdicts["info"][0], verdicts["млрд"][0]) == ("A", "A", "X")


def test_a_contraction_is_its_own_kind_and_stressed_forms_are_kept_as_a_witness():
    stress = {}
    verdicts = lemma_index.classify([
        record("zum", {"alt_of": [{"word": "zu"}], "tags": ["abbreviation", "alt-of", "contraction"], "glosses": ["contraction of zu + dem"]}),
        json.dumps({"word": "табір", "pos": "noun", "senses": [OWN], "forms": [
            {"form": "та́бір", "tags": ["canonical"]}, {"form": "tábir", "tags": ["romanization"]},
            {"form": "таборі́в", "tags": ["genitive", "plural"]}, {"form": "рук", "tags": ["genitive", "plural"]}]}, ensure_ascii=False),
    ], stress)
    assert verdicts["zum"][:2] == ("C", [])
    assert stress == {"табір": {"та́бір"}, "таборів": {"таборі́в"}}


def test_cyrillic_stress_marks_leave_the_lemma_but_latin_accents_stay():
    verdicts = lemma_index.classify([record("серці", form_of("се́рце")), record("está", form_of("estar"))])
    assert verdicts["серці"][:2] == ("F", ["серце"])
    assert lemma_index.plain("avó") == "avó"
    assert lemma_index.plain("каза́ти") == "казати"


def test_a_name_a_piece_of_a_word_and_a_form_said_only_in_words_are_told_from_words():
    entry = lambda word, pos, *senses: json.dumps({"word": word, "pos": pos, "senses": list(senses)}, ensure_ascii=False)
    verdicts = lemma_index.classify([
        entry("Bremen", "name", {"glosses": ["Bremen (the capital city of the state of Bremen, Germany)"], "tags": ["proper-noun"]}),
        entry("Müller", "noun", {"glosses": ["miller"]}), entry("Müller", "name", {"glosses": ["a common surname"]}),
        entry("-heit", "suffix", {"glosses": ["-hood, -ness"]}), entry("ß", "character", {"glosses": ["a letter of the German alphabet"]}),
        entry("possiamo", "verb", {"glosses": ["first-person plural present indicative/present subjunctive of potere"]}),
        entry("farlo", "verb", {"glosses": ["compound of the infinitive fare with lo"], "tags": ["compound-of"]}),
        entry("fizeram", "verb", {"glosses": ["third-person plural preterite indicative of fazer"]}),
        entry("robert", "noun", {"glosses": ["tit (human breast)"], "tags": ["slang", "vulgar"]}),
        entry("pierre", "noun", {"glosses": ["stone"]}), entry("gatito", "noun", {"glosses": ["diminutive of gato"], "tags": ["diminutive"]}),
    ])
    assert verdicts["Bremen"] == ("N", [], ["name"]) and verdicts["Müller"] == ("LN", [], ["name", "noun"])
    # a month, a feast and a country are names a learner needs: words like any other; a form only the lemma's table gives is a form
    more = lemma_index.classify([
        entry("October", "name", {"glosses": ["The tenth month of the Gregorian calendar, following September."]}),
        entry("Россия", "name", {"glosses": ["Russia (a transcontinental country in Eastern Europe and North Asia; capital: Moscow)"]}),
        entry("London", "name", {"glosses": ["The capital city of the United Kingdom."]}),
        json.dumps({"word": "российский", "pos": "adj", "senses": [{"glosses": ["Russian"]}], "forms": [
            {"form": "росси́йский", "tags": ["canonical"]}, {"form": "rossíjskij", "tags": ["romanization"]},
            {"form": "росси́йской", "tags": ["feminine", "genitive"]}, {"form": "Росси́юшка", "tags": ["diminutive"]},
            {"form": "бу́дет росси́йским", "tags": ["future"]}]}, ensure_ascii=False),
    ])
    assert more["October"][0] == "L" and more["Россия"][0] == "L" and more["London"][0] == "N"
    assert more["российской"][:2] == ("F", ["российский"]) and "Россиюшка" not in more and "будет российским" not in more
    assert verdicts["-heit"][0] == "P" and verdicts["ß"][0] == "S"
    assert verdicts["possiamo"][:2] == ("F", ["potere"]) and verdicts["farlo"][:2] == ("F", ["fare"]) and verdicts["fizeram"][:2] == ("F", ["fazer"])
    assert verdicts["robert"][0] == "LR" and verdicts["pierre"][0] == "L" and verdicts["gatito"][0] == "L"


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
        "zum\tC\t\n"
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


def test_a_contraction_is_written(index):
    assert lemma_gate.verdict("de", "zum") == lemma_gate.Verdict("write", "contraction")


@pytest.fixture
def names_index(tmp_path, monkeypatch):
    (tmp_path / "wiktionary").mkdir()
    (tmp_path / "wiktionary" / "de.tsv").write_text(
        "Bremen\tN\t\tname\nMüller\tLN\t\tname,noun\nTag\tL\t\tnoun\ntag\tF\ttagen\t\nhabe\tF\thaben\t\nHabe\tL\t\tnoun\n", encoding="utf-8")
    (tmp_path / "wiktionary" / "en.tsv").write_text(
        "American\tLN\t\tadj,name,noun\nDavid\tLMN\t\tname,noun\nOctober\tL\t\tname\nStates\tN\t\tname\nstates\tF\tstate\t\n"
        "robert\tLR\t\tnoun\nRobert\tN\t\tname\ncolour\tL\t\tnoun,verb\ncolor\tA\t\t\nviewed\tFL\tview\tadj\nsaw\tFL\tsee\tnoun,verb\n",
        encoding="utf-8")
    (tmp_path / "wiktionary" / "ru.tsv").write_text("Россия\tL\t\tname\nРоссии\tF\tРоссия\t\n", encoding="utf-8")
    monkeypatch.setattr(lemma_gate, "INDEX", tmp_path / "wiktionary")
    monkeypatch.setattr(lemma_gate, "RECORDS", tmp_path / "gate")
    lemma_gate.load.cache_clear()
    lemma_gate.kinds.cache_clear()
    yield tmp_path
    lemma_gate.load.cache_clear()
    lemma_gate.kinds.cache_clear()
    lemma_gate.names(False)


def test_a_headword_that_is_only_a_name_is_stopped_unless_names_are_asked_for(names_index):
    assert lemma_gate.verdict("de", "Bremen") == lemma_gate.Verdict("name", "proper name") and lemma_gate.verdict("de", "Bremen").skip
    assert lemma_gate.verdict("de", "Müller").action == "write"  # a miller too
    assert lemma_gate.verdict("en", "October").action == "write"  # a month is a word a learner needs
    lemma_gate.names(True)
    assert lemma_gate.verdict("de", "Bremen").action == "write"


def test_a_word_of_a_frequency_list_is_found_under_its_capital_but_a_form_does_not_pass_for_a_name(names_index):
    assert [s for s, _ in lemma_gate.known("en", "american")] == ["American"] and lemma_gate.verdict("en", "american").action == "write"
    assert lemma_gate.verdict("ru", "россии").lemmas == ("Россия",)  # the form of a country's name, lower-cased by the list
    assert lemma_gate.verdict("en", "states").action == "form"  # found as it is: "the States" is not asked


def test_what_does_not_belong_in_a_list_of_words_to_write_is_named(names_index):
    said = {w: lemma_gate.unfit("en", w) for w in ("american", "october", "david", "robert", "color", "viewed", "saw", "zzyzx", "states")}
    assert said == {"american": None, "october": None, "david": "proper name", "robert": "only old, rare or slang senses",
                    "color": "alternative spelling", "viewed": None, "saw": None, "zzyzx": "not in Wiktionary", "states": "inflected form"}
    assert lemma_gate.unfit("de", "bremen") == "proper name" and lemma_gate.unfit("de", "tag") is None and lemma_gate.unfit("de", "Müller") is None
    assert set(said.values()) - {None} <= set(lemma_gate.UNFIT)
    assert lemma_gate.kinds("en")["saw"] == {"noun", "verb"}


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
