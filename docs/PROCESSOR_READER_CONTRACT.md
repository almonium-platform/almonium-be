# Processor-backed edition reader — 2026-09-15

The product now selects companions by edition, not just language. A work can
contain an original, same-language adaptations and translations of their common
source. Publication still creates one Book per edition; it does not create or
approve processor content. Adaptation publication no longer settles translation
orders/jobs.

## Implemented contract

- `GET /public/books/{slug}`: `languageVariants` includes sibling/descendant
  editions of the same work, with `editionSlug`, `language`, `editionType`,
  `cefrLevel`, `sourceEditionSlug`. Withdrawn books stay excluded. This is a list
  of available editions, **not** a guarantee that every pair is aligned.
- `GET /public/books/{slug}/parallel-edition/{companionSlug}` selects exactly the
  requested edition, including same-language pairs. Reject self/different work;
  the processor validates actual correspondence. An unavailable processor pair
  becomes an explicit not-found response.
- Existing language-addressed/native endpoints remain available; Firebase
  bearer and browser-cookie boundaries are unchanged. Expo's edition picker and
  sentence-highlighting UI are a follow-up, not delivered here.
- Processor inherited-pair payload schema 2 carries stable block/group IDs,
  both texts and sentence spans, a pair revision, and optional sentence groups.
  Sentence groups contain zero-based `primary`/`secondary` index arrays and a
  `certain` flag. Many-to-many groups are intentional. No positional zipping.
- The product's HTML adapter emits `data-side="primary|secondary"` independently
  of `lang`, and preserves confirmed groups with `data-alignment`. Unicode
  code-point offsets are converted to JVM string offsets before escaping text.
  Uncertain/unmatched/missing alignment falls back to ordinary paragraph text.

## Verification and next work

Focused service tests and a real PostgreSQL repository test cover exact edition
selection, siblings, same-language levels, withdrawal, Unicode, escaping and
many-to-one rendering. The Angular browser test exercises the new endpoint and
click/keyboard highlighting, including a slow base-content response.

Next valuable slices:

1. Publish deliberately approved editions locally/staging and exercise the
   complete authenticated hand-off → catalogue → reader. Do not label the current
   B2-target Frankenstein draft verified B2: editorial level is still unset.
2. Carry processor chapter descriptions, computed difficulty/coverage and useful
   vocabulary through an explicit chapter DTO; build chapter navigation/SEO from
   those entities instead of scraping headings from HTML.
3. Add pair-eligibility discovery, source/revision and generation/review metadata
   to the product DTO, so unavailable pairs are not offered as reading choices.
   Keep editorial level separate from computed estimates and define override
   ownership before republishing richer metadata.
4. Expand the sentence preview to a chapter, then refine selected groups into
   clause spans; add Expo consumption. Do not generate every language/level pair.

Full Maven verification currently fails in pre-existing Firebase web-slice tests
because `LastSeenRecorder` is missing from their test context (14 context errors;
464 tests attempted, zero assertion failures, three skipped). Those authentication
files were not changed by this iteration. This is not a green full-build claim.
