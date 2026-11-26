"""The schema of an entry as it is published: what a writer returns (entry.schema.v4.json) and what ingest adds.

The backend takes the clean entry ingest_v4.py derives, not the written one: it carries the ids of its lexemes and
senses, the frames read into parts, the spellings it covers and what was dropped. The backend validates every
publication against the file this module writes, src/main/resources/lexicon/entry.schema.json, so the two cannot
drift: the file is derived from the writers' schema by code, and a test fails when it is stale.

Usage: published_schema.py   (run from the backend repository root; rewrites the backend's two files: the schema it
       validates a publication against, and the one it serves for download beside the Lexicon API page, see public())
"""
import copy, json
from pathlib import Path

HERE = Path(__file__).resolve().parent
WRITTEN = HERE / "entry.schema.v4.json"
PUBLISHED = HERE.parents[1] / "src" / "main" / "resources" / "lexicon" / "entry.schema.json"
# what the backend serves at /public/lexicon/schema, for the Lexicon API page's download: see public()
PUBLIC = PUBLISHED.with_name("entry.public.schema.json")

UUID = {"type": "string", "pattern": "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$"}
TEXT = {"type": "string", "minLength": 1}
MAYBE_TEXT = {"type": ["string", "null"]}


def closed(required, properties):
    return {"type": "object", "required": required, "additionalProperties": False, "properties": properties}


def published():
    schema = copy.deepcopy(json.loads(WRITTEN.read_text(encoding="utf-8")))
    schema["title"] = ("Lexicon entry as it is published, schema version 4: the entry a writer returned "
                       "(scripts/lexicon_research/entry.schema.v4.json) as ingest_v4.py cleans it. "
                       "Derived by published_schema.py; never edited by hand")
    schema["required"] += ["schema", "source"]
    schema["properties"].update({
        "schema": {"const": "4"},
        "aliases": {"type": "array", "minItems": 1, "items": TEXT,
                    "description": "Spellings that are not entries themselves and are answered by this one: an "
                                   "inflected form, another or an older spelling, a common misspelling."},
        "dropped": {"type": "array", "items": closed(["reason"], {"lemma": MAYBE_TEXT, "pos": MAYBE_TEXT, "reason": TEXT})},
        "retired_lexemes": {"type": "array", "items": closed(["id"], {"id": UUID, "lemma": MAYBE_TEXT, "pos": MAYBE_TEXT})},
        "source": closed([], {key: MAYBE_TEXT for key in ("writer", "model", "effort", "prompt_version", "prompt_sha")}),
    })
    # every other key of the entry is a count of what ingest put right (syllable_dots_added, frames_split)
    schema["additionalProperties"] = {"type": "integer", "minimum": 0}
    defs = schema["$defs"]
    lexeme = defs["lexeme"]
    lexeme["required"] += ["id", "homograph"]
    lexeme["properties"].update({
        "id": UUID,
        "homograph": {"type": "integer", "minimum": 1},
        "retired": {"type": "array", "items": closed(["id"], {"id": UUID, "definition": MAYBE_TEXT}),
                    "description": "Senses an earlier version of the lexeme had and this one has not."},
    })
    sense = defs["sense"]
    sense["required"] += ["id"]
    del sense["properties"]["from"]  # a note of the evidence pipeline's; ingest drops it
    sense["properties"].update({
        "id": UUID,
        "patterns": {"type": "array", "minItems": 1, "items": closed(["frame", "parts"], {
            "frame": {"type": "string", "minLength": 2},
            "parts": {"type": "array", "minItems": 1, "items": {"$ref": "#/$defs/part"}}}),
            "description": "Each frame a program can read, as its parts in order."},
    })
    defs["part"] = {
        "type": "object", "additionalProperties": False,
        "description": "One part of a frame: the headword, a word fixed beside it, or an open place.",
        "properties": {"head": TEXT, "word": TEXT, "slot": {"type": "array", "minItems": 1, "items": TEXT},
                       "case": {"type": "array", "minItems": 1, "items": TEXT}, "form": TEXT, "mood": TEXT, "label": TEXT},
        "oneOf": [{"required": ["head"]}, {"required": ["word"]}, {"required": ["slot"]}],
    }
    form = defs["form"]
    del form["properties"]["senses"]  # ingest names the senses of a form by id
    form["properties"]["sense_ids"] = {"type": "array", "items": UUID}
    return schema


# what ingest keeps for the pipeline's own use: who wrote the entry and what ingest did to it
INTERNAL = ("source", "dropped", "retired_lexemes")


def public():
    """The schema the backend serves for download beside the Lexicon API page: an entry's content, and nothing of how it was made.

    The published schema without the keys that are the pipeline's own (which writer, model and prompt; what ingest
    dropped or retired; its counts), so the file says what a reader of entries needs and no more.
    """
    schema = published()
    schema["title"] = "Almonium Lexicon entry, schema version 4"
    schema.pop("description", None)
    for key in INTERNAL:
        schema["properties"].pop(key, None)
    schema["required"] = [key for key in schema["required"] if key not in INTERNAL]
    schema["additionalProperties"] = False
    schema["$defs"]["lexeme"]["properties"].pop("retired", None)
    return schema


def text():
    return json.dumps(published(), ensure_ascii=False, indent=1) + "\n"


def public_text():
    return json.dumps(public(), ensure_ascii=False, indent=1) + "\n"


if __name__ == "__main__":
    for path, written in ((PUBLISHED, text()), (PUBLIC, public_text())):
        path.write_text(written, encoding="utf-8")
        print(f"wrote {path}")
