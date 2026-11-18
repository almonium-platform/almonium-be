"""From the entries the writers wrote to the lexicon that is published: rules only, no model call.

A written entry (out/<writer>-v4/<lang>/<headword>.json) is the paid text and is never changed. This
step derives the clean entry from it, into out/lexicon-s4/<lang>/<headword>.json, and can be rerun
whenever a rule changes, at no cost:

  ids         every lexeme gets an id and a homograph number, every sense an id; a form tied to senses
              names them by id ("sense_ids") instead of by position
  rewrites    when a clean entry already exists, a lexeme written again keeps its id, and so does a
              sense whose definition is the same or mostly the same words; a sense that is gone is
              listed under "retired", so what hangs on it (glosses, cues, a learner's card) can follow
  leaks       a lexeme whose lemma is not the headword ("hash out" under "hash") is dropped
  duplicates  a lexeme another entry already has under one of its spellings ("colour" when "color"
              lists it) is dropped from the less frequent of the two, which becomes an alias
  syllables   a transcription gets the dot it lacks before a stress mark inside the word (/əˈlaʊ/ becomes
              /ə.ˈlaʊ/): the prompt asks for a dot at every syllable boundary, and a quarter of the
              transcriptions left that one out
  forms       a headword the writer rejected as an inflected form ("signed": inflected form of sign)
              becomes an alias of its lemma's entry, so the reader who taps it is shown that entry
  source      which writer, model, effort and prompt wrote it (prompt_sha: the exact system prompt)

What was dropped, and why, stays in the clean entry under "dropped". _aliases.json maps every covered
spelling to the headword that holds its lexemes; _report.json counts what happened.

Usage: ingest_v4.py [--langs en,de] [--dry-run]   (run from the backend repository root)
"""
import argparse, json, re, sys, uuid
from collections import Counter, defaultdict
from pathlib import Path

HERE = Path("scripts/lexicon_research")
OUT = HERE / "out"
CLEAN = OUT / "lexicon-s4"  # schema 4; the name must not match a writer directory (*-v4)
LANGS = ["en", "de", "fr", "es", "it", "pt", "ru", "uk"]
NAMESPACE = uuid.UUID("6f1d5f0e-3a57-5d0b-9d5e-0b6a4c1e7a11")
# the system prompt of v4 as committed in 2ae77a9, for entries written before prompt_sha was recorded
V4_SHA = {"en": "586629ecdbd5", "de": "56e4236a695f", "fr": "91661a73562b", "es": "0da13a97b02b", "it": "e97499d7cad2",
          "pt": "172b21ef5e93", "uk": "5f1699c7798e"}
SAME_SENSE, SAME_LEXEME = 0.5, 0.4
# A reject that opens by saying what the headword inflects: "inflected form (plural) of the noun vocal",
# "past participle of convoiter". Only the opening counts: "a surname; otherwise the plural of ..." names no lemma.
FORM_OF = re.compile(r"^\s*(?:not an? [^:]{0,40}:\s*)?(?:an?\s+)?(?:inflected|conjugated|declined|plural|feminine|masculine|past|present|"
                     r"comparative|superlative|gerund|participle|third[- ]person)\b[^;.]{0,70}?\bof\s+"
                     r"(?:the\s+(?:verb|noun|adjective|adverb|lemma|word)\s+)?[\"“«„'‘]?([^\s\"”»“,;:()'’.]+)", re.I)


def words(text):
    return set(re.findall(r"[^\W\d_]+", (text or "").lower()))


def overlap(a, b):
    a, b = words(a), words(b)
    return len(a & b) / len(a | b) if a and b else 0.0


def norm(text):
    return " ".join((text or "").lower().split())


def signature(lexeme):
    """What tells two homographs of one part of speech apart: gender and the first pronunciation."""
    ipa = ((lexeme.get("pronunciations") or [{}])[0].get("ipa") or "").replace(".", "").replace(" ", "")
    return ((lexeme.get("grammar") or {}).get("gender"), ipa)


def group(lexeme):
    return (lexeme.get("pos"), (lexeme.get("lemma") or "").lower())


def lexeme_id(lang, headword, lexeme, homograph):
    pos, lemma = group(lexeme)
    return str(uuid.uuid5(NAMESPACE, f"lexeme|{lang}|{headword.lower()}|{pos}|{lemma}|{homograph}"))


