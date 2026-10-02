"""Listening sheet: does each enabled Chirp 3 HD voice obey IPA?

Every variety gets the same made-up word. The text sent is always TEXT; the IPA carries
the stress on each syllable in turn, and a probe spells a different word, which proves
whether the sounds come from the IPA or from the text. Symbols are chosen per locale and
the script falls back through simpler sets until Google accepts the request, because
Chirp answers an unknown symbol with HTTP 400.

Usage: chirp_sheet.py <output folder>   (run from the backend repository root)
"""
import sys, base64, html, json, time
from pathlib import Path

sys.path.insert(0, "scripts")
from tts_audition import session, ROOT

out = Path(sys.argv[1])
out.mkdir(parents=True, exist_ok=True)
c = session(ROOT / ".env")

routes, seen = [], set()
for r in json.loads((ROOT / "src/main/resources/tts-voices.json").read_text()):
    if r["enabled"] and "Chirp3-HD" in (r.get("voiceId") or "") and r["voiceId"] not in seen:
        seen.add(r["voiceId"])
        routes.append(r)

GENERIC = dict(p="p", g="g", t="t", n="n", s="s", m="m", k="k", l="l", i="i", u="u", a="a", o="o", e="e")


def M(**kw):
    d = dict(GENERIC)
    d.update(kw)
    return d


# Symbol choices per locale, from Google's published phoneme tables where one exists.
TAILORED = {
    "en-US": [M(g="ɡ", i="iː", u="uː", a="æ", o="oʊ", e="ɛ")],
    "en-GB": [M(g="ɡ", i="iː", u="uː", a="æ", o="əʊ", e="ɛ")],
    "en-AU": [M(g="ɡ", i="iː", u="uː", a="æ", o="əʊ", e="ɛ")],
    "en-IN": [M(i="iː", u="uː", a="æ", o="oː", e="ɛ", t="ʈ"), M(i="iː", u="uː", a="ɑ", o="oː", t="ʈ")],
    "de-DE": [M(i="iː", e="ɛ")],
    "nl-NL": [M(a="ɑ", o="oː", e="ɛ")],
    "nl-BE": [M(a="ɑ", o="oː", e="ɛ")],
    "it-IT": [M(g="ɡ")],
    "pl-PL": [M(t="t̪", n="n̪", s="s̪", o="ɔ", e="ɛ")],
    "ko-KR": [M(g="k", s="sʰ", o="ʌ", e="ɛ")],
    "hi-IN": [M(i="iː", u="uː", a="aː", o="oː", e="eː", t="t̪"), M(i="ɪ", u="ʊ", a="ə", o="oː", e="eː", t="t̪")],
}
for loc in ("kn-IN", "ml-IN", "mr-IN", "te-IN", "ur-IN"):
    TAILORED[loc] = TAILORED["hi-IN"]
FALLBACK = [M(), M(g="ɡ"), M(p="b", g="k", o="u", e="i"), M(p="b", g="k")]

WORD = [("p", "i"), ("g", "u"), ("t", "a", "n")]
PROBE = [("s", "o"), ("m", "a"), ("k", "e", "l")]
WORD_RESPELL = ["pee", "goo", "tan"]
PROBE_RESPELL = ["soh", "mah", "kel"]
TEXT = "pigutan"


def build(seq, m, k, stress):
    return "".join(("ˈ" if stress and i == k else "") + "".join(m[x] for x in syl) for i, syl in enumerate(seq))


def respell(parts, k, stress):
    return "-".join(s.upper() if stress and i == k else s for i, s in enumerate(parts))


def call(inp, voice):
    for i in range(4):
        r = c.post("https://texttospeech.googleapis.com/v1/text:synthesize",
                   json={"input": inp, "voice": voice, "audioConfig": {"audioEncoding": "MP3"}}, timeout=90)
        if r.status_code in (429, 503):
            time.sleep(10 * (i + 1))
            continue
        return r
    return r


def with_ipa(p):
    return {"text": TEXT, "customPronunciations": {"pronunciations": [
        {"phrase": TEXT, "phoneticEncoding": "PHONETIC_ENCODING_IPA", "pronunciation": p}]}}


