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

This machine is not the only one that writes. A cloud session writes entries in its own container,
and another machine or account may run the writers too. So every run first takes in what is on the
data repository: its own branch as it stands there, and every branch claude/* a cloud session
pushed. The entries land in out/, where the writers find them and do not buy the words again. A
word written on both sides keeps this machine's entry; a merge that fails is aborted and reported,
and what is here is pushed all the same. A push refused because someone pushed in between is tried
once more after taking that in.

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

- `<writer>-v4/<lang>/<headword>.json`: entries in schema 4 (prompt v4 and later; metas written before 18 November 2026 call it schema 3); `<writer>` names
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


def take_in(ref):
    """Merge what the data repository holds under ref into what is here; True when something came in."""
    if git("fetch", "-q", "origin", ref, check=False).returncode != 0:
        print(f"could not fetch {ref}")
        return False
    if git("merge-base", "--is-ancestor", "FETCH_HEAD", "HEAD", check=False).returncode == 0:
        return False  # already taken in
    r = git("merge", "--no-edit", "-X", "ours", "-m", f"Entries written elsewhere ({ref})", "FETCH_HEAD", check=False)
    if r.returncode != 0:
        git("merge", "--abort", check=False)  # leave the work tree as it was: the entries here are never at risk
        print(f"could not merge {ref}: {(r.stderr or r.stdout).strip()[:200]}")
        return False
    print(f"merged {ref}")
    return True


def merge_cloud():
    """Take in every branch claude/* of the data repository; says how many brought something."""
    heads = git("ls-remote", "--heads", "origin", "claude/*", check=False).stdout.splitlines()
    return sum(take_in(line.partition("\t")[2].removeprefix("refs/heads/")) for line in heads if "\t" in line)


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--no-push", action="store_true")
    a = ap.parse_args()
    if not DATA.is_dir():
        sys.exit(f"no data repository at {DATA}: git clone git@github.com:almonium-platform/almonium-lexicon-data.git next to almonium-be")
    (OUT / ".gitignore").write_text("/*\n!/.gitignore\n!/README.md\n" + "".join(f"!/{name}/\n" for name in entry_dirs()))
    (OUT / "README.md").write_text(README)
    git("add", "-A")
    committed = bool(git("status", "--porcelain").stdout.strip())
    if committed:
        counted = git("diff", "--cached", "--shortstat").stdout.strip()
        git("commit", "-q", "-m", f"Entries as of {datetime.now():%Y-%m-%d %H:%M} ({counted})")
        print(git("log", "--oneline", "-1").stdout.strip())
    branch = git("branch", "--show-current").stdout.strip() or "main"
    came = take_in(branch) + merge_cloud()  # after the commit: a merge needs a clean tree, and what is here goes first
    if not came and not committed:
        print("nothing new")
        return
    if a.no_push:
        return
    for attempt in (1, 2):
        r = git("push", "-q", "origin", f"HEAD:{branch}", check=False)
        if r.returncode == 0:
            print("pushed")
            return
        if attempt == 1:
            take_in(branch)  # someone pushed between the fetch and the push
    sys.exit(f"could not push: {(r.stderr or r.stdout).strip()[:300]}")


if __name__ == "__main__":
    main()
