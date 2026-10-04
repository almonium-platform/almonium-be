"""Trial of cross-checking entries: two models wrote the same headwords independently; each then
rules on what the two drafts disagree about.

The drafts are compared in code first (lexeme split, stress, sounds, sense counts). A word whose
drafts differ goes to a reviewer as entry A and entry B, unnamed and in an order fixed per word, and
the reviewer answers typed questions: is a lexeme only one draft has real, which pronunciation is
right, is a sense only one draft has a real distinct sense. Nothing is rewritten here; the verdicts
of the two reviewers are compared by reconcile_report.py.

The reviewers run on the owner's plans: Opus through the Claude Code CLI, a GPT model through Codex.

Output: out/reconcile/<reviewer>/<lang>/<headword>.json

Usage: reconcile.py --reviewer claude:opus|codex:<model> [--a claude-opus-5-5] [--b codex-gpt-6-astra-low]
                    [--langs en,de,uk] [--limit N] [--workers 4]
       (run from the backend repository root)
"""
import argparse, json, os, random, re, subprocess, sys, tempfile, threading, time, unicodedata
from concurrent.futures import ThreadPoolExecutor
from pathlib import Path

sys.path.insert(0, "scripts/lexicon_research")
from prompting import HERE

OUT = HERE / "out"
EMPTY = Path("/tmp/claude-1000/lexicon-writer-empty")
EMPTY.mkdir(parents=True, exist_ok=True)
CODEX = os.environ.get("CODEX_BIN") or next((str(p) for p in sorted(Path.home().glob(".nvm/versions/node/*/bin/codex"), reverse=True)), "codex")
LIMIT_HIT = threading.Event()
VOWEL = set("aeiouyæøœɑɒɔəɛɜɪʊʌɐɨʉɯɤɵɶɘɞ")
LANGUAGE = {"en": "English", "de": "German", "uk": "Ukrainian"}

SYSTEM = (
    "You are a lexicographer reconciling two draft dictionary entries for one {language} headword. The drafts were "
    "written independently for a learner's dictionary whose senses are split as finely as a standard monolingual "
    "dictionary of {language} splits them. You are given entry A and entry B; who wrote which is not known to you and "
    "does not matter. Rule only on what the drafts disagree about:\n"
    "lexemes: a lexeme is a headword with one part of speech and one pronunciation. For every lexeme that one draft "
    "has and the other lacks, say whether it is a real lexeme of this headword in contemporary standard {language} "
    "that a learner's dictionary should list (keep).\n"
    "pronunciations: for every lexeme both drafts have whose transcriptions differ in stress or in sounds, beyond "
    "notation, say which is right: A, B, both (accepted variants) or neither.\n"
    "senses: for every sense in either draft that has no counterpart in the other draft, say whether it is a real "
    "sense, distinct from every other sense listed for that lexeme, that the dictionary should list (keep), and why "
    "not otherwise: duplicate (the other senses already cover it), rare (too rare, technical or dated for a learner) "
    "or wrong (not a sense of this word, or of this part of speech).\n"
    "Refer to lexemes and senses by the numbers given. One sentence of reason each. Leave a list empty when the "
    "drafts do not disagree on it."
)
SCHEMA = {
    "type": "object", "additionalProperties": False, "required": ["lexemes", "pronunciations", "senses"],
    "properties": {
        "lexemes": {"type": "array", "items": {"type": "object", "additionalProperties": False,
                    "required": ["entry", "lexeme", "keep", "reason"],
                    "properties": {"entry": {"type": "string", "enum": ["A", "B"]}, "lexeme": {"type": "integer"},
                                   "keep": {"type": "boolean"}, "reason": {"type": "string"}}}},
        "pronunciations": {"type": "array", "items": {"type": "object", "additionalProperties": False,
                           "required": ["lexeme_a", "lexeme_b", "correct", "reason"],
                           "properties": {"lexeme_a": {"type": "integer"}, "lexeme_b": {"type": "integer"},
                                          "correct": {"type": "string", "enum": ["A", "B", "both", "neither"]},
                                          "reason": {"type": "string"}}}},
        "senses": {"type": "array", "items": {"type": "object", "additionalProperties": False,
                   "required": ["entry", "lexeme", "sense", "keep", "why", "reason"],
                   "properties": {"entry": {"type": "string", "enum": ["A", "B"]}, "lexeme": {"type": "integer"},
                                  "sense": {"type": "integer"}, "keep": {"type": "boolean"},
                                  "why": {"type": "string", "enum": ["distinct", "duplicate", "rare", "wrong"]},
                                  "reason": {"type": "string"}}}},
    },
}


