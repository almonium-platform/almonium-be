"""ElevenLabs with native voices: the input recipe per language, tested as a whole.

Ukrainian: the recipe proposed on 2026-10-02 applied to a fresh set of words. A word goes as its
spelling with a stress mark, unless the stressed vowel is "и", in which case it goes as IPA with
/ɨ/ (the stress mark turns a stressed "и" into "і").

French, Spanish, Polish: each word as its spelling and as IPA, side by side, to pick the input
mode. Spanish and Polish spelling already fixes the stress; French homographs can only be told
apart by IPA.

A machine transcription sits under every clip; it does not judge stress or accent.

Usage: elevenlabs_recipe.py <output folder>   (run from the backend repository root)
"""
import sys, base64, html, json, time
from pathlib import Path

import requests

sys.path.insert(0, "scripts")
from tts_audition import session, ROOT

out = Path(sys.argv[1])
out.mkdir(parents=True, exist_ok=True)
KEY = next(l.split("=", 1)[1].strip() for l in (ROOT / ".env").read_text().splitlines() if l.startswith("ELEVENLABS_API_KEY="))
MODEL = "eleven_v4"
VOICES = {"uk": ("Ukrainian", "Alex Nekrasov", "9Sj8ugvpK1DmcAXyvi3a"), "fr": ("French", "Nicolas", "aQROLel5sQbj1vuIVi6B"),
          "es": ("Spanish", "David Martin", "Nh2zY9kknu6z4pZy6FhD"), "pl": ("Polish", "Adam", "hIssydxXZ1WuDorjx6Ic")}
google = session(ROOT / ".env")
spent = [0]

# language -> [(what to listen for, [(how it was written, text sent)])]
SETS = {
    "uk": [
        ("замок, фортеця: ЗАмок", [("stress mark", "за́мок")]), ("замок, дверний: заМОК", [("stress mark", "замо́к")]),
        ("мука, страждання: МУка", [("stress mark", "му́ка")]), ("мука, борошно: муКА", [("stress mark", "мука́")]),
        ("дорога, шлях: доРОга", [("stress mark", "доро́га")]), ("дорога, цінна: дороГА", [("stress mark", "дорога́")]),
        ("атлас, карти: Атлас", [("stress mark", "а́тлас")]), ("атлас, тканина: атЛАС", [("stress mark", "атла́с")]),
        ("орган, тіла: Орган", [("stress mark", "о́рган")]), ("орган, інструмент: орГАН", [("stress mark", "орга́н")]),
        ("плачу, сльози: ПЛАчу", [("stress mark", "пла́чу")]), ("плачу, гроші: плаЧУ", [("stress mark", "плачу́")]),
        ("брати, взяти: БРАти", [("stress mark", "бра́ти")]), ("брати, родичі: браТИ", [("IPA ɨ", "/bra.ˈtɨ/")]),
        ("Україна", [("stress mark", "Украї́на")]), ("сьогодні", [("stress mark", "сього́дні")]),
        ("джерело", [("stress mark", "джерело́")]), ("ґанок", [("stress mark", "ґа́нок")]),
        ("п'ятниця", [("stress mark", "п'я́тниця")]), ("щодня", [("stress mark", "щодня́")]),
        ("книга", [("IPA ɨ", "/ˈknɨ.ɦa/")]), ("мити", [("IPA ɨ", "/ˈmɨ.tɨ/")]), ("сир", [("IPA ɨ", "/ˈsɨr/")]),
        ("великий", [("IPA ɨ", "/ve.ˈlɨ.kɨj/")]), ("язик", [("IPA ɨ", "/ja.ˈzɨk/")]),
        ("криниця", [("IPA ɨ", "/krɨ.ˈnɨ.tsʲa/")]), ("писати", [("IPA ɨ", "/pɨ.ˈsa.tɨ/"), ("stress mark", "писа́ти")]),
    ],
    "fr": [
        ("président, le nom: trois syllabes", [("spelling", "président"), ("IPA", "/pʁe.zi.ˈdɑ̃/")]),
        ("président, ils président: deux syllabes, d final", [("IPA", "/pʁe.ˈzid/")]),
        ("plus, davantage: avec s", [("spelling", "plus"), ("IPA", "/ˈplys/")]),
        ("plus, ne plus: sans s", [("IPA", "/ˈply/")]),
        ("fils, enfant: avec s", [("spelling", "fils"), ("IPA", "/ˈfis/")]),
        ("fils, de couture: sans s, avec l", [("IPA", "/ˈfil/")]),
        ("écureuil", [("spelling", "écureuil"), ("IPA", "/e.ky.ˈʁœj/")]),
        ("grenouille", [("spelling", "grenouille"), ("IPA", "/ɡʁə.ˈnuj/")]),
    ],
    "es": [
        ("término: TÉRmino", [("spelling", "término"), ("IPA", "/ˈteɾ.mi.no/")]),
        ("termino: terMIno", [("spelling", "termino"), ("IPA", "/teɾ.ˈmi.no/")]),
        ("terminó: termiNÓ", [("spelling", "terminó"), ("IPA", "/teɾ.mi.ˈno/")]),
        ("murciélago", [("spelling", "murciélago"), ("IPA", "/muɾ.ˈθje.la.ɡo/")]),
        ("ferrocarril", [("spelling", "ferrocarril"), ("IPA", "/fe.ro.ka.ˈril/")]),
    ],
    "pl": [
        ("chrząszcz", [("spelling", "chrząszcz"), ("IPA", "/ˈxʂɔ̃ʂtʂ/")]),
        ("źdźbło", [("spelling", "źdźbło"), ("IPA", "/ˈʑdʑbwɔ/")]),
        ("szczęście", [("spelling", "szczęście"), ("IPA", "/ˈʂtʂɛ̃ɕ.tɕɛ/")]),
        ("Wrocław", [("spelling", "Wrocław"), ("IPA", "/ˈvrɔ.tswaf/")]),
        ("dziękuję", [("spelling", "dziękuję"), ("IPA", "/dʑɛŋ.ˈku.jɛ/")]),
    ],
}


