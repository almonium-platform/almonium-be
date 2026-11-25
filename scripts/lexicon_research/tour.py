"""The shortest list of real entries that between them show everything the shape can say.

For checking the product by hand: type each word into the app and see whether the reader gets what
the entry holds. Each feature of schema 4 (a second pronunciation, a spelling of one variety, a
form used only after numbers, a label on each axis, every kind of relation, ...) is looked for in
the written entries, as ingest would publish them, and the fewest entries that cover all of them are
chosen, a common English word before a rare one or one of another language.

Output: docs/LEXICON_TOUR.md, a table of the word, its language, and what to look for, each with the
value the entry really holds. With --fixture PATH, also writes the chosen entries as a JSON array of
the exact published entries (valid against src/main/resources/lexicon/entry.schema.json).

Usage: tour.py [--langs en,ru,de,fr,es,it,pt,ja] [--extra FOLDER ...] [--fixture PATH]
(run from the backend repository root)
"""
import argparse, json, re, sys
from collections import defaultdict
from pathlib import Path

import jsonschema

sys.path.insert(0, str(Path(__file__).resolve().parent))
import ingest_v4  # noqa: E402
from prompting import HERE, LANG_NAME, PILOT_LANGUAGES  # noqa: E402

VALID = jsonschema.Draft202012Validator(json.loads((HERE / "entry.schema.v4.json").read_text()))

DOC = Path("docs/LEXICON_TOUR.md")
# What the shape can say and an entry might not happen to: named here so that the tour says when no entry shows it.
EXPECTED = {"a contraction with the words it stands for": "German *zum*", "a phrase other words can come between": "*give it up*",
            "a sense marked familiar": "*du*", "a sense marked polite": "*Sie*", "a form used after numbers": "*два часа́*",
            "a grammar value that holds mainly, not always": "*usually before a noun*", "a compound with its parts": "*Arbeitszimmer*",
            "a pronunciation that belongs to one spelling": "*período*, *periodo*",
            "a pronunciation with when it is said": "*the*, *bath*, *six*",
            "a weak form, without a stress mark": "*the* /ðə/, *we'll* /wɪl/",
            "two pronunciations in one variety, neither marked": "*direct* /dɪ.ˈɹɛkt/, /daɪ.ˈɹɛkt/",
            "the words a sense usually goes with": "*make a decision*", "a pattern whose open place has a case": "*warten auf + Akk*",
            "a pattern that opens a verb": "*afford to do sth*", "a pattern that opens a clause": "*aware that ...*",
            "the pitch accent of a Japanese word": "*橋* 2", "a noun that makes a verb with する": "*勉強*",
            "a use of the word as a suffix, a prefix or an auxiliary": "*目* in *二回目*"}
ORDER = ["en", "ru", "de", "fr", "es", "it", "pt", "ja"]  # the owner reads the first two natively; Japanese is on trial
RELATIONS = {"diminutive_of": "a diminutive", "augmentative_of": "an augmentative", "endearing_of": "an endearing form", "feminine_of": "a feminine",
             "agent_noun_of": "an agent noun", "verbal_noun_of": "a verbal noun", "abstract_noun_of": "an abstract noun", "adjective_of": "an adjective of a noun",
             "adverb_of": "an adverb of an adjective", "derived_from": "a derived word", "aspect_partner": "an aspect partner", "confusable": "a word it is confused with"}


AFFIX_NOTE = re.compile(r"\b(suffix|prefix|auxiliary)\b", re.I)
SCRIPTS = {"Hira": "hiragana", "Kana": "katakana", "Jpan": "kanji"}


def said(lexeme, variety=None):
    return [p.get("ipa") or p.get("reading") for p in lexeme.get("pronunciations") or [] if variety in (None, p.get("variety"))]


