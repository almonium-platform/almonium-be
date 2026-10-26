"""Wiktionary as the evidence an entry is written from and checked against.

kaikki.org publishes every English-Wiktionary word as JSON lines: one record per part of speech and etymology, with
gender and inflection in its tags and forms, IPA in its sounds (tagged UK, US, Brazil...), and senses carrying
region, register and topic labels. That is most of what an entry states as fact. evidence() turns the records for one
headword into lexemes and numbered senses (2.3 is the third sense of the second lexeme), which the writer cites and
the checks compare against. Facts (IPA, gender, plural, forms, regions) are free to use; Wiktionary's definitions
are CC BY-SA, so they are shown to the writer as evidence and never copied into an entry.

Records are fetched once per word and kept under out/kaikki/<lang>/<word>.jsonl (empty when Wiktionary has none).
"""

import json
import re
import time
import unicodedata
import urllib.error
import urllib.parse
import urllib.request
from pathlib import Path

HERE = Path(__file__).resolve().parent
CACHE = HERE / "out" / "kaikki"
NAMES = {
    "en": "English",
    "de": "German",
    "uk": "Ukrainian",
    "fr": "French",
    "es": "Spanish",
    "it": "Italian",
    "pt": "Portuguese",
}
POS = {
    "noun": "NOUN",
    "verb": "VERB",
    "adj": "ADJ",
    "adv": "ADV",
    "prep": "ADP",
    "postp": "ADP",
    "conj": "CCONJ",
    "pron": "PRON",
    "det": "DET",
    "article": "DET",
    "num": "NUM",
    "intj": "INTJ",
    "particle": "PART",
    "phrase": "PHRASE",
    "prep_phrase": "PHRASE",
    "proverb": "PHRASE",
    "contraction": "PHRASE",
}
GENDERS = {"masculine": "Masc", "feminine": "Fem", "neuter": "Neut", "common": "Com"}
# Wiktionary's region labels as the entry's region codes: an ISO country, or a UN M.49 area.
REGIONS = {
    "US": "US",
    "American": "US",
    "General-American": "US",
    "UK": "GB",
    "British": "GB",
    "England": "GB",
    "Scotland": "GB",
    "Scottish": "GB",
    "Wales": "GB",
    "Northern-England": "GB",
    "Ireland": "IE",
    "Irish": "IE",
    "Canada": "CA",
    "Canadian": "CA",
    "Quebec": "CA",
    "Australia": "AU",
    "Australian": "AU",
    "New-Zealand": "NZ",
    "India": "IN",
    "Indian-English": "IN",
    "South-Africa": "ZA",
    "Singapore": "SG",
    "Philippines": "PH",
    "Spain": "ES",
    "Canary-Islands": "ES",
    "Mexico": "MX",
    "Argentina": "AR",
    "Uruguay": "UY",
    "Paraguay": "PY",
    "Chile": "CL",
    "Peru": "PE",
    "Bolivia": "BO",
    "Ecuador": "EC",
    "Colombia": "CO",
    "Venezuela": "VE",
    "Cuba": "CU",
    "Dominican-Republic": "DO",
    "Puerto-Rico": "PR",
    "Guatemala": "GT",
    "Honduras": "HN",
    "El-Salvador": "SV",
    "Nicaragua": "NI",
    "Costa-Rica": "CR",
    "Panama": "PA",
    "Equatorial-Guinea": "GQ",
    "Rioplatense": "AR",
    "Brazil": "BR",
    "Brazilian": "BR",
    "Portugal": "PT",
    "European-Portuguese": "PT",
    "Angola": "AO",
    "Mozambique": "MZ",
    "France": "FR",
    "Belgium": "BE",
    "Belgian": "BE",
    "Switzerland": "CH",
    "Swiss": "CH",
    "Austria": "AT",
    "Austrian": "AT",
    "Germany": "DE",
    "Southern-Germany": "DE",
    "Bavaria": "DE",
    "Northern-Germany": "DE",
    "Luxembourg": "LU",
    "Liechtenstein": "LI",
    "South-Tyrol": "IT",
    "Italy": "IT",
    "Ukraine": "UA",
    "Africa": "002",
    "Latin-America": "419",
    "Caribbean": "029",
    "South-America": "005",
    "Central-America": "013",
    "North-America": "021",
    "Europe": "150",
    "Andes": "005",
    "Louisiana": "US",
}
# Which of the varieties the writer transcribes a sound's tags name; a sound with none of these is the language's.
VARIETY_TAGS = {
    "en-GB": {"UK", "Received-Pronunciation", "British"},
    "en-US": {"US", "General-American"},
    "pt-BR": {"Brazil", "São-Paulo", "Rio-de-Janeiro", "Brazilian"},
    "pt-PT": {"Portugal", "European-Portuguese", "Lisbon"},
    "es-ES": {"Spain", "Castilian", "Madrid"},
    "es-MX": {"Latin-America", "Mexico"},
}
REGISTER_TAGS = {
    "informal",
    "colloquial",
    "slang",
    "vulgar",
    "offensive",
    "derogatory",
    "pejorative",
    "formal",
    "literary",
    "poetic",
    "archaic",
    "obsolete",
    "dated",
    "rare",
    "humorous",
    "euphemistic",
    "figuratively",
    "dialectal",
}
SKIP_SENSE = {"obsolete", "historical"}
SENSES_PER_LEXEME = 16
# Labels Wiktionary puts on a pronunciation, as the note an entry gives a reading besides the first.
SOUND_NOTES = {
    "newer": "newer",
    "traditional": "older",
    "dated": "older",
    "obsolete": "older",
    "rare": "less_common",
    "uncommon": "less_common",
    "nonstandard": "disputed",
    "proscribed": "disputed",
    "casual": "casual",
    "colloquial": "casual",
    "careful": "careful",
}
MAINLY = re.compile(r"\b(chiefly|mainly|mostly|especially|usually|primarily|often)\b", re.IGNORECASE)


