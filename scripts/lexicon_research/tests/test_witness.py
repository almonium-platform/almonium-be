"""The witness compares what a writer wrote with what Wiktionary states. It must not cry wolf over two ways
of writing one sound, and it must not miss a stress on the wrong syllable."""
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
import witness  # noqa: E402


def table(word, genders=(), heard=(), plurals=()):
    return {word: (set(genders), [(set(regions), ipa.lstrip("?"), ipa.startswith("?")) for regions, ipa in heard], set(plurals))}


def entry(lemma, pos="NOUN", grammar=None, pronunciations=(), forms=()):
    return {"lexemes": [{"lemma": lemma, "pos": pos, "grammar": grammar or {}, "pronunciations": list(pronunciations), "forms": list(forms)}]}


def verdicts(lang, written, known):
    return {(fact, verdict) for fact, verdict, _ in witness.compare(lang, written, known)}


def test_syllables_are_counted_by_their_vowels():
    assert witness.nuclei("haʊ̯s") == 1  # a diphthong is one syllable
    assert witness.nuclei("kaˈdena") == 3
    assert witness.nuclei("ˈmɛːtçn̩") == 2  # a consonant marked as a syllable


def test_stress_place_counts_from_both_ends():
    assert witness.stress_place("/ka.ˈde.na/") == (1, 2)
    assert witness.stress_place("/ˈka.de.na/") == (0, 3)
    assert witness.stress_place("/haʊs/") == (0, 1)  # one syllable needs no mark
    assert witness.stress_place("/ka.de.na/") is None  # three syllables and no mark: the transcription does not say


def test_a_stress_on_another_syllable_differs():
    known = table("cadena", heard=[((), "/kaˈdena/"), ((), "[kaˈð̞e.na]")])
    wrong = entry("cadena", pronunciations=[{"variety": "es-ES", "ipa": "/ˈka.de.na/"}])
    right = entry("cadena", pronunciations=[{"variety": "es-ES", "ipa": "/ka.ˈde.na/"}])
    assert ("stressed syllable", "differs") in verdicts("es", wrong, known)
    assert ("stressed syllable", "agrees") in verdicts("es", right, known)
    assert ("sounds", "agrees") in verdicts("es", right, known)  # dots and the place of the mark are not sounds


def test_a_run_of_vowels_divided_differently_is_the_same_stress():
    assert witness.same_stress(witness.stress_place("/te.a.ˈtɾal/"), witness.stress_place("/teaˈtɾal/"))
    assert not witness.same_stress(witness.stress_place("/ˈkɾe.dʲit/"), witness.stress_place("[krʲɪˈdʲit]"))


def test_two_spellings_of_one_sound_agree():
    known = table("acto", heard=[((), "/ˈaɡto/")])
    assert ("sounds", "agrees") in verdicts("es", entry("acto", pronunciations=[{"variety": "es-ES", "ipa": "/ˈak.to/"}]), known)
    known = table("convertir", heard=[((), "/kombeɾˈtiɾ/")])
    assert ("sounds", "agrees") in verdicts("es", entry("convertir", "VERB", pronunciations=[{"variety": "es-MX", "ipa": "/kon.beɾ.ˈtiɾ/"}]), known)


def test_another_sound_differs_and_is_named():
    known = table("carrera", heard=[((), "/kaˈreɾa/")])
    found = witness.compare("es", entry("carrera", pronunciations=[{"variety": "es-ES", "ipa": "/ka.ˈre.ra/"}]), known)
    assert [detail.rpartition("[")[2] for fact, verdict, detail in found if fact == "sounds" and verdict == "differs"] == ["r>ɾ]"]


def test_spain_is_held_against_the_transcription_with_theta():
    known = table("cena", heard=[((), "/ˈθena/"), ((), "/ˈsena/")])
    assert ("sounds", "differs") in verdicts("es", entry("cena", pronunciations=[{"variety": "es-ES", "ipa": "/ˈse.na/"}]), known)
    assert ("sounds", "agrees") in verdicts("es", entry("cena", pronunciations=[{"variety": "es-MX", "ipa": "/ˈse.na/"}]), known)


