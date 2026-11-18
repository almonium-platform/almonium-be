"""The common words of a language that nobody has written yet: headwords/core-<lang>.json.

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

Usage: core_list.py --langs ru [--floor 3.0] [--forms-too | --forms-only] [--dry]   (run from the backend repository root)
"""
import argparse, json, math, re, sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
import lemma_gate  # noqa: E402
from prompting import HERE  # noqa: E402

WORD = re.compile(r"[^\W\d_]+(?:[-'’][^\W\d_]+)*")
UNPOINTED = {"ru": ("е", "ё")}  # the letter print uses, the letter the lemma has
ALPHABET = {"ru": re.compile(r"[а-яё]+(?:-[а-яё]+)*")}
VOWELS = {"ru": set("аеёиоуыэюя")}
ONE_LETTER = {"ru": set("авижкосуяэ")}  # а, в, и, ж, к, о, с, у, я, э: the rest are names of letters and abbreviations


def a_word(lang, word):
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


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--langs", required=True)
    ap.add_argument("--floor", type=float, default=3.0)
    ap.add_argument("--forms-too", action="store_true", help="keep the words that are inflected forms as well")
    ap.add_argument("--forms-only", action="store_true", help="only those words, as headwords/core-forms-<lang>.json")
    ap.add_argument("--dry", action="store_true", help="count and show, write nothing")
    a = ap.parse_args()
    for lang in a.langs.split(","):
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
