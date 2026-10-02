"""Second pass: older voices silently fall back to text on any unknown symbol.
Try several probe spellings per voice and see whether ANY of them changes the output
relative to plain text. Then test stress with the symbol set that worked.
"""
import sys, base64, hashlib, json, time
from pathlib import Path

sys.path.insert(0, "scripts")
from tts_audition import session, ROOT

out = Path(sys.argv[1])
c = session(ROOT / ".env")
prev = json.load(open(out / "byte-test.json"))
targets = [(r["variety"], r["voice"]) for r in prev if r["ipa_sounds"] != "changes output"]

TEXT = "pigutan"
ssml = lambda p: {"ssml": f'<speak><phoneme alphabet="ipa" ph="{p}">{TEXT}</phoneme></speak>'}
# (name, syllables of a word clearly different from "pigutan")
SETS = [
    ("a i u only", ["su", "mi", "kal"]),
    ("open vowels", ["sɔ", "ma", "kɛl"]),
    ("no s k l", ["tu", "ni", "map"]),
    ("nasals only", ["ma", "ni", "ma"]),
    ("long vowels", ["suː", "miː", "kaːl"]),
    ("plain o e", ["so", "ma", "kel"]),
]
word = lambda syl, k=None: "".join(("ˈ" if i == k else "") + s for i, s in enumerate(syl))


def sample(inp, voice, n):
    got = set()
    for _ in range(n):
        for i in range(5):
            r = c.post("https://texttospeech.googleapis.com/v1/text:synthesize",
                       json={"input": inp, "voice": voice, "audioConfig": {"audioEncoding": "MP3"}}, timeout=90)
            if r.status_code in (429, 503):
                time.sleep(8 * (i + 1))
                continue
            if r.ok:
                got.add(hashlib.sha256(base64.b64decode(r.json()["audioContent"])).hexdigest()[:10])
            break
    return got


results = []
for variety, name in targets:
    lang = "-".join(name.split("-")[:2])
    voice = {"languageCode": lang, "name": name}
    raw = sample({"text": TEXT}, voice, 5)
    working = None
    for label, syl in SETS:
        s = sample(ssml(word(syl)), voice, 3)
        if s and not (s & raw):
            working = (label, syl)
            break
    row = dict(variety=variety, voice=name, raw_renderings=len(raw), sounds_set=None, stress="not tested")
    if working:
        label, syl = working
        row["sounds_set"] = label
        st = [sample(ssml(word(syl, k)), voice, 3) for k in range(3)]
        pairs = [bool(st[0] & st[1]), bool(st[1] & st[2]), bool(st[0] & st[2])]
        row["stress"] = "ignored" if all(pairs) else "changes output" if not any(pairs) else "partly"
        row["ipa"] = [word(syl, k) for k in range(3)]
    results.append(row)
    print(f"{variety:6} {name:24} sounds: {('USED with ' + row['sounds_set']) if working else 'ignored in all 6 spellings':32} stress: {row['stress']}", flush=True)

json.dump(results, open(out / "byte-test-2.json", "w"), ensure_ascii=False, indent=1)
