"""Render the lexicon's pronunciation clips: the audio phase of the import.

For each language, pages through the backend's pronunciations that have no clip yet, keeps only
pronunciations belonging to ingested Schema 4 entries (out/lexicon-s4/<lang>/) whose headword passes
the headword generation gate (lemma_gate.unfit), orders them most frequent first (Zipf descending),
deduplicates identical synthesis inputs so homographs are never bought twice, synthesises each with
ElevenLabs (eleven_v4, per-language recipe from tts_recipe.py), transcribes with faster-whisper,
regenerates once on a mismatch, and PUTs the clip to the backend in incremental chunks.

Clips and their meta (including cached transcript and duration_ms) are stored under out/audio/ by
the same deterministic key the backend stores them under, so a crash never spends credits twice and
--cached-only can sync cached clips to any backend (local, staging, prod) for zero credits.

Usage (from the backend repository root):
    scripts/.venv/bin/python scripts/lexicon_research/render_clips.py --langs en
        [--varieties en-GB,en-US] [--backend http://localhost:9998/v1]
        [--limit N] [--credit-budget N] [--credit-floor N] [--chunk-size 200]
        [--workers 3] [--dry-run] [--cached-only] [--include-forms]
        [--no-ingest-filter] [--no-gate]

The ElevenLabs key is ELEVENLABS_API_KEY in .env (not required for --dry-run or --cached-only);
requests to the backend are signed with ALMONIUM_BOOKS_PUBLISHER_SECRET, exported or from .env
(see backend_env.py). Neither is ever printed.
"""

import argparse
import base64
import hashlib
import hmac
import json
import sys
import threading
import time
from concurrent.futures import ThreadPoolExecutor
from pathlib import Path

import requests
from wordfreq import zipf_frequency

sys.path.insert(0, str(Path(__file__).resolve().parent))
import lemma_gate  # noqa: E402
from backend_env import dotenv_value, publisher_secret  # noqa: E402
from tts_recipe import (  # noqa: E402
    ENGINE,
    MODEL,
    OUTPUT_FORMAT,
    PACE,
    VOICES,
    UnsupportedLanguage,
    engine_input,
    object_key,
    require_language,
    verdict,
)

ROOT = Path(__file__).resolve().parents[2]
HERE = Path(__file__).resolve().parent
OUT = HERE / "out"
PAGE = 500
ESTIMATE_CREDITS_PER_CHARACTER = 0.4  # eleven_v4, when no measured character-cost is cached
RETRY_HEADROOM = 1.12  # reserve ~12% inside budget when slicing chunks so retries can finish
ELEVENLABS = "https://api.elevenlabs.io"
BACKEND_TIMEOUT = 300


def api_key():
    return dotenv_value("ELEVENLABS_API_KEY") or sys.exit("ELEVENLABS_API_KEY missing from .env")


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


# ---------------------------------------------------------------- gates


def ingested_pronunciations(lang, include_forms=False):
    """Exact pronunciation keys present in out/lexicon-s4/<lang>/ (non-rejected Schema 4 entries).

    Keyed by (headword.lower(), lemma.lower(), pos, form_lower_or_None, variety, ipa) so a word with
    both a legacy schema-2 lexeme and a schema-4 lexeme only admits the schema-4 pronunciation.
    """
    folder = OUT / "lexicon-s4" / lang
    if not folder.is_dir():
        raise RuntimeError(f"no ingested schema-4 entries under {folder}: run ingest_v4.py --langs {lang} first")
    allowed = set()
    for path in folder.glob("*.json"):
        if path.name.startswith("_"):
            continue
        entry = json.loads(path.read_text(encoding="utf-8"))
        if entry.get("reject") and not entry.get("lexemes"):
            continue
        hw = (entry.get("headword") or "").strip().lower()
        for lex in entry.get("lexemes") or []:
            lemma = (lex.get("lemma") or hw).strip().lower()
            pos = (lex.get("pos") or "").strip()
            for pr in lex.get("pronunciations") or []:
                var = pr.get("variety")
                ipa = (pr.get("ipa") or "").strip()
                if var:
                    allowed.add((hw, lemma, pos, None, var, ipa))
            if include_forms:
                for form in lex.get("forms") or []:
                    ft = (form.get("form") or "").strip().lower()
                    for var, ipa in (form.get("ipa") or {}).items():
                        if var and ft:
                            allowed.add((hw, lemma, pos, ft, var, (ipa or "").strip()))
    return allowed


