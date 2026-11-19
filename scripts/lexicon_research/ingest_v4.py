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
import argparse, json, re, sys, unicodedata, uuid
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


READING_VOWELS = {"ru": "аеёиоуыэюя", "uk": "аеєиіїоуюя"}


def letters(text):
    """A spelling as its letters, each with whether it carries the stress mark: [(letter, stressed)]. A letter keeps
    the marks that make it the letter it is (й, ї, ё), and the acute is the stress whatever it was typed on,
    also when it came inside one character (the Latin í)."""
    found = []
    for ch in unicodedata.normalize("NFD", text):
        if ch == STRESS_MARK and found:
            found[-1][1] = True
        elif unicodedata.combining(ch) and found:
            found[-1][0] += ch
        else:
            found.append([ch, False])
    return [(unicodedata.normalize("NFC", letter), stressed) for letter, stressed in found]


def own_reading(lang, spelling, reading):
    """A stressed spelling as it should stand, or None when it cannot be told. It is the word's own spelling with
    the stress marked (ме́бель beside мебель). Where its letters are others and as many (a Latin í or "ov" typed
    into a Ukrainian word, е for є, диви́тись beside дивитися), it is rebuilt from the spelling with the mark on the
    same letter, if that letter is a vowel and no more than two letters differ. A reading of another length or
    with more letters changed (bordò beside bordeaux, ходи́в beside ходити) is another word or a respelling, and is
    left for an eye. Italian marks its stress with an accent
    that is part of the letter, so there only the letters are compared."""
    if lang not in READING_VOWELS:
        bare = lambda t: "".join(ch for ch in unicodedata.normalize("NFD", t) if not unicodedata.combining(ch)).lower()  # noqa: E731
        return unicodedata.normalize("NFC", reading) if bare(reading) == bare(spelling) else None
    read, spelt = letters(reading), letters(spelling)
    same = lambda a, b: a.lower().replace("ё", "е") == b.lower().replace("ё", "е")  # noqa: E731
    if len(read) == len(spelt) and all(same(a, b) for (a, _), (b, _) in zip(read, spelt)):
        return unicodedata.normalize("NFC", reading)
    stressed = [i for i, (_, mark) in enumerate(read) if mark]
    if len(read) != len(spelt) or not stressed or any(spelt[i][0].lower() not in READING_VOWELS[lang] for i in stressed) \
            or sum(not same(a, b) for (a, _), (b, _) in zip(read, spelt)) > 2:  # more than two other letters: another word
        return None
    return "".join(letter + (STRESS_MARK if i in stressed else "") for i, (letter, _) in enumerate(spelt))


def stressed_spellings(lexeme):
    """(the thing that holds a stressed spelling, the spelling it is of) for the lexeme's readings and its forms'."""
    for said in lexeme.get("pronunciations") or []:
        if said.get("reading") and said.get("notation") in (None, "stress"):
            yield said, said.get("spelling") or lexeme.get("lemma") or ""
    for form in lexeme.get("forms") or []:
        if form.get("reading") and form.get("form"):
            yield form, form["form"]


def settle_readings(lang, lexeme):
    """Puts right the stressed spellings own_reading can rebuild. Says how many."""
    changed = 0
    for holder, spelling in stressed_spellings(lexeme) if lang in READING_VOWELS else ():
        own = own_reading(lang, spelling, holder["reading"])
        if own and own != holder["reading"]:
            holder["reading"] = own
            changed += 1
    return changed


SETTLED = OUT / "review-settle"  # the decisions of settle_v4.py: a second look at a fact the sources contradict
_settled = {}


def settled(lang):
    """headword -> the changes a second look decided on and the sources then confirmed. Nothing else is applied."""
    if lang not in _settled:
        latest = {}  # a fact asked about twice (the instructions changed) has the answer to the later question
        for path in sorted((SETTLED / lang).glob("*.json")):
            try:
                answer = json.loads(path.read_text(encoding="utf-8"))
            except json.JSONDecodeError:
                continue
            for d in answer.get("decisions") or []:
                key = (answer.get("headword"), d.get("fact"), d.get("lexeme"), d.get("variety"), d.get("was"))
                if key not in latest or (answer.get("settle_version") or "") >= latest[key][0]:
                    latest[key] = (answer.get("settle_version") or "", d)
        found = defaultdict(list)
        for (headword, *_), (_, d) in latest.items():
            if d.get("verdict") in ("change", "add") and d.get("verified"):
                found[headword].append(d)
        _settled[lang] = found
    return _settled[lang]


