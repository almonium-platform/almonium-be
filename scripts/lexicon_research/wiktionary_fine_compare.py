"""After relabelling: compare our fine register labels with Wiktionary's, word level, from the cached kaikki records."""
import json, glob, sys
from collections import Counter, defaultdict
from pathlib import Path
S = Path(sys.argv[1]); OUT = Path("scripts/lexicon_research/out/cc-claude-opus-5-5")
FINE = {"archaic": "archaic", "obsolete": "archaic", "dated": "dated", "literary": "literary", "poetic": "literary", "formal": "formal", "informal": "informal",
        "colloquial": "informal", "slang": "slang", "vulgar": "vulgar", "offensive": "offensive", "derogatory": "derogatory", "pejorative": "derogatory",
        "humorous": "humorous", "jocular": "humorous", "euphemistic": "euphemistic"}
for lang in sys.argv[2:]:
    c = Counter(); pairs = Counter(); per_label = defaultdict(lambda: [0, 0, 0])  # label -> [we, wk, both]
    for f in (S / "kaikki" / lang).glob("*.jsonl"):
        w = f.stem; p = OUT / lang / f"{w}.json"; m = OUT / lang / f"{w}.meta.json"
        if not p.exists() or not m.exists() or json.loads(m.read_text()).get("register_version") != "v3": continue
        t = f.read_text()
        if not t: continue
        wk = set()
        for line in t.splitlines():
            try: d = json.loads(line)
            except Exception: continue
            if d.get("lang_code") != lang: continue
            for s in d.get("senses", []):
                tags = s.get("tags") or []
                if any(t in tags for t in ("form-of", "alt-of", "misspelling", "abbreviation")): continue  # an inflection or spelling entry, not a sense of the word
                wk |= {FINE[x] for x in tags if x in FINE}
        e = json.loads(p.read_text()); mine = {s.get("register") for lx in e.get("lexemes", []) for s in lx.get("senses", [])} - {None, "neutral", "technical"}
        c["compared"] += 1
        if wk: c["wk_flagged"] += 1; c["wk_flagged_we_flagged"] += bool(mine); c["wk_label_match"] += bool(wk & mine)
        if mine: c["we_flagged"] += 1; c["we_flagged_wk_flagged"] += bool(wk); c["we_label_match"] += bool(wk & mine)
        if not wk and not mine: c["both_neutral"] += 1
        for l in mine | wk: per_label[l][0] += l in mine; per_label[l][1] += l in wk; per_label[l][2] += l in mine and l in wk
        for a in (mine or {"neutral"}):
            for b in (wk or {"neutral"}): pairs[(a, b)] += 1
    print("==", lang, dict(c)); print("   per label (we, wiktionary, both):", {k: v for k, v in sorted(per_label.items())})
    print("   ours->wiktionary:", {f"{a}->{b}": n for (a, b), n in pairs.most_common(16)})
