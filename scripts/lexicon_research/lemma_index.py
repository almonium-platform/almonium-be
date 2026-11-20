"""Wiktionary's verdict on every spelling of a language, for the gate before a paid write.

Streams kaikki.org's per-language extract of the English Wiktionary (wiktextract JSONL) and keeps
one line per spelling in wiktionary/<lang>.tsv: the spelling, what its senses are, and the lemmas
its inflected senses point at. Nothing else of the dump is kept. The data is CC BY-SA; it is used
here only to decide whether a headword is worth a call, never published, and the index is not
committed.

A sense is one of
  L  a sense of its own: the spelling is a lemma (also every proper-name sense). A word built
     from another is a lemma too, although Wiktionary files it as "form of": a diminutive
     (хлопчик of хлопець), a verbal noun (виготовлення), a female equivalent (Lehrerin)
  F  "form of X": an inflected form; X is kept as the target lemma. Also a form that only the lemma's own
     inflection table gives (Russian "российской" under "российский")
  A  an alternative, obsolete or regional spelling, an acronym or a clipping (French "sida",
     "info"): a word a learner meets, written under its own spelling until entries carry spellings
  X  an abbreviation or misspelling of another word ("млрд", "нажаль")
  N  a sense of its own that is the name of a person, a place below a country, or a firm (Bremen, Renate, Google);
     a month, a feast or a country is an L like any word (October, Christmas, Germany)
  R  beside L: every sense of its own is marked old, rare, regional or slang (French "robert")
  M  beside L: its first sense is the name of a person or a place, and the word comes after ("David", "London")
  P  a prefix, a suffix or another piece of a word, and S, a letter or a sign: not words on their own
  C  a contraction of two words (German "zum", French "au", English "can't"): written, as kind "contraction"
A Japanese word written in kana that Wiktionary only sends on to its spelling with kanji (たべる to 食べる) is an A.
and a spelling's flags are the union over every entry and part of speech it has ("FL" for
English "parking": a noun of its own and the participle of park; "LN" for German "Müller": a miller
and a surname). A fourth column keeps the parts of speech of its senses of its own (noun,verb), for
whoever builds a list of words to write.

The same pass keeps wiktionary/<lang>.facts.tsv: for every lemma the genders, the transcriptions (with their
region) and the plurals Wiktionary gives it. check_v4.py holds an entry's gender and the syllable its
transcription stresses against them.

For Ukrainian and Russian, which do not write stress, the same pass keeps wiktionary/<lang>.stress.tsv:
every stressed form Wiktionary prints (та́бору, таборі́в) under its plain spelling. check_v4.py holds the
readings the writer gave against it; it is a witness, never copied into an entry.

Usage: lemma_index.py [--langs en,de,fr,es,it,pt,ru,uk] [--from FOLDER]   (run from the backend repository root)
       About 10 GB across the eight languages, streamed; English alone is 3.4 GB.
"""
import argparse, io, json, re, sys, unicodedata, urllib.request
from collections import defaultdict
from pathlib import Path

HERE = Path("scripts/lexicon_research")
INDEX = HERE / "wiktionary"
KAIKKI = {"en": "English", "de": "German", "fr": "French", "es": "Spanish", "it": "Italian", "pt": "Portuguese", "uk": "Ukrainian", "ru": "Russian",
          "ja": "Japanese"}
URL = "https://kaikki.org/dictionary/{name}/kaikki.org-dictionary-{name}.jsonl"
DERIVED_TAGS = {"diminutive", "augmentative", "endearing", "pejorative", "noun-from-verb", "nominalization", "frequentative"}
DERIVED_GLOSSES = ("female equivalent of", "male equivalent of")
REJECTED_ALT = {"abbreviation", "misspelling"}
KEPT_ALT = {"acronym", "initialism", "clipping"}
STRESS_WITNESS = {"uk", "ru"}
# What kind of entry a lemma sense comes from, where that is not a word of the usual kind: a name of a person or a
# place (Bremen, Renate), a piece of a word (-heit, un-), a letter or a sign. A frequency list is full of the first.
NAME_POS = {"name"}
# Not every proper name is one a list should leave out: a learner needs October, Christmas and Germany and does not
# need Renate, Leverkusen or Google. A name sense is a person, a place below a country or a firm when its gloss says so.
COUNTRY = re.compile(r"\b(?:country|continent|kingdom|republic|nation|empire)\b", re.I)
PERSON_OR_PLACE = re.compile(
    r"\b(?:given name|surname|nickname|patronymic|diminutive of the (?:male|female)|city|town|village|municipality|commune|suburb|district|"
    r"borough|county|province|prefecture|department|oblast|region|river|lake|mountain|island|capital|neighbou?rhood|state|canton|"
    r"constituency|parish|hamlet|locality|settlement|township|street|airport|community|territory|peninsula|"
    r"company|corporation|brand|website|platform|encyclop(?:a)?edia|operating system|software|search engine|social network|"
    r"football club|team|university|newspaper|"
    r"character|personage|king|queen|emperor|prince|princess|saint|apostles?|archangel|prophet|disciple|evangelist|biblical|"
    r"testament|epistle|gospel|god|goddess|deity|mytholog\w*|hero|trojan)\b", re.I)
