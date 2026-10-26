"""Compare a written entry with its Wiktionary evidence, and fill what the evidence settles.

Disagreements come in two kinds. A fact (a lexeme's gender, its plural, how a variety says it, where a sense is
used) has one answer the evidence can settle: these are compared here, and an answer the evidence gives that the
entry lacks is filled in. A judgment (how finely to split senses, which rare sense a learner needs, the wording of a
definition) has no single answer: the only thing checked is that no common evidence sense or lexeme was dropped,
and that is reported, never "fixed" by another round of review.

The writer cites, on each sense, the evidence senses it covers ("from": ["2.3"]). The citations align the entry
with the evidence: a lexeme is compared with the evidence lexemes its senses cite, a sense takes the regions of the
senses it cites.

Each finding is {"path", "check", "kind": "fact" | "coverage", "message"}; fill() returns the entry with the facts
the evidence settles written in and the patches it applied.
"""

import copy
import re
import unicodedata

# Senses a learner can do without: Wiktionary's labels for the rare, the old and the regional-dialect.
UNCOMMON = {"archaic", "obsolete", "rare", "dated", "dialectal", "poetic", "historical"}
GENDERLESS = {"en"}
IPA_SAME = [
    ("ɹ", "r"),
    ("ɡ", "g"),
    ("l̩", "əl"),
    ("n̩", "ən"),
    ("m̩", "əm"),
    ("ɚ", "ər"),
    ("ɝ", "ɜr"),
    ("ʧ", "tʃ"),
    ("ʤ", "dʒ"),
    ("ɫ", "l"),
]
IGNORED = re.compile("[/\\[\\].\u02cc\\s\u0361\u035c\u032f\u0294\u203f]")
# Notation one language writes two ways for one sound: German vocalic r as \u0259r or \u0250, English DRESS as e or \u025b.
LANGUAGE_SAME = {
    "de": [("\u0259r", "\u0250")],
    "en": [("\u025b", "e")],
    "fr": [("\u0254", "o")],
    "es": [("w", "u"), ("j", "i")],
    "pt": [("w", "u"), ("j", "i")],
}
# Citations are provenance: wrong ones cost the regions they would carry, but a reader never sees them.
PROVENANCE = "provenance"


def compare(entry, found):
    """Findings from comparing an entry with the evidence Wiktionary gives for its headword."""
    findings = []

    def find(path, check, kind, message):
        findings.append({"path": path, "check": check, "kind": kind, "message": message})

    lang = entry.get("language")
    senses = {sense["id"]: (lexeme, sense) for lexeme in found["lexemes"] for sense in lexeme["senses"]}
    cited_lexemes = set()
    cited = set()
    for li, lexeme in enumerate(entry.get("lexemes") or []):
        base = f"/lexemes/{li}"
        ids = [cite for sense in lexeme.get("senses") or [] for cite in sense.get("from") or []]
        for cite in ids:
            if cite not in senses:
                find(base, "citation", PROVENANCE, f"cites {cite}, which the evidence does not have")
        evidence = {senses[cite][0]["id"]: senses[cite][0] for cite in ids if cite in senses}
        cited |= {cite for cite in ids if cite in senses}
        cited_lexemes |= set(evidence)
        if not ids and found["lexemes"]:
            find(base, "citation", PROVENANCE, "cites no evidence sense")
        aligned = list(evidence.values())
        gender = (lexeme.get("grammar") or {}).get("gender")
        if gender and lang in GENDERLESS:
            find(f"{base}/grammar/gender", "gender", "fact", f"{lang} nouns have no grammatical gender")
        genders = {g for evidence_lexeme in aligned for g in evidence_lexeme["gender"]}
        apart = {tuple(evidence_lexeme["gender"]) for evidence_lexeme in aligned if len(evidence_lexeme["gender"]) == 1}
        if len(apart) > 1:
            find(
                base,
                "merged",
                "fact",
                "cites evidence lexemes of different genders "
                f"({', '.join(sorted(g for (g,) in apart))}): they are separate lexemes",
            )
        if gender and len(genders) == 1 and gender not in genders and lang not in GENDERLESS:
            find(f"{base}/grammar/gender", "gender", "fact", f"{gender} where the evidence has {genders.pop()}")
        plurals = {unstressed(p) for evidence_lexeme in aligned for p in evidence_lexeme["plural"]}
        ours = {
            unstressed(form["form"])
            for form in lexeme.get("forms") or []
            if (form.get("features") or {}).get("Number") == "Plur"
            and (form.get("features") or {}).get("Case") in (None, "Nom")
            and "Person" not in (form.get("features") or {})
        }
        if lexeme.get("pos") == "NOUN" and plurals and ours and not ours & plurals:
            find(
                f"{base}/forms",
                "plural",
                "fact",
                f"plural {', '.join(sorted(ours))} where the evidence has {', '.join(sorted(plurals))}",
            )
        for pi, spoken in enumerate(lexeme.get("pronunciations") or []):
            heard = [
                ipa
                for evidence_lexeme in aligned
                for ipa in evidence_lexeme["ipa"].get(spoken["variety"], []) + evidence_lexeme["ipa"].get("*", [])
            ]
            verdict = sounds_like(spoken["ipa"], heard, lang)
            if verdict:
                find(
                    f"{base}/pronunciations/{pi}",
                    "ipa",
                    "fact",
                    f"{spoken['ipa']}: {verdict} the evidence ({' '.join(heard)})",
                )
    for evidence_lexeme in found["lexemes"]:
        common = [sense for sense in evidence_lexeme["senses"] if not UNCOMMON & set(sense["register"])]
        if evidence_lexeme["pos"] == "PHRASE" or not common:
            continue
        if evidence_lexeme["id"] not in cited_lexemes:
            find(
                "/lexemes",
                "lexeme",
                "coverage",
                f"L{evidence_lexeme['id']} {evidence_lexeme['pos']} ({common[0]['gloss'][:60]}) is not covered",
            )
            continue
        for sense in common:
            if sense["id"] not in cited:
                find("/lexemes", "sense", "coverage", f"{sense['id']} {sense['gloss'][:80]} is not covered")
    return findings


