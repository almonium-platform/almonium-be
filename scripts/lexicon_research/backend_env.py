"""Secrets the lexicon scripts share with the backend, read under the backend's own names.

The publisher secret signs requests to /internal/lexicon/entries and the clip endpoints. It is the vault's
almonium_shared.books.publisher_secret, which infra hands the backend and the book processor alike as
ALMONIUM_BOOKS_PUBLISHER_SECRET. Exported, it signs for another environment; otherwise it comes from the backend's
.env. No value is ever printed.
"""
import os
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]
PUBLISHER_SECRET = "ALMONIUM_BOOKS_PUBLISHER_SECRET"


def dotenv_value(key):
    """The value of KEY= in the backend's .env, unquoted, or None."""
    env = ROOT / ".env"
    if not env.exists():
        return None
    for line in env.read_text().splitlines():
        if line.startswith(key + "="):
            return line.split("=", 1)[1].strip().strip("\"'") or None
    return None


def publisher_secret():
    secret = os.getenv(PUBLISHER_SECRET) or dotenv_value(PUBLISHER_SECRET)
    if not secret:
        sys.exit(f"{PUBLISHER_SECRET} is neither exported nor in the backend's .env")
    return secret
