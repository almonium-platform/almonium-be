"""Headwords for the writer bake-off: 40 mid-frequency lemmas per language from wordfreq, sampled
with a fixed seed from ranks 1,000 to 1,900 after spaCy has dropped inflected forms, names and
function words, plus a hand-picked hard set of 10 (homographs, heteronyms, irregular spellings,
loanwords, separable verbs).

Writes headwords/{lang}.json with the Zipf frequency of each word.

Usage: headwords.py   (run from the backend repository root)
"""
import json, random
from pathlib import Path

import spacy
from wordfreq import top_n_list, zipf_frequency

OUT = Path("scripts/lexicon_research/headwords")
OUT.mkdir(parents=True, exist_ok=True)
SEED = 20261003

HARD = {
    "en": ["lead", "tear", "wind", "present", "object", "colonel", "choir", "record", "read", "bass"],
    "de": ["übersetzen", "umfahren", "modern", "Schloss", "Montage", "Tenor", "Chirurg", "Bank", "Leiter", "umgehen"],
    "uk": ["замок", "мука", "дорога", "атлас", "орган", "брати", "книга", "плачу", "характерний", "вигляд"],
}

MODELS = {"en": "en_core_web_sm", "de": "de_core_news_sm", "uk": "uk_core_news_sm", "fr": "fr_core_news_sm", "es": "es_core_news_sm", "it": "it_core_news_sm", "pt": "pt_core_news_sm"}
KEEP = {"NOUN", "VERB", "ADJ", "ADV"}


def candidates(lang):
    """Words from ranks 1,000 to 1,900 that spaCy, given the word alone, tags as a content word and
    lemmatizes to itself. wordfreq is lowercase, so a German word is first judged lowercase: an
    inflected verb or adjective is dropped before its title-cased twin can pass as a noun, and a
    lowercase noun is written title-cased. The Ukrainian corpus carries Russian words; a word that
    wordfreq finds clearly more frequent in Russian than in Ukrainian is dropped."""
    nlp = spacy.load(MODELS[lang])
    out = []
    for w in top_n_list(lang, 3000)[1000:1900]:
        if not w.isalpha() or len(w) < 3:
            continue
        if lang == "uk" and zipf_frequency(w, "ru") > zipf_frequency(w, "uk") + 0.3:
            continue
        t = nlp(w)[0]
        if t.ent_type_ or t.pos_ not in KEEP | {"PROPN", "X"}:
            continue
        if lang == "de":
            if t.pos_ in {"VERB", "AUX", "ADJ", "ADV"} and t.lemma_.lower() != w:
                continue
            tt = nlp(w.title())[0]
            if tt.pos_ == "NOUN" and tt.lemma_ == w.title() and t.pos_ not in {"VERB", "ADJ", "ADV"}:
                out.append(w.title())
            elif t.pos_ in KEEP and t.lemma_.lower() == w:
                out.append(w)
            continue
        if t.pos_ in KEEP and t.lemma_.lower() == w:
            out.append(w)
    return out


random.seed(SEED)
for lang in ("en", "de", "uk"):
    pool = candidates(lang)
    sample = random.sample(pool, 40)
    rows = [{"headword": w, "zipf": round(zipf_frequency(w, lang), 2), "set": "frequency"} for w in sample]
    rows += [{"headword": w, "zipf": round(zipf_frequency(w, lang), 2), "set": "hard"} for w in HARD[lang]]
    (OUT / f"{lang}.json").write_text(json.dumps(rows, ensure_ascii=False, indent=1) + "\n")
    print(lang, len(rows), [r["headword"] for r in rows[:12]], "...")