def _row_ingest_key(row):
    hw = (row.get("headword") or "").strip().lower()
    lemma = (row.get("lemma") or hw).strip().lower()
    pos = (row.get("pos") or "").strip()
    ft = (row.get("form") or "").strip().lower() if row.get("formId") else None
    var = row.get("variety")
    ipa = (row.get("phonemic") or "").strip()
    return (hw, lemma, pos, ft, var, ipa)


def _default_unfit(lang, headword):
    lemma_gate.HERE = HERE
    lemma_gate.INDEX = HERE / "wiktionary"
    return lemma_gate.unfit(lang, headword)


# ---------------------------------------------------------------- the run


def plan(lang, rows, include_forms=False, varieties=None, ingested=None, unfit_fn=None, cached_only=False):
    """Turn the backend's rows into jobs, most frequent first; the rest become skips, each with its reason.

    Only a variety with a voice of its own is rendered: a Spain voice must not be filed under
    es-MX, so es-MX and de-AT wait for voices of their own while en-US and en-GB each have one.
    Inflected forms are left out unless asked for, since they multiply the bill by two to three.
    When ingested is given, only pronunciations present in out/lexicon-s4/<lang>/ are kept; when
    unfit_fn is given, headwords rejected by the headword generation gate (lemma_gate.unfit) are
    set aside.
    """
    jobs, skipped = [], []
    gate_cache = {}
    for row in rows:
        variety = row.get("variety")
        if variety not in VOICES:
            skipped.append((row, f"no voice for {variety}"))
            continue
        if varieties and variety not in varieties:
            skipped.append((row, f"{variety} not asked for"))
            continue
        voice_id = VOICES[variety][1]
        if row.get("formId") and not include_forms:
            skipped.append((row, "inflected form, not asked for"))
            continue
        if ingested is not None and _row_ingest_key(row) not in ingested:
            skipped.append((row, "not in schema-4 ingest"))
            continue
        headword = (row.get("headword") or "").strip()
        if unfit_fn is not None:
            if headword not in gate_cache:
                gate_cache[headword] = unfit_fn(lang, headword)
            reason = gate_cache[headword]
            if reason is not None:
                skipped.append((row, f"gate: {reason}"))
                continue
        text = row.get("form") if row.get("formId") else headword
        built = engine_input(lang, text, row.get("phonemic"))
        if not built:
            skipped.append((row, "recipe cannot be built"))
            continue
        sent, mode = built
        if cached_only:
            key = object_key(variety, voice_id, sent, 1)
            path = OUT / key
            if not (path.exists() and path.with_suffix(".json").exists()):
                skipped.append((row, "not cached on disk"))
                continue
        jobs.append(dict(row=row, lang=lang, variety=variety, voice_id=voice_id, text=text, input=sent, mode=mode, clips=[]))

    jobs.sort(
        key=lambda j: (
            -zipf_frequency(j["row"].get("headword") or "", lang),
            (j["row"].get("headword") or "").lower(),
            j["row"].get("rank", 1),
            j["variety"],
            str(j["row"].get("id") or ""),
        )
    )
    return jobs, skipped


def estimate(job):
    """Credits one attempt would cost: the measured cost when the clip is cached, else the eleven_v4 rate."""
    meta = (OUT / object_key(job["variety"], job["voice_id"], job["input"])).with_suffix(".json")
    if meta.exists():
        cached = json.loads(meta.read_text()).get("character_cost")
        if cached is not None:
            return cached
    return len(job["input"]) * ESTIMATE_CREDITS_PER_CHARACTER


def _is_cached(key):
    path = OUT / key
    return path.exists() and path.with_suffix(".json").exists()


def estimate_new_credits(jobs):
    """Credits to buy every uncached unique attempt-1 clip in jobs (0 for cached or duplicate keys)."""
    seen = set()
    credits = 0.0
    unique_new = 0
    unique_cached = 0
    for job in jobs:
        key = object_key(job["variety"], job["voice_id"], job["input"], 1)
        if key in seen:
            continue
        seen.add(key)
        if _is_cached(key):
            unique_cached += 1
        else:
            unique_new += 1
            credits += len(job["input"]) * ESTIMATE_CREDITS_PER_CHARACTER
    return credits, unique_new, unique_cached


