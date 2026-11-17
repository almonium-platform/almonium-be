# Plan usage: dictionary entries and books

What the owner's Claude and Codex plans buy, measured on the runs of 11 to 16 November
2026, and how to run the writers so an allowance is spent rather than lost. Numbers are
list prices unless they say otherwise; "plan money" is the share of the plan's price a
list dollar uses up. Re-measure when a plan, a model or a prompt changes.

## The plans

| Account | Plan | Week resets | Used for |
|---|---|---|---|
| `~/.claude` (main) | Max $100 (November 2026; it was $200 before) | Thursday 04:00 Kyiv | the owner's sessions, book reviews when Claude-2 is full |
| `~/.claude-2` | Max $100 | Monday 22:00 Kyiv | entry writers, book reviews first |
| `~/.codex` | Pro $100 ("prolite") | Wednesday 11:47 Kyiv | books, after Codex-2 |
| `~/.codex-2` | Pro $100 ("prolite") | Monday 16:24 Kyiv | books first |

The book processor's dashboard sets which account of a plan is spent first (priority 1).
Since 16 November 2026 translations follow it too (book processor `acc1c68`).

## Claude: the five-hour window is the real limit

- A Claude account has a weekly allowance and a five-hour window. One full window is
  about **12–15% of the week**, so a week holds roughly seven to eight windows.
- Fourteen Opus writers fill a window in about 70 minutes; then everything waits until the
  window turns. More writers fill it sooner, they do not spend more of the week.
- To spend a week, run the writers **round the clock from the start of the week**. A run
  started late cannot catch up: on 11 November the main account had 41% of its week left
  with five hours to go and could spend only about 25%.
- The laptop must not sleep. On 12 November it suspended at 00:25 while the writers waited
  for the window, and the 01:00–04:00 window was lost. Start every long run under
  `systemd-inhibit --what=sleep:idle --mode=block …`.
- Run long jobs on the account whose week expires soonest, and keep interactive sessions
  on the other one, so the owner is never locked out by their own writers.

## Entries (prompt v4, schema 3)

Measured with a warm prompt cache (the first call of a run writes the cache and costs two
to three times as much):

| Writer | List $ per entry | Output tokens | Entries per 1% of a $100 week |
|---|---|---|---|
| Opus 5.5, effort high | 0.032–0.036 | ~1,100–1,340 | ~280–340 (about 28k–34k per week) |
| Fable 5.2, effort high | 0.083–0.086 | ~1,650 | ~85 (2.3× Opus at list price, up to ~4× on the meter) |
| Astra (gpt-6-astra) on Codex, effort high | – | ~800 | not measured; 14% of its entries break the schema (the Codex CLI does not enforce it) |

**Decision (16 November 2026): Opus for mass production.** Fable's entries are not clearly
better: the free checks find the same faults at the same rates, and in the v3 blind test
the judges of the other model family scored Fable and Opus level (7.1 against 7.2); only
Claude judges preferred Fable. Fable is worth writing only in a week whose Fable reset is
going to be redeemed (below). Astra stays off writing.

About 1,000 Opus entries cost roughly $1 of plan money.

### Mending v3 entries instead of rewriting them

The v4 lists began with the most frequent words, which v3 already covered: the first
5,200 v4 entries rewrote words that had v3 entries. Since 16 November the writers skip
any word with an older entry (`v3_entries.py`, `--rewrite-older` to override) and
`mend_v4.py` mends those instead. On 210 words written both ways (Opus 5.5):

| | List $ per word | Frames | Faults (check_v4) |
|---|---|---|---|
| Fresh v4 entry | 0.044 | 777 | 30 |
| Mend v1, medium | 0.023 | 546 | 6 |
| Mend v2, high | 0.040 | – | – |
| **Mend v2, medium (default)** | **0.037** | 902 | 6 |
| Mend v2, low | 0.032 | 697 | 7 |

Thinking is billed as output, so the effort sets most of the price; the patch itself is
about a tenth of an entry. Mending is 15–20% cheaper per call, has a fifth of the faults,
and keeps v3's glosses and review cues (`out/carry/`), which the gloss pass would otherwise
buy again: about 30–40% cheaper than rewriting overall. About 49,000 words wait to be
mended.

### The Fable reset

