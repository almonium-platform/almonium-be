"""One word from Wiktionary evidence to a publishable entry, each step either free or paid once.

    evidence  Wiktionary's record for the headword (wiktionary.py), cached
    write     the writer drafts the entry from it, citing evidence senses (grounded_writer.py); a draft already
              written for the same arm and writer is reused, never bought again
    repair    structural slips with one reading are corrected in code (entry_repair.py)
    fill      the regions a sense's citations settle are written in (evidence_checks.fill)
    settle    fact disagreements with the evidence go to one patch call; only patches that verify land
              (patch_facts.py)
    rewrite   an entry that still has a structural error, or covers none of the evidence's common lexemes, is
              written once more with its problems listed, then repaired, filled and settled again
    verdict   "ready" when no structural error and no fact disagreement is left, "disputed" when only facts the
              settling kept are left (kept, with the reason, for a human or a stronger model), "held" otherwise

Judgments (sense granularity, wording, which rare sense to keep) are never reviewed here: the writer's choice
stands, and coverage is only counted. The ledger out/pipeline/<run>/ledger.jsonl has one line per word with every
step's cost, so a run's dollars per 1,000 words are read from it, not estimated.

Usage: entry_pipeline.py --words FILE --run NAME [--writer claude-haiku-5-5] [--fixer claude-haiku-5-5]
                         [--budget-usd 1] [--workers 6]
"""

import argparse
import json
import sys
import threading
from concurrent.futures import ThreadPoolExecutor
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
from wordfreq import zipf_frequency

import entry_checks
import entry_repair
import evidence_checks
import grounded_writer
import patch_facts
import prompting
import wiktionary

HERE = Path(__file__).resolve().parent
ARM = "v5r-evidence"
_lock = threading.Lock()


def structural(entry):
    return [f for f in entry_checks.check(entry) if f["severity"] == "error"]


def needs_rewrite(entry, found):
    if "error" in entry:
        return ["the draft was not valid JSON"]
    problems = [f"{f['path']}: {f['message']}" for f in structural(entry)]
    common = [
        lexeme
        for lexeme in found["lexemes"]
        if lexeme["pos"] != "PHRASE"
        and any(not evidence_checks.UNCOMMON & set(sense["register"]) for sense in lexeme["senses"])
    ]
    if common and not entry.get("lexemes"):
        problems.append("the entry has no lexeme although the evidence has common ones")
    problems += [
        f"{f['path']}: {f['message']}" for f in evidence_checks.compare(entry, found) if f["check"] in ("merged", "inflection")
    ]
    return problems


def rewrite(writer, lang, headword, found, problems, effort):
    system = prompting.system_prompt(lang, grounded_writer.ARMS[ARM][0])
    user = prompting.user_prompt(
        lang,
        {"headword": headword, "zipf": round(zipf_frequency(headword, lang), 2)},
        wiktionary.render(found),
        remind=True,
    )
    user += "\n\nA first draft had these problems. Write the whole entry again without them:\n- " + "\n- ".join(
        problems
    )
    text, meta = grounded_writer.call_openrouter(writer, system, user, effort)
    try:
        return grounded_writer.parse(text), meta["usd"] or 0
    except ValueError:
        return {"error": "unparsable"}, meta["usd"] or 0


def refine(entry, found, fixer, effort):
    """repair, fill and settle; the entry and what settling cost and decided."""
    if "error" in entry:
        return entry, {}, 0.0
    entry, repairs = entry_repair.repair(entry)
    entry, fills = evidence_checks.fill(entry, found)
    entry, report, usd = patch_facts.settle(entry, found, fixer, effort)
    report.update({"repairs": len(repairs), "fills": len(fills)})
    with grounded_writer._lock:
        grounded_writer._spent += usd
    return entry, report, usd