def features(lang, entry):
    """{feature: what this entry holds for it}. A feature's name says what the reader should be shown."""
    found = {}

    def note(name, value):
        found.setdefault(name, value)

    lexemes = entry.get("lexemes") or []
    if entry.get("reject") and not lexemes:
        text = entry["reject"]
        kind = "a headword that is an inflected form: the reader is sent to its word" if ingest_v4.FORM_OF.search(text) else \
               "a headword that is another word's spelling: the reader is sent to that word" if text.lower().startswith("spelling of") else \
               "a headword that is no word (an abbreviation, a name): nothing invented" if any(w in text.lower() for w in ("abbreviation", "proper", "name")) else None
        if kind:
            note(kind, text)
        return found
    for a in lexemes:
        for b in lexemes:
            if a is b:
                continue
            if a.get("pos") == b.get("pos") and said(a) and said(b) and set(said(a)).isdisjoint(said(b)):
                note("two words of one spelling, said differently", f"{said(a)[0]} and {said(b)[0]}")
            if a.get("pos") != b.get("pos"):
                note("one spelling, two parts of speech", f"{a.get('pos')} and {b.get('pos')}")
            ga, gb = (a.get("grammar") or {}).get("gender"), (b.get("grammar") or {}).get("gender")
            if a.get("pos") == b.get("pos") == "NOUN" and ga and gb and ga != gb:
                note("one spelling, two genders, two words", f"{ga} and {gb}")
            if a.get("lemma") != b.get("lemma") and (a.get("lemma") or "").lower() == (b.get("lemma") or "").lower():
                note("a capital letter makes it another word", f"{a.get('lemma')} and {b.get('lemma')}")
    for lexeme in lexemes:
        grammar = lexeme.get("grammar") or {}
        by_variety = defaultdict(list)
        for p in lexeme.get("pronunciations") or []:
            by_variety[p.get("variety")].append(p)
            if p.get("note"):
                note("a second pronunciation, with a note on who says it", f"{p.get('ipa') or p.get('reading')} ({p['note']})")
            if p.get("context"):
                note("a pronunciation with when it is said", f"{p.get('ipa') or p.get('reading')}: {p['context']}")
                if p.get("ipa") and "ˈ" not in p["ipa"] and "weak" in p["context"].lower():
                    note("a weak form, without a stress mark", f"{p['ipa']} ({p['context']})")
            if p.get("reading") and lang in ("ru", "uk"):
                note("the stress marked on the word", p["reading"])
            if p.get("notation") == "kana":
                note("a Japanese word given as a reading in kana, not a transcription", f"{lexeme.get('lemma')}: {p.get('reading')}")
                if "accent" in p:
                    note("the pitch accent of a Japanese word", f"{p.get('reading')} {p['accent']}")
            if p.get("reading") and lang == "it":
                note("the stress and the open or close vowel marked on the word", p["reading"])
            if p.get("spelling"):
                note("a pronunciation that belongs to one spelling", f"{p.get('ipa')} for {p['spelling']}")
        for variety, ways in by_variety.items():
            plain_ways = [w for w in ways if not w.get("note") and not w.get("context") and not w.get("spelling")]
            if len(plain_ways) > 1:
                note("two ways of saying it in one variety, neither marked", f"{variety} " + ", ".join(w.get("ipa") or w.get("reading") or "" for w in plain_ways))
        firsts = {v: ps[0].get("ipa") for v, ps in by_variety.items() if ps[0].get("ipa")}
        if len(set(firsts.values())) > 1:
            note("said differently in two varieties", ", ".join(f"{v} {ipa}" for v, ipa in firsts.items()))
        for variety, readings in by_variety.items():
            unmarked = [p["ipa"] for p in readings if p.get("ipa") and not p.get("note") and not p.get("context") and not p.get("spelling")]
            if len(set(unmarked)) > 1:
                note("two pronunciations in one variety, neither marked", f"{variety} " + ", ".join(dict.fromkeys(unmarked)))
        spellings = lexeme.get("spellings") or []
        if any(s.get("pre_reform") for s in spellings):
            note("a spelling only older books use", ", ".join(s["text"] for s in spellings if s.get("pre_reform")))
        if len({tuple(s.get("varieties") or ()) for s in spellings if s.get("varieties")}) > 1:
            note("a spelling for each variety", "; ".join(f"{s['text']} ({', '.join(s['varieties'])})" for s in spellings if s.get("varieties")))
        if len({s.get("script") for s in spellings if s.get("script")}) > 1:
            note("one word written in two scripts", "; ".join(f"{s['text']} ({SCRIPTS.get(s['script'], s['script'])})" for s in spellings if s.get("script")))
        if len(by_variety) == 1 and len({p.get("reading") for p in lexeme.get("pronunciations") or [] if p.get("notation") == "kana"}) > 1:
            note("two readings of one Japanese word, one meaning", ", ".join(p["reading"] for p in lexeme["pronunciations"] if p.get("reading")))
        if len([s for s in spellings if not s.get("varieties") and not s.get("pre_reform")]) > 1:
            note("two spellings of one word, used everywhere", ", ".join(s["text"] for s in spellings if not s.get("varieties") and not s.get("pre_reform")))
        if lexeme.get("kind") == "multiword":
            note("a phrase with its parts", " + ".join(f"{c.get('text')} ({c.get('inflects')})" for c in lexeme.get("components") or []))
            if lexeme.get("separable"):
                note("a phrase other words can come between", lexeme.get("lemma"))
        if lexeme.get("kind") == "contraction":
            note("a contraction with the words it stands for", " + ".join(c.get("lemma", "") for c in lexeme.get("components") or []))
        if lexeme.get("kind") == "word" and lexeme.get("components"):
            note("a compound with its parts", " + ".join(c.get("lemma", "") for c in lexeme["components"]))
        for form in lexeme.get("forms") or []:
            cells = form.get("features") or {}
            note("an irregular form", f"{form.get('form')} ({', '.join(f'{k}={v}' for k, v in cells.items())})")
            if form.get("varieties"):
                note("a form used in one variety", f"{form.get('form')} ({', '.join(form['varieties'])})")
            if form.get("sense_ids") or form.get("senses"):
                note("a form that belongs to one sense", form.get("form"))
            if form.get("note"):
                note("a form with a note on when it is used", f"{form.get('form')}: {form['note']}")
            if form.get("reading") and lang in ("ru", "uk"):
                note("a form with its own stress", form["reading"])
            if cells.get("Number") == "Count":
                note("a form used after numbers", form.get("form"))
            if cells.get("Case") == "Loc" and lang == "ru":
                note("a second locative", form.get("reading") or form.get("form"))
            if cells.get("Case") == "Par":
                note("a partitive", form.get("reading") or form.get("form"))
        if grammar.get("gender") == "Com":
            note("a noun of either gender", lexeme.get("lemma"))
        if grammar.get("number") == "Plur":
            note("a noun used only in the plural", lexeme.get("lemma"))
        if "¨" in str(grammar.get("plural_class", "")):
            note("a German plural with an umlaut", grammar["plural_class"])
        if grammar.get("by_variety"):
            note("grammar that differs in one variety", json.dumps(grammar["by_variety"], ensure_ascii=False))
        if grammar.get("h_aspire"):
            note("a French h that blocks elision", lexeme.get("lemma"))
        if grammar.get("separable"):
            note("a separable verb", f"{lexeme.get('lemma')} ({grammar.get('separable_prefix', '')})")
        if grammar.get("auxiliary") == "both":
            note("a verb with both auxiliaries", lexeme.get("lemma"))
        if grammar.get("invariable"):
            note("a word that never changes its form", lexeme.get("lemma"))
        if grammar.get("suru") or any((sn.get("grammar") or {}).get("suru") for sn in lexeme.get("senses") or []):
            note("a noun that makes a verb with する", lexeme.get("lemma"))
        if grammar.get("classifier"):
            note("the counter a Japanese noun is counted with", f"{lexeme.get('lemma')}: {grammar['classifier']}")
        if lang == "ja" and grammar.get("conjugation"):
            note("the class of a Japanese verb or adjective", f"{lexeme.get('lemma')}: {grammar['conjugation']}")
        if lang == "ja" and AFFIX_NOTE.search(str(grammar.get("note") or "")):
            note("a use of the word as a suffix, a prefix or an auxiliary", f"{lexeme.get('lemma')}: {grammar['note']}")
        if grammar.get("note"):
            note("a note on the word's grammar", grammar["note"])
        for key in grammar.get("mainly") or []:
            note("a grammar value that holds mainly, not always", f"{key} {grammar.get(key)}")
        counts = set()
        for sense in lexeme.get("senses") or []:
            own = sense.get("grammar") or {}
            labels = sense.get("labels") or {}
            if sense.get("frames"):
                note("the patterns a sense is used in", "; ".join(sense["frames"]))
            if sense.get("collocations"):
                note("the words a sense usually goes with", "; ".join(sense["collocations"]))
                if not sense.get("frames"):
                    note("a sense with company and no pattern", f"{lexeme.get('lemma')}: " + "; ".join(sense["collocations"][:4]))
                if len(sense["collocations"]) >= 6:
                    note("a sense with six collocations or more, the less guessable first", "; ".join(sense["collocations"]))
            for pattern in sense.get("patterns") or []:  # what a program is given to find the pattern in a text
                slots = [part for part in pattern["parts"] if "slot" in part]
                shape = " | ".join("head: " + part["head"] if "head" in part else "word: " + part["word"] if "word" in part else
                                   "slot: " + "/".join(part["slot"]) + "".join(f", {k} {'/'.join(v) if isinstance(v, list) else v}"
                                                                              for k, v in part.items() if k != "slot") for part in pattern["parts"])
                note("a pattern read into its parts for a program", f"{pattern['frame']} = {shape}")
                if any(part.get("case") for part in slots):
                    note("a pattern whose open place has a case", f"{pattern['frame']} = {shape}")
                if any("verb" in part["slot"] for part in slots):
                    note("a pattern that opens a verb", f"{pattern['frame']} = {shape}")
                if any("clause" in part["slot"] for part in slots):
                    note("a pattern that opens a clause", f"{pattern['frame']} = {shape}")
                if any(set(part["slot"]) >= {"person", "thing"} for part in slots):
                    note("an open place for a person or a thing", f"{pattern['frame']} = {shape}")
            if lang == "ja" and AFFIX_NOTE.search(str(own.get("note") or "")):
                note("a use of the word as a suffix, a prefix or an auxiliary", f"{lexeme.get('lemma')}: {sense.get('definition')} ({own['note']})")
            if sense.get("spellings"):
                note("a spelling that belongs to one sense", f"{', '.join(sense['spellings'])}: {sense.get('definition')}")
            if own.get("countability"):
                counts.add(own["countability"])
            if own.get("valency") == "Refl":
                note("a reflexive sense of a verb", sense.get("definition"))
            if own.get("position") or grammar.get("position"):
                note("a word or sense used only joined to a noun, or only after a verb like be", f"{own.get('position') or grammar.get('position')}: {sense.get('definition')}")
            if own.get("number") == "Plur":
                note("a sense the word has only in the plural", sense.get("definition"))
            if own.get("note"):
                note("a note on one sense's grammar", own["note"])
            for key in own.get("mainly") or []:
                note("a grammar value that holds mainly, not always", f"{key} {own.get(key)}: {sense.get('definition')}")
            for axis in ("formality", "time", "normativity", "politeness"):
                if labels.get(axis):
                    note(f"a sense marked {labels[axis]}", sense.get("definition"))
            for axis in ("attitude", "style"):
                for value in labels.get(axis) or []:
                    note(f"a sense marked {value}", sense.get("definition"))
            if labels.get("domain"):
                note("a sense of a special field", f"{labels['domain']}: {sense.get('definition')}")
            if labels.get("figurative"):
                note("a figurative sense", sense.get("definition"))
            if labels.get("rare"):
                note("a rare sense", sense.get("definition"))
            if sense.get("standard_alternative"):
                note("the standard word given for a proscribed one", sense["standard_alternative"])
            for region in sense.get("regions") or []:
                if isinstance(region, dict):
                    area = region.get("variety", "").split("-")[-1].isdigit()
                    note(f"a sense used {region.get('extent')} in one part of the language" + (", a whole area" if area else ""),
                         f"{region.get('variety')}: {sense.get('definition')}")
                else:
                    note("a sense used only in one part of the language", f"{region}: {sense.get('definition')}")
            if sense.get("antonyms"):
                note("an opposite", ", ".join(a.get("lemma", "") for a in sense["antonyms"]))
        if len(counts) > 1:
            note("countable in one sense, uncountable in another", lexeme.get("lemma"))
        for relation in lexeme.get("relations") or []:
            if relation.get("senses"):
                note("a link that belongs to some senses only", f"{relation.get('type')} {relation.get('lemma')}: senses {relation['senses']}")
            if relation.get("type") in RELATIONS:
                note(f"a link to the word it comes from or is mixed up with: {RELATIONS[relation['type']]}",
                     f"{relation.get('lemma')}" + (f" ({relation['why']})" if relation.get("why") else ""))
    return found


