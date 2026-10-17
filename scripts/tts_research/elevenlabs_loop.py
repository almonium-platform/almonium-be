"""ElevenLabs full loop: synthesise a headword, transcribe the clip, compare, regenerate once.

This is the verification loop decided on 2026-10-03 (docs/TTS_IPA_FINDINGS.md, "Decision for the
launch languages"): every ElevenLabs clip is machine-transcribed and compared with the headword;
a mismatch regenerates once, and a second mismatch marks the clip "unverified" (the Chirp fallback
is not built here).

Input per language follows elevenlabs_recipe.py: French, Spanish and Polish go as the bare
spelling. Ukrainian goes bare when it has one syllable, as IPA with /ɨ/ when the spelling contains
"и", and otherwise as the spelling with an acute stress mark after the stressed vowel, derived
from the entry's IPA.

Headwords: 100 per language. French, Spanish and Ukrainian from the lexicon entry files (the
file stem is the headword, the first lexeme's first pronunciation is the IPA). Polish from
wordfreq's top 400, words of four letters or more.

Transcription: faster-whisper, "small" model, CPU, int8, language fixed to the word's language,
one model instance, sequential.

Usage (from the backend repository root, with scripts/.venv):
    scripts/.venv/bin/python scripts/tts_research/elevenlabs_loop.py <output folder> [--select-only]

Writes <output folder>/audio/<variety>/<voice id>/<sha256>.mp3, manifest.json, report.md,
index.html. Re-running reuses clips already on disk, so a crash in transcription does not
spend credits twice. The key is ELEVENLABS_API_KEY in .env and is never printed.
"""

import hashlib
import html
import json
import statistics
import sys
import threading
import time
import unicodedata
from concurrent.futures import ThreadPoolExecutor
from pathlib import Path

import requests

ROOT = Path(__file__).resolve().parents[2]
ENTRIES = ROOT / "scripts/lexicon_research/out/cc-claude-opus-5-5"

ENGINE = "elevenlabs"
MODEL = "eleven_v4"
OUTPUT_FORMAT = "mp3_44100_128"
PACE = "1.0"  # no speed setting is sent; recorded in the object key for later variants
WORDS_PER_LANGUAGE = 100
PARALLEL = 3  # the plan's concurrency cap
CREDIT_BUDGET = 3000
CREDIT_FLOOR = 5000

VOICES = {
    "uk": ("Ukrainian", "Alex Nekrasov", "9Sj8ugvpK1DmcAXyvi3a"),
    "fr": ("French", "Nicolas", "aQROLel5sQbj1vuIVi6B"),
    "es": ("Spanish", "David Martin", "Nh2zY9kknu6z4pZy6FhD"),
    "pl": ("Polish", "Adam", "hIssydxXZ1WuDorjx6Ic"),
}
VARIETY = {"uk": "uk-UA", "fr": "fr-FR", "es": "es-ES", "pl": "pl-PL"}
LANGUAGES = ("fr", "es", "pl", "uk")

ACUTE = "́"
UK_VOWEL_LETTERS = set("аеєиіїоуюя")
IPA_VOWELS = set("aɑeɛiɪɨoɔuʉy")


def api_key():
    for line in (ROOT / ".env").read_text().splitlines():
        if line.startswith("ELEVENLABS_API_KEY="):
            return line.split("=", 1)[1].strip()
    sys.exit("ELEVENLABS_API_KEY missing from .env")


KEY = api_key()
HEADERS = {"xi-api-key": KEY}


def credits_remaining():
    r = requests.get("https://api.elevenlabs.io/v1/user/subscription", headers=HEADERS, timeout=30)
    r.raise_for_status()
    d = r.json()
    return d["character_limit"] - d["character_count"], d["character_limit"]


# ---------------------------------------------------------------- word selection


def spread(items, n):
    if len(items) <= n:
        return list(items)
    return [items[round(i * (len(items) - 1) / (n - 1))] for i in range(n)]


