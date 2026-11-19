"""Operations on an entry's JSON, as the review loop's patches state them (review_v4.py) and ingest applies them.

An operation is {"op": "replace" | "remove" | "add", "path": a JSON Pointer from the entry's root, "value"}, and
may carry "was": what stood at the path when the patch was written. Every path of one patch is into the entry as
it was before the patch, so an operation that adds or removes an item does not move the places the others name.
"""
import copy
import hashlib
import json

MISSING = object()


class PatchError(ValueError):
    """An operation that cannot be done on this entry."""


def sha_of(entry):
    """An entry's identity by its content."""
    return hashlib.sha256(json.dumps(entry, ensure_ascii=False, sort_keys=True).encode("utf-8")).hexdigest()[:12]


def steps(path):
    if not isinstance(path, str) or not path.startswith("/"):
        raise PatchError(f"path {path!r} does not start at the root")
    return [step.replace("~1", "/").replace("~0", "~") for step in path[1:].split("/")]


def locate(doc, path):
    """(the object or list that holds the place, its key or index; None for the end of a list)."""
    *way, last = steps(path)
    at = doc
    for step in way:
        if isinstance(at, list) and step.isdigit() and int(step) < len(at):
            at = at[int(step)]
        elif isinstance(at, dict) and step in at:
            at = at[step]
        else:
            raise PatchError(f"{path}: nothing at {step!r}")
    if isinstance(at, list):
        if last == "-":
            return at, None
        if not last.isdigit():
            raise PatchError(f"{path}: a list has no place {last!r}")
        return at, int(last)
    if isinstance(at, dict):
        return at, last
    raise PatchError(f"{path}: nothing can stand inside {type(at).__name__}")


def value_at(doc, path):
    """What stands at the path, or MISSING."""
    try:
        parent, key = locate(doc, path)
    except PatchError:
        return MISSING
    if key is None:
        return MISSING
    if isinstance(parent, list):
        return parent[key] if key < len(parent) else MISSING
    return parent.get(key, MISSING)


def apply_ops(entry, ops):
    """A copy of the entry with the operations done. PatchError when one cannot be, or when what it replaces or
    removes is not what the patch saw ("was"): the entry has been written again, or a rule has changed it since."""
    doc = copy.deepcopy(entry)
    placed = []
    for order, op in enumerate(ops):
        kind, path = op.get("op"), op.get("path")
        parent, key = locate(doc, path)
        if kind in ("replace", "remove"):
            present = value_at(doc, path)
            if present is MISSING:
                raise PatchError(f"{path}: nothing there to {kind}")
            if "was" in op and present != op["was"]:
                raise PatchError(f"{path}: the entry no longer has what the patch saw")
        elif kind == "add":
            if isinstance(parent, list) and key is not None and key > len(parent):
                raise PatchError(f"{path}: the list has {len(parent)} items")
            if isinstance(parent, dict) and key in parent:
                raise PatchError(f"{path}: already there, which is a replace")
        else:
            raise PatchError(f"unknown operation {kind!r}")
        placed.append((order, kind, parent, key, copy.deepcopy(op.get("value"))))
    for _, kind, parent, key, value in placed:  # what is put in place of something moves nothing
        if kind == "replace":
            parent[key] = value
    # in a list, from its end to its start, so that every index is still the one the patch named; at one index
    # the item goes before the new one comes, and new ones keep the order they were given in
    in_lists = [p for p in placed if p[1] != "replace" and isinstance(p[2], list) and p[3] is not None]
    for _, kind, parent, key, value in sorted(in_lists, key=lambda p: (-p[3], p[1] == "add", -p[0])):
        if kind == "remove":
            del parent[key]
        else:
            parent.insert(key, value)
    for _, kind, parent, key, value in placed:
        if kind == "replace" or (isinstance(parent, list) and key is not None):
            continue
        if isinstance(parent, list):
            parent.append(value)
        elif kind == "add":
            parent[key] = value
        else:
            del parent[key]
    return doc


def differences(one, other, at=""):
    """The places where two entries differ, each (path, value in one, value in other); a list of another length
    is one difference."""
    if isinstance(one, dict) and isinstance(other, dict):
        found = []
        for key in sorted(set(one) | set(other)):
            if key not in one or key not in other:
                found.append((f"{at}/{key}", one.get(key, MISSING), other.get(key, MISSING)))
            else:
                found += differences(one[key], other[key], f"{at}/{key}")
        return found
    if isinstance(one, list) and isinstance(other, list) and len(one) == len(other):
        return [d for n, (a, b) in enumerate(zip(one, other)) for d in differences(a, b, f"{at}/{n}")]
    return [] if one == other else [(at, one, other)]
