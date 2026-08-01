# Almonium: competitive edge, feature by feature

**Date:** 2026-07-28
**Competitor set:** Readlang, LingQ, Beelinguapp, Migaku, Clozemaster, Language
Reactor, Anki, Lute, Doppeltext / farkastranslations
**Scoring:** Uniqueness and Value are 1–5. Cost is marginal cost to serve, not
build cost.

---

## The scoreboard

| Feature | Who already does it | Unique | Value | Marginal cost | Verdict |
|---|---|---|---|---|---|
| **Confusion-aware wrong answer** ("that means B") | Nobody I could find | 5 | 4 | ~0 | Highest edge per hour of work in the whole product |
| **Multi-fluent-language meaning triangulation** | Nobody | 5 | 3 general / 5 polyglot | ~0, cached | Real novelty, narrow audience, cheap to keep |
| **AI level-adapted editions** (read *Frankenstein* at B1, step up to B2) | Beelinguapp grades original short content; nobody adapts real books | 4 | 5 | $1–5 per book per level | Possibly your single strongest content play |
| **Receptive vs productive learning intents** | Anki users hack it manually; no app models it | 4 | 5 | ~0 | Deepest idea you have, hardest to put on a landing page |
| **Paraphrase rotation** (anti shape-memorisation) | Nobody | 4 | 4 | ~0, precomputed | Cheap, demoable, fixes a pain every SRS user feels |
| **Dessert story from the words you just failed** | Nobody | 4 | 4 | fractions of a cent | Best session-ending hook available to you |
| **Phrase-level coloured alignment** | Nobody at scale | 4 | 3 | moderate, per pair, one-time | Your best screenshot |
| **Word encounter history** ("first seen in ch. 3, confused with X") | Nobody | 4 | 3 | ~0 | Emotional differentiator, nearly free |
| **Progressive graded hints** | Some SRS apps have a hint field; none grade the assistance | 3 | 3 | 0 | Minor but distinctive |
| **High-density generated rehearsal texts** | Some AI lesson generators; density targeting is rare | 3 | 4 | cents | Needs validation before you invest |
| **Frequency as learning priority (0–100)** | Migaku ships frequency lists, LingQ counts known words, Clozemaster orders by frequency | 2 | 3 | ~0 with `wordfreq` | Presentation edge only. Do not claim invention |
| **Paragraph-aligned parallel reading** | **Beelinguapp**, LingQ dual text, Doppeltext, farkastranslations, Lute | 2 | 4 | one-time, cheap | Demo asset, not a moat. See below |
| **Sharing cards and packs with friends** | Anki shared decks, Quizlet, Memrise community | 2 | 4 | ~0 | Valuable as an acquisition loop, weak as a novelty claim |
| **Custom EPUB / text import** | Readlang, LingQ, Migaku | 2 | 5 | $1–5 per book | Table stakes. Credit-gate it |
| **Native mobile apps** | LingQ, Beelinguapp, Clozemaster have them. **Readlang does not** | 2 | 5 | dev time | Genuine gap against Readlang specifically |
| **Click-to-translate in reader** | Readlang, LingQ, Migaku, Language Reactor | 1 | 5 | ~0 cached | Table stakes. Must be excellent, cannot be a selling point |
| **Browser extension** | Readlang, Language Reactor, Migaku, Toucan | 1 | 4 | ~0 | Table stakes for this niche |
| **Games / PvP** | Quizlet Live, Clozemaster, Duolingo | 1 | 2 | dev time | Park it |
| **Chat** | WhatsApp, Telegram, Discord | 0 | 1 | hosting | Keep, never market |

---

## The uncomfortable finding

**Beelinguapp** is a parallel-text app with over four million downloads, sixty
thousand store ratings, and parallel text as its entire stated method. It ships
native-narrated audiobooks with karaoke-style synchronised scrolling across
roughly fourteen to twenty-three languages.

Your belief that only hobby projects did parallel reading is wrong, and any
marketing that claims parallel reading as your unique idea will be corrected by
the first commenter on Reddit.

What Beelinguapp does **not** do, and this is where you survive:

- No vocabulary lifecycle. It explicitly markets "no memorisation and no
  flashcards needed."
- No spaced repetition, no memory state, no confusion tracking.
- Short stories, news, and fairy tales rather than full novels.
- Level-graded original content rather than level-adapted real literature.
- No import of your own books.

So the honest framing: Beelinguapp is parallel reading **as the destination**.
Yours is parallel reading **as the entrance to a memory system**. Say that.
Do not say you invented the format.

---

## Where the real gaps are

Three claims survive contact with the competitor set:

1. **Nothing tells you which word you actually confused it with.** Every app in
   this set marks you wrong and moves on. You can ship the fix in a week.
2. **Nothing lets you read a real book at your level and step up.** Beelinguapp
   grades short original content. LingQ and Readlang give you whatever the text
   already is. Adapting a public-domain novel to B1, B2, and C1, then offering
   the ladder, costs a few dollars per book and nobody has bothered.
