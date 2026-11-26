"""What the backend is sent: the writers' schema with what ingest adds. Run from the repository root:
    scripts/.venv/bin/python -m pytest scripts/lexicon_research/tests -q
"""

import json
import sys
from pathlib import Path

import jsonschema

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
import ingest_v4  # noqa: E402
import prompting  # noqa: E402
import published_schema  # noqa: E402

VALID = jsonschema.Draft202012Validator(published_schema.published())
META = {"writer": "cc-claude-opus-5-5-v4", "model": "claude-opus-5-5", "effort": "high", "prompt_version": prompting.PROMPT_VERSION}


def test_the_backends_copy_is_the_one_derived_from_the_writers_schema():
    # stale after a change to entry.schema.v4.json: run scripts/lexicon_research/published_schema.py
    assert published_schema.PUBLISHED.read_text(encoding="utf-8") == published_schema.text()


def test_what_ingest_makes_of_a_written_entry_is_valid():
    for version in ("v4", "v4.2", "v4.7", "v4.8", "v4.9"):
        raw = json.loads(prompting.schema_example_v4(version))
        clean = ingest_v4.clean_entry("en", raw, META, aliases=("leads",))
        VALID.validate(clean)
        assert all(sense["id"] for lexeme in clean["lexemes"] for sense in lexeme["senses"])
    for lang in ("de", "fr", "es", "it", "pt", "ru"):
        for raw in json.loads(prompting.gold_path(lang, prompting.PROMPT_VERSION).read_text(encoding="utf-8")):
            VALID.validate(ingest_v4.clean_entry(lang, raw, META))


def test_a_rewrite_with_a_retired_sense_and_a_reject_are_valid():
    raw = json.loads(prompting.schema_example_v4(prompting.PROMPT_VERSION))
    first = ingest_v4.clean_entry("en", raw, META)
    lexeme = next(lx for lx in raw["lexemes"] if len(lx["senses"]) > 1)
    lexeme["senses"].pop()
    again = ingest_v4.clean_entry("en", raw, META, previous=first)
    assert any(lx.get("retired") for lx in again["lexemes"])
    VALID.validate(again)
    VALID.validate(ingest_v4.clean_entry("en", {"language": "en", "headword": "leads", "lexemes": [], "reject": "inflected form of lead"}, META))


def test_an_entry_as_a_writer_returned_it_is_not_what_is_published():
    raw = json.loads(prompting.schema_example_v4(prompting.PROMPT_VERSION))
    assert not VALID.is_valid(raw)  # no ids: the backend keys a sense by the id ingest gives it


def test_no_sense_is_published_with_the_evidence_pipelines_from():
    assert "from" not in published_schema.published()["$defs"]["sense"]["properties"]


def test_the_schema_offered_for_download_says_nothing_of_how_an_entry_was_made():
    public = published_schema.public()
    assert not {"source", "dropped", "retired_lexemes"} & set(public["properties"])
    assert "source" not in public["required"] and public["additionalProperties"] is False
    text = published_schema.public_text()
    # stale after a change to the schema: run scripts/lexicon_research/published_schema.py
    assert published_schema.PUBLIC.read_text(encoding="utf-8") == text
    for word in ("Wiktionary", "evidence", "writer", "prompt", "ingest", "scripts/"):
        assert word not in text, word
    # an entry as the pipeline keeps it still fits its own schema, and its content alone fits the public one
    entry = {"language": "es", "headword": "x", "schema": "4", "lexemes": []}
    jsonschema.Draft202012Validator(public).validate(entry)