# A lemma's entry lists its inflected forms in a table. Wiktionary gives the frequent ones entries of their own and
# not the rest (Russian "российской", German "fertiggestellten"), so the table is read too: only what is marked as a
# case, number, person, tense or degree, and nothing that is another word (a diminutive, the adjective of a noun).
INFLECTED = {"nominative", "genitive", "dative", "accusative", "instrumental", "prepositional", "locative", "vocative", "partitive",
             "singular", "plural", "dual", "present", "past", "future", "preterite", "imperfect", "imperative", "subjunctive",
             "conditional", "participle", "gerund", "first-person", "second-person", "third-person", "masculine", "feminine",
             "neuter", "comparative", "superlative", "short-form", "indicative", "definite", "indefinite"}
NOT_A_FORM = {"romanization", "table-tags", "inflection-template", "class", "canonical", "alternative", "diminutive", "augmentative",
              "endearing", "pejorative", "adjective", "relational", "abbreviation", "auxiliary", "obsolete", "archaic", "dated", "rare",
              "noun-from-verb", "agent", "multiword-construction"}
AFFIX_POS = {"prefix", "suffix", "infix", "interfix", "circumfix", "affix", "combining_form"}
SIGN_POS = {"character", "symbol", "letter", "punct", "syllable"}
ACUTE = "\u0301"


# A form Wiktionary describes in words without marking it: Italian "possiamo" ("first-person plural present
# indicative of potere"), "farlo" ("compound of the infinitive fare with lo"), Portuguese "fizeram". The base word is
# the one after "of", or before "with".
FORM_GLOSS = re.compile(r"^(?:(?:first|second|third)[-/ ](?:and third-)?person\b.* of |(?:feminine|masculine|neuter)(?: singular| plural)? of |plural of |"
                        r"(?:past|present) participle of |inflection of |"
                        r"compound of (?:the )?(?:[a-z-]+ )*?(?:infinitive|imperative|gerund|participle|person)\b)", re.I)
FORM_AIM = re.compile(r"(?:compound of (?:the )?(?:[a-z-]+ )*?([^\s,;:()]+) with\b|\bof ([^\s,;:()]+)[.:;]?(?: \(.*\))?$)")
# A sense marked as outside today's ordinary language. A spelling all of whose own senses are so marked owes its
# frequency to something else: a name written the same, a form, another language.
MARGINAL = {"obsolete", "archaic", "rare", "slang", "vulgar"}  # not "dialectal" or "regional": those often mark one variant of a sense


def gloss_form(sense):
    """The base word of a sense that is a form by its wording only, or None."""
    gloss = ((sense.get("glosses") or [""])[0] or "").strip()
    if not FORM_GLOSS.match(gloss):
        return None
    found = FORM_AIM.search(gloss)  # no base word named, no form: "third-person feminine pronoun; she" is a word
    return plain(next(g for g in found.groups() if g)) if found else None


def sense_kind(sense):
    """L, F, A or X for one sense; None for an empty placeholder sense that says nothing."""
    tags = set(sense.get("tags") or [])
    gloss = ((sense.get("glosses") or [""])[0] or "").lower()
    if sense.get("form_of") or "form-of" in tags or ("compound-of" in tags and gloss_form(sense) is not None) or (
            not sense.get("alt_of") and "alt-of" not in tags and not tags & DERIVED_TAGS and gloss_form(sense) is not None):
        if tags & DERIVED_TAGS or gloss.startswith(DERIVED_GLOSSES):
            return "L"
        return "F"
    if "contraction" in tags:
        return "C"
    if sense.get("alt_of") or "alt-of" in tags:
        return "X" if tags & REJECTED_ALT and not tags & KEPT_ALT else "A"
    if "no-gloss" in tags and not sense.get("glosses"):
        return None
    return "L"


