"""A word a writer on another plan has written for the dictionary is left alone by the others."""
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
import prompting  # noqa: E402


def test_a_word_in_a_marked_review_folder_is_spoken_for(tmp_path, monkeypatch):
    monkeypatch.setattr(prompting, "HERE", tmp_path)
    monkeypatch.setattr(prompting, "_spoken_for", {})
    marked = tmp_path / "out" / "review-writer-agy-carbon-medium-healthy"
    trial = tmp_path / "out" / "review-hard-fable"
    for folder, word in ((marked, "casa"), (trial, "cane")):
        (folder / "it").mkdir(parents=True)
        (folder / "it" / f"{word}.json").write_text("{}")
    (marked / prompting.SPOKEN_FOR).write_text("")

    assert prompting.spoken_for("it", "casa")
    assert not prompting.spoken_for("it", "cane")  # a trial's words are anyone's
    assert not prompting.spoken_for("es", "casa")
    # the folder's own writer is not stopped by itself
    assert not prompting.spoken_for("it", "casa", but=marked.name)
