"""Settle an entry's fact disagreements with its evidence by small patches, once, and keep only what verifies.

A fact finding (a gender, a plural, a transcription, a citation that differs from the Wiktionary evidence) goes to a
cheap model with the entry and the evidence. For each finding it answers "fix", with a JSON Patch (RFC 6902) that
changes only that fact, or "keep", saying why the entry is right (the evidence uses another convention, or is
itself wrong). Code then decides, not the model: an operation may only touch the lexeme its finding is about; the
patched entry must pass the free checks without a new structural error and must disagree with the evidence on
fewer facts than before. Otherwise the entry stays as written. There is no second round: what is still disputed
after one is kept as disputed, never argued into agreement.

Judgments (which senses to keep, how to word them) are never patched here; coverage findings are only counted.
"""

import copy
import json
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
import entry_checks
import evidence_checks
import grounded_writer

PROMPT = """You settle disagreements between a dictionary entry and Wiktionary evidence about facts: a lexeme's
gender, its plural, a transcription, or which evidence senses an entry sense covers. For each numbered finding,
decide:
- "fix" when the evidence is right: give a JSON Patch (RFC 6902: op add, replace or remove, path a JSON Pointer
  into the entry) that changes only that fact, in the entry's own conventions (IPA in the house symbols, with a
  primary stress mark on every word and dots between syllables; gender as Masc, Fem, Neut or Com).
- "keep" when the entry is right: the evidence uses another notation for the same sounds, describes another
  lexeme, or is wrong. Say why in a few words.
Change nothing a finding does not name. Return JSON only:
{"decisions": [{"finding": <index>, "decision": "fix"|"keep", "why": <short>, "patch": [<ops>]}]}"""


def resolve(document, pointer):
    """The parent container and the last key of a JSON Pointer."""
    parts = [part.replace("~1", "/").replace("~0", "~") for part in pointer.lstrip("/").split("/")]
    parent = document
    for part in parts[:-1]:
        parent = parent[int(part)] if isinstance(parent, list) else parent[part]
    last = parts[-1]
    return parent, (len(parent) if last == "-" else int(last)) if isinstance(parent, list) else last


def apply(document, ops):
    patched = copy.deepcopy(document)
    for op in ops:
        parent, key = resolve(patched, op["path"])
        if op["op"] == "remove":
            del parent[key]
        elif op["op"] == "add" and isinstance(parent, list):
            parent.insert(key, op["value"])
        elif op["op"] in ("add", "replace"):
            parent[key] = op["value"]
        else:
            raise ValueError(f"unsupported op {op['op']}")
    return patched


def lexeme_of(path):
    parts = path.split("/")
    return "/".join(parts[:3]) if len(parts) >= 3 and parts[1] == "lexemes" else None


def settle(entry, found, model="claude-haiku-5-5", effort="low"):
    """The entry after one round of settling, the decisions, and what the call cost; the entry unchanged when
    there is nothing to settle or the patch does not verify."""
    findings = [f for f in evidence_checks.compare(entry, found) if f["kind"] == "fact"]
    if not findings:
        return entry, {"findings": 0}, 0.0
    from wiktionary import render

    listing = "\n".join(f"{i}. {f['path']}: {f['check']}: {f['message']}" for i, f in enumerate(findings))
    user = (
        f"Findings:\n{listing}\n\nEvidence (Wiktionary):\n{render(found)}\n\n"
        f"Entry:\n{json.dumps(entry, ensure_ascii=False)}"
    )
    text, meta = grounded_writer.call_openrouter(model, PROMPT, user, effort)
    report = {"findings": len(findings), "usd": meta["usd"], "fixed": 0, "kept": 0, "refused": 0}
    try:
        decisions = grounded_writer.parse(text)["decisions"]
    except (ValueError, KeyError):
        report["refused"] = len(findings)
        return entry, report, meta["usd"] or 0
    ops = []
    for decision in decisions:
        index = decision.get("finding")
        if not isinstance(index, int) or not 0 <= index < len(findings):
            continue
        if decision.get("decision") != "fix":
            report["kept"] += 1
            continue
        scope = lexeme_of(findings[index]["path"])
        allowed = [op for op in decision.get("patch") or [] if scope and op.get("path", "").startswith(scope + "/")]
        if len(allowed) != len(decision.get("patch") or []) or not allowed:
            report["refused"] += 1
            continue
        ops += allowed
        report["fixed"] += 1
    if not ops:
        return entry, report, meta["usd"] or 0
    try:
        patched = apply(entry, ops)
    except (KeyError, IndexError, ValueError, TypeError):
        report["refused"] += report["fixed"]
        report["fixed"] = 0
        return entry, report, meta["usd"] or 0
    before_errors = {(f["check"], f["path"]) for f in entry_checks.check(entry) if f["severity"] == "error"}
    after_errors = {(f["check"], f["path"]) for f in entry_checks.check(patched) if f["severity"] == "error"}
    after_facts = [f for f in evidence_checks.compare(patched, found) if f["kind"] == "fact"]
    if after_errors - before_errors or len(after_facts) >= len(findings):
        report["refused"] += report["fixed"]
        report["fixed"] = 0
        report["reverted"] = True
        return entry, report, meta["usd"] or 0
    report["ops"] = ops
    return patched, report, meta["usd"] or 0