def plain(word):
    """The spelling without the stress marks Wiktionary writes in Cyrillic lemmas (каза́ти -> казати).
    After NFC a Latin vowel with an accent is one character, so only marks no letter absorbs are dropped:
    Spanish "está" and Portuguese "avó" keep theirs."""
    return "".join(c for c in unicodedata.normalize("NFC", word) if c not in "̀́")


def targets(sense):
    named = [plain(t["word"]) for t in sense.get("form_of") or [] if t.get("word")]
    return named or [w for w in [gloss_form(sense)] if w]


def stressed_forms(record):
    """Every form of a record that carries a stress mark and is written in the record's own script."""
    for form in record.get("forms") or []:
        text = unicodedata.normalize("NFC", form.get("form") or "")
        if ACUTE in text and "romanization" not in (form.get("tags") or []) and " " not in text:
            yield text


def table_forms(record):
    """The inflected forms a lemma's own table gives, as plain spellings: one word each, and not the lemma itself."""
    word = record.get("word")
    for form in record.get("forms") or []:
        tags = set(form.get("tags") or [])
        text = plain(unicodedata.normalize("NFC", form.get("form") or ""))
        if tags & INFLECTED and not tags & NOT_A_FORM and text and text != word and " " not in text and text != "-":
            yield text


GENDERS = {"masculine": "Masc", "feminine": "Fem", "neuter": "Neut", "common-gender": "Com"}
STANDARD_TAGS = {"standard", "also", "phoneme", "traditional", "formal", "usually", "often", "stressed", "letter", "name"}


def facts_of(record):
    """What a lemma's entry states that an entry of ours states too: (genders, transcriptions, plurals).
    A transcription keeps the region Wiktionary names for it ("Portugal:/ɡɐɾˈɡɐ̃.tɐ/"), and a "?" before it when
    Wiktionary sets it apart from the standard. A witness, like the
    stressed forms: held against what a writer wrote, never copied into an entry."""
    tags = set()
    for sense in record.get("senses") or []:
        if not sense.get("form_of") and "form-of" not in (sense.get("tags") or []):
            tags |= set(sense.get("tags") or [])
    for form in record.get("forms") or []:
        if "canonical" in (form.get("tags") or []):
            tags |= set(form.get("tags") or [])
    genders = sorted({GENDERS[t] for t in tags if t in GENDERS}) if record.get("pos") == "noun" else []
    sounds = []
    for sound in record.get("sounds") or []:
        if sound.get("ipa"):
            tags = sound.get("tags") or []
            region = "+".join(t for t in tags if t[:1].isupper())
            # "?" before one Wiktionary sets apart from the standard: a note ("Southern US", "weak form") or a tag such as dialectal
            aside = bool(sound.get("note")) or any(not t[:1].isupper() and t not in STANDARD_TAGS for t in tags)
            sounds.append(f"{'?' if aside else ''}{region}:{sound['ipa']}")
    plurals = sorted({plain(f["form"]) for f in record.get("forms") or [] if set(f.get("tags") or []) == {"plural"} and f.get("form")})
    return genders, list(dict.fromkeys(sounds))[:60], plurals[:4]


def classify(lines, stress=None, facts=None):
    """Spelling -> (flags, form-of targets) over every record of a JSONL stream. When a dict is given
    as stress, it is filled with plain spelling -> the stressed forms seen for it; when one is given as
    facts, with spelling -> [genders, transcriptions, plurals] of its lemma entries."""
    flags, aims, poses, usual, first = defaultdict(set), defaultdict(list), defaultdict(set), set(), {}
    for line in lines:
        try:
            record = json.loads(line)
        except json.JSONDecodeError:
            continue
        word = record.get("word")
        if not word:
            continue
        if stress is not None:
            for text in stressed_forms(record):
                stress.setdefault(plain(text), set()).add(text)
        if record.get("pos") == "soft-redirect":
            flags[word].add("A")  # Japanese in kana (たべる), which Wiktionary sends on to the spelling with kanji (食べる)
        own = False
        for sense in record.get("senses") or []:
            kind = sense_kind(sense)
            if kind is None:
                continue
            if kind == "L":
                pos = record.get("pos") or ""
                gloss = ((sense.get("glosses") or [""])[0] or "")[:160]
                place, country = PERSON_OR_PLACE.search(gloss), COUNTRY.search(gloss)
                # whichever the gloss says first: "The capital city of the United Kingdom" is a city, "a country ...; capital: ..." a country
                named = pos in NAME_POS and place and not (country and country.start() < place.start())
                kind = "N" if named else "P" if pos in AFFIX_POS else "S" if pos in SIGN_POS else "L"
                poses[word].add(pos)
                own = own or kind in "LN"
                if kind in "LN":
                    first.setdefault(word, kind)
                if kind == "L" and not set(sense.get("tags") or []) & MARGINAL:
                    usual.add(word)
            flags[word].add(kind)
            if kind == "F":
                for t in targets(sense):
                    if t not in aims[word]:
                        aims[word].append(t)
        if own and facts is not None:
            genders, sounds, plurals = facts_of(record)
            have = facts.setdefault(word, [[], [], []])
            for kept, more in zip(have, (genders, sounds, plurals)):
                kept.extend(x for x in more if x not in kept)
        if own:
            for text in table_forms(record):
                flags[text].add("F")
                if word not in aims[text]:
                    aims[text].append(word)
    for word, found in flags.items():
        if "L" in found and word not in usual:
            found.add("R")  # every sense of its own is old, rare, regional or slang
        if "L" in found and first.get(word) == "N":
            found.add("M")  # the first thing Wiktionary says of it is a person or a place: David the name before a David, an underdog
    return {w: ("".join(sorted(f)), aims.get(w, []), sorted(poses.get(w, ()))) for w, f in flags.items()}


