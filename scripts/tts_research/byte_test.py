"""Byte test for older Google voices: does IPA change the output at all, and does a stress mark?

A voice that returns identical bytes for two different inputs treated them as the same input.
Some voices vary between a few renderings, so each input is sampled several times and the
sets of hashes are compared: overlapping sets mean same treatment, disjoint sets mean the
input made a difference. A voice that never repeats itself cannot be judged this way.
"""
import sys, base64, hashlib, json, time
from pathlib import Path

sys.path.insert(0, "scripts")
from tts_audition import session, ROOT

out = Path(sys.argv[1])
N = 4
c = session(ROOT / ".env")
rows = json.load(open(out.parent / "older-all" / "results.json"))
targets = [(r["variety"], r["locale"], r["voice"]) for r in rows if r["clips"]]
targets.append(("uk-UA", "uk-UA", "uk-UA-Wavenet-B"))  # control: heard to obey IPA and stress

TEXT = "pigutan"
ssml = lambda p: {"ssml": f'<speak><phoneme alphabet="ipa" ph="{p}">{TEXT}</phoneme></speak>'}
INPUTS = {
    "raw": {"text": TEXT},
    "s1": ssml("ˈpigutan"),
    "s2": ssml("piˈgutan"),
    "s3": ssml("piguˈtan"),
    "probe": ssml("soˈmakel"),
}


def sample(inp, voice):
    for i in range(5):
        r = c.post("https://texttospeech.googleapis.com/v1/text:synthesize",
                   json={"input": inp, "voice": voice, "audioConfig": {"audioEncoding": "MP3"}}, timeout=90)
        if r.status_code in (429, 503):
            time.sleep(8 * (i + 1))
            continue
        if not r.ok:
            return None
        return hashlib.sha256(base64.b64decode(r.json()["audioContent"])).hexdigest()[:10]
    return None


results = []
for variety, locale, name in targets:
    lang = "cmn-TW" if locale == "cmn-TW" else locale
    voice = {"languageCode": lang, "name": name}
    sets = {k: set() for k in INPUTS}
    for _ in range(N):
        for k, inp in INPUTS.items():
            h = sample(inp, voice)
            if h:
                sets[k].add(h)
    spread = max(len(s) for s in sets.values())
    judgeable = spread < N  # a voice that never repeats gives no information
    overlap = lambda a, b: bool(sets[a] & sets[b])
    if not judgeable:
        sounds = stress = "cannot judge"
    else:
        sounds = "ignored" if overlap("probe", "raw") else "changes output"
        pairs = [overlap("s1", "s2"), overlap("s2", "s3"), overlap("s1", "s3")]
        stress = "ignored" if all(pairs) else "changes output" if not any(pairs) else "partly"
    row = dict(variety=variety, voice=name, renderings_per_input=spread, ipa_sounds=sounds, stress_mark=stress,
               ipa_eq_text=overlap("s1", "raw") if judgeable else None)
    results.append(row)
    print(f"{variety:6} {name:24} renderings={spread} sounds: {sounds:15} stress: {stress:15} "
          f"{'IPA of the same word == plain text' if row['ipa_eq_text'] else ''}", flush=True)

json.dump(results, open(out / "byte-test.json", "w"), ensure_ascii=False, indent=1)
print("\nsaved", out / "byte-test.json")