def sense_id(lexeme_uuid, definition):
    return str(uuid.uuid5(NAMESPACE, f"sense|{lexeme_uuid}|{norm(definition)}"))


def dotted(ipa):
    """The transcription with a dot before every stress mark that stands inside a word."""
    return re.sub(r"(?<=[^/.\s\-])(?=[ˈˌ])", ".", ipa) if isinstance(ipa, str) else ipa


ITALIAN_CONSONANTS = "pbtdkɡfvszʃmnɲlʎr"


def italian_doubles(ipa):
    """An Italian double consonant written twice with the syllable boundary between, however it was written:
    /ˈfatːo/, /ˈfatː.o/ and /ˈfatto/ are /ˈfat.to/; /ˈpitsːa/ and /ˈpittsa/ are /ˈpit.tsa/; /proɡraˈmːare/ has
    the stress mark after the boundary, /proɡram.ˈmare/."""
    if not isinstance(ipa, str):
        return ipa
    ipa = re.sub(r"\.?([ˈˌ]?)(ts|dz|tʃ|dʒ)ː\.?", lambda m: f"{m[2][0]}.{m[1]}{m[2]}", ipa)
    ipa = re.sub(rf"\.?([ˈˌ]?)([{ITALIAN_CONSONANTS}])ː\.?", lambda m: f"{m[2]}.{m[1]}{m[2]}", ipa)
    return re.sub(rf"([{ITALIAN_CONSONANTS}])(?=\1)", r"\1.", ipa)


SOUND_FIXES = ("double consonants rewritten", "ll of Spain rewritten", "syllable dots added")


def settle_sounds(lang, lexeme):
    """Put a lexeme's transcriptions in the form the dictionary prints, by rules that need no judgement: an
    Italian double consonant written twice, the ll of Spain as most of Spain says it, and a syllable dot before
    every stress mark inside a word. Says how many transcriptions each rule changed."""
    changed = Counter()

    def settle(variety, ipa):
        if not isinstance(ipa, str):
            return ipa
        steps = ((lang == "it", italian_doubles), (variety == "es-ES", lambda text: text.replace("ʎ", "ʝ")), (True, dotted))
        for (applies, rule), name in zip(steps, SOUND_FIXES):
            if applies and rule(ipa) != ipa:
                ipa, changed[name] = rule(ipa), changed[name] + 1
        return ipa

    for spoken in lexeme.get("pronunciations") or []:
        if "ipa" in spoken:
            spoken["ipa"] = settle(spoken.get("variety"), spoken["ipa"])
    for form in lexeme.get("forms") or []:
        for variety, ipa in list((form.get("ipa") or {}).items()):
            form["ipa"][variety] = settle(variety, ipa)
    return changed


STRESS_MARK = "\u0301"  # the acute a reading of Russian or Ukrainian carries, never the spelling itself


def plain_forms(lexeme):
    """A form written with its stress mark (го́да) is the form without it, and the mark belongs to its reading.
    Says how many forms it put right."""
    changed = 0
    for form in lexeme.get("forms") or []:
        text = form.get("form") or ""
        if STRESS_MARK in text:
            form.setdefault("reading", text)
            form["form"] = text.replace(STRESS_MARK, "")
            changed += 1
    return changed


def settle_defaults(lexeme):
    """A grammar value the lexeme states for the whole word although one of its senses states another (Count for
    the word, Mass for its fourth sense) is the value of the other senses only: it is moved to each sense that
    states none, and the word no longer claims it. Nothing the writer said is lost. A word-level value that
    already allows both (Both, both, true), which the senses only narrow, stays. Says how many values moved."""
    whole, senses = lexeme.get("grammar") or {}, lexeme.get("senses") or []
    moved = 0
    for key in [k for k in whole if k not in ("note", "by_variety", "mainly") and whole[k] not in ("Both", "both", True)]:
        stated = [(s.get("grammar") or {}).get(key) for s in senses]
        if any(v is not None and v != whole[key] for v in stated):
            hedged = key in (whole.get("mainly") or [])  # "mainly" goes with the value it qualifies
            for sense, own in zip(senses, stated):
                if own is None:
                    sense["grammar"] = {key: whole[key], **(sense.get("grammar") or {})}
                    if hedged:
                        sense["grammar"]["mainly"] = sorted(set(sense["grammar"].get("mainly") or []) | {key})
            del whole[key]
            if hedged:
                whole["mainly"] = [k for k in whole["mainly"] if k != key]
                if not whole["mainly"]:
                    del whole["mainly"]
            moved += 1
    if "grammar" in lexeme and not whole:
        del lexeme["grammar"]
    return moved