def entry_candidates(lang):
    """(headword, ipa) for every single-word entry with a pronunciation, in file order."""
    out = []
    for f in sorted((ENTRIES / lang).glob("*.json")):
        if f.name.endswith(".meta.json"):
            continue
        headword = f.stem
        if " " in headword:
            continue
        data = json.loads(f.read_text())
        lexemes = data.get("lexemes") or []
        if not lexemes:
            continue
        pron = lexemes[0].get("pronunciations") or []
        ipa = pron[0].get("ipa") if pron else None
        out.append((headword, ipa))
    return out


def uk_input(headword, ipa):
    """Return (text sent, mode) per the recipe, or None when the entry lacks what the rule needs."""
    vowels = [c for c in headword if c in UK_VOWEL_LETTERS]
    if len(vowels) == 1:
        return headword, "bare"
    if "и" in headword:
        if not ipa:
            return None
        body = ipa.strip("/").replace("ɪ", "ɨ")
        # the engine wants a dot at every syllable boundary, including before the stress mark
        body = body.replace(".ˈ", "ˈ").replace("ˈ", ".ˈ")
        if body.startswith(".ˈ"):
            body = body[1:]
        return f"/{body}/", "ipa-ɨ"
    if not ipa or "ˈ" not in ipa:
        return None
    before = ipa.split("ˈ", 1)[0]
    stressed_index = sum(1 for c in before if c in IPA_VOWELS)
    total_ipa_vowels = sum(1 for c in ipa if c in IPA_VOWELS)
    if total_ipa_vowels != len(vowels) or stressed_index >= len(vowels):
        return None
    seen = -1
    for i, c in enumerate(headword):
        if c in UK_VOWEL_LETTERS:
            seen += 1
            if seen == stressed_index:
                return headword[: i + 1] + ACUTE + headword[i + 1 :], "stress-mark"
    return None


def select_words():
    words = []
    for lang in ("fr", "es"):
        cands = entry_candidates(lang)
        for headword, ipa in spread(cands, WORDS_PER_LANGUAGE):
            words.append(dict(language=lang, headword=headword, ipa=ipa, input=headword, mode="bare"))
    cands = []
    for headword, ipa in entry_candidates("uk"):
        built = uk_input(headword, ipa)
        if built:
            cands.append((headword, ipa, *built))
    for headword, ipa, text, mode in spread(cands, WORDS_PER_LANGUAGE):
        words.append(dict(language="uk", headword=headword, ipa=ipa, input=text, mode=mode))
    from wordfreq import top_n_list

    pool = [w for w in top_n_list("pl", 400) if len(w) >= 4 and w.isalpha()]
    for headword in spread(pool, WORDS_PER_LANGUAGE):
        words.append(dict(language="pl", headword=headword, ipa=None, input=headword, mode="bare"))
    return words


# ---------------------------------------------------------------- synthesis

spent_lock = threading.Lock()
spent = {"credits": 0.0, "requests": 0}


def object_key(lang, text, attempt):
    voice_id = VOICES[lang][2]
    basis = "|".join([ENGINE, voice_id, MODEL, PACE, OUTPUT_FORMAT, text])
    digest = hashlib.sha256(basis.encode()).hexdigest()
    suffix = "" if attempt == 1 else f"-r{attempt}"
    return f"audio/{VARIETY[lang]}/{voice_id}/{digest}{suffix}.mp3"


