#!/usr/bin/env python3
"""A second look at the facts the sources contradict, one small call an entry, kept only where the sources agree.

    scripts/lexicon_research/settle_v4.py --langs pt,es --take pt:10,es:6 --dry     what would be asked, nothing paid
    scripts/lexicon_research/settle_v4.py --langs pt,es --take pt:10,es:6           (run from the backend repository root)

witness.py lists the entries whose gender, plural, stressed syllable or sounds no source has. Nobody reads that
list, and half of what is on it is the entry's fault and half a source's gap. This asks a model once for each
such entry, showing it the lexeme, what the entry has and what every source has, and for each fact it answers

    change   the entry is wrong: here is the value, in the dictionary's own notation
    keep     the entry is right as it stands: the word is also said so, or the sources describe another word

Code then decides, not the model. A change is applied to a copy of the entry as published and held against the
sources again: it stands only if they now agree with it (verified). One that still differs is kept on file as
disputed and applied nowhere. The written entry is never edited: decisions live under out/review-settle/<lang>/,
and ingest_v4.clean_entry sets a verified one on the published copy, only while the entry still has the value
the decision was made about. A "keep" is on file too, so the same question is not bought twice; the witness
stops listing a kept fact that one source alone contradicted, and goes on listing one that two or more do:
the model is of the writer's family and has defended a mistake of the writer's (Russian "тема" with a hard т).

What is paid for is one question: its identity is the text asked (the lexeme, the findings, the instructions),
and a question already answered is never asked again, whatever run or list it comes up in.
"""
import argparse
import hashlib
import json
import os
import random
import re
import subprocess
import sys
import time
from concurrent.futures import ThreadPoolExecutor
from pathlib import Path

HERE = Path("scripts/lexicon_research")
sys.path.insert(0, str(HERE))
import ingest_v4  # noqa: E402
import witness  # noqa: E402
from prompting import LANG_NAME, PROMPT_VERSION, VARIETIES, sounds_v4  # noqa: E402

SETTLE_VERSION = "settle-v1"
EMPTY = Path(os.environ.get("LEXICON_WRITER_EMPTY_DIR", "/tmp/claude-1000/lexicon-writer-empty"))
READING = {"ru": "the word's own spelling with the combining acute after the stressed vowel (ме́бель)",
           "uk": "the word's own spelling with the combining acute after the stressed vowel",
           "it": "the word's own spelling with the accent on the stressed vowel (bestiàme)"}
SCHEMA = {"type": "object", "required": ["decisions"], "additionalProperties": False, "properties": {"decisions": {"type": "array", "items": {
    "type": "object", "required": ["n", "verdict", "why"], "additionalProperties": False, "properties": {
        "n": {"type": "integer"}, "verdict": {"enum": ["change", "keep"]}, "value": {"type": "string"}, "reading": {"type": "string"},
        "why": {"type": "string"}}}}}}
PROMPT = """You settle disagreements about facts between one entry of a learner's dictionary of {name} and other dictionaries. Each numbered finding names one fact of one word of the entry: its gender, its plural, or one of its transcriptions (the syllable its stress falls on, or its sounds); what the entry has; and what the sources have. The sources are Wiktionary and other open dictionaries: they are evidence, and they can be wrong, incomplete, or about another word spelled the same way. So can the entry. Decide each finding from what you know of {name}:

- "change" when the entry is wrong. Give the right value in "value": a gender as Masc, Fem, Neut or Com; a plural as the plural form itself; a transcription whole, in this dictionary's notation.
- "keep" when the entry is right as it stands: the word is also said or used that way and a dictionary of {name} prints it, or the sources describe another word or another variety, or they are wrong. Say which in "why", in a few words.

A transcription is changed only for a different sound or a different stressed syllable, never to copy the sources' notation: they write the same sounds with other symbols, mark length and syllables otherwise, and are often narrower. This dictionary's notation: between slashes, a primary stress mark ˈ before the stressed syllable of every word, also a word of one syllable, and a dot at every syllable boundary. {sounds}
{reading}
Return JSON only: {{"decisions": [{{"n": 1, "verdict": "change", "value": "...", "why": "..."}}, {{"n": 2, "verdict": "keep", "why": "..."}}]}}"""
STOP_FILE = ""


def system_prompt(lang):
    reading = (f'Where the stress changes and the entry gives a stressed spelling beside the transcription, give the new one in "reading": '
               f"{READING[lang]}.\n") if lang in READING else ""
    return PROMPT.format(name=LANG_NAME[lang], sounds=" ".join(sounds_v4(v, PROMPT_VERSION) for v in VARIETIES[lang]), reading=reading)


