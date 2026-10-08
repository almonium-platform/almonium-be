"""The writers judge each other: every candidate model ranks the entries all of them wrote for the same word.

For each headword that every writer has an entry for, the entries are shown under shuffled labels
A, B, C, D (no model names) and the judge returns a ranking, a 1-10 score per entry and the
factual slips it sees. Each judge is run through the plan CLI it lives on, as a fresh headless
session with no tools, so the judging costs plan allowance and no API money. A judge also sees
its own entry, blind; the report says whether it ranked itself higher than the others did.

Output: out/cross/<lang>/<judge>.jsonl (one line per headword, with the label key) and
out/cross/<lang>/report.md.

Usage: cross_judge.py [--lang de] [--writers cc-claude-opus-5-5,...] [--judges opus,fable,sol,astra]
                      [--limit N] [--workers 3] [--report-only]   (run from the backend repository root)
"""
import argparse, json, os, random, subprocess, sys, time
from collections import defaultdict
from concurrent.futures import ThreadPoolExecutor
from pathlib import Path
from statistics import mean

sys.path.insert(0, "scripts/lexicon_research")
from rendering import render

HERE = Path("scripts/lexicon_research")
OUT = HERE / "out"
LABELS = "ABCD"
LANG_NAME = {"en": "English", "de": "German", "uk": "Ukrainian"}
# a judge's name -> the CLI it runs on and the model id that CLI knows
JUDGES = {"opus": ("claude", "claude-opus-5-5"), "fable": ("claude", "claude-fable-5-1"),
          "sol": ("codex", "gpt-6.1-sol"), "astra": ("codex", "gpt-6-astra")}
WRITERS = {"cc-claude-opus-5-5": "opus", "cc-claude-fable-5-1": "fable",
           "codex-gpt-6.1-sol-medium": "sol", "codex-gpt-6-astra-medium": "astra"}
CODEX = os.environ.get("CODEX_BIN") or next((str(p) for p in sorted(Path.home().glob(".nvm/versions/node/*/bin/codex"), reverse=True)), "codex")
EMPTY = Path("/tmp/claude-1000/lexicon-judge-empty")
EMPTY.mkdir(parents=True, exist_ok=True)

RUBRIC = ("You are the editor of a learner's dictionary of {language}. Below are {n} candidate entries, written independently, for the "
          "same headword. Rank them from best to worst for an adult learning {language}, weighing: senses are correct, complete for "
          "ordinary reading, and split neither too fine nor too coarse; definitions are plain, short and not circular; glosses are "
          "the exact word in each language; examples are natural and show the sense; the IPA and the inflected forms are correct; "
          "the CEFR level and register are sensible; the whole reads consistently. A rejected entry (no lexemes) ranks last unless "
          "the headword really is not a lemma. Judge the content, not the length: more senses is better only when they are real. "
          "A gloss followed by ~ was marked approximate by its writer; that mark is a flag, not a typo.\n\n"
          "Answer with JSON only, no prose around it: {{\"ranking\": [labels best to worst], \"scores\": {{label: 1-10}}, "
          "\"errors\": {{label: [each clear factual error in that entry, one short line, or an empty list]}}, "
          "\"note\": one sentence on what separated the top entry from the rest}}.")
SCHEMA = json.dumps({"type": "object", "additionalProperties": False, "required": ["ranking", "scores", "errors", "note"],
                     "properties": {"ranking": {"type": "array", "items": {"type": "string"}},
                                    "scores": {"type": "object", "additionalProperties": {"type": "integer"}},
                                    "errors": {"type": "object", "additionalProperties": {"type": "array", "items": {"type": "string"}}},
                                    "note": {"type": "string"}}})


def prompt(lang, headword, shown):
    parts = [RUBRIC.format(language=LANG_NAME[lang], n=len(shown)), f"Headword: {headword}"]
    for label, entry in shown:
        parts.append(f"=== Entry {label} ===\n{render(entry)}")
    return "\n\n".join(parts)


def parse(text):
    t = text.strip()
    if t.startswith("```"):
        t = t.split("\n", 1)[1].rsplit("```", 1)[0]
    return json.loads(t)


def ask_claude(model, text):
    cmd = ["claude", "-p", text, "--model", model, "--effort", "high", "--tools", "", "--json-schema", SCHEMA,
           "--output-format", "json", "--no-session-persistence", "--strict-mcp-config", "--mcp-config", "{\"mcpServers\": {}}"]
    r = subprocess.run(cmd, capture_output=True, text=True, timeout=600, cwd=EMPTY)
    res = json.loads(r.stdout)
    if res.get("is_error") and "limit" in (res.get("result") or "").lower():
        return None, {"limit": True}
    u = res.get("usage") or {}
    usage = {"cost_usd": res.get("total_cost_usd"), "input": u.get("input_tokens"), "output": u.get("output_tokens"),
             "cache_read": u.get("cache_read_input_tokens"), "cache_write": u.get("cache_creation_input_tokens")}
    answer = res.get("structured_output")
    if answer is None:
        answer = parse(res.get("result") or "")
    return answer, usage


