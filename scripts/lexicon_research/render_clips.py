"""Render the lexicon's pronunciation clips: the audio phase of the import.

For each language, pages through the backend's pronunciations that have no clip yet, synthesises
each with ElevenLabs the way scripts/tts_research/elevenlabs_loop.py proved (eleven_v4, one native
voice per language, the per-language input recipe from tts_recipe.py), transcribes the clip with
faster-whisper, compares, regenerates once on a mismatch, and PUTs the clip to the backend as
"verified" or, when neither attempt matched, as "unverified" with the transcript.

Clips and their meta are cached under out/audio/ by the same deterministic key the backend
stores them under, so a crash never spends credits twice: re-running reuses what is on disk.

Usage (from the backend repository root):
    scripts/.venv/bin/python scripts/lexicon_research/render_clips.py --langs fr,es
        [--backend http://localhost:9998/api/v1] [--limit N] [--credit-budget N] [--credit-floor N]
        [--workers 3] [--dry-run]

--limit caps the pronunciations taken per language (0 = all). --credit-budget caps the credits this
run may spend (0 = no cap); --credit-floor stops before synthesis when the plan has fewer credits
left. --dry-run lists what would be rendered and the estimated credits, without touching ElevenLabs.

The ElevenLabs key is ELEVENLABS_API_KEY in .env and the backend token is ALMONIUM_BOOKS_PUBLISHER_TOKEN
in the environment, read as the existing scripts read them; neither is ever printed.
"""

import argparse
import base64
import hashlib
import hmac
import json
import os
import sys
import threading
import time
from concurrent.futures import ThreadPoolExecutor
from pathlib import Path

import requests

sys.path.insert(0, str(Path(__file__).resolve().parent))
from tts_recipe import (  # noqa: E402
    ENGINE,
    MODEL,
    OUTPUT_FORMAT,
    PACE,
    VARIETY,
    UnsupportedLanguage,
    engine_input,
    matched,
    object_key,
    verdict,
    voice_for,
)

ROOT = Path(__file__).resolve().parents[2]
HERE = Path(__file__).resolve().parent
OUT = HERE / "out"
PAGE = 500
ESTIMATE_CREDITS_PER_CHARACTER = 0.4  # eleven_v4, when no measured character-cost is cached
ELEVENLABS = "https://api.elevenlabs.io"
BACKEND_TIMEOUT = 300


def api_key():
    env = ROOT / ".env"
    if env.exists():
        for line in env.read_text().splitlines():
            if line.startswith("ELEVENLABS_API_KEY="):
                return line.split("=", 1)[1].strip()
    sys.exit("ELEVENLABS_API_KEY missing from .env")


def publisher_token():
    token = os.getenv("ALMONIUM_BOOKS_PUBLISHER_TOKEN", "")
    if not token:
        sys.exit("ALMONIUM_BOOKS_PUBLISHER_TOKEN is not set")
    return token


# ---------------------------------------------------------------- backend


class Backend:
    """Signed calls to the backend's internal lexicon endpoints."""

    def __init__(self, base, token):
        self.base = base.rstrip("/")
        self.token = token

    def _headers(self, body):
        timestamp = str(int(time.time()))
        signature = hmac.new(self.token.encode(), f"{timestamp}.".encode() + body, hashlib.sha256).hexdigest()
        return {
            "Content-Type": "application/json",
            "X-Almonium-Books-Timestamp": timestamp,
            "X-Almonium-Books-Signature": signature,
        }

    def pending(self, lang, limit=0):
        """Every pronunciation of the language without a clip, in the backend's order, up to limit."""
        out, offset = [], 0
        while True:
            page = PAGE if not limit else min(PAGE, limit - len(out))
            if page <= 0:
                break
            response = requests.get(
                f"{self.base}/internal/lexicon/pronunciations",
                params={"language": lang, "without": "clip", "limit": page, "offset": offset},
                headers=self._headers(b""),
                timeout=BACKEND_TIMEOUT,
            )
            if response.status_code != 200:
                raise RuntimeError(f"GET pronunciations {lang}: HTTP {response.status_code}: {response.text[:300]}")
            rows = response.json()
            out.extend(rows)
            offset += len(rows)
            if len(rows) < page:
                break
        return out

    def put_clip(self, pronunciation_id, clip):
        body = json.dumps(clip, ensure_ascii=False, separators=(",", ":")).encode()
        response = requests.put(
            f"{self.base}/internal/lexicon/pronunciations/{pronunciation_id}/clips",
            data=body,
            headers=self._headers(body),
            timeout=BACKEND_TIMEOUT,
        )
        if response.status_code != 200:
            raise RuntimeError(f"PUT clip {pronunciation_id}: HTTP {response.status_code}: {response.text[:300]}")
        return response.json()


