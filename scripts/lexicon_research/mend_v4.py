"""Mend an older entry into the v4 shape instead of writing the word again.

A v4 writer paid for a whole entry (about 1,300 output tokens) to add what v3 lacks: frames, labels on
separate axes, regions, antonyms per sense, typed relations, spellings by variety, stress readings, and
only the forms a learner must be told. Of 4,368 words written both ways on 11 November 2026, 92% had the
same lexemes in both shapes. So the older entry is converted by this program into a v4 draft, and the
model returns only a patch against it; the glosses and review cues the older entry already has are kept
beside it under out/carry/ for the per-sense passes, instead of being bought again.

Measured on 210 words also written fresh (16 November 2026, Opus 5.5, list price per word): a fresh v4
entry $0.044 with 777 frames and 30 faults check_v4.py finds; mend-v1 at medium $0.023 but only 546
frames; mend-v2 (this prompt) at high $0.040, at medium $0.037 with 902 frames and 6 faults, at low
$0.032 with 697 frames. Thinking is billed as output, so effort sets most of the price. mend-v2 at
medium is the default: a little cheaper than a fresh entry, fewer faults, and the glosses kept.

  draft(lang, entry)   the older entry in the v4 shape, no model call: register becomes labels, untyped
                       relations and usage notes ride along as _old_ fields for the model to read
  apply(draft, patch)  the v4 entry: the patch's fields replace the draft's, forms are trimmed to the
                       ones kept, senses dropped or added, relations replaced
  a patch whose verdict is "rewrite" writes nothing: the word is recorded under out/mend/ and written
  afresh later (claude_code_writer.py --headwords mend-rewrite --rewrite-older)

An older entry that rejects the headword becomes a v4 reject with no call.

Output: out/mend-<model>-v4/<lang>/<headword>.json and .meta.json, a v4 writer directory, so the writers
skip a mended word and check_v4.py reads it; --eval writes to out/mend-eval-<model>-<effort>/ instead, beside the
words a v4 writer already wrote, to compare the two.

Usage: mend_v4.py [--model claude-opus-5-5] [--effort medium] [--langs en,de] [--limit N] [--workers 2]
           [--headwords NAME] [--pause-session 100] [--eval]
       (run from the backend repository root; without --headwords every word with an older entry and no
        v4 entry, most frequent first)
"""
import argparse, copy, json, os, re, subprocess, sys, threading, time
from concurrent.futures import ThreadPoolExecutor
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
import jsonschema  # noqa: E402
import v3_entries  # noqa: E402
from prompting import HERE, REGIONS, SCHEMA_PATH, SCHEMA_VERSION, VARIETIES, system_prompt_v4  # noqa: E402

MEND_VERSION = "mend-v2"
OUT = HERE / "out"
EMPTY = Path(os.environ.get("LEXICON_WRITER_EMPTY_DIR", "/tmp/claude-1000/lexicon-writer-empty"))
SCHEMA = json.loads(SCHEMA_PATH.read_text())
DEFS = SCHEMA["$defs"]

# v3's one register becomes the v4 label axis it belongs to; "technical" has no axis (a domain is the model's call)
REGISTER = {"formal": ("formality", "formal"), "informal": ("formality", "informal"), "slang": ("formality", "slang"),
            "vulgar": ("attitude", "vulgar"), "offensive": ("attitude", "offensive"), "humorous": ("attitude", "humorous"),
            "derogatory": ("attitude", "derogatory"), "euphemistic": ("attitude", "euphemistic"),
            "literary": ("style", "literary"), "archaic": ("time", "archaic"), "dated": ("time", "dated")}
LIST_AXES = {"attitude", "style"}


# the older writers' habitual slips in grammar values, put right without a call
COERCE = {"countability": {"countable": "Count", "uncountable": "Mass", "count": "Count", "mass": "Mass", "both": "Both"}}


def _allowed(obj, props):
    out = {}
    for k, v in (obj or {}).items():
        if k not in props:
            continue
        if k == "conjugation" and isinstance(v, int):
            v = str(v)
        v = COERCE.get(k, {}).get(v, v) if isinstance(v, str) else v
        if k == "plural_class" and not (isinstance(v, str) and re.fullmatch(props[k].get("pattern", ".*"), v)):
            continue  # a description instead of an ending: the model gives it again if it is needed
        out[k] = v
    return out


