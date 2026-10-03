"""Score the bake-off: every entry each model wrote, checked by the witnesses that need no model.

Per model and language it reports: valid JSON and schema; rejects; IPA symbols inside the
inventory; IPA sounds agreeing with CMUdict (en) and WikiPron (en, de, uk); stress position
agreeing with CMUdict (en); forms that spaCy lemmatizes back to the lemma; definition words inside
the language's top 3,000; the headword absent from its own definition; glosses present for every
gloss language; cues distinct from the definition; senses per lexeme; tokens, cost, cache hit rate
and latency from the run metadata.

Writes out/report.md and out/blind.html (ten headwords per language, every model's entry side by
side in random order, labelled A to E; out/blind-key.json says which is which).

Usage: score.py   (run from the backend repository root)
"""
import json, random, re, unicodedata
from collections import defaultdict
from pathlib import Path

import cmudict, jsonschema, spacy
from wordfreq import top_n_list

HERE = Path("scripts/lexicon_research")
OUT = HERE / "out"
SCHEMA = json.loads((HERE / "entry.schema.json").read_text())
GLOSS = {"en": ["uk", "de", "pl"], "de": ["en", "uk", "pl"], "uk": ["en", "de", "pl"]}
VARIETY = {"en": "en-US", "de": "de-DE", "uk": "uk-UA"}
WIKIPRON = {"en": "eng_latn_us_broad", "de": "deu_latn_broad", "uk": "ukr_cyrl_narrow"}
MODELS_SPACY = {"en": "en_core_web_sm", "de": "de_core_news_sm", "uk": "uk_core_news_sm"}
# dollars per million tokens, list price, 2026-10-03; unknown models priced like their nearest
PRICE = {"claude-opus-5-5": (4, 20), "claude-sonnet-5-5": (2, 10), "gpt-5.5": (5, 25), "gpt-6.1-sol": (5, 25), "gemini-2.5-pro": (1.25, 10)}
CACHE_PRICE = {"claude-opus-5-5": 0.2, "claude-sonnet-5-5": 0.2}

ARPA = {"AA": "ɑ", "AE": "æ", "AH": "ʌ", "AO": "ɔ", "AW": "aʊ", "AY": "aɪ", "EH": "ɛ", "ER": "ɝ", "EY": "eɪ", "IH": "ɪ", "IY": "i",
        "OW": "oʊ", "OY": "ɔɪ", "UH": "ʊ", "UW": "u", "CH": "tʃ", "DH": "ð", "HH": "h", "JH": "dʒ", "NG": "ŋ", "R": "ɹ", "SH": "ʃ",
        "TH": "θ", "Y": "j", "ZH": "ʒ"}
VOWEL = set("iɪeɛæaɑɒɔoʊuʌəɚɝɜyʏøœɐ")


def norm(ipa):
    """Sounds only: no slashes, stress, dots, length, ties; a few symbols folded so that
    conventions do not count as disagreements."""
    s = ipa.strip("/ ").replace("ˈ", "").replace("ˌ", "").replace(".", "").replace("ː", "").replace(" ", "").replace("͡", "").replace("‿", "")
    s = unicodedata.normalize("NFD", s)
    s = "".join(c for c in s if not unicodedata.combining(c) and c not in "ʲ")
    for a, b in (("ɡ", "g"), ("ɹ", "r"), ("ɚ", "ər"), ("ɝ", "ɜr"), ("ʌ", "ə"), ("ɐ", "a"), ("ʋ", "v"), ("w", "v"), ("ɫ", "l"), ("ɨ", "ɪ"), ("ʊ", "u"), ("ɛ", "e"), ("ɔ", "o"), ("ɑ", "a"), ("ɒ", "o"), ("ʃ", "ʃ"), ("x", "x"), ("ç", "x"), ("ʁ", "r"), ("ɾ", "r")):
        s = s.replace(a, b)
    return s


def stress_index(ipa):
    """Which vowel, counting from 0, carries primary stress; -1 when unmarked."""
    body = ipa.strip("/ ")
    n, i = 0, 0
    seen_vowel = False
    while i < len(body):
        c = body[i]
        if c == "ˈ":
            # the next vowel is stressed
            j = i + 1
            while j < len(body) and body[j] not in VOWEL:
                j += 1
            return sum(1 for k in range(j) if body[k] in VOWEL and not (k > 0 and body[k - 1] in VOWEL))
        i += 1
    return -1


