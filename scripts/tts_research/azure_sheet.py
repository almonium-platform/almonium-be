"""Listening sheet: does Azure neural TTS obey IPA, for every variety the app enables?

Same test as chirp_sheet.py: the text is always TEXT, the IPA carries the stress on each
syllable in turn, and a probe spells a different word. Azure documents that an unknown
phone returns HTTP 400 and that a stress mark needs every syllable marked, so syllables
are separated with dots.

Needs AZURE_SPEECH_KEY and AZURE_SPEECH_REGION in .env. The free tier allows about 20
requests a minute, so the run is slow on purpose.

Usage: azure_sheet.py <output folder>   (run from the backend repository root)
"""
import sys, hashlib, html, json, time
from pathlib import Path

import requests

ROOT = Path(__file__).resolve().parents[2]
out = Path(sys.argv[1])
out.mkdir(parents=True, exist_ok=True)
env = dict(line.split("=", 1) for line in (ROOT / ".env").read_text().splitlines() if line.startswith("AZURE_SPEECH_"))
KEY, REGION = env["AZURE_SPEECH_KEY"].strip(), env["AZURE_SPEECH_REGION"].strip()
BASE = f"https://{REGION}.tts.speech.microsoft.com/cognitiveservices"
PAUSE = 3.2

routes = json.loads((ROOT / "src/main/resources/tts-voices.json").read_text())
ALIAS = {"cmn-CN": "zh-CN", "cmn-TW": "zh-TW", "ar-XA": "ar-EG", "fil-ph": "fil-PH"}
voices = requests.get(f"{BASE}/voices/list", headers={"Ocp-Apim-Subscription-Key": KEY}, timeout=30).json()


def pick(locale):
    cands = [v for v in voices if v["Locale"] == locale and v["ShortName"].endswith("Neural")
             and "Multilingual" not in v["ShortName"] and ":" not in v["ShortName"]]
    cands.sort(key=lambda v: (v["Gender"] != "Male", v["ShortName"]))
    return cands[0] if cands else None


GENERIC = dict(p="p", g="g", t="t", n="n", s="s", m="m", k="k", l="l", i="i", u="u", a="a", o="o", e="e")


def M(**kw):
    d = dict(GENERIC)
    d.update(kw)
    return d


TAILORED = {
    "en-US": [M(g="ɡ", i="i", u="u", a="æ", o="oʊ", e="ɛ"), M(g="ɡ", i="iː", u="uː", a="æ", o="oʊ", e="ɛ")],
    "en-GB": [M(g="ɡ", i="iː", u="uː", a="æ", o="əʊ", e="ɛ")],
    "en-AU": [M(g="ɡ", i="iː", u="uː", a="æ", o="əʊ", e="ɛ")],
    "en-IN": [M(g="ɡ", i="iː", u="uː", a="æ", o="əʊ", e="ɛ")],
    "de-DE": [M(i="iː", u="uː", e="ɛ", o="ɔ"), M(i="iː", e="ɛ")],
    "nl-NL": [M(a="ɑ", o="oː", e="ɛ")],
    "nl-BE": [M(a="ɑ", o="oː", e="ɛ")],
    "pl-PL": [M(o="ɔ", e="ɛ")],
    "fi-FI": [M(a="ɑ")],
    "cs-CZ": [M(o="o", e="ɛ"), M(o="ɔ", e="ɛ")],
    "uk-UA": [M(o="ɔ", e="ɛ")],
    "ru-RU": [M()],
    "sv-SE": [M(o="ɔ", e="ɛ")],
    "nb-NO": [M(a="ɑ", o="ɔ", e="ɛ")],
    "da-DK": [M()],
    "el-GR": [M()],
    "hu-HU": [M(a="ɒ", e="ɛ")],
}
FALLBACK = [M(), M(g="ɡ"), M(o="ɔ", e="ɛ"), M(g="ɡ", o="ɔ", e="ɛ"), M(p="b", g="k", o="u", e="i")]
WORD = [("p", "i"), ("g", "u"), ("t", "a", "n")]
PROBE = [("s", "o"), ("m", "a"), ("k", "e", "l")]
WORD_RESPELL = ["pee", "goo", "tan"]
PROBE_RESPELL = ["soh", "mah", "kel"]
TEXT = "pigutan"


