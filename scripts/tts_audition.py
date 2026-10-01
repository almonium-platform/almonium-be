#!/usr/bin/env python3
"""Build a private, offline listening pack. No application or infrastructure changes.

Demos are Google's published recordings. Custom clips require the project's TTS
API to be enabled. Credentials stay in memory; manifests contain public data only.
"""
import argparse
import base64
import hashlib
import json
import math
import os
from pathlib import Path
import struct
import subprocess
import sys
import time
from html.parser import HTMLParser
from urllib.request import urlopen

ROOT = Path(__file__).resolve().parents[1]
INVENTORY = "https://docs.cloud.google.com/text-to-speech/docs/list-voices-and-types"
FOCUS = ("en-US", "en-GB", "de-DE", "uk-UA")
CANDIDATES = ("Charon", "Orus", "Puck")


class VoiceTable(HTMLParser):
    def __init__(self):
        super().__init__()
        self.rows, self.cells, self.cell, self.src = {}, [], None, None

    def handle_starttag(self, tag, attrs):
        if tag == "tr":
            self.cells, self.src = [], None
        elif tag == "td":
            self.cell = []
        elif tag == "source":
            self.src = dict(attrs).get("src")

    def handle_data(self, data):
        if self.cell is not None:
            self.cell.append(data)

    def handle_endtag(self, tag):
        if tag == "td" and self.cell is not None:
            self.cells.append("".join(self.cell).strip())
            self.cell = None
        elif tag == "tr" and len(self.cells) >= 5 and self.src:
            if self.src.startswith("/static/text-to-speech/docs/audio/"):
                self.rows[self.cells[3]] = "https://cloud.google.com" + self.src


def session(env_file):
    from google.oauth2 import service_account
    from google.auth.transport.requests import AuthorizedSession
    value = os.environ.get("GOOGLE_SERVICE_ACCOUNT_KEY_BASE64")
    if not value and env_file.exists():
        for line in env_file.read_text().splitlines():
            key, separator, candidate = line.partition("=")
            if separator and key.strip() == "GOOGLE_SERVICE_ACCOUNT_KEY_BASE64":
                value = candidate.strip().strip("\"'")
    if not value:
        raise RuntimeError("GOOGLE_SERVICE_ACCOUNT_KEY_BASE64 is not configured")
    try:
        credentials = service_account.Credentials.from_service_account_info(
            json.loads(base64.b64decode(value)),
            scopes=["https://www.googleapis.com/auth/cloud-platform"],
        )
        return AuthorizedSession(credentials)
    except Exception:
        raise RuntimeError("Google credential could not be loaded; details suppressed") from None


def provider_error(response):
    try:
        error = response.json().get("error", {})
        reasons = [detail["reason"] for detail in error.get("details", []) if "reason" in detail]
        return f"HTTP {response.status_code}: {error.get('status', 'UNKNOWN')} {' '.join(reasons)}".strip()
    except (ValueError, AttributeError):
        return f"HTTP {response.status_code}"


def samples(locale):
    if locale.startswith("en-"):
        # Google's en-GB phoneme inventory rejects the dictionary /r/ and wants the approximant.
        r = "ɹ" if locale == "en-GB" else "r"
        return [
            ("sentence", "Today I learned a new word. Could you say it again, slowly and clearly?", None),
            ("apple-text", "apple", None),
            ("apple-ipa", "apple", "ˈæpəl"),
            ("read-text", "read", None),
            ("read-present-ipa", "read", r + "iːd"),
            ("read-past-ipa", "read", r + "ɛd"),
            ("compound", "hot dog", None),
            ("compound-context", "I ordered a hot dog.", None),
        ]
    if locale == "de-DE":
        return [
            ("sentence", "Heute lerne ich neue Wörter. Kannst du das bitte langsam und deutlich wiederholen?", None),
            ("word", "Entschuldigung", None),
            ("compound", "heiße Schokolade", None),
            ("ambiguous", "umfahren", None),
        ]
    return [
        ("sentence", "Сьогодні я вивчаю нові слова. Повтори, будь ласка, повільно й чітко.", None),
        ("word", "дякую", None),
        ("ambiguous", "замок", None),
        ("castle-context", "Цей замок стоїть на високій горі.", None),
        ("lock-context", "Дверний замок зламався.", None),
    ]


def custom_plan():
    result = []
    for locale in FOCUS:
        for name in CANDIDATES:
            voice = f"{locale}-Chirp3-HD-{name}"
            for key, text, ipa in samples(locale):
                input_data = {"text": text}
                if ipa:
                    input_data["customPronunciations"] = {"pronunciations": [{
                        "phrase": text, "phoneticEncoding": "PHONETIC_ENCODING_IPA", "pronunciation": ipa,
                    }]}
                result.append({"id": f"custom-{locale}-{name}-{key}", "locale": locale,
                               "voiceId": voice, "section": "custom", "sample": key,
                               "text": text, "ipa": ipa, "input": input_data})
    return result


