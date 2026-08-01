# Almonium: context brief

**Purpose:** paste this at the start of any new conversation about Almonium.
It carries the vision, the decisions already made, and the open questions, so
you never have to re-explain the project or re-upload the whole history.

**Last updated:** 2026-07-28
**Companion docs:** `PRODUCT_VISION.md` (full detail), `BACKLOG.md`,
`COMPETITIVE_EDGE.md`, `LANDING_PAGE.md`, `EBOOK_PIPELINE.md`, `FREEMIUM.md`

---

## 1. What Almonium is

A language-learning system covering the full acquisition lifecycle of a word,
phrase, or chunk: encounter it, understand it in context, decide whether it is
worth learning, save it with provenance, retrieve it through varied practice,
diagnose exactly how the knowledge is failing, and rehearse it until it becomes
usable language.

Reading, Discover, review, stories, sharing, and games are surfaces around one
personal knowledge and memory system. They are not separate products.

**Working proposition:** *Read anything. Keep every word.*

## 2. Who built it and why

Solo founder, SWE, based in Kyiv. Took his own English to C2 self-taught,
now learning German, speaks four languages. Built Almonium because a dozen
disconnected tools never joined up. Started as a NaUKMA BSc project in 2022;
the core thesis has been stable since.

Founder-problem fit is genuine. Marketing and distribution are the weak side,
and the founder knows it.

## 3. Target user

Self-directed intermediate-to-advanced learners who already consume real
content and already collect vocabulary across several tools. Often
multilingual. Not beginners, not casual gamified learners.

**Wedge under consideration:** Ukrainian and Russian speakers learning German.
Large, motivated, deadline-driven (integration courses, B1 for residency,
professional recognition), and completely underserved by tools built for
English speakers. The founder has native access to those communities.

## 4. Current state

Real, substantial, and not yet closing its own loop.

Built: Spring/Java backend, Angular web, React Native mobile (in progress),
Postgres (self-hosted), Stripe, Stream Chat, Firebase, custom auth, a Chrome
extension, a Python book processor run manually, parallel reader, cards with
device-local review, social graph, four game shells.

Missing: the reader-to-card connection, backend-synced scheduling, a landing
page, honest terms and pricing, and any users who are not friends.

Infrastructure: Oracle pay-as-you-go, self-hosted DB, Backblaze B2 backups,
Zepto Mail. Near-zero monthly cost.

Design language: paper-warm cream, literary serif, plum accent, round geometry,
engraved illustration. Bookish and calm rather than streak-driven. This is a
real asset.

## 5. What is genuinely differentiated

Ranked by defensibility, with honesty about what is not novel.

1. **Confusion-aware feedback.** Answer A with the meaning of B, and Almonium
   names B and contrasts them. No competitor found doing this. Deterministic,
   cheap, shippable in a week.
2. **Receptive vs productive intents.** "I understand it but cannot say it" as
   a first-class state with its own exercises. Everyone else has one number.
3. **Level-adapted real books.** The same novel at B1, B2, C1. Beelinguapp
   grades short original content; nobody adapts real literature.
4. **Multi-fluent-language triangulation.** Meaning shown across all languages
   the learner speaks, because Ukrainian sometimes catches what English
   flattens. Novel, narrow audience, near-zero cost.
5. **Paraphrase rotation.** Reword the answer each review so the learner stops
   memorising the shape of the card.
6. **Encounter history.** Where a word was first met, how often, what it was
   confused with.

**Not differentiated, do not claim otherwise:** parallel reading (Beelinguapp
has 4M+ downloads and it is their whole method), click-to-translate reading
(Readlang, LingQ, Migaku), frequency ordering (Migaku, Clozemaster), deck
sharing (Anki, Quizlet), browser extensions (Readlang, Language Reactor,
Toucan), diglot-weave word blending (Prismatext).

## 6. Competitive landscape

