"""ElevenLabs on tricky real words: homographs, stress traps and irregular spellings, in seven
European languages, each driven by hand-written IPA. One plain-text sentence per language shows
how the voice reads ordinary prose.

The IPA follows the rules learned on 2026-10-02: a stress mark on every word and dots between
syllables. It was written by hand for this test and is not from a dictionary.

Usage: elevenlabs_tricky.py <output folder>   (run from the backend repository root)
"""
import sys, base64, html, json, time
from pathlib import Path

import requests

sys.path.insert(0, "scripts")
from tts_audition import session, ROOT

out = Path(sys.argv[1])
out.mkdir(parents=True, exist_ok=True)
KEY = next(l.split("=", 1)[1].strip() for l in (ROOT / ".env").read_text().splitlines() if l.startswith("ELEVENLABS_API_KEY="))
VOICE_ID, MODEL = "cjVigY5qzO86Huf0OWal", "eleven_v4"
google = session(ROOT / ".env")
spent = [0]

# language -> (name, sentence, [(word, ipa, what to listen for)])
SETS = {
    "en": ("English", "Could you tell me how to get to the nearest train station?", [
        ("lead", "ˈlɛd", "the metal: rhymes with bed"), ("lead", "ˈliːd", "to guide: rhymes with need"),
        ("tear", "ˈtɛɹ", "to rip: rhymes with care"), ("tear", "ˈtɪɹ", "from the eye: rhymes with near"),
        ("bow", "ˈboʊ", "the weapon: rhymes with go"), ("bow", "ˈbaʊ", "to bend: rhymes with now"),
        ("wind", "ˈwɪnd", "moving air"), ("wind", "ˈwaɪnd", "to turn: rhymes with find"),
        ("present", "ˈpɹɛ.zənt", "a gift: PREsent"), ("present", "pɹɪ.ˈzɛnt", "to show: preSENT"),
        ("object", "ˈɑːb.dʒɛkt", "a thing: OBject"), ("object", "əb.ˈdʒɛkt", "to protest: obJECT"),
        ("desert", "ˈdɛ.zɚt", "sand: DEsert"), ("desert", "dɪ.ˈzɝt", "to abandon: deSERT"),
        ("colonel", "ˈkɝ.nəl", "sounds like kernel"), ("choir", "ˈkwaɪ.ɚ", "sounds like quire"),
        ("Worcestershire", "ˈwʊs.tɚ.ʃɚ", "WUUS-ter-sher, three syllables"), ("epitome", "ɪ.ˈpɪ.tə.mi", "ih-PIT-uh-mee"),
        ("hyperbole", "haɪ.ˈpɝ.bə.li", "hy-PER-buh-lee"), ("anemone", "ə.ˈnɛ.mə.ni", "uh-NEM-uh-nee"),
        ("queue", "ˈkjuː", "sounds like the letter Q"), ("draught", "ˈdɹæft", "sounds like draft")]),
    "de": ("German", "Ich hätte gern einen Kaffee und ein Stück Apfelkuchen, bitte.", [
        ("übersetzen", "yː.bɐ.ˈzɛ.tsən", "to translate: stress on SET"), ("übersetzen", "ˈyː.bɐ.zɛ.tsən", "to ferry across: stress on Ü"),
        ("umgehen", "ʊm.ˈɡeː.ən", "to bypass: stress on GEH"), ("umgehen", "ˈʊm.ɡeː.ən", "to deal with: stress on UM"),
        ("modern", "mo.ˈdɛʁn", "modern, the adjective: moDERN"), ("modern", "ˈmoː.dɐn", "to rot: MOdern"),
        ("August", "aʊ.ˈɡʊst", "the month: auGUST"), ("August", "ˈaʊ.ɡʊst", "the name: AUgust"),
        ("Tenor", "te.ˈnoːɐ", "the singer: teNOR"), ("Tenor", "ˈteː.noːɐ", "the gist: TEnor"),
        ("Chirurg", "çi.ˈʁʊʁk", "soft ch at the start, stress on RURG"), ("Orchester", "ɔʁ.ˈkɛs.tɐ", "ch as k: or-KES-ter"),
        ("Restaurant", "ʁɛs.to.ˈʁãː", "French ending, nasal vowel"), ("Ingenieur", "ɪn.ʒe.ˈnjøːɐ", "zh sound, stress at the end"),
        ("Regisseur", "ʁe.ʒɪ.ˈsøːɐ", "zh sound, stress at the end"), ("Chemie", "çe.ˈmiː", "soft ch, stress on MIE"),
        ("Rhythmus", "ˈʁʏt.mʊs", "RÜT-mus"), ("Streichholzschächtelchen", "ˈʃtʁaɪç.hɔlts.ʃɛç.təl.çən", "the famous long one")]),
    "fr": ("French", "Je voudrais un café et un croissant, s'il vous plaît.", [
        ("président", "pʁe.zi.ˈdɑ̃", "the noun: three syllables, nasal ending"), ("président", "pʁe.ˈzid", "they preside: two syllables, ends in d"),
        ("couvent", "ku.ˈvɑ̃", "convent: nasal ending"), ("couvent", "ˈkuv", "they brood: one syllable"),
        ("plus", "ˈplys", "more: with s"), ("plus", "ˈply", "no more: without s"),
        ("écureuil", "e.ky.ˈʁœj", "squirrel"), ("grenouille", "ɡʁə.ˈnuj", "frog")]),
    "es": ("Spanish", "¿Me podría decir dónde está la estación de tren más cercana?", [
        ("término", "ˈteɾ.mi.no", "the term: TERmino"), ("termino", "teɾ.ˈmi.no", "I finish: terMIno"),
        ("terminó", "teɾ.mi.ˈno", "he finished: termiNO"), ("ferrocarril", "fe.ro.ka.ˈril", "two rolled r sounds"),
        ("murciélago", "muɾ.ˈθje.la.ɡo", "bat: stress on CIÉ"), ("desarrollo", "de.sa.ˈro.ʝo", "development")]),
    "it": ("Italian", "Vorrei un caffè e un cornetto, per favore.", [
        ("ancora", "ˈan.ko.ra", "anchor: ANcora"), ("ancora", "an.ˈko.ra", "still: anCOra"),
        ("capitano", "ka.pi.ˈta.no", "captain: capiTAno"), ("capitano", "ˈka.pi.ta.no", "they happen: CApitano"),
        ("sciogliere", "ˈʃɔʎ.ʎe.re", "to melt: SHOL-lye-re"), ("aiuola", "a.ˈjwɔ.la", "flowerbed: every vowel")]),
    "uk": ("Ukrainian", "Скажіть, будь ласка, як пройти до найближчої станції метро?", [
        ("замок", "ˈza.mɔk", "фортеця: ЗАмок"), ("замок", "za.ˈmɔːk", "дверний: заМОК"),
        ("мука", "ˈmu.ka", "страждання: МУка"), ("мука", "mu.ˈkaː", "борошно: муКА"),
        ("паляниця", "pa.lʲa.ˈnɪ.tsʲa", "паляНИця"), ("п'ятниця", "ˈpjat.nɪ.tsʲa", "П'ЯТниця"),
        ("щастя", "ˈʃtʃas.tʲa", "ЩАстя"), ("Укрзалізниця", "ukr.za.lʲiz.ˈnɪ.tsʲa", "УкрзалізНИця")]),
    "pl": ("Polish", "Poproszę kawę i kawałek szarlotki.", [
        ("chrząszcz", "ˈxʂɔ̃ʂtʂ", "beetle: one syllable, all consonants"), ("źdźbło", "ˈʑdʑbwɔ", "blade of grass"),
        ("szczęście", "ˈʂtʂɛ̃ɕ.tɕɛ", "happiness"), ("Wrocław", "ˈvrɔ.tswaf", "VROTS-waf"),
        ("dziękuję", "dʑɛŋ.ˈku.jɛ", "thank you: stress on KU")]),
}


