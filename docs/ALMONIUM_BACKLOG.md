# Almonium backlog

**Derived from:** Codex repo audit, product vision doc, and the strategy conversation
**Date:** 2026-07-28
**Effort key:** S = under a day · M = 2–5 days · L = 1–3 weeks · XL = a month or more

---

## How to use this

Work top to bottom. Do not start a P1 item while a P0 item is open. Anything
in **Lab** requires you to write down, in one sentence, which metric it moves
before you touch it.

Two rules that kill most of your scope anxiety:

1. **AI at write time, deterministic at read time.** Pay a model once per
   artifact, cache the structured result forever, serve it from Postgres.
   Never call a model on a path a user waits for unless the result is new.
2. **Cache warming rides on content processing.** Every book you ingest
   enqueues enrichment for its vocabulary. Your library builds your lexicon.

---

## P0 — Nothing matters until these are done

| # | Task | Effort | Notes |
|---|------|--------|-------|
| 0.1 | Landing page | M | Hero GIF of parallel reading, one-line proposition, who it's for, three feature blocks, "try without an account" CTA. Currently your login screen is your landing page and it converts nobody. |
| 0.2 | Public no-signup Discover demo | M | Stranger pastes a German sentence, gets the full word sheet. Saving requires an account. This is your conversion mechanic. |
| 0.3 | Product analytics (PostHog) | S | Events: signup, first lookup, first save, first completed review session, D1/D7/D30 return, book opened, book progress %, share link created/opened, subscribe, cancel. Capture platform (web/iOS/Android) on every event. |
| 0.4 | External-cost ledger | M | Table: user_id, feature, provider, model, prompt_version, input_tokens, output_tokens, computed_cost_usd, created_at. Daily rollup per user. Impossible to retrofit, trivial now. |
| 0.5 | Terms + Privacy that are not placeholders | S | You process payments, chat, analytics, personal data. Current placeholders are a real liability. |
| 0.6 | Align pricing page with what the backend enforces | S | Remove or implement every promised limit. Today the page promises five things the backend does not model. |
| 0.7 | Purge leaked credentials from `almonium-book-processor` | S | Untracked backup/SQL files with live-looking tokens. Move out, rotate, add to `.gitignore`. |
| 0.8 | Verify Backblaze B2 restore path | S | Backups you have never restored are not backups. Do one full restore drill into a scratch DB. |

---

## P1 — The core loop, end to end

The loop: encounter → understand → save → review → varied retrieval →
diagnose → re-encounter.

### Domain model

| # | Task | Effort | Notes |
|---|------|--------|-------|
| 1.1 | Introduce `LearningItem` alongside `Card` | L | Fields: language, written form, normalized form, lemma, item type (word/phrase/chunk/template), POS, selected sense, learning intent, source contexts, memory state. `Card` becomes a presentation, not the domain object. |
| 1.2 | `Sense` as a first-class entity | M | One spelling, many senses, different CEFR levels. Do not force `spend`(n) and `spend`(v) into one row. |
| 1.3 | `Encounter` table | M | item_id, source type (book/extension/search/story/game), source ref, context sentence, timestamp. Powers word history and re-encounter features. |
| 1.4 | `ReviewEvent` append-only table, partitioned by month | M | A power user generates ~70k rows/year. Partition from day one; retrofitting is painful. |
| 1.5 | `ConfusionEdge` table | S | (item_a, item_b, type, evidence_count). Types: confused_with, false_friend, sounds_like, near_synonym. |
| 1.6 | Paraphrase pool per sense | M | Store 4–6 wordings of each definition/translation at creation time (one AI call, cached). Solves shape-memorization. |

### Scheduling and review

| # | Task | Effort | Notes |
|---|------|--------|-------|
| 1.7 | Move scheduling to backend, adopt FSRS | L | Kill device-local intervals. Web and mobile must agree. Use an existing FSRS implementation; do not invent intervals. |
| 1.8 | Rotate prompt form every review | M | Direction, cloze, typed production, audio-first, definition vs translation, paraphrase from the pool. Never repeat the same surface form twice in a row. |
| 1.9 | Fuzzy/semantic answer acceptance | M | Accept a correct answer worded differently. Exact-string grading is what teaches answer shapes. |
| 1.10 | Confusion-aware wrong-answer feedback | M | "That answer means **B**. Here, **A** means **X**." Deterministic match against the user's own items first. Your single most distinctive shippable feature. |
| 1.11 | Progressive hints with weighted scoring | M | POS → length → first letter → morphology → context → answer. Hinted recall scores lower than unaided. Vary the hint order. |
| 1.12 | Leech handling | S | After N failures offer: suspend, discard, change intent, split, enrich context, contrast with confusable. Never make the user press "remembered" to escape a card. |
| 1.13 | Filter session by retention stage (power-user option) | S | Default session stays auto-composed. |
| 1.14 | "Dessert": short generated text containing failed items | M | End of session. 8–20 targets max, 400–800 words, other vocabulary constrained to known range. |

