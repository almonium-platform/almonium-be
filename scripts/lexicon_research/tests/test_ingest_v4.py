"""The rule-based step from written entries to the clean lexicon, on hand-made entries. Run from the repository root:
    scripts/.venv/bin/python -m pytest scripts/lexicon_research/tests -q
"""

import json
import sys
from pathlib import Path

import pytest

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
import ingest_v4  # noqa: E402
import lemma_gate  # noqa: E402


def sense(definition, **more):
    return {"definition": definition, "example": "An example sentence of some words.", "cefr": "B1", **more}


def lexeme(lemma, pos="NOUN", senses=None, **more):
    return {"lemma": lemma, "pos": pos, "kind": "word", "pronunciations": [{"variety": "en-US", "ipa": "/ˈx/"}],
            "senses": senses or [sense("a thing of some kind that people use")], **more}


def entry(headword, *lexemes, lang="en"):
    return {"language": lang, "headword": headword, "lexemes": list(lexemes)}


META = {"writer": "cc-claude-opus-5-5-v4", "model": "claude-opus-5-5", "effort": "high", "prompt_version": "v4"}


# ---------------------------------------------------------------- ids


def test_ids_are_the_same_on_every_machine_and_every_run():
    raw = entry("lead", lexeme("lead", "VERB"), lexeme("lead"), lexeme("lead", grammar={"gender": "Fem"}))
    one, two = ingest_v4.clean_entry("en", raw, META), ingest_v4.clean_entry("en", raw, META)
    assert one == two
    assert [lx["homograph"] for lx in one["lexemes"]] == [1, 1, 2]
    assert len({lx["id"] for lx in one["lexemes"]} | {s["id"] for lx in one["lexemes"] for s in lx["senses"]}) == 6
    assert one["source"]["prompt_sha"] == ingest_v4.V4_SHA["en"]  # entries written before the hash was recorded


def test_a_rewrite_keeps_the_id_of_a_sense_that_says_the_same():
    first = ingest_v4.clean_entry("en", entry("camp", lexeme("camp", senses=[
        sense("a place where people live in tents for a short time"),
        sense("a guarded place where prisoners are kept by force"),
        sense("a group of people who share the same opinions")])), META)
    a, b, c = [s["id"] for s in first["lexemes"][0]["senses"]]
    rewritten = entry("camp", lexeme("camp", senses=[
        sense("a guarded place where prisoners are kept by force"),           # the same words, moved up
        sense("a place where people live in tents for a short while"),         # one word changed
        sense("a style that is funny because it is deliberately too much")]))  # new; the third old sense is gone
    second = ingest_v4.clean_entry("en", rewritten, {**META, "prompt_version": "v4.1"}, previous=first)
    lx = second["lexemes"][0]
    assert lx["id"] == first["lexemes"][0]["id"]
    assert [s["id"] for s in lx["senses"]][:2] == [b, a]
    assert lx["senses"][2]["id"] not in (a, b, c)
    assert lx["retired"] == [{"id": c, "definition": "a group of people who share the same opinions"}]
    # ingesting the same rewrite again changes nothing, and the retired sense stays on record
    assert ingest_v4.clean_entry("en", rewritten, {**META, "prompt_version": "v4.1"}, previous=second) == second


def test_homographs_keep_their_ids_when_a_rewrite_lists_them_in_another_order():
    castle = lexeme("замок", pronunciations=[{"variety": "uk-UA", "ipa": "/ˈza.mɔk/"}], senses=[sense("велика укріплена будівля, де жили правителі")])
    lock = lexeme("замок", pronunciations=[{"variety": "uk-UA", "ipa": "/za.ˈmɔk/"}], senses=[sense("пристрій, яким зачиняють двері на ключ")])
    first = ingest_v4.clean_entry("uk", entry("замок", castle, lock, lang="uk"), META)
    second = ingest_v4.clean_entry("uk", entry("замок", lock, castle, lang="uk"), META, previous=first)
    assert [lx["id"] for lx in second["lexemes"]] == [first["lexemes"][1]["id"], first["lexemes"][0]["id"]]
    assert [lx["homograph"] for lx in second["lexemes"]] == [2, 1]


def test_a_new_homograph_never_takes_the_number_of_one_that_was_dropped():
    first = ingest_v4.clean_entry("en", entry("bass", lexeme("bass", grammar={"gender": "Masc"}), lexeme("bass", grammar={"gender": "Fem"})), META)
    second = ingest_v4.clean_entry("en", entry("bass", lexeme("bass", grammar={"gender": "Fem"}), lexeme("bass", "ADJ")), META, previous=first)
    assert [(lx["pos"], lx["homograph"]) for lx in second["lexemes"]] == [("NOUN", 2), ("ADJ", 1)]
    assert second["retired_lexemes"] == [{"id": first["lexemes"][0]["id"], "lemma": "bass", "pos": "NOUN"}]


