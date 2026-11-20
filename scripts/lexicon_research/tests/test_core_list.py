"""Which spellings of a frequency list are words to queue."""
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
import core_list  # noqa: E402


def test_a_russian_letter_or_abbreviation_is_not_a_word_to_queue():
    assert all(core_list.a_word("ru", w) for w in ("и", "в", "я", "как", "что-то", "ёлка"))
    assert not any(core_list.a_word("ru", w) for w in ("г", "м", "см", "км", "въ", "і", "ok"))
    assert core_list.a_word("fr", "a") and core_list.a_word("de", "km")  # only Russian has the rule


def test_the_print_spelling_of_a_russian_word_leads_to_the_spelling_with_its_letter():
    table = {"её": ("FL", ("она",)), "ее": ("A", ()), "лёт": ("L", ()), "лет": ("AF", ("год",)), "неё": ("F", ("она",))}
    assert core_list.pointed("ru", "ее", table) == ["её"] and core_list.pointed("ru", "лет", table) == ["лёт"]
    assert core_list.pointed("ru", "нее", table) == ["неё"] and core_list.pointed("ru", "дом", table) == []
    assert core_list.pointed("fr", "ete", table) == []


def test_a_one_letter_word_is_one_of_the_languages_own_and_a_capital_does_not_unmake_a_word():
    assert core_list.a_word("fr", "à") and core_list.a_word("fr", "y") and not core_list.a_word("fr", "c") and not core_list.a_word("fr", "t")
    assert core_list.a_word("en", "a") and core_list.a_word("en", "I") and not core_list.a_word("de", "s")
    assert core_list.a_word("ru", "Россия") and not core_list.a_word("ru", "г")


def test_the_older_words_are_listed_to_be_written_again(monkeypatch):
    """The owner's decision of 18 November 2026: a word whose only entry is of an older shape is written afresh,
    not mended. The list holds those words and no new ones, through the same gate, and not the ones that have
    an entry of the present shape already."""
    import pytest
    pytest.importorskip("wordfreq")
    table = {"house": ("L", ()), "houses": ("F", ("house",)), "walk": ("L", ()), "London": ("N", ()), "tree": ("L", ())}
    monkeypatch.setattr(core_list.lemma_gate, "load", lambda lang: table)
    monkeypatch.setattr(core_list.lemma_gate, "known", lambda lang, word: [(word, table[word])] if word in table else [])
    monkeypatch.setattr(core_list.lemma_gate, "covered", lambda lang, word: False)
    monkeypatch.setattr(core_list.lemma_gate, "unfit", lambda lang, word: "proper name" if word == "London" else None)
    monkeypatch.setattr(core_list.lemma_gate, "kinds", lambda lang: {})
    monkeypatch.setattr(core_list.v3_entries, "headwords", lambda lang: {"house", "houses", "walk", "London"})
    monkeypatch.setattr(core_list, "written_v4", lambda lang: {"walk"})
    good, doubt, dropped = core_list.healthy("fr", older=True)  # a language without English's rule for forms
    assert [(r["headword"], r["set"]) for r in good] == [("house", "rewrite-fr")]  # "houses" sends its base word, once
    assert dropped == {"has an entry already": 1, "proper name": 1} and not doubt
    assert "tree" not in {r["headword"] for r in good}  # a word with no older entry belongs to the healthy list