def kept(lang):
    """headword -> the facts a second look left as written: (fact, lexeme, variety, value, how many sources differ)."""
    found = defaultdict(set)
    for path in sorted((SETTLED / lang).glob("*.json")):
        try:
            answer = json.loads(path.read_text(encoding="utf-8"))
        except json.JSONDecodeError:
            continue
        for d in answer.get("decisions") or []:
            if d.get("verdict") == "keep":
                found[answer.get("headword")].add((d.get("fact"), d.get("lexeme"), d.get("variety"), d.get("was"), d.get("sources") or 1))
    return found


def apply_decisions(lang, entry, decisions):
    """Sets each decided fact where the entry still has the value the decision was made about, so that a decision
    never lands on an entry written again since. The written entry is not touched: this is the published copy.
    Says how many facts it set."""
    done = 0
    lexemes = entry.get("lexemes") or []
    for decision in decisions:
        if not 0 <= decision.get("lexeme", -1) < len(lexemes) or not decision.get("now"):
            continue
        lexeme = lexemes[decision["lexeme"]]
        if decision["fact"] == "gender":
            if (lexeme.get("grammar") or {}).get("gender") == decision["was"]:
                lexeme["grammar"]["gender"] = decision["now"]
                done += 1
        elif decision["fact"] == "plural":
            forms = lexeme.get("forms") or []
            for place, form in enumerate(forms):
                if form.get("form") == decision["was"] and (form.get("features") or {}).get("Number") == "Plur":
                    if decision.get("verdict") == "add":  # a second accepted plural, beside the one written
                        if not any(f.get("form") == decision["now"] for f in forms):
                            forms.insert(place + 1, {"form": decision["now"], "features": dict(form["features"])})
                            done += 1
                        break
                    form["form"] = decision["now"]
                    for key in ("reading", "ipa"):  # they were the old form's
                        form.pop(key, None)
                    done += 1
        else:  # a transcription: its sounds or its stressed syllable
            for said in lexeme.get("pronunciations") or []:
                if said.get("variety") == decision.get("variety") and said.get("ipa") == decision["was"]:
                    said["ipa"] = decision["now"]
                    if decision.get("reading"):
                        said["reading"] = decision["reading"]
                    done += 1
            settle_sounds(lang, lexeme)
    return done


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


# What shows that a frame has an open place: the abbreviations above and the full words they stand for, the names of
# kinds of word and of cases, a reflexive pronoun, and what every language uses (a plus sign, three full stops, 〜, a
# letter standing for a thing). It is generous on purpose: what it takes for a pattern stays where the writer put it.
OPEN_PLACES = {
    "en": "sb|sb's|sth|one's|your|yourself|oneself|somebody|someone|something|do|doing|adj|adv|noun|number|clause",
    "de": r"jmd\.|jmdn\.|jmdm\.|jmds\.|etw\.|etwas|jemand|jemanden|jemandem|jemandes|sich|Akk|Dat|Gen|Nom|Inf|Adj|Adv|irgendwo|irgendwohin",
    "fr": "qqn|qqch|quelqu'un|quelqu’un|quelque chose|se|s'|s’|inf|infinitif|subj|subjonctif|ind|indicatif|nom|adj|adjectif|adverbe|nombre",
    "es": "alguien|algo|se|inf|infinitivo|subj|subjuntivo|ind|indicativo|sust|sustantivo|adj|adjetivo|adverbio|cantidad|número",
    "it": "qcn|qcs|qualcuno|qualcosa|inf|infinito|cong|congiuntivo|ind|indicativo|nome|sost|sostantivo|agg|aggettivo|avverbio",
    "pt": "alguém|algo|alguma coisa|se|inf|infinitivo|subj|subjuntivo|conj|conjuntivo|ind|indicativo|subst|substantivo|nome|adj|adjetivo|oração",
    "ru": "кто|кого|кому|кем|ком|что|чего|чему|чем|чём|куда|где|откуда|когда|как|какой|какого|какому|каким|каком|сколько|чей|"
          "инф|сущ|прил|глагол|Gen|Acc|Dat|Ins|Prep|Nom|кого-л|что-л|кого-либо|что-либо|чего-либо|кому-либо|чем-либо",
    "uk": "хто|кого|кому|ким|кім|що|чого|чому|чим|чім|куди|де|звідки|коли|як|який|якого|якому|яким|скільки|чий|"
          "інф|ім|прикм|дієслово|Gen|Acc|Dat|Ins|Loc|Nom|кого-н|що-н|кого-небудь|що-небудь",
    "ja": "名詞|動詞|形容詞|数詞",
}
# A frame that ends in the word a clause opens with is open too: "ask if", "wissen, dass", "savoir si".
OPEN_CLAUSE = {
    "en": "that|if|whether|what|how|why|who|where|when|which", "de": "dass|ob|was|wie|wer|wo|warum|wann",
    "fr": "que|si|comment|pourquoi|où|quand|qui", "es": "que|si|cómo|qué|dónde|cuándo|quién|por qué",
    "it": "che|se|come|perché|dove|quando|chi", "pt": "que|se|como|onde|quando|quem|porque",
}