def test_a_form_tied_to_senses_names_them_by_id():
    raw = entry("braccio", lexeme("braccio", senses=[sense("parte del corpo dalla spalla alla mano"), sense("parte lunga che sporge da una macchina")],
                                  forms=[{"form": "braccia", "features": {"Number": "Plur"}, "senses": [1]}]), lang="it")
    lx = ingest_v4.clean_entry("it", raw, META)["lexemes"][0]
    assert lx["forms"][0] == {"form": "braccia", "features": {"Number": "Plur"}, "sense_ids": [lx["senses"][0]["id"]]}


# ---------------------------------------------------------------- leaks and duplicates


def test_a_lexeme_of_another_lemma_is_dropped_but_a_capital_or_a_listed_spelling_is_not():
    raw = entry("hash", lexeme("hash"), lexeme("hash out", "VERB"), lexeme("Hash"),
                lexeme("hashe", spellings=[{"text": "hashe"}, {"text": "hash"}]))
    clean = ingest_v4.clean_entry("en", raw, META)
    assert [lx["lemma"] for lx in clean["lexemes"]] == ["hash", "Hash", "hashe"]
    assert clean["dropped"] == [{"lemma": "hash out", "pos": "VERB", "reason": "not the headword's lemma"}]


def both_spellings():
    spellings = [{"text": "color", "varieties": ["en-US"]}, {"text": "colour", "varieties": ["en-GB"]}]
    us = entry("color", lexeme("color", senses=[sense("the quality such as red or blue that you see in things")], spellings=spellings),
               lexeme("color", "VERB", senses=[sense("to put paint or pencil on a picture")], spellings=spellings))
    gb = entry("colour", lexeme("colour", senses=[sense("the quality such as red, blue or green that you see in things")], spellings=spellings),
               lexeme("colour", "VERB", senses=[sense("to put paint or pencil on something")]))  # this one does not list the other spelling
    return {"color": (us, META), "colour": (gb, META)}


def test_a_word_written_under_two_spellings_is_kept_once_under_the_more_frequent():
    duplicates, aliases = ingest_v4.find_duplicates(both_spellings(), {"color": 5.2, "colour": 4.9})
    assert dict(duplicates) == {"colour": {0: "color", 1: "color"}}
    assert aliases == {"colour": {"to": "color", "as": "spelling"}}
    duplicates, aliases = ingest_v4.find_duplicates(both_spellings(), {"color": 4.0, "colour": 4.9})
    assert dict(duplicates) == {"color": {0: "colour", 1: "colour"}}


def test_another_word_with_the_same_spelling_is_not_a_duplicate():
    tyre = entry("tyre", lexeme("tyre", senses=[sense("a thick rubber ring that fits round a wheel")],
                                spellings=[{"text": "tyre", "varieties": ["en-GB"]}, {"text": "tire", "varieties": ["en-US"]}]))
    tire = entry("tire", lexeme("tire", "VERB", senses=[sense("to make someone feel that they need to rest")]),
                 lexeme("tire", senses=[sense("a thick rubber ring that fits around a wheel")]))
    duplicates, aliases = ingest_v4.find_duplicates({"tyre": (tyre, META), "tire": (tire, META)}, {"tire": 4.5, "tyre": 3.9})
    assert dict(duplicates) == {"tyre": {0: "tire"}} and aliases == {"tyre": {"to": "tire", "as": "spelling"}}
    story = entry("story", lexeme("story", senses=[sense("a description of events that people tell or read for pleasure")]))
    storey = entry("storey", lexeme("storey", senses=[sense("one level of a building, with rooms on it")],
                                    spellings=[{"text": "storey"}, {"text": "story", "varieties": ["en-US"]}]))
    assert ingest_v4.find_duplicates({"story": (story, META), "storey": (storey, META)}, {})[0] == {}


