"""Writer bake-off: the same headwords, the same prompt, several models; one JSON entry each.

The system prompt (rules, inventory, schema example) is a fixed block per language so that every
provider's prompt cache can serve it after the first call; only the headword is in the user
message. Each call's usage, cache hits and latency are kept beside the entry, so the report can
show cost per entry and cache hit rate per provider.

Output: out/<model>/<lang>/<headword>.json (the entry, or {"error": ...}) and .meta.json.
Already-written entries are skipped, so a run can be resumed or extended.

Usage: bakeoff.py <models...> [--langs en,de,uk] [--limit N]   (run from the backend repository root)
Models: claude-opus-5-5 claude-sonnet-5-5 gpt-5.5 gpt-6-sol gpt-6.1-sol gemini-2.5-pro ...
"""
import argparse, json, sys, time
from concurrent.futures import ThreadPoolExecutor
from pathlib import Path

sys.path.insert(0, "scripts")
from tts_audition import session, ROOT

HERE = Path("scripts/lexicon_research")
OUT = HERE / "out"
ENV = {l.split("=", 1)[0]: l.split("=", 1)[1].strip() for l in (ROOT / ".env").read_text().splitlines() if "=" in l and not l.startswith("#")}
PROMPT_VERSION = "v1"
LANG_NAME = {"en": "English", "de": "German", "uk": "Ukrainian"}
VARIETIES = {"en": ["en-US", "en-GB"], "de": ["de-DE"], "uk": ["uk-UA"]}
GLOSS = {"en": "uk, de, pl", "de": "en, uk, pl", "uk": "en, de, pl"}

SCHEMA_EXAMPLE = json.dumps({
    "language": "en", "headword": "lead",
    "lexemes": [{
        "lemma": "lead", "pos": "VERB", "kind": "word", "grammar": {"irregular": True},
        "forms": [{"form": "lead", "features": {"VerbForm": "Inf"}},
                  {"form": "leads", "features": {"Tense": "Pres", "Person": 3, "Number": "Sing"}},
                  {"form": "led", "features": {"Tense": "Past"}, "ipa": {"en-US": "/ˈlɛd/", "en-GB": "/ˈlɛd/"}}],
        "pronunciations": [{"variety": "en-US", "ipa": "/ˈliːd/"}, {"variety": "en-GB", "ipa": "/ˈliːd/"}],
        "senses": [{
            "definition": "to show someone the way by going in front of them",
            "pos_detail": "transitive", "register": "neutral", "cefr": "A2",
            "glosses": {"uk": "вести", "de": "führen", "pl": "prowadzić"},
            "example": "She led us through the dark corridor to the exit.",
            "cues": [{"kind": "wording", "text": "to go first so that others can follow you"},
                     {"kind": "cloze", "text": "She ___ us through the dark corridor to the exit."}]}],
        "relations": [{"type": "family", "lemma": "leader", "pos": "NOUN"},
                      {"type": "confusable", "lemma": "lead", "pos": "NOUN", "why": "same spelling, different vowel"}]
    }]}, ensure_ascii=False, indent=1)


def system_prompt(lang):
    inv = []
    for v in VARIETIES[lang]:
        d = json.loads((HERE / "inventories" / f"{v}.json").read_text())
        inv.append(f"{v}: consonants {' '.join(d['consonants'])}; vowels {' '.join(d['vowels'])}; marks {' '.join(d['diacritics'])}. {d['conventions']}")
    tpl = (HERE / "prompts" / "entry_writer" / f"{PROMPT_VERSION}.md").read_text()
    return (tpl.replace("{language_name}", LANG_NAME[lang]).replace("{gloss_languages}", GLOSS[lang])
            .replace("{inventory}", "\n".join(inv)).replace("{schema_example}", SCHEMA_EXAMPLE))


def user_prompt(lang, row):
    return f"Language: {lang}\nHeadword: {row['headword']}\nZipf frequency: {row['zipf']}"


# ---- providers: each returns (text, meta)
def call_anthropic(model, system, user):
    import anthropic
    client = anthropic.Anthropic(api_key=ENV["ANTHROPIC_API_KEY"])
    t0 = time.time()
    with client.messages.stream(model=model, max_tokens=16000,
                                system=[{"type": "text", "text": system, "cache_control": {"type": "ephemeral"}}],
                                output_config={"effort": "high"},
                                messages=[{"role": "user", "content": user}]) as s:
        m = s.get_final_message()
    u = m.usage
    text = "".join(b.text for b in m.content if b.type == "text")
    return text, {"latency_s": round(time.time() - t0, 1), "input_tokens": u.input_tokens, "output_tokens": u.output_tokens,
                  "cache_read": getattr(u, "cache_read_input_tokens", 0) or 0, "cache_write": getattr(u, "cache_creation_input_tokens", 0) or 0,
                  "stop_reason": m.stop_reason, "model": m.model}