CLAUSE_OPENERS = {**OPEN_CLAUSE, "ru": "что|чтобы|как|ли|будто", "uk": "що|щоб|як|чи|ніби"}


def _open_place(lang):
    words = "|".join(sorted([re.escape(v) for v in FRAME_PLACES.get(lang, {})] + OPEN_PLACES[lang].split("|"), key=len, reverse=True))
    clause = rf"|(?<!\w)(?:{OPEN_CLAUSE[lang]})(?:/\w+)*\W*$" if lang in OPEN_CLAUSE else ""
    letters = "|[ABXYＡＢ]" if lang == "ja" else ""  # Japanese has no spaces to set a letter off: AはBだ
    return re.compile(r"\+|＋|\.\.\.|…|〜|～|~|（" + letters + r"|(?<![\w'’-])(?:[ABXYN]|" + words + r")(?![\w'’-])" + clause)


OPEN_PLACE = {lang: _open_place(lang) for lang in OPEN_PLACES}


MOST_COLLOCATIONS = 10  # what the shape lets a sense hold, from v4.9
MOST_FRAMES = 5
# A mark of an open place that is nothing else: an abbreviation no phrase contains as a word. Spanish and Portuguese
# "algo" and the Russian question words are words too ("por algo", "как раз"), so those languages are not judged here.
UNMISTAKABLE = {"en": {"sb", "sth", "sb's"}, "de": {"jmd.", "jmdn.", "jmdm.", "jmds.", "etw."}, "fr": {"qqn", "qqch"}, "it": {"qcn", "qcs"}}


def is_pattern(lang, text):
    """Whether a string a writer filed as a collocation has an open place, which makes it a frame."""
    if lang == "ja":
        return "〜" in text or "～" in text
    marks = UNMISTAKABLE.get(lang, set())
    return any(part in marks for token in text.split() for part in token.rstrip(",;:").split("/"))


