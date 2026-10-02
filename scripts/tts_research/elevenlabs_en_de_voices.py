"""ElevenLabs voice audition for English and German.

Each voice reads one sentence as plain text and six tricky words from IPA, which is the input mode
chosen for these two languages. English voices are ElevenLabs' own stock voices, so nobody can
withdraw them; German has no stock voice, so those are library voices with a 730-day notice period.

Usage: elevenlabs_en_de_voices.py <output folder>   (run from the backend repository root)
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
spent = [0]

EN_SENTENCE = ("Could you tell me how to get to the nearest train station?", "plain text")
# (group, language code, [(voice, id, note)], [(text sent, what to listen for)])
GROUPS = [
    ("English, American", "en",
     [("Eric", "cjVigY5qzO86Huf0OWal", "male, used so far"), ("Brian", "nPczCjzI2devNBz1zQrb", "male, deep"),
      ("Matilda", "XrExE9yKIg1WjnnlVkGX", "female, educational"), ("Bella", "hpp4J3VqNfWAUOO0d1Us", "female, educational")],
     [EN_SENTENCE, ("/ˈpɹɛ.zənt/", "present, a gift: PREsent"), ("/pɹɪ.ˈzɛnt/", "present, to show: preSENT"),
      ("/ˈtɪɹ/", "tear, from the eye"), ("/ˈwaɪnd/", "wind, to turn"), ("/ˈkwaɪ.ɚ/", "choir: ended in t before"),
      ("/ˈkɝ.nəl/", "colonel")]),
    ("English, British", "en",
     [("Daniel", "onwK4e9ZLuTAKqWW03F9", "male, broadcaster"), ("George", "JBFqnCBsd6RMkjVDRZzb", "male, storyteller"),
      ("Alice", "Xb7hH8MSUJpSbSDYk0k2", "female, educator"), ("Lily", "pFZP5JQG7iQjIQuC4Bku", "female, educational")],
     [EN_SENTENCE, ("/ˈpɹɛ.zənt/", "present, a gift: PREsent"), ("/pɹɪ.ˈzɛnt/", "present, to show: preSENT"),
      ("/ˈtɪə/", "tear, from the eye"), ("/ˈwaɪnd/", "wind, to turn"), ("/ˈkwaɪ.ə/", "choir"),
      ("/ˈkɜː.nəl/", "colonel")]),
    ("German", "de",
     [("Otto", "FTNCalFNG5bRnkkaP5Ug", "male, used so far"), ("Stephan", "IWm8DnJ4NGjFI7QAM5lM", "male, educational"),
      ("Leon Stern", "re2r5d74PqDzicySNW0I", "male, deep"), ("Ben", "aTTiK3YzK3dXETpuDE2h", "male, young"),
      ("Petra", "lzvBSKYbNWDD0a6BaJSK", "female, educational"), ("Annika", "ViKqgJNeCiWZlYgHiAOO", "female, calm")],
     [("Ich hätte gern einen Kaffee und ein Stück Apfelkuchen, bitte.", "plain text"),
      ("/yː.bɐ.ˈzɛ.tsən/", "übersetzen, to translate: stress on SET"), ("/ˈyː.bɐ.zɛ.tsən/", "übersetzen, to ferry: stress on Ü"),
      ("/mo.ˈdɛʁn/", "modern, the adjective: moDERN"), ("/ˈmoː.dɐn/", "modern, to rot: MOdern"),
      ("/çi.ˈʁʊʁk/", "Chirurg: soft ch"), ("/çe.ˈmiː/", "Chemie: soft ch")]),
]


def eleven(text, voice_id, code):
    for attempt in range(4):
        r = requests.post(f"https://api.elevenlabs.io/v1/text-to-speech/{voice_id}?output_format=mp3_44100_128",
                          headers={"xi-api-key": KEY}, json={"text": text, "model_id": MODEL, "language_code": code}, timeout=120)
        if r.status_code == 429:
            time.sleep(8 * (attempt + 1))
            continue
        break
    if not r.ok:
        print("ElevenLabs", r.status_code, r.text[:160], flush=True)
        return None
    spent[0] += len(text)
    return r.content


STYLE = "<style>body{font:16px system-ui;max-width:1300px;margin:30px auto;padding:0 16px}table{border-collapse:collapse;width:100%}td,th{border-bottom:1px solid #ddd;padding:8px 6px;vertical-align:top;text-align:left}audio{width:150px;height:32px}small{color:#666;font-weight:normal}h2{margin-top:36px}@media(prefers-color-scheme:dark){body{background:#221e23;color:#f5edf2}td,th{border-color:#444}small{color:#bbb}}</style>"
page = f"<!doctype html><meta charset=utf-8><title>ElevenLabs: English and German voices</title>{STYLE}<h1>Voices for English and German</h1><p>One column per voice. The first row is a sentence sent as plain text; the rest are single words sent as the IPA shown.</p>"
for g, (group, code, voices, items) in enumerate(GROUPS):
    page += f"<h2>{group}</h2><table><tr><th>Sent</th>" + "".join(f"<th>{html.escape(n)}<br><small>{html.escape(note)}</small></th>" for n, _, note in voices) + "</tr>"
    for i, (text, note) in enumerate(items):
        page += f"<tr><td><b>{html.escape(note)}</b><br><small>{html.escape(text)}</small></td>"
        for v, (name, voice_id, _) in enumerate(voices):
            audio = eleven(text, voice_id, code)
            if audio:
                (out / f"v-{g}-{i}-{v}.mp3").write_bytes(audio)
                page += f"<td><audio controls preload=none src='v-{g}-{i}-{v}.mp3'></audio></td>"
            else:
                page += "<td><small>none</small></td>"
        page += "</tr>"
        print(group, note, flush=True)
    page += "</table>"
(out / "voices.html").write_text(page)
print(f"\ncharacters sent: {spent[0]}; {out / 'voices.html'}")