def build(seq, m, k, stress):
    parts = [("ˈ" if stress and i == k else "") + "".join(m[x] for x in syl) for i, syl in enumerate(seq)]
    return (".".join(parts)) if stress else "".join(parts)


def respell(parts, k, stress):
    return "-".join(s.upper() if stress and i == k else s for i, s in enumerate(parts))


last_call = [0.0]


def synth(locale, voice, inner):
    ssml = (f'<speak version="1.0" xmlns="http://www.w3.org/2001/10/synthesis" xml:lang="{locale}">'
            f'<voice name="{voice}">{inner}</voice></speak>')
    for attempt in range(5):
        wait = PAUSE - (time.time() - last_call[0])
        if wait > 0:
            time.sleep(wait)
        last_call[0] = time.time()
        r = requests.post(f"{BASE}/v1", data=ssml.encode("utf-8"), timeout=60, headers={
            "Ocp-Apim-Subscription-Key": KEY, "Content-Type": "application/ssml+xml",
            "X-Microsoft-OutputFormat": "audio-24khz-48kbitrate-mono-mp3", "User-Agent": "almonium-tts-research"})
        if r.status_code == 429:
            time.sleep(25)
            continue
        return r
    return r


def with_ipa(p):
    return f'<phoneme alphabet="ipa" ph="{html.escape(p, quote=True)}">{TEXT}</phoneme>'


seen, targets = set(), []
for r in routes:
    if not r["enabled"]:
        continue
    loc = ALIAS.get(r["languageCode"], r["languageCode"])
    if loc in seen:
        continue
    seen.add(loc)
    targets.append((r["variety"], loc))

results = []
for variety, loc in targets:
    v = pick(loc)
    row = {"variety": variety, "locale": loc, "voice": v["ShortName"] if v else None,
           "gender": v["Gender"] if v else None, "clips": [], "note": "", "bytes": ""}
    if not v:
        row["note"] = "no Azure neural voice for this locale"
        results.append(row)
        print(f"{variety:7} {loc:7} no voice", flush=True)
        continue
    done, last = False, ""
    for stress in (True, False):
        for m in TAILORED.get(loc, []) + FALLBACK:
            syllables = (0, 1, 2) if stress else (0,)
            items = [(f"{loc}-s{k+1}.mp3", build(WORD, m, k, stress), respell(WORD_RESPELL, k, stress)) for k in syllables]
            items.append((f"{loc}-probe.mp3", build(PROBE, m, 1, stress), respell(PROBE_RESPELL, 1, stress)))
            got = []
            for name, ipa, label in items:
                resp = synth(loc, v["ShortName"], with_ipa(ipa))
                if not resp.ok or not resp.content:
                    last = f"HTTP {resp.status_code} {resp.reason} [{ipa}]"
                    got = None
                    break
                got.append((name, ipa, label, resp.content))
            if got:
                hashes = {}
                for name, ipa, label, audio in got:
                    (out / name).write_bytes(audio)
                    hashes[name] = hashlib.sha256(audio).hexdigest()[:10]
                row["clips"] = [(name, ipa, label) for name, ipa, label, _ in got]
                raw = synth(loc, v["ShortName"], TEXT)
                if raw.ok:
                    (out / f"{loc}-raw.mp3").write_bytes(raw.content)
                    row["clips"].append((f"{loc}-raw.mp3", None, "no IPA, plain text"))
                    raw_hash = hashlib.sha256(raw.content).hexdigest()[:10]
                    probe_hash = hashes[f"{loc}-probe.mp3"]
                    stress_hashes = [hashes[n] for n in hashes if "-s" in n]
                    notes = ["probe differs from plain text" if probe_hash != raw_hash else "PROBE IDENTICAL TO PLAIN TEXT"]
                    if len(stress_hashes) == 3:
                        notes.append("3 stress clips all differ" if len(set(stress_hashes)) == 3 else
                                     "stress clips identical" if len(set(stress_hashes)) == 1 else "2 of 3 stress clips identical")
                    row["bytes"] = "; ".join(notes)
                row["note"] = "" if stress else "stress mark rejected; sounds only"
                done = True
                break
        if done:
            break
    if not done:
        row["note"] = "REFUSED: " + last
    results.append(row)
    print(f"{variety:7} {loc:7} {row['voice']:28} {'ok ' + str(len(row['clips'])) + ' clips | ' + row['bytes'] + ' ' + row['note'] if done else row['note']}", flush=True)
    json.dump(results, open(out / "results.json", "w"), ensure_ascii=False, indent=1)

