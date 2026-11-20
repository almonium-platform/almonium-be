"""The review loop: a second model reads a written entry, the writer answers, a third model settles what they dispute.

  1. read    a reader of another model family names the faults of an entry, in measure.py's words and classes,
             several entries to a call. An answer already given in a chat (measure/<name>/answers) counts as read.
  2. reply   the writer's model is shown the findings on its entry, under the rules it wrote by, and answers each:
             accept, with the patch that mends it, or reject, with a reason a third reader can check.
  3. rule    a third model reads every finding the writer rejected, with both sides, and upholds or dismisses it.
  4. mend    the writer writes the patch for each finding the third model upheld.
  5. settle  no call: each patch is tried on the entry and refused if it breaks the shape or moves a fact away
             from what the sources state (witness.py). What is left is the outcome, a file ingest applies to the
             published copy where the entry still has what the patch saw. A written entry is never edited.

So a change reaches an entry only when two models of different families agree it is a fault, or a third has ruled,
and never against the dictionaries on a fact they state.

Every paid unit is one file under out/<folder>/<lang>/, named by the entry's content, the step and a hash of what
was asked of which model, and is found again by that name: a run that is stopped, or meets the plan's wall, buys
nothing twice when it is started again, and an entry written again is a different entry. A wall is a stop, not
a failure of the entry.

  review_v4.py --langs en,de --take 200                      the present prompt's entries, most frequent first
  review_v4.py --sample v410 --answers v410 --folder review-loop-v410 --take 12   a measured sample, read in a chat
  review_v4.py --decoys v410 --folder review-loop-decoys     right entries, each with one false finding: does the writer agree to anything
  review_v4.py --report --folder review-loop-v410            what the loop did, by fault
  --dry prices the next step without asking; --steps read,reply stops after those; --faults names what is looped.

The seats: --reader codex:gpt-6-astra:medium:search, --writer claude:claude-opus-5-5:high, --judge openrouter:google/gemini-4-neon:high.
A seat is the command line of a plan (claude or codex), a model, an effort and, with ":search" after them, leave to
search the web. The reader that the samples were measured with had it (the ChatGPT app looked the words up in
Oxford, Cambridge, Collins and the rest), so the reader's seat has it by default: a reader working from memory is
another, weaker instrument, and a seat without ":search" is a different seat with answers of its own. Two more
seats are paid by the call and not by a plan: gemini:<model>:<effort> (the Gemini API; GEMINI_API_KEY in the
environment or the backend's .env) and openrouter:<vendor/model>:<effort> (any model OpenRouter lists;
OPENROUTER_API_KEY). Run from the repository's root.

  review_v4.py --judge-test v410 --take 3 --folder review-loop-judges --judge openrouter:google/gemini-4-neon:high
      measures a third model by itself: findings that are true and findings that are false, each rejected by a
      "writer" in the same words, so that only knowing the language tells them apart.
"""
import argparse
import copy
import hashlib
import json
import os
import re
import subprocess
import sys
import threading
from collections import Counter, defaultdict
from concurrent.futures import ThreadPoolExecutor
from pathlib import Path

HERE = Path("scripts/lexicon_research")
sys.path.insert(0, str(HERE))
import ingest_v4  # noqa: E402
import measure  # noqa: E402
import witness  # noqa: E402
from entry_patch import MISSING, PatchError, apply_ops, differences, sha_of, value_at  # noqa: E402
from prompting import LANG_NAME, PROMPT_VERSION, TARGET_LANGUAGES, system_prompt  # noqa: E402

LOOP_VERSION = "loop-v1"
OUT = HERE / "out"
EMPTY = Path(os.environ.get("LEXICON_WRITER_EMPTY_DIR", "/tmp/claude-1000/lexicon-writer-empty"))
CODEX = os.environ.get("CODEX_BIN") or next((str(p) for p in sorted(Path.home().glob(".nvm/versions/node/*/bin/codex"), reverse=True)), "codex")
# Three families: Anthropic writes, OpenAI reads, Google rules (the owner, 20 November 2026). The third seat is paid by
# the call, through OpenRouter, and so does not draw on the plan the writer works on.
SEATS = {"reader": "codex:gpt-6-astra:medium:search", "writer": "claude:claude-opus-5-5:high", "judge": "openrouter:google/gemini-4-neon:high"}
# The faults the bar counts, and the two senses numbered as one. A reader finds a rarer sense missing or a wording
# too narrow in half the entries it is shown, hand-checked ones included, so those are looped only when asked for.
LOOPED = ("fact", "claim", "missing-core", "split")
STEPS = ("read", "reply", "rule", "mend")
# what a plan says when its allowance is used up, in the Claude and the Codex command lines' words
WALL = re.compile(r"usage limit|limit reached|usage_limit|rate limit|quota|hit your (?:[\w-]+ )?limit|out of (?:credits|quota)"
                  r"|resource_exhausted|insufficient credits|credit balance|billing", re.I)
# list prices of the models paid by the call, dollars a million tokens: input, output (thinking is output); OpenRouter says its own
PRICES = {"gemini-4-neon": (3.0, 15.0), "gemini-4-carbon": (2.0, 10.0), "gemini-4-hydrogen": (1.5, 7.5)}  # as listed on 20 November 2026

PATCH = {"type": "array", "items": {"type": "object", "required": ["op", "path", "value"], "additionalProperties": False, "properties": {
    "op": {"enum": ["replace", "add", "remove"]}, "path": {"type": "string"}, "value": {"type": "string"}}}}
ANSWERS = {"type": "object", "required": ["answers"], "additionalProperties": False, "properties": {"answers": {"type": "array", "items": {
    "type": "object", "required": ["n", "verdict", "reason", "patch"], "additionalProperties": False, "properties": {
        "n": {"type": "integer"}, "verdict": {"enum": ["accept", "reject"]}, "reason": {"type": "string"}, "patch": PATCH}}}}}
