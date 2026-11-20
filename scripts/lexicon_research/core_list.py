"""The words of a language that are worth a call: headwords/healthy-<lang>.json and doubtful-<lang>.json (--healthy),
made from every list of the language, its frequency list and what Wiktionary says of each spelling; see healthy()
and "Healthy lists" in docs/LEXICON.md. What follows describes the older list of one kind, core-<lang>.json, which
the healthy list replaces and whose rules for Russian and Japanese it still uses.

The common words of a language that nobody has written yet: headwords/core-<lang>.json.

Every spelling wordfreq gives Zipf 3 or above that Wiktionary knows as a word of its own (a lemma, a
contraction or an alternative spelling) and as nothing else, and that no entry of any writer holds, as
a headword or as a spelling of another headword, most frequent first. It is the rule the lists of
German, French, Spanish, Italian and Portuguese were made by (it gives the French one back but for
some twenty words, most of them written with an apostrophe). wordfreq writes everything in lower
case, so a proper name (Paris, Москва) is never found and never queued; German looks the capitalised
spelling up too and keeps the lower-case one where both are lemmas.

A spelling that is an inflected form as well (French "été", "est"; Russian "есть", "дома") is left
out: most of its frequency belongs to the form, and which of these words a learner needs as words is
a judgement no frequency makes. --forms-too lists them, for whoever makes it.

Russian print leaves ё as е, and Wiktionary files that spelling ("ее", "елка") as an alternative of
the lemma ("её", "ёлка"). Such a spelling is not a word to write: the lemma is queued instead, with
the frequency of both spellings, and its entry lists the е spelling. A spelling that is a lemma in
its own right as well ("все" beside "всё") is kept, and one that is a form of another word ("лет",
the years, beside "лёт") gives the ё word nothing. A frequency list of Russian is also full of
letters and abbreviations that Wiktionary files as words (г, см, км): a word without a vowel is left
out, and so is a single letter that is not one of the one-letter words.

Japanese is not written in words, and wordfreq's list of it is what MeCab cut the text into: the
pieces a word is inflected from (なっ, 食べ) beside the words. Each piece is read alone by MeCab and
counted under its dictionary form (なっ under なる), names, affixes and auxiliaries apart from だ are
left out, and what remains must be a word Wiktionary has. A word written both ways (こと and 事, 私 and
わたし) is queued once, under the spelling met more often; a kana spelling rarer than a word with
kanji it could be (はし beside 橋 and 箸) is not queued. What comes out still has suffixes (者, 性, 的)
and pieces MeCab did not know (てる, ちゃ) near the top, which the gate cannot tell from words: it is
raw material for a list someone reads, as headwords/top-ja-ja.json was, and needs wordfreq's Japanese
extra (mecab-python3 and ipadic).

Usage: core_list.py --langs de,fr --healthy [--floor 3.0] [--dry]
       core_list.py --langs ru [--floor 3.0] [--forms-too | --forms-only] [--dry]   (run from the backend repository root)
"""
import argparse, json, math, re, sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
import lemma_gate  # noqa: E402
import v3_entries  # noqa: E402
from prompting import HERE  # noqa: E402

WORD = re.compile(r"[^\W\d_]+(?:[-'’][^\W\d_]+)*")
UNPOINTED = {"ru": ("е", "ё")}  # the letter print uses, the letter the lemma has
ALPHABET = {"ru": re.compile(r"[а-яё]+(?:-[а-яё]+)*")}
VOWELS = {"ru": set("аеёиоуыэюя")}
# The one-letter words of each language: the rest are names of letters, elided forms (French c', t') and abbreviations
ONE_LETTER = {"ru": set("авижкосуяэ"), "en": set("aio"), "de": set(), "fr": set("àayô"), "es": set("aeoyu"), "it": set("aeèio"),
              "pt": set("aeéoà")}


def a_word(lang, word):
    word = word.lower()  # "Россия" is a word like "россия"
    if lang in ALPHABET and not ALPHABET[lang].fullmatch(word):
        return False
    if lang in ONE_LETTER and len(word) == 1:
        return word in ONE_LETTER[lang]
    return lang not in VOWELS or bool(set(word) & VOWELS[lang])


def pointed(lang, word, table):
    """The spellings this one is the unpointed print of: "ее" gives ["её"]."""
    if lang not in UNPOINTED:
        return []
    plain, mark = UNPOINTED[lang]
    out = []
    for i, ch in enumerate(word):
        if ch == plain:
            other = word[:i] + mark + word[i + 1:]
            if other in table and other not in out:
                out.append(other)
    return out


def written(lang):
    return {f.stem.lower() for f in lemma_gate.OUT.glob(f"*/{lang}/*.json") if not f.name.endswith(".meta.json")}


