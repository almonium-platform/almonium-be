"""Free checks on prompt-v4 entries, no model call: run it while a writer is going to see drift early.

For every entry under out/*-v4/<lang>/ it reports schema failures and the faults the papers found in
LLM-written dictionaries that a program can see: dictionary formulas in definitions ("the act of"),
the headword inside its own definition, definitions and examples outside their word limits, examples
that all open alike (share opening with an article), senses per lexeme and the share with three or more, a
missing stress reading where the language needs one (uk, it), varieties outside the language's list,
and output tokens and plan cost per entry from the meta files.

Usage: check_v4.py [--langs en,de] [--writer cc-claude-opus-5-5-v4] [--show 5]   (run from the backend repository root)
"""
import argparse, json, re, statistics, sys
from collections import Counter, defaultdict
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
import jsonschema  # noqa: E402
from prompting import HERE, REGIONS, SCHEMA_PATH  # noqa: E402

SCHEMA = json.loads(SCHEMA_PATH.read_text())
FORMULAS = re.compile(r"\b(the act of|the state of being|the process of|relating to|characteristic of|according to|"
                      r"der Vorgang des|das Tun|action de|le fait de|relatif à|el acto de|relativo a|l'atto di|relativo a|"
                      r"o ato de|relativo a|дія за значенням|стан за значенням|стосується)\b", re.I)
ARTICLES = {"en": {"the", "a", "an"}, "de": {"der", "die", "das", "ein", "eine"}, "fr": {"le", "la", "les", "un", "une", "l"},
            "es": {"el", "la", "los", "las", "un", "una"}, "it": {"il", "lo", "la", "i", "gli", "le", "un", "una", "l"},
            "pt": {"o", "a", "os", "as", "um", "uma"}, "uk": set()}
NEEDS_READING = {"uk", "it"}


def words(text):
    return re.findall(r"[^\W\d_]+(?:['’-][^\W\d_]+)*", text.lower())


def check(lang, entry):
    """Problems of one entry, as short strings."""
    problems = []
    try:
        jsonschema.validate(entry, SCHEMA)
    except jsonschema.ValidationError as e:
        problems.append(f"schema: {e.message[:90]}")
    allowed = set(REGIONS[lang])
    for lx in entry.get("lexemes", []):
        lemma = lx.get("lemma", "").lower()
        tags = [v for s in lx.get("spellings", []) for v in s.get("varieties", [])]
        tags += [v for f in lx.get("forms", []) for v in f.get("varieties", [])]
        tags += [v for s in lx.get("senses", []) for v in s.get("regions", [])]
        if bad := sorted(set(tags) - allowed):
            problems.append(f"variety outside {lang}: {', '.join(bad)}")
        if lang in NEEDS_READING and any(len(re.findall(r"[aeiouаеєиіїоуюяàèéìòóù]", lemma)) > 1 and not p.get("reading")
                                         for p in lx.get("pronunciations", [])):
            problems.append(f"{lx.get('lemma')}: no reading")
        for s in lx.get("senses", []):
            d = s.get("definition", "")
            if FORMULAS.search(d):
                problems.append(f"formula: {d[:60]}")
            if lemma and lemma in words(d):
                problems.append(f"headword in definition: {d[:60]}")
            if not 6 <= len(words(d)) <= 15:
                problems.append(f"definition of {len(words(d))} words")
            if not 6 <= len(words(s.get("example", ""))) <= 14:
                problems.append(f"example of {len(words(s.get('example', '')))} words")
    return problems


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--langs", default=",".join(REGIONS))
    ap.add_argument("--writer", default="")
    ap.add_argument("--show", type=int, default=5)
    a = ap.parse_args()
    for lang in a.langs.split(","):
        dirs = [d for d in (HERE / "out").glob(f"*-v4/{lang}") if not a.writer or d.parent.name == a.writer]
        files = [f for d in dirs for f in sorted(d.glob("*.json")) if not f.name.endswith(".meta.json")]
        if not files:
            continue
        stats, kinds, shown = Counter(), Counter(), defaultdict(list)
        out_tokens, costs, senses = [], [], []
        openings, past = Counter(), 0
        for f in files:
            entry = json.loads(f.read_text())
            meta_f = f.with_name(f.stem + ".meta.json")
            meta = json.loads(meta_f.read_text()) if meta_f.exists() else {}
            if meta.get("output_tokens"):
                out_tokens.append(meta["output_tokens"])
            if meta.get("total_cost_usd") or meta.get("cost_usd"):
                costs.append(meta.get("total_cost_usd") or meta.get("cost_usd"))
            if "error" in entry:
                stats["error"] += 1
                continue
            stats["rejected" if entry.get("reject") and not entry.get("lexemes") else "written"] += 1
            for lx in entry.get("lexemes", []):
                senses.append(len(lx.get("senses", [])))
                for s in lx.get("senses", []):
                    w = words(s.get("example", ""))
                    if w:
                        openings[w[0] in ARTICLES[lang]] += 1
            for p in check(lang, entry):
                kind = p.split(":")[0] if ":" in p else re.sub(r"\d+", "N", p)
                kinds[kind] += 1
                if len(shown[kind]) < a.show:
                    shown[kind].append(f"{f.stem}: {p}")
        n = sum(stats.values())
        print(f"\n{lang}: {n} entries {dict(stats)}; senses per lexeme {statistics.mean(senses) if senses else 0:.2f} "
              f"(share with 3+: {sum(1 for x in senses if x >= 3) / max(1, len(senses)):.0%}); "
              f"examples opening with an article {openings[True] / max(1, sum(openings.values())):.0%}")
        if out_tokens:
            print(f"    output tokens per entry: median {statistics.median(out_tokens):.0f}, mean {statistics.mean(out_tokens):.0f}"
                  + (f"; plan-equivalent cost per entry ${statistics.mean(costs):.4f}" if costs else ""))
        for kind, c in kinds.most_common():
            print(f"    {c:4} {kind}")
            for line in shown[kind]:
                print(f"         {line}")


if __name__ == "__main__":
    main()