def leads_of(lang, entry, table):
    """What is asked about one entry: the facts no source has, one finding to a transcription (its stress and its
    sounds are one question). An English or German sound alone is not asked: it is mostly another accepted way."""
    asked, seen = [], {}
    for finding in witness.findings(lang, entry, table):
        if finding["verdict"] != "differs" or (finding["fact"] == "sounds" and lang in witness.ACCEPTED_VARIANTS):
            continue
        key = (finding["lexeme"], finding.get("variety"), finding["was"]) if finding["fact"] in ("sounds", "stressed syllable") else None
        if key and key in seen:
            if "  [" in finding["detail"] and "  [" not in seen[key]["detail"]:  # the one that names the sounds says more
                seen[key]["detail"] = finding["detail"]
            seen[key]["fact"] = "transcription"
            continue
        lead = {k: finding.get(k) for k in ("fact", "lexeme", "variety", "was", "detail", "sources")}
        asked.append(lead)
        if key:
            seen[key] = lead
    return asked


def question(lang, headword, entry, leads):
    lines = [f"Headword: {headword}"]
    for at in sorted({lead["lexeme"] for lead in leads}):
        lexeme = entry["lexemes"][at]
        said = ", ".join(f"{p.get('ipa')}{' ' + p['reading'] if p.get('reading') else ''} ({p.get('variety')})" for p in lexeme.get("pronunciations") or [])
        grammar = "; ".join(f"{k} {v}" for k, v in (lexeme.get("grammar") or {}).items() if k in ("gender", "number", "countability", "plural_class", "declension"))
        plurals = ", ".join(sorted(witness.plurals_of(lang, lexeme)))
        lines.append(f"Word {at + 1}: {lexeme.get('lemma')}, {lexeme.get('pos')}" + (f"; {grammar}" if grammar else "") + (f"; plural {plurals}" if plurals else "")
                     + f"; said {said}; first meaning: {(lexeme.get('senses') or [{}])[0].get('definition', '')}")
    lines.append("Findings:")
    for n, lead in enumerate(leads, 1):
        what = {"gender": "the gender", "plural": "the plural", "stressed syllable": f"the stressed syllable of the {lead['variety']} transcription",
                "sounds": f"the sounds of the {lead['variety']} transcription", "transcription": f"the {lead['variety']} transcription, its stress and its sounds"}[lead["fact"]]
        whose, _, theirs = lead["detail"].partition(", ")[2].partition(": ")
        theirs, _, pairs = theirs.partition("  [")
        lines.append(f"{n}. Word {lead['lexeme'] + 1}, {what}. The entry has {lead['was']}. The sources ({whose}) have {theirs.strip()}."
                     + (f" Sounds that differ once both are in one notation, the entry's then theirs: {pairs.rstrip(']').strip()}." if pairs.strip("] ") else ""))
    return "\n".join(lines)


def verified(lang, entry, lead, table):
    """Whether the sources have no quarrel left with the fact a decision changed."""
    kinds = {"gender"} if lead["fact"] == "gender" else {"plural"} if lead["fact"] == "plural" else {"sounds", "stressed syllable"}
    for finding in witness.findings(lang, entry, table):
        if finding["lexeme"] == lead["lexeme"] and finding["fact"] in kinds and finding["verdict"] == "differs" \
                and (finding.get("variety") in (None, lead.get("variety"))) and not (finding["fact"] == "sounds" and lang in witness.ACCEPTED_VARIANTS):
            return False
    return True


def decide(lang, entry, leads, answer, table):
    """The model's answer turned into decisions a program stands behind: each with the fact, where it is, the value
    it was made about, and whether the sources agree with the change."""
    decisions = []
    by_number = {d.get("n"): d for d in answer.get("decisions") or []}
    for n, lead in enumerate(leads, 1):
        said = by_number.get(n) or {"verdict": "keep", "why": "no answer"}
        fact = "sounds" if lead["fact"] == "transcription" else lead["fact"]
        decision = {"fact": fact, "lexeme": lead["lexeme"], "variety": lead.get("variety"), "was": lead["was"], "verdict": said.get("verdict"),
                    "why": (said.get("why") or "")[:300], "sources": lead.get("sources")}
        if said.get("verdict") == "change" and said.get("value"):
            decision["now"] = said["value"].strip()
            if said.get("reading"):
                decision["reading"] = said["reading"].strip()
            trial = json.loads(json.dumps(entry))
            decision["verified"] = bool(ingest_v4.apply_decisions(lang, trial, [decision])) and verified(lang, trial, lead, table)
        decisions.append(decision)
    return decisions


def rest(seconds):
    until = time.time() + seconds
    while time.time() < until:
        if STOP_FILE and Path(STOP_FILE).exists():
            return False
        time.sleep(min(10, max(0.0, until - time.time())))
    return True


