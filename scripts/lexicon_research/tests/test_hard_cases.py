"""Hard cases: what a right entry must show, checked by a program, and two writers read side by side by a reader."""
import json
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
import hard_cases  # noqa: E402

LEAD = {"headword": "lead", "lexemes": [
    {"lemma": "lead", "pos": "VERB", "pronunciations": [{"variety": "en-GB", "ipa": "/ˈliːd/"}], "grammar": {"valency": "Both"},
     "senses": [{"definition": "to go first", "regions": ["en-GB"], "labels": {"formality": "informal", "attitude": ["vulgar"]}, "grammar": {"valency": "Intr"}}]},
    {"lemma": "lead", "pos": "NOUN", "pronunciations": [{"variety": "en-GB", "ipa": "/ˈlɛd/"}, {"variety": "en-GB", "ipa": "/ləd/", "context": "weak form"}],
     "grammar": {"gender": "Fem", "by_variety": {"de-AT": {"gender": "Neut"}}}, "forms": [{"form": "leáds", "features": {"Number": "Plur"}}, {"form": "лет", "features": {"Case": "Gen"}}],
     "senses": [{"definition": "a metal", "regions": [{"variety": "es-419", "extent": "mainly"}]}]}]}
MODERN = {"headword": "modern", "lexemes": [{"lemma": "modern", "pos": "ADJ", "pronunciations": [{"variety": "de-DE", "ipa": "/mo.ˈdɛʁn/"}], "senses": []},
                                             {"lemma": "modern", "pos": "VERB", "pronunciations": [{"variety": "de-DE", "ipa": "/ˈmoː.dɐn/"}], "senses": []}]}


def test_the_key_reads_an_entry_for_what_it_must_show():
    yes = [("lexemes_min", 2), ("ipa", "liːd", "l[eɛ]d"), ("pos", "VERB", "NOUN"), ("no_pos", "ADJ"), ("pos_min", 2), ("genders", "Fem", "Neut"),
           ("grammar", "valency", "Intr"), ("plural", "leads"), ("form", "лет"), ("regions", "en-GB"), ("regions_any", "es-MX", "es-419"),
           ("label", "slang", "informal"), ("label", "vulgar", "offensive"), ("weak",), ("context",), ("any", ("pos", "ADV"), ("pos", "NOUN")),
           ("all", ("pos", "NOUN"), ("lexemes_max", 2))]
    no = [("lexemes_min", 3), ("ipa", "waɪnd"), ("pos", "ADV"), ("no_pos", "VERB"), ("genders", "Masc"), ("genders_only", "Fem"), ("grammar", "valency", "Impers"),
          ("plural", "лет"), ("regions", "en-US"), ("label", "offensive"), ("reject",), ("stress", 2), ("any", ("pos", "ADV"), ("reject",))]
    assert [check for check in yes if not hard_cases.holds(LEAD, check)] == [] and [check for check in no if hard_cases.holds(LEAD, check)] == []
    assert hard_cases.holds(MODERN, ("stress", 2)) and hard_cases.holds({"headword": "went", "lexemes": [], "reject": "inflected form of go"}, ("reject",))
    assert not hard_cases.holds({"headword": "went", "lexemes": [], "reject": "x"}, ("no_pos", "VERB"))  # a refusal shows nothing about a word that should be written
    assert hard_cases.told(("any", ("plural", "avós"), ("reject",))) == "plural avós or refused as no lemma"


def test_every_word_of_the_set_has_a_kind_and_checks_the_key_can_read():
    assert {lang: len(words) for lang, words in hard_cases.HARD.items()} == dict.fromkeys(("en", "de", "fr", "es", "it", "pt", "ru"), 12)
    for words in hard_cases.HARD.values():
        for word, (kind, checks) in words.items():
            assert kind in ("homograph", "form", "noun", "verb", "region", "register", "sound", "particle") and (checks or kind == "particle"), word
            for check in checks:
                hard_cases.holds(LEAD, check)  # no check the reader of the key does not know


def test_two_writers_are_scored_by_the_key_and_read_side_by_side_without_names(tmp_path, monkeypatch, capsys):
    monkeypatch.setattr(hard_cases, "HARD", {"en": {"lead": ("homograph", [("lexemes_min", 2)]), "went": ("form", [("reject",)]), "by": ("particle", [])}})
    one_lexeme = {"headword": "lead", "lexemes": LEAD["lexemes"][:1]}
    refused = {"headword": "went", "lexemes": [], "reject": "inflected form of go"}
    wrote = {"review-hard-a": {"lead": (LEAD, {"total_cost_usd": 0.05}), "went": (refused, {}), "by": (LEAD, {})},
             "review-hard-b": {"lead": (one_lexeme, {"total_cost_usd": 0.03}), "went": (refused, {}), "by": (one_lexeme, {})}}
    monkeypatch.setattr(hard_cases, "written", lambda folder, lang: wrote[folder])
    monkeypatch.setattr(hard_cases, "shown", lambda lang, pair: f"refused: {pair[0]['reject']}" if not pair[0]["lexemes"] else f"{len(pair[0]['lexemes'])} lexemes")
    monkeypatch.setattr(hard_cases.measure, "MEASURE", tmp_path)
    right = hard_cases.score(["review-hard-a", "review-hard-b"])
    out = capsys.readouterr().out
    assert right["review-hard-a", "en", "lead"] and not right["review-hard-b", "en", "lead"]
    assert "review-hard-a: 2 of 2 words wholly right" in out and "review-hard-b: 1 of 2 words wholly right" in out and "en lead (homograph): b: lexemes_min 2" in out
    hard_cases.pairs("review-hard-a", "review-hard-b")
    text = (tmp_path / "hard" / "review" / "en-1.md").read_text()
    sides = json.loads((tmp_path / "hard" / "pairs-en.json").read_text())
    assert [s["headword"] for s in sides] == ["lead", "by"]  # two refusals of one word: nothing to read
    assert "review-hard" not in text and "#### Entry A" in text and "#### Entry B" in text and "missing-core" in text
    (tmp_path / "hard" / "answers").mkdir()
    better = {s["id"]: ("A" if s["A"] == "review-hard-a" else "B") for s in sides}
    (tmp_path / "hard" / "answers" / "en-1.md").write_text("\n".join(json.dumps(
        {"id": i, "better": letter if i == 1 else "same", "why": "w", "A": [], "B": [], ("B" if letter == "A" else "A"): [{"fault": "missing-core", "what": "x"}]}) for i, letter in better.items()))
    hard_cases.tally()
    out = capsys.readouterr().out
    assert "2 words read: a better in 1, b better in 0, the same in 1" in out and "faults named in b: missing-core 2" in out
    assert "by language: en a 1, b 0" in out and "shown first: a better in 1, b better in 0" in out


def test_a_sense_marked_for_northern_america_is_marked_for_the_united_states():
    entry = {"lexemes": [{"pos": "NOUN", "senses": [{"regions": [{"variety": "en-021", "extent": "mainly"}]}, {"regions": ["en-GB"]}]}]}
    assert hard_cases.holds(entry, ("regions", "en-US", "en-GB")) and not hard_cases.holds(entry, ("regions", "en-AU"))