def call_openai(model, system, user):
    from openai import OpenAI
    client = OpenAI(api_key=ENV["OPENAI_API_KEY"])
    t0 = time.time()
    r = client.responses.create(model=model, instructions=system, input=user, max_output_tokens=16000,
                                prompt_cache_key=f"lexicon-{PROMPT_VERSION}-{hash(system) & 0xffffffff:x}")
    u = r.usage
    cached = getattr(getattr(u, "input_tokens_details", None), "cached_tokens", 0) or 0
    reasoning = getattr(getattr(u, "output_tokens_details", None), "reasoning_tokens", 0) or 0
    return r.output_text, {"latency_s": round(time.time() - t0, 1), "input_tokens": u.input_tokens, "output_tokens": u.output_tokens,
                           "cache_read": cached, "reasoning_tokens": reasoning, "model": r.model, "status": r.status}


def call_gemini(model, system, user):
    g = session(ROOT / ".env")
    t0 = time.time()
    body = {"systemInstruction": {"parts": [{"text": system}]}, "contents": [{"role": "user", "parts": [{"text": user}]}],
            "generationConfig": {"temperature": 0.2, "responseMimeType": "application/json", "maxOutputTokens": 16000}}
    for i in range(5):
        r = g.post(f"https://aiplatform.googleapis.com/v1/projects/{ENV['GOOGLE_PROJECT_ID']}/locations/global/publishers/google/models/{model}:generateContent", json=body, timeout=300)
        if r.status_code in (429, 503):
            time.sleep(10 * (i + 1))
            continue
        break
    r.raise_for_status()
    j = r.json()
    u = j.get("usageMetadata", {})
    text = "".join(p.get("text", "") for p in j["candidates"][0]["content"]["parts"])
    return text, {"latency_s": round(time.time() - t0, 1), "input_tokens": u.get("promptTokenCount"), "output_tokens": u.get("candidatesTokenCount"),
                  "cache_read": u.get("cachedContentTokenCount", 0), "reasoning_tokens": u.get("thoughtsTokenCount", 0), "model": model}


def provider(model):
    if model.startswith("claude"):
        return call_anthropic
    if model.startswith(("gpt", "o1", "o3", "o4")):
        return call_openai
    if model.startswith("gemini"):
        return call_gemini
    raise SystemExit(f"unknown provider for {model}")


def parse(text):
    t = text.strip()
    if t.startswith("```"):
        t = t.split("\n", 1)[1].rsplit("```", 1)[0]
    return json.loads(t)


def run_one(model, lang, system, row):
    d = OUT / model / lang
    d.mkdir(parents=True, exist_ok=True)
    f = d / f"{row['headword']}.json"
    if f.exists():
        return "skip"
    for attempt in range(3):
        try:
            text, meta = provider(model)(model, system, user_prompt(lang, row))
            break
        except Exception as e:  # rate limits, transient errors
            meta = {"error": f"{type(e).__name__}: {str(e)[:300]}"}
            text = ""
            time.sleep(15 * (attempt + 1))
    try:
        entry = parse(text) if text else {"error": meta.get("error", "no text")}
    except Exception as e:
        entry = {"error": f"not JSON: {e}", "raw": text[:4000]}
    meta.update({"prompt_version": PROMPT_VERSION, "headword": row["headword"], "set": row["set"]})
    f.write_text(json.dumps(entry, ensure_ascii=False, indent=1) + "\n")
    (d / f"{row['headword']}.meta.json").write_text(json.dumps(meta, ensure_ascii=False, indent=1) + "\n")
    status = "error" if "error" in entry else f"{len(entry.get('lexemes', []))} lexemes"
    print(f"{model:18} {lang} {row['headword']:22} {status:12} {meta.get('latency_s', '?')}s cache_read={meta.get('cache_read', 0)} out={meta.get('output_tokens', '?')}", flush=True)
    return status


ap = argparse.ArgumentParser()
ap.add_argument("models", nargs="+")
ap.add_argument("--langs", default="en,de,uk")
ap.add_argument("--limit", type=int, default=0)
ap.add_argument("--workers", type=int, default=4)
a = ap.parse_args()
for model in a.models:
    for lang in a.langs.split(","):
        rows = json.loads((HERE / "headwords" / f"{lang}.json").read_text())
        if a.limit:
            rows = rows[:a.limit]
        system = system_prompt(lang)
        # the first call warms the cache before the rest run in parallel
        run_one(model, lang, system, rows[0])
        with ThreadPoolExecutor(a.workers) as ex:
            list(ex.map(lambda r: run_one(model, lang, system, r), rows[1:]))
print("done")