# ---------------------------------------------------------------- synthesis


class ElevenLabs:
    """Synthesis with a shared credit meter; a clip already on disk (with its meta) is reused, never re-bought."""

    def __init__(self, key, budget=0):
        self.headers = {"xi-api-key": key}
        self.budget = budget
        self.lock = threading.Lock()
        self.spent = 0.0
        self.requests = 0

    def credits_remaining(self):
        r = requests.get(f"{ELEVENLABS}/v1/user/subscription", headers=self.headers, timeout=30)
        r.raise_for_status()
        d = r.json()
        return d["character_limit"] - d["character_count"], d["character_limit"]

    def synthesise(self, lang, voice_id, text, path):
        """POST one word; returns the clip record and whether it came from disk."""
        meta = path.with_suffix(".json")
        if path.exists() and meta.exists():
            return json.loads(meta.read_text()), True
        path.parent.mkdir(parents=True, exist_ok=True)
        url = f"{ELEVENLABS}/v1/text-to-speech/{voice_id}?output_format={OUTPUT_FORMAT}"
        body = {"text": text, "model_id": MODEL, "language_code": lang}
        record = dict(bytes=0, character_cost=None, seconds=None, error=None)
        for tries in range(8):
            with self.lock:
                if self.budget and self.spent >= self.budget:
                    record["error"] = "credit budget reached"
                    return record, False
            t0 = time.time()
            try:
                r = requests.post(url, headers=self.headers, json=body, timeout=120)
            except requests.RequestException as e:
                record["error"] = f"request failed: {e}"
                time.sleep(5)
                continue
            if r.status_code == 429:
                wait = float(r.headers.get("retry-after") or 5 * (tries + 1))
                print(f"  429 on {text}, waiting {wait:.0f}s", flush=True)
                time.sleep(wait)
                continue
            if not r.ok:
                record["error"] = f"HTTP {r.status_code}: {r.text[:200]}"
                if r.status_code >= 500:
                    time.sleep(5)
                    continue
                break
            cost = r.headers.get("character-cost")
            path.write_bytes(r.content)
            record.update(
                bytes=len(r.content),
                character_cost=float(cost) if cost else None,
                seconds=round(time.time() - t0, 3),
                request_id=r.headers.get("request-id"),
                error=None,
            )
            with self.lock:
                self.spent += float(cost) if cost else len(text) * ESTIMATE_CREDITS_PER_CHARACTER
                self.requests += 1
            meta.write_text(json.dumps(record, ensure_ascii=False))
            break
        return record, False


# ---------------------------------------------------------------- transcription


class Transcriber:
    """faster-whisper small, CPU, int8, one instance, used sequentially."""

    def __init__(self):
        from faster_whisper import WhisperModel

        self.model = WhisperModel("small", device="cpu", compute_type="int8")

    def transcribe(self, path, lang):
        """(text heard, duration in ms)"""
        segments, info = self.model.transcribe(
            str(path), language=lang, beam_size=5, condition_on_previous_text=False, without_timestamps=True
        )
        text = " ".join(s.text for s in segments).strip()
        return text, int(round(info.duration * 1000))


# ---------------------------------------------------------------- the run


