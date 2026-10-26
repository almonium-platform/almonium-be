"""Score written entries against the calibration set's known facts, one narrow question per fact.

For each calibration case a writer has an entry for, a judge model reads the entry and the case's facts and says of
each fact whether the entry states it (met), leaves it out (missed) or says the opposite (contradicted). Facts are
claims about the correct entry ("the verb has two opposite meanings..."), so the judge needs no knowledge of its own:
it reads, it does not rule. Verdicts are kept per writer under out/grounded/<arm>/<writer>.calibration.jsonl and a
case already judged is not judged again.

Usage: calibration_judge.py --arm v5-evidence --writers a,b [--judge claude-sonnet-5-5] [--budget-usd 1]
"""

import argparse
import json
import sys
from concurrent.futures import ThreadPoolExecutor
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
import grounded_writer

HERE = Path(__file__).resolve().parent
CASES = json.loads((HERE / "calibration_cases.json").read_text())["cases"]
PROMPT = """You check a learner-dictionary entry against a list of facts about the word. For each fact, answer
"met" if the entry states it (in any wording, field or structure: a region field, a separate lexeme, a sense),
"missed" if the entry does not state it, or "contradicted" if the entry states the opposite. Facts about a defect in
an earlier entry ("the gloss was written with a Latin letter") are met when this entry does not repeat the defect.
Judge only what the entry says; do not add facts of your own.
Return JSON only: {"verdicts": [{"fact": <index>, "verdict": "met"|"missed"|"contradicted", "why": <short>}]}"""


def judge_case(judge, arm, writer, case, budget):
    lang, headword = case["language"], case["headword"]
    path = HERE / "out" / "grounded" / arm / writer / lang / f"{headword}.json"
    if not path.exists():
        return None
    entry = json.loads(path.read_text())
    facts = "\n".join(f"{i}. [{fact['facet']}] {fact['claim']}" for i, fact in enumerate(case["facts"]))
    user = f"Facts:\n{facts}\n\nEntry:\n{json.dumps(entry, ensure_ascii=False)}"
    with grounded_writer._lock:
        if grounded_writer._spent >= budget:
            return None
    text, meta = grounded_writer.call_openrouter(judge, PROMPT, user, "low")
    with grounded_writer._lock:
        grounded_writer._spent += meta["usd"] or 0
    try:
        verdicts = grounded_writer.parse(text)["verdicts"]
    except (ValueError, KeyError):
        verdicts = []
    return {"id": case["id"], "kind": case["kind"], "facts": case["facts"], "verdicts": verdicts, "usd": meta["usd"]}


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--arm", default="v5-evidence")
    parser.add_argument("--writers", required=True)
    parser.add_argument("--judge", default="claude-sonnet-5-5")
    parser.add_argument("--budget-usd", type=float, default=1.0)
    args = parser.parse_args()
    for writer in args.writers.split(","):
        out = HERE / "out" / "grounded" / args.arm / f"{writer}.calibration.jsonl"
        done = {json.loads(line)["id"] for line in out.read_text().splitlines()} if out.exists() else set()
        todo = [case for case in CASES if case["id"] not in done]
        with ThreadPoolExecutor(4) as pool:
            results = list(pool.map(lambda c, w=writer: judge_case(args.judge, args.arm, w, c, args.budget_usd), todo))
        with out.open("a") as file:
            for result in filter(None, results):
                file.write(json.dumps(result, ensure_ascii=False) + "\n")
        rows = [json.loads(line) for line in out.read_text().splitlines()] if out.exists() else []
        tally = {}
        for row in rows:
            for verdict in row["verdicts"]:
                tally[verdict["verdict"]] = tally.get(verdict["verdict"], 0) + 1
        missed = [
            f"{row['id']}#{v['fact']} {v['verdict']}: {v.get('why', '')[:90]}"
            for row in rows
            for v in row["verdicts"]
            if v["verdict"] != "met"
        ]
        print(f"== {writer}: {len(rows)} cases, {tally}")
        for line in missed:
            print("   ", line)
    print(f"judge spent ${grounded_writer._spent:.3f}")


if __name__ == "__main__":
    main()
