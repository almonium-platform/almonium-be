"""Discover which IPA symbols an older Google voice accepts, by bytes alone.

Older voices (WaveNet, Neural2, Standard) never reject an unknown symbol: they drop the
whole <phoneme> and speak the text inside it. That fallback audio is the same whatever
the IPA said. So a symbol is accepted exactly when a string containing it produces audio
that is NOT the fallback audio. No listener is needed.

Steps per voice:
  1. Sample the fallback audio with IPA that cannot be valid (click consonants).
  2. Find an anchor consonant and vowel from a small grid whose audio is not the fallback.
  3. Test every candidate consonant and vowel inside that anchor frame.
  4. Test whether a stress mark is accepted and whether moving it changes the audio.

Bytes show a symbol is used, not that it sounds right; a listening pass still decides.

Usage: discover_inventory.py <folder holding older-all/results.json>
"""
import sys, base64, hashlib, json, threading, time
from concurrent.futures import ThreadPoolExecutor
from pathlib import Path

sys.path.insert(0, "scripts")
from tts_audition import session, ROOT

out = Path(sys.argv[1])
TEXT = "pigutan"
local = threading.local()

CONSONANTS = ("p b t d k g ɡ q ʔ m n ŋ ɲ ɳ f v s z ʃ ʒ x ɣ h ɦ θ ð ç ʂ ʐ ɕ ʑ ts dz tʃ dʒ tɕ dʑ tʂ dʐ ʧ ʤ ʦ "
              "l ɫ ʎ r ɾ ɹ ʁ ʀ j w ʋ ɥ c ɟ ʈ ɖ ɽ β ɸ pʰ tʰ kʰ bʰ dʰ gʰ t̪ d̪ n̪ s̪ z̪ rʲ lʲ nʲ tʲ dʲ sʲ").split()
VOWELS = ("a ɑ æ ɐ e ɛ ə ɜ i ɪ ɨ y ʏ ø œ o ɔ u ʊ ɯ ʌ ɒ ɵ ɤ aː eː iː oː uː ɛː ɔː ɑː yː øː æː "
          "ã ɛ̃ ɔ̃ ɑ̃ õ ẽ ai au ei ou oi aɪ aʊ eɪ oʊ ɔɪ əʊ").split()
GRID_C = ["m", "n", "t", "p", "k", "s", "l", "b", "d"]
GRID_V = ["a", "i", "u", "o", "e", "ɑ", "ɛ", "ɔ"]


def client():
    if not hasattr(local, "c"):
        local.c = session(ROOT / ".env")
    return local.c


def synth(voice, ipa):
    body = {"input": {"ssml": f'<speak><phoneme alphabet="ipa" ph="{ipa}">{TEXT}</phoneme></speak>'},
            "voice": voice, "audioConfig": {"audioEncoding": "MP3"}}
    for i in range(6):
        try:
            r = client().post("https://texttospeech.googleapis.com/v1/text:synthesize", json=body, timeout=90)
        except Exception:
            time.sleep(3)
            continue
        if r.status_code in (429, 503):
            time.sleep(5 * (i + 1))
            continue
        if not r.ok:
            return None
        return hashlib.sha256(base64.b64decode(r.json()["audioContent"])).hexdigest()[:12]
    return None


def many(voice, strings, pool):
    return dict(zip(strings, pool.map(lambda s: synth(voice, s), strings)))


rows = json.load(open(out / "older-all" / "results.json"))
voices = sorted({r["voice"] for r in rows if r["voice"]} | {"uk-UA-Wavenet-B"})
names = {}
for r in rows:
    if r["voice"]:
        names.setdefault(r["voice"], []).append(r["variety"])
names.setdefault("uk-UA-Wavenet-B", []).append("uk-UA")

results = []
with ThreadPoolExecutor(max_workers=6) as pool:
    for name in voices:
        voice = {"languageCode": "-".join(name.split("-")[:2]), "name": name}
        garbage = ["ʘǂǁ"] * 6 + ["ǃʘ"] * 4
        fallback = {h for h in pool.map(lambda s: synth(voice, s), garbage) if h}
        grid = [c + v + c + v for c in GRID_C for v in GRID_V]
        got = many(voice, grid, pool)
        anchor = next(((c, v) for c in GRID_C for v in GRID_V if got.get(c + v + c + v) and got[c + v + c + v] not in fallback), None)
        row = dict(voice=name, varieties=names[name], fallback_renderings=len(fallback), anchor=None,
                   consonants=[], vowels=[], stress_accepted=None, stress_moves=None, length_mark=None)
        if anchor:
            c0, v0 = anchor
            row["anchor"] = c0 + v0
            cons = many(voice, [x + v0 + c0 + v0 for x in CONSONANTS], pool)
            vows = many(voice, [c0 + y + c0 + v0 for y in VOWELS], pool)
            row["consonants"] = [x for x in CONSONANTS if cons.get(x + v0 + c0 + v0) and cons[x + v0 + c0 + v0] not in fallback]
            row["vowels"] = [y for y in VOWELS if vows.get(c0 + y + c0 + v0) and vows[c0 + y + c0 + v0] not in fallback]
            s1, s2 = "ˈ" + c0 + v0 + c0 + v0, c0 + v0 + "ˈ" + c0 + v0
            a = {h for h in pool.map(lambda s: synth(voice, s), [s1] * 3) if h}
            b = {h for h in pool.map(lambda s: synth(voice, s), [s2] * 3) if h}
            row["stress_accepted"] = bool(a) and not (a & fallback)
            row["stress_moves"] = row["stress_accepted"] and not (a & b)
            ln = synth(voice, c0 + v0 + "ː" + c0 + v0)
            row["length_mark"] = bool(ln) and ln not in fallback
        results.append(row)
        print(f"{','.join(names[name]):10} {name:22} anchor={row['anchor']} C={len(row['consonants'])} V={len(row['vowels'])} "
              f"stress: accepted={row['stress_accepted']} moves={row['stress_moves']} length={row['length_mark']}", flush=True)
        if anchor:
            print("    C:", " ".join(row["consonants"]))
            print("    V:", " ".join(row["vowels"]), flush=True)

json.dump(results, open(out / "older-all" / "inventory.json", "w"), ensure_ascii=False, indent=1)
print("\nsaved", out / "older-all" / "inventory.json")