def core(lang, floor, forms_too=False):
    from wordfreq import iter_wordlist, zipf_frequency

    table, have = lemma_gate.load(lang), written(lang)
    freq, order = {}, []
    for word in iter_wordlist(lang):
        zipf = zipf_frequency(word, lang)
        if zipf < floor:
            break
        if not WORD.fullmatch(word) or not a_word(lang, word):
            continue
        found = [s for s in lemma_gate.spellings(lang, word) if s in table]
        flags = set("".join(table[s][0] for s in found))
        lemmas = pointed(lang, word, table)
        if lemmas:
            flags.discard("A")  # the alternative it is filed as is the lemma with its ё
        if "F" in flags and not forms_too:
            continue
        if flags & set("LCA"):
            targets = [next(s for s in found if set(table[s][0]) & set("LCA"))]
        elif lemmas and not flags:
            targets = [w for w in lemmas if "L" in table[w][0] and (forms_too or "F" not in table[w][0])]
        else:
            continue
        for target in targets:
            if target not in freq:
                order.append(target)
            freq[target] = freq.get(target, 0) + 10 ** zipf
    rows = [{"headword": w, "zipf": round(math.log10(freq[w]), 2), "set": f"{lang}-core"} for w in order]
    rows = [r for r in rows if r["headword"].lower() not in have and not lemma_gate.covered(lang, r["headword"])]
    return sorted(rows, key=lambda r: -r["zipf"])


JAPANESE = re.compile(r"[\u3041-\u3096\u30a1-\u30fa\u30fc\u3005\u4e00-\u9fff]+")
KANA = re.compile(r"[\u3041-\u3096\u30a1-\u30fa\u30fc]+")
# MeCab's classes (ipadic) that are words of their own; an auxiliary is one only if it is the copula
JAPANESE_KINDS = {("名詞", "一般"), ("名詞", "サ変接続"), ("名詞", "形容動詞語幹"), ("名詞", "副詞可能"), ("名詞", "代名詞"), ("名詞", "数"),
                  ("名詞", "非自立"), ("名詞", "ナイ形容詞語幹"), ("動詞", "自立"), ("動詞", "非自立"), ("形容詞", "自立"), ("形容詞", "非自立"),
                  ("副詞", None), ("連体詞", None), ("接続詞", None), ("感動詞", None), ("助詞", None)}
JAPANESE_AUXILIARIES = {"だ"}


def katakana(text):
    return "".join(chr(ord(c) + 0x60) if "\u3041" <= c <= "\u3096" else c for c in text)


def japanese(floor):
    import MeCab, ipadic
    from wordfreq import iter_wordlist, zipf_frequency

    tagger = MeCab.Tagger(ipadic.MECAB_ARGS)

    def read(piece):
        lines = [line for line in tagger.parse(piece).splitlines() if line != "EOS"]
        if len(lines) != 1 or "\t" not in lines[0]:
            return None
        return lines[0].split("\t")[1].split(",")

    table, have = lemma_gate.load("ja"), written("ja")
    freq, kind, order = {}, {}, []
    for piece in iter_wordlist("ja"):
        zipf = zipf_frequency(piece, "ja")
        if zipf < floor:
            break
        tags = read(piece) if JAPANESE.fullmatch(piece) else None
        if not tags or len(tags) < 7:
            continue
        base = piece if tags[6] == "*" else tags[6]
        wanted = (tags[0], tags[1]) in JAPANESE_KINDS or (tags[0], None) in JAPANESE_KINDS or (tags[0] == "助動詞" and base in JAPANESE_AUXILIARIES)
        if not wanted or not set(table.get(base, ("",))[0]) & set("LA"):
            continue
        if base not in freq:
            order.append(base)
            kind[base] = tags[0]
        freq[base] = freq.get(base, 0) + 10 ** zipf
    reading = {}
    for base in order:
        tags = read(base)
        reading[base] = base if KANA.fullmatch(base) else (tags[7] if tags and len(tags) > 7 else None)
    drop = set()
    for kana in [b for b in order if KANA.fullmatch(b)]:
        same = [b for b in order if b != kana and not KANA.fullmatch(b) and reading[b] == katakana(kana) and kind[b] == kind[kana]]
        if same and max(freq[b] for b in same) > freq[kana]:
            drop.add(kana)  # the word is usually written with kanji (私), or the kana is several words (はし)
        elif len(same) == 1:
            drop.add(same[0])  # usually written in kana (こと), and 事 is the same word
    rows = [{"headword": w, "zipf": round(math.log10(freq[w]), 2), "set": "ja-core"} for w in order
            if w not in drop and w not in have and not lemma_gate.covered("ja", w)]
    return sorted(rows, key=lambda r: -r["zipf"])