def synthesise(out, word, attempt):
    """POST one word; returns a clip record. Reuses a clip already on disk (with its meta)."""
    lang = word["language"]
    key = object_key(lang, word["input"], attempt)
    path = out / key
    meta = path.with_suffix(".json")
    if path.exists() and meta.exists():
        return json.loads(meta.read_text())
    path.parent.mkdir(parents=True, exist_ok=True)
    url = f"https://api.elevenlabs.io/v1/text-to-speech/{VOICES[lang][2]}?output_format={OUTPUT_FORMAT}"
    body = {"text": word["input"], "model_id": MODEL, "language_code": lang}
    record = dict(attempt=attempt, object_key=key, bytes=0, character_cost=None, seconds=None, error=None)
    for tries in range(8):
        with spent_lock:
            if spent["credits"] >= CREDIT_BUDGET:
                record["error"] = "budget reached"
                return record
        t0 = time.time()
        try:
            r = requests.post(url, headers=HEADERS, json=body, timeout=120)
        except requests.RequestException as e:
            record["error"] = f"request failed: {e}"
            time.sleep(5)
            continue
        elapsed = time.time() - t0
        if r.status_code == 429:
            wait = float(r.headers.get("retry-after") or 5 * (tries + 1))
            print(f"  429 on {word['headword']}, waiting {wait:.0f}s", flush=True)
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
            seconds=round(elapsed, 3),
            request_id=r.headers.get("request-id"),
            error=None,
        )
        with spent_lock:
            spent["credits"] += float(cost) if cost else len(word["input"])
            spent["requests"] += 1
        meta.write_text(json.dumps(record, ensure_ascii=False))
        break
    return record


def synthesise_all(out, jobs):
    """jobs: list of (word, attempt). Runs PARALLEL at a time, in order of submission."""
    results = {}
    with ThreadPoolExecutor(max_workers=PARALLEL) as pool:
        futures = {pool.submit(synthesise, out, w, a): (id(w), a) for w, a in jobs}
        done = 0
        for fut in futures:
            results[futures[fut]] = fut.result()
            done += 1
            if done % 25 == 0:
                print(f"  {done}/{len(jobs)} clips, {spent['credits']:.0f} credits so far", flush=True)
    return results


# ---------------------------------------------------------------- comparison


def normalise(s):
    s = unicodedata.normalize("NFC", s).lower().replace(ACUTE, "")
    s = "".join(c for c in s if not unicodedata.category(c).startswith(("P", "S")))
    return " ".join(s.split())


def strip_diacritics(s):
    decomposed = unicodedata.normalize("NFD", s)
    return unicodedata.normalize("NFC", "".join(c for c in decomposed if not unicodedata.combining(c)))


def edit_distance(a, b):
    prev = list(range(len(b) + 1))
    for i, ca in enumerate(a, 1):
        cur = [i]
        for j, cb in enumerate(b, 1):
            cur.append(min(prev[j] + 1, cur[j - 1] + 1, prev[j - 1] + (ca != cb)))
        prev = cur
    return prev[-1]


def verdict(headword, transcript):
    h, t = normalise(headword), normalise(transcript or "")
    if not t:
        return "mismatch"
    if h == t:
        return "match"
    h2, t2 = strip_diacritics(h), strip_diacritics(t)
    if h2 == t2:
        return "match-ignoring-diacritics"
    if len(h2) >= 6 and edit_distance(h2, t2) <= 1:
        return "match-within-1"
    return "mismatch"


# ---------------------------------------------------------------- transcription


def load_whisper():
    from faster_whisper import WhisperModel

    return WhisperModel("small", device="cpu", compute_type="int8")


def transcribe(model, path, lang):
    segments, _ = model.transcribe(
        str(path), language=lang, beam_size=5, condition_on_previous_text=False, without_timestamps=True
    )
    return " ".join(s.text for s in segments).strip()


# ---------------------------------------------------------------- outputs