RULINGS = {"type": "object", "required": ["rulings"], "additionalProperties": False, "properties": {"rulings": {"type": "array", "items": {
    "type": "object", "required": ["n", "upheld", "reason"], "additionalProperties": False, "properties": {
        "n": {"type": "integer"}, "upheld": {"type": "boolean"}, "reason": {"type": "string"}}}}}}

HOW_TO_PATCH = """A patch is a list of operations on the entry's JSON exactly as given below.
- "path" is a JSON Pointer from the root, counting from 0: /lexemes/0/senses/2/definition is the definition of the first lexeme's third sense. The reviewer counts from 1 ("lexeme 1, sense 3"). Every path is into the entry as given, also after an operation that adds or removes something.
- "op" is "replace" (put "value" where the path points), "remove" (take out what stands there; "value" is then ""), or "add" (into a list: before the item the path names, or at the end when the path ends in /-; into an object: a key it does not have yet).
- "value" is JSON inside a string: "\\"Fem\\"" for a word, "[\\"lead the way\\"]" for a list, "{...}" for a whole sense.
- A new sense is a whole sense as the rules above ask for one, with its definition, example and level and the frames, collocations, labels and grammar that apply to it. A new lexeme likewise. Identifiers are left out of what you see and are not yours to give."""

REPLY = """A reviewer from outside has read the entry below, which was written under the rules above, and reports the numbered findings after it. The reviewer knows {name} well and has been told this dictionary's definitions only in short, so a finding may be right, wrong, or right about {name} and wrong about what these rules ask of an entry.

Answer every finding by its number.
- "accept": the entry has the fault, in whole or in part. Give the patch that mends it, the smallest that does, and say in one sentence what it changes.
- "reject": the entry is right as it stands under the rules above. The reviewer misread it, asks for a sense too rare for a learner's dictionary or for something the rules leave out on purpose, or prefers other words for a definition that is true. Give the reason in one sentence that a third reader can check against a dictionary of {name}, and an empty patch.

Do not accept to be agreeable and do not reject to defend the entry. A third reader rules on every finding you reject, and other dictionaries are held against every fact you change.

{how}

Entry "{headword}":
{entry}

Findings:
{findings}"""

MEND = """A third reader has ruled on findings you rejected in the entry below, which was written under the rules above, and has upheld these: the entry has the fault. Each is given with the third reader's reason. Write the patch that mends each, the smallest that does; the verdict is "accept" and the reason says in one sentence what the patch changes. Where you cannot mend one without stating something that is not so, the verdict is "reject", the patch is empty and the reason says why.

{how}

Entry "{headword}":
{entry}

Findings upheld:
{findings}"""

RULE = """You settle a disagreement about one entry of a learner's dictionary of {name}. A reviewer read the entry and reported the numbered findings below. The writer of the entry rejected each of them, for the reason given. Say for each finding whether the entry has the fault.

Uphold a finding when the entry as it stands states something that is not so in {name}, or lacks something a learner's dictionary of {name} would not leave out, and the writer's reason does not answer that. Dismiss it when the writer is right, when the matter is one of preference, when what is asked for is rare, technical, regional or dated, or when you cannot tell: an entry is changed only for a fault you could show from a dictionary of {name}. Judge the claim, not how sure either side sounds, and do not split the difference. Give the reason in one sentence.

What the faults are called:

{faults}

These are this dictionary's own definitions, not faults:

{definitions}
The entry "{headword}":

{shown}

Findings, each with the writer's answer:
{findings}"""


class Wall(Exception):
    """The plan's allowance is used up: the run stops, and nothing is counted against the entry."""


class Stopped(Exception):
    """The run was asked to stop, or a step is not among those asked for."""


def sha(*parts):
    return hashlib.sha256("\n".join(parts).encode("utf-8")).hexdigest()[:12]


def part_of_review(start, end):
    """A passage of the review file's instructions, so that the third reader is told what the first was."""
    return measure.REVIEW.partition(start)[2].partition(end)[0].strip("\n")