# In a frame the open places for a person and a thing are written one way per language: sb and sth; jmd. jmdn. jmdm.
# jmds. and etw., as Duden and the reviewed German examples write them; qqn and qqch; alguien and algo; qcn and qcs;
# alguém and algo. The writers also used the abbreviations of other dictionaries (German jdn and jdm, Italian qc, qn,
# qlco and qlcu, French qch), so that Italian wrote "something" four ways. An abbreviation is never a word of the
# phrase, so it is rewritten; the full word (etwas, qualcosa, quelqu'un) is left alone, because it can be one
# ("etwas dagegen haben"). An open clause is three full stops, as the examples have it.
FRAME_PLACES = {
    "en": {"sb.": "sb", "smb": "sb", "smb.": "sb", "sth.": "sth", "smth": "sth", "smth.": "sth"},
    "de": {"jdn": "jmdn.", "jdn.": "jmdn.", "jmdn": "jmdn.", "j-n": "jmdn.", "jdm": "jmdm.", "jdm.": "jmdm.", "jmdm": "jmdm.", "j-m": "jmdm.",
           "jds": "jmds.", "jds.": "jmds.", "jmds": "jmds.", "j-s": "jmds.", "jd": "jmd.", "jd.": "jmd.", "jmd": "jmd.", "etw": "etw."},
    "fr": {"qn": "qqn", "qn.": "qqn", "qqn.": "qqn", "qch": "qqch", "qch.": "qqch", "qqch.": "qqch"},
    "es": {"algn": "alguien", "algn.": "alguien"},
    "it": {"qn": "qcn", "qn.": "qcn", "qlcu": "qcn", "qlcu.": "qcn", "qcn.": "qcn",
           "qc": "qcs", "qc.": "qcs", "qlco": "qcs", "qlco.": "qcs", "qcs.": "qcs"},
}
FRAME_PLACE = {lang: re.compile(r"(?<![\w-])(?:" + "|".join(re.escape(v) for v in sorted(table, key=len, reverse=True)) + r")(?![\w.])")
               for lang, table in FRAME_PLACES.items()}


def settle_frames(lang, lexeme):
    """Write the open places of every frame the one way the language has, and an open clause as three full stops.
    A frame that becomes the same as another of its sense is dropped. Says how many frames changed."""
    changed = 0
    for sense in lexeme.get("senses") or []:
        frames = []
        for frame in sense.get("frames") or []:
            new = frame.replace("…", "...")
            if lang in FRAME_PLACE:
                new = FRAME_PLACE[lang].sub(lambda m: FRAME_PLACES[lang][m.group(0)], new)
            changed += new != frame
            if new not in frames:
                frames.append(new)
        if frames:
            sense["frames"] = frames
    return changed


BE_WHEN_PRONOMINAL = {"fr", "it"}  # être, essere: every pronominal use of a verb, without exception


def pronominal_auxiliary(lang, lexeme):
    """Give a French or Italian pronominal sense the auxiliary it is conjugated with; says how many lacked it."""
    whole = lexeme.get("grammar") or {}
    if lang not in BE_WHEN_PRONOMINAL or lexeme.get("pos") != "VERB" or whole.get("auxiliary") == "be":
        return 0
    changed = 0
    for sense in lexeme.get("senses") or []:
        own = sense.get("grammar") or {}
        if (own.get("valency") == "Refl" or own.get("reflexive") is True) and own.get("auxiliary") != "be":
            sense["grammar"] = {**own, "auxiliary": "be"}
            changed += 1
    return changed


def is_own(headword, lexeme):
    """Whether the lexeme belongs under the headword: its lemma, or one of its spellings, is the headword."""
    own = headword.lower()
    if (lexeme.get("lemma") or "").lower() == own:
        return True
    return any((s.get("text") or "").lower() == own for s in lexeme.get("spellings") or [])


