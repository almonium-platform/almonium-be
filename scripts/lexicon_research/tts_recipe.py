"""What the lexicon's audio phase shares with the research loop that proved it.

The recipe is the one decided on 2026-10-03 (docs/TTS_IPA_FINDINGS.md, "Decision for the launch
languages") and tested in scripts/tts_research/elevenlabs_loop.py: ElevenLabs eleven_v4 with one
native voice per language, the bare spelling for French, Spanish and Polish, and for Ukrainian the
spelling when the word has one syllable, IPA with /ɨ/ when the spelling contains "и", otherwise the
spelling with an acute stress mark after the stressed vowel, derived from the entry's IPA.

Also here: the deterministic object key a clip is stored under (the backend derives the same key),
and the headword-versus-transcript verdict used to decide whether a clip is verified.
"""

import hashlib
import unicodedata

ENGINE = "elevenlabs"
MODEL = "eleven_v4"
OUTPUT_FORMAT = "mp3_44100_128"
PACE = 1.0
PACE_KEY = "1.00"  # the pace as it appears in the object key: two decimals, matching the backend

# language -> (language name, voice name, voice id), the voices the owner listened to on 2026-10-03
# (docs/TTS_IPA_FINDINGS.md). English and German are sent IPA, which Eric and Otto read "perfectly"
# on the tricky words; the Romance and Slavic voices get the spelling, per the recipe below.
VOICES = {
    "en": ("English", "Eric", "cjVigY5qzO86Huf0OWal"),
    "de": ("German", "Otto", "FTNCalFNG5bRnkkaP5Ug"),
    "uk": ("Ukrainian", "Alex Nekrasov", "9Sj8ugvpK1DmcAXyvi3a"),
    "fr": ("French", "Nicolas", "aQROLel5sQbj1vuIVi6B"),
    "es": ("Spanish", "David Martin", "Nh2zY9kknu6z4pZy6FhD"),
    "pl": ("Polish", "Adam", "hIssydxXZ1WuDorjx6Ic"),
}
# The variety each voice speaks: Eric is American, so en-GB rows wait for a British voice.
VARIETY = {"en": "en-US", "de": "de-DE", "uk": "uk-UA", "fr": "fr-FR", "es": "es-ES", "pl": "pl-PL"}
IPA_LANGUAGES = {"en", "de"}

ACUTE = "́"
UK_VOWEL_LETTERS = set("аеєиіїоуюя")
IPA_VOWELS = set("aɑeɛiɪɨoɔuʉy")


class UnsupportedLanguage(ValueError):
    pass


def voice_for(lang):
    """The (language name, voice name, voice id) for a language, or a clear refusal."""
    if lang in VOICES:
        return VOICES[lang]
    raise UnsupportedLanguage(f"{lang}: no ElevenLabs voice in the table, refusing to guess")


def uk_input(headword, ipa):
    """Return (text sent, mode) per the recipe, or None when the entry lacks what the rule needs."""
    vowels = [c for c in headword if c in UK_VOWEL_LETTERS]
    if len(vowels) == 1:
        return headword, "bare"
    if "и" in headword:
        if not ipa:
            return None
        body = ipa.strip("/").replace("ɪ", "ɨ")
        # the engine wants a dot at every syllable boundary, including before the stress mark
        body = body.replace(".ˈ", "ˈ").replace("ˈ", ".ˈ")
        if body.startswith(".ˈ"):
            body = body[1:]
        return f"/{body}/", "ipa-ɨ"
    if not ipa or "ˈ" not in ipa:
        return None
    before = ipa.split("ˈ", 1)[0]
    stressed_index = sum(1 for c in before if c in IPA_VOWELS)
    total_ipa_vowels = sum(1 for c in ipa if c in IPA_VOWELS)
    if total_ipa_vowels != len(vowels) or stressed_index >= len(vowels):
        return None
    seen = -1
    for i, c in enumerate(headword):
        if c in UK_VOWEL_LETTERS:
            seen += 1
            if seen == stressed_index:
                return headword[: i + 1] + ACUTE + headword[i + 1 :], "stress-mark"
    return None


def engine_input(lang, text, phonemic=None):
    """The text to send for a word in a language: (text, mode), or None when the recipe cannot be built."""
    voice_for(lang)
    text = (text or "").strip()
    if not text:
        return None
    if lang == "uk":
        return uk_input(text, phonemic)
    if lang in IPA_LANGUAGES:
        ipa = (phonemic or "").strip()
        if not ipa.startswith("/") or not ipa.endswith("/") or len(ipa) < 3:
            return None
        return ipa, "ipa"
    return text, "bare"


def object_key(variety, voice_id, text, attempt=1):
    """audio/{variety}/{voice}/{sha256 of "engine|voice|model|pace|format|input"}[-rN].mp3"""
    basis = "|".join([ENGINE, voice_id, MODEL, PACE_KEY, OUTPUT_FORMAT, text])
    digest = hashlib.sha256(basis.encode()).hexdigest()
    suffix = "" if attempt == 1 else f"-r{attempt}"
    return f"audio/{variety}/{voice_id}/{digest}{suffix}.mp3"


# ---------------------------------------------------------------- comparison


def normalise(s):
    s = unicodedata.normalize("NFC", s).lower().replace(ACUTE, "")
    s = "".join(c for c in s if not unicodedata.category(c).startswith(("P", "S")))
    return " ".join(s.split())


def strip_diacritics(s):
    decomposed = unicodedata.normalize("NFD", s)
    return unicodedata.normalize("NFC", "".join(c for c in decomposed if not unicodedata.combining(c)))


def edit_distance(a, b):
    prev = list(range(len(b) + 1))
    for i, ca in enumerate(a, 1):
        cur = [i]
        for j, cb in enumerate(b, 1):
            cur.append(min(prev[j] + 1, cur[j - 1] + 1, prev[j - 1] + (ca != cb)))
        prev = cur
    return prev[-1]


def verdict(headword, transcript):
    """'match', 'match-ignoring-diacritics', 'match-ignoring-spacing', 'match-within-1' or 'mismatch'."""
    h, t = normalise(headword), normalise(transcript or "")
    if not t:
        return "mismatch"
    if h == t:
        return "match"
    h2, t2 = strip_diacritics(h), strip_diacritics(t)
    if h2 == t2:
        return "match-ignoring-diacritics"
    # the transcriber spells a homophone as it pleases: "abord" came back as "à bord"
    if h2.replace(" ", "") == t2.replace(" ", ""):
        return "match-ignoring-spacing"
    if len(h2) >= 6 and edit_distance(h2, t2) <= 1:
        return "match-within-1"
    return "mismatch"


def matched(result):
    return result != "mismatch"