def ask(seat, user, system=None, schema=None):
    """One call through a plan's command line: (the answer, what it used). The answer is the structured object where
    a schema is given, else the text. Wall when the plan's allowance is used up."""
    cli, model, effort, *more = seat.split(":") + ["medium"] * (2 - min(2, seat.count(":")))
    search = "search" in more
    EMPTY.mkdir(parents=True, exist_ok=True)
    if cli == "claude":
        tools = ["--tools", "WebSearch,WebFetch", "--allowedTools", "WebSearch,WebFetch"] if search else ["--tools", ""]
        cmd = ["claude", "-p", user, "--model", model, "--effort", effort, *tools, "--system-prompt", system or "You are an editor of dictionaries.",
               "--output-format", "json", "--no-session-persistence", "--strict-mcp-config", "--mcp-config", "{\"mcpServers\": {}}"]
        if schema:
            cmd += ["--json-schema", json.dumps(schema)]
        try:
            r = subprocess.run(cmd, capture_output=True, text=True, timeout=900, cwd=EMPTY)
            res = json.loads(r.stdout)
        except (subprocess.TimeoutExpired, json.JSONDecodeError) as broken:
            raise RuntimeError(f"no answer from {seat}: {str(broken)[:200]}") from broken
        text = res.get("result") or ""
        if res.get("is_error"):
            raise (Wall if WALL.search(text) else RuntimeError)(text[:300])
        u = res.get("usage") or {}
        usage = {"cost_usd": res.get("total_cost_usd"), "input": u.get("input_tokens"), "output": u.get("output_tokens"),
                 "cache_read": u.get("cache_read_input_tokens"), "cache_write": u.get("cache_creation_input_tokens")}
        if not schema:
            return text, usage
        answer = res.get("structured_output")
        return (answer if answer is not None else parse(text)), usage
    if cli == "codex":
        how = ("Do not run commands or read files. Look the words up on the web, in dictionaries of the language, wherever you are not sure."
               if search else "Do not run commands or read files; answer from your own knowledge.")
        full = f"{how}\n\n" + (f"{system}\n\n" if system else "") + user
        if schema:
            full += "\n\nAnswer with one JSON object and nothing else, of this shape:\n" + json.dumps(schema)
        r = subprocess.run([CODEX, "exec", "-m", model, "--skip-git-repo-check", "--ephemeral", "--json", "--sandbox", "read-only",
                            "-c", "mcp_servers={}", "-c", f"model_reasoning_effort={effort}", *(["-c", 'web_search="live"'] if search else []), full],
                           capture_output=True, text=True, timeout=1800, cwd=EMPTY, stdin=subprocess.DEVNULL)  # an open stdin makes codex wait for it
        text, usage, error = "", {}, ""
        for line in r.stdout.splitlines():
            try:
                event = json.loads(line)
            except json.JSONDecodeError:
                continue
            if event.get("type") == "item.completed" and (event.get("item") or {}).get("type") == "agent_message":
                text = event["item"].get("text", "")
            elif event.get("type") == "turn.completed":
                u = event.get("usage") or {}
                usage = {"input": u.get("input_tokens"), "cache_read": u.get("cached_input_tokens"), "output": u.get("output_tokens"),
                         "reasoning": u.get("reasoning_output_tokens")}
            elif event.get("type") in ("error", "turn.failed"):
                error = str(event.get("message") or event.get("error"))[:300]
        if not text:
            raise (Wall if WALL.search(error or r.stderr or "") else RuntimeError)(error or (r.stderr or "no agent message")[-300:])
        return (parse(text) if schema else text), usage
    if cli in ("gemini", "openrouter"):
        told = user + ("\n\nAnswer with one JSON object and nothing else, of this shape:\n" + json.dumps(schema) if schema else "")
        level = {"xhigh": "high"}.get(effort, effort)
        if cli == "gemini":
            body = {"systemInstruction": {"parts": [{"text": system or "You are an editor of dictionaries."}]}, "contents": [{"role": "user", "parts": [{"text": told}]}],
                    "generationConfig": {"thinkingConfig": {"thinkingLevel": level}, **({"responseMimeType": "application/json"} if schema and not search else {})}}
            if search:
                body["tools"] = [{"google_search": {}}]
            res = post(f"https://generativelanguage.googleapis.com/v1beta/models/{model}:generateContent", {"x-goog-api-key": key_of("GEMINI_API_KEY")}, body)
            parts = ((res.get("candidates") or [{}])[0].get("content") or {}).get("parts") or []
            text = "".join(part.get("text", "") for part in parts if not part.get("thought"))
            u = res.get("usageMetadata") or {}
            usage = {"input": u.get("promptTokenCount"), "output": u.get("candidatesTokenCount"), "reasoning": u.get("thoughtsTokenCount"), "cache_read": u.get("cachedContentTokenCount")}
            if model in PRICES:
                usage["cost_usd"] = round(((usage["input"] or 0) * PRICES[model][0] + ((usage["output"] or 0) + (usage["reasoning"] or 0)) * PRICES[model][1]) / 1e6, 6)
        else:
            body = {"model": model, "messages": [{"role": "system", "content": system or "You are an editor of dictionaries."}, {"role": "user", "content": told}],
                    # a ceiling on the answer: without one OpenRouter holds back the price of the model's longest answer for every
                    # request in flight, and four of them were refused with a dollar still in the account
                    "reasoning": {"effort": level}, "usage": {"include": True}, "max_tokens": 16000, **({"response_format": {"type": "json_object"}} if schema else {}),
                    **({"plugins": [{"id": "web"}]} if search else {})}
            res = post("https://openrouter.ai/api/v1/chat/completions", {"Authorization": f"Bearer {key_of('OPENROUTER_API_KEY')}"}, body)
            if res.get("error"):
                raise (Wall if WALL.search(str(res["error"])) else RuntimeError)(str(res["error"])[:300])
            text = ((res.get("choices") or [{}])[0].get("message") or {}).get("content") or ""
            u = res.get("usage") or {}
            usage = {"input": u.get("prompt_tokens"), "output": u.get("completion_tokens"), "cost_usd": u.get("cost"),
                     "reasoning": (u.get("completion_tokens_details") or {}).get("reasoning_tokens")}
        if not text:
            raise RuntimeError(f"no answer from {seat}: {json.dumps(res, ensure_ascii=False)[:300]}")
        if not schema:
            return text, usage
        import jsonschema
        try:
            answer = parse(text)
            jsonschema.validate(answer, schema)
        except (json.JSONDecodeError, jsonschema.ValidationError) as unfit:  # not an answer: the next run asks again
            raise RuntimeError(f"{seat} answered in another shape: {str(unfit)[:160]}") from unfit
        return answer, usage
    raise SystemExit(f"a seat is claude:, codex:, gemini: or openrouter: with <model>:<effort> and :search after it or not; not {seat!r}")


def key_of(name):
    """A key for a model paid by the call: from the environment, else the backend's .env. Never printed."""
    if os.environ.get(name):
        return os.environ[name]
    env = Path(".env")
    for line in env.read_text(encoding="utf-8").splitlines() if env.exists() else []:
        if line.startswith(name + "="):
            return line.split("=", 1)[1].strip()
    raise SystemExit(f"{name} is not set, in the environment or in .env")


def post(url, headers, body):
    """One JSON request. Wall when the account is out of credit or over its rate; any other refusal is a broken call."""
    import urllib.error
    import urllib.request

    request = urllib.request.Request(url, data=json.dumps(body).encode("utf-8"), headers={"Content-Type": "application/json", **headers}, method="POST")
    try:
        with urllib.request.urlopen(request, timeout=900) as response:
            return json.loads(response.read().decode("utf-8"))
    except urllib.error.HTTPError as error:
        text = error.read().decode("utf-8", "replace")[:300]
        raise (Wall if error.code in (402, 429) or WALL.search(text) else RuntimeError)(f"{error.code} {text}") from error
    except (urllib.error.URLError, TimeoutError, json.JSONDecodeError) as broken:
        raise RuntimeError(f"no answer from {url.split('/')[2]}: {str(broken)[:200]}") from broken


