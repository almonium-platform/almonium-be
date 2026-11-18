"""Publish the dictionary to the backend's lexicon, the way the book processor publishes editions.

What is published is the clean entry ingest_v4.py derives (out/lexicon-s4/<lang>/<headword>.json), not the entry a
writer returned: it carries the ids of its lexemes and senses, which the backend keys a sense by, its frames read
into parts, and the spellings it answers for. Run ingest first; it is free. Each entry goes with the meta record of
the writer that wrote it (out/<writer>/<lang>/<headword>.meta.json: model, prompt, tokens, cost) and with the
headword's Zipf frequency from wordfreq, which the backend keeps on its lexemes to rank suggestions; wordfreq ships
its tables, so this costs no network call (pip install -r requirements.txt). Entries are PUT in signed batches to
/internal/lexicon/entries and the backend answers one result per entry; it refuses an entry that does not pass the
published schema (published_schema.py) and names the fault. The signature is HMAC-SHA256 of "{timestamp}." and the
body under ALMONIUM_BOOKS_PUBLISHER_SECRET, exported or from the backend's .env (see backend_env.py); it is never
printed.
Usage: publish_entries.py [--langs en,de] [--backend http://localhost:9998/api/v1] [--words lead,program]
                          [--limit N] [--workers 4] [--batch 50]
(run from the backend repository root)
"""
import argparse, hashlib, hmac, json, sys, threading, time
from concurrent.futures import ThreadPoolExecutor
from datetime import datetime, timezone
from pathlib import Path

import requests
from wordfreq import zipf_frequency

from backend_env import publisher_secret

HERE = Path(__file__).resolve().parent
OUT = HERE / "out"
CLEAN = OUT / "lexicon-s4"
PATH = "/internal/lexicon/entries"
TIMEOUT = 300


def publications(langs, limit, words=()):
    """Yield (path, body) for every clean entry of the languages, with the meta of the entry it was derived from."""
    count = 0
    for lang in langs:
        folder = CLEAN / lang
        if not folder.is_dir():
            print(f"no entries under {folder}: run ingest_v4.py first", file=sys.stderr)
            continue
        for path in sorted(folder.glob("*.json")):
            if path.name.startswith("_") or (words and path.stem not in words):  # _aliases.json, _report.json
                continue
            entry = json.loads(path.read_text(encoding="utf-8"))
            writer = (entry.get("source") or {}).get("writer")
            if not writer:
                print(f"held {path}: the entry does not say which writer wrote it", file=sys.stderr)
                continue
            written = OUT / writer / lang / path.name
            meta_path = written.with_name(path.stem + ".meta.json")
            meta = json.loads(meta_path.read_text(encoding="utf-8")) if meta_path.exists() else {}
            built = written if written.exists() else path
            built_at = datetime.fromtimestamp(built.stat().st_mtime, tz=timezone.utc).isoformat()
            zipf = round(zipf_frequency(entry["headword"], lang), 2)
            yield path, {"writer": writer, "entry": entry, "meta": meta, "built_at": built_at, "zipf": zipf}
            count += 1
            if limit and count >= limit:
                return


def batched(items, size):
    batch = []
    for item in items:
        batch.append(item)
        if len(batch) == size:
            yield batch
            batch = []
    if batch:
        yield batch


def send(backend, token, batch):
    body = json.dumps([publication for _, publication in batch], ensure_ascii=False, separators=(",", ":")).encode()
    timestamp = str(int(time.time()))
    signature = hmac.new(token.encode(), f"{timestamp}.".encode() + body, hashlib.sha256).hexdigest()
    response = requests.put(
        backend.rstrip("/") + PATH,
        data=body,
        headers={
            "Content-Type": "application/json",
            "X-Almonium-Books-Timestamp": timestamp,
            "X-Almonium-Books-Signature": signature,
        },
        timeout=TIMEOUT,
    )
    if response.status_code != 200:
        raise RuntimeError(f"HTTP {response.status_code}: {response.text[:300]}")
    return response.json()


def main():
    parser = argparse.ArgumentParser(description=__doc__.split("\n")[0])
    parser.add_argument("--langs", default="en,de,fr,es,it,pt", help="comma-separated language folders under out/lexicon-s4")
    parser.add_argument("--words", default="", help="comma-separated headwords, to publish these only")
    parser.add_argument("--backend", default="http://localhost:9998/api/v1")
    parser.add_argument("--limit", type=int, default=0, help="stop after this many entries (0 = all)")
    parser.add_argument("--workers", type=int, default=4)
    parser.add_argument("--batch", type=int, default=50)
    args = parser.parse_args()

    token = publisher_secret()

    langs = [lang.strip() for lang in args.langs.split(",") if lang.strip()]
    words = {word.strip() for word in args.words.split(",") if word.strip()}
    accepted, rejected, failed_batches = 0, 0, 0
    rejections, reasons = [], {}
    lock = threading.Lock()
    started = time.time()

    def work(batch):
        nonlocal accepted, rejected, failed_batches
        try:
            results = send(args.backend, token, batch)
        except Exception as exception:
            with lock:
                failed_batches += 1
                rejected += len(batch)
                if len(rejections) < 10:
                    rejections.append((f"batch of {len(batch)} from {batch[0][0].name}", str(exception)))
            return
        with lock:
            for (path, _), result in zip(batch, results):
                if result.get("accepted"):
                    accepted += 1
                else:
                    rejected += 1
                    reason = result.get("message", "")
                    reasons[reason[:60]] = reasons.get(reason[:60], 0) + 1
                    if len(rejections) < 10:
                        rejections.append((str(path.relative_to(OUT)), reason))
            done = accepted + rejected
            if done % 500 < len(batch):
                print(f"{done} entries in {time.time() - started:.0f}s: {accepted} accepted, {rejected} rejected", flush=True)

    with ThreadPoolExecutor(max_workers=args.workers) as pool:
        for batch in batched(publications(langs, args.limit, words), args.batch):
            pool.submit(work, batch)

    elapsed = time.time() - started
    print(f"\n{accepted} accepted, {rejected} rejected ({failed_batches} whole batches failed) in {elapsed:.0f}s")
    for where, message in rejections:
        print(f"  {where}: {message}")
    if len(reasons) > 1 or rejected > len(rejections):
        print("refused, by how the reason begins:")
        for reason, count in sorted(reasons.items(), key=lambda item: -item[1])[:15]:
            print(f"  {count:6}  {reason}")
    sys.exit(1 if rejected else 0)


if __name__ == "__main__":
    main()