def write_index(lang, verdicts):
    INDEX.mkdir(parents=True, exist_ok=True)
    out = INDEX / f"{lang}.tsv"
    tmp = out.with_suffix(".tsv.part")
    with tmp.open("w", encoding="utf-8") as f:
        for word in sorted(verdicts):
            fl, aims, poses = verdicts[word]
            clean = [a for a in aims if "\t" not in a and "|" not in a][:8]
            f.write(f"{word}\t{fl}\t{'|'.join(clean)}\t{','.join(poses)}\n")
    tmp.replace(out)  # a run cut off halfway leaves the old index, never half a new one
    return out


def write_stress(lang, stress):
    out = INDEX / f"{lang}.stress.tsv"
    tmp = out.with_suffix(".tsv.part")
    with tmp.open("w", encoding="utf-8") as f:
        for word in sorted(stress):
            f.write(f"{word}\t{'|'.join(sorted(stress[word]))}\n")
    tmp.replace(out)
    return out


def write_facts(lang, facts):
    out = INDEX / f"{lang}.facts.tsv"
    tmp = out.with_suffix(".tsv.part")
    with tmp.open("w", encoding="utf-8") as f:
        for word in sorted(facts):
            genders, sounds, plurals = facts[word]
            if genders or sounds or plurals:
                f.write(f"{word}\t{','.join(genders)}\t{'|'.join(x.replace('|', '') for x in sounds)}\t{','.join(plurals)}\n")
    tmp.replace(out)
    return out


def build(lang, folder=None):
    stress, facts = ({} if lang in STRESS_WITNESS else None), {}
    if folder:  # an extract already on disk, as kaikki names it
        with (Path(folder) / f"{KAIKKI[lang]}.jsonl").open(encoding="utf-8") as lines:
            verdicts = classify(lines, stress, facts)
    else:
        req = urllib.request.Request(URL.format(name=KAIKKI[lang]), headers={"User-Agent": "almonium-lexicon-research/1.0"})
        with urllib.request.urlopen(req, timeout=120) as response:
            verdicts = classify(io.TextIOWrapper(response, encoding="utf-8"), stress, facts)
    print(f"{lang}: {len(facts)} lemmas with a gender, a transcription or a plural -> {write_facts(lang, facts)}", flush=True)
    out = write_index(lang, verdicts)
    if stress is not None:
        print(f"{lang}: {len(stress)} stressed spellings -> {write_stress(lang, stress)}", flush=True)
    counts = defaultdict(int)
    for fl, _, _ in verdicts.values():
        counts["lemma" if "L" in fl else "contraction" if "C" in fl else "name only" if "N" in fl else "form only" if "F" in fl
               else "spelling only" if "A" in fl else "piece or sign" if set(fl) & set("PS") else "abbreviation or misspelling"] += 1
    print(f"{lang}: {len(verdicts)} spellings -> {out} {dict(counts)}", flush=True)


if __name__ == "__main__":
    ap = argparse.ArgumentParser()
    ap.add_argument("--langs", default="en,de,fr,es,it,pt,ru,uk")
    ap.add_argument("--from", dest="folder", help="a folder that holds the extracts already (German.jsonl, ...), instead of streaming them")
    a = ap.parse_args()
    for lang in a.langs.split(","):
        if lang not in KAIKKI:
            sys.exit(f"no kaikki extract is named for {lang}")
        build(lang, a.folder)
