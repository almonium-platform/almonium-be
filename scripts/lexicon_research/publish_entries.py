"""Publish written entries to the backend's lexicon, the way the book processor publishes editions.

Walks out/<writer>/<lang>/*.json, pairs each entry with its <headword>.meta.json, skips files holding an
"error" key, and PUTs them in signed batches to /internal/lexicon/entries; the backend answers one result
per entry. The signature is HMAC-SHA256 of "{timestamp}." and the body under the publisher token, read from
ALMONIUM_BOOKS_PUBLISHER_TOKEN as the processor reads it. The token is never printed.
Usage: publish_entries.py [--writer cc-claude-opus-5-5] [--langs en,de] [--backend http://localhost:9998/api/v1]
                          [--limit N] [--workers 4] [--batch 50]
(run from the backend repository root)
"""
import argparse, hashlib, hmac, json, os, sys, threading, time
from concurrent.futures import ThreadPoolExecutor
from datetime import datetime, timezone
from pathlib import Path

import requests

HERE = Path(__file__).resolve().parent
OUT = HERE / "out"
PATH = "/internal/lexicon/entries"
TIMEOUT = 300


def publications(writer, langs, limit):
    """Yield (path, body) for every entry file of the writer, newest meta beside it, skipping failed writes."""
    count = 0
    for lang in langs:
        folder = OUT / writer / lang
        if not folder.is_dir():
            print(f"no entries under {folder}", file=sys.stderr)
            continue
        for path in sorted(folder.glob("*.json")):
            if path.name.endswith(".meta.json"):
                continue
            entry = json.loads(path.read_text())
            if "error" in entry:
                continue
            meta_path = path.with_name(path.stem + ".meta.json")
            meta = json.loads(meta_path.read_text()) if meta_path.exists() else {}
            built_at = datetime.fromtimestamp(path.stat().st_mtime, tz=timezone.utc).isoformat()
            yield path, {"writer": writer, "entry": entry, "meta": meta, "built_at": built_at}
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
    parser.add_argument("--writer", default="cc-claude-opus-5-5")
    parser.add_argument("--langs", default="en,de", help="comma-separated language folders under out/<writer>")
    parser.add_argument("--backend", default="http://localhost:9998/api/v1")
    parser.add_argument("--limit", type=int, default=0, help="stop after this many entries (0 = all)")
    parser.add_argument("--workers", type=int, default=4)
    parser.add_argument("--batch", type=int, default=50)
    args = parser.parse_args()

    token = os.getenv("ALMONIUM_BOOKS_PUBLISHER_TOKEN", "")
    if not token:
        sys.exit("ALMONIUM_BOOKS_PUBLISHER_TOKEN is not set")

    langs = [lang.strip() for lang in args.langs.split(",") if lang.strip()]
    accepted, rejected, failed_batches = 0, 0, 0
    rejections = []
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
                    if len(rejections) < 10:
                        rejections.append((str(path.relative_to(OUT)), result.get("message", "")))
            done = accepted + rejected
            if done % 500 < len(batch):
                print(f"{done} entries in {time.time() - started:.0f}s: {accepted} accepted, {rejected} rejected", flush=True)

    with ThreadPoolExecutor(max_workers=args.workers) as pool:
        for batch in batched(publications(args.writer, langs, args.limit), args.batch):
            pool.submit(work, batch)

    elapsed = time.time() - started
    print(f"\n{accepted} accepted, {rejected} rejected ({failed_batches} whole batches failed) in {elapsed:.0f}s")
    for where, message in rejections:
        print(f"  {where}: {message}")
    sys.exit(1 if rejected else 0)


if __name__ == "__main__":
    main()
