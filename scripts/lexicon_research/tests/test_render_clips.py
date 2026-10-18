"""render_clips with ElevenLabs, whisper and the backend all faked. Run from the repository root:
    scripts/.venv/bin/python -m pytest scripts/lexicon_research/tests -q
"""

import base64
import hashlib
import hmac
import json
import sys
from pathlib import Path

import pytest

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
import render_clips  # noqa: E402
import tts_recipe  # noqa: E402

FR_VOICE = "aQROLel5sQbj1vuIVi6B"
UK_VOICE = "9Sj8ugvpK1DmcAXyvi3a"


# ---------------------------------------------------------------- the shared recipe


def test_object_key_is_deterministic_with_two_decimal_pace():
    key = tts_recipe.object_key("fr-FR", FR_VOICE, "maison")
    assert key == f"audio/fr-FR/{FR_VOICE}/63415050ba97b9ccdff8fe4a049571322e98d8faf42e02ef6a06958d8e484c6f.mp3"
    assert tts_recipe.object_key("fr-FR", FR_VOICE, "maison", 2) == key.replace(".mp3", "-r2.mp3")
    # the basis carries the pace as "1.00"; the research loop's "1.0" would give a different digest
    assert hashlib.sha256(f"elevenlabs|{FR_VOICE}|eleven_v4|1.00|mp3_44100_128|maison".encode()).hexdigest() in key
    assert hashlib.sha256(f"elevenlabs|{FR_VOICE}|eleven_v4|1.0|mp3_44100_128|maison".encode()).hexdigest() not in key


def test_ukrainian_recipe_on_three_known_words():
    assert tts_recipe.uk_input("банк", "/ˈbank/") == ("банк", "bare")
    assert tts_recipe.uk_input("бачити", "/ˈba.tʃɪ.tɪ/") == ("/ˈba.tʃɨ.tɨ/", "ipa-ɨ")
    assert tts_recipe.uk_input("автор", "/ˈaʋ.tɔr/") == ("а́втор", "stress-mark")
    assert tts_recipe.uk_input("бачити", None) is None
    assert tts_recipe.engine_input("uk", "бачити", None) is None
    assert tts_recipe.engine_input("fr", "maison") == ("maison", "bare")


def test_unsupported_languages_are_refused():
    with pytest.raises(tts_recipe.UnsupportedLanguage, match="audition pending"):
        tts_recipe.voice_for("en")
    with pytest.raises(tts_recipe.UnsupportedLanguage):
        tts_recipe.voice_for("de")
    with pytest.raises(tts_recipe.UnsupportedLanguage):
        tts_recipe.engine_input("it", "ancora")


def test_verdict_match_near_miss_and_miss():
    assert tts_recipe.verdict("maison", "Maison.") == "match"
    assert tts_recipe.verdict("fenêtre", "fenetre") == "match-ignoring-diacritics"
    assert tts_recipe.verdict("murciélago", "murcielagos") == "match-within-1"
    assert tts_recipe.verdict("chat", "chats") == "mismatch"
    assert tts_recipe.verdict("maison", "la maison") == "mismatch"
    assert tts_recipe.verdict("maison", "") == "mismatch"
    assert tts_recipe.verdict("а́втор", "Автор") == "match"


# ---------------------------------------------------------------- fakes


class Response:
    def __init__(self, status=200, body=None, content=b"", headers=None):
        self.status_code = status
        self.ok = status < 400
        self._body = body
        self.content = content
        self.headers = headers or {}
        self.text = json.dumps(body) if body is not None else ""

    def json(self):
        return self._body

    def raise_for_status(self):
        assert self.ok


