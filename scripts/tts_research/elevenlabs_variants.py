"""ElevenLabs with native voices: the same word sent several ways, side by side.

Follows the 2026-10-02 listen, where Ukrainian from IPA sometimes sounded Russian (the vowel
written /ɪ/ came out as Russian "и") and Italian `ancora` kept its stress on CO. Each row is one
word; each column is one way of writing it: bare spelling, spelling with a stress mark, or IPA
with a different symbol for the doubtful sound.

Usage: elevenlabs_variants.py <output folder> [language codes]   (run from the backend repository root)
"""
import sys, html, time
from pathlib import Path

import requests

sys.path.insert(0, "scripts")
from tts_audition import ROOT

out = Path(sys.argv[1])
out.mkdir(parents=True, exist_ok=True)
KEY = next(l.split("=", 1)[1].strip() for l in (ROOT / ".env").read_text().splitlines() if l.startswith("ELEVENLABS_API_KEY="))
MODEL = "eleven_v4"
VOICES = {"uk": ("Alex Nekrasov", "9Sj8ugvpK1DmcAXyvi3a"), "it": ("MarcoTrox", "W71zT1VwIFFx3mMGH2uZ")}
spent = [0]

# (language, word and what to listen for, [(how it was written, text sent)])
ROWS = [
    ("uk", "риба: український и", [("bare", "риба"), ("stress mark", "ри́ба"), ("IPA ɪ", "/ˈrɪ.ba/"), ("IPA ɨ", "/ˈrɨ.ba/"), ("IPA e", "/ˈre.ba/")]),
    ("uk", "син: український и", [("bare", "син"), ("IPA ɪ", "/ˈsɪn/"), ("IPA ɨ", "/ˈsɨn/")]),
    ("uk", "мити: и та и", [("bare", "мити"), ("stress mark", "ми́ти"), ("IPA ɪ", "/ˈmɪ.tɪ/"), ("IPA ɨ", "/ˈmɨ.tɨ/")]),
    ("uk", "криниця: криНИця", [("bare", "криниця"), ("stress mark", "крини́ця"), ("IPA ɪ", "/krɪ.ˈnɪ.tsʲa/"), ("IPA ɨ", "/krɨ.ˈnɨ.tsʲa/")]),
    ("uk", "паляниця: паляНИця", [("bare", "паляниця"), ("stress mark", "паляни́ця"), ("IPA ɪ", "/pa.lʲa.ˈnɪ.tsʲa/"), ("IPA ɨ", "/pa.lʲa.ˈnɨ.tsʲa/")]),
    ("uk", "книга: и під наголосом", [("bare", "книга"), ("stress mark", "кни́га"), ("IPA ɪ", "/ˈknɪ.ɦa/"), ("IPA ɨ", "/ˈknɨ.ɦa/")]),
    ("uk", "Укрзалізниця", [("bare", "Укрзалізниця"), ("stress mark", "Укрзалізни́ця"), ("IPA ɪ", "/ukr.za.lʲiz.ˈnɪ.tsʲa/")]),
    ("uk", "замок, фортеця: ЗАмок", [("bare", "замок"), ("stress mark", "за́мок"), ("IPA", "/ˈza.mɔk/")]),
    ("uk", "замок, дверний: заМОК", [("stress mark", "замо́к"), ("IPA", "/za.ˈmɔːk/")]),
    ("uk", "мука, страждання: МУка", [("bare", "мука"), ("stress mark", "му́ка"), ("IPA", "/ˈmu.ka/")]),
    ("uk", "мука, борошно: муКА", [("stress mark", "мука́"), ("IPA", "/mu.ˈkaː/")]),
    ("uk", "дорога, шлях: доРОга", [("bare", "дорога"), ("stress mark", "доро́га")]),
    ("uk", "дорога, цінна: дороГА", [("stress mark", "дорога́")]),
    ("it", "ancora, anchor: ANcora", [("bare", "ancora"), ("accent in spelling", "àncora"), ("IPA as before", "/ˈan.ko.ra/"), ("IPA long a", "/ˈaːn.ko.ra/"), ("IPA ŋ", "/ˈaŋ.ko.ra/")]),
    ("it", "ancora, still: anCOra", [("accent in spelling", "ancóra"), ("IPA", "/an.ˈkoː.ra/")]),
    ("it", "principi, princes: PRINcipi", [("accent in spelling", "prìncipi"), ("IPA", "/ˈprin.tʃi.pi/")]),
    ("it", "principi, principles: prinCIpi", [("accent in spelling", "princìpi"), ("IPA", "/prin.ˈtʃi.pi/")]),
]


def eleven(text, language):
    for attempt in range(4):
        r = requests.post(f"https://api.elevenlabs.io/v1/text-to-speech/{VOICES[language][1]}?output_format=mp3_44100_128",
                          headers={"xi-api-key": KEY}, json={"text": text, "model_id": MODEL, "language_code": language}, timeout=120)
        if r.status_code == 429:
            time.sleep(8 * (attempt + 1))
            continue
        break
    if not r.ok:
        print("ElevenLabs", r.status_code, r.text[:160], flush=True)
        return None
    spent[0] += len(text)
    return r.content


STYLE = "<style>body{font:16px system-ui;max-width:1100px;margin:30px auto;padding:0 16px}table{border-collapse:collapse;width:100%}td,th{border-bottom:1px solid #ddd;padding:8px 6px;vertical-align:top;text-align:left}audio{width:150px;height:32px}small{color:#666}@media(prefers-color-scheme:dark){body{background:#221e23;color:#f5edf2}td,th{border-color:#444}small{color:#bbb}}</style>"
page = f"<!doctype html><meta charset=utf-8><title>ElevenLabs: ways of writing a word</title>{STYLE}<h1>One word, written several ways</h1><p>Each row is one word spoken by a native voice. Each clip was sent the text shown above it: the bare spelling, the spelling with a stress mark, or IPA.</p><table>"
for n, (code, label, variants) in enumerate(ROWS):
    if sys.argv[2:] and code not in sys.argv[2:]:
        continue
    cells = ""
    for k, (how, text) in enumerate(variants):
        audio = eleven(text, code)
        if not audio:
            continue
        f = f"{code}-{n}-{k}.mp3"
        (out / f).write_bytes(audio)
        cells += f"<td><b>{html.escape(how)}</b><br><small>{html.escape(text)}</small><br><audio controls preload=none src='{f}'></audio></td>"
    page += f"<tr><td><b>{html.escape(label)}</b><br><small>{code}, {VOICES[code][0]}</small></td>{cells}</tr>"
    print(code, label, len(variants), flush=True)
page += "</table>"
(out / "variants.html").write_text(page)
print(f"\ncharacters sent: {spent[0]}; {out / 'variants.html'}")