# Lists a healthy list is not made from: samples, trials and what this file writes itself.
NOT_QUEUES = ("measure-", "v48-", "v49-", "v410-", "v410e-", "v411-", "v411e-", "hard-", "mend-", "tour-", "pilot-", "codex-", "cross-", "healthy-", "doubtful-", "rewrite-")
# A word that is a lemma and an inflected form as well is refused once in fifty outside English and once in two in
# English, where "viewed" and "fines" are filed as an adjective and a noun too. Of 4,277 such English words given to
# a writer, those met more often than their base word were refused 16% of the time (data, vaccinated), those with a
# verb of their own 0 to 7% (saw, felt, offer), those with two parts of speech of their own 26 to 30% (dancing,
# insured); the rest, one adjective or one noun beside the form, 55 to 67%. English keeps the first three kinds.
FORM_TOO_NEEDS = {"en": 0.5}  # how far above its base word's Zipf such a word must be met, failing the other two
LENT_FREQUENCY = 3.5  # where a German noun stands in the list when its frequency is that of a verb form written like it


def queued(lang):
    """Every headword a list holds for the language: {headword: zipf or None}."""
    found = {}
    for path in sorted((HERE / "headwords").glob(f"*{lang}.json")):
        if path.name.startswith(NOT_QUEUES) or not (path.name == f"{lang}.json" or path.name.endswith(f"-{lang}.json")):
            continue
        for row in json.loads(path.read_text()):
            if isinstance(row, dict) and row.get("headword"):
                found.setdefault(row["headword"], row.get("zipf"))
    return found


def as_written(lang, word, table):
    """The spelling Wiktionary has the word under: German nouns get their capital, and a word that is a lemma both
    ways keeps the lower case."""
    own = [s for s, (flags, _) in lemma_gate.known(lang, word) if set(flags) & set("LC")]
    lower = [s for s in own if s == s.lower()]
    return (lower or own or [word])[0]


def written_v4(lang):
    """The headwords that have an entry of the present shape, written or mended."""
    return {f.stem.lower() for f in lemma_gate.OUT.glob(f"*-v4/{lang}/*.json") if not f.name.endswith(".meta.json")}