def test_a_variety_is_held_against_its_own_place():
    heard = [({"Brazil"}, "/ɡaʁˈɡɐ̃.tɐ/"), ({"São-Paulo"}, "/ɡaɾˈɡɐ̃.tɐ/"), ({"Portugal"}, "/ɡɐɾˈɡɐ̃.tɐ/")]
    known = table("garganta", ["Fem"], heard)
    lisbon_as_brazil = entry("garganta", grammar={"gender": "Fem"}, pronunciations=[{"variety": "pt-PT", "ipa": "/ɡaɾ.ˈɡɐ̃.tɐ/"}])
    assert ("sounds", "differs") in verdicts("pt", lisbon_as_brazil, known)
    assert ("gender", "agrees") in verdicts("pt", lisbon_as_brazil, known)
    brazil = entry("garganta", pronunciations=[{"variety": "pt-BR", "ipa": "/ɡaɾ.ˈɡɐ̃.tɐ/"}])
    assert ("sounds", "agrees") in verdicts("pt", brazil, known)


def test_gender_is_among_those_wiktionary_gives():
    known = table("See", ["Masc", "Fem"])
    assert ("gender", "agrees") in verdicts("de", entry("See", grammar={"gender": "Fem"}), known)
    assert ("gender", "differs") in verdicts("de", entry("See", grammar={"gender": "Neut"}), known)
    assert not verdicts("de", entry("Meer", grammar={"gender": "Neut"}), known)  # a word Wiktionary does not have is not judged


def test_plural_is_the_nominative_one():
    known = table("Haus", ["Neut"], plurals=["Häuser"])
    forms = [{"form": "Hauses", "features": {"Case": "Gen", "Number": "Sing"}}, {"form": "Häuser", "features": {"Case": "Nom", "Number": "Plur"}}]
    assert ("plural", "agrees") in verdicts("de", entry("Haus", grammar={"gender": "Neut"}, forms=forms), known)
    forms[1]["form"] = "Hause"
    assert ("plural", "differs") in verdicts("de", entry("Haus", grammar={"gender": "Neut"}, forms=forms), known)


def said(lang, variety, ours, heard):
    """The verdict on the sounds of one transcription of ours against a word's transcriptions."""
    known = table("w", heard=heard)
    found = witness.compare(lang, entry("w", "VERB", pronunciations=[{"variety": variety, "ipa": ours}]), known)
    return [verdict for fact, verdict, _ in found if fact == "sounds"]


def test_a_sound_in_brackets_may_be_said_or_not():
    assert witness.readings_of("/ˈkʌl.ə(ɹ)/") == ["ˈkʌl.əɹ", "ˈkʌl.ə"]
    assert witness.readings_of("/twɛlfθ/ [tʰw̥ɛɫfθ]") == ["twɛlfθ", "tʰw̥ɛɫfθ"]
    assert said("en", "en-GB", "/ˈkʌl.ə/", [(("Received-Pronunciation",), "/ˈkʌl.ə(ɹ)/")]) == ["agrees"]
    assert said("en", "en-US", "/ˈkʌl.ɚ/", [(("General-American",), "/ˈkʌl.ə(ɹ)/")]) == ["agrees"]


def test_english_notation_is_brought_to_ours():
    rp, ga = ("Received-Pronunciation",), ("General-American",)
    assert said("en", "en-GB", "/ˈbɒt.əl/", [(rp, "/ˈbɒtl̩/")]) == ["agrees"]  # a consonant that is a syllable
    assert said("en", "en-GB", "/ə.ˈdɹes/", [(rp, "/əˈdɹɛs/")]) == ["agrees"]  # ɛ is our e in Britain
    assert said("en", "en-US", "/ˈʃiːp/", [(ga, "/ʃip/")]) == ["agrees"]  # length left out in America
    assert said("en", "en-US", "/ˈʃiːp/", [(ga, "/ʃɪp/")]) == ["differs"]  # but sheep is not ship
    assert said("en", "en-GB", "/ˈhæp.i/", [(rp, "/ˈhæpɪ/")]) == ["agrees"]  # the vowel of happy
    assert said("en", "en-US", "/ˈsɝ.kəl/", [(ga, "/ˈsɜɹkəl/")]) == ["agrees"]
    assert said("en", "en-US", "/ˈʃɪɹ/", [(ga, "/ʃɪɚ/")]) == ["agrees"]
    assert said("en", "en-US", "/ˈkɑːɡ/", [(ga, "/kɔɡ/")]) == ["agrees"]  # cot and caught
    assert said("en", "en-GB", "/ˈkɑːt/", [(rp, "/kɔːt/")]) == ["differs"]  # not in Britain
    assert said("en", "en-US", "/ˈfæð.ɚm/", [(ga, "/ˈfæðəm/")]) == ["differs"]


