"""The second look at a fact the sources contradict: what is asked, what is kept of the answer, where it lands."""
import json
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
import ingest_v4  # noqa: E402
import settle_v4  # noqa: E402

TABLE = {"palabra": ({"Fem"}, [(set(), "/paˈlabɾa/", False), (set(), "[paˈla.β̞ɾa]", False)], {"palabras"}, {"the English Wiktionary"})}


def word(ipa="/ˈpa.la.bɾa/", gender="Fem"):
    return {"headword": "palabra", "lexemes": [{"lemma": "palabra", "pos": "NOUN", "grammar": {"gender": gender},
                                               "pronunciations": [{"variety": "es-ES", "ipa": ipa}],
                                               "senses": [{"definition": "sonido o grupo de sonidos con significado", "example": "e", "cefr": "A1"}]}]}


def test_one_question_to_a_transcription_and_one_to_a_gender():
    leads = settle_v4.leads_of("es", word(gender="Masc"), TABLE)
    assert [(lead["fact"], lead["was"]) for lead in leads] == [("gender", "Masc"), ("stressed syllable", "/ˈpa.la.bɾa/")]
    asked = settle_v4.question("es", "palabra", word(gender="Masc"), leads)
    assert "Word 1: palabra, NOUN; gender Masc" in asked and "1. Word 1, the gender. The entry has Masc." in asked
    assert "2. Word 1, the stressed syllable of the es-ES transcription. The entry has /ˈpa.la.bɾa/. The sources (the English Wiktionary) have /paˈlabɾa/" in asked
    assert settle_v4.leads_of("es", word("/pa.ˈla.bɾa/"), TABLE) == []  # nothing differs: nothing is asked, nothing paid


def test_a_change_stands_only_if_the_sources_then_agree():
    entry = word(gender="Masc")
    leads = settle_v4.leads_of("es", entry, TABLE)
    answer = {"decisions": [{"n": 1, "verdict": "change", "value": "Fem", "why": "la palabra"},
                            {"n": 2, "verdict": "change", "value": "/pa.la.ˈbɾa/", "why": "moved the wrong way"}]}
    gender, stress = settle_v4.decide("es", entry, leads, answer, TABLE)
    assert gender["verified"] is True and gender["now"] == "Fem" and gender["was"] == "Masc"
    assert stress["verified"] is False  # still not where the sources have it: on file as disputed, applied nowhere
    kept = settle_v4.decide("es", entry, leads, {"decisions": [{"n": 1, "verdict": "keep", "why": "both genders"}]}, TABLE)
    assert kept[0]["verdict"] == "keep" and "now" not in kept[0] and kept[1]["why"] == "no answer"
    assert entry["lexemes"][0]["grammar"]["gender"] == "Masc"  # deciding changes nothing


def test_a_decision_lands_only_where_the_entry_still_says_what_it_was_about(tmp_path, monkeypatch):
    change = {"fact": "sounds", "lexeme": 0, "variety": "es-ES", "was": "/ˈpa.la.bɾa/", "verdict": "change", "now": "/pa.ˈla.bɾa/", "verified": True}
    entry = word()
    assert ingest_v4.apply_decisions("es", entry, [change]) == 1 and entry["lexemes"][0]["pronunciations"][0]["ipa"] == "/pa.ˈla.bɾa/"
    rewritten = word("/pa.ˈla.βɾa/")  # the word was written again since: the old decision is about nothing
    assert ingest_v4.apply_decisions("es", rewritten, [change]) == 0 and rewritten["lexemes"][0]["pronunciations"][0]["ipa"] == "/pa.ˈla.βɾa/"
    monkeypatch.setattr(ingest_v4, "SETTLED", tmp_path)
    monkeypatch.setattr(ingest_v4, "_settled", {})
    (tmp_path / "es").mkdir()
    disputed = {**change, "variety": "es-MX", "now": "/pa.la.ˈbɾa/", "verified": False}
    (tmp_path / "es" / "palabra.abc.json").write_text(json.dumps({"headword": "palabra", "decisions": [change, disputed, {"fact": "gender", "lexeme": 0, "was": "Fem", "verdict": "keep"}]}))
    raw = {**word(), "language": "es"}
    assert ingest_v4.clean_entry("es", json.loads(json.dumps(raw)), {})["lexemes"][0]["pronunciations"][0]["ipa"] == "/pa.ˈla.bɾa/"
    assert ingest_v4.clean_entry("es", json.loads(json.dumps(raw)), {}).get("facts_settled") == 1
    untouched = ingest_v4.clean_entry("es", json.loads(json.dumps(raw)), {}, settle=False)
    assert untouched["lexemes"][0]["pronunciations"][0]["ipa"] == "/ˈpa.la.bɾa/" and "facts_settled" not in untouched
    assert raw["lexemes"][0]["pronunciations"][0]["ipa"] == "/ˈpa.la.bɾa/"  # the written entry is as it was
    assert ingest_v4.kept("es")["palabra"] == {("gender", 0, None, "Fem", 1)}


def test_a_second_accepted_plural_is_added_beside_the_first_and_a_later_answer_replaces_an_earlier(tmp_path, monkeypatch):
    entry = {"headword": "caméraman", "lexemes": [{"lemma": "caméraman", "pos": "NOUN", "forms": [{"form": "cameramen", "features": {"Number": "Plur"}}], "senses": []}]}
    added = {"fact": "plural", "lexeme": 0, "was": "cameramen", "verdict": "add", "now": "caméramans", "verified": True}
    assert ingest_v4.apply_decisions("fr", entry, [added]) == 1
    assert [f["form"] for f in entry["lexemes"][0]["forms"]] == ["cameramen", "caméramans"]
    assert ingest_v4.apply_decisions("fr", entry, [added]) == 0  # once
    assert "third answer" in settle_v4.system_prompt("fr", plural=True) and "third answer" not in settle_v4.system_prompt("fr")
    monkeypatch.setattr(ingest_v4, "SETTLED", tmp_path)
    monkeypatch.setattr(ingest_v4, "_settled", {})
    (tmp_path / "fr").mkdir()
    replaced = {**added, "verdict": "change"}
    (tmp_path / "fr" / "caméraman.aaa.json").write_text(json.dumps({"headword": "caméraman", "settle_version": "settle-v1", "decisions": [replaced]}))
    (tmp_path / "fr" / "caméraman.bbb.json").write_text(json.dumps({"headword": "caméraman", "settle_version": "settle-v2", "decisions": [added]}))
    assert ingest_v4.settled("fr")["caméraman"] == [added]


def test_the_same_entry_asks_the_same_question_in_every_run():
    """A question is paid for by its text. The sounds that differ are part of it, and were once picked from a set in
    whatever order the run happened to have: three questions were bought twice."""
    import subprocess
    code = ("import sys; sys.path.insert(0, 'scripts/lexicon_research'); import witness; "
            "print(witness.differences('pt', {'admiravew', 'admirabew'}, {'adʒimiravew', 'adʒmiravew', 'adimiravew'}))")
    seen = {subprocess.run([sys.executable, "-c", code], capture_output=True, text=True, env={"PYTHONHASHSEED": str(seed), "PATH": "/usr/bin:/bin"},
                           cwd=Path(__file__).resolve().parents[3]).stdout for seed in (1, 2, 3, 4, 5)}
    assert len(seen) == 1 and seen != {""}