def test_a_reject_that_names_its_word_becomes_an_alias_of_the_right_kind():
    raw = {"dass": (entry("dass", lexeme("dass", "SCONJ"), lang="de"), META),
           "daß": ({"language": "de", "headword": "daß", "lexemes": [], "reject": "pre-reform spelling of dass"}, META),
           "das s": ({"language": "de", "headword": "das s", "lexemes": [], "reject": "misspelling of \"dass\""}, META),
           "Dass": ({"language": "de", "headword": "Dass", "lexemes": [], "reject": "spelling of dass"}, META),
           "dasse": ({"language": "de", "headword": "dasse", "lexemes": [], "reject": "inflected form of dassen"}, META)}
    aliases = ingest_v4.find_duplicates(raw, {})[1]
    assert aliases == {"daß": {"to": "dass", "as": "old spelling"}, "das s": {"to": "dass", "as": "misspelling"}, "Dass": {"to": "dass", "as": "spelling"}}


def test_a_reject_as_an_inflected_form_becomes_an_alias_of_its_lemma():
    def reject(headword, reason):
        return ({"language": "en", "headword": headword, "lexemes": [], "reject": reason}, META)
    raw = {"sign": (entry("sign", lexeme("sign", "VERB")), META), "vocal": (entry("vocal", lexeme("vocal")), META),
           "signed": reject("signed", "inflected form of sign (past tense and past participle)"),
           "vocals": reject("vocals", "inflected form (plural) of the noun vocal"),
           "Signing": reject("Signing", "present participle of sign"),
           "soelden": reject("soelden", "Place name, not a common word; otherwise only the plural of the noun sign"),
           "ricky": reject("ricky", "English given name (a pet form of vocal), not a word"),
           "walked": reject("walked", "inflected form of walk")}  # walk has no entry yet: nothing to point at
    aliases = ingest_v4.find_duplicates(raw, {})[1]
    assert aliases == {"signed": {"to": "sign", "as": "form"}, "vocals": {"to": "vocal", "as": "form"}, "Signing": {"to": "sign", "as": "form"}}


# ---------------------------------------------------------------- the whole step, on disk


@pytest.fixture
def out(tmp_path, monkeypatch):
    monkeypatch.setattr(ingest_v4, "OUT", tmp_path)
    monkeypatch.setattr(ingest_v4, "CLEAN", tmp_path / "lexicon-s4")
    monkeypatch.setattr(ingest_v4, "HERE", tmp_path)
    (tmp_path / "headwords").mkdir()
    (tmp_path / "headwords" / "v4-top-en.json").write_text(json.dumps([{"headword": "color", "zipf": 5.2}, {"headword": "colour", "zipf": 4.9}]))
    d = tmp_path / "cc-claude-opus-5-5-v4" / "en"
    d.mkdir(parents=True)
    for headword, (e, meta) in both_spellings().items():
        (d / f"{headword}.json").write_text(json.dumps(e, ensure_ascii=False))
        (d / f"{headword}.meta.json").write_text(json.dumps({k: v for k, v in meta.items() if k != "writer"}))
    (d / "broken.json").write_text(json.dumps({"error": "not valid", "raw": "{"}))
    return tmp_path


def test_ingest_writes_clean_entries_and_a_second_run_writes_nothing(out):
    counts, _, aliases = ingest_v4.ingest("en")
    assert (counts["entries"], counts["lexemes"], counts["duplicate lexemes dropped"], counts["aliases"], counts["files written"]) == (2, 2, 2, 1, 2)
    clean = json.loads((out / "lexicon-s4" / "en" / "color.json").read_text())
    assert clean["aliases"] == ["colour"] and clean["source"]["writer"] == "cc-claude-opus-5-5-v4"
    assert json.loads((out / "lexicon-s4" / "en" / "colour.json").read_text())["lexemes"] == []
    assert json.loads((out / "lexicon-s4" / "en" / "_aliases.json").read_text()) == {"colour": {"to": "color", "as": "spelling"}}
    assert not (out / "lexicon-s4" / "en" / "broken.json").exists()
    assert ingest_v4.ingest("en")[0]["files written"] == 0
    # the written entries are never touched
    assert "id" not in json.loads((out / "cc-claude-opus-5-5-v4" / "en" / "color.json").read_text())["lexemes"][0]


# ---------------------------------------------------------------- what the gate tells the writer


def test_the_gate_knows_which_spellings_written_entries_cover(out, monkeypatch):
    monkeypatch.setattr(lemma_gate, "OUT", out)
    monkeypatch.setattr(lemma_gate, "_covered", {})
    assert lemma_gate.covered("en", "Colour") == [("color", "NOUN"), ("color", "VERB")]
    assert lemma_gate.covered("en", "tire") == []
    lemma_gate.register("en", entry("tyre", lexeme("tyre", spellings=[{"text": "tyre"}, {"text": "tire"}])))
    lemma_gate.register("en", {"error": "not valid"})
    assert lemma_gate.covered("en", "tire") == [("tyre", "NOUN")]