def write_report(out, words, totals):
    lines = ["# ElevenLabs full loop", "", f"Model `{MODEL}`, {len(words)} words, {PARALLEL} requests in parallel.", ""]
    lines.append("| Language | Voice | Words | Matched first time | Matched after regeneration | Unverified | Errors | Credits | Mean s/request |")
    lines.append("|---|---|---|---|---|---|---|---|---|")
    for lang in LANGUAGES:
        t = totals[lang]
        lines.append(
            f"| {VOICES[lang][0]} | {VOICES[lang][1]} | {t['words']} | {t['first']} | {t['second']} | {t['unverified']} | {t['errors']} | {t['credits']:.0f} | {t['mean_seconds']:.2f} |"
        )
    t = totals["all"]
    lines.append(f"| **All** | | {t['words']} | {t['first']} | {t['second']} | {t['unverified']} | {t['errors']} | {t['credits']:.0f} | {t['mean_seconds']:.2f} |")
    lines += ["", f"Credits: {totals['all']['credits']:.0f} across {totals['all']['requests']} requests; " f"{totals['remaining_after']} of {totals['limit']} remain on the plan.", ""]
    for lang in LANGUAGES:
        bad = [w for w in words if w["language"] == lang and w["verdict"] in ("unverified", "error")]
        lines += [f"## {VOICES[lang][0]}: unverified ({len(bad)})", ""]
        if bad:
            lines += ["| Headword | Input | Transcript 1 | Transcript 2 |", "|---|---|---|---|"]
            for w in bad:
                ts = [c.get("transcript", "") or "" for c in w["clips"]]
                ts += [""] * (2 - len(ts))
                lines.append(f"| {w['headword']} | `{w['input']}` | {ts[0]} | {ts[1]} |")
        lines.append("")
        second = [w for w in words if w["language"] == lang and w["verdict"] == "match-second"]
        lines += [f"## {VOICES[lang][0]}: matched only after regeneration ({len(second)})", ""]
        if second:
            lines += ["| Headword | Input | Transcript 1 | Transcript 2 |", "|---|---|---|---|"]
            for w in second:
                ts = [c.get("transcript", "") or "" for c in w["clips"]]
                lines.append(f"| {w['headword']} | `{w['input']}` | {ts[0]} | {ts[1]} |")
        lines.append("")
    (out / "report.md").write_text("\n".join(lines))


STYLE = (
    "<style>body{font:16px system-ui;max-width:1100px;margin:30px auto;padding:0 16px}"
    "table{border-collapse:collapse;width:100%}td,th{border-bottom:1px solid #ddd;padding:8px 6px;vertical-align:top;text-align:left}"
    "audio{width:200px;height:32px}small{color:#666}h2{margin-top:36px}code{font-size:15px}"
    "@media(prefers-color-scheme:dark){body{background:#221e23;color:#f5edf2}td,th{border-color:#444}small{color:#bbb}}</style>"
)


def write_index(out, words):
    page = f"<!doctype html><meta charset=utf-8><title>ElevenLabs loop: listen</title>{STYLE}"
    page += "<h1>ElevenLabs loop: clips that did not match first time</h1><p>Under each clip is what faster-whisper (small) wrote down. Clips that matched the headword on the first try are not listed.</p>"
    for lang in LANGUAGES:
        rows = [w for w in words if w["language"] == lang and w["verdict"] != "match-first"]
        page += f"<h2>{VOICES[lang][0]} <small>voice: {html.escape(VOICES[lang][1])}, {len(rows)} words</small></h2>"
        page += "<table><tr><th>Headword</th><th>Input sent</th><th>Verdict</th><th>Clip 1</th><th>Clip 2</th></tr>"
        for w in rows:
            cells = ""
            for c in w["clips"]:
                if c.get("error"):
                    cells += f"<td><small>{html.escape(c['error'])}</small></td>"
                    continue
                cells += (
                    f"<td><audio controls preload=none src='{html.escape(c['object_key'])}'></audio><br>"
                    f"<small>heard: {html.escape(c.get('transcript') or '(nothing)')}</small></td>"
                )
            cells += "<td></td>" * (2 - len(w["clips"]))
            ipa = f"<br><small>{html.escape(w['ipa'])}</small>" if w.get("ipa") else ""
            page += f"<tr><td><b>{html.escape(w['headword'])}</b>{ipa}</td><td><code>{html.escape(w['input'])}</code><br><small>{w['mode']}</small></td><td>{w['verdict']}</td>{cells}</tr>"
        page += "</table>"
    (out / "index.html").write_text(page)