def eleven(text, code):
    for attempt in range(4):
        r = requests.post(f"https://api.elevenlabs.io/v1/text-to-speech/{VOICES[code][2]}?output_format=mp3_44100_128",
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


def hear(path, language):
    body = {"contents": [{"role": "user", "parts": [
        {"inlineData": {"mimeType": "audio/mpeg", "data": base64.b64encode(path.read_bytes()).decode()}},
        {"text": f"One {language} word is spoken. Write that word in normal {language} spelling. Reply JSON: {{\"word\": \"...\"}}"}]}],
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


STYLE = "<style>body{font:16px system-ui;max-width:1000px;margin:30px auto;padding:0 16px}table{border-collapse:collapse;width:100%}td,th{border-bottom:1px solid #ddd;padding:8px 6px;vertical-align:top;text-align:left}audio{width:190px;height:32px}small{color:#666}h2{margin-top:36px}@media(prefers-color-scheme:dark){body{background:#221e23;color:#f5edf2}td,th{border-color:#444}small{color:#bbb}}</style>"
page = f"<!doctype html><meta charset=utf-8><title>ElevenLabs: input recipe</title>{STYLE}<h1>The input recipe, tested as a whole</h1><p>Above each clip is exactly what was sent. Under it is what a machine wrote down on hearing it; the machine cannot judge stress or accent.</p>"
for code, rows in SETS.items():
    language, voice, _ = VOICES[code]
    page += f"<h2>{language} <small>voice: {html.escape(voice)}</small></h2><table>"
    for n, (label, variants) in enumerate(rows):
        cells = ""
        for k, (how, text) in enumerate(variants):
            audio = eleven(text, code)
            if not audio:
                continue
            f = f"{code}-{n}-{k}.mp3"
            (out / f).write_bytes(audio)
            heard = hear(out / f, language)
            cells += f"<td><b>{html.escape(how)}</b> <small>{html.escape(text)}</small><br><audio controls preload=none src='{f}'></audio><br><small>machine wrote: {html.escape(str(heard))}</small></td>"
            print(f"{code} {label[:34]:34} {how:11} {text:22} machine wrote: {heard}", flush=True)
        page += f"<tr><td><b>{html.escape(label)}</b></td>{cells}</tr>"
    page += "</table>"
(out / "recipe.html").write_text(page)
print(f"\ncharacters sent: {spent[0]}; {out / 'recipe.html'}")