def draft(lang, entry):
    """The older entry in the v4 shape, without a model call."""
    lexeme_props = DEFS["lexeme"]["properties"]
    grammar_props = lexeme_props["grammar"]["properties"]
    sense_grammar_props = DEFS["sense"]["properties"]["grammar"]["properties"]
    form_props = DEFS["form"]["properties"]
    out = {"language": lang, "headword": entry["headword"], "lexemes": []}
    if not entry.get("lexemes"):
        out["reject"] = entry.get("reject") or "no lexemes in the older entry"
        return out
    for old in entry["lexemes"]:
        lexeme = {"lemma": old["lemma"], "pos": old["pos"], "kind": "multiword" if old.get("kind") == "phrase" else "word"}
        if grammar := _allowed(old.get("grammar"), grammar_props):
            lexeme["grammar"] = grammar
        lexeme["pronunciations"] = [{"variety": p["variety"], "ipa": p["ipa"]} for p in old.get("pronunciations") or []
                                    if p.get("variety") in VARIETIES[lang]]
        forms = []
        for f in old.get("forms") or []:
            form = _allowed(f, form_props)
            if isinstance(form.get("ipa"), dict):
                form["ipa"] = {v: i for v, i in form["ipa"].items() if v in VARIETIES[lang]} or None
            forms.append({k: v for k, v in form.items() if v is not None})
        if forms:
            lexeme["forms"] = forms
        lexeme["senses"] = []
        for s in old.get("senses") or []:
            sense = {"definition": s["definition"], "example": s["example"], "cefr": s["cefr"]}
            axis = REGISTER.get(s.get("register"))
            if axis:
                sense["labels"] = {axis[0]: [axis[1]] if axis[0] in LIST_AXES else axis[1]}
            if grammar := _allowed(s.get("grammar"), sense_grammar_props):
                sense["grammar"] = grammar
            if s.get("usage"):
                sense["_old_usage"] = s["usage"]
            if s.get("register") == "technical":
                sense["_old_register"] = "technical"
            lexeme["senses"].append(sense)
        if old.get("relations"):
            lexeme["_old_relations"] = old["relations"]
        out["lexemes"].append(lexeme)
    return out


def carry(entry):
    """What the older entry has that v4 leaves to later passes: glosses and cues per sense, by position."""
    return [{"lemma": x["lemma"], "pos": x["pos"],
             "senses": [{"definition": s.get("definition"), "glosses": s.get("glosses"), "cues": s.get("cues")} for s in x.get("senses") or []]}
            for x in entry.get("lexemes") or []]


def patch_schema():
    """The shape of a patch, draft-07 as the CLI's validator takes it, built from the v4 entry schema."""
    lexeme = DEFS["lexeme"]["properties"]
    sense = DEFS["sense"]["properties"]
    ref = lambda name: {"$ref": f"#/definitions/{name}"}
    return {
        "type": "object", "additionalProperties": False, "required": ["verdict"],
        "properties": {
            "verdict": {"enum": ["keep", "patch", "rewrite", "reject"]},
            "reason": {"type": "string"},
            "lexemes": {"type": "array", "items": {
                "type": "object", "additionalProperties": False, "required": ["i"],
                "properties": {
                    "i": {"type": "integer", "minimum": 0},
                    "set": {"type": "object", "additionalProperties": False,
                            "properties": {k: lexeme[k] for k in ("spellings", "grammar", "pronunciations", "forms", "components", "separable") if k in lexeme}},
                    "keep_forms": {"type": "array", "items": {"type": "integer", "minimum": 0}},
                    "senses": {"type": "array", "items": {
                        "type": "object", "additionalProperties": False, "required": ["i"],
                        "properties": {"i": {"type": "integer", "minimum": 0}, "drop": {"type": "boolean"},
                                       "set": {"type": "object", "additionalProperties": False,
                                               "properties": {k: sense[k] for k in ("definition", "example", "cefr", "frames", "labels", "regions",
                                                                                   "standard_alternative", "grammar", "antonyms") if k in sense}}}}},
                    "add_senses": {"type": "array", "items": ref("sense")},
                    "relations": {"type": "array", "items": ref("relation")},
                }}},
        },
        "definitions": {k: v for k, v in DEFS.items()},
    }


def _draft07(schema):
    return json.loads(json.dumps(schema).replace("#/$defs/", "#/definitions/"))


PATCH_SCHEMA = _draft07(patch_schema())


def _strip_old(node):
    if isinstance(node, dict):
        return {k: _strip_old(v) for k, v in node.items() if not k.startswith("_old_")}
    if isinstance(node, list):
        return [_strip_old(v) for v in node]
    return node