def test_a_transcription_gets_the_dot_it_lacks_before_a_stress_mark():
    assert ingest_v4.dotted("/əˈlaʊ/") == "/ə.ˈlaʊ/"
    assert ingest_v4.dotted("/ˌɪn.təˈɹɛs.tɪd/") == "/ˌɪn.tə.ˈɹɛs.tɪd/"
    for already in ("/ˈliːd/", "/ə.ˈlaʊ/", "/ˈmʌ.ðɚ ˈɪn.lɔː/", "/ˈɡɪv ˈʌp/"):
        assert ingest_v4.dotted(already) == already
    raw = entry("allow", lexeme("allow", "VERB", pronunciations=[{"variety": "en-US", "ipa": "/əˈlaʊ/"}, {"variety": "en-GB", "ipa": "/ə.ˈlaʊ/"}],
                                forms=[{"form": "allowed", "features": {"Tense": "Past"}, "ipa": {"en-US": "/əˈlaʊd/"}}]))
    clean = ingest_v4.clean_entry("en", raw, META)
    assert [p["ipa"] for p in clean["lexemes"][0]["pronunciations"]] == ["/ə.ˈlaʊ/", "/ə.ˈlaʊ/"]
    assert clean["lexemes"][0]["forms"][0]["ipa"] == {"en-US": "/ə.ˈlaʊd/"} and clean["syllable_dots_added"] == 2
    assert raw["lexemes"][0]["pronunciations"][0]["ipa"] == "/əˈlaʊ/"  # the written entry is left as it was


def test_a_french_or_italian_pronominal_sense_gets_the_auxiliary_it_is_conjugated_with():
    verb = lexeme("expliquer", "VERB", grammar={"conjugation": "1", "auxiliary": "have"},
                  senses=[sense("faire comprendre une chose à une autre personne"),
                          sense("dire clairement ce que l'on pense ou veut", grammar={"valency": "Refl"}),
                          sense("se disputer avec une personne pour régler un désaccord", grammar={"reflexive": True, "auxiliary": "be"})])
    raw = entry("expliquer", verb, lang="fr")
    clean = ingest_v4.clean_entry("fr", raw, META)
    assert [s.get("grammar") for s in clean["lexemes"][0]["senses"]] == [{"auxiliary": "have"}, {"valency": "Refl", "auxiliary": "be"}, {"reflexive": True, "auxiliary": "be"}]
    # the word no longer says "have" for all its senses: that value went to the one sense it is true of
    assert clean["pronominal_auxiliaries_set"] == 1 and clean["lexemes"][0]["grammar"] == {"conjugation": "1"}
    assert "auxiliary" not in raw["lexemes"][0]["senses"][1]["grammar"]  # the written entry is left as it was
    # a verb that is pronominal throughout already says so once; German and Spanish have no such rule
    whole = entry("évanouir", lexeme("évanouir", "VERB", grammar={"auxiliary": "be", "reflexive": True}, senses=[sense("perdre connaissance pendant un court moment", grammar={"valency": "Refl"})]), lang="fr")
    assert "pronominal_auxiliaries_set" not in ingest_v4.clean_entry("fr", whole, META)
    assert "pronominal_auxiliaries_set" not in ingest_v4.clean_entry("de", entry("regeln", verb, lang="de"), META)


def test_an_italian_double_consonant_is_written_twice_however_it_was_written():
    same_word = {"/ˈfatːo/": "/ˈfat.to/", "/ˈfatto/": "/ˈfat.to/", "/ˈfatː.o/": "/ˈfat.to/", "/ˈfat.to/": "/ˈfat.to/",
                 "/pro.ɡraˈmːa.re/": "/pro.ɡram.ˈma.re/", "/ˈpittsa/": "/ˈpit.tsa/", "/ˈpitsːa/": "/ˈpit.tsa/", "/ˈɔddʒi/": "/ˈɔd.dʒi/",
                 "/ˈpeʃːe/": "/ˈpeʃ.ʃe/", "/beˈlːettsa/": "/bel.ˈlet.tsa/", "/ˈka.ne/": "/ˈka.ne/"}
    for written, printed in same_word.items():
        assert ingest_v4.dotted(ingest_v4.italian_doubles(written)) == printed
    raw = entry("fatto", lexeme("fatto", pronunciations=[{"variety": "it-IT", "ipa": "/ˈfatːo/", "reading": "fàtto"}],
                                forms=[{"form": "fatti", "features": {"Number": "Plur"}, "ipa": {"it-IT": "/ˈfat.ti/"}}]), lang="it")
    clean = ingest_v4.clean_entry("it", raw, META)
    assert clean["lexemes"][0]["pronunciations"][0] == {"variety": "it-IT", "ipa": "/ˈfat.to/", "reading": "fàtto"}
    assert clean["double_consonants_rewritten"] == 1 and "syllable_dots_added" not in clean
    assert ingest_v4.clean_entry("en", entry("fatto", lexeme("fatto", pronunciations=[{"variety": "en-US", "ipa": "/ˈfɑːtoʊ/"}])), META)["lexemes"][0]["pronunciations"][0]["ipa"] == "/ˈfɑːtoʊ/"