3. **Nothing models "I understand it but cannot say it."** Every competitor
   treats knowledge as one number.

Claims 1 and 3 are your intelligence layer. Claim 2 is your content layer, and
it is the one an ordinary learner will understand in one sentence.

---

## Positioning: everyone owns a verb except you

| Product | Self-description | Verb it owns |
|---|---|---|
| Readlang | Learn any language by reading what you love | **read** |
| LingQ | Input-based learning, known-word statistics, community | **input** |
| Beelinguapp | Master languages with bilingual audiobooks, parallel-text method | **listen** |
| Clozemaster | Mass exposure to useful vocabulary in context | **drill** |
| Migaku | Learn from real content: Netflix, YouTube, articles | **mine** |
| Almonium (today) | "Your language learning multitool" | none |

"Multitool" is the worst available position. It asks the visitor to do the
work of figuring out what you are for.

The verb nobody has claimed is what happens to a word **after** you meet it.
Not reading, not immersion, not drilling. The journey of one word from first
sight to first use.

Candidate lines:

- **Read anything. Keep every word.**
- Meet a word once. Use it for good.
- Almonium remembers every word you have ever met.

I would test the first. It names both surfaces, implies the loop, and takes
nothing that Readlang already owns.

---

## Margin, since you asked

Software margin is ~100% on everything here. The costs that actually vary:

| Cost | Type | Scale | Notes |
|---|---|---|---|
| Book processing and alignment | One-time per book-language | $0.50–5 | Amortised across every reader forever |
| Level-adapted editions | One-time per book-level | $1–5 | Same amortisation |
| Lexical entry enrichment | One-time per word, globally | ~$0.0003 | Zipf flattens this fast |
| Runtime AI per active user | Recurring | Cents per month | Once the cache is warm |
| **Text-to-speech** | Recurring, per user | **Real money** | Your only genuinely scaling cost |
| Custom EPUB import | Per user, per book | $1–5 | The one thing that must be credit-gated |
| Support and content QA | Your hours | The actual bottleneck | Not modelled by anyone, always underestimated |

Note what Readlang gates: audio storage hours, ten at $6 and two hundred at
$15. That is not arbitrary. Whole-text TTS is the expensive part of a reading
product, and their pricing page tells you they learned that the hard way. Copy
the lesson before you promise unlimited audio.

---

## What competitors make

Treat all of these as models rather than facts. None of these companies
publish revenue.

**Readlang.** Solo founder Steve Ridout, formerly a Duolingo engineer,
unfunded, running since 2012, based in Madrid. Over 700,000 learners have
registered. Tiers are $6/month or $48/year, and $15/month or $120/year. If
somewhere between two and six thousand of those registrations are active
subscribers at a blended $7, that puts him between roughly $170k and $500k a
year, run by one person. Even the bottom of that range is several times your
stated goal.

**LingQ.** Founded 2007, over 3.5 million registered members as of 2022, and
third-party trackers estimate around $3.1M annual revenue with roughly 27
employees. Note the ratio: fifteen years and a co-founder with a million
YouTube subscribers produced a company with two dozen staff, not a unicorn.
That is the ceiling of this category.

**Clozemaster.** $8/month, run by a small US LLC.

**Beelinguapp.** Four million-plus downloads and sixty thousand ratings, with a
subscription and ad-supported free tier. Downloads are not subscribers, and
app-store reviews mention aggressive trial prompts, which usually signals
paid acquisition and thin margins.

**The two conclusions that matter for you:**

1. Readlang proves one person can run a reading-plus-vocabulary product
   profitably for over a decade. Your target of a few thousand a month sits
   comfortably inside what a solo operator in this exact niche has achieved.
2. Readlang's premium tier charges $15/month and the headline difference from
   the $6 tier is which model answers your questions. If a better model
   justifies $15, your intelligence layer justifies at least that.

---

## What you actually do better

Ranked by how defensible each one is:

1. **You model the word, not the card.** Senses, chunks, intents, confusions,
   provenance. Everyone else has a row with a next-review date.
2. **You are building for multilingual learners.** Every competitor assumes one
   bridge language. You assume four.
3. **Your content can be levelled.** Nobody else can hand a learner the same
   novel at three difficulties.
4. **Native apps with push.** The single strongest retention lever, and
   Readlang cannot pull it.
5. **Design.** Your product looks like a book. Readlang looks like 2014, LingQ
   looks like a dashboard, Clozemaster looks like a form. Taste is not a moat,
   and it converts anyway.

## What they do better

1. **Language coverage.** Readlang claims 100+ languages, LingQ 11 fully
   supported, Beelinguapp 14–23. You have two you can do well.
2. **Content volume.** LingQ's library is enormous and community-fed.
3. **Distribution.** LingQ has Steve Kaufmann's million subscribers. That is
   worth more than every feature in your backlog combined.
4. **Time in market.** Fourteen years of SEO and word of mouth for Readlang.
5. **They have shipped.** Yours does not yet close the loop.

Point 3 is the whole game and you know it.
