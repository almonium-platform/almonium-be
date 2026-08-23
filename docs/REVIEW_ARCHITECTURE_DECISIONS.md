# Review architecture decisions

**Status:** accepted for the first server-synchronised review release

**Decision date:** 2026-08-24

**Source brief:** `../almonium-fe/docs/Almonium Review.dc.html`

This log records the decisions made while turning the Review design brief into
an executable cross-client contract. It is intentionally more specific than
`ALMONIUM_CONTEXT.md`; amend this document when the review invariants change.

## ADR-001: LearningItem is the aggregate; Card remains a compatibility word

A card is a prompt presentation, not the durable domain object. The Java
entity is therefore `LearningItem`, with written and normalized forms, lemma,
type, sense, source context, multiple learning intents, and memory state.

The physical `card` table and `/cards` routes remain during an expand/contract
migration. Angular uses `/review`; the existing web and mobile card clients can
continue using `/cards`. Renaming the table or removing the compatibility API
requires a later release after every deployed slot and client has moved.

The old schema also constrained `card.owner_id` to `user_core` while JPA treated
it as a `Learner`. The migration adds the correct `learner_id` relationship. A
database trigger derives `learner_id` for writes from old slots and preserves
`owner_id` for old readers, making the ownership correction safe during a
rolling deployment.

## ADR-002: Scheduling is server-owned FSRS state

The backend owns due times and serialised FSRS card state. It uses the official
`io.github.open-spaced-repetition:fsrs:1.0.0` Java implementation at 90% default
retention. Clients never calculate or submit intervals. The scheduler is kept
behind `ReviewService` so a library upgrade or future per-user parameter
optimizer does not alter the HTTP contract.

Fuzzing is disabled initially to keep event replay and tests deterministic.
Changing that is a scheduling-policy decision, not a UI preference.

## ADR-003: Review evidence is append-only and time-partitioned

Every checked or revealed answer creates a `review_event` containing the
prompt/intent, submitted and expected answers, hint ledger, confusion target,
and FSRS state/due values before and after scheduling. The table is range
partitioned on `reviewed_at`, with a default partition so writes cannot fail
when an operations-created monthly partition is late.

“I just mistyped” appends a `TYPO_CORRECTION` event referring to the original
event and increments the confusion edge's resolved count. It never rewrites
history. This preserves the evidence needed to tune grading and FSRS later.

## ADR-004: A session is a persisted, bounded unit of ten

Starting review snapshots at most ten due, non-leech items into an owned
`ReviewSession`. Answer endpoints verify both session ownership and membership,
and reject a second answer for the same session item. Leaving does not delete
or reschedule unanswered work. The queue and session record can therefore be
continued safely across devices without turning the whole backlog into a task.

## ADR-005: Grading is deterministic before semantic infrastructure exists

The first release normalizes Unicode, case, whitespace, and punctuation. Exact
matches pass; a one-edit tolerance is allowed for answers of at least five
characters. Before that tolerance is accepted, the answer is checked against
the learner's other items. An exact match to another known item is a directional
confusion, not a typo—this precedence is essential for pairs such as `Ausgabe`
and `Aufgabe`.

An LLM is not called per review. When semantic grading is added, it must be an
adapter that returns evidence into this same outcome model; it must not own the
schedule or erase the deterministic confusion lookup.

## ADR-006: Prompts are stored artifacts and hints are priced evidence

`ReviewPrompt` stores prompt variants by learning item and intent. Existing
items are lazily seeded from stored translations/senses, while newly supplied
domain content can populate the same pool at write time. Rotation uses review
count, so the client cannot train a single device-local shape.

The API returns the disclosed cost and content for gender, letters, and source
context hints. Opening hints is sent with the answer and changes a successful
rating from `GOOD` to `HARD`; revealing the answer is an `AGAIN`. The ledger is
part of the immutable event.

## ADR-007: Four same-shape failures remove an item from the queue

Four consecutive failures for one prompt type mark the item as a leech. It is
excluded from due sessions. “Meet it another way” adds or selects the opposite
receptive/productive intent, creates that prompt pool if needed, resets the
same-shape failure counter, and returns it due now. A successful answer also
clears the failure counter.

## ADR-008: Completion copy must not claim generation that did not happen

The result endpoint currently returns saved example sentences for missed and
confused items. Angular labels this a “short re-encounter.” It does not call it
a story “written this minute.” A generated dessert needs a separately reviewed
write-time generation adapter, quality checks, and free-tier metering; until
those exist, making the stronger claim would be product dishonesty.

## HTTP contract

- `GET /review/summary/{language}` — due composition and leeches.
- `POST /review/sessions/{language}` — snapshot up to ten due items.
- `POST /review/sessions/{sessionId}/items/{itemId}/answer` — grade, record,
  diagnose confusion, and schedule.
- `GET /review/sessions/{sessionId}/result` — immutable session record and
  available re-encounter sentences.
- `POST /review/events/{eventId}/mistype` — append a correction signal.
- `POST /review/leeches/{itemId}/reencounter` — change prompt shape and return
  the owned item to the queue.