def synthesise_all(eleven, jobs, attempt, workers):
    """Synthesise each unique (variety, voice, input, attempt) key at most once, even across threads."""
    groups = {}
    for job in jobs:
        key = object_key(job["variety"], job["voice_id"], job["input"], attempt)
        groups.setdefault(key, []).append(job)

    def one(key, rep):
        record, reused = eleven.synthesise(rep["lang"], rep["voice_id"], rep["input"], OUT / key)
        return key, record, reused

    with ThreadPoolExecutor(max_workers=workers) as pool:
        futures = [pool.submit(one, key, group[0]) for key, group in groups.items()]
        for n, future in enumerate(futures, 1):
            key, record, reused = future.result()
            for i, job in enumerate(groups[key]):
                job["clips"].append(dict(record, attempt=attempt, object_key=key, reused=reused or i > 0))
            if n % 50 == 0:
                print(f"  {n}/{len(groups)} unique clips, {eleven.spent:.0f} credits so far", flush=True)


def load_cached_attempt(jobs, attempt):
    """Attach already-cached clips from disk to jobs (used by --cached-only, zero ElevenLabs calls)."""
    loaded = []
    for job in jobs:
        key = object_key(job["variety"], job["voice_id"], job["input"], attempt)
        if not _is_cached(key):
            continue
        meta = (OUT / key).with_suffix(".json")
        record = json.loads(meta.read_text(encoding="utf-8"))
        job["clips"].append(dict(record, attempt=attempt, object_key=key, reused=True))
        loaded.append(job)
    return loaded


def transcribe_latest(transcriber, jobs, model_box=None):
    """Transcribe each unique latest clip at most once, reusing & persisting transcript/duration_ms in meta."""
    seen = {}
    for job in jobs:
        clip = job["clips"][-1]
        if clip.get("error"):
            clip["verdict"] = "error"
            continue
        key = (clip["object_key"], job["lang"])
        if key in seen:
            transcript, duration_ms = seen[key]
        else:
            meta = (OUT / clip["object_key"]).with_suffix(".json")
            meta_data = json.loads(meta.read_text(encoding="utf-8")) if meta.exists() else {}
            if meta_data.get("transcript") is not None and meta_data.get("duration_ms") is not None:
                transcript, duration_ms = meta_data["transcript"], meta_data["duration_ms"]
            else:
                if model_box is not None:
                    if model_box[0] is None:
                        model_box[0] = transcriber() if callable(transcriber) else transcriber
                    model = model_box[0]
                else:
                    model = transcriber() if callable(transcriber) else transcriber
                transcript, duration_ms = model.transcribe(OUT / clip["object_key"], job["lang"])
                if meta.exists():
                    meta_data["transcript"] = transcript
                    meta_data["duration_ms"] = duration_ms
                    meta.write_text(json.dumps(meta_data, ensure_ascii=False), encoding="utf-8")
            seen[key] = (transcript, duration_ms)
        clip["transcript"], clip["duration_ms"] = transcript, duration_ms
        clip["verdict"] = verdict(job["text"], transcript)


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


def _chunk_by_headword(jobs, chunk_size):
    """Split jobs into chunks of ~chunk_size without separating varieties/homographs of the same headword."""
    if not chunk_size or chunk_size >= len(jobs):
        return [jobs] if jobs else []
    chunks, current = [], []
    last_hw = None
    for job in jobs:
        hw = (job["row"].get("headword") or "").lower()
        if current and len(current) >= chunk_size and hw != last_hw:
            chunks.append(current)
            current = []
        current.append(job)
        last_hw = hw
    if current:
        chunks.append(current)
    return chunks


def _fit_budget(chunk, available):
    """Keep whole headwords from chunk whose estimated new credits (with retry headroom) fit in available."""
    if available == float("inf"):
        return chunk, False
    # group chunk by headword preserving order
    hw_groups = []
    for job in chunk:
        hw = (job["row"].get("headword") or "").lower()
        if hw_groups and hw_groups[-1][0] == hw:
            hw_groups[-1][1].append(job)
        else:
            hw_groups.append((hw, [job]))
    kept = []
    seen = set()
    est_credits = 0.0
    stopped = False
    for _, group in hw_groups:
        add = 0.0
        new_keys = []
        for job in group:
            key = object_key(job["variety"], job["voice_id"], job["input"], 1)
            if key not in seen and not _is_cached(key):
                add += len(job["input"]) * ESTIMATE_CREDITS_PER_CHARACTER
                new_keys.append(key)
        if add > 0 and (est_credits + add) * RETRY_HEADROOM > available:
            stopped = True
            break
        est_credits += add
        seen.update(new_keys)
        kept.extend(group)
    return kept, stopped