def sort_frames(lang, lexeme):
    """A frame is a pattern with an open place (depend on sth). What the writers before v4.8 also put there, the
    word's usual company and its set phrases (heavy rain, in the lead), is moved to the sense's collocations, up
    to the ten a sense holds; the rest stays. One word alone (suicidarse, 勉強する) is the form of a pattern and
    stays too. A collocation with an unmistakable open place goes the other way. Says how many moved."""
    if lang not in OPEN_PLACE:
        return 0
    lemma, moved = lexeme.get("lemma") or "", 0
    for k, sense in enumerate(lexeme.get("senses") or []):
        frames, company, before = [], list(sense.get("collocations") or []), moved
        for frame in sense.get("frames") or []:
            one_word = frame == lemma + "する" if lang == "ja" else " " not in frame.strip()
            if OPEN_PLACE[lang].search(frame) or one_word or len(company) >= MOST_COLLOCATIONS:
                frames.append(frame)
            else:
                moved += 1
                if frame not in company:
                    company.append(frame)
        for pair in list(company):  # and the other way: "copy sb's style" filed as a collocation is a frame
            if is_pattern(lang, pair) and (pair in frames or len(frames) < MOST_FRAMES):
                company.remove(pair)
                moved += 1
                if pair not in frames:
                    frames.append(pair)
        if moved > before and not sense.get("frames"):
            sense = {**{k: v for k, v in sense.items() if k != "collocations"}, "frames": [], "collocations": company} if "cefr" not in sense else {
                k2: v2 for k, v in sense.items() for k2, v2 in ((k, v), *((("frames", []),) if k == "cefr" else ()))}
        if moved > before:
            placed = {}
            for key, value in sense.items():  # the collocations stand where the frames stood, after what is left of them
                if key == "frames":
                    placed.update({"frames": frames} if frames else {})
                    placed.update({"collocations": company} if company else {})
                elif key != "collocations":
                    placed[key] = value
            lexeme["senses"][k] = placed
    return moved


# What stands in an open place, by the word that marks it: the kinds of thing, and for a language with cases the
# cases it can be in. The book processor finds a frame in a sentence by these, so they are part of the clean entry.
_P, _T = ("person",), ("thing",)
SLOTS = {
    "en": {"sb": (_P,), "sth": (_T,), "sb's": (("possessive",),), "one's": (("possessive",),), "your": (("possessive",),),
           "yourself": (("reflexive",),), "oneself": (("reflexive",),), "adj": (("adj",),), "adv": (("adv",),), "noun": (("noun",),),
           "number": (("number",),)},
    "de": {"jmd.": (_P, ("Nom",)), "jmdn.": (_P, ("Acc",)), "jmdm.": (_P, ("Dat",)), "jmds.": (_P, ("Gen",)), "etw.": (_T,),
           "sich": (("reflexive",),), "irgendwo": (("place",),), "irgendwohin": (("direction",),), "irgendwoher": (("source",),)},
    "fr": {"qqn": (_P,), "qqch": (_T,), "se": (("reflexive",),), "s'": (("reflexive",),), "s’": (("reflexive",),)},
    "es": {"alguien": (_P,), "algo": (_T,), "se": (("reflexive",),)},
    "it": {"qcn": (_P,), "qcs": (_T,)},
    "pt": {"alguém": (_P,), "algo": (_T,), "se": (("reflexive",),)},
    "ru": {"кто": (_P, ("Nom",)), "кого": (_P, ("Gen", "Acc")), "кому": (_P, ("Dat",)), "кем": (_P, ("Ins",)), "ком": (_P, ("Loc",)),
           "что": (_T, ("Nom", "Acc")), "чего": (_T, ("Gen",)), "чему": (_T, ("Dat",)), "чем": (_T, ("Ins",)), "чём": (_T, ("Loc",)),
           "куда": (("direction",),), "где": (("place",),), "откуда": (("source",),), "когда": (("time",),), "как": (("manner",),),
           "сколько": (("amount",),), "какой": (("attribute",),), "какого": (("attribute",),), "какому": (("attribute",),),
           "каким": (("attribute",),), "каком": (("attribute",),), "какая": (("attribute",),), "какую": (("attribute",),),
           "чей": (("possessive",),), "чьего": (("possessive",),)},
    "uk": {"хто": (_P, ("Nom",)), "кого": (_P, ("Gen", "Acc")), "кому": (_P, ("Dat", "Loc")), "ким": (_P, ("Ins",)), "кім": (_P, ("Loc",)),
           "що": (_T, ("Nom", "Acc")), "чого": (_T, ("Gen",)), "чому": (_T, ("Dat", "Loc")), "чим": (_T, ("Ins",)), "чім": (_T, ("Loc",)),
           "куди": (("direction",),), "де": (("place",),), "звідки": (("source",),), "коли": (("time",),), "як": (("manner",),),
           "скільки": (("amount",),)},
}
# What stands after a plus sign: a case, a kind of word, the mood of a clause
CASES = {"Akk": "Acc", "Dat": "Dat", "Gen": "Gen", "Nom": "Nom", "Acc": "Acc", "Ins": "Ins", "Loc": "Loc", "Prep": "Loc"}
AFTER_PLUS = {"inf": ("verb", "form", "inf"), "инф.": ("verb", "form", "inf"), "інф.": ("verb", "form", "inf"), "Inf": ("verb", "form", "inf"),
              "subj": ("clause", "mood", "subj"), "cong": ("clause", "mood", "subj"), "conj": ("clause", "mood", "subj"),
              "ind": ("clause", "mood", "ind"),
              "adj": ("adj",), "agg": ("adj",), "Adj": ("adj",), "adv": ("adv",), "Adv": ("adv",), "nom": ("noun",), "nome": ("noun",),
              "sust": ("noun",), "subst": ("noun",), "noun": ("noun",), "lieu": ("place",), "lugar": ("place",), "luogo": ("place",)}