results = []
for r in routes:
    loc = r["languageCode"]
    voice = {"languageCode": loc, "name": r["voiceId"]}
    row = {"variety": r["variety"], "locale": loc, "voice": r["voiceId"], "clips": [], "note": ""}
    done, last = False, ""
    for stress in (True, False):  # a language with no stress mark in its list rejects the mark
        for m in TAILORED.get(loc, []) + FALLBACK:
            syllables = (0, 1, 2) if stress else (0,)
            items = [(f"{loc}-s{k+1}.mp3", build(WORD, m, k, stress), respell(WORD_RESPELL, k, stress)) for k in syllables]
            items.append((f"{loc}-probe.mp3", build(PROBE, m, 1, stress), respell(PROBE_RESPELL, 1, stress)))
            got = []
            for name, ipa, label in items:
                resp = call(with_ipa(ipa), voice)
                if not resp.ok:
                    last = resp.json().get("error", {}).get("message", "")[:90]
                    got = None
                    break
                got.append((name, ipa, label, base64.b64decode(resp.json()["audioContent"])))
            if got:
                for name, ipa, label, audio in got:
                    (out / name).write_bytes(audio)
                row["clips"] = [(name, ipa, label) for name, ipa, label, _ in got]
                raw = call({"text": TEXT}, voice)
                if raw.ok:
                    (out / f"{loc}-raw.mp3").write_bytes(base64.b64decode(raw.json()["audioContent"]))
                    row["clips"].append((f"{loc}-raw.mp3", None, "no IPA, plain text"))
                row["note"] = "" if stress else "no stress mark in this language's sound list; sounds only"
                done = True
                break
            if "not supported for locale" in last or "must use PHONETIC" in last:
                break
        if done or "not supported for locale" in last or "must use PHONETIC" in last:
            break
    if not done:
        row["note"] = "REFUSED: " + last
    results.append(row)
    print(f"{row['variety']:7} {loc:7} {'ok ' + str(len(row['clips'])) + ' clips ' + row['note'] if done else row['note']}", flush=True)

json.dump(results, open(out / "results.json", "w"), ensure_ascii=False, indent=1)

ok = [r for r in results if r["clips"]]
bad = [r for r in results if not r["clips"]]
page = """<!doctype html><meta charset=utf-8><title>Chirp IPA obedience</title><style>body{font:16px system-ui;max-width:1040px;margin:30px auto;padding:0 16px}table{border-collapse:collapse;width:100%}td,th{border-bottom:1px solid #ddd;padding:8px 6px;vertical-align:top;text-align:left}audio{width:150px;height:32px}small{color:#666}select{font:inherit}textarea{width:100%;height:180px;font:13px monospace}@media(prefers-color-scheme:dark){body{background:#221e23;color:#f5edf2}td,th{border-color:#444}small{color:#bbb}}</style>
<h1>Chirp 3 HD: does it obey IPA, language by language</h1>
<p>The text sent is always <b>pigutan</b>; the IPA decides what you should hear. Capitals mark the syllable that should be stressed.</p>
<p><b>Pass</b> means the stress clips put the stress where the label says, and the probe says <b>soh-MAH-kel</b>, a different word, which proves the sounds come from the IPA and not from the text.</p>
<table><tr><th>Language</th><th>Stress 1st</th><th>Stress 2nd</th><th>Stress 3rd</th><th>Probe</th><th>No IPA</th><th>Verdict</th></tr>"""


def cell(clips, suffix):
    for name, ipa, label in clips:
        if name.endswith(suffix):
            shown = html.escape("/" + ipa + "/") if ipa else ""
            return f"<b>{html.escape(label)}</b><br><small>{shown}</small><br><audio controls preload=none src='{name}'></audio>"
    return "<small>none</small>"


for r in ok:
    page += f"<tr><td><b>{r['variety']}</b><br><small>{r['voice']}<br>{html.escape(r['note'])}</small></td>"
    page += "".join(f"<td>{cell(r['clips'], s)}</td>" for s in ("-s1.mp3", "-s2.mp3", "-s3.mp3", "-probe.mp3", "-raw.mp3"))
    page += f"<td><select data-v='{r['variety']}'><option value=''>not judged</option><option>pass</option><option>stress wrong</option><option>sounds wrong</option><option>both wrong</option><option>unsure</option></select></td></tr>"
page += "</table><p><button onclick=\"const t=[...document.querySelectorAll('select')].filter(s=>s.value).map(s=>s.dataset.v+': '+s.value).join('\\n');document.getElementById('o').value=t;navigator.clipboard&&navigator.clipboard.writeText(t)\">Copy results</button></p><textarea id=o placeholder='Results appear here'></textarea>"
page += "<h2>Not testable on Chirp</h2><ul>" + "".join(f"<li><b>{r['variety']}</b> {html.escape(r['note'].replace('REFUSED: ', ''))}</li>" for r in bad) + "</ul>"
(out / "index.html").write_text(page)
print(f"\n{len(ok)} languages with clips, {len(bad)} not testable; {out / 'index.html'}")