class FakeHttp:
    """Stands in for the requests module: ElevenLabs and the backend behind one object."""

    RequestException = Exception

    def __init__(self, pending, transcripts):
        self.pending = pending  # lang -> rows without a clip
        self.transcripts = transcripts  # (text, attempt) -> what whisper will hear
        self.calls = {}  # text -> synthesis count
        self.puts = []  # (pronunciation id, body, headers)
        self.gets = []

    def get(self, url, params=None, headers=None, timeout=None):
        self.gets.append((url, params, headers))
        if url.endswith("/v1/user/subscription"):
            return Response(body={"character_limit": 100000, "character_count": 1000})
        assert url.endswith("/internal/lexicon/pronunciations")
        assert params["without"] == "clip"
        rows = self.pending[params["language"]]
        return Response(body=rows[params["offset"] : params["offset"] + params["limit"]])

    def post(self, url, headers=None, json=None, timeout=None):
        assert headers == {"xi-api-key": "k"}
        assert json["model_id"] == "eleven_v4" and url.endswith("?output_format=mp3_44100_128")
        text = json["text"]
        self.calls[text] = self.calls.get(text, 0) + 1
        clip = f"mp3:{text}:{self.calls[text]}".encode()
        return Response(content=clip, headers={"character-cost": str(len(text) * 0.4)})

    def put(self, url, data=None, headers=None, timeout=None):
        pronunciation_id = url.rsplit("/", 2)[-2]
        assert url.endswith(f"/internal/lexicon/pronunciations/{pronunciation_id}/clips")
        self.puts.append((pronunciation_id, json.loads(data), headers, data))
        return Response(body={"clip_id": "c", "object_key": "k", "stored": pronunciation_id != "p-unstored"})


class FakeWhisper:
    def __init__(self, transcripts):
        self.transcripts = transcripts

    def transcribe(self, path, lang):
        _, text, attempt = Path(path).read_bytes().decode().split(":")
        return self.transcripts[(text, int(attempt))], 640


def row(id_, headword, lang, phonemic=None, form=None):
    return {
        "id": id_,
        "lexemeId": "lx",
        "headword": headword,
        "lemma": headword,
        "pos": "noun",
        "formId": "f1" if form else None,
        "form": form,
        "variety": tts_recipe.VARIETY[lang],
        "rank": 1,
        "phonemic": phonemic,
        "reading": None,
        "engineInput": None,
    }


@pytest.fixture
def out(tmp_path, monkeypatch):
    monkeypatch.setattr(render_clips, "OUT", tmp_path / "out")
    return tmp_path / "out"


# ---------------------------------------------------------------- the loop


