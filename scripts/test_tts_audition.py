"""Offline tests for billable-request guards and resumable audition generation."""
import contextlib
import io
import json
from pathlib import Path
import tempfile
import unittest
from unittest.mock import Mock, patch

import tts_audition as audition


class AuditionTest(unittest.TestCase):
    def test_provider_errors_do_not_expose_response_messages(self):
        response = Mock(status_code=403)
        response.json.return_value = {"error": {"status": "PERMISSION_DENIED",
            "message": "private account details", "details": [{"reason": "SERVICE_DISABLED",
                "metadata": {"consumer": "private-project"}}]}}
        self.assertEqual(audition.provider_error(response), "HTTP 403: PERMISSION_DENIED SERVICE_DISABLED")

    def test_budget_guard_precedes_credentials_and_requests(self):
        with tempfile.TemporaryDirectory() as folder, patch.object(audition, "session") as session:
            with patch("sys.argv", ["audition", "custom", "--output", folder, "--max-characters", "1"]):
                with self.assertRaisesRegex(RuntimeError, "character limit"):
                    audition.main()
            session.assert_not_called()

    def test_resume_reuses_only_identical_completed_requests(self):
        row = audition.custom_plan()[0]
        client = Mock()
        client.get.return_value = Mock(ok=True)
        client.get.return_value.json.return_value = {"voices": [{"name": row["voiceId"]}]}
        client.post.return_value = Mock(ok=True)
        client.post.return_value.json.return_value = {"audioContent": "YXVkaW8="}
        with tempfile.TemporaryDirectory() as folder:
            with patch.object(audition, "session", return_value=client), \
                    patch.object(audition, "custom_plan", return_value=[row]), \
                    patch.object(audition, "metrics", return_value={"decodeOk": True}), \
                    patch.object(audition.time, "sleep"), \
                    patch("sys.argv", ["audition", "custom", "--output", folder]), \
                    contextlib.redirect_stdout(io.StringIO()):
                self.assertEqual(audition.main(), 0)
                self.assertEqual(audition.main(), 0)
                self.assertEqual(client.post.call_count, 1)
                row["input"] = {"text": "Changed request"}
                self.assertEqual(audition.main(), 0)
                self.assertEqual(client.post.call_count, 2)
            data = json.loads((Path(folder) / "manifest.json").read_text())
            self.assertEqual(data["clips"][0]["input"], row["input"])

    def test_inventory_parser_accepts_only_published_audio_paths(self):
        table = audition.VoiceTable()
        table.feed('<tr><td>English</td><td>Premium</td><td>en-US</td><td>voice</td>'
                   '<td>MALE</td><td><source src="/static/text-to-speech/docs/audio/voice.wav"></td></tr>')
        self.assertEqual(table.rows["voice"], "https://cloud.google.com/static/text-to-speech/docs/audio/voice.wav")
        table.feed('<tr><td>X</td><td>X</td><td>X</td><td>bad</td><td>X</td>'
                   '<td><source src="https://example.test/private"></td></tr>')
        self.assertNotIn("bad", table.rows)


if __name__ == "__main__":
    unittest.main()