def plan(lang, rows):
    """Turn the backend's rows into jobs; rows whose recipe cannot be built become skips."""
    variety = VARIETY[lang]
    voice_id = voice_for(lang)[2]
    jobs, skipped = [], []
    for row in rows:
        text = row.get("form") if row.get("formId") else row.get("headword")
        built = engine_input(lang, text, row.get("phonemic"))
        if not built:
            skipped.append(row)
            continue
        sent, mode = built
        jobs.append(dict(row=row, lang=lang, variety=variety, voice_id=voice_id, text=text, input=sent, mode=mode, clips=[]))
    return jobs, skipped


def estimate(job):
    """Credits one attempt would cost: the measured cost when the clip is cached, else the eleven_v4 rate."""
    meta = (OUT / object_key(job["variety"], job["voice_id"], job["input"])).with_suffix(".json")
    if meta.exists():
        cached = json.loads(meta.read_text()).get("character_cost")
        if cached is not None:
            return cached
    return len(job["input"]) * ESTIMATE_CREDITS_PER_CHARACTER


def synthesise_all(eleven, jobs, attempt, workers):
    def one(job):
        key = object_key(job["variety"], job["voice_id"], job["input"], attempt)
        record, reused = eleven.synthesise(job["lang"], job["voice_id"], job["input"], OUT / key)
        return job, dict(record, attempt=attempt, object_key=key, reused=reused)

    with ThreadPoolExecutor(max_workers=workers) as pool:
        futures = [pool.submit(one, job) for job in jobs]
        for n, future in enumerate(futures, 1):
            job, clip = future.result()
            job["clips"].append(clip)
            if n % 25 == 0:
                print(f"  {n}/{len(jobs)} clips, {eleven.spent:.0f} credits so far", flush=True)


def transcribe_latest(transcriber, jobs):
    for job in jobs:
        clip = job["clips"][-1]
        if clip.get("error"):
            clip["verdict"] = "error"
            continue
        clip["transcript"], clip["duration_ms"] = transcriber.transcribe(OUT / clip["object_key"], job["lang"])
        clip["verdict"] = verdict(job["text"], clip["transcript"])


def choose(job):
    """The clip to publish and its status, or None when every attempt failed to synthesise."""
    good = [c for c in job["clips"] if c["verdict"] not in ("error", "mismatch")]
    if good:
        return good[0], "verified"
    first = job["clips"][0]
    if first["verdict"] == "error":
        return None, None
    return first, "unverified"


def clip_body(job, clip, status):
    return {
        "engine": ENGINE,
        "voice": job["voice_id"],
        "model": MODEL,
        "pace": PACE,
        "format": OUTPUT_FORMAT,
        "engine_input": job["input"],
        "status": status,
        "duration_ms": clip["duration_ms"],
        "transcript": None if status == "verified" else clip["transcript"],
        "character_cost": clip.get("character_cost"),
        "audio_base64": base64.b64encode((OUT / clip["object_key"]).read_bytes()).decode(),
    }


def new_totals():
    return dict(pending=0, rendered=0, verified=0, unverified=0, skipped=0, errors=0, credits=0.0, reused=0, not_stored=0)