| Product | Owns the verb | Notes |
|---|---|---|
| Readlang | read | Solo founder, unfunded since 2012, 700k+ registered learners, $6/mo and $15/mo tiers. **No native apps.** The proof a solo operator can sustain this niche. |
| LingQ | input | ~3.5M members, ~$3.1M revenue, ~27 staff after 19 years. The ceiling of the category. |
| Beelinguapp | listen | 4M+ downloads, parallel text plus narrated audiobooks. Explicitly anti-flashcard. |
| Migaku | mine | Netflix/YouTube sentence mining, frequency lists, AI explanations. |
| Clozemaster | drill | $8/mo, cloze in context, frequency-ordered. |
| Prismatext | blend | Diglot weave: real novels with foreign words woven in progressively. |
| Almonium | *(unclaimed)* | Currently positioned as a "multitool", which is the worst available position. |

## 7. Decisions already made — do not relitigate

| Question | Decision |
|---|---|
| Angular → React rewrite | **No.** Extract shared TS API client and domain types instead. |
| Per-language DB tables | **No.** 2–5M lexeme rows is ordinary Postgres. Partition `review_events` by month only. |
| Prefill the whole lexicon | **No.** Write-through cache + 10k-lemma warm-up + book-driven enrichment. |
| Oxford / WordsAPI / Twinword | **No.** An LLM produces a richer structured entry for a fraction of a cent and you own it. |
| Frontier model at runtime | **No.** Nano/flash for cached generation, mid-tier for translation and adaptation, frontier only for designing prompts. |
| Launch at $5, raise later | **No.** Launch at target price with a generous free tier; grandfather founding members. |
| Chat as a marketed feature | **No.** Keep it as transport for shared learning items and study-buddy pairs. Never on the landing page. |
| DeepL for book translation | **No.** ~5–30× more expensive than an LLM per novel and worse at literary register. |
| AI-generated book covers | **No.** Typographic covers. This audience is hostile to AI art and it undercuts the literary positioning. |
| Games, PvP, streaks | **Parked** until the core loop retains anyone. |

**Governing engineering principle:** pay a model at write time, serve
deterministically at read time. Cache every reusable artifact with its model
and prompt version.

## 8. The core domain model

`LearningItem` replaces `Card` as the domain object; a card becomes one
presentation of an item.

```
LearningItem
  language, written form, normalized form, lemma
  type: word | phrase | chunk | template
  part of speech, selected sense
  learning intent: understand | produce | pronounce | disambiguate | chunk
  translations (multiple fluent languages)
  source contexts + encounter history
  frequency evidence with provenance
  confusion edges
  memory state (FSRS, server-side)
```

Supporting tables: `Sense`, `Encounter`, `ReviewEvent` (append-only,
month-partitioned), `ConfusionEdge`.

## 9. Language strategy

**Target languages:** DE first (founder is learning it), then EN, then FR/ES.
**Bridge/fluent languages:** UA, RU, PL, EN.

Capability tiers: Basic (manual cards, any language) → Supported (translation,
TTS, frequency) → Enhanced (morphology, collocations, sense-aware examples) →
Almonium-grade (evaluated end to end). Only DE and EN reach Enhanced at launch.

## 10. Economics

- Structured lexical entry: ~$0.0003, cached forever
- Novel translated into one language by LLM: ~$0.50–5
- Level-adapted edition: ~$1–5
- Runtime AI per active user with warm cache: cents per month
- **TTS: the only cost that scales linearly with users.** Gate it.
- Real bottleneck: content QA hours and support, not tokens

**Target:** ~$3k/month. At $12–15/month that is roughly 250 subscribers, not
600. Price accordingly.

## 11. Open questions

- Does anyone outside the founder's circle complete the loop three times a week?
- Do the public parallel-text pages rank and convert?
- Is the UA→DE wedge real, or is the founder over-indexing on his own case?
- Will level-adapted editions read well enough to be a selling point?
- What actually creates willingness to pay: books, intelligence, or sync?

## 12. Known failure mode

The founder builds whatever feature is interesting when he reaches it, and the
digressions consistently land on invisible work with no audience (custom auth,
avatar pickers, settings, chat) at exactly the moments when the next honest
step was showing the product to a stranger. Four years of stable vision, zero
external users.

The current constraint is not capability or capital. It is that progress has
meant "features exist" rather than "strangers came back."