def run_one(run, writer, fixer, effort, row, budget):
    lang, headword = row["language"], row["headword"]
    out = HERE / "out" / "pipeline" / run / lang / f"{headword}.json"
    if out.exists():
        return None
    with grounded_writer._lock:
        if grounded_writer._spent >= budget:
            return {"language": lang, "headword": headword, "verdict": "budget"}
    found = wiktionary.evidence(lang, headword)
    if not found["lexemes"] and found["form_of"]:
        # An inflected form is not an entry of its own: the reader is sent to its lemma, nothing is written.
        line = {
            "language": lang,
            "headword": headword,
            "verdict": "inflected",
            "lemma": wiktionary.lemma_of(found),
            "rewritten": False,
            "cost": {"write": 0.0, "settle": 0.0, "rewrite": 0.0},
            "settle": {},
            "problems": [],
            "disputed": [],
        }
        out.parent.mkdir(parents=True, exist_ok=True)
        out.write_text(json.dumps({"form_of": found["form_of"], "lemma": line["lemma"]}, ensure_ascii=False))
        with _lock, (HERE / "out" / "pipeline" / run / "ledger.jsonl").open("a") as ledger:
            ledger.write(json.dumps(line, ensure_ascii=False) + "\n")
        return line
    status = grounded_writer.write_one(ARM, writer, row, budget, effort)
    draft_path = HERE / "out" / "grounded" / ARM / f"{writer.replace('/', '--')}-{effort}" / lang / f"{headword}.json"
    if not draft_path.exists():
        return {"language": lang, "headword": headword, "verdict": "held", "why": f"write {status}"}
    draft_meta = json.loads(draft_path.with_name(f"{headword}.meta.json").read_text())
    cost = {"write": draft_meta.get("usd") or 0, "settle": 0.0, "rewrite": 0.0}
    entry, report, usd = refine(json.loads(draft_path.read_text()), found, fixer, effort)
    cost["settle"] += usd
    problems = needs_rewrite(entry, found)
    rewritten = False
    if problems:
        draft, usd = rewrite(writer, lang, headword, found, problems, effort)
        cost["rewrite"] += usd
        with grounded_writer._lock:
            grounded_writer._spent += usd
        entry, report, usd = refine(draft, found, fixer, effort)
        cost["settle"] += usd
        rewritten = True
        problems = needs_rewrite(entry, found)
    if "error" in entry:
        facts = []
    elif found["lexemes"]:
        facts = [f for f in evidence_checks.compare(entry, found) if f["kind"] == "fact"]
    else:
        facts = evidence_checks.compare_without_evidence(entry, lambda word: wiktionary.evidence("de", word))
    rejected = "error" not in entry and not entry.get("lexemes") and entry.get("reject")
    if problems:
        verdict = "held"
    elif rejected:
        verdict = "rejected"  # the writer said there is no entry to write (a name, a misspelling)
    elif facts:
        verdict = "disputed"
    else:
        # With no record nothing could disagree: the entry stands on the writer's word alone.
        verdict = "ready" if found["lexemes"] else "unverified"
    out.parent.mkdir(parents=True, exist_ok=True)
    out.write_text(json.dumps(entry, ensure_ascii=False, indent=1))
    line = {
        "language": lang,
        "headword": headword,
        "verdict": verdict,
        "rewritten": rewritten,
        "cost": cost,
        "settle": {k: v for k, v in report.items() if k != "ops"},
        "problems": problems,
        "disputed": [f"{f['check']} {f['path']}: {f['message']}" for f in facts],
        # saw is also the past of see: the lookup reaches see through its forms only if see is written too.
        "also_form_of": wiktionary.lemmas_of(found),
    }
    with _lock, (HERE / "out" / "pipeline" / run / "ledger.jsonl").open("a") as ledger:
        ledger.write(json.dumps(line, ensure_ascii=False) + "\n")
    return line


def main():
    global ARM
    parser = argparse.ArgumentParser()
    parser.add_argument("--words", required=True)
    parser.add_argument("--run", required=True)
    parser.add_argument("--writer", default="claude-haiku-5-5")
    parser.add_argument("--fixer", default="claude-haiku-5-5")
    parser.add_argument("--effort", default="low")
    parser.add_argument("--budget-usd", type=float, default=1.0)
    parser.add_argument("--arm", default=ARM, help="the writer arm whose drafts are used or written")
    parser.add_argument("--workers", type=int, default=6)
    args = parser.parse_args()
    grounded_writer.VIA = "openrouter"
    ARM = args.arm
    rows = grounded_writer.words(args.words)
    with ThreadPoolExecutor(args.workers) as pool:
        list(pool.map(lambda row: run_one(args.run, args.writer, args.fixer, args.effort, row, args.budget_usd), rows))
    ledger = HERE / "out" / "pipeline" / args.run / "ledger.jsonl"
    rows = [json.loads(line) for line in ledger.read_text().splitlines()] if ledger.exists() else []
    verdicts = {}
    for row in rows:
        verdicts[row["verdict"]] = verdicts.get(row["verdict"], 0) + 1
    total = sum(sum(row["cost"].values()) for row in rows)
    print(
        f"{len(rows)} words: {verdicts}; rewritten {sum(r['rewritten'] for r in rows)}; "
        f"${total:.3f} in all, ${total / max(len(rows), 1) * 1000:.2f} per 1,000 words"
    )
    lemmas = sorted({(r["language"], lemma) for r in rows for lemma in r.get("also_form_of") or [r.get("lemma")] if lemma})
    if lemmas:
        print("lemmas to write so their forms are found:", ", ".join(f"{lang}/{lemma}" for lang, lemma in lemmas))
    for row in rows:
        if row["verdict"] != "ready":
            print(
                f"  {row['verdict']:8} {row['language']}/{row['headword']}: "
                f"{'; '.join(row['problems'] + row['disputed'])[:220]}"
            )


if __name__ == "__main__":
    main()