def run(langs, backend, eleven, transcriber, limit=0, workers=3, dry_run=False):
    """Render every language; returns {lang: totals}. transcriber may be a factory, called once on first need."""
    totals = {}
    model = None
    for lang in langs:
        voice_for(lang)  # refuse before touching the backend
        rows = backend.pending(lang, limit)
        jobs, skipped = plan(lang, rows)
        t = totals[lang] = new_totals()
        t["pending"], t["skipped"] = len(rows), len(skipped)
        for row in skipped:
            print(f"  skip {lang} {row.get('headword')!r} (pronunciation {row.get('id')}): recipe cannot be built", flush=True)
        if dry_run:
            credits = sum(estimate(j) for j in jobs)
            print(f"{lang}: {len(jobs)} to render, {len(skipped)} skipped, about {credits:.0f} credits for first attempts")
            for j in jobs:
                print(f"  {lang} {j['text']:24} {j['mode']:12} {j['input']}")
            t["credits"] = credits
            continue
        if not jobs:
            continue

        print(f"{lang}: synthesising {len(jobs)} words, {workers} in parallel", flush=True)
        before = eleven.spent
        synthesise_all(eleven, jobs, 1, workers)
        if model is None:
            model = transcriber() if callable(transcriber) else transcriber
        transcribe_latest(model, jobs)
        retry = [j for j in jobs if j["clips"][0]["verdict"] == "mismatch"]
        if retry:
            print(f"{lang}: regenerating {len(retry)} mismatches", flush=True)
            synthesise_all(eleven, retry, 2, workers)
            transcribe_latest(model, retry)
        t["credits"] = eleven.spent - before
        t["reused"] = sum(1 for j in jobs for c in j["clips"] if c.get("reused"))

        for job in jobs:
            clip, status = choose(job)
            if clip is None:
                t["errors"] += 1
                print(f"  {lang} {job['text']!r}: {job['clips'][0].get('error')}", flush=True)
                continue
            try:
                answer = backend.put_clip(job["row"]["id"], clip_body(job, clip, status))
            except Exception as e:
                t["errors"] += 1
                print(f"  {lang} {job['text']!r}: {e}", flush=True)
                continue
            t["rendered"] += 1
            t[status] += 1
            if not answer.get("stored", True):
                t["not_stored"] += 1
    return totals


def print_report(totals, dry_run=False):
    print()
    print("| Language | Pending | Rendered | Verified | Unverified | Skipped | Errors | Credits | Reused from disk | stored: false |")
    print("|---|---|---|---|---|---|---|---|---|---|")
    for lang, t in totals.items():
        print(
            f"| {lang} | {t['pending']} | {t['rendered']} | {t['verified']} | {t['unverified']} | {t['skipped']} | {t['errors']} "
            f"| {t['credits']:.0f}{' (estimate)' if dry_run else ''} | {t['reused']} | {t['not_stored']} |"
        )


def main():
    parser = argparse.ArgumentParser(description=__doc__.split("\n")[0])
    parser.add_argument("--langs", required=True, help="comma-separated languages, e.g. fr,es")
    parser.add_argument("--backend", default="http://localhost:9998/api/v1")
    parser.add_argument("--limit", type=int, default=0, help="pronunciations per language (0 = all)")
    parser.add_argument("--credit-budget", type=float, default=0, help="credits this run may spend (0 = no cap)")
    parser.add_argument("--credit-floor", type=float, default=5000, help="stop when the plan has fewer credits left")
    parser.add_argument("--workers", type=int, default=3)
    parser.add_argument("--dry-run", action="store_true", help="list what would be rendered; no synthesis")
    args = parser.parse_args()

    langs = [lang.strip() for lang in args.langs.split(",") if lang.strip()]
    try:
        for lang in langs:
            voice_for(lang)
    except UnsupportedLanguage as e:
        sys.exit(str(e))

    backend = Backend(args.backend, publisher_token())
    eleven = None
    if not args.dry_run:
        eleven = ElevenLabs(api_key(), args.credit_budget)
        remaining, limit = eleven.credits_remaining()
        print(f"credits remaining: {remaining} of {limit}", flush=True)
        if remaining < args.credit_floor:
            sys.exit(f"fewer than {args.credit_floor:.0f} credits remain; stopping")

    def load_whisper():
        print("loading faster-whisper small (cpu, int8)", flush=True)
        return Transcriber()

    try:
        totals = run(langs, backend, eleven, load_whisper, args.limit, args.workers, args.dry_run)
    except (RuntimeError, requests.RequestException) as e:
        sys.exit(f"backend: {e}")
    print_report(totals, args.dry_run)
    if eleven is not None:
        remaining, limit = eleven.credits_remaining()
        print(f"\n{eleven.spent:.0f} credits across {eleven.requests} requests; {remaining} of {limit} remain on the plan")
    sys.exit(1 if any(t["errors"] for t in totals.values()) else 0)


if __name__ == "__main__":
    main()