def load_extra(folder, lang):
    """headword -> (entry, meta) from a trial or review folder outside out/*-v4/."""
    found = {}
    root = Path(folder)
    d = (root if root.is_dir() else ingest_v4.OUT / folder) / lang
    if not d.is_dir():
        return found
    for f in sorted(d.glob("*.json")):
        if f.name.endswith(".meta.json"):
            continue
        try:
            entry = json.loads(f.read_text(encoding="utf-8"))
        except json.JSONDecodeError:
            continue
        if not isinstance(entry, dict) or "error" in entry or "headword" not in entry:
            continue
        meta_f = f.with_name(f.stem + ".meta.json")
        meta = json.loads(meta_f.read_text(encoding="utf-8")) if meta_f.exists() else {}
        found[f.stem] = (entry, {**meta, "writer": d.parent.name})
    return found


def select(langs, extra=()):
    """(chosen, everything, clean_entries): greedily, the entries that cover the most unseen features."""
    pool = []
    for lang in langs:
        zipf = ingest_v4.zipf_of(lang)
        raw_entries = ingest_v4.load_raw(lang)
        for folder in extra:
            for headword, pair in load_extra(folder, lang).items():
                raw_entries.setdefault(headword, pair)
        for headword, (raw, meta) in raw_entries.items():
            if not VALID.is_valid(raw):
                continue  # an entry that breaks the shape shows nothing of it
            clean = ingest_v4.clean_entry(lang, raw, meta)
            got = features(lang, clean)
            if got and headword.replace(" ", "").replace("'", "").replace("-", "").isalpha():
                pool.append((lang, headword, zipf.get(headword.lower(), 0), got, clean))
    everything = set().union(*(set(got) for _, _, _, got, _ in pool)) if pool else set()
    left, chosen, entries = set(everything), [], []
    # a language on trial shows only what no target language can: nobody should have to judge a Japanese entry for
    # what an English or a Russian one shows as well
    for stage in ([c for c in pool if c[0] not in PILOT_LANGUAGES], pool):
        while any(left & set(candidate[3]) for candidate in stage):
            def worth(candidate):
                lang, headword, z, got, _ = candidate
                new = len(left & set(got))
                common = z >= 4.0  # a word the owner knows is easier to judge than a rare one
                return (new if common else new - 1.5, -ORDER.index(lang) if lang in ORDER else -9, z)
            best = max((candidate for candidate in stage if left & set(candidate[3])), key=worth)
            gain = left & set(best[3])
            chosen.append((best[0], best[1], best[2], {f: best[3][f] for f in sorted(gain)}))
            entries.append(best[4])
            left -= gain
    return chosen, everything, entries