def norm(ipa):
    s = ipa.strip("/ ")
    for c in "ˈˌ.ː ͡‿":
        s = s.replace(c, "")
    s = unicodedata.normalize("NFD", s)
    s = "".join(c for c in s if not unicodedata.combining(c) and c != "ʲ")
    for a, b in (("ɡ", "g"), ("ɹ", "r"), ("ɚ", "ər"), ("ɝ", "ɜr"), ("ʌ", "ə"), ("ɐ", "a"), ("ʋ", "v"), ("w", "v"), ("ɫ", "l"), ("ɨ", "ɪ"),
                 ("ʊ", "u"), ("ɛ", "e"), ("ɔ", "o"), ("ɑ", "a"), ("ɒ", "o"), ("ç", "x"), ("ʁ", "r"), ("ɾ", "r")):
        s = s.replace(a, b)
    return s


def stress(ipa):
    body = ipa.strip("/ ")
    i = body.find("ˈ")
    if i < 0:
        return -1
    j = i + 1
    while j < len(body) and body[j] not in VOWEL:
        j += 1
    return sum(1 for k in range(j) if body[k] in VOWEL and not (k > 0 and body[k - 1] in VOWEL))


def load(model, lang, word):
    f = OUT / model / lang / f"{word}.json"
    if not f.exists():
        return None
    e = json.loads(f.read_text())
    if "error" in e:  # a trailing comma is the usual slip; anything worse is left out of the trial
        try:
            e = json.loads(re.sub(r",\s*([}\]])", r"\1", e.get("raw", "")))
        except Exception:
            return None
    return e if isinstance(e, dict) and e.get("lexemes") else None


def ipas(lexeme):
    return [p.get("ipa", "") for p in lexeme.get("pronunciations", [])]


def differ(a, b):
    """Whether two drafts disagree on anything code can see: split, stress, sounds, sense counts."""
    key = lambda x: x.get("pos") or ""
    la, lb = sorted(a["lexemes"], key=key), sorted(b["lexemes"], key=key)
    if [key(x) for x in la] != [key(x) for x in lb]:
        return True
    if sorted((key(x), tuple(sorted(stress(i) for i in ipas(x)))) for x in la) != sorted((key(x), tuple(sorted(stress(i) for i in ipas(x)))) for x in lb):
        return True
    for x, y in zip(la, lb):
        if ipas(x) and ipas(y) and not ({norm(i) for i in ipas(x)} & {norm(i) for i in ipas(y)}):
            return True
        if len(x.get("senses", [])) != len(y.get("senses", [])):
            return True
    return False


def compact(entry):
    return [{"lexeme": i, "pos": lx.get("pos"), "grammar": lx.get("grammar"), "ipa": ipas(lx),
             "senses": [{"sense": j, "definition": s.get("definition"), "english": (s.get("glosses") or {}).get("en"), "example": s.get("example")}
                        for j, s in enumerate(lx.get("senses", []))]}
            for i, lx in enumerate(entry["lexemes"])]


def ask_claude(model, system, payload):
    cmd = ["claude", "-p", "--model", model, "--effort", "high", "--tools", "", "--system-prompt", system,
           "--json-schema", json.dumps(SCHEMA), "--output-format", "json", "--no-session-persistence",
           "--strict-mcp-config", "--mcp-config", "{\"mcpServers\": {}}"]
    r = subprocess.run(cmd, input=payload, capture_output=True, text=True, timeout=900, cwd=EMPTY)
    res = json.loads(r.stdout)
    if res.get("is_error") or res.get("structured_output") is None:
        raise RuntimeError(str(res.get("result"))[:300])
    return res["structured_output"], {"output_tokens": (res.get("usage") or {}).get("output_tokens")}