def cmu_variants(word):
    out = []
    for pron in cmudict.dict().get(word.lower(), []):
        syms, stress, vi = [], -1, 0
        for p in pron:
            base, digit = re.match(r"([A-Z]+)(\d?)", p).groups()
            ipa = ARPA.get(base, base.lower())
            if base == "AH" and digit == "0":
                ipa = "ə"
            if base == "ER" and digit == "0":
                ipa = "ɚ"
            if digit:
                if digit == "1" and stress < 0:
                    stress = vi
                vi += 1
            syms.append(ipa)
        out.append(("".join(syms), stress))
    return out


def load_wikipron(lang):
    d = defaultdict(set)
    f = HERE / "wikipron" / f"{WIKIPRON[lang]}.tsv"
    if f.exists():
        for line in f.read_text().splitlines():
            w, p = line.split("\t")
            d[w.lower()].add(norm(p))
    return d


def words(text):
    return [w for w in re.findall(r"[^\W\d_]+", text.lower())]


random.seed(20261003)
nlp = {l: spacy.load(m) for l, m in MODELS_SPACY.items()}
top3000 = {l: set(top_n_list(l, 3000)) for l in GLOSS}
wikipron = {l: load_wikipron(l) for l in GLOSS}
models = sorted(p.name for p in OUT.iterdir() if p.is_dir())
stats = {}
entries = defaultdict(dict)  # (lang, headword) -> model -> entry

for model in models:
    for lang in GLOSS:
        d = OUT / model / lang
        if not d.exists():
            continue
        c = defaultdict(int)
        for f in sorted(d.glob("*.json")):
            if f.name.endswith(".meta.json"):
                continue
            e = json.loads(f.read_text())
            meta = json.loads((d / (f.stem + ".meta.json")).read_text()) if (d / (f.stem + ".meta.json")).exists() else {}
            c["entries"] += 1
            c["in_tokens"] += meta.get("input_tokens") or 0
            c["out_tokens"] += meta.get("output_tokens") or 0
            c["cache_read"] += meta.get("cache_read") or 0
            c["cache_write"] += meta.get("cache_write") or 0
            c["latency"] += meta.get("latency_s") or 0
            if "error" in e:
                c["errors"] += 1
                continue
            try:
                jsonschema.validate(e, SCHEMA)
            except jsonschema.ValidationError:
                c["schema_fail"] += 1
                continue
            c["valid"] += 1
            entries[(lang, f.stem)][model] = e
            if not e["lexemes"]:
                c["rejects"] += 1
                continue
            inv = json.loads((HERE / "inventories" / f"{VARIETY[lang]}.json").read_text())
            allowed = set("".join(inv["consonants"] + inv["vowels"] + inv["diacritics"]))
            for lx in e["lexemes"]:
                c["lexemes"] += 1
                c["senses"] += len(lx["senses"])
                c["senses_max"] = max(c["senses_max"], len(lx["senses"]))
                pron = next((p["ipa"] for p in lx["pronunciations"] if p["variety"] == VARIETY[lang]), None)
                if pron:
                    c["prons"] += 1
                    bad = set(unicodedata.normalize("NFC", pron.strip("/"))) - allowed
                    if not bad:
                        c["pron_in_inventory"] += 1
                    else:
                        c.setdefault("bad_symbols", set()).update(bad) if isinstance(c.get("bad_symbols"), set) else None
                    key = lx["lemma"].lower()
                    wp = wikipron[lang].get(key)
                    if wp:
                        c["wp_seen"] += 1
                        c["wp_sounds_agree"] += norm(pron) in wp
                    if lang == "en":
                        cv = cmu_variants(key)
                        if cv:
                            c["cmu_seen"] += 1
                            c["cmu_sounds_agree"] += norm(pron) in {norm(s) for s, _ in cv}
                            c["cmu_stress_agree"] += stress_index(pron) in {st for _, st in cv}
                for fm in lx["forms"]:
                    c["forms"] += 1
                    t = nlp[lang](fm["form"])[0]
                    c["forms_lemmatize"] += t.lemma_.lower() == lx["lemma"].lower()
                for s in lx["senses"]:
                    dw = words(s["definition"])
                    c["def_words"] += len(dw)
                    c["def_common"] += sum(w in top3000[lang] for w in dw)
                    c["def_no_headword"] += lx["lemma"].lower() not in dw
                    c["def_len_ok"] += 6 <= len(dw) <= 15
                    c["gloss_full"] += all(g in s["glosses"] for g in GLOSS[lang])
                    cue_texts = [q["text"] for q in s["cues"]]
                    c["cues_distinct"] += len(set(cue_texts + [s["definition"]])) == len(cue_texts) + 1
        stats[(model, lang)] = dict(c)