def parse(text):
    t = text.strip()
    if t.startswith("```"):
        t = t.split("\n", 1)[1].rsplit("```", 1)[0]
    return json.loads(t)


class Loop:
    """One run's settings, and the counting of what it bought."""

    def __init__(self, folder="review-loop", seats=None, faults=LOOPED, steps=STEPS, dry=False, stop_file="", asker=ask, per_call=10, answers=None):
        self.folder, self.seats, self.faults, self.steps = folder, {**SEATS, **(seats or {})}, tuple(faults), tuple(steps)
        self.dry, self.stop_file, self.ask, self.per_call, self.answers = dry, stop_file, asker, per_call, answers
        self.bought, self.cost, self.lock, self.wall = Counter(), defaultdict(float), threading.Lock(), None

    def dir(self, lang):
        d = OUT / self.folder / lang
        d.mkdir(parents=True, exist_ok=True)
        return d

    def stopped(self):
        return self.wall is not None or bool(self.stop_file and Path(self.stop_file).exists())

    def unit(self, item, step, seat, user, system=None, schema=None):
        """The answer to one paid question about one entry: from its file when it was asked before, else asked now.
        Stopped when the run may not ask (a step left out, a dry run, the stop file, another call met the wall)."""
        path = self.dir(item["lang"]) / f"{item['headword']}.{item['entry_sha']}.{step}.{sha(step, LOOP_VERSION, seat, system or '', user)}.json"
        if path.exists():
            return json.loads(path.read_text(encoding="utf-8"))["answer"]
        if self.dry or step not in self.steps or self.stopped():
            with self.lock:
                self.bought[step + " to ask"] += 1
            raise Stopped(step)
        try:
            answer, usage = self.ask(seat, user, system, schema)
        except Wall as wall:
            self.wall = str(wall)[:120]
            raise
        record = {"step": step, "loop_version": LOOP_VERSION, "seat": seat, "language": item["lang"], "headword": item["headword"],
                  "entry_sha": item["entry_sha"], "asked": user, "answer": answer, "usage": usage}
        path.write_text(json.dumps(record, ensure_ascii=False, indent=1) + "\n", encoding="utf-8")
        with self.lock:
            self.bought[step] += 1
            self.cost[step] += usage.get("cost_usd") or 0
        return answer


ADDED_BY_INGEST = ("id", "homograph", "retired", "sense_ids", "patterns")  # inside a lexeme; the writers' schema knows none of them


def bare(entry):
    """The entry as the writer wrote it, in the places a patch names: what ingest added to tell lexemes and senses
    apart and to read frames is left out, and nothing else, so that a path into this is a path into the entry."""
    def strip(node):
        if isinstance(node, dict):
            return {k: strip(v) for k, v in node.items() if k not in ADDED_BY_INGEST}
        return [strip(v) for v in node] if isinstance(node, list) else node
    return {"language": entry.get("language"), "headword": entry.get("headword"), "lexemes": strip(entry.get("lexemes") or [])}


def shown_to_writer(entry):
    return json.dumps({k: v for k, v in bare(entry).items() if k in ("headword", "lexemes")}, ensure_ascii=False, indent=1)


def listed(findings, more=None):
    return "\n".join(f"{f['n']}. [{f['fault']}] {f.get('where', '')}: {f.get('what', '')}" + (f"\n   {more[f['n']]}" if more else "") for f in findings)


def read_identity(loop, lang):
    """What a reading is the answer to: the instructions and the reader, or the chat a sample's answers came from."""
    if loop.answers:
        return sha("read", "answers", loop.answers)
    return sha("read", LOOP_VERSION, loop.seats["reader"], measure.REVIEW, measure.OURS.get(lang, ""))


def read_path(loop, item):
    return loop.dir(item["lang"]) / f"{item['headword']}.{item['entry_sha']}.read.{read_identity(loop, item['lang'])}.json"


def numbered(findings):
    return [{"n": n, **{k: f.get(k) for k in ("fault", "where", "what", "misleads")}} for n, f in enumerate(findings, 1)]


def read(loop, lang, items):
    """Step 1 for the entries not read yet, several to a call. An entry the reader's answer leaves out is asked
    again by the next run, with whatever else is unread then."""
    unread = [item for item in items if not read_path(loop, item).exists()]
    if loop.answers:  # a chat's answers, filed with a sample: nothing to buy, and an entry the chat left out stays unread
        for item in unread:
            if item.get("read") is not None:
                record = {"step": "read", "loop_version": LOOP_VERSION, "seat": f"chat:{loop.answers}", "language": lang, "headword": item["headword"],
                          "entry_sha": item["entry_sha"], "answer": numbered(item["read"]), "usage": {}}
                read_path(loop, item).write_text(json.dumps(record, ensure_ascii=False, indent=1) + "\n", encoding="utf-8")
        return
    for start in range(0, len(unread), loop.per_call):
        batch = unread[start:start + loop.per_call]
        if loop.dry or "read" not in loop.steps or loop.stopped():
            loop.bought["read to ask"] += len(batch)
            continue
        text = measure.REVIEW.format(name=LANG_NAME[lang], part=1, parts=1, ours=measure.OURS.get(lang, ""),
                                     entries="\n\n".join(f"### {n}. {item['headword']}\n\n{measure.show(item['entry'])}" for n, item in enumerate(batch, 1)))
        try:
            answer, usage = loop.ask(loop.seats["reader"], text)
        except Wall as wall:
            loop.wall = str(wall)[:120]
            return
        found = measure.answers_of(answer)
        for n, item in enumerate(batch, 1):
            if n in found:
                record = {"step": "read", "loop_version": LOOP_VERSION, "seat": loop.seats["reader"], "language": lang, "headword": item["headword"],
                          "entry_sha": item["entry_sha"], "answer": numbered(found[n]), "usage": {**usage, "shared_by": len(batch)}}
                read_path(loop, item).write_text(json.dumps(record, ensure_ascii=False, indent=1) + "\n", encoding="utf-8")
                loop.bought["read"] += 1
        loop.cost["read"] += usage.get("cost_usd") or 0


