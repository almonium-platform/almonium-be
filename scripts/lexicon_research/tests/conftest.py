import pytest


@pytest.fixture(autouse=True)
def no_gateway(monkeypatch):
    """No test reaches the book processor's gateway, which asks paid models: a test that means to go through it
    takes this away and fakes the command."""
    monkeypatch.setenv("LEXICON_DIRECT", "1")
