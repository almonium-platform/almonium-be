"""A tranche of fresh headwords for real-fill tests: wordfreq ranks from --start, filtered like the
bake-off set, minus every word that already has an entry under out/ or sits in another tranche.

Tests of a new path or model run on these, so no call is spent on a word that has an entry.

Usage: tranche.py <name> --start 3000 --count 40 [--langs en,de,uk] [--scan 4000]   (run from the backend repository root)
Writes headwords/<name>-<lang>.json.
"""
import argparse, json
from pathlib import Path

import spacy
from wordfreq import top_n_list, zipf_frequency

HERE = Path("scripts/lexicon_research")
MODELS = {"en": "en_core_web_sm", "de": "de_core_news_sm", "uk": "uk_core_news_sm", "fr": "fr_core_news_sm", "es": "es_core_news_sm", "it": "it_core_news_sm", "pt": "pt_core_news_sm"}
KEEP = {"NOUN", "VERB", "ADJ", "ADV"}


def taken(lang):
    """Words with an entry from any model, or already assigned to a headword file."""
    words = {f.stem.lower() for d in (HERE / "out").glob(f"*/{lang}") for f in d.glob("*.json") if not f.name.endswith(".meta.json")}
    for f in (HERE / "headwords").glob(f"*{lang}.json"):
        words |= {r["headword"].lower() for r in json.loads(f.read_text())}
    return words


def lemma_ok(nlp, lang, w):
    if not w.isalpha() or len(w) < 3:
        return None
    if lang == "uk" and zipf_frequency(w, "ru") > zipf_frequency(w, "uk") + 0.3:
        return None
    if lang != "en" and zipf_frequency(w, "en") > zipf_frequency(w, lang) + 1.0:
        return None  # an English word that drifted into the list ("out", "real", "mike"); loanwords both languages use stay
    t = nlp(w)[0]
    if t.ent_type_:
        return None
    if lang == "en" and (nlp(w.title())[0].pos_ == "PROPN" or nlp(w.title())[0].ent_type_):
        return None  # wordfreq is lowercase, so a name hides from the entity check until it is capitalised
    if lang == "de":
        if t.pos_ in {"VERB", "AUX", "ADJ", "ADV"} and t.lemma_.lower() != w:
            return None
        tt = nlp(w.title())[0]
        if tt.pos_ == "NOUN" and tt.lemma_ == w.title() and t.pos_ not in {"VERB", "ADJ", "ADV"}:
            return w.title()
    return w if t.pos_ in KEEP and t.lemma_.lower() == w else None


ap = argparse.ArgumentParser()
ap.add_argument("name")
ap.add_argument("--start", type=int, default=3000)
ap.add_argument("--count", type=int, default=40)
ap.add_argument("--langs", default="en,de,uk")
ap.add_argument("--scan", type=int, default=4000, help="how many wordfreq ranks past --start to look through")
a = ap.parse_args()
for lang in a.langs.split(","):
    nlp, used, rows = spacy.load(MODELS[lang]), taken(lang), []
    for w in top_n_list(lang, a.start + a.scan)[a.start:]:
        h = lemma_ok(nlp, lang, w)
        if h and h.lower() not in used:
            rows.append({"headword": h, "zipf": round(zipf_frequency(w, lang), 2), "set": a.name})
            used.add(h.lower())
        if len(rows) == a.count:
            break
    (HERE / "headwords" / f"{a.name}-{lang}.json").write_text(json.dumps(rows, ensure_ascii=False, indent=1) + "\n")
    print(lang, len(rows), [r["headword"] for r in rows[:10]], "...")