def pct(n, d):
    return f"{100 * n / d:.0f}%" if d else "n/a"


lines = ["# Writer bake-off report", "", f"Models: {', '.join(models)}. Prompt v1. Headwords: 50 per language (40 by frequency, 10 hard).", ""]
for lang in GLOSS:
    lines += [f"## {lang}", "", "| Measure | " + " | ".join(models) + " |", "|---|" + "---|" * len(models)]
    rows = [
        ("Entries written", lambda c: str(c.get("entries", 0))),
        ("Valid JSON and schema", lambda c: pct(c.get("valid", 0), c.get("entries", 0))),
        ("Rejected as not a lemma", lambda c: str(c.get("rejects", 0))),
        ("Lexemes per entry", lambda c: f"{c.get('lexemes', 0) / max(1, c.get('valid', 1) - c.get('rejects', 0)):.2f}"),
        ("Senses per lexeme (max)", lambda c: f"{c.get('senses', 0) / max(1, c.get('lexemes', 1)):.1f} ({c.get('senses_max', 0)})"),
        ("IPA symbols inside the inventory", lambda c: pct(c.get("pron_in_inventory", 0), c.get("prons", 0))),
        ("IPA sounds agree with WikiPron (of words it lists)", lambda c: f"{pct(c.get('wp_sounds_agree', 0), c.get('wp_seen', 0))} of {c.get('wp_seen', 0)}"),
        ("IPA sounds agree with CMUdict", lambda c: f"{pct(c.get('cmu_sounds_agree', 0), c.get('cmu_seen', 0))} of {c.get('cmu_seen', 0)}"),
        ("Stress agrees with CMUdict", lambda c: pct(c.get("cmu_stress_agree", 0), c.get("cmu_seen", 0))),
        ("Forms spaCy lemmatizes to the lemma", lambda c: f"{pct(c.get('forms_lemmatize', 0), c.get('forms', 0))} of {c.get('forms', 0)}"),
        ("Definition words in top 3,000", lambda c: pct(c.get("def_common", 0), c.get("def_words", 0))),
        ("Definition 6 to 15 words", lambda c: pct(c.get("def_len_ok", 0), c.get("senses", 0))),
        ("Headword absent from its definition", lambda c: pct(c.get("def_no_headword", 0), c.get("senses", 0))),
        ("All three glosses present", lambda c: pct(c.get("gloss_full", 0), c.get("senses", 0))),
        ("Cues distinct from definition and each other", lambda c: pct(c.get("cues_distinct", 0), c.get("senses", 0))),
        ("Output tokens per entry", lambda c: f"{c.get('out_tokens', 0) / max(1, c.get('entries', 1)):.0f}"),
        # Anthropic's input_tokens excludes cached tokens; OpenAI's and Gemini's include them
        ("Cache hit rate (input)", lambda c: pct(c.get("cache_read", 0), c.get("in_tokens", 0) + (c.get("cache_read", 0) + c.get("cache_write", 0) if c.get("cache_write") else 0))),
        ("Seconds per entry", lambda c: f"{c.get('latency', 0) / max(1, c.get('entries', 1)):.0f}"),
    ]
    for label, fn in rows:
        lines.append(f"| {label} | " + " | ".join(fn(stats.get((m, lang), {})) for m in models) + " |")
    def cost(m, c):
        pin, pout = PRICE.get(m, (5, 25))
        if m.startswith("claude"):
            dollars = c.get("in_tokens", 0) * pin + c.get("cache_read", 0) * CACHE_PRICE[m] + c.get("cache_write", 0) * pin * 1.25 + c.get("out_tokens", 0) * pout
        else:
            dollars = (c.get("in_tokens", 0) - c.get("cache_read", 0)) * pin + c.get("cache_read", 0) * pin / 2 + c.get("out_tokens", 0) * pout
        return f"${dollars / 1e6 / max(1, c.get('entries', 1)):.3f}"
    lines.append("| Cost per entry, list price | " + " | ".join(cost(m, stats.get((m, lang), {})) for m in models) + " |")
    lines.append("")