def eleven(text, language):
    for attempt in range(4):
        r = requests.post(f"https://api.elevenlabs.io/v1/text-to-speech/{VOICE_ID}?output_format=mp3_44100_128",
                          headers={"xi-api-key": KEY}, json={"text": text, "model_id": MODEL, "language_code": language}, timeout=120)
        if r.status_code == 429:
            time.sleep(8 * (attempt + 1))
            continue
        break
    if not r.ok:
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


STYLE = "<style>body{font:16px system-ui;max-width:1000px;margin:30px auto;padding:0 16px}table{border-collapse:collapse;width:100%}td,th{border-bottom:1px solid #ddd;padding:8px 6px;vertical-align:top;text-align:left}audio{width:190px;height:32px}small{color:#666}.miss{color:#a33}select{font:inherit}textarea{width:100%;height:160px;font:13px monospace}h2{margin-top:36px}@media(prefers-color-scheme:dark){body{background:#221e23;color:#f5edf2}td,th{border-color:#444}small{color:#bbb}}</style>"
page = f"<!doctype html><meta charset=utf-8><title>ElevenLabs: tricky words</title>{STYLE}<h1>ElevenLabs on tricky words</h1><p>Every word below is spoken from the IPA shown, not from its spelling. Pairs share a spelling and differ only in the IPA. The grey line is what a machine wrote down on hearing the clip; red means it wrote a different word, which is worth a careful listen.</p>"
matched = total = 0
for code, (name, sentence, words) in SETS.items():
    page += f"<h2>{name}</h2>"
    audio = eleven(sentence, code)
    if audio:
        (out / f"{code}-sentence.mp3").write_bytes(audio)
        page += f"<p><b>{html.escape(sentence)}</b> <small>plain text, no IPA</small><br><audio controls preload=none src='{code}-sentence.mp3' style='width:420px'></audio></p>"
    page += "<table><tr><th>Word</th><th>Should sound like</th><th>IPA sent</th><th>Clip</th><th>Verdict</th></tr>"
    for i, (word, ipa, note) in enumerate(words):
        audio = eleven(f"/{ipa}/", code)
        if not audio:
            print("failed", word, flush=True)
            continue
        f = f"{code}-{i}.mp3"
        (out / f).write_bytes(audio)
        heard = hear(out / f, name)
        same = heard.strip().lower().strip(".!") == word.lower()
        total += 1
        matched += same
        page += (f"<tr><td><b>{html.escape(word)}</b></td><td>{html.escape(note)}</td><td>/{html.escape(ipa)}/</td>"
                 f"<td><audio controls preload=none src='{f}'></audio><br><small class='{'' if same else 'miss'}'>machine wrote: {html.escape(str(heard))}</small></td>"
                 f"<td><select data-v='{code} {html.escape(word)} {i}'><option value=''>not judged</option><option>right</option><option>wrong</option><option>unsure</option></select></td></tr>")
        print(f"{code} {word:26} /{ipa}/ machine wrote: {heard} {'' if same else '<<'}", flush=True)
    page += "</table>"
page += "<p><button onclick=\"const t=[...document.querySelectorAll('select')].filter(s=>s.value).map(s=>s.dataset.v+': '+s.value).join('\\n');document.getElementById('o').value=t;navigator.clipboard&&navigator.clipboard.writeText(t)\">Copy results</button></p><textarea id=o placeholder='Results appear here'></textarea>"
(out / "tricky.html").write_text(page)
print(f"\nmachine wrote the same word for {matched} of {total}; characters sent: {spent[0]}; {out / 'tricky.html'}")
