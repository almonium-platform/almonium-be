"""ElevenLabs: does it obey IPA, and can a listener tell it from Chirp on single words?

Two pages:
  index.html  the IPA test. Eleven v4 takes IPA inline between slashes, so the whole text of a
              clip is the IPA. Per language: the stress on each syllable of a made-up word, and
              a second made-up word. Plus real homograph pairs.
  blind.html  twenty real German and English words as plain text, ElevenLabs beside Google
              Chirp 3 HD, unlabelled and in random order.

Needs ELEVENLABS_API_KEY in .env. Texts are kept tiny: the free plan has 10,000 characters.

Usage: elevenlabs_sheet.py <output folder>   (run from the backend repository root)
"""
import sys, base64, hashlib, html, json, random, time
from pathlib import Path

import requests

sys.path.insert(0, "scripts")
from tts_audition import session, ROOT

out = Path(sys.argv[1])
out.mkdir(parents=True, exist_ok=True)
KEY = next(l.split("=", 1)[1].strip() for l in (ROOT / ".env").read_text().splitlines() if l.startswith("ELEVENLABS_API_KEY="))
VOICE = ("Eric", "cjVigY5qzO86Huf0OWal")
MODEL = "eleven_v4"
google = session(ROOT / ".env")
spent = [0]


def eleven(text, language=None, seed=None, model=MODEL):
    body = {"text": text, "model_id": model}
    if language:
        body["language_code"] = language
    if seed is not None:
        body["seed"] = seed
    for attempt in range(4):
        r = requests.post(f"https://api.elevenlabs.io/v1/text-to-speech/{VOICE[1]}?output_format=mp3_44100_128",
                          headers={"xi-api-key": KEY}, json=body, timeout=120)
        if r.status_code == 429:
            time.sleep(8 * (attempt + 1))
            continue
        if r.status_code == 400 and language and attempt == 0:
            body.pop("language_code")  # some models refuse the hint; let the model detect
            continue
        break
    if not r.ok:
        return None, f"HTTP {r.status_code} {r.text[:120]}"
    spent[0] += len(text)
    return r.content, ""


def chirp(text, locale):
    r = google.post("https://texttospeech.googleapis.com/v1/text:synthesize", timeout=90, json={
        "input": {"text": text}, "voice": {"languageCode": locale, "name": f"{locale}-Chirp3-HD-Charon"},
        "audioConfig": {"audioEncoding": "MP3"}})
    return base64.b64decode(r.json()["audioContent"]) if r.ok else None


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


# ---- determinism: the same request with the same seed, twice
a, _ = eleven("/piˈgutan/", "en", seed=7)
b, _ = eleven("/piˈgutan/", "en", seed=7)
c, _ = eleven("/piˈgutan/", "en")
d, _ = eleven("/piˈgutan/", "en")
h = lambda x: hashlib.sha256(x).hexdigest()[:8] if x else "none"
determinism = (f"same seed twice: {h(a)} {h(b)} ({'identical' if a and a == b else 'different'}); "
               f"no seed twice: {h(c)} {h(d)} ({'identical' if c and c == d else 'different'})")
print("determinism:", determinism, flush=True)

# ---- page 1: IPA
LANGS = [("de", "German"), ("en", "English"), ("fr", "French"), ("es", "Spanish"), ("it", "Italian"), ("uk", "Ukrainian"),
         ("pl", "Polish"), ("ru", "Russian"), ("cs", "Czech"), ("hu", "Hungarian"), ("sv", "Swedish"), ("el", "Greek"),
         ("he", "Hebrew"), ("lt", "Lithuanian"), ("et", "Estonian"), ("eu", "Basque"), ("gl", "Galician"), ("sw", "Swahili")]
CLIPS = [("s1", "/ˈpigutan/", "PEE-goo-tan"), ("s2", "/piˈgutan/", "pee-GOO-tan"), ("s3", "/piguˈtan/", "pee-goo-TAN"),
         ("w2", "/soˈmakel/", "soh-MAH-kel")]
rows = []
for code, name in LANGS:
    row = {"code": code, "name": name, "clips": [], "heard": ""}
    for key, text, label in CLIPS:
        audio, err = eleven(text, code)
        if audio:
            (out / f"{code}-{key}.mp3").write_bytes(audio)
            row["clips"].append((key, text, label))
        else:
            row["error"] = err
    if any(k == "s2" for k, _, _ in row["clips"]):
        row["heard"] = f"{hear(out / f'{code}-s2.mp3')} / {hear(out / f'{code}-w2.mp3')}"
    rows.append(row)
    print(f"{name:11} clips={len(row['clips'])} machine heard: {row['heard']} {row.get('error', '')}", flush=True)

PAIRS = [("de", "umfahren", "/ʊmˈfaːʁən/", "drive around, stress on FAH"), ("de", "umfahren", "/ˈʊmfaːʁən/", "knock over, stress on UM"),
         ("en", "record", "/ˈɹɛkɚd/", "noun, REcord"), ("en", "record", "/ɹɪˈkɔɹd/", "verb, reCORD"),
         ("en", "read", "/ɹiːd/", "present, reed"), ("en", "read", "/ɹɛd/", "past, red"),
         ("uk", "замок", "/ˈzamɔk/", "castle, ZAmok"), ("uk", "замок", "/zaˈmɔk/", "lock, zaMOK"),
         ("it", "principi", "/ˈprintʃipi/", "princes, PRINcipi"), ("it", "principi", "/prinˈtʃipi/", "principles, prinCIpi"),
         ("es", "público", "/ˈpubliko/", "audience, PUblico"), ("es", "publicó", "/publiˈko/", "he published, publiCO"),
         ("fr", "fils", "/fis/", "son, with s"), ("fr", "fils", "/fil/", "threads, no s")]