VERB_PLACES = {"en": {"do": "inf", "doing": "ing"}}
FULL_WORD_PLACES = {"en": "somebody|someone|something", "de": "etwas|jemand|jemanden|jemandem|jemandes", "fr": "quelqu'un|quelqu’un|quelque chose",
                    "it": "qualcuno|qualcosa", "pt": "alguma coisa"}


def _slot(lang, token):
    """The open place a token is, or None: "sb/sth" is one place for a person or a thing; "кого/что" one in the accusative."""
    table = SLOTS.get(lang, {})
    marks = token.split("/")
    if not all(m in table for m in marks):
        return None
    kinds = [k for m in marks for k in table[m][0]]
    cases = [set(table[m][1]) for m in marks if len(table[m]) > 1]
    slot = {"slot": list(dict.fromkeys(kinds))}
    if cases:
        shared = set.intersection(*cases) if len(cases) == len(marks) else set()
        slot["case"] = sorted(shared or set.union(*cases))
    return slot


def split_frames(lang, lexeme):
    """One frame, one pattern, and one collocation, one phrase. A string that offers a choice of words with a slash
    ("decision on/about sth", "politically/socially aware") becomes one string for each word; a slash between two
    open places (sb/sth, кого/что) is one place and stays. A string with two such choices, or with one of phrases
    (" / "), is left: which words go together there is not said. Says how many strings were split."""
    split = 0
    for sense in lexeme.get("senses") or []:
        for key in ("frames", "collocations"):
            out = []
            for text in sense.get(key) or []:
                tokens = text.split(" ")
                choices = []
                for i, token in enumerate(tokens):
                    core = token.rstrip(",;:")
                    words = core.split("/")
                    if len(words) > 1 and all(words) and not any(w in SLOTS.get(lang, {}) or not w[0].isalpha() for w in words):
                        choices.append((i, [w + token[len(core):] for w in words]))
                if len(choices) == 1 and "(" not in text and lang != "ja":
                    i, words = choices[0]
                    split += 1
                    for word in words:
                        one = " ".join(tokens[:i] + [word] + tokens[i + 1:])
                        if one not in out:
                            out.append(one)
                elif text not in out:
                    out.append(text)
            if out:
                sense[key] = out
    return split


def _is_head(lemma, token):
    lemma, token = lemma.lower(), token.lower().strip(",;:()")
    if "'" in token or "’" in token:  # l'hôpital, d'accord, dell'arte: the word after the elided one
        token = re.split(r"['’]", token)[-1] or token
    stem = lemma[: max(3, len(lemma) - 3)]
    return bool(token) and (token == lemma or (len(stem) >= 3 and token.startswith(stem)) or (len(lemma) < 3 and token.startswith(lemma)))


def names(lexeme):
    """Every way the lexeme's word is written: its lemma, its other spellings and, for Japanese, its readings
    (つらい for 辛い), any of which may stand for it in a frame."""
    found = [lexeme.get("lemma") or ""]
    found += [sp.get("text") for sp in lexeme.get("spellings") or [] if isinstance(sp, dict) and sp.get("text")]
    found += [p["reading"] for p in lexeme.get("pronunciations") or [] if p.get("notation") == "kana" and p.get("reading")]
    return list(dict.fromkeys(n for n in found if n))


