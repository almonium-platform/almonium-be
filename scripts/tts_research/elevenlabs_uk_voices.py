"""ElevenLabs Ukrainian: does the slip into Russian belong to one voice or to the model?

Words Ukrainian shares with Russian, where a Russian reading is audible (г as a stop, unstressed
о as а, и as і), each written with a stress mark and spoken by several Ukrainian library voices,
plus one sentence to judge how natural each voice is.

The first round (Evgeniy Shevchenko, Alex Nekrasov, Leonid Drapei) went to Alex Nekrasov; this
round keeps him as the reference and adds six more, three of them female.

Usage: elevenlabs_uk_voices.py <output folder>   (run from the backend repository root)
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
VOICES = [("Alex Nekrasov", "9Sj8ugvpK1DmcAXyvi3a"), ("Bogdan", "jn6ifzU1eO5tfUZ2ZJVg"), ("Artem Klopotenko", "h9NSQvWZaC4NFusYsxT9"),
          ("Yevhen", "TEyBWD5tAHAWqAGEv6yI"), ("Yaroslava", "0ZQZuw8Sn4cU0rN1Tm2K"), ("Vira", "nCqaTnIbLdME87OuQaZY"),
          ("Mariya Maro", "2OXYbN1uGomXXJtv9Dq6")]
spent = [0]

# (text sent, what a Russian reading would get wrong)
WORDS = [("Скажіть, будь ласка, як пройти до найближчої станції метро?", "ціле речення: наскільки природний голос"),
         ("дорога́", "г як ґ, перше о як а"), ("доро́га", "г як ґ"), ("голова́", "г як ґ, о як а"),
         ("молоко́", "о як а"), ("горо́д", "г як ґ, о як а"), ("паляниця", "и як і")]


def eleven(text, voice_id):
    for attempt in range(4):
        r = requests.post(f"https://api.elevenlabs.io/v1/text-to-speech/{voice_id}?output_format=mp3_44100_128",
                          headers={"xi-api-key": KEY}, json={"text": text, "model_id": MODEL, "language_code": "uk"}, timeout=120)
        if r.status_code == 429:
            time.sleep(8 * (attempt + 1))
            continue
        break
    if not r.ok:
        print("ElevenLabs", r.status_code, r.text[:160], flush=True)
        return None
    spent[0] += len(text)
    return r.content


STYLE = "<style>body{font:16px system-ui;max-width:1300px;margin:30px auto;padding:0 16px}table{border-collapse:collapse;width:100%}td,th{border-bottom:1px solid #ddd;padding:8px 6px;vertical-align:top;text-align:left}audio{width:120px;height:32px}small{color:#666}@media(prefers-color-scheme:dark){body{background:#221e23;color:#f5edf2}td,th{border-color:#444}small{color:#bbb}}</style>"
page = f"<!doctype html><meta charset=utf-8><title>ElevenLabs: Ukrainian voices</title>{STYLE}<h1>Ukrainian or Russian?</h1><p>Each word was sent as the spelling shown, with its stress mark, to several Ukrainian voices. The note says what a Russian reading would sound like.</p><table><tr><th>Word</th>"
page += "".join(f"<th>{html.escape(name)}</th>" for name, _ in VOICES) + "</tr>"
for n, (text, note) in enumerate(WORDS):
    page += f"<tr><td><b>{html.escape(text)}</b><br><small>російською було б: {html.escape(note)}</small></td>"
    for v, (name, voice_id) in enumerate(VOICES):
        audio = eleven(text, voice_id)
        if audio:
            (out / f"ukv-{n}-{v}.mp3").write_bytes(audio)
            page += f"<td><audio controls preload=none src='ukv-{n}-{v}.mp3'></audio></td>"
        else:
            page += "<td><small>none</small></td>"
    page += "</tr>"
    print(text, flush=True)
page += "</table>"
(out / "uk-voices.html").write_text(page)
print(f"\ncharacters sent: {spent[0]}; {out / 'uk-voices.html'}")