def test_a_dialect_of_wiktionary_confirms_and_does_not_contradict():
    heard = [(("General-American",), "/ˈpaʊnd/"), ((), "?/ˈpæʊ̯nd/")]  # the second is set apart: "Southern US"
    assert said("en", "en-US", "/ˈpaʊnd/", heard) == ["agrees"]
    assert said("en", "en-US", "/ˈpæʊnd/", heard) == ["agrees"]
    assert said("en", "en-US", "/ˈpɪnd/", heard) == ["differs"]
    assert said("en", "en-US", "/ˈpæʊnd/", [((), "?/ˈpæʊ̯nd/")]) == []  # nothing standard to hold it against


def test_a_place_wiktionary_does_not_name_cannot_contradict_one_variety():
    known = table("bower", heard=[((), "/ˈbaʊ.əɹ/")])
    both = entry("bower", pronunciations=[{"variety": "en-US", "ipa": "/ˈbaʊ.ɚ/"}, {"variety": "en-GB", "ipa": "/ˈbaʊ.ə/"}])
    assert [v for f, v, _ in witness.compare("en", both, known) if f == "sounds"] == ["agrees"]
    neither = entry("bower", pronunciations=[{"variety": "en-US", "ipa": "/ˈboʊ.ɚ/"}, {"variety": "en-GB", "ipa": "/ˈbəʊ.ə/"}])
    assert [v for f, v, _ in witness.compare("en", neither, known) if f == "sounds"] == ["differs", "differs"]


def test_german_notation_is_brought_to_ours():
    assert said("de", "de-DE", "/ˈɡeː.ɡən.zats/", [((), "/ˈɡeːɡn̩zats/")]) == ["agrees"]
    assert said("de", "de-DE", "/ˈfɔʏ.ɐ/", [((), "/ˈfɔʏ̯ər/")]) == ["agrees"]  # -er, and r after a vowel
    assert said("de", "de-DE", "/fɛɐ.ˈkeːɐ/", [((), "/fɛrˈkeːr/")]) == ["agrees"]
    assert said("de", "de-DE", "/fɛɐ.ˈaɪn/", [((), "/fɛˈʁaɪ̯n/")]) == ["agrees"]
    assert said("de", "de-DE", "/ak.ˈtsjoːn/", [((), "/akˈtsi̯oːn/")]) == ["agrees"]
    assert said("de", "de-DE", "/po.li.ˈtiːk/", [((), "/poːliˈtiːk/")]) == ["agrees"]  # length outside the stress
    assert said("de", "de-DE", "/ˈʃtat/", [((), "/ʃtaːt/")]) == ["differs"]  # Stadt is not Staat
    assert said("de", "de-DE", "/mɪ.ˈnuː.tə/", [((), "/miˈnuːtə/")]) == ["differs"]


def test_russian_narrow_transcription_is_brought_to_ours():
    assert said("ru", "ru-RU", "/mə.lɐ.ˈko/", [((), "[məɫɐˈko]")]) == ["agrees"]
    assert said("ru", "ru-RU", "/ˈtɕas.tʲi/", [((), "[ˈt͡ɕæsʲtʲɪ]")]) == ["agrees"]  # æ between soft consonants, the s softened by the t
    assert said("ru", "ru-RU", "/u.ˈvʲe.rʲɪn.nə/", [((), "[ʊˈvʲerʲɪn(ː)ə]")]) == ["agrees"]
    assert said("ru", "ru-RU", "/də.ˈvatʲ/", [((), "[dɐˈvatʲ]")]) == ["differs"]  # ɐ before the stress is our own rule
    assert said("ru", "ru-RU", "/ˈte.mə/", [((), "[ˈtʲemə]")]) == ["differs"]


def test_french_and_italian_habits_of_notation():
    assert said("fr", "fr-FR", "/o.pe.ʁa.ˈsjɔ̃/", [((), "/ɔ.pe.ʁa.sjɔ̃/")]) == ["agrees"]
    assert said("fr", "fr-FR", "/ˈkɔt/", [((), "/kot/")]) == ["differs"]  # cote is not côte: the last syllable is judged
    assert said("fr", "fr-FR", "/ab.ˈsɛ̃t/", [((), "/ap.sɛ̃t/")]) == ["agrees"]
    assert said("fr", "fr-FR", "/ta.lis.ˈman/", [((), "/ta.lis.mɑ̃/")]) == ["differs"]
    assert said("it", "it-IT", "/pre.kaw.ˈtsjo.ne/", [((), "/pre.kawtˈt͡sjo.ne/")]) == ["agrees"]
    assert said("it", "it-IT", "/ˈkau.za/", [((), "/ˈkaw.sa/")]) == ["agrees"]
    assert said("it", "it-IT", "/an.ˈtɛn.na/", [((), "/anˈten.na/")]) == ["differs"]