def new_totals():
    return dict(pending=0, rendered=0, verified=0, unverified=0, skipped=0, errors=0, credits=0.0, reused=0, not_stored=0)


def run(
    langs,
    backend,
    eleven,
    transcriber,
    limit=0,
    workers=3,
    dry_run=False,
    include_forms=False,
    varieties=None,
    ingested_only=False,
    gate=False,
    cached_only=False,
    chunk_size=0,
    unfit_fn=None,
):
    """Render every language; returns {lang: totals}. transcriber may be a factory, called once on first need.

    limit caps the clips rendered per language, after the rows nobody asked for are set aside.
    """
    totals = {}
    model_box = [None]
    effective_unfit = (unfit_fn or _default_unfit) if gate else None
    for lang in langs:
        require_language(lang)  # refuse before touching the backend
        ingested = ingested_pronunciations(lang, include_forms) if ingested_only else None
        rows = backend.pending(lang)
        jobs, skipped = plan(lang, rows, include_forms, varieties, ingested, effective_unfit, cached_only)
        if limit:
            jobs = jobs[:limit]
        t = totals[lang] = new_totals()
        t["pending"], t["skipped"] = len(rows), len(skipped)
        reasons = {}
        for row, reason in skipped:
            reasons[reason] = reasons.get(reason, 0) + 1
            if reason == "recipe cannot be built":
                print(f"  skip {lang} {row.get('headword')!r} (pronunciation {row.get('id')}): {reason}", flush=True)
        for reason, count in sorted(reasons.items()):
            print(f"{lang}: {count} set aside, {reason}", flush=True)
        if dry_run:
            new_credits, unique_new, unique_cached = estimate_new_credits(jobs)
            all_first_pass = sum(estimate(j) for j in jobs)
            unique_hw = len({(j["row"].get("headword") or "").lower() for j in jobs})
            print(
                f"{lang}: {len(jobs)} to render ({unique_hw} headwords, {unique_new} unique new clips, "
                f"{unique_cached} cached on disk), {len(skipped)} skipped, "
                f"about {new_credits:.0f} new credits (~{new_credits * RETRY_HEADROOM:.0f} w/ retries)"
            )
            for j in jobs[:40]:
                print(f"  {lang} {j['text']:24} {j['variety']:5} {j['mode']:12} {j['input']}")
            if len(jobs) > 40:
                print(f"  ... ({len(jobs) - 40} more)")
            t["credits"] = new_credits if (ingested_only or gate or cached_only or unique_cached) else all_first_pass
            continue
        if not jobs:
            continue

        chunks = _chunk_by_headword(jobs, chunk_size)
        for idx, chunk in enumerate(chunks, 1):
            if cached_only:
                load_cached_attempt(chunk, 1)
                transcribe_latest(transcriber, chunk, model_box)
                retry = [j for j in chunk if j["clips"][0]["verdict"] == "mismatch"]
                retry_loaded = load_cached_attempt(retry, 2)
                if retry_loaded:
                    transcribe_latest(transcriber, retry_loaded, model_box)
                t["reused"] += sum(1 for j in chunk for c in j["clips"] if c.get("reused"))
            else:
                available = (eleven.budget - eleven.spent) if eleven.budget else float("inf")
                chunk, budget_stopped = _fit_budget(chunk, available)
                if not chunk:
                    print(
                        f"{lang}: stopping before chunk {idx}/{len(chunks)} "
                        f"(budget remaining {available:.0f} credits)",
                        flush=True,
                    )
                    break
                if len(chunks) > 1:
                    print(
                        f"{lang}: chunk {idx}/{len(chunks)} ({len(chunk)} jobs, {workers} workers, "
                        f"{eleven.spent:.0f} credits spent so far)",
                        flush=True,
                    )
                else:
                    print(f"{lang}: synthesising {len(chunk)} words, {workers} in parallel", flush=True)
                before = eleven.spent
                synthesise_all(eleven, chunk, 1, workers)
                transcribe_latest(transcriber, chunk, model_box)
                retry = [j for j in chunk if j["clips"][0]["verdict"] == "mismatch"]
                if retry:
                    print(f"{lang}: regenerating {len(retry)} mismatches", flush=True)
                    synthesise_all(eleven, retry, 2, workers)
                    transcribe_latest(transcriber, retry, model_box)
                t["credits"] += eleven.spent - before
                t["reused"] += sum(1 for j in chunk for c in j["clips"] if c.get("reused"))

            chunk_rendered = chunk_verified = chunk_unverified = 0
            for job in chunk:
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
                chunk_rendered += 1
                if status == "verified":
                    chunk_verified += 1
                else:
                    chunk_unverified += 1
                if not answer.get("stored", True):
                    t["not_stored"] += 1
            if len(chunks) > 1:
                spent_str = f"{eleven.spent:.0f}" if eleven is not None else "0"
                print(
                    f"  -> uploaded {chunk_rendered} ({chunk_verified} verified, {chunk_unverified} unverified); "
                    f"total rendered {t['rendered']}, {spent_str} credits spent",
                    flush=True,
                )
            if not cached_only and budget_stopped:
                print(f"{lang}: credit budget reached after chunk {idx}/{len(chunks)}; stopping cleanly", flush=True)
                break
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
    parser.add_argument("--langs", required=True, help="comma-separated languages, e.g. en,fr,es")
    parser.add_argument("--backend", default="http://localhost:9998/v1")
    parser.add_argument("--limit", type=int, default=0, help="pronunciations per language (0 = all)")
    parser.add_argument("--credit-budget", type=float, default=0, help="credits this run may spend (0 = no cap)")
    parser.add_argument("--credit-floor", type=float, default=5000, help="stop when the plan has fewer credits left")
    parser.add_argument("--chunk-size", type=int, default=200, help="jobs per incremental upload chunk (0 = all at once)")
    parser.add_argument("--workers", type=int, default=3)
    parser.add_argument("--dry-run", action="store_true", help="list what would be rendered; no synthesis")
    parser.add_argument("--cached-only", action="store_true", help="upload clips already cached on disk; zero ElevenLabs calls")
    parser.add_argument(
        "--include-forms", action="store_true", help="render inflected forms too, not only the headword level"
    )
    parser.add_argument("--varieties", default="", help="only these varieties, e.g. en-GB,en-US (default: every voiced one)")
    parser.add_argument(
        "--no-ingest-filter", action="store_true", help="do not restrict to out/lexicon-s4/<lang>/ schema-4 entries"
    )
    parser.add_argument("--no-gate", action="store_true", help="do not apply the lemma_gate.unfit headword filter")
    args = parser.parse_args()

    langs = [lang.strip() for lang in args.langs.split(",") if lang.strip()]
    try:
        for lang in langs:
            require_language(lang)
    except UnsupportedLanguage as e:
        sys.exit(str(e))

    backend = Backend(args.backend, publisher_secret())
    eleven = None
    if not args.dry_run and not args.cached_only:
        probe = ElevenLabs(api_key())
        remaining, limit = probe.credits_remaining()
        print(f"credits remaining: {remaining} of {limit}", flush=True)
        if remaining < args.credit_floor:
            sys.exit(f"fewer than {args.credit_floor:.0f} credits remain; stopping")
        floor_cap = max(0.0, remaining - args.credit_floor) if args.credit_floor > 0 else 0.0
        effective_budget = args.credit_budget
        if floor_cap > 0:
            effective_budget = min(effective_budget, floor_cap) if effective_budget else floor_cap
        eleven = ElevenLabs(api_key(), effective_budget)

    def load_whisper():
        print("loading faster-whisper small (cpu, int8)", flush=True)
        return Transcriber()

    try:
        varieties = {v.strip() for v in args.varieties.split(",") if v.strip()} or None
        totals = run(
            langs,
            backend,
            eleven,
            load_whisper,
            args.limit,
            args.workers,
            args.dry_run,
            args.include_forms,
            varieties,
            ingested_only=not args.no_ingest_filter,
            gate=not args.no_gate,
            cached_only=args.cached_only,
            chunk_size=args.chunk_size,
        )
    except (RuntimeError, requests.RequestException) as e:
        sys.exit(f"backend: {e}")
    print_report(totals, args.dry_run)
    if eleven is not None:
        remaining, limit = eleven.credits_remaining()
        print(f"\n{eleven.spent:.0f} credits across {eleven.requests} requests; {remaining} of {limit} remain on the plan")
    sys.exit(1 if any(t["errors"] for t in totals.values()) else 0)


if __name__ == "__main__":
    main()