def ask_codex(model, system, payload):
    with tempfile.NamedTemporaryFile("w", suffix=".schema.json", delete=False) as f:
        json.dump(SCHEMA, f)
    try:
        r = subprocess.run([CODEX, "exec", "-m", model, "--skip-git-repo-check", "--ephemeral", "--json", "--sandbox", "read-only",
                            "-c", "mcp_servers={}", "-c", "model_reasoning_effort=high", "--output-schema", f.name, "-"],
                           input=f"{system}\n\nDo not run commands or read files; answer from your own knowledge.\n\n{payload}",
                           capture_output=True, text=True, timeout=900, cwd=EMPTY)
    finally:
        os.unlink(f.name)
    text, usage, error = "", {}, ""
    for line in r.stdout.splitlines():
        try:
            ev = json.loads(line)
        except json.JSONDecodeError:
            continue
        if ev.get("type") == "item.completed" and (ev.get("item") or {}).get("type") == "agent_message":
            text = ev["item"].get("text", "")
        elif ev.get("type") == "turn.completed":
            usage = ev.get("usage") or {}
        elif ev.get("type") in ("error", "turn.failed"):
            error = str(ev.get("message") or ev.get("error"))[:300]
    if not text:
        if "usage limit" in error:
            LIMIT_HIT.set()
        raise RuntimeError(error or "no answer")
    return json.loads(text), {"output_tokens": usage.get("output_tokens")}


def review_one(reviewer, a_model, b_model, lang, word):
    provider, model = reviewer.split(":", 1)
    out = OUT / "reconcile" / reviewer.replace(":", "-") / lang / f"{word}.json"
    if out.exists() or LIMIT_HIT.is_set():
        return
    drafts = {a_model: load(a_model, lang, word), b_model: load(b_model, lang, word)}
    if not all(drafts.values()) or not differ(*drafts.values()):
        return
    # the same order for every reviewer of a word, so their verdicts can be compared
    order = [a_model, b_model]
    random.Random(f"{lang}:{word}").shuffle(order)
    payload = json.dumps({"headword": word, "language": lang, "entry_A": compact(drafts[order[0]]), "entry_B": compact(drafts[order[1]])}, ensure_ascii=False)
    t0 = time.time()
    try:
        verdict, usage = (ask_claude if provider == "claude" else ask_codex)(model, SYSTEM.format(language=LANGUAGE[lang]), payload)
    except Exception as e:
        print(f"{reviewer} {lang} {word}: FAILED {type(e).__name__} {str(e)[:200]}", flush=True)
        return
    out.parent.mkdir(parents=True, exist_ok=True)
    out.write_text(json.dumps({"headword": word, "A": order[0], "B": order[1], "wall_s": round(time.time() - t0, 1), **usage, **verdict}, ensure_ascii=False, indent=1) + "\n")
    print(f"{reviewer} {lang} {word:18} lexemes {len(verdict['lexemes'])} pronunciations {len(verdict['pronunciations'])} senses {len(verdict['senses'])} {time.time() - t0:.0f}s", flush=True)


if __name__ == "__main__":
    ap = argparse.ArgumentParser()
    ap.add_argument("--reviewer", required=True)
    ap.add_argument("--a", default="claude-opus-5-5")
    ap.add_argument("--b", default="codex-gpt-6-astra-low")
    ap.add_argument("--langs", default="en,de,uk")
    ap.add_argument("--limit", type=int, default=0)
    ap.add_argument("--workers", type=int, default=4)
    a = ap.parse_args()
    jobs = [(lang, r["headword"]) for lang in a.langs.split(",") for r in json.loads((HERE / "headwords" / f"{lang}.json").read_text())]
    if a.limit:
        jobs = jobs[:a.limit]
    with ThreadPoolExecutor(a.workers) as ex:
        list(ex.map(lambda j: review_one(a.reviewer, a.a, a.b, *j), jobs))
    print("done")
