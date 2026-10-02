"""Older Google voices (WaveNet / Neural2 / Standard) with the SSML phoneme tag, for varieties Chirp cannot steer."""
import sys, base64, html, json, time
from pathlib import Path

sys.path.insert(0, "scripts")
from tts_audition import session, ROOT

out = Path(sys.argv[1])
out.mkdir(parents=True, exist_ok=True)
c = session(ROOT / ".env")
# Run chirp_sheet.py first: this sheet covers the varieties Chirp refused, read from its results.
chirp = json.load(open(out.parent / "chirp-all" / "results.json"))
routes = json.loads((ROOT / "src/main/resources/tts-voices.json").read_text())

targets = []  # (variety, locale, configured older voice or None)
for r in chirp:
    if "not supported for locale" in r["note"] and r["locale"] != "uk-UA":
        targets.append((r["variety"], r["locale"], None))
for r in routes:
    if r["enabled"] and "Chirp3-HD" not in r["voiceId"]:
        targets.append((r["variety"], r["languageCode"], r["voiceId"]))

voices = c.get("https://texttospeech.googleapis.com/v1/voices", timeout=60).json()["voices"]
FAMILY = ["Wavenet", "Neural2", "Standard"]


def pick(locale, configured):
    cands = [v for v in voices if locale.lower() in [x.lower() for x in v["languageCodes"]]
             and any(f.lower() in v["name"].lower() for f in FAMILY)]
    if configured:
        for v in cands:
            if v["name"].lower() == configured.lower():
                return v
    cands.sort(key=lambda v: (v["ssmlGender"] != "MALE",
                              next(i for i, f in enumerate(FAMILY) if f.lower() in v["name"].lower()), v["name"]))
    return cands[0] if cands else None


def call(body):
    for i in range(4):
        r = c.post("https://texttospeech.googleapis.com/v1/text:synthesize", json=body, timeout=90)
        if r.status_code in (429, 503):
            time.sleep(10 * (i + 1))
            continue
        return r
    return r


def hear(path):
    body = {"contents": [{"role": "user", "parts": [
        {"inlineData": {"mimeType": "audio/mpeg", "data": base64.b64encode(path.read_bytes()).decode()}},
        {"text": "One made-up word is spoken. Write what you hear in Latin letters, lowercase. Reply JSON: {\"word\": \"...\"}"}]}],
        "generationConfig": {"temperature": 0, "responseMimeType": "application/json"}}
    for i in range(5):
        r = c.post("https://aiplatform.googleapis.com/v1/projects/almonium-dev/locations/global/publishers/google/models/gemini-2.5-flash:generateContent", json=body, timeout=120)
        if r.status_code == 429:
            time.sleep(15 * (i + 1))
            continue
        break
    try:
        return json.loads(r.json()["candidates"][0]["content"]["parts"][0]["text"]).get("word", "?")
    except Exception:
        return "?"


TEXT = "pigutan"
WORD = ["pi", "gu", "tan"]
PROBE = ["so", "ma", "kel"]
WR = ["pee", "goo", "tan"]
PR = ["soh", "mah", "kel"]
ipa = lambda syl, k: "".join(("ˈ" if i == k else "") + s for i, s in enumerate(syl))
resp = lambda rs, k: "-".join(s.upper() if i == k else s for i, s in enumerate(rs))
ssml = lambda p: {"ssml": f'<speak><phoneme alphabet="ipa" ph="{p}">{TEXT}</phoneme></speak>'}