def cover(langs, extra=()):
    """[(lang, headword, zipf, {feature: evidence})]: greedily, the entry that shows the most not yet shown."""
    chosen, everything, _ = select(langs, extra=extra)
    return chosen, everything


def absent(everything):
    """A paragraph naming what the shape can say that no written entry shows yet, or nothing."""
    missing = [f"{name} ({example})" for name, example in EXPECTED.items() if name not in everything]
    return [f"No written entry shows these yet, so no word here does: {'; '.join(missing)}.", ""] if missing else []


def write(langs, fixture=None, extra=()):
    chosen, everything, entries = select(langs, extra=extra)
    lines = ["# A tour of the shape in real entries", "",
             f"{len(chosen)} written entries that between them show all {len(everything)} things an entry can say, picked by",
             "`scripts/lexicon_research/tour.py` from what the writers wrote: the fewest words, a common English one first.",
             "Type each into the app and check that the reader is shown what the right-hand column says.", "",
             "The entries are shown as ingest publishes them: a pattern with a choice of words split in two, a pattern without",
             "an open place among the collocations, and each pattern also read into its parts. Entries written before prompt",
             "v4.8 have few collocations; the rows that show them are from v4.8 onwards, and pronunciation contexts and weak",
             "forms are from v4.11.", "",
             "The entries are schema 4, which the backend reads since 18 November 2026 (`docs/LEXICON.md`, \"The backend reads",
             "schema 4\"). `GET /public/lexicon/{language}/entries` returns everything in the right-hand column, and this table",
             "is the UI contract of what real entries can show across the web and mobile clients.", "",
             *absent(everything),
             "| # | Word | Language | What the reader should see |", "|---|---|---|---|"]
    for n, (lang, headword, z, got) in enumerate(chosen, 1):
        shown = "<br>".join(f"**{feature}**: {str(value)[:140]}" for feature, value in got.items())
        lines.append(f"| {n} | {headword} | {LANG_NAME.get(lang, lang)} | {shown} |")
    DOC.write_text("\n".join(lines) + "\n", encoding="utf-8")
    if fixture is not None:
        Path(fixture).write_text(json.dumps(entries, ensure_ascii=False, indent=1) + "\n", encoding="utf-8")
    return chosen, everything


if __name__ == "__main__":
    ap = argparse.ArgumentParser()
    ap.add_argument("--langs", default=",".join(ORDER))
    ap.add_argument("--extra", nargs="*", default=[], help="extra folders under out/ (or paths) to include alongside out/*-v4/")
    ap.add_argument("--fixture", default=None, help="also write the chosen published entries as a JSON array to this path")
    a = ap.parse_args()
    chosen, everything = write(a.langs.split(","), fixture=a.fixture, extra=a.extra)
    print(f"{len(chosen)} entries show {len(everything)} features -> {DOC}" + (f" (+ {a.fixture})" if a.fixture else ""))
    for lang, headword, z, got in chosen:
        print(f"  {lang} {headword} ({z:.1f}): {len(got)} new")