def match_lexemes(old, new):
    """Pairs (old index, new index) of lexemes that are the same word, within one part of speech and lemma."""
    pairs, free_old, free_new = [], set(range(len(old))), set(range(len(new)))
    for j in sorted(free_new):
        hit = next((i for i in sorted(free_old) if group(old[i]) == group(new[j]) and signature(old[i]) == signature(new[j])), None)
        if hit is not None:
            pairs.append((hit, j)); free_old.discard(hit); free_new.discard(j)
    scored = sorted(((max((overlap(a.get("definition"), b.get("definition")) for a in old[i].get("senses", []) for b in new[j].get("senses", [])), default=0), i, j)
                     for i in free_old for j in free_new if group(old[i]) == group(new[j])), reverse=True)
    for score, i, j in scored:
        if i in free_old and j in free_new and (score > 0 or sum(group(old[k]) == group(new[j]) for k in free_old) == 1):
            pairs.append((i, j)); free_old.discard(i); free_new.discard(j)
    return pairs


def match_senses(old, new):
    """{new index: old sense} for senses that are the same sense: the same definition, or mostly the same words."""
    kept, free = {}, list(range(len(old)))
    for j, s in enumerate(new):
        hit = next((i for i in free if norm(old[i].get("definition")) == norm(s.get("definition"))), None)
        if hit is not None:
            kept[j] = old[hit]; free.remove(hit)
    scored = sorted(((overlap(old[i].get("definition"), s.get("definition")), i, j) for i in free for j, s in enumerate(new) if j not in kept), reverse=True)
    for score, i, j in scored:
        if score >= SAME_SENSE and i in free and j not in kept:
            kept[j] = old[i]; free.remove(i)
    return kept, [old[i] for i in free]


def clean_entry(lang, raw, meta, previous=None, duplicates=None, aliases=()):
    """The clean entry for one written entry. previous is the clean entry of an earlier ingest, if any;
    duplicates maps the index of a lexeme to the headword that already has it."""
    headword = raw["headword"]
    entry = {"schema": "4", "language": lang, "headword": headword, "lexemes": []}
    dropped, kept, sounds, auxiliaries, defaults, marks, places = [], [], Counter(), 0, 0, 0, 0
    for index, lexeme in enumerate(raw.get("lexemes") or []):
        if not is_own(headword, lexeme):
            dropped.append({"lemma": lexeme.get("lemma"), "pos": lexeme.get("pos"), "reason": "not the headword's lemma"})
        elif duplicates and index in duplicates:
            dropped.append({"lemma": lexeme.get("lemma"), "pos": lexeme.get("pos"), "reason": f"written under {duplicates[index]}"})
        else:
            kept.append(json.loads(json.dumps(lexeme)))
    old = (previous or {}).get("lexemes") or []
    pairs = dict((j, i) for i, j in match_lexemes(old, kept))
    used = {(group(o), o.get("homograph")) for o in old}
    retired_lexemes = [{"id": o["id"], "lemma": o.get("lemma"), "pos": o.get("pos")} for i, o in enumerate(old) if i not in pairs.values()]
    for j, lexeme in enumerate(kept):
        before = old[pairs[j]] if j in pairs else None
        if before:
            homograph, uid = before["homograph"], before["id"]
        else:
            homograph = next(n for n in range(1, 1000) if (group(lexeme), n) not in used)
            uid = lexeme_id(lang, headword, lexeme, homograph)
        used.add((group(lexeme), homograph))
        senses = lexeme.get("senses") or []
        same, gone = match_senses(before.get("senses") or [], senses) if before else ({}, [])
        for k, sense in enumerate(senses):
            sense_uid = same[k]["id"] if k in same else sense_id(uid, sense.get("definition"))
            senses[k] = {"id": sense_uid, **sense}
        for form in lexeme.get("forms") or []:
            if "senses" in form:
                form["sense_ids"] = [senses[n - 1]["id"] for n in form.pop("senses") if 0 < n <= len(senses)]
        sounds += settle_sounds(lang, lexeme)
        auxiliaries += pronominal_auxiliary(lang, lexeme)
        defaults += settle_defaults(lexeme)
        marks += plain_forms(lexeme)
        places += settle_frames(lang, lexeme)
        cleaned = {"id": uid, "homograph": homograph, **lexeme}
        retired = [{"id": s["id"], "definition": s.get("definition")} for s in gone] + list((before or {}).get("retired") or [])
        if retired:
            cleaned["retired"] = retired
        entry["lexemes"].append(cleaned)
    if raw.get("reject") and not raw.get("lexemes"):
        entry["reject"] = raw["reject"]
    if aliases:
        entry["aliases"] = sorted(aliases)
    if dropped:
        entry["dropped"] = dropped
    for name in SOUND_FIXES:
        if sounds[name]:
            entry[name.replace(" ", "_").lower()] = sounds[name]
    if auxiliaries:
        entry["pronominal_auxiliaries_set"] = auxiliaries
    if defaults:
        entry["word_grammar_moved_to_senses"] = defaults
    if marks:
        entry["stress_marks_moved_to_readings"] = marks
    if places:
        entry["frames_respelled"] = places
    retired_lexemes += list((previous or {}).get("retired_lexemes") or [])
    if retired_lexemes:
        entry["retired_lexemes"] = retired_lexemes
    entry["source"] = {"writer": meta.get("writer"), "model": meta.get("model"), "effort": meta.get("effort"),
                       "prompt_version": meta.get("prompt_version"),
                       "prompt_sha": meta.get("prompt_sha") or (V4_SHA.get(lang) if meta.get("prompt_version") == "v4" else None)}
    return entry