def test_the_ll_of_spain_is_written_as_most_of_spain_says_it():
    raw = entry("calle", lexeme("calle", pronunciations=[{"variety": "es-ES", "ipa": "/ˈka.ʎe/"}, {"variety": "es-MX", "ipa": "/ˈka.ʝe/"}]), lang="es")
    clean = ingest_v4.clean_entry("es", raw, META)
    assert [p["ipa"] for p in clean["lexemes"][0]["pronunciations"]] == ["/ˈka.ʝe/", "/ˈka.ʝe/"] and clean["ll_of_spain_rewritten"] == 1
    italian = entry("figlio", lexeme("figlio", pronunciations=[{"variety": "it-IT", "ipa": "/ˈfiʎ.ʎo/"}]), lang="it")
    assert ingest_v4.clean_entry("it", italian, META)["lexemes"][0]["pronunciations"][0]["ipa"] == "/ˈfiʎ.ʎo/"


def test_a_value_stated_for_the_word_that_one_sense_overrides_is_moved_to_the_other_senses():
    noun = lexeme("unidade", grammar={"gender": "Fem", "countability": "Count"},
                  senses=[sense("uma coisa só, considerada à parte das outras"), sense("o que faz de várias partes um todo", grammar={"countability": "Mass"}),
                          sense("parte de uma organização com função própria", grammar={"note": "often plural"})])
    clean = ingest_v4.clean_entry("pt", entry("unidade", noun, lang="pt"), META)
    got = clean["lexemes"][0]
    assert got["grammar"] == {"gender": "Fem"} and clean["word_grammar_moved_to_senses"] == 1
    assert [s.get("grammar") for s in got["senses"]] == [{"countability": "Count"}, {"countability": "Mass"}, {"countability": "Count", "note": "often plural"}]
    assert noun["grammar"] == {"gender": "Fem", "countability": "Count"}  # the written entry is left as it was
    agreed = lexeme("lake", grammar={"countability": "Count"}, senses=[sense("a large area of water with land around it", grammar={"countability": "Count"})])
    assert "word_grammar_moved_to_senses" not in ingest_v4.clean_entry("en", entry("lake", agreed), META)
    # a word said to be both, whose senses say which, is as the prompt asks: nothing to move
    both = lexeme("study", "VERB", grammar={"valency": "Both"}, senses=[sense("to learn about a subject at school", grammar={"valency": "Tr"}), sense("to spend time learning", grammar={"valency": "Intr"})])
    assert ingest_v4.clean_entry("en", entry("study", both), META)["lexemes"][0]["grammar"] == {"valency": "Both"}


def test_a_value_that_holds_mainly_keeps_that_when_it_moves_to_the_senses():
    noun = lexeme("soap", grammar={"countability": "Mass", "mainly": ["countability"]},
                  senses=[sense("a substance used with water for washing"), sense("a television story told in many parts", grammar={"countability": "Count"})])
    got = ingest_v4.clean_entry("en", entry("soap", noun), META)["lexemes"][0]
    assert "grammar" not in got
    assert [s.get("grammar") for s in got["senses"]] == [{"countability": "Mass", "mainly": ["countability"]}, {"countability": "Count"}]


def test_a_stress_mark_written_into_a_form_goes_to_its_reading():
    noun = lexeme("год", pronunciations=[{"variety": "ru-RU", "ipa": "/ˈɡot/"}],
                  forms=[{"form": "го́да", "features": {"Case": "Gen", "Number": "Sing"}, "reading": "го́да"},
                         {"form": "году́", "features": {"Case": "Loc", "Number": "Sing"}}, {"form": "лет", "features": {"Case": "Gen", "Number": "Plur"}}])
    clean = ingest_v4.clean_entry("ru", entry("год", noun, lang="ru"), META)
    assert [(f["form"], f.get("reading")) for f in clean["lexemes"][0]["forms"]] == [("года", "го́да"), ("году", "году́"), ("лет", None)]
    assert clean["stress_marks_moved_to_readings"] == 2
