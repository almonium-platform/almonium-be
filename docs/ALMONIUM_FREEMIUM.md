# Almonium freemium model

**Date:** 2026-07-28
**Principle:** free where cost is amortised, paid where cost scales per user.

---

## The rule that decides everything

Sort every feature by how its cost behaves:

| Cost shape | Example | Should be |
|---|---|---|
| One-time, shared by everyone forever | A processed book, a cached lexical entry | **Free** |
| Zero marginal | Your intelligence layer, confusion detection, paraphrase rotation | **Free at low volume, unlimited when paid** |
| Linear per user | TTS audio, custom book import, AI story generation | **Paid, metered** |
| Storage that grows forever | Uploaded books, generated audio | **Paid, capped** |

Your books cost the same whether ten people or ten thousand read them. Making
them free costs you nothing and buys you your only compounding acquisition
channel. Your TTS costs you every time somebody presses play.

Note what Readlang does, because they learned it the expensive way: unlimited
single-word translations on the free tier, but phrase translations and context
explanations capped at ten per day, and audio storage gated at ten hours on the
$6 tier versus two hundred on the $15 tier. Generous where cheap, tight where
it costs money.

---

## The tiers

### Free — "a real reading tool"

| Feature | Limit |
|---|---|
| Read any book, any language pair | **Unlimited** |
| Public parallel-text pages | **Unlimited, no account** |
| Word lookup in Discover | **Unlimited** (cached, costs you nothing) |
| Saved learning items | **100** |
| Review sessions | **Unlimited** |
| Confusion detection, paraphrase rotation, hints | **Included** |
| Target languages | **1** |
| Fluent/bridge languages | **1** |
| Level-adapted editions | Preview one chapter |
| AI dessert stories | **3 / week** |
| Audio | ✗ |
| Custom book import | ✗ |
| Cross-device sync | ✗ |
| Shared packs | Receive and import; cannot create |

### Premium — $12/month or $96/year

Everything above, unlimited, plus:

- Unlimited saved items
- Unlimited target and fluent languages
- All level-adapted editions
- Cross-device sync
- Unlimited dessert stories, fair use
- **10 hours** of book audio
- **3** custom book imports per month
- Create and share packs
- Encounter history and statistics

### Founding member — $8/month, first 100 subscribers, locked while subscribed

Same as Premium. Say the number publicly and count down. This is not a fake
countdown; it is a real, honest scarcity that converts the exact people you
want, and it lets you launch at the right price without punishing the people
who took a chance first. A founder who cancels moves to the current public
price if they return.

The $8 offer is a distinct Stripe recurring Price that grants the same
`PREMIUM` entitlement as the public $12 offer. The application reserves one of
the one hundred durable founding-member slots before opening checkout, then
confirms it from Stripe's paid webhook. This is a launch requirement; keep
those records permanently as the evidence for the promise, not disposable
launch data.

### Regional pricing

Ukraine, CEE, LATAM, SEA at 40–60% of the list price via Stripe's regional
pricing. Note that Ukrainians living in Germany bill in the EU band, so your
wedge audience splits across both.

---

## Where to draw the line, precisely

**The 100-item free cap is the load-bearing limit.** It is the number that
converts. Someone who has saved a hundred words from a book has already had the
experience and built something they do not want to lose. That is the moment to
ask for money, and it arrives on its own.

LingQ's most common free-tier complaint is that the saved-word cap bites before
users understand the system. Do not repeat that. A hundred words takes a
serious learner two or three sessions to reach, which is exactly right: long
enough to see value, short enough to matter.

**Never gate:** reading, word lookup, or the confusion feature. Reading is your
SEO engine. Lookup is cached and free to you. Confusion detection is the thing
people will tell their friends about, and it costs nothing to give away.

**Always gate:** audio, custom import, and anything that generates fresh tokens
per user per use.

---

## Your instinct about "borderline usable"

You said the free tier should be uncomfortable enough to convert but generous
enough to taste. That is right in spirit and slightly wrong in method.

Do not make the free tier *annoying*. Make it *finite*. There is a real
difference:

- **Annoying:** ads, artificial delays, three-second lockouts, nagging modals.
  These make people leave and say bad things about you.
- **Finite:** you have saved 100 words and the 101st needs Premium. Nothing is
  degraded. You simply reached the end of a real, generous allocation.

Finite converts better and costs you no goodwill. Every limit should feel like
a natural boundary rather than a punishment.

---

## Trial period: no

Not at first. Reasons:

1. A generous free tier already does the job of a trial, and does it
   indefinitely rather than for fourteen days.
2. Trials add Stripe webhook complexity, trial-abuse handling, and a churn
   spike at day fifteen that will make your early retention numbers unreadable.
3. With fewer than a few hundred users you need clean signal, not a funnel with
   an extra stage.

Revisit once you have a working paid funnel and want to test conversion lift.

---

## Enforcement

You need three things in the backend, and you need them before launch because
retrofitting them is miserable:

1. **A limits table**, not scattered `if premium` checks. One place that answers
   "may this user do X, and how many have they done this period."
2. **The cost ledger** from the backlog: per-user, per-feature, per-model token
   accounting with a daily rollup.
3. **A soft ceiling above the stated limit.** If Premium says "fair use", pick
   an actual number (say 5× the p95 user), alert yourself when someone crosses
   it, and handle it as a conversation rather than a silent cutoff.

The pricing page must describe only limits the backend actually enforces. Right
now it promises five things that do not exist, which is the one thing on this
list that can genuinely hurt you.

### Internal access and support operations

Internal and QA accounts use an audited access grant (`FREE`, `PREMIUM`, or
`UNLIMITED`) rather than a fake Stripe subscription. The grant overrides the
effective entitlement only; it never changes a customer's billing record.
Operator actions require an authenticated admin and a reason. For support
resets, use a quota-adjustment ledger so imported books remain part of the
user's history.

---

## What not to charge for

- **Skins, avatars, cosmetics.** Wrong signal entirely for a serious
  instrument.
- **Streak repairs.** Readlang sells these. It is a small revenue line and a
  large statement about what your product values. You have positioned against
  streak-driven learning; do not sell absolution for breaking one.
- **Removing artificial friction.** Never introduce inconvenience in order to
  sell its removal.