def ask_codex(model, text):
    full = f"Do not run commands or read files; answer from your own knowledge.\n\n{text}"
    r = subprocess.run([CODEX, "exec", "-m", model, "--skip-git-repo-check", "--ephemeral", "--json", "--sandbox", "read-only",
                        "-c", "mcp_servers={}", "-c", "model_reasoning_effort=medium", full],
                       capture_output=True, text=True, timeout=900, cwd=EMPTY, stdin=subprocess.DEVNULL)
    answer_text, usage, error = "", {}, ""
    for line in r.stdout.splitlines():
        try:
            ev = json.loads(line)
        except json.JSONDecodeError:
            continue
        if ev.get("type") == "item.completed" and (ev.get("item") or {}).get("type") == "agent_message":
            answer_text = ev["item"].get("text", "")
        elif ev.get("type") == "turn.completed":
            u = ev.get("usage") or {}
            usage = {"input": u.get("input_tokens"), "cache_read": u.get("cached_input_tokens"),
                     "output": u.get("output_tokens"), "reasoning": u.get("reasoning_output_tokens")}
        elif ev.get("type") in ("error", "turn.failed"):
            error = str(ev.get("message") or ev.get("error"))[:300]
    if not answer_text and "usage limit" in error:
        return None, {"limit": True}
    if not answer_text:
        raise RuntimeError(error or "no agent message")
    return parse(answer_text), usage


def judge_one(judge, lang, headword, entries, log, lock):
    cli, model = JUDGES[judge]
    rng = random.Random(f"{lang}/{headword}/{judge}")
    order = sorted(entries)
    rng.shuffle(order)
    key = dict(zip(LABELS, order))  # label -> writer
    text = prompt(lang, headword, [(label, entries[key[label]]) for label in LABELS[:len(order)]])
    row = {"headword": headword, "judge": judge, "key": key}
    t0 = time.time()
    for attempt in (1, 2):
        try:
            answer, usage = (ask_claude if cli == "claude" else ask_codex)(model, text)
            if answer is None:
                row["error"] = "plan limit"
                break
            labels = set(key)
            if set(answer.get("ranking", [])) != labels or set(answer.get("scores", {})) != labels:
                raise ValueError(f"labels do not match: {answer.get('ranking')} {list(answer.get('scores', {}))}")
            row.update({"ranking": [key[l] for l in answer["ranking"]], "scores": {key[l]: int(v) for l, v in answer["scores"].items()},
                        "errors": {key[l]: v for l, v in (answer.get("errors") or {}).items() if l in key}, "note": answer.get("note", ""),
                        "usage": usage})
            break
        except Exception as e:
            row["error"] = f"{type(e).__name__}: {str(e)[:200]}"
    row["wall_s"] = round(time.time() - t0, 1)
    with lock:
        with log.open("a") as out:
            out.write(json.dumps(row, ensure_ascii=False) + "\n")
    print(f"{judge:5} {headword:20} {' > '.join(row.get('ranking', [])) or row.get('error')} {row['wall_s']}s", flush=True)
    return row