ok = [r for r in results if r["clips"]]
bad = [r for r in results if not r["clips"]]
page = """<!doctype html><meta charset=utf-8><title>Azure IPA obedience</title><style>body{font:16px system-ui;max-width:1100px;margin:30px auto;padding:0 16px}table{border-collapse:collapse;width:100%}td,th{border-bottom:1px solid #ddd;padding:8px 6px;vertical-align:top;text-align:left}audio{width:150px;height:32px}small{color:#666}select{font:inherit}textarea{width:100%;height:180px;font:13px monospace}@media(prefers-color-scheme:dark){body{background:#221e23;color:#f5edf2}td,th{border-color:#444}small{color:#bbb}}</style>
<h1>Azure neural voices: do they obey IPA, language by language</h1>
<p>Same test as the Google sheets. The text sent is always <b>pigutan</b>. The stress clips should be PEE-goo-tan, pee-GOO-tan, pee-goo-TAN. The probe should say <b>soh-MAH-kel</b>.</p>
<table><tr><th>Language</th><th>Stress 1st</th><th>Stress 2nd</th><th>Stress 3rd</th><th>Probe</th><th>No IPA</th><th>Bytes say</th><th>Verdict</th></tr>"""


def cell(clips, suffix):
    for name, ipa, label in clips:
        if name.endswith(suffix):
            shown = html.escape("/" + ipa + "/") if ipa else ""
            return f"<b>{html.escape(label)}</b><br><small>{shown}</small><br><audio controls preload=none src='{name}'></audio>"
    return "<small>none</small>"


for r in ok:
    page += f"<tr><td><b>{r['variety']}</b><br><small>{r['voice']}<br>{r['gender'].lower()}<br>{html.escape(r['note'])}</small></td>"
    page += "".join(f"<td>{cell(r['clips'], s)}</td>" for s in ("-s1.mp3", "-s2.mp3", "-s3.mp3", "-probe.mp3", "-raw.mp3"))
    page += f"<td><small>{html.escape(r['bytes'])}</small></td>"
    page += f"<td><select data-v='{r['variety']}'><option value=''>not judged</option><option>sounds and stress</option><option>sounds only</option><option>says pigutan</option><option>unsure</option></select></td></tr>"
page += "</table><p><button onclick=\"const t=[...document.querySelectorAll('select')].filter(s=>s.value).map(s=>s.dataset.v+': '+s.value).join('\\n');document.getElementById('o').value=t;navigator.clipboard&&navigator.clipboard.writeText(t)\">Copy results</button></p><textarea id=o placeholder='Results appear here'></textarea>"
page += "<h2>No clips</h2><ul>" + "".join(f"<li><b>{r['variety']}</b> ({r['locale']}) {html.escape(r['note'])}</li>" for r in bad) + "</ul>"
(out / "index.html").write_text(page)
print(f"\n{len(ok)} varieties with clips, {len(bad)} without; {out / 'index.html'}")