rows = []
for variety, locale, configured in targets:
    v = pick(locale, configured)
    row = {"variety": variety, "locale": locale, "voice": v["name"] if v else None,
           "gender": v["ssmlGender"] if v else None, "clips": [], "note": "", "heard": ""}
    if not v:
        row["note"] = "no older Google voice exists for this locale"
        rows.append(row)
        print(f"{variety:7} {locale:8} no older voice", flush=True)
        continue
    voice = {"languageCode": v["languageCodes"][0], "name": v["name"]}
    items = [(f"{locale}-s{k+1}.mp3", ipa(WORD, k), resp(WR, k)) for k in range(3)] + [(f"{locale}-probe.mp3", ipa(PROBE, 1), resp(PR, 1))]
    err = ""
    for n, p, l in items:
        r = call({"input": ssml(p), "voice": voice, "audioConfig": {"audioEncoding": "MP3"}})
        if not r.ok:
            err = r.json().get("error", {}).get("message", "")[:90]
            break
        (out / n).write_bytes(base64.b64decode(r.json()["audioContent"]))
        row["clips"].append((n, p, l))
    if err:
        row["clips"] = []
        row["note"] = "request rejected: " + err
    else:
        r = call({"input": {"text": TEXT}, "voice": voice, "audioConfig": {"audioEncoding": "MP3"}})
        if r.ok:
            (out / f"{locale}-raw.mp3").write_bytes(base64.b64decode(r.json()["audioContent"]))
            row["clips"].append((f"{locale}-raw.mp3", None, "no IPA, plain text"))
        row["heard"] = hear(out / f"{locale}-probe.mp3")
    rows.append(row)
    print(f"{variety:7} {locale:8} {row['voice']:24} {row['gender']:6} probe transcribed as: {row['heard']} {row['note']}", flush=True)

json.dump(rows, open(out / "results.json", "w"), ensure_ascii=False, indent=1)
ok = [r for r in rows if r["clips"]]
bad = [r for r in rows if not r["clips"]]
page = """<!doctype html><meta charset=utf-8><title>Older voices IPA obedience</title><style>body{font:16px system-ui;max-width:1100px;margin:30px auto;padding:0 16px}table{border-collapse:collapse;width:100%}td,th{border-bottom:1px solid #ddd;padding:8px 6px;vertical-align:top;text-align:left}audio{width:150px;height:32px}small{color:#666}select{font:inherit}textarea{width:100%;height:180px;font:13px monospace}@media(prefers-color-scheme:dark){body{background:#221e23;color:#f5edf2}td,th{border-color:#444}small{color:#bbb}}</style>
<h1>Older Google voices: do they obey IPA where Chirp cannot</h1>
<p>Same test as the Chirp sheet. The text sent is always <b>pigutan</b>. The stress clips should be PEE-goo-tan, pee-GOO-tan, pee-goo-TAN. The probe should say <b>soh-MAH-kel</b>.</p>
<p>These voices never reject a request, so a pass can only come from listening. If the probe says pigutan, the voice ignored the IPA. The grey machine guess under each probe is a rough hint, not a verdict.</p>
<table><tr><th>Language</th><th>Stress 1st</th><th>Stress 2nd</th><th>Stress 3rd</th><th>Probe</th><th>No IPA</th><th>Verdict</th></tr>"""


def cell(clips, suffix, extra=""):
    for n, p, l in clips:
        if n.endswith(suffix):
            return f"<b>{html.escape(l)}</b><br><small>{html.escape('/' + p + '/') if p else ''}</small><br><audio controls preload=none src='{n}'></audio>{extra}"
    return "<small>none</small>"


for r in ok:
    hint = f"<br><small>machine guess: {html.escape(str(r['heard']))}</small>"
    page += f"<tr><td><b>{r['variety']}</b><br><small>{r['voice']}<br>{r['gender'].lower()}</small></td>"
    page += "".join(f"<td>{cell(r['clips'], s, hint if s == '-probe.mp3' else '')}</td>" for s in ("-s1.mp3", "-s2.mp3", "-s3.mp3", "-probe.mp3", "-raw.mp3"))
    page += f"<td><select data-v='{r['variety']}'><option value=''>not judged</option><option>pass</option><option>stress wrong</option><option>sounds wrong, IPA ignored</option><option>both wrong</option><option>unsure</option></select></td></tr>"
page += "</table><p><button onclick=\"const t=[...document.querySelectorAll('select')].filter(s=>s.value).map(s=>s.dataset.v+': '+s.value).join('\\n');document.getElementById('o').value=t;navigator.clipboard&&navigator.clipboard.writeText(t)\">Copy results</button></p><textarea id=o placeholder='Results appear here'></textarea>"
page += "<h2>No clips</h2><ul>" + "".join(f"<li><b>{r['variety']}</b> {html.escape(r['note'])}</li>" for r in bad) + "</ul>"
(out / "index.html").write_text(page)
print(f"\n{len(ok)} varieties with clips, {len(bad)} without; {out / 'index.html'}")
