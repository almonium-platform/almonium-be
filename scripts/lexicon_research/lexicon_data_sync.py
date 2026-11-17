"""Commit the written entries to the almonium-lexicon-data repository and push them.

The entries live only under out/, which this repository ignores, so a lost disk would lose every
paid entry, and a cloud session cannot read them. The data repository tracks out/ itself: its git
directory is ../almonium-lexicon-data/.git and its work tree is out/, so nothing is copied (the disk
is nearly full and the entries are some 160,000 small files). out/.gitignore, rewritten on every
run, lets in only what holds entries: every writer directory (v4 and older), the mender's
directories and carry/ (the v3 glosses and cues kept for the gloss pass). Logs, caches, failed
answers, the status page's file, audio and the derived lexicon stay out.

A host timer runs it every three hours (almonium-lexicon-data-sync.timer); a run with nothing new
commits nothing.

Usage: lexicon_data_sync.py [--no-push]   (run from the backend repository root)
"""
import argparse, subprocess, sys
from datetime import datetime
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
import v3_entries  # noqa: E402

OUT = Path("scripts/lexicon_research/out").resolve()
DATA = (Path(__file__).resolve().parents[3] / "almonium-lexicon-data" / ".git")
README = """# Almonium lexicon data

The dictionary entries Almonium's writers have written, one JSON file per headword, and beside
each its `.meta.json` (model, effort, prompt version and hash, schema version, tokens, cost).
The code that writes and checks them is `scripts/lexicon_research/` in `almonium-be`.

- `<writer>-v4/<lang>/<headword>.json`: entries in schema 3 (prompt v4 and later); `<writer>` names
  the runner and model (`cc-` the Claude Code CLI, `codex-` the Codex CLI).
- `<writer>/<lang>/<headword>.json` without `-v4`: older entries in schema 2 (prompt v3 and before);
  `cc-claude-opus-5-5` is the main one, the others are bake-off writers.
- `mend-*`: v3 entries mended into the v4 shape (`mend_v4.py`); `mend-eval-*` are its pilots.
- `carry/<lang>/<headword>.json`: the v3 glosses and review cues of a mended word, kept for the
  gloss pass.

Entries are written for en, de, fr, es, it and pt; Ukrainian is only the bridge language of the
glosses (Ukrainian entries here are from before that rule). Committed every three hours from the
owner's machine by `lexicon_data_sync.py`; never edited by hand.
"""


def entry_dirs():
    names = set(v3_entries.writers())
    names |= {d.name for d in OUT.iterdir() if d.is_dir() and (d.name.endswith("-v4") or d.name.startswith("mend-") or d.name == "carry")}
    return sorted(names)


def git(*args, check=True):
    return subprocess.run(["git", f"--git-dir={DATA}", f"--work-tree={OUT}", *args], check=check, capture_output=True, text=True)


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--no-push", action="store_true")
    a = ap.parse_args()
    if not DATA.is_dir():
        sys.exit(f"no data repository at {DATA}: git clone git@github.com:almonium-platform/almonium-lexicon-data.git next to almonium-be")
    (OUT / ".gitignore").write_text("/*\n!/.gitignore\n!/README.md\n" + "".join(f"!/{name}/\n" for name in entry_dirs()))
    (OUT / "README.md").write_text(README)
    git("add", "-A")
    if not git("status", "--porcelain").stdout.strip():
        print("nothing new")
        return
    counted = git("diff", "--cached", "--shortstat").stdout.strip()
    git("commit", "-q", "-m", f"Entries as of {datetime.now():%Y-%m-%d %H:%M} ({counted})")
    print(git("log", "--oneline", "-1").stdout.strip())
    if not a.no_push:
        branch = git("branch", "--show-current").stdout.strip() or "main"
        git("push", "-q", "origin", f"HEAD:{branch}")
        print("pushed")


if __name__ == "__main__":
    main()