def patch_of(answer):
    """An answer's patch with each value read as the JSON it is; a value that is no JSON is the word itself."""
    ops = []
    for op in answer.get("patch") or []:
        value = op.get("value", "")
        try:
            value = json.loads(value) if isinstance(value, str) and op.get("op") != "remove" else value
        except json.JSONDecodeError:
            pass
        ops.append({"op": op.get("op"), "path": op.get("path"), **({} if op.get("op") == "remove" else {"value": value})})
    return ops


def by_number(answer, key):
    return {row.get("n"): row for row in (answer or {}).get(key) or [] if isinstance(row, dict)}


def schema_errors(entry):
    import jsonschema
    if not hasattr(schema_errors, "validator"):
        schema_errors.validator = jsonschema.Draft202012Validator(json.loads((HERE / "entry.schema.v4.json").read_text(encoding="utf-8")))
    # counted by what is wrong and in which kind of place, not by the index: a sense put in before another moves it
    return Counter((re.sub(r"\d+", "N", "/".join(str(p) for p in e.absolute_path)), e.message[:160]) for e in schema_errors.validator.iter_errors(bare(entry)))


def refused(lang, before, after, table):
    """Why a patch is not taken, or None: it changes nothing, breaks the shape, or sets a fact against the sources."""
    if after == before:
        return "changes nothing"
    broken = schema_errors(after) - schema_errors(before)
    if broken:
        place, message = sorted(broken)[0]
        return f"breaks the shape at {place}: {message}"
    if table is not None:
        agreed = {(f["fact"], f["lexeme"], f.get("variety")) for f in witness.findings(lang, before, table) if f["verdict"] == "agrees"}
        against = [f for f in witness.findings(lang, after, table) if f["verdict"] == "differs" and (f["fact"], f["lexeme"], f.get("variety")) in agreed]
        if against:
            return f"against the sources: {against[0]['detail'][:160]}"
    return None


def settle(loop, item, findings, answers, rulings, mends, table):
    """Step 5: what became of each finding, and the operations ingest is to apply. Nothing is asked here."""
    lang, entry = item["lang"], item["entry"]
    ops, outcomes = [], []
    for f in findings:
        n, answer, ruling, mend = f["n"], answers.get(f["n"]), rulings.get(f["n"]), mends.get(f["n"])
        outcome = {**f, "writer": (answer or {}).get("verdict"), "writer_reason": (answer or {}).get("reason")}
        patch = None
        if answer is None:
            outcome["end"] = "unanswered"
        elif answer.get("verdict") == "accept":
            patch, outcome["by"] = patch_of(answer), "both"
        elif ruling is None:
            outcome["end"] = "disputed, not ruled yet"
        else:
            outcome.update(judge="upheld" if ruling.get("upheld") else "dismissed", judge_reason=ruling.get("reason"))
            if not ruling.get("upheld"):
                outcome["end"] = "dismissed"
            elif mend is None:
                outcome["end"] = "upheld, not mended yet"
            elif mend.get("verdict") != "accept":
                outcome.update(end="upheld, and the writer could not mend it", mend_reason=mend.get("reason"))
            else:
                patch, outcome["by"] = patch_of(mend), "judge"
        if patch is not None:
            try:
                before = apply_ops(entry, ops)
                why = "no patch" if not patch else refused(lang, before, apply_ops(entry, ops + patch), table)
            except PatchError as error:
                why = f"cannot be applied: {error}"
            if why:
                outcome.update(end="patch refused", refused=why, patch=patch)
            else:
                for op in patch:  # what the patch saw, so that it never lands on another text
                    was = value_at(entry, op["path"])
                    ops.append({**op, "n": n, **({} if op["op"] == "add" or was is MISSING else {"was": was})})
                outcome.update(end="mended", patch=patch)
        outcomes.append(outcome)
    record = {"language": lang, "headword": item["headword"], "loop_version": LOOP_VERSION, "entry_sha": item["entry_sha"], "raw_sha": item.get("raw_sha"),
              "part": item.get("part"), "seats": loop.seats if not loop.answers else {**loop.seats, "reader": f"chat:{loop.answers}"},
              "faults": list(loop.faults), "outcomes": outcomes, "ops": ops}
    if item.get("original") is not None:  # a planted fault: is the entry what it was before the planting, there
        mended = apply_ops(entry, ops)
        record["restored"] = all(value_at(mended, path) == was for path, was, _ in differences(item["original"], entry) if was is not MISSING)
    (loop.dir(lang) / f"{item['headword']}.{item['entry_sha']}.outcome.json").write_text(json.dumps(record, ensure_ascii=False, indent=1) + "\n", encoding="utf-8")
    return record