def healthy(lang, floor=3.0, older=False):
    """The words worth a call, the doubtful ones and why the rest were dropped: (rows, rows with "why", Counter).

    With older, the words to write again and no others: those whose only entry is of the shape before
    schema 4. The owner decided on 18 November 2026 that they are written afresh and not mended, a mend
    costing nine tenths of a new entry and keeping the older, narrower definitions. They pass the same
    gate as a new word, whatever their frequency: an older entry for an inflected form sends its base word.

    Candidates are every unwritten headword of the language's lists, every spelling wordfreq gives the floor or
    more, and the base words of the inflected forms among them, which get the frequency of their forms. A healthy
    word is one Wiktionary has as a word of its own that is not only a name, a spelling or a piece of a word:
    the kinds the writers refused once in a hundred. The doubtful list holds what they refused a quarter of the
    time or more and may still be a word."""
    from collections import Counter
    from wordfreq import iter_wordlist, zipf_frequency

    table, have = lemma_gate.load(lang), written_v4(lang) if older else written(lang)
    weight, seen = {}, {}
    candidates = dict.fromkeys(sorted(v3_entries.headwords(lang))) if older else dict(queued(lang))
    floor, name = (0.0, "rewrite") if older else (floor, "healthy")
    for word in [] if older else iter_wordlist(lang):
        zipf = zipf_frequency(word, lang)
        if zipf < floor:
            break
        if WORD.fullmatch(word):
            candidates.setdefault(word, zipf)
    unpointed = {}
    for word in list(candidates):  # Russian print: "ее" is the word "её" and "идет" the form "идёт", and its frequency is theirs
        if lang in UNPOINTED and "L" not in table.get(word, ("",))[0] and "F" not in table.get(word, ("",))[0]:
            with_letter = pointed(lang, word, table)
            lemmas = [w for w in with_letter if "L" in table[w][0]]
            if lemmas or with_letter:
                unpointed[word] = (lemmas or with_letter)[0]
    for word, zipf in candidates.items():
        zipf = zipf if isinstance(zipf, (int, float)) else zipf_frequency(word, lang)
        target = unpointed.get(word, word)
        found = lemma_gate.known(lang, target)
        flags = "".join(entry[0] for _, entry in found)
        if flags and not set(flags) & set("LCNAPS") and "F" in flags:  # only a form: its frequency goes to its base word
            aims = [aim for _, entry in found for aim in entry[1]]
            if aims:  # "va" is a form of aller and of a verb of Louisiana: the one met most as itself takes it
                best = max(zipf_frequency(aim, lang) for aim in aims)
                for aim in {as_written(lang, a, table) for a in aims if zipf_frequency(a, lang) == best}:
                    weight[aim] = weight.get(aim, 0) + 10 ** zipf
            seen[target] = max(seen.get(target, 0), zipf)
            continue
        target = as_written(lang, target, table)
        weight[target] = weight.get(target, 0) + 10 ** zipf
        seen[target] = max(seen.get(target, 0), zipf)
    good, doubt, dropped = [], [], Counter()
    for word, mass in weight.items():
        zipf = round(math.log10(mass), 2)
        if word.lower() in have or lemma_gate.covered(lang, word):
            dropped["has an entry already"] += 1
            continue
        if not WORD.fullmatch(word) or not a_word(lang, word):
            dropped["not a word (letter, abbreviation, sign)"] += 1
            continue
        why = lemma_gate.unfit(lang, word)
        if why is None and lang in FORM_TOO_NEEDS and word in seen:
            flags = "".join(entry[0] for _, entry in lemma_gate.known(lang, word))
            bases = [a for _, entry in lemma_gate.known(lang, word) for a in entry[1]]
            own = set().union(*(lemma_gate.kinds(lang).get(s, frozenset()) for s, _ in lemma_gate.known(lang, word))) - {"name"}
            if ("F" in flags and bases and seen[word] - max(zipf_frequency(b, lang) for b in bases) < FORM_TOO_NEEDS[lang]
                    and "verb" not in own and len(own) < 2):
                why = "a form with one sense of its own"
        if why is None and word != word.lower() and word.lower() in table and "L" not in table[word.lower()][0] and table[word.lower()][1]:
            base = max(zipf_frequency(b, lang) for b in table[word.lower()][1])
            if seen.get(word, zipf) - base < 1.0:  # "Habe" is met as "habe", of haben; "Tag" is not met as a form of tagen
                zipf = min(zipf, LENT_FREQUENCY)
        if why is None:
            if zipf >= floor or word in candidates:
                good.append({"headword": word, "zipf": zipf, "set": f"{name}-{lang}"})
            else:
                dropped["base word of a form, below the floor"] += 1
        elif why in ("not in Wiktionary", "alternative spelling", "only old, rare or slang senses", "a form with one sense of its own"):
            doubt.append({"headword": word, "zipf": zipf, "set": f"{'rewrite-' if older else ''}doubtful-{lang}", "why": why})
        else:
            dropped[why] += 1
    return sorted(good, key=lambda r: -r["zipf"]), sorted(doubt, key=lambda r: -r["zipf"]), dropped


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--langs", required=True)
    ap.add_argument("--floor", type=float, default=3.0)
    ap.add_argument("--forms-too", action="store_true", help="keep the words that are inflected forms as well")
    ap.add_argument("--forms-only", action="store_true", help="only those words, as headwords/core-forms-<lang>.json")
    ap.add_argument("--dry", action="store_true", help="count and show, write nothing")
    ap.add_argument("--healthy", action="store_true", help="headwords/healthy-<lang>.json and doubtful-<lang>.json from every list and the frequency list")
    ap.add_argument("--rewrite", action="store_true", help="headwords/rewrite-<lang>.json: the words whose only entry is of an older shape, to be written "
                    "afresh with claude_code_writer.py --headwords rewrite --rewrite-older")
    a = ap.parse_args()
    for lang in a.langs.split(",") if a.healthy or a.rewrite else []:
        good, doubt, dropped = healthy(lang, a.floor, older=a.rewrite)
        kind, doubtful = ("rewrite", "rewrite-doubtful") if a.rewrite else ("healthy", "doubtful")
        print(f"{lang}: {len(good)} {'to write again' if a.rewrite else 'healthy'}, {len(doubt)} doubtful; dropped {dict(dropped.most_common())}")
        print(f"    first: {', '.join(r['headword'] for r in good[:30])}")
        if not a.dry:
            (HERE / "headwords" / f"{kind}-{lang}.json").write_text(json.dumps(good, ensure_ascii=False, indent=1) + "\n")
            (HERE / "headwords" / f"{doubtful}-{lang}.json").write_text(json.dumps(doubt, ensure_ascii=False, indent=1) + "\n")
    for lang in [] if a.healthy or a.rewrite else a.langs.split(","):
        name = f"core-{lang}.json"
        if lang == "ja":
            rows = japanese(a.floor)
        elif a.forms_only:
            plain = {r["headword"] for r in core(lang, a.floor)}
            rows, name = [r for r in core(lang, a.floor, True) if r["headword"] not in plain], f"core-forms-{lang}.json"
        else:
            rows = core(lang, a.floor, a.forms_too)
        print(f"{lang}: {len(rows)} words at Zipf {a.floor} or above with no entry; first: {', '.join(r['headword'] for r in rows[:25])}")
        if not a.dry:
            (HERE / "headwords" / name).write_text(json.dumps(rows, ensure_ascii=False, indent=1) + "\n")


if __name__ == "__main__":
    main()