pairs = []
for i, (code, word, ipa, meaning) in enumerate(PAIRS):
    audio, err = eleven(ipa, code)
    if audio:
        (out / f"pair-{i}.mp3").write_bytes(audio)
        pairs.append((f"pair-{i}.mp3", code, word, ipa, meaning))
    print(f"pair {code} {word} {ipa} {'ok' if audio else err}", flush=True)

STYLE = "<style>body{font:16px system-ui;max-width:1040px;margin:30px auto;padding:0 16px}table{border-collapse:collapse;width:100%}td,th{border-bottom:1px solid #ddd;padding:8px 6px;vertical-align:top;text-align:left}audio{width:170px;height:32px}small{color:#666}select{font:inherit}textarea{width:100%;height:160px;font:13px monospace}@media(prefers-color-scheme:dark){body{background:#221e23;color:#f5edf2}td,th{border-color:#444}small{color:#bbb}}</style>"
COPY = "<p><button onclick=\"const t=[...document.querySelectorAll('select')].filter(s=>s.value).map(s=>s.dataset.v+': '+s.value).join('\\n');document.getElementById('o').value=t;navigator.clipboard&&navigator.clipboard.writeText(t)\">Copy results</button></p><textarea id=o placeholder='Results appear here'></textarea>"
page = f"<!doctype html><meta charset=utf-8><title>ElevenLabs IPA</title>{STYLE}<h1>ElevenLabs {MODEL}, voice {VOICE[0]}: does it obey IPA?</h1>"
page += "<p>The text of every clip is only the IPA shown, between slashes. There is no spelling for the voice to fall back on, so the question is simply whether it says those sounds, with the stress where the capitals are.</p>"
page += f"<p><small>Determinism: {html.escape(determinism)}</small></p>"
page += "<h2>Real words with two pronunciations</h2><table><tr><th>Word</th><th>IPA sent</th><th>Should sound like</th><th>Clip</th></tr>"
for f, code, word, ipa, meaning in pairs:
    page += f"<tr><td><b>{html.escape(word)}</b> <small>{code}</small></td><td>{html.escape(ipa)}</td><td>{html.escape(meaning)}</td><td><audio controls preload=none src='{f}'></audio></td></tr>"
page += "</table><h2>Made-up word, stress moved across syllables</h2><table><tr><th>Language</th><th>Stress 1st</th><th>Stress 2nd</th><th>Stress 3rd</th><th>Other word</th><th>Verdict</th></tr>"
for r in rows:
    cells = ""
    for key in ("s1", "s2", "s3", "w2"):
        clip = next((x for x in r["clips"] if x[0] == key), None)
        cells += (f"<td><b>{clip[2]}</b><br><small>{html.escape(clip[1])}</small><br><audio controls preload=none src='{r['code']}-{key}.mp3'></audio></td>"
                  if clip else "<td><small>none</small></td>")
    page += (f"<tr><td><b>{r['name']}</b><br><small>machine heard: {html.escape(r['heard'])}</small></td>{cells}"
             f"<td><select data-v='{r['code']}'><option value=''>not judged</option><option>sounds and stress</option><option>sounds only</option><option>wrong sounds</option><option>unsure</option></select></td></tr>")
page += "</table>" + COPY
(out / "index.html").write_text(page)

# ---- page 2: blind comparison against Chirp on real words, plain text
WORDS = [("de-DE", "de", w) for w in ["Ausgabe", "Entschuldigung", "Schmetterling", "Eichhörnchen", "zwanzig", "Geschwindigkeit",
                                      "Wörterbuch", "gemütlich", "Frühstück", "Verantwortung"]]
WORDS += [("en-US", "en", w) for w in ["schedule", "comfortable", "thorough", "rural", "particularly", "vegetable",
                                       "entrepreneur", "squirrel", "clothes", "sixth"]]
random.seed(20261002)
key, blind = {}, f"<!doctype html><meta charset=utf-8><title>Blind: ElevenLabs or Chirp</title>{STYLE}<h1>Which voice would you rather learn from?</h1><p>Twenty real words, each spoken by two engines in random order. Pick the one you prefer, or say they are the same. The answer key is in <code>blind-key.json</code>; do not open it until you are done.</p><table><tr><th>Word</th><th>A</th><th>B</th><th>Preference</th></tr>"
for i, (locale, code, word) in enumerate(WORDS):
    e, _ = eleven(word, code)
    g = chirp(word, locale)
    if not e or not g:
        print("skip", word, flush=True)
        continue
    order = ["eleven", "chirp"]
    random.shuffle(order)
    for slot, engine in zip("AB", order):
        (out / f"blind-{i}-{slot}.mp3").write_bytes(e if engine == "eleven" else g)
    key[word] = {"A": order[0], "B": order[1]}
    blind += (f"<tr><td><b>{html.escape(word)}</b> <small>{code}</small></td><td><audio controls preload=none src='blind-{i}-A.mp3'></audio></td>"
              f"<td><audio controls preload=none src='blind-{i}-B.mp3'></audio></td>"
              f"<td><select data-v='{html.escape(word)}'><option value=''>not judged</option><option>A</option><option>B</option><option>same</option></select></td></tr>")
    print("blind", word, order, flush=True)
blind += "</table>" + COPY
(out / "blind.html").write_text(blind)
json.dump(key, open(out / "blind-key.json", "w"), ensure_ascii=False, indent=1)
print(f"\ncharacters sent to ElevenLabs: {spent[0]}; pages: {out / 'index.html'} and {out / 'blind.html'}")