def one(loop, item, table):
    """Steps 2 to 5 for one entry that has been read. Says how far it got."""
    path = read_path(loop, item)
    if not path.exists():
        return "not read"
    lang, headword = item["lang"], item["headword"]
    findings = [f for f in json.loads(path.read_text(encoding="utf-8"))["answer"] if f.get("fault") in loop.faults]
    answers, rulings, mends, got = {}, {}, {}, "settled"
    try:
        if findings:
            system, shown = system_prompt(lang, item.get("prompt_version") or PROMPT_VERSION), shown_to_writer(item["entry"])
            asked = REPLY.format(name=LANG_NAME[lang], how=HOW_TO_PATCH, headword=headword, entry=shown, findings=listed(findings))
            answers = by_number(loop.unit(item, "reply", loop.seats["writer"], asked, system, ANSWERS), "answers")
            disputed = [f for f in findings if (answers.get(f["n"]) or {}).get("verdict") == "reject"]
            if disputed:
                asked = RULE.format(name=LANG_NAME[lang], headword=headword, shown=measure.show(item["entry"]),
                                    faults=part_of_review("## The faults\n\n", "\nDo not report").replace("{name}", LANG_NAME[lang]),
                                    definitions=part_of_review("not faults:\n\n", "{ours}").replace("{name}", LANG_NAME[lang]) + "\n" + measure.OURS.get(lang, ""),
                                    findings=listed(disputed, {n: f"The writer: {a.get('reason', '')}" for n, a in answers.items()}))
                rulings = by_number(loop.unit(item, "rule", loop.seats["judge"], asked, None, RULINGS), "rulings")
            upheld = [f for f in disputed if (rulings.get(f["n"]) or {}).get("upheld")]
            if upheld:
                asked = MEND.format(name=LANG_NAME[lang], how=HOW_TO_PATCH, headword=headword, entry=shown,
                                    findings=listed(upheld, {n: f"The third reader: {r.get('reason', '')}" for n, r in rulings.items()}))
                mends = by_number(loop.unit(item, "mend", loop.seats["writer"], asked, system, ANSWERS), "answers")
    except Stopped as stopped:
        got = f"waits for {stopped}"
    except Wall:
        got = "wall"
    except (RuntimeError, json.JSONDecodeError) as broken:  # a call that broke is not an answer: the next run asks again
        print(f"{lang} {headword}: {str(broken)[:160]}", flush=True)
        got = "error"
    record = settle(loop, item, findings, answers, rulings, mends, table)
    ends = Counter(o["end"] for o in record["outcomes"])
    print(f"{lang} {headword:20} {got:16} " + (", ".join(f"{k} {v}" for k, v in ends.items()) or "nothing to answer"), flush=True)
    return got


def written_items(lang, version, take):
    """The entries of a prompt version as ingest would publish them without this loop, the most frequent words first."""
    from wordfreq import zipf_frequency
    rows = [(headword, raw, meta) for headword, (raw, meta) in ingest_v4.load_raw(lang).items() if not version or meta.get("prompt_version") == version]
    rows.sort(key=lambda row: (-zipf_frequency(row[0], lang), row[0]))
    items = []
    for headword, raw, meta in rows[:take or None]:
        clean = ingest_v4.clean_entry(lang, copy.deepcopy(raw), meta, review=False)
        if clean.get("lexemes"):
            items.append({"lang": lang, "headword": headword, "entry": clean, "entry_sha": sha_of(clean), "raw_sha": sha_of(raw), "prompt_version": meta.get("prompt_version")})
    return items


def sample_items(name, lang, take, answers):
    """A measured sample's entries (measure.py), each as its reader saw it: the planted ones with the entry they were
    made from, and each with the chat's findings where it has an answer. An entry whose text has moved since the
    review file was written is left out, because the findings are about the text of then."""
    _, found, _ = measure.sample_entries(name, lang)
    read, moved = {}, 0
    for f in sorted((measure.MEASURE / (answers or name) / "answers").glob(f"{lang}-*.md")):
        read.update(measure.answers_of(f.read_text(encoding="utf-8")))
    files = "\n".join(f.read_text(encoding="utf-8") for f in sorted((measure.MEASURE / name / "review").glob(f"{lang}-*.md")))
    written = ingest_v4.load_raw(lang)
    items = []
    for item, clean, meta in found:
        if f"### {item['id']}. {item['headword']}\n\n{measure.show(clean)}" not in files:
            moved += 1
            continue
        row = {"lang": lang, "headword": item["headword"], "entry": clean, "entry_sha": sha_of(clean), "part": item["part"],
               "prompt_version": None if item["part"] == "gold" else meta.get("prompt_version"), "read": read.get(item["id"]) if answers else None}
        if item["part"] == "planted" and item["headword"] in written:
            raw, raw_meta = written[item["headword"]]
            row["original"] = ingest_v4.clean_entry(lang, copy.deepcopy(raw), raw_meta, review=False)
            row["prompt_version"] = raw_meta.get("prompt_version")
        items.append(row)
    if moved:
        print(f"{lang}: {moved} entries left out, their text has changed since the sample was shown", flush=True)
    if take:  # a few of each part, so that a small trial has planted faults, checked entries and ordinary ones
        parts = defaultdict(list)
        for row in items:
            parts[row["part"]].append(row)
        items = [row for rows in parts.values() for row in rows[:take]]
    return items


GENDERS = {"Masc": "masculine", "Fem": "feminine", "Neut": "neuter"}
DECOYS = {"gender": ("fact", "lexeme 1 grammar gender", "{headword} is a {now} noun in standard {name}, where the entry gives {was}."),
          "stress": ("fact", "lexeme 1 pronunciation", "The stress falls on another syllable: the word is said {now}, where the entry gives {was}."),
          "countability": ("claim", "lexeme 1 grammar countability",
                           "{headword} is an uncountable noun in ordinary use, without a plural and without the indefinite article, where the entry marks it countable.")}


def decoy_items(name, lang, skip=0):
    """Entries that are right, each with one finding that is false: the value a planted fault of the sample put
    there, asserted as the truth against the entry as written. The loop should end every one of them dismissed.
    A writer that accepts them is agreeing with its reviewer, not judging the finding."""
    items = json.loads((measure.MEASURE / name / f"sample-{lang}.json").read_text(encoding="utf-8"))
    put = json.loads((measure.MEASURE / name / f"planted-{lang}.json").read_text(encoding="utf-8"))
    written = ingest_v4.load_raw(lang)
    rows = []
    for headword in [i["headword"] for i in items if i["part"] == "planted"][skip:]:
        spec = put[headword]
        if spec["kind"] not in DECOYS or headword not in written:
            continue
        raw, meta = written[headword]
        clean = ingest_v4.clean_entry(lang, copy.deepcopy(raw), meta, review=False)
        fault, where, what = DECOYS[spec["kind"]]
        what = what.format(headword=headword, name=LANG_NAME[lang], now=GENDERS.get(spec["now"], spec["now"]), was=GENDERS.get(spec["was"], spec["was"]))
        rows.append({"lang": lang, "headword": headword, "entry": clean, "entry_sha": sha_of(clean), "part": "decoy", "prompt_version": meta.get("prompt_version"),
                     "read": [{"fault": fault, "where": where, "what": what, "misleads": True}]})
    return rows