### Capture

| # | Task | Effort | Notes |
|---|------|--------|-------|
| 1.15 | One-tap save with sensible defaults | M | Ordinary capture must never feel like filling a form. Editing stays available. |
| 1.16 | Reader word/phrase selection opens the same Discover sheet | M | Without losing reading position. |
| 1.17 | Learning intent selector (optional, on save) | S | understand / produce / pronounce / disambiguate / chunk. Drives which exercises run. |
| 1.18 | Save always captures the source sentence | S | Provenance is cheap now and expensive to backfill. |

### Discover data pipeline

| # | Task | Effort | Notes |
|---|------|--------|-------|
| 1.19 | LLM-generated structured lexical entry, write-through cached | L | Strict JSON schema: senses, POS, translations, collocations, examples, register, morphology. Store model + prompt version. First lookup pays; every later lookup is a DB read. |
| 1.20 | Warm the top 8–10k lemmas for DE and EN offline | M | Zipf means this covers most lookups. Batch API, 50% discount. Budget: single-digit dollars. |
| 1.21 | Enqueue enrichment from book ingestion | M | Processing a book pre-warms its vocabulary. Readers never wait. |
| 1.22 | User correction flow on lexical entries | M | Corrections become gold data. This is how your DB gets better than the model that seeded it. |
| 1.23 | `wordfreq` integration for frequency | S | 40+ languages, offline, Zipf scale. Keep your 0–100 presentation, drop the plan to build the corpus yourself. |
| 1.24 | Frequency provenance fields | S | surface form vs lemma vs family, corpus, domain, version. Product honesty. |

### NLP service

| # | Task | Effort | Notes |
|---|------|--------|-------|
| 1.25 | Extract NLP into a Python FastAPI service | L | Kills the 500MB Stanford bundle in the JVM. spaCy for DE/EN lemmatization, POS, morphology; `simplemma` for broad-but-shallow coverage. Consolidates with `almonium-book-processor`. |
| 1.26 | Phrase-template matcher | L | AI builds the template once (slots + lemmatized skeleton). Runtime match is lemmatize + wildcard, fully deterministic. Handles "X has got nothing on Y" across tenses and substitutions. |

---

## P2 — Acquisition and content

| # | Task | Effort | Notes |
|---|------|--------|-------|
| 2.1 | Server-rendered public parallel-text pages | L | An Angular SPA will not rank. Static-generate or SSR these. One page per book, one per chapter. Full text, no login wall. This is your only acquisition channel that scales without you. |
| 2.2 | Sitemap, canonical URLs, schema.org Book markup | S | |
| 2.3 | Auto-generated “50 useful words from *Book*” pages | M | The Python processor publishes a versioned artifact containing ranked lemmas, counts, chapter dispersion, `wordfreq` provenance, and source occurrences. The backend stores/serves the projection and owns the SSR SEO route; it must not independently tokenize the book or recalculate frequency. |
| 2.4 | Shareable/forkable word packs with public preview | M | Recipient previews without an account, imports selected items. Acquisition loop that needs no social graph. |
| 2.5 | Study-buddy pairing on top of existing chat | M | Two people learning the same language, sharing packs, seeing each other's progress. Highest-retention social configuration and it needs density of two, not thousands. |
| 2.6 | Book pipeline: switch source to Standard Ebooks where available | M | Consistently structured, modern typography, US public domain. Far less per-book special-casing than raw Gutenberg HTML. |
| 2.7 | Per-book YAML recipe files | M | Keep the pipeline generic; put each book's quirks in data, not code. |
| 2.8 | Pipeline QA gates | M | Chapter count, paragraph count, orphaned markup, language detection, alignment confidence. Fail loudly rather than publishing quietly-broken books. |
| 2.9 | Canonical alignment groups (N editions, not N²) | L | Each edition aligns once to a canonical sequence; the client composes any pair. |
| 2.10 | Sentence alignment via embeddings + dynamic programming | L | LaBSE or similar, then DP. LLM only resolves low-confidence spans. Cuts alignment cost by an order of magnitude. |
| 2.11 | AI translation of PD originals into modern DE/UA | M | Solves the scarcity of public-domain modern translations. Label honestly as machine-assisted. |
| 2.12 | Push notifications for due reviews | M | The single biggest retention lever in SRS apps and the thing Readlang cannot do without native apps. |
| 2.13 | Extension: highlight learning items on any page | M | Turns the web into a re-encounter engine. Your "Hunt" concept, minimum version. |
| 2.14 | Localized (PPP) price overrides in Paddle | S | UA/CEE at 40–60% of EU/US. |
| 2.15 | Fair-use limits expressed in user-visible units | M | Lookups, imports, story generations. Not tokens. |
| 2.16 | Credit-gated custom book import | L | The only genuinely expensive per-user feature. Client-side EPUB parsing (epub.js), user rights attestation, private-only, no sharing, takedown process. |