def fetch(lang, word):
    """The kaikki records for a word, from the cache or the network; [] when Wiktionary has none."""
    path = CACHE / lang / f"{word}.jsonl"
    if not path.exists():
        path.parent.mkdir(parents=True, exist_ok=True)
        quote = urllib.parse.quote
        url = (
            f"https://kaikki.org/dictionary/{NAMES[lang]}/meaning/"
            f"{quote(word[0])}/{quote(word[:2])}/{quote(word)}.jsonl"
        )
        text = ""
        for attempt in range(3):
            try:
                with urllib.request.urlopen(url, timeout=30) as response:
                    text = response.read().decode("utf-8")
                break
            except urllib.error.HTTPError as error:
                if error.code == 404:
                    break
                time.sleep(2 * (attempt + 1))
            except (urllib.error.URLError, TimeoutError):
                time.sleep(2 * (attempt + 1))
        else:
            raise RuntimeError(f"kaikki unreachable for {lang}/{word}")
        path.write_text(text)
    return [json.loads(line) for line in path.read_text().splitlines() if line.strip()]


def evidence(lang, word):
    """The headword's lexemes as Wiktionary records them, each with numbered senses; proper names and lexemes
    holding only inflected-form senses ("plural of ...") are kept apart as form_of."""
    lexemes, form_of = [], []
    for record in fetch(lang, word):
        pos = POS.get(record.get("pos"))
        if pos is None or record.get("word") != word:
            continue
        senses = [sense for sense in record.get("senses", []) if not SKIP_SENSE & set(sense.get("tags", []))]
        own = [sense for sense in senses if not sense.get("form_of") and not sense.get("alt_of")]
        if not own:
            form_of += [gloss(sense) for sense in senses]
            continue
        canonical = [form for form in record.get("forms", []) if "canonical" in form.get("tags", [])]
        tags = [tag for sense in own for tag in sense.get("tags", [])] + [
            tag for form in canonical for tag in form.get("tags", [])
        ]
        lexemes.append(
            {
                "pos": pos,
                "etymology": record.get("etymology_number", 1),
                "gender": sorted({GENDERS[tag] for tag in tags if tag in GENDERS}),
                "stressed": next((form["form"] for form in canonical if form["form"] != word), None),
                "plural": sorted(
                    {
                        form["form"]
                        for form in record.get("forms", [])
                        if set(form.get("tags", [])) in ({"plural"}, {"nominative", "plural"})
                    }
                ),
                "forms": key_forms(record),
                "ipa": sounds(lang, record),
                "senses": [
                    {
                        "gloss": gloss(sense),
                        "regions": sorted({REGIONS[label] for label in labels(sense) if label in REGIONS}),
                        "mainly": bool(MAINLY.search(" ".join(sense.get("raw_glosses", [])))),
                        "register": sorted(REGISTER_TAGS & set(sense.get("tags", []))),
                        "topics": sense.get("topics", []),
                    }
                    for sense in own[:SENSES_PER_LEXEME]
                ],
            }
        )
    for number, lexeme in enumerate(lexemes, 1):
        lexeme["id"] = str(number)
        for index, sense in enumerate(lexeme["senses"], 1):
            sense["id"] = f"{number}.{index}"
    return {"lexemes": lexemes, "form_of": form_of[:6]}