A Fable reset refills the five-hour window and the Fable allowance and gives back what
Fable used of the week; it does not reset the whole week. Redeemed on a night the week
resets anyway, it is worth one window (~13% of a week). Redeemed after a week of Fable-only
writing has filled the Fable meter (one window is about +17% on it, so about six windows),
it gives back about 80% of the week. Claude-2 holds one (until 9 March 2027), the main
account probably another. Use one only in a week planned for it, early, with interactive
sessions on the other account.

## Books: how much Claude a week of Codex needs

From the book processor's ledger for 16 November 2026, about 90% of a Codex week spent on
translations into EN, FR, ES and DE (plus some level adaptations):

| Model, role | Calls | List $ |
|---|---|---|
| gpt-6.1-sol, translating | 327 | ~250 |
| gpt-6-astra, second reviewer | 2,604 | ~370 |
| Opus 5.5, first reviewer | 5,176 | ~250 |

So $1 of Codex book work needs about $0.40 of Opus, and **one Codex week needs about a
third of a $100 Claude week**: one Claude account can carry the reviews of about three
Codex weeks. Level adaptations use Claude differently (difficulty judges, auditors);
measure them separately.

## What made books and entries fail when nothing was wrong with them

Fixed on 16 November 2026; listed so it is not reintroduced.

- A job refused for a plan's limit recorded only its type (`AllowanceUsedUp`), so the
  production did not recognise it, started the step again and held the book after two
  tries; a refusal it did recognise held the book for a person anyway. Now a refusal makes
  the book wait 30 minutes and try again, uncounted (book processor `d764501`, `48d6204`).
- With thirty books under way, a step whose job waited for a worker was held after three
  hours as "Nothing finished"; it now gets twelve.
- Restarting a plan worker while productions run kills their jobs: never do it (RUNBOOK).
- The entry writers saved a give-up or an unreadable answer as the word's entry, so later
  runs skipped the word; the Codex writer stopped the whole run at the first limit; a hung
  call ended a language's run. All three now wait or retry (`f44ca8ea`).

## How deep English goes

English is the deepest language. Counted on 16 November 2026 from Wiktionary's English
lemmas (plain lower-case words, not hyphenated compounds, whose wordfreq score is inflated
by their parts) against wordfreq's Zipf scale:

| Zipf at least | Lemma spellings | Of them with an entry then |
|---|---|---|
| 4 (once per 100k words) | 5,551 | 3,404 |
| 3 | 18,912 | 10,578 |
| 2.5 | 30,411 | 12,841 |
| 2 | 45,101 | 14,940 |
| 1.5 | 64,554 | 15,827 |
| 1 | 90,932 | 15,902 |

The target is everything down to Zipf 1.5: about 65,000 single words, the depth of a
large advanced learner's dictionary, and enough for the rare words of nineteenth-century
novels that a reader actually taps. Below 1.5 the list turns into scientific names and
one-off coinages, so 100,000 single words would buy mostly noise; the next depth is
multiword lexemes (phrasal verbs, idioms), which need a frequency source of their own, and
capitalised lemmas (Monday, English, Christmas), left out of the first list because most
capitalised spellings are names.

The first v4 lists kept only words spaCy tagged as nouns, verbs, adjectives and adverbs out
of context, which left out every function word (the, to, of, I) and some common ones
(time, first, two). `headwords/en-deep-en.json` (48,727 words, most frequent first) holds
every lemma down to Zipf 1.5 that had no entry, function words first. At about 28,000 Opus
entries per $100 week it is under two weeks of one Claude account.

## Where to look

- The book processor's Operations page, Entries tab: entries per language and writer,
  prompt and schema versions, faults, queues left and words to mend, refreshed every quarter
  of an hour by `almonium-lexicon-status.timer` (`scripts/lexicon_research/lexicon_status.py`).
- `scripts/lexicon_research/check_v4.py` for the faults with examples.
- The private repository `almonium-platform/almonium-lexicon-data`: every written entry, committed
  every three hours from the owner's machine by `scripts/lexicon_research/lexicon_data_sync.py`
  (`almonium-lexicon-data-sync.timer`). Its git directory sits beside the backend checkout and its
  work tree is `out/`, so nothing is copied. It is the only copy of the entries off the laptop and
  the way a cloud session reads them.