def load_raw(lang):
    """headword -> (entry, meta) for every entry a v4 writer wrote, errors left out. The first writer directory wins."""
    found = {}
    for d in sorted(OUT.glob(f"*-v4/{lang}")):
        for f in sorted(d.glob("*.json")):
            if f.name.endswith(".meta.json") or f.stem in found:
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


def zipf_of(lang):
    best = {}
    for f in (HERE / "headwords").glob(f"*{lang}.json"):
        for row in json.loads(f.read_text(encoding="utf-8")):
            key = row["headword"].lower()
            best[key] = max(best.get(key, 0), row.get("zipf") or 0)
    return best


def alike(a, b):
    """How much two lexemes say the same thing: the share of one's senses the other also has, either way round."""
    def share(x, y):
        xs = x.get("senses") or []
        return sum(max((overlap(s.get("definition"), t.get("definition")) for t in y.get("senses") or []), default=0) for s in xs) / max(1, len(xs))
    return max(share(a, b), share(b, a))


def lists(lexeme, headword):
    return any((s.get("text") or "").lower() == headword.lower() for s in lexeme.get("spellings") or [])


def find_duplicates(raw, zipf):
    """Lexemes written twice because a word has two spellings and both were headwords.

    When a lexeme of entry A lists the headword of entry B among its spellings, and B has a lexeme of the
    same part of speech that lists A back or says mostly the same, the two are one word: the less frequent
    entry loses its copy. Returns {headword: {lexeme index: the headword that keeps it}} and the aliases,
    {headword: {"to": canonical, "as": kind}}, for an entry left with no lexeme of its own and for a reject
    that names the entry holding its word ("spelling of colour", "pre-reform spelling of dass", "inflected
    form of sign"). kind is spelling, old spelling, misspelling or form."""
    by_lower = defaultdict(list)
    for headword in raw:
        by_lower[headword.lower()].append(headword)
    rank = lambda h: (-zipf.get(h.lower(), 0), h)
    duplicates = defaultdict(dict)
    for a, (entry_a, _) in raw.items():
        for i, mine in enumerate(entry_a.get("lexemes") or []):
            if not is_own(a, mine):
                continue
            for spelling in mine.get("spellings") or []:
                text = (spelling.get("text") or "").lower()
                for b in by_lower.get(text, []) if text != a.lower() else []:
                    candidates = [(alike(mine, theirs), j, theirs) for j, theirs in enumerate(raw[b][0].get("lexemes") or [])
                                  if is_own(b, theirs) and theirs.get("pos") == mine.get("pos")]
                    if not candidates:
                        continue
                    score, j, theirs = max(candidates, key=lambda c: (lists(c[2], a), c[0]))
                    if not (lists(theirs, a) or score >= SAME_LEXEME):
                        continue
                    if rank(a) < rank(b):
                        duplicates[b][j] = a
                    else:
                        duplicates[a][i] = b
    aliases = {}
    for headword, (entry, _) in raw.items():
        own = [i for i, lx in enumerate(entry.get("lexemes") or []) if is_own(headword, lx)]
        if own and all(i in duplicates.get(headword, {}) for i in own):
            aliases[headword] = {"to": duplicates[headword][own[0]], "as": "spelling"}
        m = re.search(r"([\w-]*)\s*spelling of\s+[\"“«„]?([^\"”»“,;(]+)", entry.get("reject") or "", re.I)
        if m and not entry.get("lexemes"):
            target = by_lower.get(m.group(2).strip().lower())
            before = (entry.get("reject") or "")[:m.start(2)].lower()
            kind = "misspelling" if "misspelling" in before else "old spelling" if re.search(r"pre-reform|old|obsolete|archaic|former", before) else "spelling"
            if target:
                aliases[headword] = {"to": target[0], "as": kind}
        m = FORM_OF.search(entry.get("reject") or "")
        if m and not entry.get("lexemes") and headword not in aliases:
            target = by_lower.get(m.group(1).lower())
            if target:
                aliases[headword] = {"to": target[0], "as": "form"}
    return duplicates, aliases