def labels(sense):
    """The sense's tags and the labels its gloss opens with, "(Caribbean, Equatorial Guinea) bus", as tag names:
    kaikki does not turn every label into a tag."""
    found = set(sense.get("tags", []))
    raw = (sense.get("raw_glosses") or [""])[-1]
    if raw.startswith("("):
        found |= {label.strip().replace(" ", "-") for label in raw[1 : raw.find(")")].split(",")}
    return found


def gloss(sense):
    """The sense's own label-prefixed gloss, short; a subsense's list starts with its parent's, so the last one."""
    text = (sense.get("raw_glosses") or sense.get("glosses") or [""])[-1]
    return text if len(text) <= 120 else text[:117] + "..."


def key_forms(record):
    """A few inflected forms with their tags, the principal parts a learner needs; tables are left out."""
    picked = []
    for form in record.get("forms", []):
        tags = form.get("tags", [])
        skip = {
            "table-tags",
            "inflection-template",
            "romanization",
            "diminutive",
            "canonical",
            "class",
            "alternative",
            "definite",
            "indefinite",
            "dialectal",
            "obsolete",
            "nonstandard",
            "misspelling",
        }
        if skip & set(tags) or not tags or set(tags) in ({"plural"}, {"nominative", "plural"}):
            continue
        label = f"{form['form']} ({' '.join(tags)})"
        if label not in picked:
            picked.append(label)
        if len(picked) == 6:
            break
    return picked


def sounds(lang, record):
    """Phonemic IPA by the variety it is tagged with; untagged sounds stand for every variety of the language."""
    by_variety = {}
    phonemic = any(sound.get("ipa", "").startswith("/") for sound in record.get("sounds", []))
    for sound in record.get("sounds", []):
        # The non-syllabic mark of aɪ̯ is Wiktionary's notation, not the house's: shown, a writer copies it.
        ipa = unicodedata.normalize("NFC", unicodedata.normalize("NFD", sound.get("ipa", "")).replace("\u032f", ""))
        if not ipa or (phonemic and not ipa.startswith("/")):
            continue
        tags = set(sound.get("tags", []))
        varieties = [variety for variety, names in VARIETY_TAGS.items() if variety.startswith(lang) and names & tags]
        if not varieties and tags & set(REGIONS):  # a place the writer does not transcribe (Australia, Canada)
            continue
        note = SOUND_NOTES.get(sound.get("note")) or next((SOUND_NOTES[t] for t in tags if t in SOUND_NOTES), None)
        shown = f"{ipa} ({note})" if note else ipa
        for variety in varieties or ["*"]:
            if shown not in by_variety.setdefault(variety, []):
                by_variety[variety].append(shown)
    return by_variety


def render(found):
    """The evidence as the writer reads it: one line a lexeme, one a sense, ids to cite."""
    lines = []
    for lexeme in found["lexemes"]:
        facts = [lexeme["pos"]]
        if lexeme["stressed"]:
            facts.append("stressed " + lexeme["stressed"])
        if lexeme["gender"]:
            facts.append("/".join(lexeme["gender"]))
        if lexeme["plural"]:
            facts.append("plural " + ", ".join(lexeme["plural"]))
        if lexeme["forms"]:
            facts.append("forms " + "; ".join(lexeme["forms"]))
        for variety, ipas in lexeme["ipa"].items():
            facts.append(("IPA " if variety == "*" else f"IPA {variety} ") + " ".join(ipas))
        lines.append(f"L{lexeme['id']} " + " · ".join(facts))
        for sense in lexeme["senses"]:
            extra = sense["topics"][:2]
            lines.append(f"  {sense['id']} {sense['gloss']}" + (f" [{', '.join(extra)}]" if extra else ""))
    if found["form_of"]:
        lines.append("Also an inflected form: " + "; ".join(found["form_of"]))
    return "\n".join(lines) if lines else "Wiktionary has no entry for this spelling."
