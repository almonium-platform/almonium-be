"""Secrets the lexicon scripts share with the backend, read from the backend repository's .env.

The publisher token signs requests to /internal/lexicon/entries and the clip endpoints. The book processor calls
it ALMONIUM_BOOKS_PUBLISHER_TOKEN; the backend verifies against the same value as ALMONIUM_BOOKS_PUBLISHER_SECRET.
An exported ALMONIUM_BOOKS_PUBLISHER_TOKEN wins, so a run can sign for another environment; otherwise the backend's
own secret is used. No value is ever printed.
"""
import os
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]


def dotenv_value(key):
    """The value of KEY= in the backend's .env, unquoted, or None."""
    env = ROOT / ".env"
    if not env.exists():
        return None
    for line in env.read_text().splitlines():
        if line.startswith(key + "="):
            return line.split("=", 1)[1].strip().strip("\"'") or None
    return None


def publisher_token():
    token = os.getenv("ALMONIUM_BOOKS_PUBLISHER_TOKEN") or dotenv_value("ALMONIUM_BOOKS_PUBLISHER_SECRET")
    if not token:
        sys.exit("Set ALMONIUM_BOOKS_PUBLISHER_TOKEN, or ALMONIUM_BOOKS_PUBLISHER_SECRET in the backend's .env")
    return token