def settle_one(a, lang, system, table, headword, entry, leads):
    asked = question(lang, headword, entry, leads)
    sha = hashlib.sha256((system + "\n" + asked).encode("utf-8")).hexdigest()[:12]
    folder = ingest_v4.SETTLED / lang
    folder.mkdir(parents=True, exist_ok=True)
    target = folder / f"{headword}.{sha}.json"
    if target.exists():
        return "have"
    if a.dry:
        print(f"--- {lang} {headword}\n{asked}\n", flush=True)
        return "dry"
    cmd = ["claude", "-p", asked, "--model", a.model, "--effort", a.effort, "--tools", "", "--system-prompt", system, "--json-schema", json.dumps(SCHEMA),
           "--output-format", "json", "--no-session-persistence", "--strict-mcp-config", "--mcp-config", "{\"mcpServers\": {}}"]
    retries = 0
    while True:
        if STOP_FILE and Path(STOP_FILE).exists():
            return "stopped"
        t0 = time.time()
        try:
            r = subprocess.run(cmd, capture_output=True, text=True, timeout=600, cwd=EMPTY)
        except subprocess.TimeoutExpired:
            r = subprocess.CompletedProcess(cmd, 1, stdout="", stderr="the call took longer than ten minutes")
        try:
            res = json.loads(r.stdout)
        except json.JSONDecodeError:
            res = {"is_error": True, "result": (r.stderr or r.stdout or "no output")[-300:]}
        if not res.get("is_error"):
            break
        limited = re.search(r"limit|usage|quota", str(res.get("result")), re.I)
        retries += not limited
        if retries > 6:
            print(f"giving up for this run at {headword}: {str(res.get('result'))[:120]!r}", flush=True)
            return "error"
        print(f"{'limit' if limited else 'error'} at {headword} ({str(res.get('result'))[:80]!r}); waiting ten minutes", flush=True)
        if not rest(600):
            return "stopped"
    answer = res.get("structured_output")
    if answer is None:
        try:
            answer = json.loads((res.get("result") or "").strip().strip("`").removeprefix("json"))
        except json.JSONDecodeError:
            answer = {"decisions": []}
    usage = res.get("usage") or {}
    decisions = decide(lang, entry, leads, answer, table)
    record = {"headword": headword, "language": lang, "settle_version": SETTLE_VERSION, "question_sha": sha, "model": a.model, "effort": a.effort,
              "wall_s": round(time.time() - t0, 1), "total_cost_usd": res.get("total_cost_usd"), "input_tokens": usage.get("input_tokens"),
              "output_tokens": usage.get("output_tokens"), "question": asked, "decisions": decisions}
    target.write_text(json.dumps(record, ensure_ascii=False, indent=1) + "\n", encoding="utf-8")
    told = "; ".join(f"{d['fact']} {d['was']} -> {d.get('now')} ({'verified' if d.get('verified') else 'DISPUTED'})" if d["verdict"] == "change"
                     else f"{d['fact']} {d['was']} kept{' against ' + str(d['sources']) + ' sources' if (d.get('sources') or 0) > 1 else ''}: {d['why'][:60]}" for d in decisions)
    print(f"settle {lang} {headword:20} ${res.get('total_cost_usd') or 0:.4f}  {told}", flush=True)
    return "asked"


def main():
    global STOP_FILE
    ap = argparse.ArgumentParser(description=__doc__.split("\n")[0])
    ap.add_argument("--langs", default="es,it,pt,ru,fr,de,en")
    ap.add_argument("--take", default="", help="how many entries of each language, drawn at random: pt:10,es:6 (a language not named: all)")
    ap.add_argument("--seed", default="2026-11-19")
    ap.add_argument("--model", default="claude-opus-5-5")
    ap.add_argument("--effort", default="medium")
    ap.add_argument("--workers", type=int, default=4)
    ap.add_argument("--dry", action="store_true", help="print the questions, pay nothing")
    ap.add_argument("--stop-file", default="", help="once this file exists, finish the calls in flight and make no other")
    a = ap.parse_args()
    STOP_FILE = a.stop_file
    EMPTY.mkdir(parents=True, exist_ok=True)
    take = dict((part.split(":")[0], int(part.split(":")[1])) for part in a.take.split(",") if part)
    for lang in a.langs.split(","):
        table = witness.facts(lang)
        if table is None:
            print(f"{lang}: no facts table; run lemma_index.py and open_sources.py")
            continue
        raw, asked = ingest_v4.load_raw(lang), []
        for headword in sorted(raw):
            written, meta = raw[headword]
            if not written.get("lexemes"):
                continue
            entry = ingest_v4.clean_entry(lang, json.loads(json.dumps(written)), meta, settle=False)
            leads = leads_of(lang, entry, table)
            if leads:
                asked.append((headword, entry, leads))
        if lang in take:
            asked = random.Random(f"{a.seed}-{lang}").sample(asked, min(len(asked), take[lang]))
        system = system_prompt(lang)
        print(f"{lang}: {len(asked)} entries, {sum(len(leads) for _, _, leads in asked)} facts to settle", flush=True)
        with ThreadPoolExecutor(a.workers) as pool:
            done = list(pool.map(lambda job: settle_one(a, lang, system, table, *job), asked))
        print(f"{lang}: " + ", ".join(f"{done.count(k)} {k}" for k in sorted(set(done))), flush=True)
    print("done")


if __name__ == "__main__":
    main()
