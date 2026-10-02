"""IPA probe on Azure for languages that have no Google voice in the app's catalogue.

Reads a JSON list of {code, name, locales, voice} and, for each, sends the same made-up word
with the stress on each syllable plus a probe whose IPA spells a different word. Azure accepts
any request and never repeats itself byte for byte, so the sheet is for listening; a machine
transcription of the probe is added as a rough hint and used only to sort the rows.

Usage: azure_new_languages.py <targets.json> <output folder>   (from the backend root)
"""
import sys, base64, html, json, time
from pathlib import Path

import requests

sys.path.insert(0, "scripts")
from tts_audition import session, ROOT

targets = json.load(open(sys.argv[1]))
out = Path(sys.argv[2])
out.mkdir(parents=True, exist_ok=True)
env = dict(line.split("=", 1) for line in (ROOT / ".env").read_text().splitlines() if line.startswith("AZURE_SPEECH_"))
KEY, REGION = env["AZURE_SPEECH_KEY"].strip(), env["AZURE_SPEECH_REGION"].strip()
URL = f"https://{REGION}.tts.speech.microsoft.com/cognitiveservices/v1"
google = session(ROOT / ".env")
TEXT = "pigutan"
CLIPS = [("s1", "ˈpi.gu.tan", "PEE-goo-tan"), ("s2", "pi.ˈgu.tan", "pee-GOO-tan"), ("s3", "pi.gu.ˈtan", "pee-goo-TAN"),
         ("probe", "so.ˈma.kel", "soh-MAH-kel")]
last = [0.0]


def synth(locale, voice, inner):
    ssml = (f'<speak version="1.0" xmlns="http://www.w3.org/2001/10/synthesis" xml:lang="{locale}">'
            f'<voice name="{voice}">{inner}</voice></speak>')
    for _ in range(5):
        wait = 3.3 - (time.time() - last[0])
        if wait > 0:
            time.sleep(wait)
        last[0] = time.time()
        r = requests.post(URL, data=ssml.encode("utf-8"), timeout=60, headers={
            "Ocp-Apim-Subscription-Key": KEY, "Content-Type": "application/ssml+xml",
            "X-Microsoft-OutputFormat": "audio-24khz-48kbitrate-mono-mp3", "User-Agent": "almonium-tts-research"})
        if r.status_code == 429:
            time.sleep(25)
            continue
        return r.content if r.ok and r.content else None
    return None


def hear(path):
    body = {"contents": [{"role": "user", "parts": [
        {"inlineData": {"mimeType": "audio/mpeg", "data": base64.b64encode(path.read_bytes()).decode()}},
        {"text": "One made-up word is spoken. Write what you hear in Latin letters, lowercase. Reply JSON: {\"word\": \"...\"}"}]}],
        "generationConfig": {"temperature": 0, "responseMimeType": "application/json"}}
    for i in range(5):
        r = google.post("https://aiplatform.googleapis.com/v1/projects/almonium-dev/locations/global/publishers/google/models/gemini-2.5-flash:generateContent", json=body, timeout=120)
        if r.status_code == 429:
            time.sleep(12 * (i + 1))
            continue
        break
    try:
        return json.loads(r.json()["candidates"][0]["content"]["parts"][0]["text"]).get("word", "?")
    except Exception:
        return "?"


rows, done = [], set()
for t in targets:
    locale, voice = t["locales"][0], t["voice"]
    if not voice or voice in done:
        continue
    done.add(voice)
    row = dict(t, locale=locale, clips=[])
    for key, ipa, label in CLIPS:
        audio = synth(locale, voice, f'<phoneme alphabet="ipa" ph="{ipa}">{TEXT}</phoneme>')
        if audio:
            (out / f"{locale}-{key}.mp3").write_bytes(audio)
            row["clips"].append((key, ipa, label))
    raw = synth(locale, voice, TEXT)
    if raw:
        (out / f"{locale}-raw.mp3").write_bytes(raw)
    heard = hear(out / f"{locale}-probe.mp3") if any(k == "probe" for k, _, _ in row["clips"]) else "?"
    g = str(heard).lower()
    row["heard"] = heard
    row["guess"] = ("follows" if ("mak" in g or "mac" in g or (g[:1] in "sz" and "m" in g))
                    else "text" if g[:2] in ("pi", "pe", "bi", "pu", "be") else "unclear")
    rows.append(row)
    print(f"{t['code']:4} {t['name']:26} {voice:26} probe heard: {heard:14} {row['guess']}", flush=True)
    json.dump(rows, open(out / "results.json", "w"), ensure_ascii=False, indent=1)

order = {"follows": 0, "unclear": 1, "text": 2}
rows.sort(key=lambda r: order[r["guess"]])
titles = {"follows": "Machine guess: probe says the IPA word", "unclear": "Machine guess: unclear",
          "text": "Machine guess: probe says pigutan, IPA ignored"}
page = """<!doctype html><meta charset=utf-8><title>Azure: languages with no Google voice</title><style>body{font:16px system-ui;max-width:1100px;margin:30px auto;padding:0 16px}table{border-collapse:collapse;width:100%}td,th{border-bottom:1px solid #ddd;padding:8px 6px;vertical-align:top;text-align:left}audio{width:150px;height:32px}small{color:#666}select{font:inherit}textarea{width:100%;height:180px;font:13px monospace}@media(prefers-color-scheme:dark){body{background:#221e23;color:#f5edf2}td,th{border-color:#444}small{color:#bbb}}</style>
<h1>Azure: languages the app has no voice for yet</h1>
<p>The text sent is always <b>pigutan</b>. The stress clips should be PEE-goo-tan, pee-GOO-tan, pee-goo-TAN. The probe should say <b>soh-MAH-kel</b>. Only listening decides; the grey machine guess is a hint.</p>"""
prev = None
for r in rows:
    if r["guess"] != prev:
        if prev:
            page += "</table>"
        page += f"<h2>{titles[r['guess']]}</h2><table><tr><th>Language</th><th>Stress 1st</th><th>Stress 2nd</th><th>Stress 3rd</th><th>Probe</th><th>No IPA</th><th>Verdict</th></tr>"
        prev = r["guess"]
    cells = ""
    for key in ("s1", "s2", "s3", "probe"):
        clip = next((c for c in r["clips"] if c[0] == key), None)
        hint = f"<br><small>machine heard: {html.escape(str(r['heard']))}</small>" if key == "probe" else ""
        cells += (f"<td><b>{clip[2]}</b><br><small>/{html.escape(clip[1])}/</small><br><audio controls preload=none src='{r['locale']}-{key}.mp3'></audio>{hint}</td>"
                  if clip else "<td><small>none</small></td>")
    page += (f"<tr><td><b>{html.escape(r['name'])}</b><br><small>{r['code']} · {r['voice']}</small></td>{cells}"
             f"<td><audio controls preload=none src='{r['locale']}-raw.mp3'></audio></td>"
             f"<td><select data-v='{r['code']}'><option value=''>not judged</option><option>sounds and stress</option><option>sounds only</option><option>says pigutan</option><option>unsure</option></select></td></tr>")
page += "</table><p><button onclick=\"const t=[...document.querySelectorAll('select')].filter(s=>s.value).map(s=>s.dataset.v+': '+s.value).join('\\n');document.getElementById('o').value=t;navigator.clipboard&&navigator.clipboard.writeText(t)\">Copy results</button></p><textarea id=o placeholder='Results appear here'></textarea>"
(out / "index.html").write_text(page)
print(f"\n{len(rows)} voices; {out / 'index.html'}")