def test_main_loop_publishes_verified_retried_and_unverified_clips(out, monkeypatch):
    pending = {
        "fr": [
            row("p-first", "maison", "fr"),
            row("p-retry", "fenêtre", "fr"),
            row("p-unstored", "chat", "fr", form="chats"),
        ],
        "uk": [
            row("p-skip", "бачити", "uk", phonemic=None),
            row("p-uk", "банк", "uk", phonemic="/ˈbank/"),
        ],
    }
    transcripts = {
        ("maison", 1): "Maison.",
        ("fenêtre", 1): "la fenêtre",
        ("fenêtre", 2): "fenetre",
        ("chats", 1): "chatte",
        ("chats", 2): "chante",
        ("банк", 1): "Банк",
    }
    http = FakeHttp(pending, transcripts)
    monkeypatch.setattr(render_clips, "requests", http)
    monkeypatch.setattr(render_clips.time, "sleep", lambda s: None)

    backend = render_clips.Backend("http://backend/api/v1/", "secret")
    eleven = render_clips.ElevenLabs("k")
    totals = render_clips.run(["fr", "uk"], backend, eleven, lambda: FakeWhisper(transcripts), workers=2)

    assert totals["fr"] == dict(pending=3, rendered=3, verified=2, unverified=1, skipped=0, errors=0, credits=pytest.approx(0.4 * (6 + 7 + 7 + 5 + 5)), reused=0, not_stored=1)
    assert totals["uk"] == dict(pending=2, rendered=1, verified=1, unverified=0, skipped=1, errors=0, credits=pytest.approx(0.4 * 4), reused=0, not_stored=0)
    assert http.calls == {"maison": 1, "fenêtre": 2, "chats": 2, "банк": 1}

    bodies = {pid: body for pid, body, _, _ in http.puts}
    assert set(bodies) == {"p-first", "p-retry", "p-unstored", "p-uk"}
    common = {"engine": "elevenlabs", "model": "eleven_v4", "pace": 1.0, "format": "mp3_44100_128", "duration_ms": 640}
    assert bodies["p-first"] == dict(common, voice=FR_VOICE, engine_input="maison", status="verified", transcript=None, character_cost=pytest.approx(2.4), audio_base64=base64.b64encode(b"mp3:maison:1").decode())
    assert bodies["p-retry"] == dict(common, voice=FR_VOICE, engine_input="fenêtre", status="verified", transcript=None, character_cost=pytest.approx(2.8), audio_base64=base64.b64encode("mp3:fenêtre:2".encode()).decode())
    assert bodies["p-unstored"] == dict(common, voice=FR_VOICE, engine_input="chats", status="unverified", transcript="chatte", character_cost=pytest.approx(2.0), audio_base64=base64.b64encode(b"mp3:chats:1").decode())
    assert bodies["p-uk"] == dict(common, voice=UK_VOICE, engine_input="банк", status="verified", transcript=None, character_cost=pytest.approx(1.6), audio_base64=base64.b64encode("mp3:банк:1".encode()).decode())

    # every call is signed over "{timestamp}." + raw body under the token; the GET signs the empty body
    for _, _, headers, raw in http.puts:
        expected = hmac.new(b"secret", f"{headers['X-Almonium-Books-Timestamp']}.".encode() + raw, hashlib.sha256).hexdigest()
        assert headers["X-Almonium-Books-Signature"] == expected
    _, params, headers = http.gets[0]
    assert params == {"language": "fr", "without": "clip", "limit": 500, "offset": 0}
    assert headers["X-Almonium-Books-Signature"] == hmac.new(b"secret", f"{headers['X-Almonium-Books-Timestamp']}.".encode(), hashlib.sha256).hexdigest()

    # clips and meta are cached under the deterministic key, retry under -r2
    first = out / tts_recipe.object_key("fr-FR", FR_VOICE, "fenêtre")
    second = out / tts_recipe.object_key("fr-FR", FR_VOICE, "fenêtre", 2)
    assert first.read_bytes() == "mp3:fenêtre:1".encode() and second.read_bytes() == "mp3:fenêtre:2".encode()
    assert json.loads(first.with_suffix(".json").read_text())["character_cost"] == pytest.approx(2.8)

    # a second run spends nothing: every clip is reused from disk
    http.puts.clear()
    again = render_clips.run(["fr"], backend, render_clips.ElevenLabs("k"), FakeWhisper(transcripts), workers=2)
    assert http.calls == {"maison": 1, "fenêtre": 2, "chats": 2, "банк": 1}
    assert again["fr"]["credits"] == 0 and again["fr"]["reused"] == 5 and again["fr"]["rendered"] == 3


def test_dry_run_lists_and_estimates_without_synthesis(out, monkeypatch, capsys):
    http = FakeHttp({"es": [row("p1", "murciélago", "es"), row("p2", "sol", "es")]}, {})
    monkeypatch.setattr(render_clips, "requests", http)
    totals = render_clips.run(["es"], render_clips.Backend("http://backend/api/v1", "secret"), None, None, dry_run=True)
    assert http.calls == {} and http.puts == []
    assert totals["es"]["pending"] == 2 and totals["es"]["rendered"] == 0
    assert totals["es"]["credits"] == pytest.approx(0.4 * (10 + 3))
    assert "murciélago" in capsys.readouterr().out


def test_limit_pages_until_the_cap(out, monkeypatch):
    rows = [row(f"p{i}", f"w{i}", "pl") for i in range(7)]
    http = FakeHttp({"pl": rows}, {})
    monkeypatch.setattr(render_clips, "requests", http)
    monkeypatch.setattr(render_clips, "PAGE", 3)
    backend = render_clips.Backend("http://backend/api/v1", "secret")
    assert [r["id"] for r in backend.pending("pl", 5)] == ["p0", "p1", "p2", "p3", "p4"]
    assert [p["limit"] for _, p, _ in http.gets] == [3, 2]
    http.gets.clear()
    assert len(backend.pending("pl")) == 7
    assert [(p["limit"], p["offset"]) for _, p, _ in http.gets] == [(3, 0), (3, 3), (3, 6)]


def test_run_refuses_a_language_without_a_voice(out, monkeypatch):
    http = FakeHttp({}, {})
    monkeypatch.setattr(render_clips, "requests", http)
    with pytest.raises(tts_recipe.UnsupportedLanguage):
        render_clips.run(["en"], render_clips.Backend("http://backend/api/v1", "secret"), None, None, dry_run=True)
    assert http.gets == []