def frame_parts(lang, lemma, frame, also=()):
    """A frame as a program reads it: the headword, the words fixed around it and the open places, in order.
      "decision on sth"        [{"head": "decision"}, {"word": "on"}, {"slot": ["thing"]}]
      "warten auf + Akk"       [{"head": "warten"}, {"word": "auf"}, {"slot": ["any"], "case": ["Acc"]}]
      "платить кому за что"    [{"head": "платить"}, {"slot": ["person"], "case": ["Dat"]}, {"word": "за"}, {"slot": ["thing"], "case": ["Acc", "Nom"]}]
      "afford to do sth"       [{"head": "afford"}, {"slot": ["verb"], "form": "to-inf"}]
    Japanese is written without spaces, so its frame is cut at the open places only and the piece with the headword
    is the head. Nothing is guessed: a token that is not a known mark of an open place is a word."""
    if lang == "ja":
        stems = [n[:-1] if len(n) > 1 and "\u3041" <= n[-1] <= "\u3096" else n for n in (lemma, *also)]
        parts = []
        for piece in re.split(r"([〜～])", frame):
            if piece in ("〜", "～"):
                parts.append({"slot": ["any"]})
            elif piece:
                parts.append({"head": piece} if any(st in piece for st in stems) and not any("head" in p for p in parts) else {"word": piece})
        return parts
    tokens = [t for t in re.sub(r"(?<=\w)\.\.\.", " ...", re.sub(r"[()]", " ", frame).replace("…", "...")).split() if t]
    parts, i = [], 0
    while i < len(tokens):
        raw = tokens[i]
        token = raw.rstrip(",;:")
        slot = _slot(lang, token)
        if token == "+" and i + 1 < len(tokens):
            label = tokens[i + 1].rstrip(",;:")
            if label in CASES:
                parts.append({"slot": ["any"], "case": [CASES[label]]})
            elif label.rstrip(".") in AFTER_PLUS or label in AFTER_PLUS:
                kind = AFTER_PLUS.get(label) or AFTER_PLUS[label.rstrip(".")]
                parts.append({"slot": [kind[0]], **({kind[1]: kind[2]} if len(kind) > 1 else {})})
            else:
                parts.append({"slot": ["other"], "label": label})
            i += 2
            while i < len(tokens) and tokens[i] != "+" and not _slot(lang, tokens[i].rstrip(",;:")) and not _is_head(lemma, tokens[i]) \
                    and tokens[i][:1].islower() and parts[-1]["slot"][0] in ("noun", "verb", "other"):
                parts[-1]["label"] = f"{parts[-1].get('label', label)} {tokens[i].rstrip(',;:')}"  # "+ nom pluriel", "+ inf passé"
                i += 1
            continue
        if token in CASES and parts and "slot" in parts[-1] and "case" not in parts[-1]:
            parts[-1]["case"] = [CASES[token]]  # "sich (Dat) etw. leisten": the case of the place before it
        elif token == "...":  # a clause after the word that opens one ("that ...", "что ..."); elsewhere anything ("с ... до ...")
            opener = parts and "word" in parts[-1] and re.fullmatch(CLAUSE_OPENERS.get(lang, "$^"), parts[-1]["word"].lower())
            parts.append({"slot": ["clause" if opener else "any"]})
        elif token in VERB_PLACES.get(lang, {}):
            form = VERB_PLACES[lang][token]
            if form == "inf" and parts and parts[-1] == {"word": "to"}:
                parts.pop()
                form = "to-inf"
            parts.append({"slot": ["verb"], "form": form})
            if i + 1 < len(tokens) and tokens[i + 1].rstrip(",;:") in ("sth", "sb", "sb/sth"):
                i += 1  # "do sth": the thing done belongs to the open verb
        elif lang == "de" and token == "etw." and [t.rstrip(",;:") for t in tokens[i + 1:i + 3]] == ["zu", "tun"]:
            parts.append({"slot": ["verb"], "form": "zu-inf"})
            i += 2
        elif slot and not (i + 1 < len(tokens) and tokens[i + 1].startswith("...") and "thing" in slot["slot"] and len(slot["slot"]) == 1):
            parts.append(slot)  # but "что ..." and "що ..." open a clause: there the word is a conjunction
        elif any(_is_head(n, token) for n in (lemma, *also)) and not any("head" in p for p in parts):
            parts.append({"head": token})
        elif token in ("A", "B", "X", "Y", "N"):
            parts.append({"slot": ["number" if token == "N" else "any"]})
        else:
            parts.append({"word": token})
        i += 1
    last = parts[-1] if parts else {}
    if lang in OPEN_CLAUSE and "word" in last and re.fullmatch(rf"(?:{OPEN_CLAUSE[lang]})(?:/\w+)*", last["word"]):
        parts.append({"slot": ["clause"]})  # "ask if", "savoir si": the clause the last word opens
    return parts