# ---------------------------------------------------------------- main


def main():
    if len(sys.argv) < 2:
        sys.exit(__doc__)
    out = Path(sys.argv[1])
    out.mkdir(parents=True, exist_ok=True)
    words = select_words()
    counts = {l: sum(1 for w in words if w["language"] == l) for l in LANGUAGES}
    print("selected", counts, flush=True)
    if "--select-only" in sys.argv:
        for w in words:
            print(f"{w['language']} {w['headword']:20} {w['mode']:12} {w['input']}")
        return

    remaining, limit = credits_remaining()
    print(f"credits remaining: {remaining} of {limit}", flush=True)
    if remaining < CREDIT_FLOOR:
        sys.exit(f"fewer than {CREDIT_FLOOR} credits remain; stopping")

    print(f"synthesising {len(words)} words, {PARALLEL} in parallel", flush=True)
    first = synthesise_all(out, [(w, 1) for w in words])
    for w in words:
        w["clips"] = [first[(id(w), 1)]]

    print("loading faster-whisper small (cpu, int8)", flush=True)
    model = load_whisper()
    for n, w in enumerate(words, 1):
        c = w["clips"][0]
        if c.get("error"):
            c["verdict"] = "error"
            continue
        c["transcript"] = transcribe(model, out / c["object_key"], w["language"])
        c["verdict"] = verdict(w["headword"], c["transcript"])
        if n % 50 == 0:
            print(f"  transcribed {n}/{len(words)}", flush=True)

    retry = [w for w in words if w["clips"][0]["verdict"] == "mismatch"]
    print(f"regenerating {len(retry)} mismatches", flush=True)
    second = synthesise_all(out, [(w, 2) for w in retry])
    for w in retry:
        c = second[(id(w), 2)]
        w["clips"].append(c)
        if c.get("error"):
            c["verdict"] = "error"
            continue
        c["transcript"] = transcribe(model, out / c["object_key"], w["language"])
        c["verdict"] = verdict(w["headword"], c["transcript"])

    for w in words:
        verdicts = [c["verdict"] for c in w["clips"]]
        if verdicts[0] == "error":
            w["verdict"] = "error"
        elif verdicts[0] != "mismatch":
            w["verdict"] = "match-first"
        elif len(verdicts) > 1 and verdicts[1] not in ("mismatch", "error"):
            w["verdict"] = "match-second"
        elif len(verdicts) > 1 and verdicts[1] == "error":
            w["verdict"] = "error"
        else:
            w["verdict"] = "unverified"
        w["variety"] = VARIETY[w["language"]]
        w["voice"] = {"name": VOICES[w["language"]][1], "id": VOICES[w["language"]][2]}
        w["model"] = MODEL

    totals = {}
    for lang in LANGUAGES + ("all",):
        ws = [w for w in words if lang == "all" or w["language"] == lang]
        clips = [c for w in ws for c in w["clips"] if not c.get("error")]
        totals[lang] = dict(
            words=len(ws),
            first=sum(w["verdict"] == "match-first" for w in ws),
            second=sum(w["verdict"] == "match-second" for w in ws),
            unverified=sum(w["verdict"] == "unverified" for w in ws),
            errors=sum(w["verdict"] == "error" for w in ws),
            credits=sum(c["character_cost"] or 0 for c in clips),
            requests=len(clips),
            mean_seconds=statistics.mean([c["seconds"] for c in clips]) if clips else 0.0,
        )
    remaining_after, limit = credits_remaining()
    totals["remaining_after"], totals["limit"] = remaining_after, limit

    (out / "manifest.json").write_text(json.dumps(dict(engine=ENGINE, model=MODEL, output_format=OUTPUT_FORMAT, totals=totals, words=words), ensure_ascii=False, indent=1))
    write_report(out, words, totals)
    write_index(out, words)
    print((out / "report.md").read_text())
    print(f"\n{out / 'index.html'}")


if __name__ == "__main__":
    main()