def report(lang, writers):
    folder = OUT / "cross" / lang
    rows = defaultdict(list)
    for judge in JUDGES:
        f = folder / f"{judge}.jsonl"
        if f.exists():
            rows[judge] = [json.loads(l) for l in f.read_text().splitlines() if "ranking" in l]
    names = [WRITERS[w] for w in writers]
    lines = [f"# The writers judge each other ({LANG_NAME[lang]})", "",
             "Every judge saw the four entries for a word under shuffled letters, without model names. "
             "Rank 1 is best; a win is rank 1. Each judge also rated its own entry, blind.", ""]
    for judge in JUDGES:
        if not rows[judge]:
            continue
        rs = rows[judge]
        cost = sum((r.get("usage") or {}).get("cost_usd") or 0 for r in rs)
        lines += [f"## Judge: {judge} ({len(rs)} words{f', ${cost:.2f} list' if cost else ''})", "",
                  "| Writer | Mean rank | Wins | Mean score | Words with an error flagged |", "|---|---|---|---|---|"]
        for w in sorted(names, key=lambda w: mean(r["ranking"].index(w) + 1 for r in rs if w in r["ranking"])):
            had = [r for r in rs if w in r["ranking"]]
            ranks = [r["ranking"].index(w) + 1 for r in had]
            wins = sum(r["ranking"][0] == w for r in had)
            scores = [r["scores"][w] for r in had if w in r.get("scores", {})]
            flagged = sum(bool(r.get("errors", {}).get(w)) for r in had)
            own = " (itself)" if w == judge else ""
            lines.append(f"| {w}{own} | {mean(ranks):.2f} | {wins} ({100 * wins / len(had):.0f}%) | {mean(scores):.1f} | {flagged} |")
        lines.append("")
    # all judges together, and whether each judge favours its own entries
    lines += ["## All judges together", "", "| Writer | Mean rank | Wins | Mean score | Rank by itself | Rank by the other judges |", "|---|---|---|---|---|---|"]
    every = [r for judge in JUDGES for r in rows[judge]]
    if every:
        for w in sorted(names, key=lambda w: mean(r["ranking"].index(w) + 1 for r in every if w in r["ranking"])):
            had = [r for r in every if w in r["ranking"]]
            ranks = [r["ranking"].index(w) + 1 for r in had]
            wins = sum(r["ranking"][0] == w for r in had)
            scores = [r["scores"][w] for r in had if w in r.get("scores", {})]
            self_ranks = [r["ranking"].index(w) + 1 for r in rows[w] if w in r["ranking"]]
            other_ranks = [r["ranking"].index(w) + 1 for j in JUDGES if j != w for r in rows[j] if w in r["ranking"]]
            lines.append(f"| {w} | {mean(ranks):.2f} | {wins} ({100 * wins / len(had):.0f}%) | {mean(scores):.1f} | "
                         f"{mean(self_ranks):.2f} | {mean(other_ranks):.2f} |" if self_ranks and other_ranks else
                         f"| {w} | {mean(ranks):.2f} | {wins} ({100 * wins / len(had):.0f}%) | {mean(scores):.1f} | - | - |")
    # agreement between judges on the best entry
    lines += ["", "## Agreement on the best entry", ""]
    judges = [j for j in JUDGES if rows[j]]
    by_word = {j: {r["headword"]: r["ranking"][0] for r in rows[j]} for j in judges}
    for i, a in enumerate(judges):
        for b in judges[i + 1:]:
            common = set(by_word[a]) & set(by_word[b])
            if common:
                agree = sum(by_word[a][h] == by_word[b][h] for h in common)
                lines.append(f"- {a} and {b} picked the same best entry on {agree} of {len(common)} words ({100 * agree / len(common):.0f}%)")
    # the errors judges found, for a person to check
    lines += ["", "## Errors flagged (first 40), for a person to check", ""]
    n = 0
    for judge in judges:
        for r in rows[judge]:
            for w, errs in (r.get("errors") or {}).items():
                for e in errs:
                    if n < 40:
                        lines.append(f"- {r['headword']} / {w}, by {judge}: {e}")
                        n += 1
    (folder / "report.md").write_text("\n".join(lines) + "\n")
    print("\n".join(lines))


ap = argparse.ArgumentParser()
ap.add_argument("--lang", default="de")
ap.add_argument("--writers", default=",".join(WRITERS))
ap.add_argument("--judges", default=",".join(JUDGES))
ap.add_argument("--limit", type=int, default=0)
ap.add_argument("--workers", type=int, default=3, help="parallel calls per judge; the judges themselves run side by side")
ap.add_argument("--report-only", action="store_true")
a = ap.parse_args()
writers = a.writers.split(",")
folder = OUT / "cross" / a.lang
folder.mkdir(parents=True, exist_ok=True)
if not a.report_only:
    heads = sorted(set.intersection(*({f.stem for f in (OUT / w / a.lang).glob("*.json") if not f.name.endswith(".meta.json")} for w in writers)))
    words = {}
    for h in heads:
        entries = {WRITERS[w]: json.loads((OUT / w / a.lang / f"{h}.json").read_text()) for w in writers}
        if all("error" not in e for e in entries.values()):
            words[h] = entries
    if a.limit:
        words = dict(list(words.items())[:a.limit])
    print(f"{len(words)} words with an entry from every writer", flush=True)
    per_judge = []
    for judge in a.judges.split(","):
        log = folder / f"{judge}.jsonl"
        done = {json.loads(l)["headword"] for l in log.read_text().splitlines() if "ranking" in l} if log.exists() else set()
        per_judge.append([(judge, h, e, log) for h, e in words.items() if h not in done])
    # interleaved, so the judges work side by side instead of one plan at a time
    jobs = [job for group in zip(*[q + [None] * (max(map(len, per_judge)) - len(q)) for q in per_judge]) for job in group if job]
    print(f"{len(jobs)} judgements to make", flush=True)
    import threading
    lock = threading.Lock()
    with ThreadPoolExecutor(a.workers * len(a.judges.split(","))) as ex:
        list(ex.map(lambda j: judge_one(j[0], a.lang, j[1], j[2], j[3], lock), jobs))
report(a.lang, writers)
