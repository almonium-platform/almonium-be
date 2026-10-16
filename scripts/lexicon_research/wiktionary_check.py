"""Compare our register labels with Wiktionary's (kaikki.org per-word records) at word level, per language."""
import json, glob, random, sys, time, urllib.request, urllib.parse
from collections import Counter, defaultdict
from pathlib import Path
S = Path(sys.argv[1]); OUT = Path("scripts/lexicon_research/out/cc-claude-opus-5-5"); CACHE = S / "kaikki"
NAMES = {"en": "English", "de": "German", "uk": "Ukrainian", "fr": "French", "es": "Spanish", "it": "Italian", "pt": "Portuguese"}
FINE = {"archaic": "archaic", "obsolete": "archaic", "dated": "dated", "literary": "literary", "poetic": "literary", "formal": "formal", "informal": "informal",
        "colloquial": "informal", "slang": "slang", "vulgar": "vulgar", "offensive": "offensive", "derogatory": "derogatory", "pejorative": "derogatory",
        "humorous": "humorous", "jocular": "humorous", "euphemistic": "euphemistic"}
SIX = {"archaic": "dated", "dated": "dated", "literary": "literary", "formal": "formal", "informal": "informal", "slang": "informal", "vulgar": "vulgar", "offensive": "vulgar"}
random.seed(7)
def fetch(lang, word):
    f = CACHE / lang / (word + ".jsonl")
    if f.exists(): return f.read_text()
    f.parent.mkdir(parents=True, exist_ok=True)
    q = urllib.parse.quote(word); url = f"https://kaikki.org/dictionary/{NAMES[lang]}/meaning/{urllib.parse.quote(word[0])}/{urllib.parse.quote(word[:2])}/{q}.jsonl"
    for i in range(3):
        try:
            with urllib.request.urlopen(urllib.request.Request(url, headers={"User-Agent": "almonium-lexicon-check/1 (register comparison)"}), timeout=30) as r:
                t = r.read().decode(); f.write_text(t); time.sleep(0.25); return t
        except urllib.error.HTTPError as e:
            if e.code == 404: f.write_text(""); time.sleep(0.25); return ""
            time.sleep(5 * (i + 1))
        except Exception: time.sleep(5 * (i + 1))
    return None
report = {}
for lang in (sys.argv[2:] or NAMES):
    ours = {}
    for p in glob.glob(f"{OUT}/{lang}/*.json"):
        if p.endswith(".meta.json"): continue
        try: e = json.load(open(p))
        except Exception: continue
        if not e.get("lexemes"): continue
        regs = {s.get("register") for lx in e["lexemes"] for s in lx.get("senses", [])} - {None, "neutral"}
        ours[e["headword"]] = regs
    flagged = [w for w, r in ours.items() if r]; plain = [w for w, r in ours.items() if not r]
    sample = random.sample(flagged, min(300, len(flagged))) + random.sample(plain, min(200, len(plain)))
    c = Counter(); pairs = Counter(); fine_words = {}
    for i, w in enumerate(sample):
        t = fetch(lang, w)
        if t is None: c["fetch_failed"] += 1; continue
        if not t: c["not_in_wiktionary"] += 1; continue
        wk = set()
        for line in t.splitlines():
            try: d = json.loads(line)
            except Exception: continue
            if d.get("lang_code") != lang: continue
            for s in d.get("senses", []): wk |= {FINE[x] for x in (s.get("tags") or []) if x in FINE}
        c["compared"] += 1; fine_words[w] = sorted(wk)
        wk6 = {SIX[x] for x in wk if x in SIX}; mine = ours[w]
        if wk: c["wk_flagged"] += 1; c["wk_flagged_we_flagged"] += bool(mine)
        if wk6: c["wk6"] += 1; c["wk6_label_match"] += bool(wk6 & mine)
        if mine: c["we_flagged"] += 1; c["we_flagged_wk_flagged"] += bool(wk); c["we_flagged_wk_label_match"] += bool(wk6 & mine)
        if not wk and not mine: c["both_neutral"] += 1
        for m in mine:
            for x in (wk6 or {"neutral"}): pairs[(m, x)] += 1
        if i % 100 == 0: print(lang, i, "of", len(sample), dict(c), flush=True)
    report[lang] = {"counts": dict(c), "pairs": {f"{a}->{b}": n for (a, b), n in pairs.most_common(20)}, "fine": fine_words}
    print("==", lang, json.dumps(report[lang]["counts"]), flush=True); print("   ours->wiktionary pairs:", report[lang]["pairs"], flush=True)
    json.dump(report, open(S / "wiktionary-report.json", "w"), ensure_ascii=False, indent=1)