def ingest(lang, dry_run=False):
    raw = load_raw(lang)
    duplicates, aliases = find_duplicates(raw, zipf_of(lang))
    covered_by = defaultdict(set)
    for spelling, alias in aliases.items():
        covered_by[alias["to"]].add(spelling)
    counts = Counter(entries=len(raw))
    target = CLEAN / lang
    if not dry_run:
        target.mkdir(parents=True, exist_ok=True)
    for headword, (entry, meta) in raw.items():
        path = target / f"{headword}.json"
        previous = json.loads(path.read_text(encoding="utf-8")) if path.exists() else None
        clean = clean_entry(lang, entry, meta, previous, duplicates.get(headword), covered_by.get(headword, ()))
        counts["lexemes"] += len(clean["lexemes"])
        counts["senses"] += sum(len(lx.get("senses") or []) for lx in clean["lexemes"])
        counts["rejects"] += bool(clean.get("reject"))
        for d in clean.get("dropped", []):
            counts["leaked lexemes dropped" if d["reason"].startswith("not the") else "duplicate lexemes dropped"] += 1
        counts["senses retired"] += sum(len(lx.get("retired") or []) for lx in clean["lexemes"])
        for name in SOUND_FIXES:
            counts[name] += clean.get(name.replace(" ", "_").lower(), 0)
        counts["pronominal senses given their auxiliary"] += clean.get("pronominal_auxiliaries_set", 0)
        counts["word-level grammar moved to the senses"] += clean.get("word_grammar_moved_to_senses", 0)
        counts["stress marks moved from a form to its reading"] += clean.get("stress_marks_moved_to_readings", 0)
        counts["frames with an open place respelled"] += clean.get("frames_respelled", 0)
        text = json.dumps(clean, ensure_ascii=False, indent=1) + "\n"
        if not dry_run and (previous is None or path.read_text(encoding="utf-8") != text):
            path.write_text(text, encoding="utf-8")
            counts["files written"] += 1
    counts["aliases"] = len(aliases)
    if not dry_run:
        (target / "_aliases.json").write_text(json.dumps(dict(sorted(aliases.items())), ensure_ascii=False, indent=1) + "\n", encoding="utf-8")
        (target / "_report.json").write_text(json.dumps(dict(counts), indent=1) + "\n", encoding="utf-8")
    return counts, duplicates, aliases


if __name__ == "__main__":
    ap = argparse.ArgumentParser()
    ap.add_argument("--langs", default=",".join(LANGS))
    ap.add_argument("--dry-run", action="store_true", help="count what would happen, write nothing")
    a = ap.parse_args()
    for lang in a.langs.split(","):
        counts, _, aliases = ingest(lang, a.dry_run)
        if counts["entries"]:
            print(f"{lang}: {dict(counts)}")
            print("    aliases:", ", ".join(f"{k} -> {v['to']} ({v['as']})" for k, v in list(sorted(aliases.items()))[:8]))
    sys.exit(0)
