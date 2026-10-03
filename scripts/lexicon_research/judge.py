"""Jev as the judge of the writer bake-off.

For every headword, every pair of models' entries is shown to Jev as A and B (order random) with
one Choice question: which is the better learner's dictionary entry, with the criteria the owner
judges by. Jev returns the pick, a probability per option and a confidence; no text is written, so
nothing can be malformed. A pair is also asked whether either entry has a visible factual slip.

Output: out/judge.jsonl (one line per pair) and out/judge-report.md (wins per model, mean
confidence, the low-confidence pairs for a person, and agreement with the owner's blind picks if
out/blind-results.txt exists in the "lang/headword best: A" format the blind page copies out).

Usage: judge.py [--only-blind] [--limit N]   (run from the backend repository root)
"""
import argparse, itertools, json, random, sys, time
from collections import defaultdict
from pathlib import Path

import requests

sys.path.insert(0, "scripts")
from tts_audition import ROOT

HERE = Path("scripts/lexicon_research")
OUT = HERE / "out"
KEY = next(l.split("=", 1)[1].strip() for l in (ROOT / ".env").read_text().splitlines() if l.startswith("JEV_API_KEY="))
URL = "https://jevtypesafeai.com/api/v1/decide"  # the gateway the owner's account is on; TypeSafe's own endpoint is api.typesafe.ai/v1/systemone
LANGS = ("en", "de", "uk")

INSTRUCTIONS = ("Two candidate entries, A and B, for the same headword in a learner's dictionary. Judge which is the better entry "
                "for an adult learning the language, weighing: senses are correct, complete for ordinary reading, and split "
                "neither too fine nor too coarse; definitions are plain, short and not circular; glosses are the exact word in "
                "each language; examples are natural; the IPA and the forms are correct; the whole reads consistently. "
                "A rejected entry (no lexemes) loses unless the headword really is not a lemma.")


def render(e):
    """A compact plain-text view of an entry, the same for every model, so that formatting cannot sway the judge."""
    if not e.get("lexemes"):
        return f"REJECTED: {e.get('reject', '')}"
    out = []
    for lx in e["lexemes"]:
        out.append(f"{lx['lemma']} [{lx['pos']}] grammar={json.dumps(lx.get('grammar', {}), ensure_ascii=False)}")
        out.append("  ipa: " + "; ".join(f"{p.get('variety')} {p.get('ipa')}" for p in lx.get("pronunciations", [])))
        out.append("  forms: " + ", ".join(f"{f.get('form')}({','.join(f'{k}={v}' for k, v in (f.get('features') or {}).items())})" for f in lx.get("forms", [])))
        for i, s in enumerate(lx.get("senses", []), 1):
            gl = ", ".join(f"{k}={v if isinstance(v, str) else (v or {}).get('text', '') + ('' if (v or {}).get('exact', True) else '~')}" for k, v in (s.get("glosses") or {}).items())
            out.append(f"  {i}. {s.get('definition')} [{s.get('cefr')}, {s.get('register')}] glosses: {gl}")
            out.append(f"     example: {s.get('example')}")
            out.append(f"     cues: {' | '.join(c.get('text', '') for c in s.get('cues', []))}")
        if lx.get("relations"):
            out.append("  relations: " + ", ".join(f"{r.get('type')}:{r.get('lemma')}" for r in lx["relations"]))
    return "\n".join(out)


def ask(lang, headword, a_text, b_text):
    body = {"state": {"language": lang, "headword": headword, "entry_A": a_text, "entry_B": b_text},
            "questions": {
                "better": {"type": "choice", "instructions": INSTRUCTIONS,
                           "criteria": {"A": "entry A is the better dictionary entry", "B": "entry B is the better dictionary entry", "same": "the two are of equal quality"}},
                "a_error": {"type": "noul", "instructions": "Does entry A contain a visible factual error: a wrong IPA, a wrong gloss, a wrong inflected form, or a sense that does not exist?",
                            "criteria": {"true": "yes, there is at least one clear error", "false": "no clear error"}},
                "b_error": {"type": "noul", "instructions": "Does entry B contain a visible factual error: a wrong IPA, a wrong gloss, a wrong inflected form, or a sense that does not exist?",
                            "criteria": {"true": "yes, there is at least one clear error", "false": "no clear error"}}}}
    for i in range(4):
        r = requests.post(URL, headers={"Authorization": f"Bearer {KEY}", "Content-Type": "application/json"}, json=body, timeout=120)
        if r.status_code == 429:
            time.sleep(5 * (i + 1))
            continue
        break
    if not r.ok:
        return {"error": f"HTTP {r.status_code} {r.text[:200]}"}
    return r.json()


ap = argparse.ArgumentParser()
ap.add_argument("--only-blind", action="store_true", help="judge only the headwords on the blind page")
ap.add_argument("--limit", type=int, default=0)
a = ap.parse_args()
random.seed(7)

models = sorted(p.name for p in OUT.iterdir() if p.is_dir())
blind = json.loads((OUT / "blind-key.json").read_text()) if (OUT / "blind-key.json").exists() else {}
done = set()
log = OUT / "judge.jsonl"
if log.exists():
    for line in log.read_text().splitlines():
        j = json.loads(line)
        done.add((j["lang"], j["headword"], j["model_a"], j["model_b"]))