def sounds_like(ipa, heard, lang=None):
    """None when the transcription says what one of the evidence's does; else how it differs. Notation is set
    aside: syllable dots, secondary stress, tie bars, non-syllabic marks, the glottal stop, ɹ for r, a syllabic l
    for əl, and length when the evidence marks none (American transcriptions often write /but/ for /buːt/).
    An optional sound, /ˈpeɪvm(ə)nt/, counts both ways. A partial transcription, /ɹɪ-/, and a phonetic one in
    brackets, [zɐˈmɔk], are no evidence about phonemes."""
    heard = [other.split(" (")[0] for other in heard if other.startswith("/")]
    full = [variant for other in heard if "-" not in other for variant in optional(other)]
    # A partial transcription, /tɹæns-/, says how the word opens: it stands for the full ones with that opening.
    for part in (other.strip("/").rstrip("-") for other in heard if other.endswith("-/")):
        for whole in list(full):
            body = whole.strip("/")
            full += [f"/{part}{body[cut:]}/" for cut in range(max(len(part) - 1, 1), len(part) + 2)]
    heard = full
    if not heard:
        return None
    ours = plain(ipa, lang)
    theirs = [plain(other, lang) for other in heard]
    if not any("ː" in other for other in theirs):
        ours = ours.replace("ː", "")
    # A stress mark opening the word says what an unmarked one does; Wiktionary leaves it off monosyllables, and
    # writes none at all for a language with fixed stress (French), where the house marks it.
    if not any("ˈ" in other for other in theirs):
        ours = ours.replace("ˈ", "")
    ours = ours.removeprefix("ˈ")
    theirs = [other.removeprefix("ˈ") for other in theirs]
    if ours in theirs:
        return None
    if ours.replace("ˈ", "") in [other.replace("ˈ", "") for other in theirs]:
        return "stress differs from"
    return "sounds differ from"


def unstressed(word):
    """A spelling without the stress accents Wiktionary writes on Ukrainian forms (адре́си)."""
    return unicodedata.normalize("NFC", unicodedata.normalize("NFD", word).replace("\u0301", ""))


def optional(ipa):
    """/ˈpeɪvm(ə)nt/ as /ˈpeɪvmənt/ and /ˈpeɪvmnt/."""
    match = re.search(r"\(([^)]*)\)", ipa)
    if not match:
        return [ipa]
    kept = ipa[: match.start()] + match.group(1) + ipa[match.end() :]
    return optional(kept) + optional(ipa[: match.start()] + ipa[match.end() :])


def plain(ipa, lang=None):
    text = unicodedata.normalize("NFD", ipa)
    text = re.sub("([wj])\u0303", r"\1", text)  # a nasal glide after a nasal vowel, /ɐ̃w̃/, is /ɐ̃w/
    for a, b in IPA_SAME + LANGUAGE_SAME.get(lang, []):
        text = text.replace(unicodedata.normalize("NFD", a), b)
    text = IGNORED.sub("", text)
    if lang == "it":  # a double consonant, /t.t/, is the house's long one, /tː/
        text = re.sub(r"([^\Wˈaeiouɛɔ])\1", "\\1ː", text)
    return unicodedata.normalize("NFC", text.replace("ː", "ː"))


def fill(entry, found):
    """The entry with the regions its citations settle written in, and the patches that did it. A sense whose cited
    evidence senses are all labelled with places, and which names none itself, takes those places."""
    filled = copy.deepcopy(entry)
    lang = entry.get("language")
    senses = {sense["id"]: sense for lexeme in found["lexemes"] for sense in lexeme["senses"]}
    patches = []
    for li, lexeme in enumerate(filled.get("lexemes") or []):
        for si, sense in enumerate(lexeme.get("senses") or []):
            cited = [senses[cite] for cite in sense.get("from") or [] if cite in senses]
            if not cited or sense.get("regions") or not all(evidence["regions"] for evidence in cited):
                continue
            mainly = any(evidence["mainly"] for evidence in cited)
            regions = [
                {"variety": f"{lang}-{code}", "extent": "mainly" if mainly else "only"}
                for code in sorted({code for evidence in cited for code in evidence["regions"]})
            ]
            sense["regions"] = regions
            patches.append({"op": "add", "path": f"/lexemes/{li}/senses/{si}/regions", "value": regions})
    return filled, patches