def apply(base, patch):
    """The v4 entry the patch makes of the draft."""
    entry = copy.deepcopy(base)
    if patch.get("verdict") == "reject":
        return {"language": entry["language"], "headword": entry["headword"], "lexemes": [], "reject": patch.get("reason") or "not a lemma"}
    for change in patch.get("lexemes") or []:
        if not 0 <= change["i"] < len(entry["lexemes"]):
            raise ValueError(f"the patch names lexeme {change['i']} of {len(entry['lexemes'])}")
        lexeme = entry["lexemes"][change["i"]]
        if "keep_forms" in change:
            lexeme["forms"] = [f for k, f in enumerate(lexeme.get("forms") or []) if k in set(change["keep_forms"])]
        lexeme.update(copy.deepcopy(change.get("set") or {}))
        dropped = set()
        for s in change.get("senses") or []:
            if not 0 <= s["i"] < len(lexeme["senses"]):
                raise ValueError(f"the patch names sense {s['i']} of {len(lexeme['senses'])} in {lexeme['lemma']}")
            if s.get("drop"):
                dropped.add(s["i"])
            else:
                lexeme["senses"][s["i"]].update(copy.deepcopy(s.get("set") or {}))
        lexeme["senses"] = [s for k, s in enumerate(lexeme["senses"]) if k not in dropped] + copy.deepcopy(change.get("add_senses") or [])
        if "relations" in change:
            lexeme["relations"] = copy.deepcopy(change["relations"])
    entry = _strip_old(entry)
    for lexeme in entry["lexemes"]:
        for key in ("forms", "relations", "spellings"):
            if key in lexeme and not lexeme[key]:
                del lexeme[key]
        for sense in lexeme["senses"]:
            if "labels" in sense and not sense["labels"]:
                del sense["labels"]
    return entry


def system_prompt(lang):
    """The v4 writer's rules, then the mending instructions in place of its output section."""
    rules, _, _ = system_prompt_v4(lang).partition("\n# Output")
    from prompting import SCHEMA_EXAMPLE_V4
    mend = (HERE / "prompts" / "entry_mender" / f"{MEND_VERSION.removeprefix('mend-')}.md").read_text()
    return f"{rules}\n# The shape of an entry\n\n{SCHEMA_EXAMPLE_V4}\n\n{mend}"


def user_prompt(row, base):
    return f"Headword: {row['headword']}\nZipf frequency: {row['zipf']}\nDraft:\n{json.dumps(base, ensure_ascii=False)}"


_lock = threading.Lock()


def record_rewrite(lang, row, reason):
    d = OUT / "mend"
    d.mkdir(parents=True, exist_ok=True)
    with _lock, (d / f"rewrite-{lang}.jsonl").open("a") as f:
        f.write(json.dumps({**row, "reason": reason}, ensure_ascii=False) + "\n")


def meters():
    """Percent used of the five-hour session, as /usage shows it. No tokens."""
    r =subprocess.run(["claude", "-p", "--output-format", "json", "--no-session-persistence", "--strict-mcp-config",
                        "--mcp-config", "{\"mcpServers\": {}}"], input="/usage", capture_output=True, text=True, timeout=120, cwd=EMPTY)
    screen = json.loads(r.stdout)["result"]
    m = re.search(r"Current session: (\d+)% used", screen)
    return int(m.group(1)) if m else None


_checked = 0.0


def wait_for_session(pause_session):
    global _checked
    with _lock:
        while time.time() - _checked >= 180:
            try:
                session = meters()
            except Exception:
                session = None
            _checked = time.time()
            if session is None or session < pause_session:
                return
            print(f"waiting: {session}% of the session is used", flush=True)
            time.sleep(300)
            _checked = 0.0