def unread(lang, lemma, frame, also=()):
    """Why a program cannot rely on a frame, or None: it has two patterns, no headword, no open place it knows, or
    an open place written as a word."""
    if "(" in re.sub(r"\((?:Akk|Dat|Gen|Nom)\)", "", frame) or "（" in frame or " / " in frame:
        return "two patterns in one"
    parts = frame_parts(lang, lemma, frame, also)
    if any("/" in p.get("word", "") for p in parts):
        return "two patterns in one"
    if not any("head" in p for p in parts):
        return "no headword"
    if lang in FULL_WORD_PLACES and re.search(rf"(?<![\w'’-])(?:{FULL_WORD_PLACES[lang]})(?![\w'’-])", frame):
        return "open place written as a word"
    if not any("slot" in p for p in parts):
        return "no open place it knows"
    return None


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


def clean_entry(lang, raw, meta, previous=None, duplicates=None, aliases=(), settle=True):
    """The clean entry for one written entry. previous is the clean entry of an earlier ingest, if any;
    duplicates maps the index of a lexeme to the headword that already has it."""
    headword = raw["headword"]
    entry = {"schema": "4", "language": lang, "headword": headword, "lexemes": []}
    dropped, kept, sounds, auxiliaries, defaults, marks, places, company, choices, readings = [], [], Counter(), 0, 0, 0, 0, 0, 0, 0
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
        readings += settle_readings(lang, lexeme)
        places += settle_frames(lang, lexeme)
        choices += split_frames(lang, lexeme)
        company += sort_frames(lang, lexeme)
        for sense in lexeme.get("senses") or []:  # each frame a program can rely on, as its parts: what the reader's text is matched against
            read = [{"frame": frame, "parts": frame_parts(lang, lexeme.get("lemma") or "", frame, names(lexeme))}
                    for frame in sense.get("frames") or [] if not unread(lang, lexeme.get("lemma") or "", frame, names(lexeme))]
            if read:
                sense["patterns"] = read
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
    if readings:
        entry["stressed_spellings_rebuilt"] = readings
    if places:
        entry["frames_respelled"] = places
    if company:
        entry["frames_moved_to_collocations"] = company
    if choices:
        entry["frames_split"] = choices
    retired_lexemes += list((previous or {}).get("retired_lexemes") or [])
    if retired_lexemes:
        entry["retired_lexemes"] = retired_lexemes
    entry["source"] = {"writer": meta.get("writer"), "model": meta.get("model"), "effort": meta.get("effort"),
                       "prompt_version": meta.get("prompt_version"),
                       "prompt_sha": meta.get("prompt_sha") or (V4_SHA.get(lang) if meta.get("prompt_version") == "v4" else None)}
    if settle and (facts := apply_decisions(lang, entry, settled(lang).get(headword, []))):
        entry["facts_settled"] = facts
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
        counts["stressed spellings rebuilt from the spelling"] += clean.get("stressed_spellings_rebuilt", 0)
        counts["facts set by a second look the sources confirm"] += clean.get("facts_settled", 0)
        counts["frames with an open place respelled"] += clean.get("frames_respelled", 0)
        counts["frames without an open place moved to the collocations"] += clean.get("frames_moved_to_collocations", 0)
        counts["frames and collocations with a choice of words split"] += clean.get("frames_split", 0)
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