TRUTHS = {"gender": DECOYS["gender"], "stress": DECOYS["stress"],
          "countability": ("claim", "lexeme 1 grammar countability",
                           "{headword} is a countable noun in ordinary use, with a plural and the indefinite article, where the entry marks it uncountable.")}
REBUTTALS = {"gender": "Dictionaries of {name} give {headword} as a {has} noun; the entry is right as it stands.",
             "stress": "The stress is where the entry puts it: dictionaries of {name} transcribe the word {has}.",
             "countability": "Learner's dictionaries of {name} mark {headword} as {has}; the entry is right as it stands."}
COUNTED = {"Count": "countable", "Mass": "uncountable"}


def judge_items(name, lang, take):
    """A third model's own test, from a sample's planted faults: for each, the entry with the fault and a finding
    that names it (true), and the entry as written with a finding that asserts the fault (false). Both carry the
    same rejection from a "writer", word for word but for the value, so that nothing in the tone tells them apart."""
    items = json.loads((measure.MEASURE / name / f"sample-{lang}.json").read_text(encoding="utf-8"))
    put = json.loads((measure.MEASURE / name / f"planted-{lang}.json").read_text(encoding="utf-8"))
    written = ingest_v4.load_raw(lang)
    rows = []
    for headword in [i["headword"] for i in items if i["part"] == "planted"]:
        spec = put[headword]
        if spec["kind"] not in TRUTHS or headword not in written or len(rows) >= 2 * (take or 99):
            continue
        raw, meta = written[headword]
        original = ingest_v4.clean_entry(lang, copy.deepcopy(raw), meta, review=False)
        named = {k: GENDERS.get(spec[k], COUNTED.get(spec[k], spec[k])) for k in ("was", "now")}
        if spec["kind"] == "countability":  # the entry's own value, not the planted file's wording of it
            named = {"was": "countable", "now": "uncountable"}
        for true, entry, table, says, has in ((True, spec["entry"], TRUTHS, ("was", "now"), "now"), (False, original, DECOYS, ("now", "was"), "was")):
            fault, where, what = table[spec["kind"]]
            what = what.format(headword=headword, name=LANG_NAME[lang], now=named[says[0]], was=named[says[1]])
            rows.append({"lang": lang, "headword": headword, "entry": entry, "entry_sha": sha_of(entry), "true": true, "kind": spec["kind"],
                         "finding": {"n": 1, "fault": fault, "where": where, "what": what},
                         "rebuttal": REBUTTALS[spec["kind"]].format(headword=headword, name=LANG_NAME[lang], has=named[has])})
    return rows


def judge_test(loop, langs, name, take, workers):
    """Asks the third seat to rule on each pair and says how often it upheld what was true and dismissed what was false."""
    got, missed = Counter(), []

    def rule(item):
        lang = item["lang"]
        asked = RULE.format(name=LANG_NAME[lang], headword=item["headword"], shown=measure.show(item["entry"]),
                            faults=part_of_review("## The faults\n\n", "\nDo not report").replace("{name}", LANG_NAME[lang]),
                            definitions=part_of_review("not faults:\n\n", "{ours}").replace("{name}", LANG_NAME[lang]) + "\n" + measure.OURS.get(lang, ""),
                            findings=listed([item["finding"]], {1: f"The writer: {item['rebuttal']}"}))
        try:
            ruling = by_number(loop.unit(item, "rule", loop.seats["judge"], asked, None, RULINGS), "rulings").get(1) or {}
        except (Stopped, Wall):
            return item, None
        except (RuntimeError, json.JSONDecodeError) as broken:
            print(f"{lang} {item['headword']}: {str(broken)[:160]}", flush=True)
            return item, None
        return item, ruling

    rows = [item for lang in langs for item in judge_items(name, lang, take)]
    with ThreadPoolExecutor(workers) as pool:
        for item, ruling in pool.map(rule, rows):
            side = "true" if item["true"] else "false"
            if ruling is None:
                got[side, "not ruled"] += 1
                continue
            right = bool(ruling.get("upheld")) == item["true"]
            got[side, "right" if right else "wrong"] += 1
            got[item["kind"], "right" if right else "wrong"] += 1
            if not right:
                missed.append(f"  {item['lang']} {item['headword']} ({item['kind']}): {'dismissed a true finding' if item['true'] else 'upheld a false finding'}: "
                              f"{item['finding']['what'][:90]} | {str(ruling.get('reason'))[:150]}")
    print(f"\n{loop.seats['judge']} on {len(rows)} findings of {name}:")
    print(f"  true findings a writer rejected:  upheld {got['true', 'right']} of {got['true', 'right'] + got['true', 'wrong']}")
    print(f"  false findings a writer rejected: dismissed {got['false', 'right']} of {got['false', 'right'] + got['false', 'wrong']}")
    print("  by kind: " + ", ".join(f"{kind} {got[kind, 'right']} of {got[kind, 'right'] + got[kind, 'wrong']}" for kind in TRUTHS))
    if got["true", "not ruled"] or got["false", "not ruled"]:
        print(f"  not ruled: {got['true', 'not ruled'] + got['false', 'not ruled']}")
    print("\n".join(missed))
    print("asked: " + (", ".join(f"{step} {n}" + (f" (${loop.cost[step]:.2f})" if loop.cost[step] else "") for step, n in loop.bought.items()) or "nothing"))
    if loop.wall:
        print(f"stopped at a wall ({loop.wall}): run again when it has reset, and nothing is bought twice")