def mend_one(a, lang, system, row, retries=0):
    folder = OUT / (f"mend-eval-{a.model}-{a.effort}" if a.eval else f"mend-{a.model}-v4") / lang
    folder.mkdir(parents=True, exist_ok=True)
    target = folder / f"{row['headword']}.json"
    if target.exists():
        return "skip"
    if not a.eval and any((d / f"{row['headword']}.json").exists() for d in OUT.glob(f"*-v4/{lang}")):
        return "skip"
    writer, older = v3_entries.older_entry(lang, row["headword"])
    if older is None:
        return "no older entry"
    base = draft(lang, older)
    # the backend's lexicon_build keeps model, effort and prompt_version; a mended entry also names who wrote it first
    older_meta_path = OUT / writer / lang / f"{row['headword']}.meta.json"
    older_meta = json.loads(older_meta_path.read_text()) if older_meta_path.exists() else {}
    meta = {"prompt_version": f"v4+{MEND_VERSION}", "mend_version": MEND_VERSION, "schema_version": SCHEMA_VERSION,
            "headword": row["headword"], "set": row.get("set"), "model": a.model, "effort": a.effort,
            "mended_from": writer, "older_model": older_meta.get("model"), "older_prompt_version": older_meta.get("prompt_version")}
    if base.get("reject"):
        target.write_text(json.dumps(base, ensure_ascii=False, indent=1) + "\n")
        (folder / f"{row['headword']}.meta.json").write_text(json.dumps({**meta, "verdict": "reject", "total_cost_usd": 0}, indent=1) + "\n")
        return "reject (free)"
    wait_for_session(a.pause_session)
    t0 = time.time()
    cmd = ["claude", "-p", user_prompt(row, base), "--model", a.model, "--effort", a.effort, "--tools", "",
           "--system-prompt", system, "--json-schema", json.dumps(PATCH_SCHEMA), "--output-format", "json",
           "--no-session-persistence", "--strict-mcp-config", "--mcp-config", "{\"mcpServers\": {}}"]
    r = subprocess.run(cmd, capture_output=True, text=True, timeout=600, cwd=EMPTY)
    meta["wall_s"] = round(time.time() - t0, 1)
    try:
        res = json.loads(r.stdout)
    except json.JSONDecodeError:
        res = {"is_error": True, "result": (r.stderr or r.stdout)[-300:]}
    if res.get("is_error"):
        if retries >= 144:
            raise RuntimeError(f"still failing after a day: {res.get('result', '')[:200]}")
        print(f"error at {row['headword']} ({str(res.get('result'))[:80]!r}); waiting ten minutes", flush=True)
        time.sleep(600)
        return mend_one(a, lang, system, row, retries + 1)
    u = res.get("usage") or {}
    meta.update({"total_cost_usd": res.get("total_cost_usd"), "input_tokens": u.get("input_tokens"), "output_tokens": u.get("output_tokens"),
                 "cache_read": u.get("cache_read_input_tokens"), "cache_write": u.get("cache_creation_input_tokens")})
    patch = res.get("structured_output")
    try:
        if patch is None:
            patch = json.loads((res.get("result") or "").strip().strip("`").removeprefix("json"))
        meta["verdict"] = patch.get("verdict")
        meta["patch"] = patch
        if patch.get("verdict") == "rewrite":
            record_rewrite(lang, row, patch.get("reason", ""))
            status = "rewrite"
        else:
            entry = apply(base, patch)
            jsonschema.validate(entry, SCHEMA)
            target.write_text(json.dumps(entry, ensure_ascii=False, indent=1) + "\n")
            carried = OUT / "carry" / lang
            carried.mkdir(parents=True, exist_ok=True)
            (carried / f"{row['headword']}.json").write_text(json.dumps(carry(older), ensure_ascii=False, indent=1) + "\n")
            status = patch.get("verdict")
    except Exception as e:  # an unusable patch: recorded with the meta, written again on the next run
        meta["error"] = f"{type(e).__name__}: {str(e)[:300]}"
        status = "error"
    (folder / f"{row['headword']}.meta.json").write_text(json.dumps(meta, ensure_ascii=False, indent=1) + "\n")
    if status in ("error", "rewrite"):
        (folder / f"{row['headword']}.meta.json").rename(folder / f"{row['headword']}.{status}.meta.json")
    print(f"mend {lang} {row['headword']:22} {status:8} {meta['wall_s']}s out={meta.get('output_tokens')} cost~${meta.get('total_cost_usd')}", flush=True)
    return status


def queue(lang, name):
    """The words to mend: a headword list, or every word with an older entry and no v4 entry, most frequent first."""
    if name:
        rows = json.loads((HERE / "headwords" / f"{name}-{lang}.json").read_text())
    else:
        from wordfreq import zipf_frequency
        rows = sorted(({"headword": h, "zipf": zipf_frequency(h, lang), "set": "mend"} for h in v3_entries.headwords(lang)),
                      key=lambda r: -r["zipf"])
    return [r for r in rows if v3_entries.has_older(lang, r["headword"])]


if __name__ == "__main__":
    ap = argparse.ArgumentParser()
    ap.add_argument("--model", default="claude-opus-5-5")
    ap.add_argument("--effort", default="medium")
    ap.add_argument("--langs", default=",".join(REGIONS))
    ap.add_argument("--limit", type=int, default=0)
    ap.add_argument("--workers", type=int, default=2)
    ap.add_argument("--headwords", default="")
    ap.add_argument("--pause-session", type=int, default=100)
    ap.add_argument("--eval", action="store_true", help="write to out/mend-eval-<model>-<effort>/, beside words a v4 writer already wrote")
    a = ap.parse_args()
    EMPTY.mkdir(parents=True, exist_ok=True)
    for lang in a.langs.split(","):
        rows = queue(lang, a.headwords)
        if a.limit:
            rows = rows[:a.limit]
        system = system_prompt(lang)
        with ThreadPoolExecutor(a.workers) as ex:
            list(ex.map(lambda r: mend_one(a, lang, system, r), rows))
    print("done")