(OUT / "report.md").write_text("\n".join(lines))
print("\n".join(lines))

# ---- blind page
STYLE = "<style>body{font:15px system-ui;max-width:1400px;margin:30px auto;padding:0 16px}h2{margin-top:40px;border-top:2px solid #888;padding-top:16px}.row{display:flex;gap:12px;align-items:flex-start;overflow-x:auto}.col{flex:1 0 320px;border:1px solid #ccc;border-radius:6px;padding:10px;font-size:14px}.col h4{margin:0 0 6px}.ipa{font-family:'Charis SIL','Gentium Plus',serif}small{color:#666}select{font:inherit}textarea{width:100%;height:160px;font:13px monospace}@media(prefers-color-scheme:dark){body{background:#221e23;color:#f5edf2}.col{border-color:#555}small{color:#bbb}}</style>"
page = f"<!doctype html><meta charset=utf-8><title>Writer bake-off, blind</title>{STYLE}<h1>Which entry would you want in your dictionary?</h1><p>Ten headwords per language. Each column is one model's entry, in random order, labelled A to E. Judge the whole entry: senses, definitions, glosses, examples, IPA. Pick the best and the worst per headword.</p>"
key = {}
for lang in GLOSS:
    heads = [h for (l, h) in entries if l == lang and len(entries[(l, h)]) >= 2 and any(e["lexemes"] for e in entries[(l, h)].values())]
    random.shuffle(heads)
    page += f"<h2>{lang}</h2>"
    for h in sorted(heads[:10]):
        ms = list(entries[(lang, h)].items())
        random.shuffle(ms)
        key[f"{lang}/{h}"] = [m for m, _ in ms]
        page += f"<h3>{h}</h3><div class='row'>"
        for i, (m, e) in enumerate(ms):
            label = "ABCDEFG"[i]
            body = ""
            if not e["lexemes"]:
                body = f"<p><i>rejected: {e.get('reject', '')}</i></p>"
            for lx in e["lexemes"]:
                pr = ", ".join(f"{p['variety']} <span class='ipa'>{p['ipa']}</span>" for p in lx["pronunciations"])
                body += f"<p><b>{lx['lemma']}</b> <small>{lx['pos']} {json.dumps(lx.get('grammar', {}), ensure_ascii=False)}</small><br>{pr}<br><small>forms: {', '.join(f['form'] for f in lx['forms'][:12])}{' …' if len(lx['forms']) > 12 else ''}</small></p><ol>"
                for s in lx["senses"]:
                    gl = ", ".join(f"{k}: {v if isinstance(v, str) else v['text'] + ('' if v['exact'] else ' (≈)')}" for k, v in s["glosses"].items())
                    body += f"<li>{s['definition']} <small>{s['cefr']}, {s['register']}</small><br><small>{gl}</small><br><i>{s['example']}</i><br><small>cues: {' / '.join(q['text'] for q in s['cues'])}</small></li>"
                body += "</ol>"
            page += f"<div class='col'><h4>{label}</h4>{body}</div>"
        page += f"</div><p>Best <select data-v='{lang}/{h} best'><option value=''>-</option>{''.join(f'<option>{l}</option>' for l in 'ABCDEFG'[:len(ms)])}</select> Worst <select data-v='{lang}/{h} worst'><option value=''>-</option>{''.join(f'<option>{l}</option>' for l in 'ABCDEFG'[:len(ms)])}</select></p>"
page += "<p><button onclick=\"const t=[...document.querySelectorAll('select')].filter(s=>s.value).map(s=>s.dataset.v+': '+s.value).join('\\n');document.getElementById('o').value=t;navigator.clipboard&&navigator.clipboard.writeText(t)\">Copy results</button></p><textarea id=o></textarea>"
(OUT / "blind.html").write_text(page)
json.dump(key, open(OUT / "blind-key.json", "w"), ensure_ascii=False, indent=1)
print("\nblind page:", OUT / "blind.html")