def report(folder, langs):
    """What the loop did with the findings on file, by fault, and what it did to planted faults and checked entries."""
    by_fault, parts, cost, refusals = defaultdict(Counter), defaultdict(Counter), defaultdict(float), Counter()
    for lang in langs:
        for path in sorted((OUT / folder / lang).glob("*.json")):
            record = json.loads(path.read_text(encoding="utf-8"))
            if not path.name.endswith(".outcome.json"):
                cost[record.get("step")] += (record.get("usage") or {}).get("cost_usd") or 0
                continue
            part = record.get("part") or "written"
            parts[part]["entries"] += 1
            parts[part]["with a finding looped"] += bool(record["outcomes"])
            parts[part]["changed"] += bool(record["ops"])
            if "restored" in record:
                parts[part]["planted fault put right"] += record["restored"]
            if part == "decoy":
                parts[part]["accepted by the writer"] += any(o.get("writer") == "accept" for o in record["outcomes"])
                parts[part]["upheld by the third"] += any(o.get("judge") == "upheld" for o in record["outcomes"])
            for o in record["outcomes"]:
                c = by_fault[o["fault"]]
                c["findings"] += 1
                c["accepted by the writer"] += o.get("writer") == "accept"
                c["upheld by the third"] += o.get("judge") == "upheld"
                c["dismissed by the third"] += o.get("judge") == "dismissed"
                c["mended"] += o["end"] == "mended"
                c["open"] += o["end"] in ("unanswered", "disputed, not ruled yet", "upheld, not mended yet")
                if o.get("refused"):
                    c["patch refused"] += 1
                    refusals[f"{lang} {record['headword']}: {o['refused'][:110]}"] += 1
    columns = ("findings", "accepted by the writer", "upheld by the third", "dismissed by the third", "mended", "patch refused", "open")
    print(f"{'fault':14}" + "".join(f"{c:>24}" for c in columns))
    for fault in measure.FAULTS:
        if fault in by_fault:
            print(f"{fault:14}" + "".join(f"{by_fault[fault][c]:>24}" for c in columns))
    for part, c in parts.items():
        print(f"{part}: {c['entries']} entries, {c['with a finding looped']} with a finding looped, {c['changed']} changed"
              + (f", planted fault put right in {c['planted fault put right']}" if part == "planted" else "")
              + (f"; the false finding accepted by the writer in {c['accepted by the writer']}, upheld by the third in {c['upheld by the third']}" if part == "decoy" else ""))
    for line in refusals:
        print(f"  refused  {line}")
    print("spent: " + ", ".join(f"{step} ${amount:.2f}" for step, amount in cost.items() if step))


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--langs", default=",".join(TARGET_LANGUAGES))
    ap.add_argument("--folder", default="review-loop", help="under out/. Only review-loop is applied by ingest; a trial names another, beginning review-")
    ap.add_argument("--take", type=int, default=0, help="entries a language, the most frequent first; with --sample, of each part")
    ap.add_argument("--version", default=PROMPT_VERSION, help="the prompt version whose entries are looped; empty for every version")
    ap.add_argument("--sample", default=None, help="loop a measured sample (measure/<name>) and not the dictionary's entries")
    ap.add_argument("--answers", default=None, help="with --sample: the readings are the chat answers filed under measure/<name>/answers")
    ap.add_argument("--decoys", default=None, help="loop right entries with one false finding each, made from this sample's planted faults")
    ap.add_argument("--skip", type=int, default=0, help="with --decoys: leave out the first planted entries of each language")
    ap.add_argument("--judge-test", default=None, help="measure the third seat alone on true and false findings made from this sample's planted faults")
    ap.add_argument("--reader", default=SEATS["reader"])
    ap.add_argument("--writer", default=SEATS["writer"])
    ap.add_argument("--judge", default=SEATS["judge"])
    ap.add_argument("--faults", default=",".join(LOOPED), help="the faults that go to the writer; 'all' for every one")
    ap.add_argument("--steps", default=",".join(STEPS), help="the paid steps this run may take")
    ap.add_argument("--per-call", type=int, default=10, help="entries the reader is shown in one call")
    ap.add_argument("--workers", type=int, default=3)
    ap.add_argument("--dry", action="store_true", help="count what the next step would ask and ask nothing")
    ap.add_argument("--stop-file", default="", help="the run stops asking when this file exists")
    ap.add_argument("--report", action="store_true")
    a = ap.parse_args()
    langs = a.langs.split(",")
    if not a.folder.startswith("review-"):
        sys.exit("--folder takes a name that begins review-: any other would be read as a writer's entries")
    if a.report:
        return report(a.folder, langs)
    loop = Loop(a.folder, {"reader": a.reader, "writer": a.writer, "judge": a.judge}, measure.FAULTS if a.faults == "all" else a.faults.split(","),
                a.steps.split(","), a.dry, a.stop_file, per_call=a.per_call, answers="decoys" if a.decoys else a.answers)
    if a.judge_test:
        return judge_test(loop, langs, a.judge_test, a.take, a.workers)
    got = Counter()
    for lang in langs:
        items = decoy_items(a.decoys, lang, a.skip) if a.decoys else sample_items(a.sample, lang, a.take, a.answers) if a.sample else written_items(lang, a.version, a.take)
        read(loop, lang, items)
        table = witness.facts(lang)
        with ThreadPoolExecutor(a.workers) as pool:
            got.update(pool.map(lambda item: one(loop, item, table), items))
    print("\n" + ", ".join(f"{k} {v}" for k, v in got.items()))
    print("asked: " + (", ".join(f"{step} {n}" + (f" (${loop.cost[step]:.2f})" if loop.cost[step] else "") for step, n in loop.bought.items()) or "nothing"))
    if loop.wall:
        print(f"stopped at the plan's wall ({loop.wall}): run again when it has reset, and nothing is bought twice")


if __name__ == "__main__":
    main()