def metrics(path):
    """Decode to PCM; these technical checks do not judge pronunciation."""
    result = subprocess.run(["ffmpeg", "-v", "error", "-i", str(path), "-f", "s16le",
                             "-ac", "1", "-ar", "16000", "-"], capture_output=True, timeout=30)
    if result.returncode or not result.stdout:
        return {"decodeOk": False}
    pcm = struct.unpack("<" + "h" * (len(result.stdout) // 2), result.stdout)
    rms = math.sqrt(sum(value * value for value in pcm) / len(pcm))
    return {"decodeOk": True, "durationSeconds": round(len(pcm) / 16000, 2),
            "peak": round(max(abs(v) for v in pcm) / 32768, 4),
            "rmsDbfs": round(20 * math.log10(max(rms, 1) / 32768), 1),
            "nearFullScaleFraction": round(sum(abs(v) >= 32760 for v in pcm) / len(pcm), 6),
            "sha256": hashlib.sha256(path.read_bytes()).hexdigest()}


def save(output, data):
    (output / "manifest.json").write_text(json.dumps(data, ensure_ascii=False, indent=2) + "\n")
    template = (ROOT / "scripts/tts-audition.html").read_text()
    embedded = json.dumps(data, ensure_ascii=False).replace("<", "\\u003c")
    (output / "index.html").write_text(template.replace("/*MANIFEST*/null", embedded))


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("mode", choices=["demos", "custom", "render", "plan"])
    parser.add_argument("--output", type=Path, required=True)
    parser.add_argument("--env-file", type=Path, default=ROOT / ".env")
    parser.add_argument("--max-characters", type=int, default=12000)
    args = parser.parse_args()
    output = args.output.resolve()
    output.mkdir(parents=True, exist_ok=True)
    (output / "audio").mkdir(exist_ok=True)
    manifest = output / "manifest.json"
    data = json.loads(manifest.read_text()) if manifest.exists() else {
        "title": "Almonium voice audition", "clips": [], "customStatus": "Not generated",
        "source": INVENTORY, "focus": list(FOCUS),
    }
    routes = json.loads((ROOT / "src/main/resources/tts-voices.json").read_text())
    data["unavailableVarieties"] = [r["variety"] for r in routes if not r["enabled"]]
    plan = custom_plan()
    characters = sum(len(json.dumps(row["input"], ensure_ascii=False)) for row in plan)
    data["customPlan"] = {"requests": len(plan), "conservativeInputCharacters": characters,
                          "estimatedUpperUsdBeforeFreeTier": round(characters * 0.00003, 2)}
    (output / "custom-plan.json").write_text(json.dumps(plan, ensure_ascii=False, indent=2) + "\n")
    existing = {row["id"]: row for row in data["clips"]}
    if args.mode == "demos":
        table = VoiceTable()
        with urlopen(INVENTORY, timeout=30) as response:
            table.feed(response.read().decode())
        rows = [{"id": "demo-" + route["variety"], "locale": route["variety"],
                 "voiceId": route["voiceId"], "section": "demo", "sample": "Published demo",
                 "text": "Google’s published demonstration; not our word/IPA test.", "ipa": None}
                for route in routes if route["enabled"]]
        for locale in FOCUS:
            for name in CANDIDATES[1:]:
                rows.append({"id": f"demo-{locale}-{name}", "locale": locale,
                             "voiceId": f"{locale}-Chirp3-HD-{name}", "section": "demo",
                             "sample": "Published demo", "text": "Google’s published demonstration; not our word/IPA test.", "ipa": None})
        for row in rows:
            filename = f"audio/{row['id']}.wav"
            path = output / filename
            source = table.rows.get(row["voiceId"])
            row["sourceUrl"] = source
            if not source:
                row["error"] = "No published demo link in Google's voice table"
            else:
                try:
                    if not path.exists():
                        with urlopen(source, timeout=30) as response:
                            path.write_bytes(response.read())
                    row.update(file=filename, metrics=metrics(path))
                except Exception as error:
                    row["error"] = "Demo download/check failed: " + type(error).__name__
            existing[row["id"]] = row
            data["clips"] = list(existing.values())
            save(output, data)
            print(row["id"], "ready" if row.get("file") else "unavailable", flush=True)
    elif args.mode == "custom":
        if characters > args.max_characters:
            raise RuntimeError("Batch exceeds character limit; review custom-plan.json")
        client = session(args.env_file)
        response = client.get("https://texttospeech.googleapis.com/v1/voices", timeout=30)
        if not response.ok:
            data["customStatus"] = provider_error(response)
            save(output, data)
            print(data["customStatus"])
            return 2
        available = {v["name"] for v in response.json()["voices"]}
        data["customStatus"] = "Generating"
        for row in plan:
            filename = f"audio/{row['id']}.mp3"
            path = output / filename
            old = existing.get(row["id"], {})
            if row["voiceId"] not in available:
                row["error"] = "Voice not returned by live ListVoices"
            else:
                # Reuse only identical requests; no automatic retries of billable calls.
                if not (path.exists() and old.get("input") == row["input"]):
                    response = client.post("https://texttospeech.googleapis.com/v1/text:synthesize", json={
                        "input": row["input"],
                        "voice": {"languageCode": row["locale"], "name": row["voiceId"]},
                        "audioConfig": {"audioEncoding": "MP3"},
                    }, timeout=60)
                    if response.ok:
                        path.write_bytes(base64.b64decode(response.json()["audioContent"]))
                    else:
                        row["error"] = provider_error(response)
                if not row.get("error"):
                    row.update(file=filename, metrics=metrics(path))
            existing[row["id"]] = row
            data["clips"] = list(existing.values())
            save(output, data)
            print(row["id"], "ready" if row.get("file") else row["error"], flush=True)
            if row.get("error", "").startswith(("HTTP 401", "HTTP 403", "HTTP 429", "HTTP 5")):
                data["customStatus"] = "Stopped: " + row["error"]
                save(output, data)
                return 2
            time.sleep(0.2)
        failures = sum(bool(row.get("error")) for row in data["clips"] if row["section"] == "custom")
        data["customStatus"] = f"Finished; {failures} failed requests. API acceptance is not pronunciation validation."
    save(output, data)
    print("Review:", output / "index.html")
    print("Planned custom batch:", data["customPlan"])
    return 0


if __name__ == "__main__":
    try:
        sys.exit(main())
    except Exception as error:
        # Never print credential objects, HTTP headers, provider response bodies or traceback locals.
        print("Audition runner stopped:", type(error).__name__, file=sys.stderr)
        sys.exit(1)