### Book-derived artifact boundary

A normalized original book is independently readable and publishable. It does
not wait for translations, alignment, summaries, quizzes, or lexical
enrichment. Those arrive asynchronously as separately versioned artifacts or
derived editions.

The Python book processor owns book-wide tokenization, lemmatization,
`wordfreq` lookup, source-text QA, difficulty estimation, and generation. This
backend owns authorization, the reader-facing projection, public API, and SSR
SEO pages. Integration is an explicit HTTP or messaging contract carrying
artifact kind, schema version, normalized-content input hash, processor/model
version, prompt version where applicable, and structured payload. Do not send
raw Python model objects across this boundary and do not implement a second
lexical pipeline in Java.

The first published artifact is `useful_words`: up to 50 recurring,
learner-useful, moderately uncommon lemmas with evidence from the book. The
literal rarest strings are intentionally excluded because they are dominated
by names, OCR/import defects, obsolete one-offs, and poor learning targets.

---

## P3 — Later

- Optional yes/no vocabulary calibration test (banded word list + pseudowords, no runtime AI)
- Continuous level estimation from lookup behaviour
- Phrase-level coloured alignment, generated only for demanded pairs
- Pre-reading difficulty analysis (known / learning / useful-unknown / too rare)
- Word history page ("first seen in *Frankenstein* ch. 3, looked up 3×, confused with X")
- Higher-or-Lower frequency game
- Heatmaps, meaningful streaks, annual recap
- Stream Chat attachments carrying learning items
- Free-recall warm-up ("write any German words you remember")
- Turn off fluent-language support as an item matures

---

## Lab — only with a stated metric

- Toucan-style in-page word substitution (great demo, weak pedagogy: it drills L1 syntax)
- Rare-word poker / "whose cards are stronger"
- Graffiti handwriting space
- Homophone exploration
- PvP anything
- Any additional dictionary API
- Elasticsearch, Kafka, or managed search infrastructure

---

## Decisions made — stop relitigating these

| Question | Decision |
|----------|----------|
| Angular → React rewrite | **No.** Extract shared TypeScript API client + domain types into a package instead (≈1 week). Revisit only if Angular becomes a measured velocity blocker. |
| Per-language database tables | **No.** 2–5M lexeme rows and 300k learning items are unremarkable Postgres. Partition `review_events` by month; that is the only partitioning you need for years. |
| Prefill the whole lexicon in advance | **No.** Lazy write-through cache + a bounded 10k-lemma warm-up + book-driven enrichment. |
| Oxford / WordsAPI / Twinword subscriptions | **No.** An LLM generates a richer structured entry for a fraction of a cent, and you own the result. |
| Frontier model at runtime | **No.** Nano/flash tier for cached structured generation; mid-tier for translation, adaptation, and hard alignment cases. Frontier only for designing and evaluating prompts. |
| Launch at $5 and raise later | **No.** Launch at target price with a generous free tier; grandfather early users as founding members. |
| Chat as a marketed feature | **No.** Keep it, use it as the transport for shared learning items and buddy pairs. Nobody switches messengers for a language app. |

---

## Rough unit economics (July 2026 prices, verify before committing)

| Workload | Model tier | Approx. cost |
|----------|-----------|--------------|
| One structured lexical entry (~800 output tokens) | nano/flash ($0.10–0.40/M out) | ~$0.0003 |
| Warming 10k lemmas × 2 languages | nano/flash + batch discount | ~$5 |
| Translating a 100k-word novel into one language | mid-tier ($0.60–3/M out) | ~$0.50–5 |
| A 50-book × 4-language parallel library | mid-tier + batch | ~$100–600 one-time |
| One "dessert" story per review session | nano/flash | ~$0.0004 |
| One user's monthly runtime AI cost, cache warm | — | cents |
| One custom EPUB import by a user | mid-tier | ~$1–5 |

The conclusion that should change your planning: your content ambition is not
token-constrained. It is constrained by QA time and by whether anyone reads
the books.