pairs = []
for lang in LANGS:
    heads = sorted({f.stem for m in models for f in (OUT / m / lang).glob("*.json") if not f.name.endswith(".meta.json")}) if (OUT / models[0] / lang).exists() else []
    for h in heads:
        if a.only_blind and f"{lang}/{h}" not in blind:
            continue
        entries = {}
        for m in models:
            f = OUT / m / lang / f"{h}.json"
            if f.exists():
                e = json.loads(f.read_text())
                if "error" not in e:
                    entries[m] = e
        for m1, m2 in itertools.combinations(sorted(entries), 2):
            if (lang, h, m1, m2) not in done:
                pairs.append((lang, h, m1, m2, entries[m1], entries[m2]))
if a.limit:
    pairs = pairs[:a.limit]
print(f"{len(pairs)} pairs to judge", flush=True)

with log.open("a") as out:
    for n, (lang, h, m1, m2, e1, e2) in enumerate(pairs, 1):
        flip = random.random() < 0.5
        a_m, b_m = (m2, m1) if flip else (m1, m2)
        a_e, b_e = (e2, e1) if flip else (e1, e2)
        res = ask(lang, h, render(a_e), render(b_e))
        row = {"lang": lang, "headword": h, "model_a": m1, "model_b": m2, "shown_a": a_m, "shown_b": b_m}
        if "error" in res:
            row["error"] = res["error"]
        else:
            ans = res["answers"]
            pick = ans["better"]["choice"]
            row.update({"winner": {"A": a_m, "B": b_m}.get(pick, "same"), "confidence": ans["better"].get("confidence"),
                        "probabilities": ans["better"].get("probabilities"),
                        "error_" + a_m: ans["a_error"].get("noul"), "error_" + b_m: ans["b_error"].get("noul"),
                        "usage": res.get("usage"), "cost_usd": (res.get("usage") or {}).get("cost_usd"), "jev_model": res.get("model")})
        out.write(json.dumps(row, ensure_ascii=False) + "\n")
        out.flush()
        print(f"{n}/{len(pairs)} {lang} {h:20} {row.get('winner', row.get('error', '?'))[:40]} conf={row.get('confidence')}", flush=True)

# ---- report
rows = [json.loads(l) for l in log.read_text().splitlines()]
rows = [r for r in rows if "winner" in r]
wins, games, conf, errs = defaultdict(lambda: defaultdict(int)), defaultdict(lambda: defaultdict(int)), [], defaultdict(list)
for r in rows:
    for m in (r["model_a"], r["model_b"]):
        games[r["lang"]][m] += 1
        if r.get(f"error_{m}") is not None:
            errs[m].append(r[f"error_{m}"])
    if r["winner"] != "same":
        wins[r["lang"]][r["winner"]] += 1
    conf.append(r.get("confidence") or 0)
lines = ["# Jev as judge", "", f"{len(rows)} pairs judged; mean confidence {sum(conf) / max(1, len(conf)):.2f}.", ""]
for lang in LANGS:
    if not games[lang]:
        continue
    lines += [f"## {lang}", "", "| Model | Pairs | Wins | Win rate |", "|---|---|---|---|"]
    for m in sorted(games[lang], key=lambda m: -wins[lang][m] / max(1, games[lang][m])):
        lines.append(f"| {m} | {games[lang][m]} | {wins[lang][m]} | {100 * wins[lang][m] / max(1, games[lang][m]):.0f}% |")
    lines.append("")
lines += ["## Mean probability of a visible factual error, per model", "", "| Model | Mean |", "|---|---|"]
for m in sorted(errs, key=lambda m: sum(errs[m]) / len(errs[m])):
    lines.append(f"| {m} | {sum(errs[m]) / len(errs[m]):.2f} |")
low = sorted((r for r in rows if (r.get("confidence") or 0) < 0.6), key=lambda r: r.get("confidence") or 0)[:20]
lines += ["", "## Lowest-confidence pairs, for a person", ""] + [f"- {r['lang']} {r['headword']}: {r['model_a']} vs {r['model_b']}, picked {r['winner']} at {r.get('confidence')}" for r in low]
owner = OUT / "blind-results.txt"
if owner.exists() and blind:
    agree = total = 0
    for line in owner.read_text().splitlines():
        if " best: " not in line:
            continue
        k, letter = line.split(" best: ")
        key = k.strip()
        if key not in blind:
            continue
        best = blind[key]["ABCDEFG".index(letter.strip())]
        lang, h = key.split("/", 1)
        for r in rows:
            if r["lang"] == lang and r["headword"] == h and best in (r["model_a"], r["model_b"]) and r["winner"] != "same":
                total += 1
                agree += r["winner"] == best
    lines += ["", f"## Agreement with the owner's blind picks", "", f"On pairs that include the owner's best entry, Jev picked the same entry in {agree} of {total} ({100 * agree / max(1, total):.0f}%)."]
(OUT / "judge-report.md").write_text("\n".join(lines))
print("\n".join(lines))
